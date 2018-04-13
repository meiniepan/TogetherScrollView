package com.example.meiniepan.togetherscrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.meiniepan.togetherscrollview.widget.CommonUtil;
import com.example.meiniepan.togetherscrollview.widget.ScrollViewListener;
import com.example.meiniepan.togetherscrollview.widget.WalletFootAdapter;
import com.example.meiniepan.togetherscrollview.widget.WalletFootRecyclerView;
import com.example.meiniepan.togetherscrollview.widget.WalletHeadRecyclerView;
import com.example.meiniepan.togetherscrollview.widget.WalletHeaderAdapter;
import com.gs.buluo.common.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * create by Solang on 2018/4/12
 * i love YuanJiao && WangYouRan
 */
public class MainActivity extends AppCompatActivity implements ScrollViewListener {
    WalletHeadRecyclerView rvHead;
    WalletFootRecyclerView rvFoot;
    private WalletHeaderAdapter adapter;
    private WalletFootAdapter adapterFoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        rvHead = findViewById(R.id.rv_head);
        rvFoot = findViewById(R.id.rv_foot);
        rvFoot.setOnScrollViewListener(this);
        rvHead.setOnScrollViewListener(this);
        initRvHead();
        initRvFoot();
    }

    private void initRvHead() {
        List list = new ArrayList();
        list.add("2");
        list.add("3");
        list.add("4");

        adapter = new WalletHeaderAdapter(R.layout.item_wallet_header, list);
        rvHead.setAdapter(adapter);
        initHeadFoot();
    }

    private void initRvFoot() {
        ArrayList list = new ArrayList<>();
        list.add("2");
        list.add("3");
        list.add("4");
        adapterFoot = new WalletFootAdapter(rvFoot);
        adapterFoot.setData(list);
        rvFoot.setAdapter(adapterFoot);
    }

    /**
     * 为上面的recyclerView添加头布局和尾布局
     */
    private void initHeadFoot() {
        View head = LayoutInflater.from(this).inflate(R.layout.wallet_header_foot, rvHead, false);
        View foot = LayoutInflater.from(this).inflate(R.layout.wallet_header_foot, rvHead, false);
        int width = DensityUtils.dip2px(this, 20);
        int width1 = DensityUtils.dip2px(this, 40);

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) head.getLayoutParams();
        params.width = width;
        head.setLayoutParams(params);
        adapter.addHeaderView(head, 0, 0);
        RecyclerView.LayoutParams params1 = (RecyclerView.LayoutParams) foot.getLayoutParams();
        params1.width = width1;
        foot.setLayoutParams(params1);
        adapter.addFooterView(foot, 0, 0);
    }

    @Override
    public void onScrollChanged(Object scrollView, int x, int y) {
        int width1 = CommonUtil.getScreenWidth(this) - DensityUtils.dip2px(this, 60);
        int width2 = CommonUtil.getScreenWidth(this);
        if (scrollView == rvHead) {
            rvFoot.setmark(false);
            rvFoot.scrollTo(x * width2 / width1, y);
        } else if (scrollView == rvFoot) {
            rvHead.setmark(false);

            rvHead.scrollTo(x * width1 / width2, y);
        }
        rvHead.setmark(true);
        rvFoot.setmark(true);
    }
}
