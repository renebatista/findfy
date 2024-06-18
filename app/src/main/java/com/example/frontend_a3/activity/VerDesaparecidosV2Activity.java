package com.example.frontend_a3.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend_a3.R;
import com.example.frontend_a3.adapter.DesaparecidoV2Adapter;
import com.example.frontend_a3.client.RetrofitClient;
import com.example.frontend_a3.model.DesaparecidoModel;
import com.example.frontend_a3.services.ApiService;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerDesaparecidosV2Activity extends AppCompatActivity implements DesaparecidoV2Adapter.OnDesaparecidoClickListener {

    private RecyclerView recyclerView;
    private DesaparecidoV2Adapter desaparecidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_desaparecidos_v2);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchDesaparecidos();
    }

    private void fetchDesaparecidos() {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<DesaparecidoModel>> call = apiService.getDesaparecidos();
        call.enqueue(new Callback<List<DesaparecidoModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<DesaparecidoModel>> call, @NonNull Response<List<DesaparecidoModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DesaparecidoModel> desaparecidos = response.body();
                    desaparecidoAdapter = new DesaparecidoV2Adapter(desaparecidos, VerDesaparecidosV2Activity.this);
                    recyclerView.setAdapter(desaparecidoAdapter);

                    // Após configurar o adapter, buscar as imagens dos desaparecidos
                    fetchImagensDesaparecidos(desaparecidos);
                } else {
                    Toast.makeText(VerDesaparecidosV2Activity.this, "Erro ao buscar dados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DesaparecidoModel>> call, @NonNull Throwable t) {
                Toast.makeText(VerDesaparecidosV2Activity.this, "Falha na requisição: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchImagensDesaparecidos(List<DesaparecidoModel> desaparecidos) {
        Executor executor = Executors.newFixedThreadPool(5); // Executor com 5 threads
        for (final DesaparecidoModel desaparecido : desaparecidos) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    ApiService apiService = RetrofitClient.getApiService();
                    Call<ResponseBody> call = apiService.getImagemDesaparecido(desaparecido.getId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                                if (bitmap != null) {
                                    desaparecido.setImagemBitmap(bitmap);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (desaparecidoAdapter != null) {
                                                desaparecidoAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            Log.e("VerDesaparecidosV2Activity", "Falha ao obter imagem: " + t.getMessage());
                        }
                    });
                }
            });
        }
    }


    @Override
    public void onDesaparecidoClick(DesaparecidoModel desaparecido) {
        // Ação a ser realizada quando um desaparecido é clicado no RecyclerView
    }
}
