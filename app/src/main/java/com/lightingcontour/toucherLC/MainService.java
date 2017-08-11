package com.lightingcontour.toucherLC;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainService extends Service {

    private static final String TAG = "MainService";

    //布局、布局参数、WindowManager
    ConstraintLayout toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;

    //几个按钮.
    ImageButton openbtn;
    ImageButton[] functionbtn = new ImageButton[5];

    //状态栏高度.
    int statusBarHeight = -1;

    //获取dpi,以确保布局大小不变
    int dpi = 0;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"MainService Created");

        DisplayMetrics dispalyMectrecs = getResources().getDisplayMetrics();
        dpi = dispalyMectrecs.densityDpi;

        createToucher();
        initComponents();
    }

    private void createToucher()
    {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.px = dp * (dpi / 160).
        //width&height均使用px.
        params.width = dpiToPxInt(150);
        params.height = dpiToPxInt(150);
        Log.i(TAG,"获得Width为：" + params.width);
        Log.i(TAG,"获得Height为：" + params.height);

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.toucherlayout,null);
        //添加toucherlayout
        windowManager.addView(toucherLayout,params);

        Log.i(TAG,"toucherlayout-->left:" + toucherLayout.getLeft());
        Log.i(TAG,"toucherlayout-->right:" + toucherLayout.getRight());
        Log.i(TAG,"toucherlayout-->top:" + toucherLayout.getTop());
        Log.i(TAG,"toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG,"状态栏高度为:" + statusBarHeight);
    }

    private void initComponents()
    {
        //浮动窗口按钮.
        openbtn = (ImageButton) toucherLayout.findViewById(R.id.openbtn);
        functionbtn[0] = (ImageButton) toucherLayout.findViewById(R.id.backbtn);
        functionbtn[1] = (ImageButton) toucherLayout.findViewById(R.id.menubtn);
        functionbtn[2] = (ImageButton) toucherLayout.findViewById(R.id.managerbtn);
        functionbtn[3] = (ImageButton) toucherLayout.findViewById(R.id.lockbtn);
        functionbtn[4] = (ImageButton) toucherLayout.findViewById(R.id.closebtn);

        for (ImageButton imageButton:functionbtn)
        {
            imageButton.setOnClickListener(onClickListener);
        }
        openbtn.setOnClickListener(onClickListener);

        openbtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                params.x = (int) event.getRawX() - toucherLayout.getWidth() / 2;
                params.y = (int) event.getRawY() - toucherLayout.getHeight() / 2 - statusBarHeight;
                windowManager.updateViewLayout(toucherLayout,params);
                return false;
            }
        });

        /*
         * 点击外部悬浮窗缩小.
         *
         */
        toucherLayout.setOnTouchListener(new View.OnTouchListener() {
            long hints[] = new long[2];
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
                {
                    if (functionbtn[0].getVisibility() == View.VISIBLE)
                    {
                        System.arraycopy(hints,1,hints,0,hints.length -1);
                        hints[hints.length -1] = SystemClock.uptimeMillis();
                        if (SystemClock.uptimeMillis() - hints[0] >= 800)
                        {
                            transformLayout();
                        }
                    }
                }
                return false;
            }
        });

        transformLayout();
    }

    /**
     * 按钮功能实现.
     *
     * */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        long[] hints = new long[2];
        @Override
        public void onClick(View v) {
            AnimatorUtil.clickAnimator(v);
            switch (v.getId())
            {
                case R.id.openbtn:
                    transformLayout();
                    break;
                case R.id.backbtn:

                    break;
                case R.id.menubtn:

                    break;
                case R.id.managerbtn:

                    break;
                case R.id.lockbtn:

                    break;
                case R.id.closebtn:
                    System.arraycopy(hints,1,hints,0,hints.length -1);
                    hints[hints.length -1] = SystemClock.uptimeMillis();
                    if (SystemClock.uptimeMillis() - hints[0] >= 1300)
                    {
                        Log.i(TAG,"检测到点击关闭按钮.");
                        ObjectAnimator confirm1 = ObjectAnimator.ofFloat(functionbtn[4],"rotation",0f,180f);
                        ObjectAnimator confirm2 = ObjectAnimator.ofFloat(functionbtn[4],"rotation",180f,0f);
                        confirm1.start();
                        AnimatorSet set = new AnimatorSet();
                        set.play(confirm2).after(1300);
                        set.start();
                        Toast.makeText(MainService.this,"连续点击两次以退出",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Log.i(TAG,"即将关闭");
                        stopSelf();
                    }
                    break;
            }
        }
    };

    /*
    * 将layout进行变形，并隐藏/显示出功能按钮.
    *
    * */
    private void transformLayout()
    {
        if (functionbtn[0].getVisibility() == View.VISIBLE)
        {

            AnimatorUtil.translationAnimator(functionbtn[0],0,52 * (dpi / 160),0,0,1);
            AnimatorUtil.translationAnimator(functionbtn[1],0,0,0,52 * (dpi / 160),1);
            AnimatorUtil.translationAnimator(functionbtn[2],0,-52 * (dpi / 160),0,0,1);
            AnimatorUtil.translationAnimator(functionbtn[3],0,0,0,-52 * (dpi / 160),1);
            AnimatorUtil.scaleAnimator(functionbtn[4],0,500);
            for (ImageButton imageButton:functionbtn)
            {
                AnimatorUtil.alphaAnimator(imageButton,0,500);
            }
            for (ImageButton imageButton:functionbtn)
            {
                imageButton.setVisibility(View.GONE);
            }
            params.width = dpiToPxInt(50);
            params.height = dpiToPxInt(50);
            params.x += dpiToPxInt(50);
            params.y += dpiToPxInt(50);
            openbtn.setVisibility(View.INVISIBLE);
            //防止出现跳跃动画.
            windowManager.removeView(toucherLayout);
            windowManager.addView(toucherLayout,params);
            openbtn.setVisibility(View.VISIBLE);
            ObjectAnimator.ofFloat(openbtn,"rotation",-180f,0f).setDuration(200).start();
        }else
        {
            params.width = dpiToPxInt(150);
            params.height = dpiToPxInt(150);
            params.x -= dpiToPxInt(50);
            params.y -= dpiToPxInt(50);
            //防止出现跳跃动画.
            windowManager.removeView(toucherLayout);
            windowManager.addView(toucherLayout,params);
            for (ImageButton imageButton:functionbtn)
            {
                imageButton.setVisibility(View.VISIBLE);
                AnimatorUtil.alphaAnimator(imageButton,1,500);
            }
            AnimatorUtil.translationAnimator(functionbtn[0],dpiToPxFloat(52),0,0,0,1);
            AnimatorUtil.translationAnimator(functionbtn[1],0,0,dpiToPxFloat(52),0,1);
            AnimatorUtil.translationAnimator(functionbtn[2],dpiToPxFloat(-52),0,0,0,1);
            AnimatorUtil.translationAnimator(functionbtn[3],0,0,dpiToPxFloat(-52),0,1);
            AnimatorUtil.scaleAnimator(functionbtn[4],1,500);
            ObjectAnimator.ofFloat(openbtn,"rotation",180f,0f).setDuration(200).start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openbtn.setVisibility(View.GONE);
                }
            },200);
        }
    }

    /**
     * dpi→px单位转化,整数型
     * 输入dpi，转化为px
     * */
    private int dpiToPxInt(int x)
    {
        return (x * dpi / 160);
    }

    /**
     * dpi→px单位转化,浮点型
     * 输入dpi，转化为px
     * */
    private float dpiToPxFloat(float x)
    {
        return (x * dpi / 160);
    }

    @Override
    public void onDestroy()
    {
        if (openbtn != null)
        {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }
}
