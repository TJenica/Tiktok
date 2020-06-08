package com.hcl.tiktok;

import android.view.animation.*;
import android.widget.ImageView;

import java.util.Random;

public class LikeAnination {
    private static int[] angles = new int[]{-30, 0, 30};

    public static Animation playAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        int degrees = angles[new Random().nextInt(3)];
        animationSet.addAnimation(rotateAnim(0, 0, degrees));
        animationSet.addAnimation(scaleAnim(100, 2f, 1f, 0));
        animationSet.addAnimation(alphaAnim(0, 1, 100, 0));
        animationSet.addAnimation(scaleAnim(500, 1f, 1.8f, 300));
        animationSet.addAnimation(alphaAnim(1f, 0, 500, 300));
        animationSet.addAnimation(translationAnim(500, 0, 0, 0, -400, 300));
        return animationSet;
    }

    private static ScaleAnimation scaleAnim(long time, float from, float to, long offsetTime) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(from, to, from, to,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setStartOffset(offsetTime);
        scaleAnimation.setInterpolator(new DecelerateInterpolator());
        scaleAnimation.setDuration(time);
        return scaleAnimation;
    }

    private static RotateAnimation rotateAnim(long time, int fromDegrees, float toDegrees) {
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(time);
        return rotateAnimation;
    }

    private static TranslateAnimation translationAnim(long time, float fromX, float toX, float fromY, float toY, long offsetTime) {
        TranslateAnimation anim = new TranslateAnimation(fromX, toX, fromY, toY);
        anim.setDuration(time);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setStartOffset(offsetTime);
        return anim;
    }

    private static AlphaAnimation alphaAnim(float fromAlpha, float toAlpha, long duration, long offsetTime) {
        AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
        anim.setDuration(duration);
        anim.setStartOffset(offsetTime);
        return anim;
    }
}
