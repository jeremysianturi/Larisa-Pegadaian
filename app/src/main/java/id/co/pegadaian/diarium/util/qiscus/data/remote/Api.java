package id.co.pegadaian.diarium.util.qiscus.data.remote;

import com.google.gson.JsonElement;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by adicatur on 12/24/16.
 */

public interface Api {

    @FormUrlEncoded
    @POST("qiscus/initiate_chat")
    Observable<JsonElement> initiateChat(@Field("app_id") String appId,
                                         @Field("user_id") String email,
                                         @Field("name") String name,
                                         @Field("nonce") String nonce);


}
