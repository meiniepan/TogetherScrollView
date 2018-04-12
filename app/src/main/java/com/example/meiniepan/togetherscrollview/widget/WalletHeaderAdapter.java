package com.example.meiniepan.togetherscrollview.widget;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiniepan.togetherscrollview.R;
import com.gs.buluo.common.utils.DensityUtils;

import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class WalletHeaderAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public WalletHeaderAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        View view = helper.getView(R.id.ll);
        if (helper.getAdapterPosition() == 1) {
            view.setBackground(mContext.getResources().getDrawable(R.mipmap.wallet_blue_bac));

        } else if (helper.getAdapterPosition() == 2) {
            view.setBackground(mContext.getResources().getDrawable(R.mipmap.wallet_orange_bac));
        } else if (helper.getAdapterPosition() == 3) {
            view.setBackground(mContext.getResources().getDrawable(R.mipmap.wallet_blue_bac));
        }

    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        int width;
        width = CommonUtil.getScreenWidth(mContext) - DensityUtils.dip2px(mContext, 60);
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
        return view;
    }
}
