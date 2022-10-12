package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import bitirme.sorsor.R;
import bitirme.sorsor.model.Post;
import uk.co.senab.photoview.PhotoViewAttacher;

/* Created by Efe*/
public class PostViewingActivity extends Activity {
    private Post thePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_viewing);
        receiveIntents();
        ImageView photo = (ImageView) findViewById(R.id.postPhoto);
            photo.setImageBitmap(thePost.getPhoto().getPictureBitmap());
        PhotoViewAttacher attacher = new PhotoViewAttacher(photo);
    }

    public void receiveIntents() {

        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(getString(R.string.x_PhotoViewing_showPhoto_question))) //Soru sorma ekranında geri dönüyor.
            {
                thePost = (Post) i.getSerializableExtra(getString(R.string.x_PhotoViewing_showPhoto_question));
            }
        }
    }
}
