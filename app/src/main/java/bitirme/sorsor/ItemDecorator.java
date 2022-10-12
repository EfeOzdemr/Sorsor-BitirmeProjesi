package bitirme.sorsor;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Efe on 28.05.2016.
 */
public class ItemDecorator extends RecyclerView.ItemDecoration {
    int offset;
    public ItemDecorator(int offset) {
        this.offset = offset/2;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = this.offset;
        outRect.top = this.offset;
    }


}
