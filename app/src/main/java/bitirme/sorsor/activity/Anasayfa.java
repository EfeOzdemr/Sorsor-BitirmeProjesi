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
    //GEÇİCİ KOD
    User asker;

    //GEÇİCİ KOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Toolbar'ı layout'tan bul.
        setSupportActionBar(toolbar);
        receiveIntents(getIntent());
        recyclerQuestions = (RecyclerView) findViewById(R.id.recyclerQuestions);  //RecyclerView'ı layout'tan bul
        rcyclerQuestionsLayoutManager = new LinearLayoutManager(this); //RecyclerView için Linear layout manager oluşturuyoruz.
        recyclerQuestions.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });
        recyclerQuestions.setLayoutManager(rcyclerQuestionsLayoutManager); //Yukarıdaki Layout Manager'ı-
        recyclerQuestions.addItemDecoration(new ItemDecorator(50));        //RecyclerViewın manager'ı olarak kullan
        /*
        RecyclerPostsAdapter ile ilgili açıklama:
            Model sınıflarımız olan Question ve Answer sınıfları bir çok özelliğini Post sınıfından alıyor.
            Ve bu sınıflardan oluşan ArrayListleri uygulamanın çeşitli yerlerindeki RecyclerViewlarda gösterebilmek için
            Adapter sınıfı yazmamız gerekiyordu. Biz de bu iş için RecyclerPostsAdapter sınıfını yazdık.
            Aşağıda kullanılan constructor şöyle:
            RecyclerPostsAdapter(RecyclerViewda gözükecek liste, RecyclerView nesnesi, Listenin ne tipten veriler içereceği)
            Şuanlık boş liste veriyoruz çünkü henüz web servise istek atmadık.
         */
        rcycQuestionsAdapter = new RecyclerPostsAdapter(currentUser, new ArrayList(), recyclerQuestions, Question.class);
        qS = new SingletonQuestionService(rcycQuestionsAdapter);
        recyclerQuestions.setAdapter(rcycQuestionsAdapter); //RecyclerView'ın adapterını ayarladık.

        swipeRefresh = (SwipeRefreshResponder) findViewById(R.id.swipeRefresh); //Sayfa yenilemeleri için kullandığımız View'ımızı layout'tan aldık.
        swipeRefresh.setOnRefreshListener(new SwipeRefreshResponder()); //Refresh edilmesi halinde şu yazdığımız Listener sınıfının çalışmasını istiyoruz dedik.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.sorSorFab); //Yeni soru ekleme FAB'ini layout'tan aldık.
        fab.setOnClickListener(new View.OnClickListener() { //Fab'e tıklandığında ne olacağını bu anonim sınıfta yazıyoruz.
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SoruHazirlama.class); //Soru hazırlama activitysine geçiş için yeni intent
                /*
                Activity geçişlerinde kullanılan key'ler ile ilgili açıklama:
                    Uygulama içinde bir çok activity geçisi olacağını tahmin ettiğimiz için kullanılacak key'leri
                    bir sisteme bağlamak istedik. Bu yüzden resource klasöründeki String dosyasına yazdık.
                    Yani string dosyasındaki key/value kısmındaki value, bizim intent geçişlerinde kullandığımız key'imiz oluyor.
                    Ona da aşağıdaki şekilde ulaşıyoruz:
                    String dosyasındaki key kısmının yapısı şöyle:
                    ŞuankiActivityAdı_GidilecekActivityAdı_EylemİleİlgiliAçıklama_TaşınanNesne
                 */
                i.putExtra(getString(R.string.Anasayfa_SoruHazirlama_newQuestion_user), currentUser);
                startActivity(i); //SoruHazırlama activity'sini başlat.
            }
        });
        /*Geçici Kod
        * Buralar raporda olmasın.
        * */

        /*Geçici kod bitti*/


        /*

         Aşağıdaki geçici kodların yerine kodları yazdım ama yine de silmiyorum.


         soruList = new ArrayList<Question>();
        asker = new User("Örnek Ad", "Adam");
        asker.setUsername("mert95");

        Bitmap soru = ImageProcess.getBitmapFromDrawable(getApplicationContext(), R.drawable.geo1);
        Photo soruP = new Photo(soru);

        asker.setPhoto(avatar);
        //TODO: GEÇİCİ KOD BUNLAR HEP ÇEKİLECEK ALINACAK,

        for (int i = 0; i < 15; i++) {
            Question aQuestion = new Question(i, "Örnek Title " + i, "Örnek Description " + i, asker);
            if (i == 0) {
                for (int j = 0; j < 10; j++) {
                    Post aPost=new Post(currentUser, "Örnek Desc" + j);
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
        Bu aşağıda olanlar
        Drawer (Soldan çekmeli menü) ile ilgili olan kodların tamamı. Drawer içeriği gibi şeyleri ayarladık.
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
    Back butonuna bastığında Drawer'ı kapatmasını istediğimiz için bu metodu override ettik.
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
        Bu metod iki şekilde tetiklenebilir:
        1-Kullanıcı ekranı yukarı doğru çekerse,
        2-Kullanıcı menüden yenile tuşuna basars
        3-Her birkaç dakikada bir -- HENÜZ GERÇEKLEŞTİRİLMEDİ --
     */
    private void updateQuestions() {
        getQuestions(); //WebServisin sorular bölümüne istek gönder.
        swipeRefresh.setRefreshing(false); //Dönen yuvarlağı kapat.
    }
    private void getQuestions(){
        Intent i = new Intent(this, LoadingScreenActivity.class);
        i.setAction(getString(R.string.wsAc_getAnasayfa));
        startActivityForResult(i, Integer.parseInt(getString(R.string.wsRC_getAnasayfa)));
    }
    /*
    Soldan çekmeli menünün altındaki navigation kısmında herhangi bir item seçildiğinde tetiklenen metod.
    Kullanıcının seçtiği id'yi alıp ilgili yere gönderiyoruz.
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
    /**BURDAN ALTINI YAZMA DEĞİŞECEK.*/


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.hasExtra(getString(R.string.SoruPreview1_Anasayfa_newQuestion_question))) //Soru sorma ekranında geri dönüyor.
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
