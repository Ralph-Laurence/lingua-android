package psu.signlinguaasl.ui.animations;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class Transitions
{
    public static void FadeOut(View view, int durationMillis, Runnable onFinish)
    {
        Fade(view, 1.0f, 0.0f, durationMillis, null, onFinish);
    }

    private static void Fade(View view, float fromAlpha, float toAlpha, int durationMillis, Runnable onStart, Runnable onFinish)
    {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha);
        fadeOut.setDuration(durationMillis); // Set the duration of the animation
        fadeOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // Code to execute when the animation starts

                if (onStart != null)
                    onStart.run();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // Code to execute when the animation ends

                if (onFinish != null)
                    onFinish.run();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // Code to execute if the animation is canceled
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Code to execute when the animation repeats
            }
        });
        fadeOut.start();
    }
}
