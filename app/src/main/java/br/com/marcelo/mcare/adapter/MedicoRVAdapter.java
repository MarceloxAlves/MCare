package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.marcelo.mcare.model.Medico;
import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.MedicoActivity;
import br.com.marcelo.mcare.app.MedicoDetalheActivity;
import io.objectbox.Box;

/**
 * Created by Marcelo on 03/03/2018.
 */

public class MedicoRVAdapter extends RecyclerView.Adapter<MedicoRVAdapter.ViewHolder> {
    private Context contexto;
    private List<Medico> Medicos;
    private Box<Medico> MedicoBox;

    public MedicoRVAdapter(Box<Medico> box, List<Medico> lista, Context context) {
        Medicos = new ArrayList<>();
        setMedicos(lista);
        this.contexto =  context;
        this.MedicoBox =  box;

    }

    public void setMedicos(List<Medico> lista) {
        Collections.sort(lista);
        this.Medicos = lista;
    }


    @Override
    public MedicoRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_medico,parent,false);
        MedicoRVAdapter.ViewHolder viewHolder = new MedicoRVAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MedicoRVAdapter.ViewHolder holder, int position) {
        Medico medico = Medicos.get(position);
        holder.txtDescricao.setText(medico.getNome());
        holder.txtCrm.setText(medico.getCrm());
        if(medico.getFoto()!=0)
             holder.imgMedico.setImageDrawable(this.contexto.getDrawable(medico.getFoto()));

        holder.itemView.setOnClickListener(
                v->{
                     Intent intent = new Intent(this.contexto, MedicoDetalheActivity.class);
                     intent.putExtra("medicoId",medico.getId());
                     this.contexto.startActivity(intent);
                });

        holder.itemView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.contexto, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_action_medico, popupMenu.getMenu());
           // popupMenu.getMenu().findItem(R.id.item_marcar_consulta).setVisible(false);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.item_deletar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
                        builder.setTitle("Deletar");
                        builder.setMessage(contexto.getString(R.string.deseja_deletar) + medico.getNome() + "?");
                        builder.setPositiveButton(R.string.sim, (dialog, i) -> {
                            Medicos.remove(medico);
                            MedicoBox.remove(medico);
                            medico.especialidade.applyChangesToDb();
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton(R.string.nao, (dialog, i) -> popupMenu.dismiss());

                        builder.create().show();
                        break;
                    case R.id.item_editar:
                        ((MedicoActivity)contexto).addMedico(holder.itemView,medico);
                        break;
                    case R.id.item_adicionar_especialidade:
                        ((MedicoActivity)contexto).addEspecialidade(medico);
                        break;
                    case R.id.item_marcar_consulta:
                        ((MedicoActivity)contexto).agendarConsulta(medico);
                        break;
                }
                return true;
            });
            popupMenu.show();
            return true;
        });
    }



    @Override
    public int getItemCount() {
        return Medicos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtDescricao;
        public TextView txtCrm;
        public ImageView imgMedico;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);
            txtCrm = itemView.findViewById(R.id.txt_crm);
            imgMedico = itemView.findViewById(R.id.image_medico);

        }
    }
}
