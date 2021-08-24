package id.co.pegadaian.diarium.util.qiscus.data.remote;

import com.google.gson.JsonObject;
import com.qiscus.sdk.Qiscus;
import com.qiscus.sdk.chat.core.data.remote.QiscusApi;

import java.util.concurrent.TimeUnit;

import id.co.pegadaian.diarium.util.qiscus.data.local.DataLocal;
import id.co.pegadaian.diarium.util.qiscus.data.pojo.Account;
import id.co.pegadaian.diarium.util.qiscus.util.Constant;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by adicatur on 12/24/16.
 */

public enum QismoWidgetApi {

    INSTANCE;
    private final String baseUrl = "https://qismo.qiscus.com/api/v1/";
    private Api api;
    private OkHttpClient httpClient;

    QismoWidgetApi() {

        httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }

    public static QismoWidgetApi getInstance() {
        return INSTANCE;
    }

    public Observable<Account> initiateChat(String email, String name) {

        DataLocal dataLocal = new DataLocal(Qiscus.getApps().getApplicationContext());
        return QiscusApi.getInstance().requestNonce()
                .flatMap(qiscusNonce -> api.initiateChat(Constant.QISCUS_APP_ID, email, name, qiscusNonce.getNonce())
                        .map(jsonElement -> jsonElement.getAsJsonObject().get("data").getAsJsonObject())
                        .map(this::parseAccount)
                        .doOnNext(dataLocal::setAccount)
                        .flatMap(account -> Qiscus.setUserAsObservable(account.getIdentityToken()))
                        .map(qiscusAccount -> dataLocal.getAccount()));
    }

    private Account parseAccount(JsonObject jsonObject) {
        Account account = new Account();
        account.setIdentityToken(jsonObject.getAsJsonObject().get("identity_token").getAsString());
        account.setRoomId(jsonObject.getAsJsonObject().get("room_id").getAsString());
        return account;
    }
}
