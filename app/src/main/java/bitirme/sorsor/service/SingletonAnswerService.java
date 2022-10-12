package bitirme.sorsor.service;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bitirme.sorsor.GsonRequest;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.JsonPaginator;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.Token;
import bitirme.sorsor.VolleySingleton;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.model.Answer;

/**
 * Created by mert on 01.05.2016.
 */
public class SingletonAnswerService {

    private List<Answer> listOfAnswers;
    private RecyclerPostsAdapter adapter;
    private RecyclerView rcV;

    public  SingletonAnswerService(RecyclerPostsAdapter rcycAdapter) {
        this.adapter = rcycAdapter;
        rcV = adapter.rc;
        listOfAnswers = adapter.answerList;
    }
    /*
    Soru silinirse true, silinemezse false
     */
    public boolean removeAnswerFromListByID(long answerID) {
        int indexOf = listOfAnswers.indexOf(new Answer(answerID));
        if (indexOf == -1)
            return false;
        else {
            listOfAnswers.remove(indexOf);
            adapter.notifyItemRemoved(indexOf);
            return true;
        }
    }

    public void addAnswerToList(Answer answer) {
        listOfAnswers.add(0, answer);
        adapter.notifyItemInserted(0);
        rcV.scrollToPosition(0);
    }

    public void removeAllAnswersFromList() {
        listOfAnswers.clear();
        adapter.notifyDataSetChanged();
    }

    public void addListOfAnswersToList(List<Answer> newList) {
        listOfAnswers.clear();
        listOfAnswers.addAll(newList);
        adapter.notifyDataSetChanged();
    }
}
