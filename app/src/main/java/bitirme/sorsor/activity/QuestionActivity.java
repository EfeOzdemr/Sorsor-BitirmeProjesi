package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import bitirme.sorsor.listeners.AppBarStateChangeListener;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import bitirme.sorsor.service.SingletonAnswerService;
import bitirme.sorsor.service.SingletonQuestionService;

/* Created by Mert & Efe & Caner */
public class QuestionActivity extends AppCompatActivity {
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton fab;

    public User currentUser;
    public Question theQuestion;
    public ArrayList<Answer> responses = new ArrayList<>();
    public SingletonAnswerService answerService;
    private RecyclerView recyclerView;
    private CardView qCv;
    private RecyclerPostsAdapter recyclerPostsAdapter;
    private AppBarStateChangeListener appBarStateChangeListener;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveIntents();
        setContentView(R.layout.activity_question);
        appBarLayout = (AppBarLayout) findViewById(R.id.topic_appBar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.topic_toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        fab = (FloatingActionButton) findViewById(R.id.topic_fabRespond);
        toolbar = (Toolbar) findViewById(R.id.topic_toolbar);
        setSupportActionBar(toolbar);
        setMenuIconBehaviour();
        setTitle(theQuestion.getTitle());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerQuestions);
        recyclerPostsAdapter = new RecyclerPostsAdapter(responses, recyclerView, Answer.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerPostsAdapter);
        qCv = (CardView) findViewById(R.id.cv);
        RecyclerPostsAdapter.PostVH holder = new RecyclerPostsAdapter().new PostVH(qCv, theQuestion); //Burada questionı aldık çünkü sayfayı kaldırırken placeholderlar çıkmasın.
        RecyclerPostsAdapter.bindViewHolderAndObject(holder, theQuestion);
        answerService = new SingletonAnswerService(recyclerPostsAdapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh); //Sayfa yenilemeleri için kullandığımız View'ımızı layout'tan aldık.
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAnswers();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Respond.class);
                i.putExtra(getString(R.string.Question_Respond_newAnswer_question), theQuestion);
                i.putExtra(getString(R.string.Question_Respond_newAnswer_user), currentUser);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra(getString(R.string.Respond_Question_newAnswer_answer))){
            Answer newAnswer = (Answer) intent.getSerializableExtra(getString(R.string.Respond_Question_newAnswer_answer));
            answerService.addAnswerToList(newAnswer);
            updateQuestions();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (appBarStateChangeListener.isExpanded() == true) { //Yukari ok
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_expand_less_black_24dp));
        } else if (appBarStateChangeListener.isExpanded() == false) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_expand) {
            appBarLayout.setExpanded(!appBarStateChangeListener.isExpanded());
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    private void updateQuestions() {
        getAnswers(); //WebServisin sorular bölümüne istek gönder.
        swipeRefresh.setRefreshing(false); //Dönen yuvarlağı kapat.
    }
    private void getAnswers(){
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.putExtra(getString(R.string.x_TopicActivity_newQuestion_question), theQuestion);
        i.setAction(getString(R.string.wsAc_getAnswers));
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_getAnswers)));
    }
    public void setMenuIconBehaviour(){
        appBarStateChangeListener = new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if(state == State.EXPANDED || state == State.COLLAPSED)
                    invalidateOptionsMenu();
            }
        };
        appBarLayout.addOnOffsetChangedListener(appBarStateChangeListener);
    }
    public void receiveIntents() {
        Intent i = getIntent();
        if (i != null) {
            if (i.hasExtra(getString(R.string.x_TopicActivity_newQuestion_question))) //Anasayfadan gelme mesela?
            {
                currentUser = (User) i.getSerializableExtra(getString(R.string.x_TopicActivity_newQuestion_user));
                theQuestion = (Question) i.getSerializableExtra(getString(R.string.x_TopicActivity_newQuestion_question));
                getResponses(i);
            }
            if(i.hasExtra(getString(R.string.x_TopicActivity_newQuestion_answers))){
                responses = (ArrayList<Answer>)i.getSerializableExtra(getString(R.string.x_TopicActivity_newQuestion_answers));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Integer.parseInt(getString(R.string.wsRC_getAnswers))) {
            if(resultCode == Activity.RESULT_OK){
                responses = (ArrayList<Answer>) data.getSerializableExtra("ANSWERS");
                answerService.addListOfAnswersToList(responses);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
    public void getResponses(Intent intent) {
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.setAction(getString(R.string.wsAc_getAnswers));
        i.putExtras(intent);
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_getAnswers)));
    }
}
