package psu.signlinguaasl.ui.animations;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

public class LinearAnimations
{
    public static void SpinLeft(View view, float durationSecs)
    {
        spin(view, 0, -360, durationSecs);
    }

    public static void SpinRight(View view, float durationSecs)
    {
        spin(view, 0, 360, durationSecs);
    }

    private static void spin(View view, float from, float to, float speed)
    {
        // Create rotation animations for each gear
        RotateAnimation anim = new RotateAnimation(
                from, to,
                Animation.RELATIVE_TO_SELF, 0.5f,  // pivotX is center of the gear
                Animation.RELATIVE_TO_SELF, 0.5f   // pivotY is center of the gear
        );

        long duration = (long) speed * 1000;

        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);

        view.startAnimation(anim);
    }

    public static void animateTextViewExpansion(final TextView textView, final int maxLines, final boolean expand)
    {
        // Determine the target height
        int startHeight = textView.getHeight();
        textView.setMaxLines(expand ? Integer.MAX_VALUE : maxLines); // Temporarily set max lines for measurement
        textView.measure(
                View.MeasureSpec.makeMeasureSpec(textView.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        int endHeight = textView.getMeasuredHeight();

        // Reset max lines to avoid issues during animation
        textView.setMaxLines(Integer.MAX_VALUE);

        // Animate the height
        ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
        animator.setDuration(300); // Set animation duration
        animator.addUpdateListener(animation -> {
            textView.getLayoutParams().height = (int) animation.getAnimatedValue();
            textView.requestLayout();
        });

        // Set the final state after animation ends
        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                textView.setMaxLines(expand ? Integer.MAX_VALUE : maxLines);
                textView.requestLayout(); // Ensure proper layout update
            }
        });

        animator.start();
    }
}
