package ir.khaled.myleitner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.activity.MainActivity;
import ir.khaled.myleitner.model.Card;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by khaled on 6/20/2014.
 */
public class AddCardFragment extends Fragment implements Card.AddCard.OnCardAddListener{
    private Context context;
    private ViewHolder viewHolder;

    public static AddCardFragment newInstance() {
        AddCardFragment fragment = new AddCardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context = getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).onFragmentAttached(MainActivity.FRAGMENT_ADD_CARD, getString(R.string.addCard_title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewHolder = new ViewHolder();
        viewHolder.v_root = inflater.inflate(R.layout.fragment_add_card, container, false);

        viewHolder.et_title = (AppEditText) viewHolder.v_root.findViewById(R.id.et_title);
        viewHolder.et_front = (AppEditText) viewHolder.v_root.findViewById(R.id.et_front);
        viewHolder.et_back = (AppEditText) viewHolder.v_root.findViewById(R.id.et_back);
        AppButton btn_saveCard = (AppButton) viewHolder.v_root.findViewById(R.id.btn_saveCard);
        btn_saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });
        return viewHolder.v_root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_saveCard) {
            saveCard();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.add_card, menu);
    }

    private void saveCard() {
        Card card = new Card(
                viewHolder.et_title.getText().toString(),
                viewHolder.et_front.getText().toString(),
                viewHolder.et_back.getText().toString()
        );
        Card.AddCard addCard = new Card.AddCard(context, viewHolder, this);
        addCard.addCard();
    }

    @Override
    public void onCardAdded() {
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public void onCardAddCanceled() {

    }

    public class ViewHolder {
        public View v_root;
        public AppEditText et_title;
        public AppEditText et_front;
        public AppEditText et_back;
    }
}
