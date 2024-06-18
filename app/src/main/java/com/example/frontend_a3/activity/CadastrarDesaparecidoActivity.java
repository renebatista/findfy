package com.example.frontend_a3.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend_a3.R;
import com.example.frontend_a3.client.RetrofitClient;
import com.example.frontend_a3.model.DesaparecidoModel;
import com.example.frontend_a3.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarDesaparecidoActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Bitmap selectedImageBitmap;
    private EditText nomeEditText, dataNascimentoEditText, descricaoEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_desaparecido);

        imageView = findViewById(R.id.image_view);
        nomeEditText = findViewById(R.id.campo_nome);
        dataNascimentoEditText = findViewById(R.id.campo_dataNascimento);
        descricaoEditText = findViewById(R.id.campo_descricao);
        Button selectImageButton = findViewById(R.id.button_select_image);
        Button cadastrarButton = findViewById(R.id.campo_botao_cadastrar);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarDesaparecido();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void cadastrarDesaparecido() {
        String nome = nomeEditText.getText().toString();
        String dataNascimento = dataNascimentoEditText.getText().toString();
        String descricao = descricaoEditText.getText().toString();
        String status = "DESAPARECIDO";

        if (nome.isEmpty() || dataNascimento.isEmpty() || descricao.isEmpty() || selectedImageBitmap == null) {
            Toast.makeText(this, "Por favor, preencha todos os campos e selecione uma imagem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Formatar a data de nascimento para o formato esperado
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        String dataNascimentoFormatted = sdf.format(new Date());

        // Converter a imagem Bitmap para base64
        String imagemBase64 = bitmapToBase64(selectedImageBitmap);

        // Criar objeto DesaparecidoModel com os dados
        DesaparecidoModel desaparecidoModel = new DesaparecidoModel();
        desaparecidoModel.setNome(nome);
        desaparecidoModel.setDataNascimento(dataNascimentoFormatted);
        desaparecidoModel.setDescricao(descricao);
        desaparecidoModel.setStatus(status);

        // Converter o objeto DesaparecidoModel para JSON
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        String desaparecidoJson = gson.toJson(desaparecidoModel);

        // Enviar requisição POST para a API
        ApiService apiService = RetrofitClient.getApiService();

        // Preparar imagem para envio
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        RequestBody imagemRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part imagemPart = MultipartBody.Part.createFormData("imagem", "image.jpg", imagemRequestBody);

        // Preparar texto JSON para envio
        RequestBody desaparecidoJsonBody = RequestBody.create(MediaType.parse("multipart/form-data"), desaparecidoJson);

        // Enviar requisição
        Call<Void> call = apiService.cadastrarDesaparecido(desaparecidoJsonBody, imagemPart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CadastrarDesaparecidoActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CadastrarDesaparecidoActivity.this, "Falha ao realizar cadastro. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("Cadastro", "Erro no cadastro: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CadastrarDesaparecidoActivity.this, "Erro de conexão: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Cadastro", "Erro de conexão: " + t.getMessage());
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
