package psu.signlinguaasl.localservice.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import org.javatuples.Quartet;
import org.javatuples.Triplet;

import java.util.HashMap;

import psu.signlinguaasl.R;
import psu.signlinguaasl.localservice.auth.AuthenticatedSession;
import psu.signlinguaasl.localservice.security.SecureAuthToken;

public class Disabilities
{
    public static final int NO_DISABILITY = 0;
    public static final int DISABILITY_DEAF = 1;
    public static final int DISABILITY_MUTE = 2;
    public static final int DISABILITY_DEAF_MUTE = 3;

    // Id, Name, Desc, Badge
    private static HashMap<Integer, Triplet<String, String, Integer>> disabilityMap;

    private static Disabilities m_instance;

    private Disabilities() {
        // Private constructor to prevent instantiation
    }

    // Ensures thread safety
    public static synchronized Disabilities getInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance    = new Disabilities();
            disabilityMap = new HashMap<>();
            disabilityMap.put(NO_DISABILITY,        new Triplet<>("No Impairments", "No hearing or speech impairments.", -1));
            disabilityMap.put(DISABILITY_DEAF,      new Triplet<>("Deaf or Hard of Hearing", "Difficulty hearing or completely deaf.", R.drawable.disability_deaf));
            disabilityMap.put(DISABILITY_MUTE,      new Triplet<>("Mute or Non-Verbal", "Difficulty speaking or do not speak at all.", R.drawable.disability_mute));
            disabilityMap.put(DISABILITY_DEAF_MUTE, new Triplet<>("Deaf and Non-Verbal", "Difficulty hearing and speaking, or do not hear and speak at all.", R.drawable.disability_deafmute));
        }

        return m_instance;
    }

    public Triplet<String, String, Integer> map(int disability)
    {
        if (disabilityMap == null)
            return null;

        if (!disabilityMap.containsKey(disability))
            return null;

        return disabilityMap.get(disability);
    }

    public Drawable mapBadgeIcon(Context context, int disability)
    {
        if (disabilityMap == null)
            return null;

        if (!disabilityMap.containsKey(disability))
            return null;

        Triplet<String, String, Integer> item = disabilityMap.get(disability);
        int iconResource = item.getValue2();

        if (iconResource == -1)
            return null;

        return ContextCompat.getDrawable(context, iconResource);
    }
}
