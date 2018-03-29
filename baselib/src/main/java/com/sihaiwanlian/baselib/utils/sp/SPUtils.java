package com.sihaiwanlian.baselib.utils.sp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Lin on 2016/7/7.
 */
public class SPUtils {
    private SharedPreferences mSettings = null;
    private Context mContext;

    public SPUtils(Context context)
    {
        this.mContext = context;
        this.mSettings = PreferenceManager.getDefaultSharedPreferences(this.mContext);
    }

    public void saveBooleanValue(String key, boolean bValue)
    {
        SharedPreferences.Editor editor = this.mSettings.edit();
        editor.putBoolean(key, bValue);
        editor.commit();
    }

    public boolean getBooleanValue(String key)
    {
        return this.mSettings.getBoolean(key, false);
    }

    public boolean getBooleanValue(String key, boolean defaultValue)
    {
        return this.mSettings.getBoolean(key, defaultValue);
    }

    public void saveIntValue(String key, int nValue)
    {
        SharedPreferences.Editor editor = this.mSettings.edit();
        editor.putInt(key, nValue);
        editor.commit();
    }

    public int getIntValue(String key, int defaultVal)
    {
        return this.mSettings.getInt(key, defaultVal);
    }

    public void saveLongValue(String key, long lValue)
    {
        SharedPreferences.Editor editor = this.mSettings.edit();
        editor.putLong(key, lValue);
        editor.commit();
    }

    public long getLongValue(String key)
    {
        return this.mSettings.getLong(key, 0L);
    }

    public void saveStringValue(String key, String strValue)
    {
        SharedPreferences.Editor editor = this.mSettings.edit();
        editor.putString(key, strValue);
        editor.commit();
    }

    public String getStringValue(String key)
    {
        return this.mSettings.getString(key, "");
    }

    public String getStringValue(String key, String defaultVal)
    {
        return this.mSettings.getString(key, defaultVal);
    }

    public void saveListValue(String key, List<String> list)
    {
        int size = list == null ? 0 : list.size();
        Set<String> sets = new HashSet();
        if (size > 0) {
            sets.addAll(list);
        }
        SharedPreferences.Editor editor = this.mSettings.edit();
        editor.remove(key);
        editor.commit();
        editor.putStringSet(key, sets);
        editor.commit();
    }

    public List<String> getListValue(String key)
    {
        Set<String> stringSet = this.mSettings.getStringSet(key, new HashSet());
        List<String> list = new ArrayList();
        list.addAll(stringSet);
        return list;
    }

    @SuppressLint({"NewApi"})
    public void saveObjectValue(String key, Object objectValue)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(objectValue);

            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), 0));
            SharedPreferences.Editor editor = this.mSettings.edit();
            editor.putString(key, oAuth_Base64);
            editor.commit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public Object getObjectValue(String key)
    {
        byte[] base64Bytes = Base64.decode(this.mSettings.getString(key, ""), 0);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = null;
        Object obj = null;
        try
        {
            ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public void clear(){
        this.mSettings.edit().clear().commit();
    }

}
