package bitirme.sorsor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import bitirme.sorsor.R;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import bitirme.sorsor.soruHazirlaFragments.ResimCekFragment1;
import bitirme.sorsor.soruHazirlaFragments.SoruHazirlamaInterface;
import bitirme.sorsor.soruHazirlaFragments.SoruPreview1;
import bitirme.sorsor.soruHazirlaFragments.SoruProperty1;
import bitirme.sorsor.soruHazirlaFragments.SoruPropertyKonu1;

/* Created by Mert & Efe */
public class SoruHazirlama extends AppCompatActivity {

    private ViewPager viewPager;
    final int PAGE_COUNT = 4;

    private SoruHazirlamaInterface pages[] = new SoruHazirlamaInterface[]{
            new SoruProperty1(),
            new SoruPropertyKonu1(),
            new ResimCekFragment1(),
            new SoruPreview1()
    };

    private User currentUser;
    private Question newQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_hazirlama);
        viewPager = (ViewPager) findViewById(R.id.viewPagerSoruHazirla);
        viewPager.setAdapter(new SoruHazirlaPagerAdapter(getSupportFragmentManager()));
        currentUser = (User) getIntent().getSerializableExtra(getString(R.string.Anasayfa_SoruHazirlama_newQuestion_user));
        newQuestion = new Question(currentUser);
        setTitle(pages[0].getPageTitle());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int lastPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*
                OnPageChangeListener'da a pozisyonundan b pozisyonuna gidince a pozisyonunu veren bir variable olmadığı için
                bu onPageC
                 */
            }

            @Override
            public void onPageSelected(int position) {
                setTitle(pages[position].getPageTitle());
                onPageChange(lastPos, position);
                lastPos = position;
            }

            public void onPageChange(int lastPosition, int currentPosition) {
                pages[lastPosition].setQuestionParams();
                if (lastPosition == 2 && currentPosition == 3) {
                    try {
                        pages[currentPosition].getClass().getDeclaredMethod("initializeCard").invoke(pages[currentPosition]);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private class SoruHazirlaPagerAdapter extends FragmentPagerAdapter {
        public SoruHazirlaPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.SoruHazirlama_SoruPreview1_renderNewQuestion_question), newQuestion);
            Fragment newFragment = null;
            try {
                newFragment = ((Fragment) pages[position]);
                newFragment.setArguments(bundle);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return newFragment;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position].getPageTitle();
        }

        @Override
        public int getCount() {
            return pages.length;
        }
    }
}
