package psu.signlinguaasl.ui.animations;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class FadeViewPagerPageTransformer implements ViewPager2.PageTransformer
{
    @Override
    public void transformPage(View page, float position)
    {
        float MIN_SCALE = 0.5f; // Animate the view item to resize Half the height & width
        float MIN_ALPHA = 0.5f; // And also half the transparency

        if (position <= -1 || position >= 1)
        {
            page.setAlpha(0f);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }
        else if (position == 0)
        {
            page.setAlpha(1f);
            page.setScaleX(1f);
            page.setScaleY(1f);
        }
        else
        {
            float scaleFactor = MIN_SCALE + (1 - Math.abs(position)) * (1 - MIN_SCALE);
            float alphaFactor = MIN_ALPHA + (1 - Math.abs(position)) * (1 - MIN_ALPHA);

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);
            page.setAlpha(alphaFactor);
        }
    }
}
