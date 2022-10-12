package bitirme.sorsor.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import java.util.ArrayList;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.JsonPaginator;
import bitirme.sorsor.PhotoDownloader;
import bitirme.sorsor.R;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Exam;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import bitirme.sorsor.profileFragments.ProfileRecyclerViewFragment;
import bitirme.sorsor.service.SingleetonUserService;
import de.hdodenhof.circleimageview.CircleImageView;

/* Created by Mert */
public class ProfileActivity extends AppCompatActivity {

    private User thisUser;
    private ImageButton swipeUp;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager viewPager;

    public String sayfaAdlari[] = new String[]{Question.USER_PROFILE_LIST_NAME, Answer.USER_PROFILE_LIST_NAME};
    public ArrayList listeler[] = new ArrayList[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        receiveIntents(getIntent());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.profileCoordinatorL);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        swipeUp = (ImageButton) findViewById(R.id.profile_swipeUp);
        viewPager = (ViewPager) findViewById(R.id.profileViewPager);
        if(thisUser.getAnswers() == null )
        {
            listeler[1] = new ArrayList();
        }
        if(thisUser.getQuestions() == null){
            listeler[0] = new ArrayList();
        }
        viewPager.setAdapter(new SoruListViewPagerAdapter(getSupportFragmentManager(), listeler, sayfaAdlari));

                swipeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false, true);
            }
        });
        RelativeLayout questionsRL = (RelativeLayout) findViewById(R.id.profile_questionsRL);
        RelativeLayout responsesRL = (RelativeLayout) findViewById(R.id.profile_responsesRL);
        questionsRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false, true);
                viewPager.setCurrentItem(0, true);
            }
        });
        responsesRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false, true);
                viewPager.setCurrentItem(1, true);
            }
        });
        initializeTopUI();

    }
    private void initializeBottomUI(){
        listeler = new ArrayList[]{(ArrayList) thisUser.getQuestions().getData(), (ArrayList) thisUser.getAnswers().getData()};
        ((SoruListViewPagerAdapter)viewPager.getAdapter()).setLists(listeler);
        collapsingToolbarLayout.setTitle(thisUser.getFullname());
    }
    private void initializeTopUI() {
        //Yukarki sayfanın işleri aşağıda
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.profile_relative);
        CircleImageView profilPic = (CircleImageView) relativeLayout.findViewById(R.id.profile_ProfilePic);
        TextView fullName = (TextView) relativeLayout.findViewById(R.id.profile_Name);
        TextView school = (TextView) relativeLayout.findViewById(R.id.profile_school);
        TextView likeCount = (TextView) relativeLayout.findViewById(R.id.profile_UserLikeCount);
        TextView answerCount = (TextView) relativeLayout.findViewById(R.id.profile_UserAnswerCount);
        TextView questionCount = (TextView) relativeLayout.findViewById(R.id.profile_UserQuestionCount);
        FloatingActionButton gender = (FloatingActionButton) relativeLayout.findViewById(R.id.profile_UserGender);
        FloatingActionButton age = (FloatingActionButton) relativeLayout.findViewById(R.id.profile_UserAge);
        FloatingActionButton interestedExam = (FloatingActionButton) relativeLayout.findViewById(R.id.profile_UserExam);
        FABClickListener fabClickListener = new FABClickListener();
        gender.setOnClickListener(fabClickListener);
        age.setOnClickListener(fabClickListener);
        interestedExam.setOnClickListener(fabClickListener);

        if(thisUser.getPhoto().getPictureBitmap() == null)
        {
            if(PhotoDownloader.checkUrl(thisUser.getProfilePic()))
                PhotoDownloader.downloadPhoto(thisUser.getProfilePic(), thisUser, profilPic);
            else
                thisUser.setPhoto(new Photo(ImageProcess.getBitmapFromDrawable(this, R.drawable.adam), null));
        }
        else
            profilPic.setImageBitmap(thisUser.getPhoto().getPictureBitmap());

        fullName.setText(thisUser.getFullname());
        school.setText(thisUser.getOkulAdi());
        likeCount.setText("" + thisUser.getBegeniSayisi());
        answerCount.setText("" + thisUser.getCevapSayisi());
        questionCount.setText("" + thisUser.getSoruSayisi());
        if (thisUser.isWoman() == 1)
            gender.setImageBitmap(ImageProcess.getBitmapFromDrawable(getApplicationContext(), R.drawable.ic_gender_female_white_48dp));
        else {
            Bitmap bmp = ImageProcess.getBitmapFromDrawable(getApplicationContext(), R.drawable.ic_gender_male_white_48dp);
            gender.setImageBitmap(bmp);
        }
        TextView ageV = new TextView(getApplicationContext());
        ageV.setText("" + thisUser.getYas());
        ageV.setTextColor(getResources().getColor(android.R.color.white));
        age.setImageBitmap(ImageProcess.loadBitmapFromView(ageV));

        TextView interestedExamV = new TextView(getApplicationContext());
        thisUser.setInterestedExam(thisUser.getInterestedExam());
        if(thisUser.getInterestedExam() == null){ //Default.
            thisUser.setInterestedExam(new Exam("YGS", "YGS"));
        }
        interestedExamV.setText(thisUser.getInterestedExam().getAcronym());
        interestedExamV.setTextColor(getResources().getColor(android.R.color.white));

        interestedExam.setImageBitmap(ImageProcess.loadBitmapFromView(interestedExamV));
    }

    public void receiveIntents(Intent i) {
        if (i.getAction().contentEquals(getString(R.string.wsAc_showSomeone))) {
            if (i.hasExtra(getString(R.string.x_Profile_getSomeone_user))) {
                thisUser = (User) i.getSerializableExtra(getString(R.string.x_Profile_getSomeone_user));
                Intent intent = new Intent(this, LoadingScreenActivity.class);
                intent.setAction(getString(R.string.wsAc_showSomeone));
                intent.putExtra("USER", thisUser);
                startActivityForResult(intent, Integer.parseInt(getString(R.string.wsRC_showSomeone)));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Integer.parseInt(getString(R.string.wsRC_showSomeone))) {
            if(resultCode == Activity.RESULT_OK){
                thisUser = (User) data.getSerializableExtra("USER");
                initializeBottomUI();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }
    private class FABClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String mesaj = "";
            switch (v.getId()) {
                case R.id.profile_UserAge:
                    mesaj = thisUser.getName() + " " + thisUser.getYas() + " " + "yaşında.";
                    break;
                case R.id.profile_UserGender:
                    if (thisUser.isWoman() == 1)
                        mesaj = thisUser.getName() + " iki adet X kromozomuna sahip.";
                    else
                        mesaj = thisUser.getName() + " bir adet Y krozomuna sahip.";
                    break;
                case R.id.profile_UserExam:
                    mesaj = thisUser.getName() + " son günlerde " + thisUser.getInterestedExam().getAcronym() + " çalışıyor.";
                    break;
                case R.id.profile_setProfileInfo:
                    //TODO: Profil ayarları
                    break;
            }
            Snackbar.make(coordinatorLayout, mesaj, Snackbar.LENGTH_LONG).show();
        }
    }

    private class SoruListViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList lists[];
        private int PAGE_COUNT = 2;
        private String[] pageTitles;
        private ProfileRecyclerViewFragment fragments[] = {null, null};
        public SoruListViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SoruListViewPagerAdapter(FragmentManager fm, ArrayList[] listeler, String[] adlar) {
            super(fm);
            this.lists = listeler;
            this.pageTitles = adlar;
        }

        @Override
        public Fragment getItem(int position) {
            fragments[position] = new ProfileRecyclerViewFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.Profile_Profile_getList_postsOfQuestions), lists[position]);
            bundle.putBoolean(getString(R.string.Profile_Profile_isQuestionList_boolean), isItQuestionList(position));
            fragments[position].setArguments(bundle);
            return fragments[position];
        }

        private boolean isItQuestionList(int pos) {
            return pageTitles[pos].contentEquals(Question.USER_PROFILE_LIST_NAME);
        }

        @Override
        public int getCount() {
            return pageTitles.length;
        }

        public void setLists(ArrayList[] lists) {
            this.lists = lists;
            if(fragments[0] != null)
                fragments[0].notifyAdapter(lists[0]);
            if(fragments[1] != null)
                fragments[1].notifyAdapter(lists[1]);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles[position];
        }
    }

}
