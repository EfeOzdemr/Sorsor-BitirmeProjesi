package bitirme.sorsor;

import android.util.Log;
import android.widget.ImageView;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.listeners.PhotoListener;
import bitirme.sorsor.model.Post;
import bitirme.sorsor.model.User;

/**
 * Created by Efe on 13.05.2016.
 */
public class PhotoDownloader {
    public static boolean checkUrl(String url){
        try {
            URL u = new URL(url);
            u.toURI();
            return true;
        } catch (Exception e){
            return false;
        }
    }
    public static void downloadPhoto(String url, Post p, ImageView iv){
        if(checkUrl(url) == false)
            return;
        VolleySingleton.getInstance(null).getImageLoader().get(url, new PhotoListener(p, iv));
    }
    public static void downloadPhoto(String url, User u, ImageView iv){
        if(checkUrl(url) == false)
            return;

        VolleySingleton.getInstance(null).getImageLoader().get(url, new PhotoListener(u, iv));
    }


}
