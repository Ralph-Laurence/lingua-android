package psu.signlinguaasl.localservice.utils;

import android.content.Context;
import android.util.TypedValue;

public class Convert
{
    public static int PxToDp(Context context, float px)
    {
        return (int) TypedValue.applyDimension
        (
            TypedValue.COMPLEX_UNIT_DIP,
            px,
            context.getResources().getDisplayMetrics()
        );
    }

    public static int DpToPx(Context context, int dp)
    {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }
}
