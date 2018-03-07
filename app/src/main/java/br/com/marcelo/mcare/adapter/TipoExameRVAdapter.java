package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.TipoExameActivity;
import br.com.marcelo.mcare.model.TipoExame;
import br.com.marcelo.mcare.model.TipoResultado;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;


public class TipoExameRVAdapter extends RecyclerView.Adapter<TipoExameRVAdapter.ViewHolder> {


    private Context contexto;
    private List<TipoExame> tipoExames;
    private Box<TipoExame> tipoExameBox;

    public TipoExameRVAdapter(Box<TipoExame> box, List<TipoExame> lista, Context context) {
        tipoExames = new ArrayList<>();
        settipoExames(lista);
        this.contexto =  context;
        this.tipoExameBox =  box;

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
        final TipoExame tipoExame = tipoExames.get(position);
        holder.txtDescricao.setText(tipoExame.getDescricao());

        setupLongClick(holder, position, tipoExame);

        holder.itemView.setOnClickListener(v -> {

        });


    }

    @Override
    public int getItemCount() {
        return tipoExames.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtDescricao;
        public ViewHolder(View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txt_descricao);

        }
    }

    public void settipoExames(List<TipoExame> lista) {
        Collections.sort(lista);
        this.tipoExames = lista;
    }

    private void setupLongClick(ViewHolder holder, int position, TipoExame tipoExame) {
        holder.itemView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(this.contexto, view);
            popupMenu.getMenuInflater().inflate(R.menu.menu_action_tipo_exame, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.item_deletar:
                        AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
                        builder.setTitle("Deletar");
                        builder.setMessage("Deseja deletar " + tipoExame.getDescricao() + "?");
                        builder.setPositiveButton("SIM", (dialog, i) -> {
                            tipoExames.remove(tipoExame);
                            tipoExameBox.remove(tipoExame);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
                        });
                        builder.setNegativeButton("NAO", (dialog, i) -> popupMenu.dismiss());

                        builder.create().show();
                        break;
                    case R.id.item_editar:
                        ((TipoExameActivity)contexto).addTipoExame(holder.itemView,tipoExame);
                        break;
                    case R.id.item_add_resultado:
                        adicionarResultado(position,tipoExame,popupMenu);
                        break;
                }
                return true;
            });
            popupMenu.show();
            return true;
        });
    }

    private void adicionarResultado(int position, TipoExame tipoExame, PopupMenu popupMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        FragmentActivity fragmentActivity = ((FragmentActivity)(contexto));
        View inflate = ((FragmentActivity)(contexto)).getLayoutInflater().inflate(R.layout.activity_especialidade_form, null);

        EditText textDescricao =  inflate.findViewById(R.id.edit_decricao);

        builder.setTitle(fragmentActivity.getString(R.string.add_resultado));
        builder.setIcon(fragmentActivity.getResources().getDrawable(R.drawable.ic_resultado));
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try {
                         TipoResultado resultado= new TipoResultado();
                         resultado.setDescricao(Validator.validade(textDescricao));
                         resultado.tipoExameToOne.setTarget(tipoExame);
                         tipoExame.tipoResultados.add(resultado);
                         tipoExameBox.put(tipoExame);
                        Toast.makeText(contexto, fragmentActivity.getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

}
