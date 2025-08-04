package com.example.safekids.network;


import com.example.safekids.models.Family;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @Headers("Accept: application/json")
    @POST("api1/guardians/login")
    Call<GuardianResponse> loginGuardian(@Body LoginRequest loginRequest);

    @Headers("Accept: application/json")
    @POST("api1/guardians/verify-2fa")
    Call<GuardianResponse> verify2FA(@Body Verify2FARequest request);

    @POST("api1/guardians/logout")
    Call<GuardianResponse> logoutGuardian(@Header("Authorization") String authHeader);

    @Headers("Accept: application/json")
    @POST("api1/guardians/new-password")
    Call<GuardianResponse> changePassword(
            @Header("Authorization") String authHeader,
            @Body ChangePasswordRequest request
    );

    @GET("api1/authPeoples/my-authorizeds/{studentId}")
    Call<AuthorizedResponse> getAuthorizedPeoples(
            @Header("Authorization") String token,
            @Path("studentId") int studentId
    );

    @Headers("Accept: application/json")
    @POST("api1/authPeoples/{studentId}")
    Call<GuardianResponse> addAuthorizedFamily(
            @Header("Authorization") String authHeader,
            @Path("studentId") int studentId,
            @Body AddFamilyRequest request
    );

    @PUT("api1/authPeoples/{id}")
    Call<UpdateFamilyResponse> updateFamily(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body Family family
    );

    @DELETE("api1/authPeoples/{id}")
    Call<DeleteFamilyResponse> deleteFamily(
            @Header("Authorization") String token,
            @Path("id") int id
    );






}
