package ir.khaled.myleitner.model;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ir.khaled.myleitner.interfaces.ResponseReceiveListener;

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

    public static void saveCard(Context context, final Card card, SaveCardListener saveCardListener) {
        if (TextUtils.isEmpty(card.title)) {
            saveCardListener.saveCardInvalidName();
            return;
        }
        if (TextUtils.isEmpty(card.front)) {
            saveCardListener.saveCardInvalidFront();
            return;
        }
        if (TextUtils.isEmpty(card.back)) {
            saveCardListener.saveCardInvalidBack();
            return;
        }

        WebRequest request = new WebRequest(context, WebRequest.REQUEST_ADD_CARD) {
            @Override
            public ArrayList<WebRequest.Param> getExtraParams() {
                ArrayList<WebRequest.Param> extraParams = new ArrayList<Param>();
                extraParams.add(new Param(PARAM_CARD, gson.toJson(card)));
                return extraParams;
            }
        };

        Type myType = new TypeToken<WebResponse<Boolean>>() {
        }.getType();
        WebClient<Boolean> webClient = new WebClient<Boolean>(context, request, WebClient.Connection.PERMANENT, myType, saveCardListener);
        webClient.start();
    }



    public static interface SaveCardListener extends ResponseReceiveListener<Boolean> {

        public void saveCardInvalidName();

        public void saveCardInvalidFront();

        public void saveCardInvalidBack();
    }
}
