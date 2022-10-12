package bitirme.sorsor.service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bitirme.sorsor.GsonRequest;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.Token;
import bitirme.sorsor.VolleySingleton;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.User;

/**
 * Created by mert on 01.05.2016.
 */
public class SingleetonUserService {
    private static SingleetonUserService instance = null;
    private Activity act;
    private SingleetonUserService() {
    }
    public static synchronized SingleetonUserService getInstance(Activity act) {
        if (instance == null) {
            instance = new SingleetonUserService();
            instance.act = act;
        }
        else if(instance.act==null){
            instance.act = act;
        }
        return instance;
    }
    public void getMyProfile(Response.Listener<User> listener){
        VolleySingleton.getInstance(act).addToRequestQueue(createAGetCurrentUserProfileRequest(listener));
    }
    public void getUserProfile(Response.Listener<User> listener, int id){
        VolleySingleton.getInstance(act).addToRequestQueue(createAGetUserProfileRequest(listener, id));
    }
    private GsonRequest<User> createAGetCurrentUserProfileRequest(Response.Listener<User> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<>(Request.Method.GET, act.getString(R.string.ws_getMyProfile), //HTTP Metodu, String dosyasından çekilen url
                new VolleyErrorListener(act.getApplicationContext()), //Bu sınıf istek gönderilirken bir hata olduysa nedenini yazdırıyor.
                User.class, headers, null, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
    private GsonRequest<User> createAGetUserProfileRequest(Response.Listener<User> listener, int userID){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<>(Request.Method.GET, act.getString(R.string.getUserProfile)+userID, //HTTP Metodu, String dosyasından çekilen url
                new VolleyErrorListener(act.getApplicationContext()), //Bu sınıf istek gönderilirken bir hata olduysa nedenini yazdırıyor.
                User.class, headers, null, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
}
