package com.example.frontend_a3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend_a3.R;
import com.example.frontend_a3.model.DesaparecidoModel;
import java.util.List;

public class DesaparecidoAdapter extends RecyclerView.Adapter<DesaparecidoAdapter.DesaparecidoViewHolder> {

    private List<DesaparecidoModel> desaparecidos;
    private OnDesaparecidoClickListener listener;

    public DesaparecidoAdapter(List<DesaparecidoModel> desaparecidos, OnDesaparecidoClickListener listener) {
        this.desaparecidos = desaparecidos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DesaparecidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_desaparecido, parent, false);
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

        holder.botaoAddAvistamento.setOnClickListener(v -> {
            listener.onDesaparecidoClick(desaparecidoModel);
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
        Button botaoAddAvistamento;

        public DesaparecidoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeTextView = itemView.findViewById(R.id.nomeTextView);
            descricaoTextView = itemView.findViewById(R.id.descricaoTextView);
            fotoImageView = itemView.findViewById(R.id.fotoDesaparecido);
            botaoAddAvistamento = itemView.findViewById(R.id.botao_add_avistamento);
        }
    }
}
