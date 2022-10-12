package bitirme.sorsor;

/**
 * Created by Efe on 12.05.2016.
 * Token singleton bir sınıf olup login olduktan sonra kullanıcıyı authenticate eden tokenı saklıyor.
 */
public class Token {
    private static Token instance;
    private String token;
    public static synchronized Token getInstance() {
        if(instance == null)
            instance = new Token();
        return instance;
    }

    private Token() {
    }

    public String getToken() {
        return "Bearer {"+token+"}";
    }

    public void setToken(String token) {
        this.token = token;
    }
}
