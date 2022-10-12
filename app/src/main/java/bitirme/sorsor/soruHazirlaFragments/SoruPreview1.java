package bitirme.sorsor.soruHazirlaFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bitirme.sorsor.activity.Anasayfa;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.model.Question;

/**
 * Created by mert on 02.03.2016.
 */
public class SoruPreview1 extends Fragment implements SoruHazirlamaInterface {
    public static String pageTitle = "Önizleme";

    private CardView newQuestionPreviewCard;
    private Question theQuestion;
    private RecyclerPostsAdapter.PostVH holder;
    private FloatingActionButton soruTamamFab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.soru_preview_fragment1, container, false);
        newQuestionPreviewCard =  (CardView) view.findViewById(R.id.newQuestionCardLayout);
        soruTamamFab = (FloatingActionButton) view.findViewById(R.id.soruTamamFab);
        Bundle bundle = getArguments();
        theQuestion = (Question) bundle.getSerializable(getString(R.string.SoruHazirlama_SoruPreview1_renderNewQuestion_question));
        holder = new RecyclerPostsAdapter().new PostVH(newQuestionPreviewCard, theQuestion); //Burada questionı aldık çünkü sayfayı kaldırırken placeholderlar çıkmasın.
        soruTamamFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Anasayfa.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP); //yeni activity yaratmıyoruz eskisine dönüyoruz
                i.putExtra(getString(R.string.SoruPreview1_Anasayfa_newQuestion_question), theQuestion);
                startActivity(i);
            }
        });
        return view;
    }


    public void initializeCard() {
    try {
        holder.postTitle.setText(theQuestion.getTitle());
        holder.askerName.setText(theQuestion.getAuthor().getName() + ":");
        holder.askerPhoto.setImageBitmap(theQuestion.getAuthor().getPhoto().getPictureBitmap());
        //TODO: TIMEAGOYU HALLET
        holder.postDescription.setText(theQuestion.getShortDescription());
        holder.postPhoto.setImageBitmap(theQuestion.getPhoto().getPictureBitmap());
        holder.postLikeCount.setText("" + theQuestion.getLikeCount());
        holder.questionAnswerCount.setText("" + theQuestion.getAnswerCount());
        }
    catch (NullPointerException e){
    }
    }


    @Override
    public void setQuestionParams() {
    } //Preview'da setQuestionParam olmayacak

    @Override
    public Question getQuestion() { //Biraz gereksiz.
        return theQuestion;
    }

    @Override
    public String getPageTitle() { //Preview yazısını döndürüyo
        return pageTitle;
    }
}
