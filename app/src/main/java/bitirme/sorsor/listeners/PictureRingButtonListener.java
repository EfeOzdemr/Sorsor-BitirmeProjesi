package bitirme.sorsor.listeners;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.victor.ringbutton.RingButton;

import bitirme.sorsor.Image.PictureFactory;

/**
 * Created by Efe on 30.04.2016.
 * Fotoğraf opsiyonu sürekli üstte olacak diye kabul ettik. Bu değiştirilebilir.
 *
 * */
public class PictureRingButtonListener implements RingButton.OnClickListener{
    private Fragment fg;
    private Activity act;
    private PictureFactory pF;

    public PictureRingButtonListener(Activity act, PictureFactory pF) {
        this.act = act;
        this.pF = pF;
    }

    public PictureRingButtonListener(Fragment fg, PictureFactory pF) {
        this.pF = pF;
        this.fg = fg;
    }

    @Override
    public void clickUp() { //fotoğraf
        if(fg != null)
            pF.startIntent(fg, pF.METHOD_CAMERA);
        else if(act != null)
            pF.startIntent(act, pF.METHOD_CAMERA);
    }
    @Override
    public void clickDown() { //galeri
        if(fg != null)
            pF.startIntent(fg, pF.METHOD_GALLERY);
        else if(act != null)
            pF.startIntent(act, pF.METHOD_GALLERY);
    }
}
