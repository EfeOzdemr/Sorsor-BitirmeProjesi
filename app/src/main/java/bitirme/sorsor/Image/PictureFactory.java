package bitirme.sorsor.Image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import bitirme.sorsor.ImageProcess.ImageProcess;

/**
 * Created by mert on 29.04.2016.
 *
 * Photo Factory kullanım:
 * 1- Nesne yaratılır.
 * 2- Kamera veya galeriye geçiş yapılması istenen yerde startIntent'in uygun metodu çağrılır.
 * Method olarak METHOD_CAMERA veya METHOD_GALLERY seçilir.
 * 3- OnActivityResult metodu override edilir. getPhoto ile kullanıcının seçtiği resim alınır.
 *
 * */
public class PictureFactory  {

    public static final String METHOD_CAMERA = MediaStore.ACTION_IMAGE_CAPTURE;
    public static final String METHOD_GALLERY = Intent.ACTION_PICK;
    private int REQ_RESULT = -1; //galeri 0, kamera 1
    private String filePath;

    /* Method yerine yazılabilecek şeyler:
    Kamera için: MediaStore.ACTION_IMAGE_CAPTURE
    Galeri için: Intent.ACTION_PICK
     */
    public void startIntent(Activity act, String method){
        Intent picPickerIntent = createIntent(method);
        act.startActivityForResult(picPickerIntent, REQ_RESULT);
    }
    public void startIntent(Fragment fragment, String method){
        Intent picPickerIntent = createIntent(method);
        fragment.startActivityForResult(picPickerIntent, REQ_RESULT);
    }
    private Intent createIntent(String method){
        Intent picPickerIntent;
        if(method.contentEquals(METHOD_CAMERA) == true){
            REQ_RESULT = 1;
            picPickerIntent = new Intent(METHOD_CAMERA);
            File imageFile = createImageFile();
            if(imageFile != null){
                picPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(imageFile));
                filePath = imageFile.getAbsolutePath();
            }
        }
        else if(method.contentEquals(METHOD_GALLERY) == true){
            REQ_RESULT = 0;
            picPickerIntent = new Intent(METHOD_GALLERY);
            picPickerIntent.setType("image/*");
        }
        else {
            throw new IllegalArgumentException();
        }
        if(picPickerIntent != null)
            return picPickerIntent;
        else
            return null;
    }

    private File createImageFile(){
        // Create an image file name
        String imageFileName = ImageProcess.getPhotoFilename();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Photo getPicture(Context ctx, int requestCode, int resultCode, Intent data) {
        Photo pict=null;
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            pict = new Photo(ctx, filePath);
        } else if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            pict = new Photo(ctx, selectedImage);
        }
        return pict;
    }
}
