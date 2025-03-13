package psu.signlinguaasl.localservice.auth;

import android.content.Context;

import kotlin.Pair;
import psu.signlinguaasl.localservice.models.Credentials;
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

    // Cache the user instance in memory
    public void setUser(User user) { m_user = user; }

    // Write the auth token to disk
    public void setAuthToken(String token) { m_tokenStore.saveToken(token); }

    public void rememberUserId(String hashedId) {
        m_tokenStore.saveUserHash(hashedId);
    }

    // Read the cached user instance from memory
    public User getUser() { return m_user; }

    // Read the auth token from disk
    public String getAuthToken() { return m_tokenStore.getToken(); }

    // Read the hashed user id from disk
    public String getUserId()
    {
        // Read from the memory first ...
        if (m_user != null)
            return m_user.getHashedId();

        // If none, read from the cache instead
        return m_tokenStore.getHashedId();
    }

    public void setSession(String token, User user)
    {
        setUser(user);
        setAuthToken(token);
    }

    // Write the credentials on to the secure storage as these
    // will be used for "Remember Me" option during login.

    public void rememberUserCredentials(String umail, String password)
    {
        m_tokenStore.saveAuthCredentials(new Credentials(umail, password));
    }

    public Credentials loadUserCredentials()
    {
        Credentials credentials = m_tokenStore.getAuthCredentials();

        if (Str.IsNullOrEmpty(credentials.getUmail()) ||
            Str.IsNullOrEmpty(credentials.getPassword()))
            return null;

        return credentials;
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
