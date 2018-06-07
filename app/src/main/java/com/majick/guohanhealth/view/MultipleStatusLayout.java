package com.majick.guohanhealth.view;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majick.guohanhealth.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MultipleStatusLayout extends RelativeLayout {

    private static final String TAG_LOADING = "MultipleStatusLayout.TAG_LOADING";
    private static final String TAG_EMPTY = "MultipleStatusLayout.TAG_EMPTY";
    private static final String TAG_ERROR = "MultipleStatusLayout.TAG_ERROR";

    final String CONTENT = "type_content";
    final String LOADING = "type_loading";
    final String EMPTY = "type_empty";
    final String ERROR = "type_error";

    LayoutInflater inflater;
    LayoutParams layoutParams;

    List<View> contentViews = new ArrayList<>();

    View loadingStateRelativeLayout;


    View emptyStateRelativeLayout;
    ImageView emptyStateImageView;
    TextView emptyStateContentTextView;

    View errorStateRelativeLayout;
    ImageView errorStateImageView;
    TextView errorStateContentTextView;
    Button errorStateButton;


    private String state = CONTENT;

    public MultipleStatusLayout(Context context) {
        this(context, null);
    }

    public MultipleStatusLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public MultipleStatusLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflater = LayoutInflater.from(context);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {

            contentViews.add(child);
        }
    }

    /**
     * 显示内容
     */
    public void showContent() {
        switchState(CONTENT, 0, null, null, null, Collections.<Integer>emptyList());
    }


    /**
     * 显示内容，并排除要跳过的id
     *
     * @param skipIds 跳过的viewId，不隐藏
     */
    public void showContent(List<Integer> skipIds) {
        switchState(CONTENT, 0, null, null, null, skipIds);
    }

    /**
     * 显示正在加载
     */
    public void showLoading() {
        switchState(LOADING, 0, null, null, null, Collections.<Integer>emptyList());
    }

    /**
     * 显示正在加载
     *
     * @param skipIds 跳过的id，不隐藏
     */
    public void showLoading(List<Integer> skipIds) {
        switchState(LOADING, 0, null, null, null, skipIds);
    }

    /**
     * 显示空界面
     *
     * @param emptyImageDrawable 空界面显示图片
     * @param emptyTextContent   空界面文字
     */
    public void showEmpty(int emptyImageDrawable, String emptyTextContent) {
        switchState(EMPTY, emptyImageDrawable, emptyTextContent, null, null, Collections.<Integer>emptyList());
    }

    /**
     * 显示空界面
     *
     * @param emptyImageDrawable 空界面显示图片
     * @param emptyTextContent   空界面文字
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextContent) {
        switchState(EMPTY, emptyImageDrawable, emptyTextContent, null, null, Collections.<Integer>emptyList());
    }


    /**
     * 显示空界面
     *
     * @param emptyImageDrawable 空界面显示图片
     * @param emptyTextContent   空界面文字
     * @param skipIds            跳出的viewId, 不隐藏
     */
    public void showEmpty(int emptyImageDrawable, String emptyTextContent, List<Integer> skipIds) {
        switchState(EMPTY, emptyImageDrawable, emptyTextContent, null, null, skipIds);
    }


    /**
     * 显示空界面
     *
     * @param emptyImageDrawable 空界面显示图片
     * @param emptyTextContent   空界面文字
     * @param skipIds            跳出的viewId, 不隐藏
     */
    public void showEmpty(Drawable emptyImageDrawable, String emptyTextContent, List<Integer> skipIds) {
        switchState(EMPTY, emptyImageDrawable, emptyTextContent, null, null, skipIds);
    }


    /**
     * 显示错误界面
     *
     * @param errorImageDrawable 错误显示图片
     * @param errorTextContent   错误提示文字
     * @param errorButtonText    错误按钮提示文字
     * @param onClickListener    点击监听
     */
    public void showError(int errorImageDrawable, String errorTextContent, String errorButtonText, OnClickListener onClickListener) {
        switchState(ERROR, errorImageDrawable, errorTextContent, errorButtonText, onClickListener, Collections.<Integer>emptyList());
    }


    public void showError(Drawable errorImageDrawable, String errorTextContent, String errorButtonText, OnClickListener onClickListener) {
        switchState(ERROR, errorImageDrawable, errorTextContent, errorButtonText, onClickListener, Collections.<Integer>emptyList());
    }


    public void showError(int errorImageDrawable, String errorTextContent, String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        switchState(ERROR, errorImageDrawable, errorTextContent, errorButtonText, onClickListener, skipIds);
    }


    public void showError(Drawable errorImageDrawable, String errorTextContent, String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        switchState(ERROR, errorImageDrawable, errorTextContent, errorButtonText, onClickListener, skipIds);
    }

    public String getState() {
        return state;
    }


    public boolean isContent() {
        return state.equals(CONTENT);
    }


    public boolean isLoading() {
        return state.equals(LOADING);
    }


    public boolean isEmpty() {
        return state.equals(EMPTY);
    }


    public boolean isError() {
        return state.equals(ERROR);
    }

    private void switchState(String state, Drawable drawable, String errorTextContent,
                             String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;

        switch (state) {
            case CONTENT:
                //Hide all state views to display content
                setContentState(skipIds);
                break;
            case LOADING:
                setLoadingState(skipIds);
                break;
            case EMPTY:
                setEmptyState(skipIds);

                emptyStateImageView.setImageDrawable(drawable);
                emptyStateContentTextView.setText(errorTextContent);
                break;
            case ERROR:
                setErrorState(skipIds);

                errorStateImageView.setImageDrawable(drawable);
                errorStateContentTextView.setText(errorTextContent);
                errorStateButton.setText(errorButtonText);
                errorStateButton.setOnClickListener(onClickListener);
                break;
        }
    }

    /**
     * 切换当前状态
     *
     * @param state            状态
     * @param drawable         图片id
     * @param errorTextContent 提示文字
     * @param errorButtonText  按钮提示文字
     * @param onClickListener  点击事件
     */
    private void switchState(String state, int drawable, String errorTextContent,
                             String errorButtonText, OnClickListener onClickListener, List<Integer> skipIds) {
        this.state = state;
        switch (state) {
            case CONTENT:
                setContentState(skipIds);
                break;
            case LOADING:
                setLoadingState(skipIds);
                break;
            case EMPTY:
                setEmptyState(skipIds);
                emptyStateImageView.setImageResource(drawable);
                emptyStateContentTextView.setText(errorTextContent);
                break;
            case ERROR:
                setErrorState(skipIds);
                errorStateImageView.setImageResource(drawable);
                errorStateContentTextView.setText(errorTextContent);
                errorStateButton.setText(errorButtonText);
                errorStateButton.setOnClickListener(onClickListener);
                break;
        }
    }

    /**
     * 设置当前加载布局
     */
    private void setLoadingView() {
        if (loadingStateRelativeLayout == null) {
            loadingStateRelativeLayout = inflater.inflate(R.layout.layout_loading, null);
            loadingStateRelativeLayout.setTag(TAG_LOADING);
            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);
            addView(loadingStateRelativeLayout, layoutParams);
        } else {
            loadingStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置当前空界面
     */
    private void setEmptyView() {
        if (emptyStateRelativeLayout == null) {
            emptyStateRelativeLayout = inflater.inflate(R.layout.layout_empty, null);
            emptyStateRelativeLayout.setTag(TAG_EMPTY);

            emptyStateImageView = emptyStateRelativeLayout.findViewById(R.id.iv_image);
            emptyStateContentTextView = emptyStateRelativeLayout.findViewById(R.id.tv_text);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(emptyStateRelativeLayout, layoutParams);
        } else {
            emptyStateRelativeLayout.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置错误界面
     */
    private void setErrorView() {
        if (errorStateRelativeLayout == null) {
            errorStateRelativeLayout = inflater.inflate(R.layout.layout_error, null);
            errorStateRelativeLayout.setTag(TAG_ERROR);

            errorStateImageView = errorStateRelativeLayout.findViewById(R.id.iv_image);
            errorStateContentTextView = errorStateRelativeLayout.findViewById(R.id.tv_text);
            errorStateButton = errorStateRelativeLayout.findViewById(R.id.btn_retry);

            layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_IN_PARENT);

            addView(errorStateRelativeLayout, layoutParams);
        } else {
            errorStateRelativeLayout.setVisibility(VISIBLE);
        }
    }


    private void setContentState(List<Integer> skipIds) {
        hideLoadingView();
        hideEmptyView();
        hideErrorView();

        setContentVisibility(true, skipIds);
    }

    private void setLoadingState(List<Integer> skipIds) {
        hideEmptyView();
        hideErrorView();

        setLoadingView();
        setContentVisibility(false, skipIds);
    }

    private void setEmptyState(List<Integer> skipIds) {
        hideLoadingView();
        hideErrorView();

        setEmptyView();
        setContentVisibility(false, skipIds);
    }

    private void setErrorState(List<Integer> skipIds) {
        hideLoadingView();
        hideEmptyView();

        setErrorView();
        setContentVisibility(false, skipIds);
    }

    /**
     * 设置内容是否显示，并排除要排除的view
     *
     * @param visible
     * @param skipIds
     */
    private void setContentVisibility(boolean visible, List<Integer> skipIds) {
        for (View v : contentViews) {
            if (skipIds != null && !skipIds.contains(v.getId())) {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            } else {
                v.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }
    }

    /**
     * 隐藏加载界面
     */
    private void hideLoadingView() {
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

        }
    }

    /**
     * 隐藏空界面
     */
    private void hideEmptyView() {
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);
        }
    }

    /**
     * 隐藏错误界面
     */
    private void hideErrorView() {
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);
        }
    }

    public void hideLoadView(){
        if (errorStateRelativeLayout != null) {
            errorStateRelativeLayout.setVisibility(GONE);
        }
        if (emptyStateRelativeLayout != null) {
            emptyStateRelativeLayout.setVisibility(GONE);
        }
        if (loadingStateRelativeLayout != null) {
            loadingStateRelativeLayout.setVisibility(GONE);

        }
    }
}
