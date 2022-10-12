package bitirme.sorsor.Image;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import bitirme.sorsor.ImageProcess.ImageProcess;

/**
 * Created by mert on 29.04.2016.
 * Daha önceki versiyonlarda soruyla alakalı resimler ilgili model sınıflarında pictureBitmap, picturePath şeklinde saklanıyordu.
 * Kullanıcıdan resim isteme işi için bir factory pattern uygulayan bir sınıf yazmak istedim. O sınıfın üreteceği nesne bu olacak.
 *
 * Activityler arası geçişte bitmap'leri büyük boyutlarından ötürü taşımak zor olduğundan serializasyon işlemine sokmuyoruz (transient keywordüyle)
 * Fakat dosyanın path'i serilize ediliyor. Her deserilizasyonda bu path'ten dosyayı bulup, bitmap nesnesini oluşturuyoruz.
 */

public class Photo implements Serializable{
    private transient Bitmap pictureBitmap;
    private String picturePath;
    private String url;
    private transient Context mContext;

    public Photo(Context context, String path) { //Fotoğraf constr.
        this.mContext = context;
        setPicturePath(path);
        setPictureBitmap(ImageProcess.decodePicFromPath(path));
    }
    public Photo(Context context, Uri path){ //Galeri const.
        this.mContext = context;
        if(path == null)
            throw new NullPointerException();
        else{
            setPicturePath(getRealPathFromURI(path));
            setPictureBitmap(ImageProcess.decodePicFromPath(picturePath));
        }
    }
    public Photo(Bitmap bmp, String url){ //Geçici bir constr. db işleri bitince kalkmalı. path'e ulaşamıyoruz.
        setUrl(url);
        setPictureBitmap(bmp);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public Bitmap getPictureBitmap() {
        return pictureBitmap;
    }

    public void setPictureBitmap(Bitmap pictureBitmap) {
        if(pictureBitmap == null)
            return;
        this.pictureBitmap = pictureBitmap;
        if(getPicturePath() == null){
            setPicturePath(ImageProcess.saveBitmapToAFile(pictureBitmap, getUrl()).getAbsolutePath());
        }
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
        if(pictureBitmap == null){
            handleTransientFields();
        }
    }

    private void handleTransientFields() {
        if (getPicturePath() != null) {
            setPictureBitmap(ImageProcess.decodePicFromPath(getPicturePath()));
        }
    }
    /* Bitmap nesneleri Parcelableı implement ettiği için serializationda sıkıntı yaratıyordu.
    Bu yüzden resimlerin pathlerini de saklamak istedik ki bu geçişlerde bu pathleri koyalım.
    Lazım olduğunda da path'ten resmi okuyup bitmap nesnesini yaratabilelim.
    Serialization işleminde nesne her oluşturulduğunda readObject sınıfı çağırılıyor.
    Biz bunu defaultReadObject metoduna gönderiyoruz. Böylece nesnemiz transient sınıflar dışında
    oluşmuş oluyor. Ardından handleTransientFields metoduyla işimizi hallediyoruz.
 */
    private void readObject(ObjectInputStream s)
            throws IOException {
        try {
            s.defaultReadObject();
            handleTransientFields();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
