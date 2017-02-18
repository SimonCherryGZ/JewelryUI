package simoncherry.jewelryui.lm;


import android.content.Context;
import android.view.View;

/**
 * Created by Simon on 2017/2/12.
 */

public class StackLayoutManager extends BaseLayoutManager {

    private static final float SCALE_RATE = 1.0f;

    public StackLayoutManager(Context context) {
        super(context);
    }

    public StackLayoutManager(Context context, boolean isClockWise) {
        super(context, isClockWise);
    }

    @Override
    protected float setInterval() {
        return (int) (mDecoratedChildWidth*((SCALE_RATE-1f)/2f+1));
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {
        float scale = calculateScale((int) targetOffset + startLeft);
        itemView.setScaleX(scale);
        itemView.setScaleY(scale);
    }

    private float calculateScale(int x){
        int deltaX = Math.abs(x-(getHorizontalSpace() - mDecoratedChildWidth) / 2);
        float diff = 0f;
        if((mDecoratedChildWidth-deltaX)>0) diff = mDecoratedChildWidth-deltaX;
        return (SCALE_RATE-1f)/mDecoratedChildWidth * diff + 1;
    }
}
