package ir.khaled.myleitner.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import ir.khaled.myleitner.model.Card;

/**
 * Created by khaled on 6/30/2014.
 */
public class FrontAdapter extends AppBaseAdapter<Card> {

    public FrontAdapter(Context context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
