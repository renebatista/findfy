package com.example.frontend_a3.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.frontend_a3.R;
import java.util.List;

public class AvistamentoAdapter extends RecyclerView.Adapter<AvistamentoAdapter.AvistamentoViewHolder> {

    private List<String> avistamentos;

    public AvistamentoAdapter(List<String> avistamentos) {
        this.avistamentos = avistamentos;
    }

    @NonNull
    @Override
    public AvistamentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avistamento, parent, false);
        return new AvistamentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvistamentoViewHolder holder, int position) {
        String avistamento = avistamentos.get(position);
        holder.avistamentoTextView.setText(avistamento);
    }

    @Override
    public int getItemCount() {
        return avistamentos.size();
    }

    public static class AvistamentoViewHolder extends RecyclerView.ViewHolder {
        TextView avistamentoTextView;

        public AvistamentoViewHolder(@NonNull View itemView) {
            super(itemView);
            avistamentoTextView = itemView.findViewById(R.id.avistamentoTextView);
        }
    }
}
