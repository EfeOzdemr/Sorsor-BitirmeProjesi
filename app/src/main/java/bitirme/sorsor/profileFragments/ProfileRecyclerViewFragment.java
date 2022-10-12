package bitirme.sorsor.profileFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Question;

/**
 * Created by mert on 08.03.2016.
 */
public class ProfileRecyclerViewFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerPostsAdapter recyclerPostsAdapter;
    private List posts;
    private Class clazz;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_anasayfa, container, false);
        getList();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPostsAdapter = new RecyclerPostsAdapter(posts, recyclerView, clazz);
        recyclerView.setAdapter(recyclerPostsAdapter);
        return view;
    }
    public void notifyAdapter(List list){
        posts = list;
        if(clazz == Question.class){
            recyclerPostsAdapter.questionList.clear();
            recyclerPostsAdapter.questionList.addAll(list);
            recyclerPostsAdapter.notifyDataSetChanged();
        }
        else if(clazz == Answer.class){
            recyclerPostsAdapter.answerList.clear();
            recyclerPostsAdapter.answerList.addAll(list);
            recyclerPostsAdapter.notifyDataSetChanged();
        }
    }
    private void getList() {
        Bundle bundle = getArguments();
        boolean isItQuestions = bundle.getBoolean(getString(R.string.Profile_Profile_isQuestionList_boolean));
        if(isItQuestions == true)
        {
            posts = (List<Question>) bundle.getSerializable(getString(R.string.Profile_Profile_getList_postsOfQuestions));
            clazz = Question.class;
        }
        else{
            posts = (List<Answer>) bundle.getSerializable(getString(R.string.Profile_Profile_getList_postsOfQuestions));
            clazz = Answer.class;
        }
    }

}
