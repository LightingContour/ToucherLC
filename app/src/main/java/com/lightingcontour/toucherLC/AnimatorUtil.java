package com.lightingcontour.toucherLC;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by lich6389 on 2017/7/25.
 */

/**
* 动画类，用于存放各种动画方法.
* */
public class AnimatorUtil {
    /**
    *点击动画，适合按钮点击时使用.
    * @param view 要使用动画的组件.
    * */
    public static void clickAnimator(View view)
    {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,"scaleX",1f,1.2f,1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,"scaleY",1f,1.2f,1f);
        animatorX.setDuration(500).setInterpolator(new BounceInterpolator());
        animatorX.start();
        animatorY.setDuration(500).setInterpolator(new BounceInterpolator());
        animatorY.start();
    }

    /**
     * 过程动画，适合打开/关闭动作时使用.
     * @param view     要使用动画的组件.
     * @param f1       起始的translationX值.
     * @param f2       结束的translationX值.
     * @param f3       起始的translationY值.
     * @param f4       结束的translationY值.
     * @param inter    要使用的插值器.0为不使用,1为回弹插值器,2为预期插值器.
     * @param duration 动画持续时间(ms)
     *
    * */
    public static void translationAnimator(View view, float f1, float f2, float f3, float f4, int inter, int duration)
    {
        ObjectAnimator transX = ObjectAnimator.ofFloat(view,"translationX",f1,f2);
        ObjectAnimator transY = ObjectAnimator.ofFloat(view,"translationY",f3,f4);
        switch (inter)
        {
            case 1:
                //回弹插值器，多用于放出动画.
                transX.setDuration(duration).setInterpolator(new BounceInterpolator());
                transY.setDuration(duration).setInterpolator(new BounceInterpolator());
                break;
            case 2:
                //预期插值器，多用于回收动画.
                transX.setDuration(duration).setInterpolator(new AnticipateInterpolator());
                transY.setDuration(duration).setInterpolator(new AnticipateInterpolator());
                break;
            default:
                break;
        }
        transX.start();
        transY.start();
    }

    public static void translationAnimator(View view, float f1, float f2, float f3, float f4, int inter)
    {
        ObjectAnimator transX = ObjectAnimator.ofFloat(view,"translationX",f1,f2);
        ObjectAnimator transY = ObjectAnimator.ofFloat(view,"translationY",f3,f4);
        switch (inter)
        {
            case 1:
                //回弹插值器，多用于放出动画.
                transX.setDuration(500).setInterpolator(new BounceInterpolator());
                transY.setDuration(500).setInterpolator(new BounceInterpolator());
                break;
            case 2:
                //预期插值器，多用于回收动画.
                transX.setDuration(500).setInterpolator(new AnticipateInterpolator());
                transY.setDuration(500).setInterpolator(new AnticipateInterpolator());
        }
        transX.start();
        transY.start();
    }

    /**
     * 缩放动画
     * scaleX & scaleY → 0 | 1.
     * 用于组件消失|显现过渡.
     * 注意：使用后组件的ScaleX & ScaleY值将改变!建议配套使用消失 & 显现.
     * @param view 目标组件.
     * @param duration 动画时间.
     * @param inter 效果.0为消失，1为出现.
     * */
    public static void scaleAnimator(View view,int inter,int duration)
    {
        switch (inter)
        {
            case 0:
                ObjectAnimator scaleXGone = ObjectAnimator.ofFloat(view,"scaleX",1f,0f).setDuration(duration);
                ObjectAnimator scaleYGone = ObjectAnimator.ofFloat(view,"scaleY",1f,0f).setDuration(duration);
                scaleXGone.setInterpolator(new AnticipateInterpolator());
                scaleYGone.setInterpolator(new AnticipateInterpolator());
                scaleXGone.start();
                scaleYGone.start();
                break;
            case 1:
                ObjectAnimator scaleXCome = ObjectAnimator.ofFloat(view,"scaleX",0f,1f).setDuration(duration);
                ObjectAnimator scaleYCome = ObjectAnimator.ofFloat(view,"scaleY",0f,1f).setDuration(duration);
                scaleXCome.setInterpolator(new AnticipateInterpolator());
                scaleYCome.setInterpolator(new AnticipateInterpolator());
                scaleXCome.start();
                scaleYCome.start();
                break;
            default:
                break;
        }
    }

    /**
     * 透明度动画
     * Alpha → 0 | 1.
     * 用于组件消失|显现过渡.
     * 注意：使用后组件的Alpha值将改变!建议配套使用消失 & 显现.
     * @param view 目标组件.
     * @param duration 动画时间.
     * @param inter 效果.0为消失，1为出现.
     * */
    public static void alphaAnimator(View view,int inter,int duration)
    {
        switch (inter)
        {
            case 0:
                ObjectAnimator.ofFloat(view,"alpha",1f,0f).setDuration(duration).start();
                break;
            case 1:
                ObjectAnimator.ofFloat(view,"alpha",0f,1f).setDuration(duration).start();
                break;
            default:
                break;
        }
    }
}
