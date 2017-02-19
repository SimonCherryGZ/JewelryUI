package simoncherry.jewelryui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import simoncherry.jewelryui.R;
import simoncherry.jewelryui.custom.PagerAdapter;
import simoncherry.jewelryui.jazzy.JazzyViewPager;
import simoncherry.jewelryui.jazzy.OutlineContainer;

/**
 * Created by Simon on 2017/2/19.
 */

public class VpAdapter extends PagerAdapter {

    //private int[] modelResId = {R.drawable.hand1, R.drawable.hand2, R.drawable.hand3, R.drawable.hand4, R.drawable.hand5};
    private int[] modelResId = {R.drawable.model1, R.drawable.model2, R.drawable.model3, R.drawable.model4, R.drawable.model5};

    private Context mContext;
    private JazzyViewPager vpModel;

    public VpAdapter(Context mContext, JazzyViewPager vpModel) {
        this.mContext = mContext;
        this.vpModel = vpModel;
    }

    @Override
    public int getCount() {
        return modelResId.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view instanceof OutlineContainer) {
            return ((OutlineContainer) view).getChildAt(0) == object;
        } else {
            return view == object;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(vpModel.findViewFromObject(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int index = position % modelResId.length;
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(modelResId[index]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        container.addView(imageView);
        vpModel.setObjectForPosition(imageView, position);
        return imageView;
    }
}
