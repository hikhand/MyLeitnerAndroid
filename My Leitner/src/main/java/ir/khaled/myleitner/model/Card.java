package ir.khaled.myleitner.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.dialog.AppDialog;
import ir.khaled.myleitner.fragment.AddCardFragment;
import ir.khaled.myleitner.interfaces.ResponseListener;
import ir.khaled.myleitner.webservice.Request;
import ir.khaled.myleitner.webservice.Response;
import ir.khaled.myleitner.webservice.WebClient;

/**
 * Created by khaled on 6/22/2014.
 */
public class Card {
    private static final String PARAM_CARD = "card";
    private static Gson gson = new Gson();
    //    public int id;
//    public int userId;
//    public int leitnerId;
//    public int createTime;
//    public int checkTime;
//    public int likeCount;
//    public int boxIndex;
//    public int deckIndex;
//    public int countCorrect;
//    public int countIncorrect;
    @Expose
    public String title;
    @Expose
    public String front;
    @Expose
    public String back;
//    public Tag[] tags;
//    public Category category;
//    public Comment[] comments;
//    public Example[] examples;

    public Card(String title, String front, String back) {
        this.title = title;
        this.front = front;
        this.back = back;
    }

    public static class AddCard extends AppDialog implements ResponseListener<Boolean>, View.OnClickListener {
        private Context context;
        private Card card;
        private AddCardFragment.ViewHolder viewHolder;
        private OnCardAddListener cardAddListener;

        public AddCard(Context context, AddCardFragment.ViewHolder viewHolder, OnCardAddListener cardAddListener) {
            super(context);
            this.context = context;
            this.card = new Card(
                    viewHolder.et_title.getText().toString(),
                    viewHolder.et_front.getText().toString(),
                    viewHolder.et_back.getText().toString()
            );
            this.viewHolder = viewHolder;
            this.cardAddListener = cardAddListener;
        }

        public void addCard() {
            if (saveCard(context, card)) {
                startLoading(context.getString(R.string.addCardLoading));
                show();
            }
        }

        /**
         *
         * @param context
         * @param card
         * @return true if starts webservice, false otherwise
         */
        private boolean saveCard(Context context, final Card card) {
            if (TextUtils.isEmpty(card.title)) {
                saveCardInvalidTitle();
                return false;
            }
            if (TextUtils.isEmpty(card.front)) {
                saveCardInvalidFront();
                return false;
            }
            if (TextUtils.isEmpty(card.back)) {
                saveCardInvalidBack();
                return false;
            }

            Request request = new Request(context, Request.REQUEST_ADD_CARD) {
                @Override
                public ArrayList<Param> getExtraParams() {
                    ArrayList<Request.Param> extraParams = new ArrayList<Param>();
                    extraParams.add(new Param(PARAM_CARD, gson.toJson(card)));
                    return extraParams;
                }
            };

            Type myType = new TypeToken<Response<Boolean>>() {
            }.getType();
            WebClient<Boolean> webClient = new WebClient<Boolean>(context, request, WebClient.Connection.PERMANENT, myType, this);
            webClient.start();
            return true;
        }

        public void saveCardInvalidTitle() {
            viewHolder.et_title.setError(context.getString(R.string.invalidTitle));
        }

        public void saveCardInvalidFront() {
            viewHolder.et_front.setError(context.getString(R.string.invalidFront));
        }

        public void saveCardInvalidBack() {
            viewHolder.et_back.setError(context.getString(R.string.invalidBack));
        }

        @Override
        public void onResponse(Boolean response) {
            dismiss();
            Toast.makeText(context, context.getString(R.string.cardAdded), Toast.LENGTH_SHORT).show();
            cardAddListener.onCardAdded();
        }

        @Override
        public void onResponseError(Response<Boolean> response) {
            stopLoading();
            showError(context.getString(R.string.cardAddFailed), context.getString(R.string.retry), context.getString(R.string.cancel), this, this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == ID_BTN_POS) {
                addCard();
            } else {
                dismiss();
                cardAddListener.onCardAddCanceled();
            }
        }

        public static interface OnCardAddListener {
            public void onCardAdded();

            public void onCardAddCanceled();
        }
    }
}