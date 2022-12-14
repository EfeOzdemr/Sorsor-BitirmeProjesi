package bitirme.sorsor.activity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import java.util.ArrayList;
import java.util.List;
import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.ItemDecorator;
import bitirme.sorsor.PhotoDownloader;
import bitirme.sorsor.R;
import bitirme.sorsor.RecyclerPostsAdapter;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import bitirme.sorsor.service.SingleetonUserService;
import bitirme.sorsor.service.SingletonQuestionService;
import de.hdodenhof.circleimageview.CircleImageView;


/* Created by Mert & Efe & Caner */
public class Anasayfa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //Layouts
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private SwipeRefreshLayout swipeRefresh;

    private TextView txtName;
    private TextView txtUserName;
    private CircleImageView crcProfilePic;
    private RecyclerView recyclerQuestions;
    private RecyclerPostsAdapter rcycQuestionsAdapter;
    private RecyclerView.LayoutManager rcyclerQuestionsLayoutManager;

    private SingletonQuestionService qS;
    private List<Question> soruList;

    private User currentUser;
    //GE????C?? KOD
    User asker;

    //GE????C?? KOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Toolbar'?? layout'tan bul.
        setSupportActionBar(toolbar);
        receiveIntents(getIntent());
        recyclerQuestions = (RecyclerView) findViewById(R.id.recyclerQuestions);  //RecyclerView'?? layout'tan bul
        rcyclerQuestionsLayoutManager = new LinearLayoutManager(this); //RecyclerView i??in Linear layout manager olu??turuyoruz.
        recyclerQuestions.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        recyclerQuestions.setLayoutManager(rcyclerQuestionsLayoutManager); //Yukar??daki Layout Manager'??-
        recyclerQuestions.addItemDecoration(new ItemDecorator(50));        //RecyclerView??n manager'?? olarak kullan
        /*
        RecyclerPostsAdapter ile ilgili a????klama:
            Model s??n??flar??m??z olan Question ve Answer s??n??flar?? bir ??ok ??zelli??ini Post s??n??f??ndan al??yor.
            Ve bu s??n??flardan olu??an ArrayListleri uygulaman??n ??e??itli yerlerindeki RecyclerViewlarda g??sterebilmek i??in
            Adapter s??n??f?? yazmam??z gerekiyordu. Biz de bu i?? i??in RecyclerPostsAdapter s??n??f??n?? yazd??k.
            A??a????da kullan??lan constructor ????yle:
            RecyclerPostsAdapter(RecyclerViewda g??z??kecek liste, RecyclerView nesnesi, Listenin ne tipten veriler i??erece??i)
            ??uanl??k bo?? liste veriyoruz ????nk?? hen??z web servise istek atmad??k.
         */
        rcycQuestionsAdapter = new RecyclerPostsAdapter(currentUser, new ArrayList(), recyclerQuestions, Question.class);
        qS = new SingletonQuestionService(rcycQuestionsAdapter);
        recyclerQuestions.setAdapter(rcycQuestionsAdapter); //RecyclerView'??n adapter??n?? ayarlad??k.

        swipeRefresh = (SwipeRefreshResponder) findViewById(R.id.swipeRefresh); //Sayfa yenilemeleri i??in kulland??????m??z View'??m??z?? layout'tan ald??k.
        swipeRefresh.setOnRefreshListener(new SwipeRefreshResponder()); //Refresh edilmesi halinde ??u yazd??????m??z Listener s??n??f??n??n ??al????mas??n?? istiyoruz dedik.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sorSorFab); //Yeni soru ekleme FAB'ini layout'tan ald??k.
        fab.setOnClickListener(new View.OnClickListener() { //Fab'e t??kland??????nda ne olaca????n?? bu anonim s??n??fta yaz??yoruz.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SoruHazirlama.class); //Soru haz??rlama activitysine ge??i?? i??in yeni intent
                /*
                Activity ge??i??lerinde kullan??lan key'ler ile ilgili a????klama:
                    Uygulama i??inde bir ??ok activity ge??isi olaca????n?? tahmin etti??imiz i??in kullan??lacak key'leri
                    bir sisteme ba??lamak istedik. Bu y??zden resource klas??r??ndeki String dosyas??na yazd??k.
                    Yani string dosyas??ndaki key/value k??sm??ndaki value, bizim intent ge??i??lerinde kulland??????m??z key'imiz oluyor.
                    Ona da a??a????daki ??ekilde ula????yoruz:
                    String dosyas??ndaki key k??sm??n??n yap??s?? ????yle:
                    ??uankiActivityAd??_GidilecekActivityAd??_Eylem??le??lgiliA????klama_Ta????nanNesne
                 */
                i.putExtra(getString(R.string.Anasayfa_SoruHazirlama_newQuestion_user), currentUser);
                startActivity(i); //SoruHaz??rlama activity'sini ba??lat.
            }
        });
        /*Ge??ici Kod
        * Buralar raporda olmas??n.
        * */

        /*Ge??ici kod bitti*/


        /*

         A??a????daki ge??ici kodlar??n yerine kodlar?? yazd??m ama yine de silmiyorum.


         soruList = new ArrayList<Question>();
        asker = new User("??rnek Ad", "Adam");
        asker.setUsername("mert95");

        Bitmap soru = ImageProcess.getBitmapFromDrawable(getApplicationContext(), R.drawable.geo1);
        Photo soruP = new Photo(soru);

        asker.setPhoto(avatar);
        //TODO: GE????C?? KOD BUNLAR HEP ??EK??LECEK ALINACAK,

        for (int i = 0; i < 15; i++) {
            Question aQuestion = new Question(i, "??rnek Title " + i, "??rnek Description " + i, asker);
            if (i == 0) {
                for (int j = 0; j < 10; j++) {
                    Post aPost=new Post(currentUser, "??rnek Desc" + j);
                    aPost.setLikeCount(j * 10);
                    aPost.setPhoto(soruP);
                    aQuestion.addResponse(new Answer(aQuestion, aPost));

                }
            }
            aQuestion.setAnswerCount(403);
            aQuestion.setPhoto(avatarP);
            aQuestion.setLikeCount(43);
            soruList.add(aQuestion);
        }


       */


        /*
        Bu a??a????da olanlar
        Drawer (Soldan ??ekmeli men??) ile ilgili olan kodlar??n tamam??. Drawer i??eri??i gibi ??eyleri ayarlad??k.
         */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeUIContents(currentUser);

    }//end of oncreate

    private void receiveIntents(Intent intent) {
        if(intent.getAction().contentEquals(getString(R.string.Login_Anasayfa_getUser_user)))
        {
            currentUser = (User) intent.getSerializableExtra("USER");
            getQuestions();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Integer.parseInt(getString(R.string.wsRC_getAnasayfa))) {
            if(resultCode == Activity.RESULT_OK){
                qS.addListOfQuestionsToList((List<Question>) data.getSerializableExtra("QUESTIONS"));
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
        if(requestCode == Integer.parseInt(getString(R.string.wsRC_postQuestion))){
            if (resultCode == Activity.RESULT_OK){
                updateQuestions();
            }
        }
    }



    private void initializeUIContents(User currentUser) {
        View header = navigationView.getHeaderView(0);
        txtName = (TextView) header.findViewById(R.id.txtName);
        txtUserName = (TextView) header.findViewById(R.id.txtUsername);
        crcProfilePic = (CircleImageView) header.findViewById(R.id.crcUserProfilePic);
        txtName.setText(currentUser.getName() + " " + currentUser.getSurname());
        txtUserName.setText(currentUser.getUsername());
        PhotoDownloader.downloadPhoto(currentUser.getProfilePic(), currentUser, crcProfilePic);
    }

    /*
    Back butonuna bast??????nda Drawer'?? kapatmas??n?? istedi??imiz i??in bu metodu override ettik.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            updateQuestions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_anasayfa, menu);
        return true;
    }
    /*
    UpdateQuestions:
        Bu metod iki ??ekilde tetiklenebilir:
        1-Kullan??c?? ekran?? yukar?? do??ru ??ekerse,
        2-Kullan??c?? men??den yenile tu??una basars
        3-Her birka?? dakikada bir -- HEN??Z GER??EKLE??T??R??LMED?? --
     */
    private void updateQuestions() {
        getQuestions(); //WebServisin sorular b??l??m??ne istek g??nder.
        swipeRefresh.setRefreshing(false); //D??nen yuvarla???? kapat.
    }
    private void getQuestions(){
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.setAction(getString(R.string.wsAc_getAnasayfa));
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_getAnasayfa)));
    }
    /*
    Soldan ??ekmeli men??n??n alt??ndaki navigation k??sm??nda herhangi bir item se??ildi??inde tetiklenen metod.
    Kullan??c??n??n se??ti??i id'yi al??p ilgili yere g??nderiyoruz.
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profilim) {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            i.putExtra(getString(R.string.x_Profile_getSomeone_user), currentUser);
            i.setAction(getString(R.string.wsAc_showSomeone));
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**BURDAN ALTINI YAZMA DE??????ECEK.*/


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.hasExtra(getString(R.string.SoruPreview1_Anasayfa_newQuestion_question))) //Soru sorma ekran??nda geri d??n??yor.
            {
                Question question = (Question) intent.getSerializableExtra(getString(R.string.SoruPreview1_Anasayfa_newQuestion_question));
                qS.addQuestionToList(question);
                Intent i = new Intent(this, LoadingScreenActivity.class);
                i.putExtra("QUESTION", question);
                i.setAction(getString(R.string.wsAc_postQuestion));
                startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_postQuestion)));
            }
        }
    }


    class SwipeRefreshResponder implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            updateQuestions();
        }
    }
}
