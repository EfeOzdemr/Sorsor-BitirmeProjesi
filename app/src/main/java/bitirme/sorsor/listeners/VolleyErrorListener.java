package bitirme.sorsor.listeners;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Efe on 12.05.2016.
 */
public class VolleyErrorListener implements Response.ErrorListener{
    private Context context;

    public VolleyErrorListener(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this.context.getApplicationContext(), "Sunucuyla bağlantı kurarken problem oluştu. "+error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        Log.d("VOLLEY ERROR", new String(error.networkResponse.data));
    }
}