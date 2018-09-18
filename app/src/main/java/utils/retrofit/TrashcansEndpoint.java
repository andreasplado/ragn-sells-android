package utils.retrofit;

import java.util.ArrayList;

import data.ResponseData;
import model.Trashcan;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Andreas on 03.04.2017.
 */

public interface TrashcansEndpoint {
    @GET("trashcans")
    Call<ArrayList<Trashcan>> getAllTrashcans(@Query("access_token")String accessToken);

    @GET("trashcans/{id}")
    Call<Trashcan> getTrashcanById(
            @Path("id") int id,
            @Query("access_token")String accessToken);

    @FormUrlEncoded
    @POST("trashcans")
    Call<ResponseData> postTrashcan(@Field("longitude") double longitude,
                                    @Field("latitude") double latitude,
                                    @Field("issue") String issue,
                                    @Field("user_id_fk") int user_id_fk,
                                    @Field("place_name") String place_name,
                                    @Field("access_token") String accessToken);
    @FormUrlEncoded
    @PUT("trashcans/{id}")
    Call<ResponseData> updateTrashcan(@Path("id")int id,
                                      @Field("issue") String issue,
                                      @Field("access_token") String accessToken);
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "trashcans/{id}",hasBody = true)
    //@DELETE("trashcans/{id}")
    Call<ResponseData> deleteTrashcan(@Path("id")int id,
                                      @Field("access_token") String accessToken);
    @GET("trashcanswithissues")
    Call<ArrayList<Trashcan>> getAllTrashcansWithIssues(@Query("access_token")String accessToken);

    @GET("usertrashcan/{user_id}")
    Call<ArrayList<Trashcan>> getAllUserTrashcans(@Path("user_id") int user_id,
                                                  @Query("access_token")String accessToken);
}
