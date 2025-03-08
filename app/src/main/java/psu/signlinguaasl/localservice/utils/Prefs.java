package psu.signlinguaasl.localservice.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs
{
    public static final String PREF_KEY_FIRST_RUN = "signlingua_first_run";

    private static final String PREFS_KEY = "signlingua_prefs";
    private static Prefs m_instance;
    private SharedPreferences m_sharedPrefs;

    private Prefs(Context context)
    {
        // Private constructor to prevent instantiation
        m_sharedPrefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
    }

    // Ensures thread safety
    public static synchronized Prefs getInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance = new Prefs(context.getApplicationContext());
        }
        return m_instance;
    }

    public void store(String key, boolean value)
    {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putBoolean(key, value); // Use the boolean value you want to store

        // async
        editor.apply();

        // or editor.commit() synchronous;
    }

    public void store(String key, String value)
    {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void store(String key, int value)
    {
        SharedPreferences.Editor editor = m_sharedPrefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public boolean read(String key, boolean defaultValue)
    {
        return m_sharedPrefs.getBoolean(key, defaultValue);
    }

    public String read(String key, String defaultValue)
    {
        return m_sharedPrefs.getString(key, defaultValue);
    }

    public int read(String key, int defaultValue)
    {
        return m_sharedPrefs.getInt(key, defaultValue);
    }
}
