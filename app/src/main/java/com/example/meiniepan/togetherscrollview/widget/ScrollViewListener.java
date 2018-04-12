package com.example.meiniepan.togetherscrollview.widget;

/**
 * Created by Solang on 2018/3/29.
 */

public interface ScrollViewListener<T> {
    void onScrollChanged(T scrollView, int x, int y);
}
