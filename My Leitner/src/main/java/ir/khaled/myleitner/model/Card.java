package ir.khaled.myleitner.model;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import ir.khaled.myleitner.R;
import ir.khaled.myleitner.dialog.AppDialog;
import ir.khaled.myleitner.fragment.AddCardFragment;
import ir.khaled.myleitner.interfaces.ResponseListener;
import ir.khaled.myleitner.webservice.Request;
import ir.khaled.myleitner.webservice.Response;
import ir.khaled.myleitner.webservice.WebClient;
import ir.khaled.myleitner.webservice.WebRequest;

/**
 * Created by khaled on 6/22/2014.
 */
public class Card {
    private static final String PARAM_CARD = "card";
    private static Gson gson = new Gson();
    @SerializedName("id")
    public int id;
    @SerializedName("user")
    public User user;
    @SerializedName("leitnerId")
    public int leitnerId;
    @SerializedName("createTime")
    public long createTime;
    @SerializedName("checkTime")
    public int checkTime;
    @SerializedName("likeCount")
    public int likeCount;
    @SerializedName("boxIndex")
    public int boxIndex;
    @SerializedName("deckIndex")
    public int deckIndex;
    @SerializedName("countCorrect")
    public int countCorrect;
    @SerializedName("countIncorrect")
    public int countIncorrect;
    @Expose
    @SerializedName("title")
    public String title;
    @Expose
    @SerializedName("front")
    public String front;
    @Expose
    @SerializedName("back")
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
            if (isCardValid()) {
                startLoading(context.getString(R.string.addCardLoading));
                show();
                saveCard();
            }
        }

        /**
         * checks whether the card is valid to be saved or not.
         * <br/>
         * if card is not valid show the error on their EditTexts
         *
         * @return true if card is valid false otherwise
         */
        private boolean isCardValid() {
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
            return true;
        }

        /**
         * saves card to server
         */
        private void saveCard() {

            Request request = new Request(Request.Method.ADD_CARD, Request.ConType.PERMANENT) {
                @Override
                public HashMap<String, String> getExtraParams() {
                    HashMap<String, String> extraParams = new HashMap<String, String>();
                    extraParams.put(PARAM_CARD, gson.toJson(card));
                    return extraParams;
                }
            };

            WebRequest<Boolean> webRequest = new WebRequest<Boolean>(request, this);
            webRequest.start();
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
