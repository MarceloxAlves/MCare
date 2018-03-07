package br.com.marcelo.mcare.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.app.ConsultaFragment;
import br.com.marcelo.mcare.app.MainActivity;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Consulta;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.model.Exame;
import br.com.marcelo.mcare.model.Login;
import br.com.marcelo.mcare.model.Resultado;
import br.com.marcelo.mcare.model.StatusConsulta;
import br.com.marcelo.mcare.model.TipoExame;
import br.com.marcelo.mcare.model.TipoExame_;
import br.com.marcelo.mcare.model.TipoResultado;
import br.com.marcelo.mcare.model.Usuario;
import br.com.marcelo.mcare.utils.TextoFormato;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

/**
 * Created by Marcelo on 05/03/2018.
 */

public class ConsultaRVAdapter extends RecyclerView.Adapter<ConsultaRVAdapter.ViewHolder> {
    private Context contexto;
    private List<Consulta> consultas;
    private Box<Consulta> consultaBox;
    private Login login;

    public ConsultaRVAdapter(Box<Consulta> box, List<Consulta> lista, Context context) {
        this.consultas = lista;
        this.contexto =  context;
        this.consultaBox =  box;
        login = new Login(context);

    }

    @Override
    public ConsultaRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.contexto);
        View view =  layoutInflater.inflate(R.layout.view_holder_consulta,parent,false);
        ConsultaRVAdapter.ViewHolder viewHolder = new ConsultaRVAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ConsultaRVAdapter.ViewHolder holder, int position) {
        Consulta consulta = consultas.get(position);

        holder.txtHora.setText(String.valueOf(consulta.getStatus()));
        if(!consulta.medico.isNull())
             holder.txtNomeMedico.setText(consulta.medico.getTarget().getNome());
        holder.txtHora.setText(consulta.getHorario());
        Date date = consulta.getData();
        String formato = new SimpleDateFormat("dd-MM-yyyy").format(date);
        holder.txtData.setText(formato);
        String especialidades =  "| ";
        List<Especialidade> especialidadesList = consulta.medico.getTarget().especialidade;
        for (int i = 0; i < especialidadesList.size() ; i++) {
            especialidades += especialidadesList.get(i).getDescricao() + " | ";
        }
        if(!consulta.especialidadeToOne.isNull()) {
            holder.txtEspecialidade.setTypeface(null, Typeface.BOLD);
            holder.txtEspecialidade.setText(consulta.especialidadeToOne.getTarget().getDescricao());
        }else{
            holder.txtEspecialidade.setText(especialidades);
        }
        Date formatoTime = date;
        try {
            formatoTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(date.toString().concat(consulta.getHorario()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(formatoTime.compareTo(new Date()) == -1 && consulta.getStatus() == StatusConsulta.AGENDADA){
                consulta.setStatus(StatusConsulta.FINALIZADA);
                consultaBox.put(consulta);
        }

        setCorStatus(holder, consulta);

        if(!consulta.especialidadeToOne.isNull()) {
            holder.txtEspecialidade.setText(consulta.especialidadeToOne.getTarget().getDescricao());
        }
        holder.itemView.setOnLongClickListener(view -> {
            setupLongClick(position, consulta, view);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return consultas.size();
    }

    public void setconsultas(List<Consulta> lista) {
        this.consultas = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtData;
        public TextView txtHora;
        public TextView txtNomeMedico;
        public TextView txtStatus;
        public TextView txtEspecialidade;


        public ViewHolder(View itemView) {
            super(itemView);
            txtData = itemView.findViewById(R.id.txt_data);
            txtHora = itemView.findViewById(R.id.txt_hora);
            txtNomeMedico = itemView.findViewById(R.id.txt_nome_medico);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtEspecialidade = itemView.findViewById(R.id.txt_especialidade);

        }
    }

    private void setupLongClick(int position, Consulta consulta, View view) {
        PopupMenu popupMenu = new PopupMenu(this.contexto, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_action_consulta, popupMenu.getMenu());
        if(consulta.getStatus() == StatusConsulta.FINALIZADA || consulta.getStatus() == StatusConsulta.CANCELADA)
            popupMenu.getMenu().removeItem(R.id.item_cancelar);

        if(consulta.getStatus() != StatusConsulta.FINALIZADA)
            popupMenu.getMenu().removeItem(R.id.item_add_exame);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_deletar:
                    deletarConsulta(position, consulta, popupMenu);
                    break;
                case R.id.item_cancelar:
                    consulta.setStatus(StatusConsulta.CANCELADA);
                    consultaBox.put(consulta);
                    notifyDataSetChanged();
                    break;
                case R.id.item_add_exame:
                    adicionarExame(position, consulta, popupMenu);
                    break;
            }
            return true;
        });
        popupMenu.show();
    }

    private void deletarConsulta(int position, Consulta consulta, PopupMenu popupMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.contexto);
        builder.setTitle(R.string.deletar);
        builder.setMessage(contexto.getString(R.string.deseja_deletar) + consulta.getStatus() + "?");
        builder.setPositiveButton(R.string.sim, (dialog, i) -> {
            consultas.remove(consulta);
            consultaBox.remove(consulta);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
            Toast.makeText(this.contexto, this.contexto.getString(R.string.item_removido), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(R.string.nao, (dialog, i) -> popupMenu.dismiss());

        builder.create().show();
    }

    private void adicionarExame(int position, Consulta consulta, PopupMenu popupMenu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        FragmentActivity fragmentActivity = ((FragmentActivity)(contexto));
        View inflate = ((FragmentActivity)(contexto)).getLayoutInflater().inflate(R.layout.activity_exame_form, null);
        Spinner spinner = inflate.findViewById(R.id.spinner_tipo_exame);
        EditText nota = inflate.findViewById(R.id.txt_nota);
        EditText dataExame = inflate.findViewById(R.id.data_exame);
        ImageButton btnData = inflate.findViewById(R.id.btn_datapicker);

        btnData.setOnClickListener(v-> { setupData(dataExame); });

        Exame exame = new Exame();

        Box<TipoExame> tipoExameBox = ((App)fragmentActivity.getApplication()).getBoxStore().boxFor(TipoExame.class);
        List<TipoExame> tipoExameList = tipoExameBox.query().order(TipoExame_.descricao).build().find();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exame.tipoExame.setTarget(tipoExameList.get(position));
                exame.resultados.clear();
                for (TipoResultado tipo: tipoExameList.get(position).tipoResultados) {
                    Resultado resultado = new Resultado();
                    resultado.exameToOne.setTarget(exame);
                    resultado.tipoResultadoToOne.setTarget(tipo);
                    resultado.setDescricao(tipo.getDescricao());
                    resultado.setValor(0.0);
                    exame.resultados.add(resultado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<TipoExame> arrayAdapter = new ArrayAdapter<TipoExame>(
                contexto, android.R.layout.simple_spinner_item, tipoExameList);

        arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);

        spinner.setAdapter(arrayAdapter);


        builder.setTitle(fragmentActivity.getString(R.string.add_exame));
        builder.setIcon(fragmentActivity.getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try {
                       Box<Exame> exameBox =  ((App)fragmentActivity.getApplication()).getBoxStore().boxFor(Exame.class);
                        exame.consultaToOne.setTarget(consulta);
                        exame.setNota(Validator.validade(nota));
                        exame.usuarioToOne.setTarget(login.getUsuarioLogado());
                        SimpleDateFormat formato =  new SimpleDateFormat("dd-MM-yyyy");
                        Date data = formato.parse(Validator.validade(dataExame));
                        exame.setData(data);

                        exameBox.put(exame);
                        notifyDataSetChanged();
                        Toast.makeText(contexto, fragmentActivity.getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();

                    } catch (Exception ex) {
                        Toast.makeText(contexto, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

    private void setCorStatus(ViewHolder holder, Consulta consulta) {
        switch (consulta.getStatus()){
            case StatusConsulta.AGENDADA:
                holder.itemView.setBackgroundColor(contexto.getResources().getColor(R.color.colorWhite));
                break;
            case StatusConsulta.CANCELADA:
                holder.itemView.setBackgroundColor(contexto.getResources().getColor(R.color.colorLightTheme));
                break;
            case StatusConsulta.FINALIZADA:
                holder.itemView.setBackgroundColor(contexto.getResources().getColor(R.color.coloLigthGreen));
                break;
        }
    }
    private void setupData(EditText dataExame) {
        DatePickerDialog datePickerDialog;
        final Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(contexto, (datePicker, year, monthOfYear, dayOfMonth) ->
                dataExame.setText(
                        TextoFormato.LPad(String.valueOf(dayOfMonth),2, '0')  + "-" +  TextoFormato.LPad(String.valueOf(monthOfYear + 1),2, '0') + "-" + year),ano,mes,dia);
        datePickerDialog.setTitle("Data");
        datePickerDialog.show();
    }



}
