package com.kenshin.mcassigment.mastercardinterviewassignment.adapter.difUtil;

import android.support.v7.util.DiffUtil;

import com.kenshin.mcassigment.mastercardinterviewassignment.model.Currency;

import java.util.List;

/**
 * Created by kennethleong on 22/9/17.
 */

public class CurrencyListDiffUtil extends DiffUtil.Callback {

    private List<Currency> oldList;
    private List<Currency> newList;

    public CurrencyListDiffUtil(List<Currency> oldList, List<Currency> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    //Check the Id
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCode().equals(newList.get(newItemPosition).getCode());
    }

    // check the contents
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        Currency newItem = newList.get(newItemPosition);
        Currency oldItem = oldList.get(oldItemPosition);

        return (oldItem.getCode().equals(newItem.getCode())) &&
                (oldItem.getCountry().equals(newItem.getCountry())) &&
                (oldItem.getFlagPath().equals(newItem.getFlagPath())) &&
                (oldItem.getName().equals(newItem.getName())) &&
                (oldItem.getRate() == newItem.getRate());

    }

}
