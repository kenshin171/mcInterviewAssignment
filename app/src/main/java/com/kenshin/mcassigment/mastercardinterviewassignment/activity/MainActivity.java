package com.kenshin.mcassigment.mastercardinterviewassignment.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.View;

import com.evernote.android.state.State;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.ActivityMainBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.ExchangeResponse;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;
import com.kenshin.mcassigment.mastercardinterviewassignment.util.CommonUtility;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.MainActivityViewModel;
import com.kenshin.mcassigment.mastercardinterviewassignment.viewModel.providerFactory.MainActvityVMproviderFactory;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static final int I_WANT_CURRENCY = 111;
    public static final int I_HAVE_CURRENCY = 999;
    public static final String WANT_HAVE = "WANT_HAVE";
    public static final String SELECTED = "SELECTED";

    ActivityMainBinding binding;

    @Inject
    RetroFitService retroFitService;

    @Inject
    @Named("MainActivityVMFactory")
    MainActvityVMproviderFactory factory;

    MainActivityViewModel viewModel;

    @State
    long haveAmount;

    @State
    long wantAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) binding.incToolbar.llToolbar.getLayoutParams();
        lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        setSupportActionBar(binding.incToolbar.toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setActionBarTitle(getString(R.string.main_activity_title), binding.incToolbar.toolbar);
        }

        if(null == savedInstanceState) {
            binding.setIHaveCur(null);
            binding.setIWantCur(null);
        }

        RxView.clicks(binding.cvHaveCard)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIHaveCur());

        RxView.clicks(binding.ivHaveCountryFlag)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIHaveCur());

        RxView.clicks(binding.tvHaveCtyCode)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIHaveCur());

        RxView.clicks(binding.cvWantCard)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIWantCur());

        RxView.clicks(binding.ivWantCountryFlag)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIWantCur());

        RxView.clicks(binding.tvWantCtyCode)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIWantCur());


        RxTextView.textChanges(binding.cdtHaveAmount)
                .compose(provider.bindToLifecycle())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {

                    haveAmount = binding.cdtHaveAmount.getRawValue();

                    boolean focus = binding.cdtHaveAmount.hasFocus();

                    if(focus) {

                        if (null != viewModel.getiHaveWantExc().getValue()) {
                            long calculated = CommonUtility.calculateConverstion(haveAmount / 100, viewModel.getiHaveWantExc().getValue()[0].getRate());
                            binding.cdtWantAmount.setValue(calculated);

                        }
                    }
                });

        RxTextView.textChanges(binding.cdtWantAmount)
                .compose(provider.bindToLifecycle())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> {

                    wantAmount = binding.cdtWantAmount.getRawValue();

                    boolean focus = binding.cdtWantAmount.hasFocus();

                    if(focus) {

                        if (null != viewModel.getiHaveWantExc().getValue()) {
                            long calculated = CommonUtility.calculateConverstion(wantAmount / 100, viewModel.getiHaveWantExc().getValue()[1].getRate());
                            binding.cdtHaveAmount.setValue(calculated);

                        }
                    }
                });

        binding.cdtHaveAmount.setDefaultLocale(Locale.US);
        binding.cdtWantAmount.setDefaultLocale(Locale.US);

    }

    @Override
    protected void onResume() {
        super.onResume();

        setHaveWantAmountQuietly();

        viewModel.getiHaveCur().observe(this, iHaveCur -> {
            binding.setIHaveCur(iHaveCur);
//            if(null != iHaveCur) {
//                Locale l = CommonUtility.localeLookUp(iHaveCur.getCountry(), iHaveCur.getCode());
//                binding.cdtHaveAmount.setLocale(l);
//            }

            boolean enableTextFields = (null != viewModel.getiWantCur().getValue())
                    && (null != viewModel.getiHaveCur().getValue());
            binding.cdtHaveAmount.setEnabled(enableTextFields);
            binding.cdtWantAmount.setEnabled(enableTextFields);

        });

        viewModel.getiWantCur().observe(this, iWantCur -> {
            binding.setIWantCur(iWantCur);
//            if(null != iWantCur) {
//                Locale l = CommonUtility.localeLookUp(iWantCur.getCountry(), iWantCur.getCode());
//                binding.cdtHaveAmount.setLocale(l);
//            }

            boolean enableTextFields = (null != viewModel.getiWantCur().getValue())
                    && (null != viewModel.getiHaveCur().getValue());
            binding.cdtHaveAmount.setEnabled(enableTextFields);
            binding.cdtWantAmount.setEnabled(enableTextFields);
        });

        viewModel.getiHaveWantExc().observe(this, iHaveWantExc -> {

            if(iHaveWantExc != null) {
                if(null != iHaveWantExc[0]) {
                    ExchangeResponse iHaveEx = iHaveWantExc[0];
                    binding.tvHaveExchangeRate.setText(getString(R.string.currency_convert,
                            1,iHaveEx.getBaseCode(), iHaveEx.getRate(), iHaveEx.getTargetCode()));
                }
                else {
                    if(null != viewModel.getiHaveCur().getValue() && null != viewModel.getiWantCur().getValue()) {
                        binding.tvHaveExchangeRate.setText(getString(R.string.currency_convert,
                                0.0, viewModel.getiHaveCur().getValue().getCode(), 0.0, viewModel.getiWantCur().getValue().getCode()));
                    }
                    else {
                        binding.tvHaveExchangeRate.setText("");
                    }
                }

                if(null != iHaveWantExc[1]) {
                    ExchangeResponse iWantEx = iHaveWantExc[1];
                    binding.tvWantExchangeRate.setText(getString(R.string.currency_convert,
                            1,iWantEx.getBaseCode(), iWantEx.getRate(), iWantEx.getTargetCode()));
                }
                else {
                    if(null != viewModel.getiHaveCur().getValue() && null != viewModel.getiWantCur().getValue()) {

                        binding.tvWantExchangeRate.setText(getString(R.string.currency_convert,
                                0.0, viewModel.getiWantCur().getValue().getCode(), 0.0, viewModel.getiHaveCur().getValue().getCode()));
                    }
                    else {
                        binding.tvWantExchangeRate.setText("");
                    }
                }

                if(iHaveWantExc.length >= 1 && haveAmount == -1 && wantAmount == -1) {
                    haveAmount = 100L;
                    wantAmount = CommonUtility.roundParseRate(iHaveWantExc[0].getRate());
                    setHaveWantAmountQuietly();
                }

            }
            else {
                binding.tvHaveExchangeRate.setText("");
                binding.tvWantExchangeRate.setText("");

                binding.cdtHaveAmount.setVisibility(View.GONE);
                binding.cdtWantAmount.setVisibility(View.GONE);
                binding.clpbHaveLoading.setVisibility(View.VISIBLE);
                binding.clpbHaveLoading.setVisibility(View.VISIBLE);
            }

        });

    }

    private void setHaveWantAmountQuietly() {
        binding.cdtHaveAmount.clearFocus();
        binding.cdtWantAmount.clearFocus();

        binding.cdtHaveAmount.setVisibility(View.VISIBLE);
        binding.cdtWantAmount.setVisibility(View.VISIBLE);
        binding.clpbHaveLoading.setVisibility(View.GONE);
        binding.clpbHaveLoading.setVisibility(View.GONE);

        binding.cdtHaveAmount.setValue(haveAmount);
        binding.cdtWantAmount.setValue(wantAmount);
    }

    private void gotoSelectIHaveCur() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(WANT_HAVE, I_HAVE_CURRENCY);
        if(null != viewModel.getiHaveCur().getValue()) {
            i.putExtra(SELECTED, viewModel.getiHaveCur().getValue().getCode());
        }
        startActivityForResult(i,I_HAVE_CURRENCY);
        overridePendingTransition(R.anim.enter_from_bottom,R.anim.stay);
    }

    private void gotoSelectIWantCur() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(WANT_HAVE, I_WANT_CURRENCY);
        if(null != viewModel.getiWantCur().getValue()) {
            i.putExtra(SELECTED, viewModel.getiWantCur().getValue().getCode());
        }
        startActivityForResult(i,I_WANT_CURRENCY);
        overridePendingTransition(R.anim.enter_from_bottom,R.anim.stay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == I_HAVE_CURRENCY && resultCode == RESULT_OK) {
            Currency iHaveCur = data.getParcelableExtra(SearchActivity.CURRENCY_RESULT);
            binding.setIHaveCur(iHaveCur);
            viewModel.getiHaveCur().setValue(iHaveCur);

            if(viewModel.getiWantCur().getValue() != null &&
                    iHaveCur.getCode().equals(viewModel.getiWantCur().getValue().getCode())) {
                viewModel.getiWantCur().setValue(null);
            }

            binding.executePendingBindings();

        }
        else if (requestCode == I_WANT_CURRENCY && resultCode == RESULT_OK) {
            Currency iWantCur = data.getParcelableExtra(SearchActivity.CURRENCY_RESULT);
            binding.setIWantCur(iWantCur);
            viewModel.getiWantCur().setValue(iWantCur);

            if(viewModel.getiHaveCur().getValue() != null &&
                    iWantCur.getCode().equals(viewModel.getiHaveCur().getValue().getCode())) {
                viewModel.getiHaveCur().setValue(null);
            }

            binding.executePendingBindings();
        }

        haveAmount = -1;
        wantAmount = -1;

        viewModel.getExchangeRates();
    }
}
