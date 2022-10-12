package bitirme.sorsor.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import bitirme.sorsor.Image.Photo;

/**
 * Created by mert on 28.04.2016.
 */
public class Post implements Serializable {
    public static final int SHORT_DESCRIPTION_MAX_LENGTH = 25;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user")
    @Expose
    private User author; //YazarID
    private String description;
    private Date date;
    private Photo photo;
    private int likeCount = 0;
    @SerializedName(value="questionPic", alternate={"answerPic"})
    @Expose
    private String photoUrl;
    public Post() {
    }

    public Post(User author, String description) {
        this.author = author;
        this.description = description;
    }

    public Post(User author, String description, Date date) {
        this.author = author;
        this.description = description;
        this.date = date;
    }

    public Post(User author, String description, Date date, Photo photo) {
        this.author = author;
        this.description = description;
        this.date = date;
        this.photo = photo;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getShortDescription() {
        if (description != null) { //TODO: OLUYOR MU KONTROL ET
            if (description.length() < SHORT_DESCRIPTION_MAX_LENGTH)
                return description;
            else
                return description.substring(0, 25) + "..";
        } else {
            return "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
