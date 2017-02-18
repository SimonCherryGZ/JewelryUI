package simoncherry.jewelryui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fivehundredpx.android.blur.BlurringView;

import java.util.ArrayList;
import java.util.List;

import simoncherry.customlayoutmanager.CenterScrollListener;
import simoncherry.jewelryui.R;
import simoncherry.jewelryui.custom.ObservableScrollView;
import simoncherry.jewelryui.custom.PagerAdapter;
import simoncherry.jewelryui.custom.RecyclerViewPager;
import simoncherry.jewelryui.custom.ViewPager;
import simoncherry.jewelryui.jazzy.JazzyViewPager;
import simoncherry.jewelryui.jazzy.OutlineContainer;
import simoncherry.jewelryui.lm.CircleZoomLayoutManager;
import simoncherry.jewelryui.lm.StackLayoutManager;



public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private int[] ringResId = {R.drawable.ring1, R.drawable.ring2, R.drawable.ring3, R.drawable.ring4, R.drawable.ring5};
    //private int[] modelResId = {R.drawable.hand1, R.drawable.hand2, R.drawable.hand3, R.drawable.hand4, R.drawable.hand5};
    private int[] modelResId = {R.drawable.model1, R.drawable.model2, R.drawable.model3, R.drawable.model4, R.drawable.model5};
    private String[] ringName = {
            "ROSE GOLD SWIRL\nCROSS OVER RING", "HEART BOUQUET RING\nIN ROSE GOLD", "PAVE HEART SWIRL\nSTRETCH RING",
            "HORSE STONE RING\nROSE GOLD", "ROSE GOLD\nCRYSTAL RING"};
    private String[] effects;
    private int index = 6;

    private RecyclerViewPager rvRing;
    private RecyclerView rvModel;
    private ObservableScrollView svContainer;
    private ScrollView svDetail;
    private BlurringView blurringView;
    private JazzyViewPager vpModel;
    private ImageView ivBlur;
    private TextView tvName;

    private ModelAdapter modelAdapter;
    private StackLayoutManager stackLayoutManager;
    private CircleZoomLayoutManager circleZoomLayoutManager;

    private boolean isBlur = true;
    private float adjustFactor = 1.4f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_main);

        findView();
        initView();
        initRvRing();
        initRvModel();

        effects = getResources().getStringArray(R.array.jazzy_effects);
        setupJazziness(JazzyViewPager.TransitionEffect.valueOf(effects[index]));
        initScrollView();
        initFAB();

        initBlur();
    }

    private void initBlur() {
        if (isBlur) {
            //blurringView.setBlurredView(rvModel);
            //blurringView.setBlurredView(vpModel);
            blurringView.setBlurredView(ivBlur);
            blurringView.setVisibility(View.VISIBLE);
            ivBlur.setVisibility(View.GONE);
        } else {
            blurringView.setVisibility(View.GONE);
        }
    }

    private void findView() {
        rvRing =(RecyclerViewPager)findViewById(R.id.rv_ring);
        rvModel = (RecyclerView)findViewById(R.id.rv_model);
        svContainer = (ObservableScrollView) findViewById(R.id.sv_container);
        svDetail = (ScrollView) findViewById(R.id.sv_detail);
        blurringView = (BlurringView) findViewById(R.id.blurring);
        vpModel = (JazzyViewPager) findViewById(R.id.vp_model);
        ivBlur = (ImageView) findViewById(R.id.iv_blur);
        tvName = (TextView) findViewById(R.id.tv_name);
    }

    private void initView() {
        setSpannableText(ringName[1]);
    }

    private void initRvRing(){
//        circleLayoutManager = new CircleLayoutManager(this, true);
//        circleZoomLayoutManager = new CircleZoomLayoutManager(this, true);
//        scrollZoomLayoutManager = new ScrollZoomLayoutManager(this, Dp2px(0));
//        galleryLayoutManager = new GalleryLayoutManager(this, Dp2px(10));

        rvRing.addOnScrollListener(new CenterScrollListener());
        circleZoomLayoutManager = new CircleZoomLayoutManager(this, true);
        rvRing.setLayoutManager(circleZoomLayoutManager);
        rvRing.setAdapter(new RingAdapter());
        rvRing.setSinglePageFling(true);
        rvRing.setTriggerOffset(0.1f);
        rvRing.setFlingFactor(0.3f);
        rvRing.setChildWidth(Dp2px(200.0f));

        rvRing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.e(TAG, "onScrollStateChanged  newState: " + String.valueOf(newState));
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e(TAG, "onScrolled  x: " + String.valueOf(dx) + " | y: " + String.valueOf(dy));

                //rvModel.scrollBy((int) (dx*adjustFactor), dy);
//                if (isNeedAdjust) {
//                    vpModel.scrollBy((int) (dx*adjustFactor), dy);
//                }

                if (isBlur) {
                    blurringView.invalidate();
                }
            }
        });

        rvRing.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MotionEvent fakeEvent = MotionEvent.obtain(
                        event.getDownTime(), event.getEventTime(), event.getAction(),
                        event.getX()*adjustFactor, event.getY(), event.getMetaState());
                try {
                    vpModel.onTouchEvent(fakeEvent);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

                return false;
            }
        });
    }

    private void initRvModel() {
        rvModel.addOnScrollListener(new CenterScrollListener());
        stackLayoutManager = new StackLayoutManager(this);
        rvModel.setLayoutManager(stackLayoutManager);
        modelAdapter = new ModelAdapter();
        rvModel.setAdapter(modelAdapter);
    }

    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        vpModel.setTransitionEffect(effect);
        vpModel.setFadeEnabled(true);
        vpModel.setAdapter(new VpAdapter());
        vpModel.setPageMargin(60);

        vpModel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("vpModel.onPageScrolled",
//                        "position:" + String.valueOf(position) +
//                                ", positionOffset:" + String.valueOf(positionOffset) +
//                                ", positionOffsetPixels:" + String.valueOf(positionOffsetPixels));
                if (isBlur) {
                    ivBlur.setImageResource(modelResId[(position+1)%5]);
                    blurringView.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("vpModel.onPageSelected", "position:" + String.valueOf(position));
                //tvName.setText(ringName[(position+1)%5]);
                showTextAnimation(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Log.e("onStateChanged", "state: " + String.valueOf(state));
            }
        });
    }

    private void initScrollView() {
        svContainer.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//                Log.e(TAG, "onScrollChanged  x=" + String.valueOf(x) + ", y=" + String.valueOf(y)
//                        + ", oldx=" + String.valueOf(oldx) + ", oldy=" + String.valueOf(oldy));

                if (isBlur) {
                    if (y > 0) {
                        ivBlur.setVisibility(View.VISIBLE);
                        blurringView.setVisibility(View.VISIBLE);
                        circleZoomLayoutManager.setCanScroll(false);
                    } else {
                        ivBlur.setVisibility(View.GONE);
                        blurringView.setVisibility(View.GONE);
                        circleZoomLayoutManager.setCanScroll(true);
                    }

                    float scaleRatio = (y / 1000.0f) + 1.0f;
                    blurringView.setScaleX(scaleRatio);
                    blurringView.setScaleY(scaleRatio);

                    int radiusRatio = y / 200 + 1;
                    int downSample = y / 60 + 1;
                    blurringView.setBlurRadius(radiusRatio);
                    blurringView.setDownsampleFactor(downSample);
                    blurringView.invalidate();
                }
            }
        });

        svContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewParent parent = findViewById(R.id.sv_detail).getParent();
                parent.requestDisallowInterceptTouchEvent(false);  //允许父类截断
                return false;
            }
        });

        svDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewParent parent1 = v.getParent();
                parent1.requestDisallowInterceptTouchEvent(true); //不允许父类截断
                return false;
            }
        });
    }

    private void initFAB() {
        FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //determinLayoutManager();
                changeJazzyEffect();
            }
        });
    }

    private void changeJazzyEffect() {
        index++;
        if (index > effects.length-1) {
            index = 0;
        }
        setupJazziness(JazzyViewPager.TransitionEffect.valueOf(effects[index]));
        Toast.makeText(this, effects[index], Toast.LENGTH_SHORT).show();
    }

    private int Dp2px(float dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    class RingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_ring,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int index = (position+1)%5;
            ((MyViewHolder)holder).imageView.setImageResource(ringResId[index]);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public MyViewHolder(View itemView){
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

    class ModelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_model,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int index = (position+1)%5;
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.imageView.setImageResource(modelResId[index]);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public MyViewHolder(View itemView){
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

    class VpAdapter extends PagerAdapter {

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
            int index = (position+1)%5;
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setImageResource(modelResId[index]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
            container.addView(imageView);
            vpModel.setObjectForPosition(imageView, position);
            return imageView;
        }
    }

    private void showTextAnimation(final int position) {
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation transOut = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f);

        TranslateAnimation transIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

        AnimationSet animationSet1 = new AnimationSet(true);
        animationSet1.addAnimation(fadeOut);
        animationSet1.addAnimation(transOut);
        animationSet1.setDuration(200);

        final AnimationSet animationSet2 = new AnimationSet(true);
        animationSet2.addAnimation(fadeIn);
        animationSet2.addAnimation(transIn);
        animationSet2.setDuration(200);

        animationSet1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                setSpannableText(ringName[(position+1)%5]);
                tvName.startAnimation(animationSet2);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        tvName.clearAnimation();
        tvName.startAnimation(animationSet1);
    }

    private void setSpannableText(String src) {
        //tvName.setText(src);
        String temp = src.replace("\n", " ");
        List<Integer> mList = new ArrayList<>();
        int index = 0;
        int max = 0;
        if (temp.indexOf(" ", index) > 0) {
            while (temp.indexOf(" ", index) > 0 || index >= temp.length() || max < 50) {
                max++;
                index = temp.indexOf(" ", index);
                if (index <0 ) {
                    break;
                } else {
                    Log.e(TAG, "空格位置: " + String.valueOf(index));
                    mList.add(index);
                    index++;
                }
            }
        }
        SpannableString spannable = new SpannableString(src);
        spannable.setSpan(new RelativeSizeSpan(1.5f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                int position = mList.get(i) + 1;
                Log.e(TAG, "变更样式位置: " + String.valueOf(position));
                spannable.setSpan(new RelativeSizeSpan(1.5f), position, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tvName.setText(spannable);
    }
}
