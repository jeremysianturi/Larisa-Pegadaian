package id.co.pegadaian.diarium.util;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGeneratorCollege {

  public static String PREF_COOKIES = "pref_cookies";

  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  private static Retrofit.Builder builder;


  public static <S> S createService(Class<S> serviceClass) {
    return createService(serviceClass, null);
  }

  public static <S> S createService(Class<S> serviceClass, final HashMap<String, String> headers) {

    builder = new Retrofit.Builder()
            .baseUrl("https://metamorph.pelindo1.co.id")
            .addConverterFactory(GsonConverterFactory.create());

    httpClient.sslSocketFactory(getSSLSocketFactory());
    httpClient.hostnameVerifier(new HostnameVerifier() {

      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });

    OkHttpClient client = httpClient.build();
    Retrofit retrofit = builder.client(client).build();
    return retrofit.create(serviceClass);
  }

  private static SSLSocketFactory getSSLSocketFactory() {
    try {
      // Create a trust manager that does not validate certificate chains
      final TrustManager[] trustAllCerts = new TrustManager[]{
              new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                  return new java.security.cert.X509Certificate[]{};
                }
              }
      };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      return sslSocketFactory;
    } catch (KeyManagementException | NoSuchAlgorithmException e) {
      return null;
    }

  }
}
