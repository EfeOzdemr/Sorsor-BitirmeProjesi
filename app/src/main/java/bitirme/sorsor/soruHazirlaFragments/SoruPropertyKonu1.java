package bitirme.sorsor.soruHazirlaFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import bitirme.sorsor.R;
import bitirme.sorsor.model.Course;
import bitirme.sorsor.model.Exam;
import bitirme.sorsor.model.Question;

/**
 * Created by mert on 02.03.2016.
 */
public class SoruPropertyKonu1 extends Fragment implements SoruHazirlamaInterface {
    public static String pageTitle = "Ders";

    private Spinner spinnerDers;
    private Spinner spinnerSinav;
    private ArrayList<Exam> examsList;
    private Question newQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.soru_property_konu_fragment1, container, false);
        spinnerDers = (Spinner) view.findViewById(R.id.spinnerDers);
        spinnerSinav = (Spinner) view.findViewById(R.id.spinnerSinav);
        examsList = new ArrayList<>();
        initializeSpinnerContent();
        ArrayAdapter<Exam> examAdapter = new ArrayAdapter<Exam>(view.getContext(), android.R.layout.simple_spinner_item, examsList);
        spinnerSinav.setAdapter(examAdapter);
        spinnerSinav.setOnItemSelectedListener(new ExamSpinnerListener());

        Bundle bundle = getArguments();
        newQuestion = (Question) bundle.getSerializable(getString(R.string.SoruHazirlama_SoruPreview1_renderNewQuestion_question));
        return view;
    }

    public Course getCourse() {
        return ((Course) spinnerDers.getSelectedItem());
    }

    public Exam getExam() {
        return ((Exam) spinnerSinav.getSelectedItem());
    }

    private void initializeSpinnerContent() {
        //GEÇİCİ KOD
        Exam lys = new Exam(1, "LYS-MF", null);
        new Course(lys, "MAT2", 1);
        new Course(lys, "FİZİK2", 2);
        Exam ygs = new Exam(2, "YGS", null);
        new Course(ygs, "MAT1", 3);
        new Course(ygs, "FIZIK", 4);
        examsList.add(lys);
        examsList.add(ygs);
    }

    @Override
    public void setQuestionParams() {
        newQuestion.setCourseID(getCourse());
        newQuestion.getCourseID().setExam(getExam());
    }

    @Override
    public Question getQuestion() {
        return newQuestion;
    }

    @Override
    public String getPageTitle() {
        return pageTitle;
    }

    private class ExamSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Exam exam = (Exam) parent.getItemAtPosition(position);
            ArrayAdapter<Course> courseAdapter = new ArrayAdapter<Course>(getContext(), android.R.layout.simple_spinner_item, exam.getCourseList());
            spinnerDers.setAdapter(courseAdapter);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
