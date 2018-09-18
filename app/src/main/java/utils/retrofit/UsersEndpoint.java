package utils.retrofit;

import java.util.ArrayList;

import data.ResponseData;
import data.UserData;
import model.Trashcan;
import model.User;
import model.auth.AuthResponse;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andreas on 05.05.2017.
 */

public interface UsersEndpoint {

    @FormUrlEncoded
    @POST("oauth/access_token")
    Call<AuthResponse> getToken(@Field("grant_type") String grantType,
                                @Field("client_id") String clientId,
                                @Field("client_secret") String clientSecret,
                                @Field("username") String username,
                                @Field("password") String password);

    @FormUrlEncoded
    @POST("logout")
    Call<ResponseData> logout(@Field("access_token") String accessToken);

    @GET("user/{username}")
    Call<User> getUser(@Path("username") String username,
                       @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseData> registerUser(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("role") String role,
                                    @Field("phone_nr") String phoneNr);

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ResponseData> updateName(@Path("id")int id,
                                  @Field("name") String name,
                                  @Field("access_token") String accessToken);
    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ResponseData> updateUserPhone(@Path("id")int id,
                                       @Field("phone_nr") String phone_nr,
                                       @Field("access_token") String accessToken);
    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ResponseData> updateUserPass(@Path("id")int id,
                                      @Field("password") String password,
                                      @Field("access_token") String accessToken);

    @FormUrlEncoded
    @PUT("users/{id}")
    Call<ResponseData> updateUserRole(@Path("id")int id,
                                      @Field("role") String role,
                                      @Field("access_token") String accessToken);
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "users/{id}",hasBody = true)
    Call<ResponseData> deleteUser(@Path("id")int id,
                                  @Field("access_token") String accessToken);
}
