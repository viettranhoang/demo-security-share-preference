package com.vit.demoandroidkeystore;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;

public class PrefUtils {

    private static final String PREF_NAME = "VIT_PREF";

    private static final String TAG = PrefUtils.class.getSimpleName();
    private static final String ENCODING = "UTF-8";

    private SharedPreferences prefs;

    AesCbcWithIntegrity.SecretKeys keys;


    public PrefUtils(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        try {
            keys = AesCbcWithIntegrity.generateKey();
        } catch (Exception e) {
            keys = null;
            e.printStackTrace();
        }
    }

    public interface PREF_KEY {
        public static final String PREF_KEY_LAST_CACHE = "PREF_BUFFER_PACKAGE_NAME";
    }

    public void set(String key, String value) {
        encrypt(key, value);
    }

    public String get(String key, String defValue) {
        return decrypt(key, defValue);
    }

    public void set(String key, int value) {
        encrypt(key, Integer.toString(value));
    }

    public int get(String key, int defValue) {
        String value = decrypt(key, null);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defValue;
    }

    public void set(String key, boolean value) {
        encrypt(key, Boolean.toString(value));
    }

    public boolean get(String key, boolean defValue) {
        String value = decrypt(key, null);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defValue;
    }

    public void set(String key, long value) {
        encrypt(key, Long.toString(value));
    }

    public long get(String key, long defValue) {
        String value = decrypt(key, null);
        if (value != null) {
            return Long.parseLong(value);
        }
        return defValue;
    }

    public void clearKey(String key) {
        prefs.edit().remove(getHashed(key)).apply();
    }

    public void clearAllKey() {
        prefs.edit().clear().apply();
    }

    private void encrypt(String key, String value) {
        try {
            String hashedKey = getHashed(key);
            String encryptValue;
            if (TextUtils.isEmpty(value) || keys == null) {
                encryptValue = value;
            } else {
                encryptValue = AesCbcWithIntegrity.encrypt(value, keys).toString();
            }

            prefs.edit().putString(hashedKey, encryptValue).apply();
        } catch (Exception e) {
            Log.e(TAG, "encrypt: ", e);
        }
    }

    private String decrypt(String key, String defValue) {
        try {
            String hashedKey = getHashed(key);
            String value = prefs.getString(hashedKey, defValue);
            if (TextUtils.isEmpty(value) || keys == null) {
                return value;
            }
            if (value.equals(defValue))
                return defValue;
            AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = new AesCbcWithIntegrity.CipherTextIvMac(value);
            return AesCbcWithIntegrity.decryptString(cipherTextIvMac, keys);
        } catch (Exception e) {
            Log.e(TAG, "encrypt: ", e);
            return defValue;
        }
    }

    private String getHashed(String text) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] result = digest.digest(text.getBytes(ENCODING));

            StringBuilder sb = new StringBuilder();

            for (byte b : result) {
                sb.append(String.format("%02X", b));
            }

            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, "getHashed: ", e);
            return text;
        }
    }

}
