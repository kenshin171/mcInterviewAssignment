package com.kenshin.mcassigment.mastercardinterviewassignment.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.LayoutCurrencyItemBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

import java.util.List;

/**
 * Created by kennethleong on 20/9/17.
 */

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    private List<Currency> mDataSet;

    public CurrencyListAdapter(List<Currency> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_currency_item, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.binding.setCurrency(mDataSet.get(position));

    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        if(null != mDataSet) {
            return mDataSet.size();
        }
        else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LayoutCurrencyItemBinding binding;

        public ViewHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {


        }
    }
}
