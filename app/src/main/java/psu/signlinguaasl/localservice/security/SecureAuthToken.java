package psu.signlinguaasl.localservice.security;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

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

    public String getToken() {
        return sharedPreferences.getString("auth_token", null);
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }
}
