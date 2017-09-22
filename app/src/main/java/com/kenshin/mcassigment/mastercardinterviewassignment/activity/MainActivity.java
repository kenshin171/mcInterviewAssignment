package com.kenshin.mcassigment.mastercardinterviewassignment.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.evernote.android.state.State;
import com.jakewharton.rxbinding2.view.RxView;
import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.ActivityMainBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.ExchangeResponse;
import com.kenshin.mcassigment.mastercardinterviewassignment.retrofitService.RetroFitService;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    public static final int I_WANT_CURRENCY = 111;
    public static final int I_HAVE_CURRENCY = 999;
    public static final String WANT_HAVE = "WANT_HAVE";
    public static final String SELECTED = "SELECTED";

    ActivityMainBinding binding;

    @State
    Currency iHaveCur;

    @State
    Currency iWantCur;

    @Inject
    RetroFitService retroFitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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

        RxView.clicks(binding.cvWantCard)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIWantCur());

        RxView.clicks(binding.ivWantCountryFlag)
                .compose(provider.bindToLifecycle())
                .subscribe(View -> gotoSelectIWantCur());

    }

    private void gotoSelectIHaveCur() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(WANT_HAVE, I_HAVE_CURRENCY);
        if(null != iHaveCur) {
            i.putExtra(SELECTED, iHaveCur.getCode());
        }
        startActivityForResult(i,I_HAVE_CURRENCY);
        overridePendingTransition(R.anim.enter_from_bottom,R.anim.stay);
    }

    private void gotoSelectIWantCur() {
        Intent i = new Intent(this, SearchActivity.class);
        i.putExtra(WANT_HAVE, I_WANT_CURRENCY);
        if(null != iWantCur) {
            i.putExtra(SELECTED, iWantCur.getCode());
        }
        startActivityForResult(i,I_WANT_CURRENCY);
        overridePendingTransition(R.anim.enter_from_bottom,R.anim.stay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == I_HAVE_CURRENCY && resultCode == RESULT_OK) {
            iHaveCur = data.getParcelableExtra(SearchActivity.CURRENCY_RESULT);
            binding.setIHaveCur(iHaveCur);

            if(iWantCur != null && iHaveCur.getCode().equals(iWantCur.getCode())) {
                iWantCur = null;
                binding.setIWantCur(null);
            }

            binding.executePendingBindings();

        }
        else if (requestCode == I_WANT_CURRENCY && resultCode == RESULT_OK) {
            iWantCur = data.getParcelableExtra(SearchActivity.CURRENCY_RESULT);
            binding.setIWantCur(iWantCur);

            if(iHaveCur != null && iWantCur.getCode().equals(iHaveCur.getCode())) {
                iHaveCur = null;
                binding.setIHaveCur(null);
            }

            binding.executePendingBindings();
        }

        if(null != iHaveCur && null != iWantCur) {

            Observable<ExchangeResponse> iHaveCurExchange = retroFitService.getExchangeRate(iHaveCur.getCode(), iWantCur.getCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindToLifecycle());

            Observable<ExchangeResponse> iWantCurExchange = retroFitService.getExchangeRate(iWantCur.getCode(), iHaveCur.getCode())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindToLifecycle());

            Observable<ExchangeResponse[]> combined =
                    Observable.zip(iHaveCurExchange, iWantCurExchange,
                            (haveExchange, wantExchange) -> new ExchangeResponse[]{haveExchange, wantExchange})
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(provider.bindToLifecycle());


            combined.subscribe( exchangeResponses -> {

                if(exchangeResponses != null) {
                    if(null != exchangeResponses[0]) {
                        ExchangeResponse iHaveEx = exchangeResponses[0];
                        binding.tvHaveExchangeRate.setText(getString(R.string.currency_convert,
                                1,iHaveEx.getBaseCode(), iHaveEx.getRate(), iHaveEx.getTargetCode()));
                    }
                    else {
                        binding.tvHaveExchangeRate.setText(getString(R.string.currency_convert,
                                0.0,iHaveCur.getCode(), 0.0, iWantCur.getCode()));
                    }

                    if(null != exchangeResponses[1]) {
                        ExchangeResponse iWantEx = exchangeResponses[1];
                        binding.tvWantExchangeRate.setText(getString(R.string.currency_convert,
                                1,iWantEx.getBaseCode(), iWantEx.getRate(), iWantEx.getTargetCode()));
                    }
                    else {
                        binding.tvHaveExchangeRate.setText(getString(R.string.currency_convert,
                                0.0,iWantCur.getCode(), 0.0, iHaveCur.getCode()));
                    }

                }

            });
        }
        else {
            binding.tvHaveExchangeRate.setText("");
            binding.tvWantExchangeRate.setText("");
            binding.tvHaveAmount.setText("");
            binding.tvWantAmount.setText("");
        }

    }
}
