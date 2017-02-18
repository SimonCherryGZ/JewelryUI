package simoncherry.jewelryui.lm;

import android.content.Context;
import android.view.View;

import simoncherry.customlayoutmanager.CustomLayoutManager;

/**
 * Created by zixintechno on 12/7/16.
 */

public class GalleryLayoutManager extends CustomLayoutManager {
    private static float INTERVAL_ANGLE = 30f;
    private static float MIN_ALPHA = 0.5f;

    private int itemSpace = 0;

    public GalleryLayoutManager(Context context, int itemSpace) {
        super(context);
        this.itemSpace = itemSpace;
    }

    public GalleryLayoutManager(Context context, int itemSpace, boolean isClockWise) {
        super(context,isClockWise);
        this.itemSpace = itemSpace;
    }

    @Override
    protected float setInterval() {
        return mDecoratedChildWidth+itemSpace;
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected void setItemViewProperty(View itemView, float targetOffset) {
        itemView.setRotationY(calRotationY(targetOffset));
        itemView.setAlpha(calAlpha(targetOffset));
    }

    private float calRotationY(float targetOffset){
        return -INTERVAL_ANGLE / interval * targetOffset;
    }

    private float calAlpha(float targetOffset){
        float alpha = (MIN_ALPHA - 1f)/interval * Math.abs(targetOffset) + 1f;
        if(alpha < MIN_ALPHA) alpha = MIN_ALPHA;
        return alpha;
    }
}
