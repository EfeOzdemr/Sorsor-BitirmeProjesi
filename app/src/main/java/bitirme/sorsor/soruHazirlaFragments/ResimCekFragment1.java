package bitirme.sorsor.soruHazirlaFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.victor.ringbutton.RingButton;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.Image.PictureFactory;
import bitirme.sorsor.listeners.PictureRingButtonListener;
import bitirme.sorsor.R;
import bitirme.sorsor.model.Question;

/**
 * Created by Efe on 02.03.2016.
 */
public class ResimCekFragment1 extends Fragment implements SoruHazirlamaInterface {
    public static String pageTitle = "Fotoğraf";
    private RingButton ringButton;

    private Photo pct;


    private Question newQuestion;
    private PictureFactory pF = new PictureFactory();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml

        View view = inflater.inflate(R.layout.resim_cek_fragment1, container, false);
        ringButton = (RingButton) view.findViewById(R.id.ringButton);
        ringButton.setOnClickListener(new PictureRingButtonListener(this, pF));
        Bundle bundle = getArguments();
        newQuestion = (Question) bundle.getSerializable(getString(R.string.SoruHazirlama_SoruPreview1_renderNewQuestion_question));
        return view;
    }


    @Override
    public void setQuestionParams() {
        if(pct!=null)
            newQuestion.setPhoto(new Photo(getContext(), pct.getPicturePath()));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pct = pF.getPicture(getContext(), requestCode, resultCode, data);
    }

    @Override
    public Question getQuestion() {
        return newQuestion;
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }




}

