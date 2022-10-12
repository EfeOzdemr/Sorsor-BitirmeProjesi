
package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.victor.ringbutton.RingButton;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.Image.PictureFactory;
import bitirme.sorsor.listeners.PictureRingButtonListener;
import bitirme.sorsor.R;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Post;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import uk.co.senab.photoview.PhotoView;

/* Created by Mert & Caner */
public class Respond extends AppCompatActivity {
    private FloatingActionButton fab_respond;
    private PhotoView photo_respond;
    private EditText edit_respond;
    private RingButton ring_respond;
    private Question theQuestion;
    private User currentUser;
    private PictureFactory pF = new PictureFactory();
    private Answer newAnswer;
    private Photo photo;
    private Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        receiveIntents();
        setContentView(R.layout.activity_respond);
        fab_respond = (FloatingActionButton) findViewById(R.id.fab_respond);
        photo_respond = (PhotoView) findViewById(R.id.photo_respond);
        edit_respond = (EditText) findViewById(R.id.edit_respond);
        ring_respond = (RingButton) findViewById(R.id.ring_respond);
        fab_respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAnswer = new Answer(theQuestion, new Post(currentUser, edit_respond.getText().toString()));
                newAnswer.setPhoto(photo);
                postAnswer(newAnswer);
            }
        });
        ring_respond.setOnClickListener(new PictureRingButtonListener(this, pF));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void postAnswer(Answer newAnswer) {
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.setAction(getString(R.string.wsAc_postAnswer));
        i.putExtra("ANSWER", newAnswer);
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_postAnswer)));
    }

    private void receiveIntents() {
        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(getString(R.string.Question_Respond_newAnswer_question))) //Soru sorma ekranında geri dönüyor.
            {
                theQuestion = (Question) i.getSerializableExtra(getString(R.string.Question_Respond_newAnswer_question));
                currentUser = (User) i.getSerializableExtra(getString(R.string.Question_Respond_newAnswer_user));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Integer.parseInt(getString(R.string.wsRC_postAnswer))) {
                Intent i = new Intent(this, QuestionActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //yeni activity yaratmıyoruz eskisine dönüyoruz
                i.putExtra(getString(R.string.Respond_Question_newAnswer_answer), newAnswer);
                startActivity(i);
            } else {
                photo = pF.getPicture(getApplicationContext(), requestCode, resultCode, data);
                setVisibleViews();
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED){
            finish();
        }
    }

    private void setVisibleViews() {
        if (photo == null) {
            photo_respond.setVisibility(View.INVISIBLE);
            ring_respond.setVisibility(View.VISIBLE);
        } else {
            photo_respond.setVisibility(View.VISIBLE);
            photo_respond.setImageBitmap(photo.getPictureBitmap());
            ring_respond.setVisibility(View.INVISIBLE);
        }
    }
}
