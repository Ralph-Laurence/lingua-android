package psu.signlinguaasl.localservice.auth;

import android.content.Context;

import psu.signlinguaasl.localservice.models.User;
import psu.signlinguaasl.localservice.security.SecureAuthToken;

// Singleton pattern
public class AuthenticatedSession
{
    private static AuthenticatedSession m_instance;
    private static SecureAuthToken m_authToken;
    private static User m_user;

    private AuthenticatedSession() {
        // Private constructor to prevent instantiation
    }

    // Ensures thread safety
    public static synchronized AuthenticatedSession getInstance(Context context)
    {
        if (m_instance == null)
        {
            m_instance = new AuthenticatedSession();
            m_authToken = new SecureAuthToken(context.getApplicationContext());
        }
        return m_instance;
    }

    public void setUser(User user) {
        m_user = user;
    }

    public User getUser() {
        return m_user;
    }

    public void setAuthToken(String token) {
        m_authToken.saveToken(token);
    }

    public String getAuthToken() {
        return m_authToken.getToken();
    }

    public void clearSession() {
        m_authToken.clear();
        m_user = null;
    }
}
