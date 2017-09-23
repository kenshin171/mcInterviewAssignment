package com.kenshin.mcassigment.mastercardinterviewassignment.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.evernote.android.state.State;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.kenshin.mcassigment.mastercardinterviewassignment.App;
import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.adapter.CurrencyListAdapter;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.ActivitySearchBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.SearchActivityViewModel;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.providerFactory.SearchActvityVMproviderFactory;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.Disposable;

public class SearchActivity extends BaseActivity implements ActionMode.Callback, CurrencyListAdapter.OnItemClickListener {

    private static final String TAG = "SearchActivity";

    public static final String CURRENCY_RESULT = "CURRENCY_RESULT";

    private ActivitySearchBinding binding;
    private SearchActivityViewModel viewModel;
    private CurrencyListAdapter adapter;
    private Disposable searchDisposable;

    @State
    boolean searchMode;

    @State
    String searchText;

    @State
    String selected;

    @Inject
    @Named("SearchActivityVMFactory")
    SearchActvityVMproviderFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) binding.incToolbar.llToolbar.getLayoutParams();
        lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        String want_have;

        if(getIntent().hasExtra(MainActivity.SELECTED)) {
            selected = getIntent().getStringExtra(MainActivity.SELECTED);
        }

        if(getIntent().hasExtra(MainActivity.WANT_HAVE)) {
            if(getIntent().getIntExtra(MainActivity.WANT_HAVE, -1) == MainActivity.I_HAVE_CURRENCY)
                want_have = getString(R.string.have);
            else
                want_have = getString(R.string.want);
        }
        else {
            want_have = "";
        }

        setSupportActionBar(binding.incToolbar.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setActionBarTitle(getString(R.string.search_activity_title, want_have), binding.incToolbar.toolbar);
        }

        viewModel = ViewModelProviders.of(this, factory).get(SearchActivityViewModel.class);

        binding.rvCurrencyList.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setInitialPrefetchItemCount(2);
        binding.rvCurrencyList.setLayoutManager(lm);
        binding.rvCurrencyList.setNestedScrollingEnabled(true);

        adapter = new CurrencyListAdapter(new ArrayList<>(0));
        adapter.setOnItemClickListener(this);
        binding.rvCurrencyList.setAdapter(adapter);

        RxSwipeRefreshLayout.refreshes(binding.srlSwipeContainer)
                .compose(provider.bindToLifecycle())
                .subscribe(Void -> viewModel.searchDB(searchText));
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getCurrencyList().observe(this, newList -> {

            binding.srlSwipeContainer.setRefreshing(false);

            if(!searchMode) {
                binding.clpbLoading.setVisibility(null != newList ? View.GONE : View.VISIBLE);
                binding.rvCurrencyList.setVisibility(View.VISIBLE);
            }
            else {
                binding.clpbLoading.setVisibility(View.GONE);
                binding.tvNoSearchResult.setVisibility(null == newList || newList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            adapter.updateList(newList);
        });

        viewModel.getErrorNotifier().observe(this, error -> {

            if(TextUtils.isEmpty(error)) {
                binding.tvErrorOccurred.setVisibility(View.GONE);
                binding.rvCurrencyList.setVisibility(View.VISIBLE);
            }
            else {
                binding.rvCurrencyList.setVisibility(View.GONE);
                binding.tvNoSearchResult.setVisibility(View.GONE);
                binding.clpbLoading.setVisibility(View.GONE);
                binding.tvErrorOccurred.setVisibility(View.VISIBLE);
                binding.tvErrorOccurred.setText(getString(R.string.error_occurred, error));
            }

        });

        if(searchMode) {
            startSupportActionMode(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_activity_menu, menu);

//        if (menuItem != null) {
//            UiUtillity.INSTANCE.tintMenuIcon(this, menuItem, R.color.themecolor_500);
//        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.action_search :
                searchMode = true;
                binding.srlSwipeContainer.setRefreshing(false);
                binding.srlSwipeContainer.setEnabled(false);
                startSupportActionMode(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {

        mode.getMenuInflater().inflate(R.menu.search_activity_search_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {

        SearchView mSearchView = (SearchView)  menu.findItem(R.id.action_search_view).getActionView();
        mSearchView.setIconifiedByDefault(false);

        mSearchView.requestFocus();

        InputMethodManager inputMethodManager =
                (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(null != inputMethodManager)
            inputMethodManager.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        searchDisposable = RxSearchView.queryTextChanges(mSearchView)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribe(text -> {

                    App.myLog('d', TAG,text.toString());

                    viewModel.searchDB(text);
                    searchText = text.toString();
                });

        if(!TextUtils.isEmpty(searchText)) {
            mSearchView.setQuery(searchText, false);
        }

        return false;
    }

    @Override
    public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(android.support.v7.view.ActionMode mode) {
        searchDisposable.dispose();
        viewModel.searchDB("");
        searchMode = false;
        searchText = null;
        binding.srlSwipeContainer.setRefreshing(false);
        binding.srlSwipeContainer.setEnabled(true);
    }

    @Override
    public void onItemClick(Currency item) {

        if(!TextUtils.isEmpty(selected) && item.getCode().equals(selected)) {
            App.ToastMessage("You have already selected this!", this);
        }
        else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(CURRENCY_RESULT, item);
            setResult(RESULT_OK, returnIntent);
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.exit_to_bottom);
    }

}
