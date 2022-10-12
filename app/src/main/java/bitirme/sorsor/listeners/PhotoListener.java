package bitirme.sorsor.listeners;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.lang.reflect.Field;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.model.Post;
import bitirme.sorsor.model.User;

/**
 * Created by Efe on 13.05.2016.
 */
public class PhotoListener implements ImageLoader.ImageListener{
    public Object o;
    public ImageView iv;
    public PhotoListener(Object o, ImageView iv) {
        this.iv = iv;
        this.o = o;
    }
    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        Bitmap bmp = response.getBitmap();

        if(bmp == null)
            return;
        if(o instanceof Post){
            Post p = ((Post) o);
            if(p.getPhoto() == null)
                p.setPhoto(new Photo(bmp, response.getRequestUrl()));
            else{
                p.getPhoto().setUrl(response.getRequestUrl());
                p.getPhoto().setPictureBitmap(bmp);
            }

        }
        else if(o instanceof User){
            User u = ((User) o);
            if(u.getPhoto() == null){
                u.setPhoto(new Photo(bmp, response.getRequestUrl()));
            }
            else{
                u.getPhoto().setUrl(response.getRequestUrl());
                u.getPhoto().setPictureBitmap(bmp);
            }
        }
        iv.setImageBitmap(bmp);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("Volley Image", new String(error.networkResponse.data));
    }
}
