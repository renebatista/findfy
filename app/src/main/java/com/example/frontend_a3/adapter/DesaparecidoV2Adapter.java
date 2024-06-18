package com.example.frontend_a3.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frontend_a3.R;
import com.example.frontend_a3.activity.VerAvistamentosActivity;
import com.example.frontend_a3.activity.VerDesaparecidosV2Activity;
import com.example.frontend_a3.model.DesaparecidoModel;

import java.util.List;

public class DesaparecidoV2Adapter extends RecyclerView.Adapter<DesaparecidoV2Adapter.DesaparecidoViewHolder> {

    private List<DesaparecidoModel> desaparecidos;
    private OnDesaparecidoClickListener listener;

    public DesaparecidoV2Adapter(List<DesaparecidoModel> desaparecidos, OnDesaparecidoClickListener listener) {
        this.desaparecidos = desaparecidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DesaparecidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desaparecido_v2, parent, false);
        return new DesaparecidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesaparecidoViewHolder holder, int position) {
        DesaparecidoModel desaparecidoModel = desaparecidos.get(position);
        holder.nomeTextView.setText(desaparecidoModel.getNome());
        holder.descricaoTextView.setText(desaparecidoModel.getDescricao());

        if (desaparecidoModel.getImagemBitmap() != null) {
            holder.fotoImageView.setImageBitmap(desaparecidoModel.getImagemBitmap());
        } else {
            holder.fotoImageView.setImageResource(R.drawable.ic_launcher_background); // Placeholder
        }

        // Listener para o botão "ADD AVISTAMENTO"
        holder.botaoVerAvistamento.setOnClickListener(v -> {
            listener.onDesaparecidoClick(desaparecidoModel);
        });

        // Listener para o botão "VER AVISTAMENTOS"
        holder.botaoVerAvistamento.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), VerAvistamentosActivity.class);
            intent.putExtra("desaparecido_id", desaparecidoModel.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return desaparecidos.size();
    }

    public interface OnDesaparecidoClickListener {
        void onDesaparecidoClick(DesaparecidoModel desaparecido);
    }

    public static class DesaparecidoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeTextView;
        TextView descricaoTextView;
        ImageView fotoImageView;
        Button botaoVerAvistamento;

        public DesaparecidoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nomeTextView_v2);
            descricaoTextView = itemView.findViewById(R.id.descricaoTextView_v2);
            fotoImageView = itemView.findViewById(R.id.fotoDesaparecido_v2);
            botaoVerAvistamento = itemView.findViewById(R.id.botao_ver_avistamento_v2); // Corrigido o ID do botão
        }
    }
}