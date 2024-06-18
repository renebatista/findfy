package com.example.frontend_a3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend_a3.R;
import com.example.frontend_a3.client.RetrofitClient;
import com.example.frontend_a3.model.DesaparecidoModel;
import com.example.frontend_a3.services.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarAvistamentoActivity extends AppCompatActivity {

    private int desaparecidoId; // Alterado para int para simplificar

    private EditText descricaoEditText;
    private Button cadastrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_vizualizacao);

        descricaoEditText = findViewById(R.id.campo_descricao);
        cadastrarButton = findViewById(R.id.campo_botao_cadastar);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("desaparecido_id")) {
            desaparecidoId = intent.getIntExtra("desaparecido_id", -1); // -1 é um valor padrão caso não encontre
            Log.d("DEBUG", "ID do desaparecido = " + desaparecidoId);
        } else {
            Toast.makeText(this, "Desaparecido não encontrado.", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a activity se não houver dados
        }

        cadastrarButton.setOnClickListener(v -> cadastrarAvistamento());
    }

    private void cadastrarAvistamento() {
        String descricao = descricaoEditText.getText().toString();
        if (descricao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha a descrição do avistamento.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();

        String urlRequisicao = "/api-a3/adicionarAvistamento?id=" + desaparecidoId + "&comentario=" + descricao;

        Call<Void> call = apiService.cadastrarAvistamentoByUrl(urlRequisicao);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastrarAvistamentoActivity.this, "Avistamento cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish(); // Fecha a activity após o cadastro
                } else {
                    Toast.makeText(CadastrarAvistamentoActivity.this, "Erro ao cadastrar avistamento", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(CadastrarAvistamentoActivity.this, "Falha ao cadastrar avistamento: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
