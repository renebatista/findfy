package com.example.frontend_a3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.frontend_a3.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonVerDesaparecidos = findViewById(R.id.botao_ver_desaparecidos);
        buttonVerDesaparecidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VerDesaparecidosActivity.class);
                startActivity(intent);
            }
        });

        Button buttonCadastrarDesaparecido = findViewById(R.id.botao_cadastrar_desaparecido);
        buttonCadastrarDesaparecido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastrarDesaparecidoActivity.class);
                startActivity(intent);
            }
        });

        Button buttonVisualizarAvistamentos = findViewById(R.id.botao_ver_desaparecido);
        buttonVisualizarAvistamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VerDesaparecidosV2Activity.class);
                startActivity(intent);
            }
        });
    }
}
