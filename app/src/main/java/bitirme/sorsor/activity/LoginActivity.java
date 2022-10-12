package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import bitirme.sorsor.GsonRequest;
import bitirme.sorsor.R;
import bitirme.sorsor.Token;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.VolleySingleton;
import bitirme.sorsor.model.User;
import bitirme.sorsor.service.SingleetonUserService;

/* Created by Mert & Caner */
public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView emailEdit;
    private EditText passEdit;
    private Button signIn;
    private Button signUp;
    private ProgressBar loginProgress;
    public static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //Email için regex pattern'i
    private Pattern emailPattern;
    public SharedPreferences sharedPref; //Tokenı kaydetmek için kullandığımız SharedPreferences
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Application Context'i kullanarak SharedPreference handle'ı aldık.
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.shared_key), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getString(R.string.auth_token), null); //Daha önce kaydedilmiş bir token var mı? Eğer yoksa token local değişkeni null.
        findViews(); //View'ları layout'tan çeken, çeşitli listenerları ilişkilendiren metod.
        if(token != null)
        {
            Token.getInstance().setToken(token); //Token eğer varsa, kullanıcı daha önce giriş yapmış demektir. O yüzden login ekranına gerek yok.
            startAnasayfaActivity();
        }
        emailPattern = Pattern.compile(EMAIL_REGEX); //Kullanıcı giriş yapmamış, regex pattern'ini derleyelim çünkü kullanacağız.
    }
    public void startAnasayfaActivity(){
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.setAction(getString(R.string.wsAc_getMyProfile));
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_getMyProfile)));
    }
    private void findViews(){
        emailEdit = (AutoCompleteTextView) findViewById(R.id.email);
        passEdit = (EditText) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.email_sign_in_button);
        signUp = (Button) findViewById(R.id.sign_up_button);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        ButtonListener btnLstnr = new ButtonListener(); //Login ve register butonu için listenerlar.
        signIn.setOnClickListener(btnLstnr);
        signUp.setOnClickListener(btnLstnr);
    }
    /*
    Caner bu not sana:
    Bu activity'i ide'den sağ tık new login activity diyerek oluşturdum.
    içeriğinde bir sürü metod vardı. biraz temizledim. attemt logini bu şekilde bıraktım.
    aşağıdaki yorumlar o yüzden ingilizce onları türkçeye çevirip koyabilirsin.
    RequestFocus, o edittext'e odaklanmasını sağlıyor. imleci oraya getiriyo. mesela emaili yanlıi girdin.
    emaili yanlış girdin diyip imleci direk oraya götürüyo.
     */

    private void attemptLogin() {
        // Eğer bir hata mesajı yazdırdıysak silelim.
        emailEdit.setError(null);
        passEdit.setError(null);
        // Store values at the time of the login attempt.
        String email = emailEdit.getText().toString();
        String password = passEdit.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passEdit.setError(getString(R.string.error_invalid_password));
            focusView = passEdit;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError(getString(R.string.error_field_required));
            focusView = emailEdit;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEdit.setError(getString(R.string.error_invalid_email));
            focusView = emailEdit;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Intent i = new Intent(this, LoadingScreenActivity.class);
            Map<String, String> params = new HashMap<>();
            params.put("email", emailEdit.getText().toString());
            params.put("password", passEdit.getText().toString());
            i.putExtra("1", (Serializable) params);
            i.setAction(getString(R.string.wsAc_login));
            startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_login)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Integer.parseInt(getString(R.string.wsRC_login))) {
            if(resultCode == Activity.RESULT_OK){
                startAnasayfaActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Girdiğiniz bilgiler hatalı", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == Integer.parseInt(getString(R.string.wsRC_getMyProfile))){
            if(resultCode == Activity.RESULT_OK){
                Intent i = new Intent(this, Anasayfa.class);
                i.putExtras(data);
                i.setAction(getString(R.string.Login_Anasayfa_getUser_user));
                finish(); //Login activity'sine ihtiyacımız kalmadı.
                startActivity(i);  //Anasayfa activitysini başlatalım.
            }
        }
    }
    private boolean isEmailValid(String email) {
        return emailPattern.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /*Bu aşağısını alma değişecek biraz.*/
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.sign_up_button:{
                    break;
                }
                case R.id.email_sign_in_button:{
                    attemptLogin();
                }
            }
        }
    }
}
