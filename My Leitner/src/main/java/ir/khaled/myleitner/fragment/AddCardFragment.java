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
import ir.khaled.myleitner.model.WebResponse;
import ir.khaled.myleitner.view.AppButton;
import ir.khaled.myleitner.view.AppEditText;

/**
 * Created by khaled on 6/20/2014.
 */
public class AddCardFragment extends Fragment implements Card.SaveCardListener {
    private Context context;
    private View v_root;
    private AppEditText et_title;
    private AppEditText et_front;
    private AppEditText et_back;

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
        v_root = inflater.inflate(R.layout.fragment_add_card, container, false);
        et_title = (AppEditText) v_root.findViewById(R.id.et_title);
        et_front = (AppEditText) v_root.findViewById(R.id.et_front);
        et_back = (AppEditText) v_root.findViewById(R.id.et_back);
        AppButton btn_saveCard = (AppButton) v_root.findViewById(R.id.btn_saveCard);
        btn_saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCard();
            }
        });
        return v_root;
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
                et_title.getText().toString(),
                et_front.getText().toString(),
                et_back.getText().toString()
        );
        Card.saveCard(context, card, this);
    }

    @Override
    public void saveCardInvalidName() {

    }

    @Override
    public void saveCardInvalidFront() {

    }

    @Override
    public void saveCardInvalidBack() {

    }

    @Override
    public void onResponseReceived(Boolean response) {

    }

    @Override
    public void onResponseReceiveFailed(WebResponse<Boolean> response) {

    }
}
