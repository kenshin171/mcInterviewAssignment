package com.kenshin.mcassigment.mastercardinterviewassignment.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kenshin.mcassigment.mastercardinterviewassignment.R;
import com.kenshin.mcassigment.mastercardinterviewassignment.adapter.difUtil.CurrencyListDiffUtil;
import com.kenshin.mcassigment.mastercardinterviewassignment.databinding.LayoutCurrencyItemBinding;
import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kennethleong on 20/9/17.
 */

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Currency item);
    }

    private OnItemClickListener mListener;
    private List<Currency> mDataSet;

    public CurrencyListAdapter(List<Currency> mDataSet) {
        this.mDataSet = null != mDataSet ? mDataSet : new ArrayList<>(0);
        hasStableIds();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
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

        Currency item = mDataSet.get(position);

        holder.binding.setCurrency(item);
        holder.binding.getRoot().setTag(item);
    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).getCode().hashCode();
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
            if(null != mListener) {
                Currency clickedItem = (Currency) v.getTag();
                mListener.onItemClick(clickedItem);
            }
        }
    }

    public void updateList(List<Currency> newList) {
        if(null != newList && !newList.isEmpty()) {
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CurrencyListDiffUtil(this.mDataSet, newList), true);
            this.mDataSet.clear();
            this.mDataSet.addAll(newList);
            diffResult.dispatchUpdatesTo(this);
        } else {
            int size = this.mDataSet.size();
            this.mDataSet.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

}
