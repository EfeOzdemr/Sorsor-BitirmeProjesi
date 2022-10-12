package bitirme.sorsor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Efe on 02.03.2016.
 */
public class Exam implements Serializable {
    private int examID;
    private String examTitle;
    private String acronym;
    private List<Course> courseList = new ArrayList<>();

    public Exam(int examID, String examTitle, List<Course> courseList) {
        this.examID = examID;
        this.examTitle = examTitle;
        this.courseList = courseList;
    }

    public Exam(String examTitle, String acronym) {
        this.examTitle = examTitle;
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getExamID() {
        return examID;
    }

    public void setExamID(int examID) {
        this.examID = examID;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public String toString() {
        return this.examTitle;
    }
}
