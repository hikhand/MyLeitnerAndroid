package ir.khaled.myleitner.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.adapter.AppBaseAdapter;
import ir.khaled.myleitner.interfaces.Loading;

/**
 * Created by kh.bakhtiari on 6/22/2014.
 */
public class LazyLoadHelperList<T> extends LazyLoadHelper<T> implements View.OnClickListener {
    RelativeLayout rl_errorFooter;
    ProgressBar pb_loadingFooter;
    ListView listView;
    Context context;
    private Loading loading;

    public LazyLoadHelperList(Context context, Loading loading, AppBaseAdapter<T> adapter, LazyLoadListener<T> lazyLoadListener) {
        super(adapter, lazyLoadListener);
        this.loading = loading;
        this.context = context;
        View v_footer = LayoutInflater.from(context).inflate(R.layout.list_bottom_error, null);
        rl_errorFooter = (RelativeLayout) v_footer.findViewById(R.id.rl_error);
        pb_loadingFooter = (ProgressBar) v_footer.findViewById(R.id.pb_loading);
        v_footer.findViewById(R.id.btn_retry).setOnClickListener(this);
        listView = (ListView) adapter.getListView();
        listView.addFooterView(v_footer);
    }

    public void startLoadingMain() {
        loading.startLoading();
    }

    @Override
    public void stopLoadingMain() {
        loading.stopLoading();
    }

    @Override
    public void setAdapter() {
        listView.setAdapter(adapter);
    }

    @Override
    public void showErrorMain() {
        loading.showError(context.getString(R.string.error), context.getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.startLoading();
                retry();
            }
        });
    }

    public void startLoadingFooter() {
        rl_errorFooter.setVisibility(View.GONE);
        pb_loadingFooter.setVisibility(View.VISIBLE);
    }

    public void stopLoadingFooter() {
        rl_errorFooter.setVisibility(View.GONE);
        pb_loadingFooter.setVisibility(View.GONE);
    }

    public void showErrorFooter() {
        rl_errorFooter.setVisibility(View.VISIBLE);
        pb_loadingFooter.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        retry();
    }
}
