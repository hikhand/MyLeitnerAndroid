package ir.khaled.myleitner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by kh.bakhtiari on 6/21/2014.
 */
public class AppBaseAdapter<T> extends BaseAdapter {
    protected ArrayList<T> data;
    protected Context context;
    private AbsListView listView;

    public AppBaseAdapter(Context context, AbsListView listView) {
        data = new ArrayList<T>();
        this.context = context;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public synchronized void addData(ArrayList<T> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<T> getData() {
        return data;
    }

    public AbsListView getListView() {
        return listView;
    }

}
