package psu.signlinguaasl.localservice.auth;

import android.content.Context;

import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.localservice.security.SecureAuthToken;
import psu.signlinguaasl.localservice.utils.Str;

// Singleton pattern
public class AuthenticatedSession
{
    private static AuthenticatedSession m_instance;
    private static User m_user;
    private static SecureAuthToken m_tokenStore;

    private AuthenticatedSession() {
        // Private constructor to prevent instantiation
    }

    // Ensures thread safety
    public static synchronized AuthenticatedSession getInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance   = new AuthenticatedSession();
            m_tokenStore = new SecureAuthToken(context);
        }
        return m_instance;
    }
    //-----------------------------------------
    //    D A T A  E N C A P S U L A T I O N
    //-----------------------------------------
    public void setUser(User user) {
        m_user = user;
    }

    public User getUser() {
        return m_user;
    }

    public void setAuthToken(String token) {
        m_tokenStore.saveToken(token);
    }

    public String getAuthToken() {
        return m_tokenStore.getToken();
    }
    //-------------------------------------
    //      B U S I N E S S  L O G I C
    //-------------------------------------
    public void setSession(String token, User user)
    {
        setUser(user);
        setAuthToken(token);
    }

    public boolean checkSession()
    {
        return !Str.IsNullOrEmpty(getAuthToken());
    }

    public void clearSession() {
        m_tokenStore.clear();
        m_user = null;
    }
}
