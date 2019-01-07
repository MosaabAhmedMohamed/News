package com.example.mosaab.news.Remote;

import com.example.mosaab.news.Model.AddNews;
import com.example.mosaab.news.Model.Auth;
import com.example.mosaab.news.Model.Data;
import com.example.mosaab.news.Model.News;
import com.example.mosaab.news.Model.user;
import com.example.mosaab.news.Model.response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface API_Service {

    @Headers(
            {"Key: Content-Type, value: application/x-www-form-urlencoded",
                    "Key:Accept, value: application/json"})
    @POST("registration")
    Call<user> Reg_newUser(@Body user body);


    @POST("login")
    Call<response> Login_User(@Body user body);


    @GET("user/")
    Call<Auth> get_Auth_user( @Header("Accept") String accept, @Header("Authorization") String Auth );

    @GET()
    Call<Object> getStringResponse( @Header("Accept") String accept, @Header("Authorization") String Auth ,@Url String url);

    @GET("news")
    Call<Data> get_all_news( @Header("Accept") String accept, @Header("Authorization") String Auth );


    @POST("news/add")
    Call<AddNews> add_news( @Header("Accept") String accept, @Header("Authorization") String Auth, @Body AddNews addNews);


    @POST("news/{id}/update")
    Call<News> update_news(@Header("Accept") String accept, @Header("Authorization") String Auth , @Path("id") int postId, @Body News news);


    @POST("news/{id}/delete")
    Call<News> delete_news(@Header("Accept") String accept,@Header("Authorization") String Auth,@Path("id") int postId );





}
