package com.example.frontend_a3.services;

import com.example.frontend_a3.model.DesaparecidoModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET("/api-a3/listarDesaparecidos")
    Call<List<DesaparecidoModel>> getDesaparecidos();

    @GET("/api-a3/puxarFotoDesaparecido/{id}")
    Call<ResponseBody> getImagemDesaparecido(@Path("id") Integer id);

    @Multipart
    @POST("/api-a3/cadastrarDesaparecido")
    Call<Void> cadastrarDesaparecido(
            @Part("desaparecidoJson") RequestBody desaparecidoJson,
            @Part MultipartBody.Part imagem
    );

    @GET
    Call<List<String>> getAvistamentos(@Url String url);

    @POST
    Call<Void> cadastrarAvistamentoByUrl(@Url String url);
}
