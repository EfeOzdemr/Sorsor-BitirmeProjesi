package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import bitirme.sorsor.GsonRequest;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.JsonPaginator;
import bitirme.sorsor.R;
import bitirme.sorsor.Token;
import bitirme.sorsor.VolleySingleton;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Post;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import uk.co.senab.photoview.PhotoViewAttacher;

/* Created by Mert */
public class LoadingScreenActivity extends Activity {
    private String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        receiveIntent(getIntent());
    }
    private void receiveIntent(Intent i) {
        action = i.getAction();
        if(action.contentEquals(getString(R.string.wsAc_login))){
            Map<String, String> params = (Map<String, String>) i.getSerializableExtra("1");
            Request<Token> loginRequest = createALoginRequest(new Response.Listener<Token>() {
                @Override
                public void onResponse(Token response) {
                    SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(getString(R.string.shared_key), Context.MODE_PRIVATE).edit();
                    editor.putString(getString(R.string.auth_token), response.getToken());
                    editor.apply();
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }, params);
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);
        }
        else if(action.contentEquals(getString(R.string.wsAc_getMyProfile))){
            Request<User> myProfileRequest = createAGetCurrentUserProfileRequest(new Response.Listener<User>() {
                @Override
                public void onResponse(User response) {
                    Intent i = new Intent();
                    i.putExtra("USER", response);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(myProfileRequest);
        }
        else if(action.contentEquals(getString(R.string.wsAc_getAnasayfa))){
            Request<JsonPaginator.QuestionPage> questionIndexRequest = createQuestionIndexRequest(new Response.Listener<JsonPaginator.QuestionPage>() {
                @Override
                public void onResponse(JsonPaginator.QuestionPage response) {
                    Intent i = new Intent();
                    i.putExtra("QUESTIONS", (Serializable) response.getData());
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionIndexRequest);
        }
        else if(action.contentEquals(getString(R.string.wsAc_getAnswers))){
            Question q = (Question) i.getSerializableExtra(getString(R.string.x_TopicActivity_newQuestion_question));
            Request<JsonPaginator.AnswerPage> questionResponsesIndex = createAGetAnswersRequest(q,
                    new Response.Listener<JsonPaginator.AnswerPage>() {
                        @Override
                        public void onResponse(JsonPaginator.AnswerPage response) {
                            Intent i = new Intent();
                            i.putExtra("ANSWERS", (Serializable) response.getData());
                            setResult(Activity.RESULT_OK, i);
                            finish();
                        }
                    }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionResponsesIndex);
        }
        else if(action.contentEquals(getString(R.string.wsAc_postAnswer))){
            Answer ans = (Answer) i.getSerializableExtra("ANSWER");
            Request<Void> postAnswerRequest = createAnswerStoreRequest(ans, new Response.Listener<Void>() {
                @Override
                public void onResponse(Void response) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postAnswerRequest);
        }
        else if(action.contentEquals(getString(R.string.wsAc_showSomeone))){
            User u = (User) i.getSerializableExtra("USER");
            Request<User> userProfileRequest = createAGetUserProfileRequest(u, new Response.Listener<User>() {
                @Override
                public void onResponse(User response) {
                    Intent i = new Intent();
                    i.putExtra("USER", response);
                    fixAuthorProperty(response);
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }
                //Json Userla beraber çekmiyor.
                private void fixAuthorProperty(User response) {
                    for(Answer ans : response.getAnswers().getData()){
                        ans.setAuthor(response);
                    }
                    for(Question que : response.getQuestions().getData()){
                        que.setAuthor(response);
                    }
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(userProfileRequest);
        }
        else if(action.contentEquals(getString(R.string.wsAc_postQuestion))){
            Question q = (Question) i.getSerializableExtra("QUESTION");
            Request<Void> postQuestionReq = createQuestionStoreRequest(q, new Response.Listener<Void>() {
                @Override
                public void onResponse(Void response) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(postQuestionReq);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        setResult(Activity.RESULT_CANCELED, i);
        finish();
    }

    /*
    Login için GsonRequest sınıfından bir request oluşturuyoruz.
     */
    private GsonRequest<Token> createALoginRequest(Response.Listener<Token> listener, Response.ErrorListener eListener, Map<String, String> params){
        return new GsonRequest<>(Request.Method.POST, getString(R.string.ws_login), //HTTP Metodu, String dosyasından çekilen url
                eListener,
                Token.class, null, params, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
    private GsonRequest<JsonPaginator.QuestionPage> createQuestionIndexRequest(Response.Listener<JsonPaginator.QuestionPage> listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<JsonPaginator.QuestionPage>(Request.Method.GET, getString(R.string.ws_getAnasayfa),
                new VolleyErrorListener(this),
                JsonPaginator.QuestionPage.class, headers, null, listener);
    }
    private GsonRequest<User> createAGetCurrentUserProfileRequest(Response.Listener<User> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<>(Request.Method.GET, getString(R.string.ws_getMyProfile), //HTTP Metodu, String dosyasından çekilen url
                new VolleyErrorListener(getApplicationContext()), //Bu sınıf istek gönderilirken bir hata olduysa nedenini yazdırıyor.
                User.class, headers, null, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
    private GsonRequest<User> createAGetUserProfileRequest(User u, Response.Listener<User> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<>(Request.Method.GET, getString(R.string.getUserProfile)+u.getUserID(), //HTTP Metodu, String dosyasından çekilen url
                new VolleyErrorListener(getApplicationContext()), //Bu sınıf istek gönderilirken bir hata olduysa nedenini yazdırıyor.
                User.class, headers, null, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
    private GsonRequest<JsonPaginator.AnswerPage> createAGetAnswersRequest(Question q, Response.Listener<JsonPaginator.AnswerPage> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        return new GsonRequest<>(Request.Method.GET, getString(R.string.ws_getAnswers)+q.getId(), //HTTP Metodu, String dosyasından çekilen url
                new VolleyErrorListener(getApplicationContext()), //Bu sınıf istek gönderilirken bir hata olduysa nedenini yazdırıyor.
                JsonPaginator.AnswerPage.class, headers, null, listener); //Sırasıyla, (cevabın hangi nesneye deserilize edileceği, http isteğin header parametresi, isteğin body parametreleri, cevabı dönmesini dinleyen listener)
    }
    private GsonRequest<Void> createAnswerStoreRequest(Answer answer, Response.Listener<Void> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        Map<String, String> body = new HashMap<>();
        body.put("question_id", answer.getRelatedQuestion().getId()+"");
        body.put("description", answer.getDescription());
        String base64 = Base64.encodeToString(ImageProcess.compressImage(answer.getPhoto().getPicturePath()), Base64.DEFAULT);
        body.put("base64", base64);
        return new GsonRequest<Void>(Request.Method.POST, getString(R.string.ws_postAnswer),
                new VolleyErrorListener(this),
                Void.class, headers, body, listener);
    }
    private GsonRequest<Void> createQuestionStoreRequest(Question question, Response.Listener<Void> listener){
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", Token.getInstance().getToken());
        Map<String, String> body = new HashMap<>();
        body.put("user_id", question.getAuthor().getUserID()+"");
        body.put("course_id", question.getCourseID().getCourseID()+"");
        body.put("title", question.getTitle());
        body.put("description", question.getDescription());
        //TODO: WEB SERVİSTE FOTOĞRAF YÜKLEME.
        String base64 = Base64.encodeToString(ImageProcess.compressImage(question.getPhoto().getPicturePath()), Base64.DEFAULT);
        body.put("base64", base64);
        return new GsonRequest<Void>(Request.Method.POST, getString(R.string.ws_postQuestion),
                new VolleyErrorListener(this),
                Void.class, headers, body, listener);
    }
}
