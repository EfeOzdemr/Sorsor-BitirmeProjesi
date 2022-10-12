package bitirme.sorsor.soruHazirlaFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bitirme.sorsor.R;
import bitirme.sorsor.model.Question;

/**
 * Created by Efe on 02.03.2016.
 */
public class SoruProperty1 extends Fragment implements SoruHazirlamaInterface {
    public static String pageTitle = "Soru";

    private TextView newQuestionTitle;
    private TextView newQuestionDescription;
    private Question newQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.soru_property_fragment1, container, false);
        newQuestionTitle = (TextView) view.findViewById(R.id.newQuestionTitle);
        newQuestionDescription = (TextView) view.findViewById(R.id.newQuestionDescription);
        Bundle bundle = getArguments();
        newQuestion = (Question) bundle.getSerializable(getString(R.string.SoruHazirlama_SoruPreview1_renderNewQuestion_question));
        return view;
    }

    public String getTitle() {
        return newQuestionTitle.getText().toString();
    }

    public String getDescription() {
        return newQuestionDescription.getText().toString();
    }

    @Override
    public void setQuestionParams() {
        newQuestion.setTitle(getTitle());
        newQuestion.setDescription(getDescription());
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
