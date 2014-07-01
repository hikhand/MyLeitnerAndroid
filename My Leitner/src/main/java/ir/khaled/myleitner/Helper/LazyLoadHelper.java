package ir.khaled.myleitner.helper;

import android.widget.AbsListView;

import java.util.ArrayList;

import ir.khaled.myleitner.adapter.AppBaseAdapter;
import ir.khaled.myleitner.interfaces.ResponseListener;
import ir.khaled.myleitner.webservice.Response;

/**
 * Created by kh.bakhtiari on 6/21/2014.
 */

public abstract class LazyLoadHelper<T> implements ResponseListener<ArrayList<T>>, AbsListView.OnScrollListener {
    protected AppBaseAdapter<T> adapter;
    protected LazyLoadListener<T> lazyLoadListener;
    private AbsListView listView;
    private boolean isGettingNewData;
    private int offset = 0;
    private boolean failedGetNewData;
    private boolean listFinished;
    private boolean wasInRangeRefresh;

    public LazyLoadHelper(AppBaseAdapter<T> adapter, LazyLoadListener<T> lazyLoadListener) {
        this.lazyLoadListener = lazyLoadListener;
        this.adapter = adapter;
        this.listView = adapter.getListView();
    }

    public void setup() {
        listView.setOnScrollListener(this);
        if (adapter.isEmpty()) {
            setAdapter();
            needNewData();
        }
    }

    /**
     * this method must be Override
     */
    public void setAdapter() {
//        listView.setAdapter(adapter);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int i) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean isInRangeRefresh = firstVisibleItem + visibleItemCount + 5 >= totalItemCount;

        if (wasInRangeRefresh && isInRangeRefresh)//last time was in refreshRange and so is now
            return;
        if (!isInRangeRefresh || listFinished) {
            //now is not in the refresh range or list is finished
            wasInRangeRefresh = false;
            return;
        }
        wasInRangeRefresh = true;

        //now is refresh range and needs to be refreshed
        needNewData();
    }

    private synchronized void needNewData() {
        if (isGettingNewData)
            return;
        isGettingNewData = true;

        if (offset == 0) {
            startLoadingMain();
        } else {
            startLoadingFooter();
        }

        lazyLoadListener.onNeedNewDataSet(offset, this);
    }

    @Override
    public void onResponse(ArrayList<T> entity) {
        failedGetNewData = false;
        isGettingNewData = false;
        listFinished = entity.size() == 0;

        adapter.addData(entity);

        offset += entity.size();

        lazyLoadListener.onNewDataSetSet(adapter.getData());

        stopLoadingMain();
        stopLoadingFooter();
    }

    @Override
    public void onResponseError(Response<ArrayList<T>> response) {
        failedGetNewData = true;
        isGettingNewData = false;

        if (getOffset() == 0) {
            showErrorMain();
        } else {
            showErrorFooter();
        }

//        lazyLoadListener.onFailedLoadNewDataSet();
    }

    public void retry() {
        if (!failedGetNewData)
            return;

        needNewData();
    }

    public boolean isInProgress() {
        return isGettingNewData;
    }

    public int getOffset() {
        return offset;
    }

    public abstract void startLoadingMain();

    public abstract void stopLoadingMain();

    public abstract void showErrorMain();

    public abstract void startLoadingFooter();

    public abstract void stopLoadingFooter();

    public abstract void showErrorFooter();

    public static interface LazyLoadListener<T> {

        public void onNeedNewDataSet(int offset, ResponseListener<ArrayList<T>> listener);

        /**
         * @param data ArrayList in {@link ir.khaled.myleitner.adapter.AppBaseAdapter#data}
         */
        public void onNewDataSetSet(ArrayList<T> data);

    }
}