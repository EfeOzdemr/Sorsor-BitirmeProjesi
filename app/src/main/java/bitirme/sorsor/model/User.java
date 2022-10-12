package bitirme.sorsor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.JsonPaginator;

/**
 * Created by Efe on 29.02.2016.
 */
public class User implements Serializable { //TODO: Parcelable da implement edilebilir, şuanlık gerek yok.
    private String name;
    private String surname;
    private String username;
    @SerializedName("age")
    @Expose
    private int yas;
    @SerializedName("id")
    @Expose
    private long userID;

    @SerializedName("grade")
    @Expose
    private int sinif;

    @SerializedName("likeCount")
    @Expose
    private int begeniSayisi;

    @SerializedName("isWoman")
    @Expose
    private int isWoman;
    @SerializedName("questionCount")
    @Expose
    private int soruSayisi;

    @SerializedName("answerCount")
    @Expose
    private int cevapSayisi;
    @SerializedName("exam")
    @Expose
    private Exam interestedExam;
    @SerializedName("schoolName")
    @Expose
    private String okulAdi;

    private Photo photo;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("questions")
    @Expose
    private JsonPaginator.QuestionPage questions;
    @SerializedName("answers")
    @Expose
    private JsonPaginator.AnswerPage answers;

    public User() {

    }

    public User(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public User(String name, String surname, String username, long userID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.userID = userID;
    }

    public JsonPaginator.QuestionPage getQuestions() {
        return questions;
    }

    public void setQuestions(JsonPaginator.QuestionPage questions) {
        this.questions = questions;
    }

    public JsonPaginator.AnswerPage getAnswers() {
        return answers;
    }

    public void setAnswers(JsonPaginator.AnswerPage answers) {
        this.answers = answers;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getIsWoman() {
        return isWoman;
    }

    public String getOkulAdi() {
        return okulAdi;
    }

    public void setOkulAdi(String okulAdi) {
        this.okulAdi = okulAdi;
    }

    public Exam getInterestedExam() {
        return interestedExam;
    }

    public void setInterestedExam(Exam interestedExam) {
        this.interestedExam = interestedExam;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getFullname() {
        return getName() + " " + getSurname();
    }

    public int getSoruSayisi() {
        return soruSayisi;
    }

    public void setSoruSayisi(int soruSayisi) {
        this.soruSayisi = soruSayisi;
    }

    public int getCevapSayisi() {
        return cevapSayisi;
    }

    public void setCevapSayisi(int cevapSayisi) {
        this.cevapSayisi = cevapSayisi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getYas() {
        return yas;
    }

    public void setYas(int yas) {
        this.yas = yas;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getSinif() {
        return sinif;
    }

    public void setSinif(int sinif) {
        this.sinif = sinif;
    }

    public int getBegeniSayisi() {
        return begeniSayisi;
    }

    public void setBegeniSayisi(int begeniSayisi) {
        this.begeniSayisi = begeniSayisi;
    }

    public int isWoman() {
        return isWoman;
    }

    public void setIsWoman(int isWoman) {
        this.isWoman = isWoman;
    }
}
