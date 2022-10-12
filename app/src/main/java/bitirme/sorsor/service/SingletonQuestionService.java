package bitirme.sorsor.service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bitirme.sorsor.GsonRequest;
import bitirme.sorsor.JsonPaginator;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.Token;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.VolleySingleton;
import bitirme.sorsor.model.Question;

/**
 * Created by mert on 05.03.2016.
 */
public class SingletonQuestionService {
    private List<Question> listOfQuestions;
    private RecyclerPostsAdapter adapter;
    private RecyclerView rcV;
    public SingletonQuestionService(RecyclerPostsAdapter rcycAdapter) {
        this.adapter = rcycAdapter;
        rcV = adapter.rc;
        listOfQuestions = adapter.questionList;
    }

    /*
    Soru silinirse true, silinemezse false
     */
    public boolean removeQuestionFromListByID(int questionID) {
        int indexOf = listOfQuestions.indexOf(new Question(questionID));
        if (indexOf == -1)
            return false;
        else {
            listOfQuestions.remove(indexOf);
            adapter.notifyItemRemoved(indexOf);
            return true;
        }
    }


    public void addQuestionToList(Question question) {
        listOfQuestions.add(0, question);
        adapter.notifyItemInserted(0);
        rcV.scrollToPosition(0);

    }

    public void removeAllQuestionsFromList() {
        listOfQuestions.clear();
        adapter.notifyDataSetChanged();
    }

    public void addListOfQuestionsToList(List<Question> newList) {
        removeAllQuestionsFromList();
        listOfQuestions.addAll(newList);
        adapter.notifyDataSetChanged();
    }
}
