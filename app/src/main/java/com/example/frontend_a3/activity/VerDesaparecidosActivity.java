package com.example.frontend_a3.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend_a3.R;
import com.example.frontend_a3.adapter.DesaparecidoAdapter;
import com.example.frontend_a3.client.RetrofitClient;
import com.example.frontend_a3.model.DesaparecidoModel;
import com.example.frontend_a3.services.ApiService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerDesaparecidosActivity extends AppCompatActivity implements DesaparecidoAdapter.OnDesaparecidoClickListener {

    private RecyclerView recyclerView;
    private DesaparecidoAdapter desaparecidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_desaparecidos);

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
                    desaparecidoAdapter = new DesaparecidoAdapter(desaparecidos, VerDesaparecidosActivity.this);
                    recyclerView.setAdapter(desaparecidoAdapter);
                    fetchImagensDesaparecidos(desaparecidos);
                } else {
                    Toast.makeText(VerDesaparecidosActivity.this, "Erro ao buscar dados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DesaparecidoModel>> call, @NonNull Throwable t) {
                Toast.makeText(VerDesaparecidosActivity.this, "Falha na requisição: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            desaparecidoAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                            Toast.makeText(VerDesaparecidosActivity.this, "Falha ao obter imagem: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDesaparecidoClick(DesaparecidoModel desaparecido) {
        // Abrir a tela de cadastro de avistamento passando o ID do desaparecido clicado
        Intent intent = new Intent(this, CadastrarAvistamentoActivity.class);
        intent.putExtra("desaparecido_id", desaparecido.getId());
        startActivity(intent);
    }
}
