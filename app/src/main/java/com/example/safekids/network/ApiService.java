package com.example.safekids.network;


import com.example.safekids.models.Family;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import okhttp3.MultipartBody;


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

    @Multipart
    @POST("api1/authPeoples")
    Call<AddFamilyResponse> addAuthorizedPerson(
            @Header("Authorization") String token,
            @Part MultipartBody.Part photo,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part("phone") RequestBody phone,
            @Part("relationship") RequestBody relationship,
            @Part("studentIds[]") List<RequestBody> studentIds
    );

    @Multipart
    @POST("api2/upload/authorizeds")
    Call<GenericResponse> uploadAuthorizedPhoto(
            @Header("Authorization") String token,
            @Part("school_id") RequestBody schoolId,
            @Part("id") RequestBody id,
            @Part("firstName") RequestBody firstName,
            @Part("lastName") RequestBody lastName,
            @Part MultipartBody.Part file
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

    @POST("api1/guardians/reset-password")
    @FormUrlEncoded
    Call<GenericResponse> resetPassword(
            @Field("email") String email
    );

    @POST("api1/guardians/password-challenge")
    @FormUrlEncoded
    Call<VerifyCodeResponse> verifyCode(
            @Field("code") String code
    );

    // Reenviar código
    @FormUrlEncoded
    @POST("api1/guardians/resend-2fa")
    Call<Resend2FAResponse> resend2FA(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("api1/guardians/resend-2fa")
    Call<GuardianResponse> two2FA(@Field("email") String email);

    @POST("api1/guardians/change-password")
    @FormUrlEncoded
    Call<ChangePasswordResponse> changePassword(
            @Field("resetToken") String resetToken,
            @Field("password") String password,
            @Field("password_confirmation") String passwordConfirmation
    );

}
