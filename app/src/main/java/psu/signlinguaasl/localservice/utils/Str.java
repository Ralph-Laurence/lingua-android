package psu.signlinguaasl.localservice.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * Some useful string methods
 */
public class Str
{
    public static boolean IsNullOrEmpty(String str)
    {
        return str == null || str.isEmpty();
    }

    public static String sanitize(String dangerous)
    {
        return Jsoup.clean(dangerous, Safelist.basic());
    }

    public static String pluralize(String subject, int with)
    {
        if (subject.endsWith("s"))
            return subject;

        return with == 1 ? subject : subject + 's';
    }
}
