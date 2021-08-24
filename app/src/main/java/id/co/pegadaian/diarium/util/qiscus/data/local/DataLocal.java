package id.co.pegadaian.diarium.util.qiscus.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import id.co.pegadaian.diarium.util.qiscus.data.pojo.Account;


public class DataLocal {
    private SharedPreferences sharedPreferences;

    public DataLocal(Context context) {
        sharedPreferences = context.getSharedPreferences("local", Context.MODE_PRIVATE);
    }

    public void setAccount(Account account) {
        Gson gson = new Gson();
        String json = gson.toJson(account);
        sharedPreferences.edit()
                .putString("account", json)
                .apply();
    }

    public Account getAccount() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString("account", "");
        return gson.fromJson(json, Account.class);
    }
}
