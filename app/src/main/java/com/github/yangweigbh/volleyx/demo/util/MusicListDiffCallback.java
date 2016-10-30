package com.github.yangweigbh.volleyx.demo.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.github.yangweigbh.volleyx.demo.domain.Music;
import com.github.yangweigbh.volleyx.demo.ui.StringRequestFragment;

import java.util.List;

/**
 * Created by yangwei on 2016/10/30.
 */
public class MusicListDiffCallback extends DiffUtil.Callback {
    private List<Music> mOldList;
    private List<Music> mNewList;

    public MusicListDiffCallback(List<Music> oldList, List<Music> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Music newProduct = mNewList.get(newItemPosition);
        Music oldProduct = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newProduct.getTitle().equals(oldProduct.getTitle())) {
            diffBundle.putString(StringRequestFragment.ItemRecyclerViewAdapter.KEY_TITLE, newProduct.getTitle());
        }

        if (!newProduct.getImage().equals(oldProduct.getImage())) {
            diffBundle.putString(StringRequestFragment.ItemRecyclerViewAdapter.KEY_IMAGE, newProduct.getImage());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;
    }
}
