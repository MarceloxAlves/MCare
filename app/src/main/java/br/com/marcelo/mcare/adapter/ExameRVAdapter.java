package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.MainActivity;
import br.com.marcelo.mcare.app.ResultadoDetalheActivity;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Exame;
import br.com.marcelo.mcare.model.Resultado;
import br.com.marcelo.mcare.model.TipoExame;
import br.com.marcelo.mcare.model.TipoExame_;
import br.com.marcelo.mcare.model.TipoResultado;
import br.com.marcelo.mcare.model.TipoResultado_;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

/**
 * Created by Marcelo on 05/03/2018.
 */

public class ExameRVAdapter extends RecyclerView.Adapter<ExameRVAdapter.ViewHolder> {
    private Context contexto;
    private List<Exame> exames;
    private Box<Exame> exameBox;
    private Resultado resultado;

    public ExameRVAdapter(Box<Exame> box, List<Exame> lista, Context context) {
        this.exames = lista;
        this.contexto =  context;
        this.exameBox =  box;

    }

    @Override
    public ExameRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_exame,parent,false);
        ExameRVAdapter.ViewHolder viewHolder = new ExameRVAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ExameRVAdapter.ViewHolder holder, int position) {
        Exame exame = exames.get(position);
        holder.txtTipoExame.setText(exame.tipoExame.getTarget().getDescricao());
        String formato = new SimpleDateFormat("dd-MM-yyyy").format(exame.getData());
        holder.txtData.setText(formato);
        String tipos =  "| ";
        List<TipoResultado> tipoResultadoList = exame.tipoExame.getTarget().tipoResultados;
        for (int i = 0; i < tipoResultadoList.size() ; i++) {
                tipos += tipoResultadoList.get(i).getDescricao() + " | ";
        }

        holder.txtTipoResultados.setText(tipos);

        holder.itemView.setOnClickListener(v->{
             Intent intent = new Intent(contexto, ResultadoDetalheActivity.class);
             intent.putExtra("exameId",exame.getId());
             contexto.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(view -> {
            setupLongClick(position, exame, view);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return exames.size();
    }


    public void setexames(List<Exame> lista) {
        this.exames = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtData;
        public TextView txtTipoExame;
        public TextView txtTipoResultados;


        public ViewHolder(View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txt_data);
            txtTipoExame = itemView.findViewById(R.id.txt_tipo_exame);
            txtTipoResultados = itemView.findViewById(R.id.txt_tipos_resultados);

        }
    }

    private void setupLongClick(int position, Exame exame, View view) {
        PopupMenu popupMenu = new PopupMenu(this.contexto, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_action_exame, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_deletar:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
                    builder.setTitle(R.string.deletar);
                    builder.setMessage(contexto.getString(R.string.deseja_deletar) + exame.tipoExame.getTarget().getDescricao() + "?");
                    builder.setPositiveButton(R.string.sim, (dialog, i) -> {
                        exames.remove(exame);
                        exameBox.remove(exame);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, getItemCount());
                        Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton(R.string.nao, (dialog, i) -> popupMenu.dismiss());

                    builder.create().show();
                    break;
                case R.id.item_editar:
                    Toast.makeText(contexto, "TODO", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.item_add_resultado:
                        adicionarResultado(exame);
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void adicionarResultado(Exame exame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        FragmentActivity fragmentActivity = ((FragmentActivity)(contexto));
        View inflate = ((FragmentActivity)(contexto)).getLayoutInflater().inflate(R.layout.activity_tipo_resultado_form, null);
        Spinner spinner = inflate.findViewById(R.id.spinner_tipo_resultado);
        EditText valorResultado = inflate.findViewById(R.id.edit_resultado);


        resultado = null;

        List<TipoResultado> tipoResultadoList = exame.tipoExame.getTarget().tipoResultados;


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoResultado tipo = tipoResultadoList.get(position);
                for (Resultado res: exame.resultados ) {
                    if (res.tipoResultadoToOne.getTarget().getId() == tipo.getId()) {
                        resultado = res;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<TipoResultado> arrayAdapter = new ArrayAdapter<TipoResultado>(
                contexto, android.R.layout.simple_spinner_item, tipoResultadoList);

        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        spinner.setAdapter(arrayAdapter);


        builder.setTitle(fragmentActivity.getString(R.string.add_exame));
        builder.setIcon(fragmentActivity.getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try {
                        Box<Resultado> resultadoBox =  ((App)fragmentActivity.getApplication()).getBoxStore().boxFor(Resultado.class);
                        resultado.setValor(Double.valueOf(Validator.validade(valorResultado)));
                        resultadoBox.put(resultado);
                        notifyDataSetChanged();
                        Toast.makeText(contexto, fragmentActivity.getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }


}
