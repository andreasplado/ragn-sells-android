package utils.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andreas on 03.04.2017.
 */

public class RetrofitBuilder {

    public static Retrofit retrofit;
    public static Gson gson;
    public static String baseURL = null;
    public static boolean isLenient = false;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    public RetrofitBuilder(String baseURL){
        this.baseURL = baseURL;
    }

    /**
     * Builds the retrofit for basic crud operations
     * @param baseURL base url of the link
     * @param isLenient shows if the data is in correct format (POJO)(isLenient)http://www.jsonschema2pojo.org/
     */
    public RetrofitBuilder(String baseURL, boolean isLenient){
        this.isLenient = isLenient;
        this.baseURL = baseURL;
    }
    public void build() {

        if(!isLenient){
            gson = new GsonBuilder()
                    .setLenient() //sets te lanient if the data is not in correct format
                    .create();
        }else{
            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
        }


        retrofit = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        //apiService =
        //retrofit.create(BaseInterface.class);

    }

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}