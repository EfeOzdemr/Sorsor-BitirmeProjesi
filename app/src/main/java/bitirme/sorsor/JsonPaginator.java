package bitirme.sorsor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bitirme.sorsor.activity.Anasayfa;
import bitirme.sorsor.model.Answer;
import bitirme.sorsor.model.Question;


/**
 * Created by Caner on 12.05.2016.
 * WebServisten gelen Json verileri gelen veri adetine göre sayfalara bölmemiz gerekiyordu.
 * Bu işlem için Laravel'in ORM'i olan Eloquent'in Paginator metodunu kullandık.
 * Bu metodun döndüğü sayfalanmış JSON stringi aşağıdaki sınıfın yapısıyla ulaşıyor.
 * Data kısmında gelebilecek farklı veriler için JsonPaginator'u extend eden Alt sınıflar yazıldı.
 * Mesela Question verisiyse QuestionPage, Answer verisiyse AnswerPage.
 * */


public class JsonPaginator implements Serializable {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("current_page")
    @Expose
    private Integer currentPage;
    @SerializedName("last_page")
    @Expose
    private Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    private Object nextPageUrl;
    @SerializedName("prev_page_url")
    @Expose
    private Object prevPageUrl;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;

    public Integer getTotal() {
        return total;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getTo() {
        return to;
    }

    public class QuestionPage extends JsonPaginator{
        public QuestionPage(List<Question> data) {
            this.data = data;
        }

        @SerializedName("data")
        @Expose
        private List<Question> data = new ArrayList<Question>();

        public List<Question> getData() {
            return data;
        }

        public void setData(List<Question> data) {
            this.data = data;
        }
    }
    public class AnswerPage extends JsonPaginator{
        public AnswerPage(List<Answer> data) {
            this.data = data;
        }

        @SerializedName("data")
        @Expose
        private List<Answer> data = new ArrayList<Answer>();

        public List<Answer> getData() {
            return data;
        }

        public void setData(List<Answer> data) {
            this.data = data;
        }
    }
}