package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.marcelo.mcare.model.Clinica;
import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.ClinicaActivity;
import io.objectbox.Box;

/**
 * Created by Marcelo on 05/03/2018.
 */

public class ClinicaRVAdapter extends RecyclerView.Adapter<ClinicaRVAdapter.ViewHolder> {

    private Context contexto;
    private List<Clinica> clinicas;
    private Box<Clinica> clinicaBox;

    public ClinicaRVAdapter(Box<Clinica> box, List<Clinica> lista, Context context) {
        clinicas = new ArrayList<>();
        setClinicas(lista);
        this.contexto =  context;
        this.clinicaBox =  box;

    }

    @Override
    public ClinicaRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_simple,parent,false);
        ClinicaRVAdapter.ViewHolder viewHolder = new ClinicaRVAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ClinicaRVAdapter.ViewHolder holder, int position) {
        Clinica clinica = clinicas.get(position);
        holder.txtNome.setText(clinica.getNome());
        holder.itemView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.contexto, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_action_default, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.item_deletar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
                        builder.setTitle(R.string.deletar);
                        builder.setMessage(contexto.getString(R.string.deseja_deletar) + clinica.getNome() + "?");
                        builder.setPositiveButton(R.string.sim, (dialog, i) -> {
                            clinicas.remove(clinica);
                            clinicaBox.remove(clinica);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton(R.string.nao, (dialog, i) -> popupMenu.dismiss());

                        builder.create().show();
                        break;
                    case R.id.item_editar:
                        ((ClinicaActivity)contexto).addClinica(holder.itemView,clinica);
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
        return clinicas.size();
    }

    public void setClinicas(List<Clinica> lista) {
        Collections.sort(lista);
        this.clinicas = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNome;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txt_descricao);

        }
    }


}
