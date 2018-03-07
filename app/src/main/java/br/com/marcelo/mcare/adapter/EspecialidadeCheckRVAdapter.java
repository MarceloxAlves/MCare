package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.model.Medico;
import io.objectbox.Box;


public class EspecialidadeCheckRVAdapter extends RecyclerView.Adapter<EspecialidadeCheckRVAdapter.ViewHolder> {


    private Context contexto;
    private List<Especialidade> especialidades;
    private Box<Medico> medicoBox;
    private Medico medico;

    public EspecialidadeCheckRVAdapter(Box<Medico> box, List<Especialidade> lista, Context context, Medico medico ) {
        setEspecialidades(lista);
        this.contexto =  context;
        this.medicoBox =  box;
        this.medico =  medico;

    }

    public void setEspecialidades(List<Especialidade> lista ) {
        Collections.sort(lista);
        this.especialidades = lista;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_especialidade_check,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Especialidade especialidade = especialidades.get(position);
        holder.txtDescricao.setText(especialidade.getDescricao());

            if (medico.especialidade.hasA((Especialidade espe) ->{
                return espe.getId() == especialidade.getId();
            }) ){
                holder.checkBox.setChecked(true);
            }

        holder.itemView.setOnClickListener(v -> {
            if ( holder.checkBox.isChecked()) {
                holder.checkBox.setChecked(false);
                this.medico.especialidade.removeById(especialidade.getId());
                this.medico.especialidade.applyChangesToDb();
                this.medicoBox.put(this.medico);
            }
            else
            {
                holder.checkBox.setChecked(true);
                this.medico.especialidade.add(especialidade);
                this.medico.especialidade.applyChangesToDb();
                this.medicoBox.put(this.medico);

            }
        });

    }

    @Override
    public int getItemCount() {
        return especialidades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtDescricao;
        public CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_especialidade);
            checkBox = itemView.findViewById(R.id.cb_especialidade);

        }
    }

}
