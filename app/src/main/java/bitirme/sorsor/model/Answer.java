package bitirme.sorsor.model;

import java.io.Serializable;

/**
 * Created by Efe on 28.04.2016.
 */
public class Answer extends Post implements Serializable {
    private Question relatedQuestion;
    private long answerID;
    public static final String USER_PROFILE_LIST_NAME = "Cevapları";

    public Answer(Question relatedQuestion, Post answer) {
        setAuthor(answer.getAuthor());
        setPhoto(answer.getPhoto());
        setLikeCount(answer.getLikeCount());
        setDate(answer.getDate());
        setRelatedQuestion(relatedQuestion);
        setDescription(answer.getDescription());
    }

    public Answer(long answerID) {
        this.answerID = answerID;
    }

    public long getAnswerID() {
        return answerID;
    }

    public void setAnswerID(long answerID) {
        this.answerID = answerID;
    }

    public Question getRelatedQuestion() {
        return relatedQuestion;
    }

    public void setRelatedQuestion(Question relatedQuestion) {
        this.relatedQuestion = relatedQuestion;
    }
}
