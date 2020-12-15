package com.femsa.sferea.Utilities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;

public class AnimationManager
{

    private ArrayList<AnimatorSet> allSets;

    public void stopAnimations()
    {
        for (int i = 0; i < allSets.size(); i++)
        {
            allSets.get(i).cancel();
            allSets.get(i).removeAllListeners();
        }
        allSets.clear();
    }

    public void stopAnimation(AnimatorSet animToStop)
    {
        int index = allSets.indexOf(animToStop);
        if(index > 0)
        {
            allSets.get(index).cancel();
            allSets.get(index).removeAllListeners();
            allSets.remove(animToStop);
        }
    }

    public AnimationManager() {
        allSets = new ArrayList<>();
    }

    public AnimatorSet getNewTranslateAnimForView(View toAnimate, float targetXPosition, float targetYPosition, long startAnimDelay,int animDuration) {
        return getNewTranslateAnimForView(toAnimate,0,0,targetXPosition,targetYPosition,startAnimDelay,animDuration);
    }

    public AnimatorSet getNewTranslateAnimForView(View toAnimate, float initXPosition, float initYPosition, float targetXPosition, float targetYPosition, long startAnimDelay,int animDuration) {
        ObjectAnimator moveX = ObjectAnimator.ofFloat(toAnimate, View.TRANSLATION_X,initXPosition, targetXPosition);
        moveX.setDuration(animDuration);
        ObjectAnimator moveY = ObjectAnimator.ofFloat(toAnimate, View.TRANSLATION_Y,initYPosition, targetYPosition);
        moveY.setDuration(animDuration);
        AnimatorSet result = new AnimatorSet();

        result.play(moveX);
        result.play(moveY);
        result.setStartDelay(startAnimDelay);
        result.start();
        allSets.add(result);
        return result;
    }

    public AnimatorSet getNewScaleAnimForView(View toAnimate, float initialScale, float targetScale, int animDuration, int startAnimDelay) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(toAnimate, View.SCALE_X, initialScale, targetScale);
        scaleX.setDuration(animDuration);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(toAnimate, View.SCALE_Y, initialScale, targetScale);
        scaleY.setDuration(animDuration);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        AnimatorSet result = new AnimatorSet();
        result.play(scaleX);
        result.play(scaleY);
        result.setStartDelay(startAnimDelay);
        result.start();
        allSets.add(result);
        return result;
    }

    public AnimatorSet getNewRotateAnimForView(View toAnimate, float initialRotation, float targetRotation, long animDuration) {
        return getNewRotateAnimForView(toAnimate,initialRotation,targetRotation,animDuration,0);
    }

    public AnimatorSet getNewRotateAnimForView(View toAnimate, float initialRotation, float targetRotation, long animDuration,long startDelay) {
        ObjectAnimator spin = ObjectAnimator.ofFloat(toAnimate, View.ROTATION, initialRotation, targetRotation);
        spin.setDuration(animDuration);
        spin.setStartDelay(startDelay);

        AnimatorSet result = new AnimatorSet();
        result.play(spin);
        result.start();
        allSets.add(result);
        return result;
    }

    public AnimatorSet getNewAlphaAnimForView(View toAnimate,float initialAlpha, float targetAlpha, long animDuration) {
        ObjectAnimator spin = ObjectAnimator.ofFloat(toAnimate, View.ALPHA,initialAlpha,targetAlpha);
        spin.setDuration(animDuration);

        AnimatorSet result = new AnimatorSet();
        result.play(spin);
        result.start();
        allSets.add(result);
        return result;
    }
}
