package bitirme.sorsor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import bitirme.sorsor.Image.Photo;

    /**
 * Created by Efe on 01.03.2016.
 */
public class Question extends Post implements Serializable  {

    private String title;
    //Bu alttaki silinebilir
    private int questionID;
    private Course courseID;
    private int answerCount;
    public static final String USER_PROFILE_LIST_NAME = "Soruları";
    @SerializedName("answers")
    @Expose
    private ArrayList<Answer> responses = new ArrayList<>();

    public Question() {
    }

    public Question(int questionID, String title, String description) {
        setQuestionID(questionID);
        setTitle(title);
        setDescription(description);
    }

    public Question(int questionID, String title, String description, User author) {
        this(questionID, title, description);
        setAuthor(author);
    }

    public Question(int questionID, String title, String description, Photo photo, Course courseID, User author) {
        this(questionID, title, description, author);
        setPhoto(photo);
        setCourseID(courseID);
    }


    public ArrayList<Answer> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<Answer> responses) {
        this.responses = responses;
    }

    public Question(User currentUser) {
        setAuthor(currentUser);
    }

    public Question(int questionID) {
        setQuestionID(questionID);
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public Course getCourseID() {
        return courseID;
    }

    public void setCourseID(Course courseID) {
        this.courseID = courseID;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
