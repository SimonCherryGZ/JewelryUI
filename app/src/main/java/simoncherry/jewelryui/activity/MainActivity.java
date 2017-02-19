package simoncherry.jewelryui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.fivehundredpx.android.blur.BlurringView;
import com.konifar.fab_transformation.FabTransformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import simoncherry.customlayoutmanager.CenterScrollListener;
import simoncherry.jewelryui.R;
import simoncherry.jewelryui.adapter.CheckOutAdapter;
import simoncherry.jewelryui.adapter.RingAdapter;
import simoncherry.jewelryui.adapter.VpAdapter;
import simoncherry.jewelryui.custom.ObservableScrollView;
import simoncherry.jewelryui.custom.RecyclerViewPager;
import simoncherry.jewelryui.custom.ViewPager;
import simoncherry.jewelryui.jazzy.JazzyViewPager;
import simoncherry.jewelryui.lm.CircleZoomLayoutManager;



public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private int[] modelResId = {R.drawable.model1, R.drawable.model2, R.drawable.model3, R.drawable.model4, R.drawable.model5};
    private String[] ringName = {
            "ROSE GOLD SWIRL\nCROSS OVER RING", "HEART BOUQUET RING\nIN ROSE GOLD", "PAVE HEART SWIRL\nSTRETCH RING",
            "HORSE STONE RING\nROSE GOLD", "ROSE GOLD\nCRYSTAL RING"};
    private String[] ringName2 = {
            "Rose Gold Swirl\nCross Over Ring", "Heart Bouquet Ring\nIn Rose Gold", "Pave Heart Swirl\nStretch Ring",
            "Horse Stone Ring\nRose Gold", "Rose Gold\nCrystal Ring"};
    private String[] ringPrice = {"$ 19,90", "$ 22,90", "$ 16,90"};
    private String[] effects;
    private int index = 6;

    private RecyclerViewPager rvRing;
    private ObservableScrollView svContainer;
    private ScrollView svDetail;
    private BlurringView blurringView;
    private JazzyViewPager vpModel;
    private ImageView ivBlur;
    private TextView tvName;

    private FloatingActionButton floatingActionButton;
    private RelativeLayout layoutContainer;
    private RelativeLayout layoutFakeRipple;
    private RecyclerView rvCheckOut;
    private CheckOutAdapter coAdapter;
    private List<String> nameList;
    private List<String> priceList;
    private TextView tvTitle;
    private MorphingButton btnPay;

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
        initRvCheckOut();

        effects = getResources().getStringArray(R.array.jazzy_effects);
        setupJazziness(JazzyViewPager.TransitionEffect.valueOf(effects[index]));
        initScrollView();
        initFAB();

        initBlur();
    }

    private void initBlur() {
        if (isBlur) {
            blurringView.setBlurredView(ivBlur);
            blurringView.setVisibility(View.VISIBLE);
            ivBlur.setVisibility(View.GONE);
        } else {
            blurringView.setVisibility(View.GONE);
        }
    }

    private void findView() {
        rvRing =(RecyclerViewPager)findViewById(R.id.rv_ring);
        svContainer = (ObservableScrollView) findViewById(R.id.sv_container);
        svDetail = (ScrollView) findViewById(R.id.sv_detail);
        blurringView = (BlurringView) findViewById(R.id.blurring);
        vpModel = (JazzyViewPager) findViewById(R.id.vp_model);
        ivBlur = (ImageView) findViewById(R.id.iv_blur);
        tvName = (TextView) findViewById(R.id.tv_name);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        layoutContainer = (RelativeLayout) findViewById(R.id.layout_container);
        layoutFakeRipple = (RelativeLayout) findViewById(R.id.layout_fake_ripple);
        rvCheckOut = (RecyclerView) findViewById(R.id.rv_check_out);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        btnPay = (MorphingButton) findViewById(R.id.btn_pay);
    }

    private void initView() {
        setSpannableText(ringName[1]);
        morphToCircle(btnPay);
        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initFAB() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeJazzyEffect();
                if (floatingActionButton.getVisibility() == View.VISIBLE) {
                    FabTransformation.with(floatingActionButton)
                            .setListener(new FabTransformation.OnTransformListener() {
                                @Override
                                public void onStartTransform() {
                                    FabTransformation.with(floatingActionButton)
                                            .setListener(new FabTransformation.OnTransformListener() {
                                                @Override
                                                public void onStartTransform() {
                                                }
                                                @Override
                                                public void onEndTransform() {
                                                    showCheckOutItem();
                                                }
                                            })
                                            .transformTo(layoutContainer);
                                }
                                @Override
                                public void onEndTransform() {
                                }
                            })
                            .transformTo(layoutFakeRipple);
                }
            }
        });
    }

    private void initRvRing(){
        rvRing.addOnScrollListener(new CenterScrollListener());
        circleZoomLayoutManager = new CircleZoomLayoutManager(this, true);
        rvRing.setLayoutManager(circleZoomLayoutManager);
        rvRing.setAdapter(new RingAdapter(this));
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

    private void setupJazziness(JazzyViewPager.TransitionEffect effect) {
        vpModel.setTransitionEffect(effect);
        vpModel.setFadeEnabled(true);
        vpModel.setAdapter(new VpAdapter(this, vpModel));
        vpModel.setPageMargin(60);

        vpModel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("vpModel.onPageScrolled",
//                        "position:" + String.valueOf(position) +
//                                ", positionOffset:" + String.valueOf(positionOffset) +
//                                ", positionOffsetPixels:" + String.valueOf(positionOffsetPixels));
                if (isBlur) {
                    ivBlur.setImageResource(modelResId[position % modelResId.length]);
                    blurringView.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("vpModel.onPageSelected", "position:" + String.valueOf(position));
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

    private void initRvCheckOut() {
        rvCheckOut.setLayoutManager(new LinearLayoutManager(this));
        rvCheckOut.setItemAnimator(new SlideInUpAnimator());
        nameList = new ArrayList<>();
        priceList = new ArrayList<>();
        coAdapter = new CheckOutAdapter(this, nameList, priceList);
        rvCheckOut.setAdapter(coAdapter);
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
                setSpannableText(ringName[position % ringName.length]);
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
                    //Log.e(TAG, "空格位置: " + String.valueOf(index));
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
                //Log.e(TAG, "变更样式位置: " + String.valueOf(position));
                spannable.setSpan(new RelativeSizeSpan(1.5f), position, position + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tvName.setText(spannable);
    }

    private void addCheckOutItem(String name, String price) {
        coAdapter.add(name.replace("\n", " "), price, coAdapter.getItemCount());
    }

    private void showCheckOutItem() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slideInUp(tvTitle, new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        addCheckOutItem(ringName2[0], ringPrice[0]);
                        addCheckOutItem(ringName2[1], ringPrice[1]);
                        addCheckOutItem(ringName2[2], ringPrice[2]);
                        addCheckOutItem(" ", " ");
                        addCheckOutItem("TOTAL", "$ 59,70");
                        showPayBtn();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, 200);
    }

    private void slideInUp(View view, Animation.AnimationListener animationListener) {
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        TranslateAnimation transIn = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(transIn);
        animationSet.setDuration(200);

        if (animationListener != null) {
            animationSet.setAnimationListener(animationListener);
        }

        view.setVisibility(View.VISIBLE);
        view.clearAnimation();
        view.startAnimation(animationSet);
    }

    private void showPayBtn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //btnPay.setVisibility(View.VISIBLE);
                //morphToSquare(btnPay, 200);
                reboundIn(btnPay);
            }
        }, 200);
    }

    private void reboundIn(final View view) {
        ScaleAnimation scaleBig = new ScaleAnimation(
                0.0f, 1.2f, 0.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleBig.setDuration(100);

        final ScaleAnimation scaleSmall = new ScaleAnimation(
                1.2f, 1.0f, 1.2f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleSmall.setDuration(100);

        scaleBig.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(scaleSmall);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        scaleSmall.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                morphToSquare(btnPay, 200);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.clearAnimation();
        view.startAnimation(scaleBig);
    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(Dp2px(56))
                .width(Dp2px(200))
                .height(Dp2px(56))
                .color(getResources().getColor(R.color.colorPrimary))
                .colorPressed(getResources().getColor(R.color.colorPrimaryDark))
                .text("PAY");
        btnMorph.morph(square);
    }

    private void morphToCircle(final MorphingButton btnMorph) {
        btnMorph.setVisibility(View.INVISIBLE);
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(1)
                .cornerRadius(Dp2px(56))
                .width(Dp2px(56))
                .height(Dp2px(56))
                .color(getResources().getColor(R.color.colorPrimary))
                .colorPressed(getResources().getColor(R.color.colorPrimary))
                .text("");
        btnMorph.morph(circle);
    }

    @Override
    public void onBackPressed() {
        if (floatingActionButton.getVisibility() != View.VISIBLE) {
            FabTransformation.with(floatingActionButton)
                    .setListener(new FabTransformation.OnTransformListener() {
                        @Override
                        public void onStartTransform() {
                        }
                        @Override
                        public void onEndTransform() {
                            morphToCircle(btnPay);
                            tvTitle.setVisibility(View.INVISIBLE);
                            nameList.clear();
                        }
                    })
                    .transformFrom(layoutContainer);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FabTransformation.with(floatingActionButton)
                                    .transformFrom(layoutFakeRipple);
                        }
                    });
                }
            }, 100);

            return;
        }
        super.onBackPressed();
    }
}
