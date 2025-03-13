package psu.signlinguaasl.localservice.security;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import psu.signlinguaasl.localservice.models.Credentials;

public class SecureAuthToken
{
    private SharedPreferences sharedPreferences;

    public SecureAuthToken(Context context)
    {
        try{
            String masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            sharedPreferences = EncryptedSharedPreferences.create(
                    "secure_prefs",
                    masterKey,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString("auth_token", token).apply();
    }

    public void saveUserHash(String hashedId) {
        sharedPreferences.edit().putString("hashed_id", hashedId).apply();
    }

    // Useful for "Remember Me"
    public void saveAuthCredentials(Credentials credentials)
    {
        sharedPreferences.edit().putString("cred_umail", credentials.getUmail()).apply();
        sharedPreferences.edit().putString("cred_password", credentials.getPassword()).apply();
    }

    public Credentials getAuthCredentials()
    {
        String umail = sharedPreferences.getString("cred_umail", null);
        String passw = sharedPreferences.getString("cred_password", null);

        return new Credentials(umail, passw);
    }

    public String getToken() {
        return sharedPreferences.getString("auth_token", null);
    }

    public String getHashedId() {
        return sharedPreferences.getString("hashed_id", null);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
