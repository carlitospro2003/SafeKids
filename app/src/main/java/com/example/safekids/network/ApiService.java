package com.example.safekids.network;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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


}
