package com.example.meiniepan.togetherscrollview.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.meiniepan.togetherscrollview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Solang on 2017/10/24.
 */

public class WalletFootAdapter extends RecyclerView.Adapter<WalletFootAdapter.RecyclerHolder> {
    private Context mContext;
    private List<String> dataList = new ArrayList<>();

    public WalletFootAdapter(RecyclerView recyclerView) {
        this.mContext = recyclerView.getContext();
    }


    public void setData(List<String> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.id_rv_item_layout, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        holder.textView.setText(dataList.get(position));
        if (position % 2 == 1)
            holder.itemView.setBackgroundColor(Color.BLUE);
        else holder.itemView.setBackgroundColor(Color.CYAN);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textView;

        private RecyclerHolder(View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.tv__id_item_layout);
        }
    }
}
