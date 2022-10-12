package bitirme.sorsor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bitirme.sorsor.Image.Photo;
import bitirme.sorsor.ImageProcess.ImageProcess;
import bitirme.sorsor.activity.PostViewingActivity;
import bitirme.sorsor.activity.ProfileActivity;
import bitirme.sorsor.activity.QuestionActivity;
import bitirme.sorsor.listeners.PhotoListener;
import bitirme.sorsor.listeners.VolleyErrorListener;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Post;
import bitirme.sorsor.model.Question;
import bitirme.sorsor.model.User;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mert on 01.03.2016.
 */
public class RecyclerPostsAdapter extends RecyclerView.Adapter<RecyclerPostsAdapter.PostVH> {

    public List<Question> questionList;
    public List<Answer> answerList;
    public Class listType;
    public RecyclerView rc;
    private User currentUser; //Bu burada olmamalı. Gidilecek aktivityde User olsun diye burada.

    public RecyclerPostsAdapter(List list, RecyclerView rc, Class listType) {
        if(listType == Answer.class) {
            answerList = list;
            this.listType = Answer.class;

        }
        else if(listType == Question.class){
            questionList = list;
            this.listType = Question.class;
        }
        this.rc = rc;
    }
    public RecyclerPostsAdapter(User currentUser, List list, RecyclerView rc, Class listType) {
        this(list, rc, listType);
        this.currentUser = currentUser;
    }
    public void setCurrentUser(User user)
    {
        currentUser = user;
    }

    public RecyclerPostsAdapter() {}

    @Override
    public RecyclerPostsAdapter.PostVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_question, parent, false);
        PostVH pVH = new PostVH(v, listType);
        return pVH;
    }
    private static void handleImageViews(PostVH holder, Post ph) {
        if(ph.getAuthor().getPhoto() != null && ph.getAuthor().getPhoto().getPictureBitmap() != null)
            holder.askerPhoto.setImageBitmap(ph.getAuthor().getPhoto().getPictureBitmap());
        else
            PhotoDownloader.downloadPhoto(ph.getAuthor().getProfilePic(), ph.getAuthor(), holder.askerPhoto);
        if(ph.getPhoto() != null && ph.getPhoto().getPictureBitmap() != null )
            holder.postPhoto.setImageBitmap(ph.getPhoto().getPictureBitmap());
        else
            PhotoDownloader.downloadPhoto(ph.getPhotoUrl(), ph, holder.postPhoto);
    }

    public static void bindViewHolderAndObject(RecyclerPostsAdapter.PostVH holder, Post ph) {
        handleImageViews(holder, ph);
        holder.askerName.setText(ph.getAuthor().getName()+":");
        holder.postDescription.setText(ph.getDescription());
        holder.postLikeCount.setText(""+ph.getLikeCount());
        if(ph.getClass() == Question.class){
            Question theQuestion = (Question) ph;
            holder.postTitle.setText(theQuestion.getTitle());
            holder.questionAnswerCount.setText("" + theQuestion.getAnswerCount());
        }
    }
    @Override
    public void onBindViewHolder(RecyclerPostsAdapter.PostVH holder, int position) {
        Post ph=null;
        if(listType == Answer.class){
            ph = answerList.get(position);
            bindViewHolderAndObject(holder, answerList.get(position));
        }
        else if(listType == Question.class)
        {
            ph = questionList.get(position);
            bindViewHolderAndObject(holder, questionList.get(position));
        }
        if(ph == null)
            return;
    }
    @Override
    public int getItemCount() {
        if(listType == Question.class)
            return questionList.size();
        else if(listType == Answer.class)
            return answerList.size();
        return 0;
    }

    public class PostVH extends RecyclerView.ViewHolder {
        public CardView cardView;
        public CircleImageView askerPhoto;
        public TextView askerName;
        public TextView timeAgo;
        public TextView postTitle;
        public TextView postDescription;
        public ImageView postPhoto;
        public TextView postLikeCount;
        public TextView questionAnswerCount;
        public TextView questionAnswerTXT;
        public CardViewClickListener listener = new CardViewClickListener();
        Post thePost; //Preview sayfası için kullanılan soru referansı.Adapter boş olduğundan constructorda aldığım soruyu eşitliyorum.
        private boolean isPhotoExpanded = false;

        public PostVH(View itemView, Class type) {
            super(itemView);
            listType = type;
            initializeElements();
            cardView.setOnClickListener(listener);
            postPhoto.setOnClickListener(listener);
            askerPhoto.setOnClickListener(listener);
            askerName.setOnClickListener(listener);
        }
        public PostVH(View view, Answer answer){
            super(view);
            listType = Answer.class;
            initializeElements();

        }
        public PostVH(View view, Question question) { //Preview Question Constructor
            super(view);
            listType= Question.class;
            initializeElements();
            postPhoto.setOnClickListener(listener);
            questionList = new ArrayList<>();
            questionList.add(question);
            this.thePost = question;
        }

        public void initializeElements() {
            cardView = (CardView) itemView.findViewById(R.id.cv);
            if (getAdapterPosition() != -1 && questionList.get((getAdapterPosition())).getPhoto() == null) { //Def -1
                itemView.findViewById(R.id.askerPhoto).setVisibility(View.INVISIBLE);
            } else {
                askerPhoto = (CircleImageView) itemView.findViewById(R.id.askerPhoto);
                askerName = (TextView) itemView.findViewById(R.id.askerName);
                timeAgo = (TextView) itemView.findViewById(R.id.timeAgo);
                postTitle = (TextView) itemView.findViewById(R.id.questionTitle);
                postDescription = (TextView) itemView.findViewById(R.id.questionDescription);
                postPhoto = (ImageView) itemView.findViewById(R.id.questionPhoto);
                postLikeCount = (TextView) itemView.findViewById(R.id.questionLikeCount);
                questionAnswerCount = (TextView) itemView.findViewById(R.id.questionAnswerCount);
                questionAnswerTXT = (TextView) itemView.findViewById(R.id.questionAnswerTXT);
                if(listType == Answer.class)
                {
                    questionAnswerCount.setVisibility(View.INVISIBLE);
                    questionAnswerTXT.setVisibility(View.INVISIBLE);
                    postTitle.setVisibility(View.INVISIBLE);
                }
                else
                {
                }
            }
        }


        class CardViewClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {

                if(getAdapterPosition() != -1) { //Preview'da değilsek soruyu alalım.
                    if(listType == Question.class)
                        thePost = questionList.get(getAdapterPosition()); //Değilmişiz o zaman listin adapterını kullanalım.
                    else if(listType == Answer.class)
                        thePost = answerList.get(getAdapterPosition());
                }

                Context context = v.getContext();
                Intent i = null;
                switch (v.getId()) {
                    case R.id.cv: //Soruya git eğer soruysa
                        if(listType == Question.class){
                            Question theQuestion = (Question) thePost;
                            i = new Intent(context, QuestionActivity.class);
                            i.putExtra(context.getString(R.string.x_TopicActivity_newQuestion_question), theQuestion);
                            i.putExtra(context.getString(R.string.x_TopicActivity_newQuestion_user), currentUser);
                            context.startActivity(i);
                        }
                        break;
                    case R.id.questionPhoto:
                        if(thePost != null){
                            i = new Intent(context, PostViewingActivity.class);
                            i.putExtra(v.getResources().getString(R.string.x_PhotoViewing_showPhoto_question), thePost);
                            context.startActivity(i);
                        }
                        break;
                    case R.id.askerPhoto:
                        if(thePost != null){
                            i = new Intent(context, ProfileActivity.class);
                            i.putExtra(context.getString(R.string.x_Profile_getSomeone_user), thePost.getAuthor());
                            i.setAction(context.getString(R.string.wsAc_showSomeone));
                            context.startActivity(i);
                        }
                        break;
                    case R.id.askerName:
                        if(thePost != null){
                            i = new Intent(context, ProfileActivity.class);
                            i.putExtra(context.getString(R.string.x_Profile_getSomeone_user), thePost.getAuthor());
                            i.setAction(context.getString(R.string.wsAc_showSomeone));
                            context.startActivity(i);
                        }
                        break;

                }

            }
        }
    }
}
