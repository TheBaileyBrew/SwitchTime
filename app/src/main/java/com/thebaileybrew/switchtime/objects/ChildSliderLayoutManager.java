package com.thebaileybrew.switchtime.objects;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ChildSliderLayoutManager extends LinearLayoutManager {
    private static final String TAG = ChildSliderLayoutManager.class.getSimpleName();

    private ChildSliderLayoutManager.OnItemSelectedListener callBackListener;
    private RecyclerView recyclerView;

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

    public ChildSliderLayoutManager(Context context, OnItemSelectedListener listener) {
        super(context);
        this.callBackListener = listener;
        this.setOrientation(RecyclerView.HORIZONTAL);
    }

    public final void setCallBackListener(OnItemSelectedListener listener) {
        this.callBackListener = listener;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        if (view == null) {
            Log.e(TAG, "onAttachedToWindow: view is null");
        }

        //View is not null, attach helpers and define Recycler
        this.recyclerView = view;
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        RecyclerView recycler = this.recyclerView;

        if (this.recyclerView == null) {
            Log.e(TAG, "onAttachedToWindow: recycler is null");
        }
        snapHelper.attachToRecyclerView(recycler);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        assert (state != null);
        super.onLayoutChildren(recycler, state);
        this.scaleDownView();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.getOrientation() == RecyclerView.HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            this.scaleDownView();
            return scrolled;
        } else {
            return 0;
        }
    }

    private final void scaleDownView() {
        float mid = (float) this.getWidth() / 2.0f;

        for (int i = 0; i < this.getChildCount(); i++) {
            View childView = this.getChildAt(i);
            float childMid = (float) (this.getDecoratedLeft(childView) + this.getDecoratedRight(childView)) / 2.0f;
            float distanceFromCenter = Math.abs(mid - childMid);
            float scale = (float) 1 - (float)Math.sqrt((double)(distanceFromCenter / (float) this.getWidth())) * 0.96f;
            childView.setScaleX(scale);
            childView.setScaleY(scale);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == 0) {
            int recyclerViewCenterX = this.getRecyclerViewCenterX();
            RecyclerView recycler = this.recyclerView;
            if (this.recyclerView == null) {
                Log.e(TAG, "onScrollStateChanged: null");
            }

            int minDistance = recycler.getWidth();
            int position = -1;

            for (int i = 0; i < recycler.getChildCount(); i++) {
                View child = recycler.getChildAt(i);
                int childCenterX = this.getDecoratedLeft(child) + (this.getDecoratedRight(child) - this.getDecoratedLeft(child)) / 2;
                int newDistance = Math.abs(childCenterX - recyclerViewCenterX);
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    position = recycler.getChildLayoutPosition(child);
                }
            }

            ChildSliderLayoutManager.OnItemSelectedListener listener = this.callBackListener;
            if (this.callBackListener != null) {
                listener.onItemSelected(position);
            }
        }
    }

    private final int getRecyclerViewCenterX() {
        RecyclerView recycler = this.recyclerView;
        if (this.recyclerView == null) {
            Log.e(TAG, "getRecyclerViewCenterX: null");
        }

        int recyclerRight = recycler.getRight();
        recyclerRight = (recyclerRight - recycler.getLeft()) / 2;

        return recyclerRight + recycler.getLeft();
    }
}
