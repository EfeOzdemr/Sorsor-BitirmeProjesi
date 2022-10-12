package bitirme.sorsor.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Efe on 01.03.2016.
 */
public class Course implements Serializable {
    private int courseID;
    private String courseTitle;
    private Exam exam;

    public Course(int courseID) {
        this.courseID = courseID;
    }

    public Course(Exam exam, String courseTitle, int courseID) {
        this.exam = exam;
        this.courseTitle = courseTitle;
        this.courseID = courseID;
        if (exam.getCourseList() == null)
            exam.setCourseList(new ArrayList<Course>());
        exam.getCourseList().add(this);
    }

    public Course(String courseTitle, Exam exam) {
        this.courseTitle = courseTitle;
        this.exam = exam;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public String toString() {
        return this.courseTitle;
    }
}
