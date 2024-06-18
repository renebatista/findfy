package com.example.frontend_a3.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend_a3.R;
import com.example.frontend_a3.adapter.AvistamentoAdapter;
import com.example.frontend_a3.client.RetrofitClient;
import com.example.frontend_a3.services.ApiService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerAvistamentosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AvistamentoAdapter avistamentoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_avistamentos);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int desaparecidoId = getIntent().getIntExtra("desaparecido_id", -1);
        if (desaparecidoId != -1) {
            fetchAvistamentos(desaparecidoId);
        } else {
            Toast.makeText(this, "ID do desaparecido não fornecido", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchAvistamentos(int desaparecidoId) {
        ApiService apiService = RetrofitClient.getApiService();

        String urlRequisicao = "/api-a3/listarAvistamentosPorIdDesaparecido?desaparecidoId=" + desaparecidoId;

        Call<List<String>> call = apiService.getAvistamentos(urlRequisicao);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(@NonNull Call<List<String>> call, @NonNull Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        List<String> avistamentos = new ArrayList<>();
                        avistamentos.add("Esse desaparecido não possui avistamentos!");
                        avistamentoAdapter = new AvistamentoAdapter(avistamentos);
                        recyclerView.setAdapter(avistamentoAdapter);
                    }
                    else {
                        List<String> avistamentos = response.body();
                        avistamentoAdapter = new AvistamentoAdapter(avistamentos);
                        recyclerView.setAdapter(avistamentoAdapter);
                    }
                } else {
                    Toast.makeText(VerAvistamentosActivity.this, "Erro ao buscar avistamentos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<String>> call, @NonNull Throwable t) {
                Toast.makeText(VerAvistamentosActivity.this, "Falha na requisição: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
