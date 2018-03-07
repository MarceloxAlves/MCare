package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.MedicoActivity;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.app.EspecialidadeActivity;
import io.objectbox.Box;


public class EspecialidadeRVAdapter extends RecyclerView.Adapter<EspecialidadeRVAdapter.ViewHolder> {


    private Context contexto;
    private List<Especialidade> especialidades;
    private Box<Especialidade> especialidadeBox;

    public EspecialidadeRVAdapter(Box<Especialidade> box, List<Especialidade> lista, Context context) {
        especialidades = new ArrayList<>();
        setEspecialidades(lista);
        this.contexto =  context;
        this.especialidadeBox =  box;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_simple,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Especialidade especialidade = especialidades.get(position);
        holder.txtDescricao.setText(especialidade.getDescricao());

        setupLongClick(holder, position, especialidade);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(contexto, MedicoActivity.class);
            intent.putExtra("especialidadeId",especialidade.getId());
            (contexto).startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return especialidades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtDescricao;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);

        }
    }

    public void setEspecialidades(List<Especialidade> lista) {
        Collections.sort(lista);
        this.especialidades = lista;
    }

    private void setupLongClick(ViewHolder holder, int position, Especialidade especialidade) {
        holder.itemView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.contexto, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_action_default, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.item_deletar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
                        builder.setTitle("Deletar");
                        builder.setMessage("Deseja deletar " + especialidade.getDescricao() + "?");
                        builder.setPositiveButton("SIM", (dialog, i) -> {
                            especialidades.remove(especialidade);
                            especialidadeBox.remove(especialidade);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("NAO", (dialog, i) -> popupMenu.dismiss());

                        builder.create().show();
                        break;
                    case R.id.item_editar:
                        ((EspecialidadeActivity)contexto).addEspecialidade(holder.itemView,especialidade);
                        break;
                }
                return true;
            });
            popupMenu.show();
            return true;
        });
    }
}
