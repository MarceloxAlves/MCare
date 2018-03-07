package br.com.marcelo.mcare.app;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.adapter.EspecialidadeCheckRVAdapter;
import br.com.marcelo.mcare.adapter.MedicoRVAdapter;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Consulta;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.model.Login;
import br.com.marcelo.mcare.model.Medico;
import br.com.marcelo.mcare.utils.TextoFormato;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class MedicoActivity extends AppCompatActivity{

     private Login login;
     private Box<Medico> medicoBox;
     private RecyclerView  recyclerViewMedicos;
     private MedicoRVAdapter medicoAdapter;

     private EditText editNome;
     private EditText editCrm;
     private EditText editContato;
     private long idEspecialidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewMedicos.setAdapter(medicoAdapter);
        recyclerViewMedicos.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if(requestCode==1 && requestCode ==  RESULT_OK && data != null){
           Uri localImg = data.getData();
           long medicoId =  data.getLongExtra("medicoId",0);
           ImageView imageView  = (ImageView) data.getSerializableExtra("view");
           Toast.makeText(this, "" + imageView.getId(), Toast.LENGTH_SHORT).show();
           try {
               Bitmap imBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),localImg);
               Drawable drawable = new BitmapDrawable(imBitmap);
           } catch (IOException e) {
               Toast.makeText(this, getString(R.string.inconsistencia)+ e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       }
    }



    public void addMedico(View view) {
        this.addMedico(view,new Medico());
    }

    public void addMedico(View itemView, Medico medico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_medico_form, null);

        editNome = inflate.findViewById(R.id.edit_nome);
        editCrm = inflate.findViewById(R.id.edit_crm);
        editContato = inflate.findViewById(R.id.edit_contato);


        if (medico.getId() > 0) {
            editNome.setText(medico.getNome());
            editCrm.setText(medico.getCrm());
            editContato.setText(medico.getContato());
        }
        builder.setTitle(getString(R.string.add_medico));
        builder.setIcon(getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setPositiveButton(R.string.cancelar, (dialog, id) -> {

                })
                .setNegativeButton(R.string.salvar, (dialog, id) -> {
                    try {
                        medico.setNome(Validator.validade(editNome));
                        medico.setCrm(Validator.validade(editCrm));
                        medico.setContato(Validator.validade(editContato));
                        medicoBox.put(medico);
                        medicoAdapter.setMedicos(medicoBox.getAll());
                        medicoAdapter.notifyDataSetChanged();


                        Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
                    } catch (Exception exemption) {
                        Toast.makeText(this, exemption.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

    public void addEspecialidade(Medico medico) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_especialidade_check, null);

        Box<Especialidade> especialidadeBox = ((App) getApplication()).getBoxStore().boxFor(Especialidade.class);

        RecyclerView  rv_especialidades = inflate.findViewById(R.id.rv_especialidades);

        List idEspecialidadesEmMedico = new ArrayList<>();
        for (Especialidade especialidade: medico.especialidade){
            idEspecialidadesEmMedico.add(especialidade.getId());
        }
        EspecialidadeCheckRVAdapter especialidadeCheckRVAdapter = new EspecialidadeCheckRVAdapter(
                medicoBox,especialidadeBox.getAll(),this, medico);
        rv_especialidades.setAdapter(especialidadeCheckRVAdapter);
        rv_especialidades.setLayoutManager(new LinearLayoutManager(this));


        builder.setTitle(getString(R.string.especialidade));
        builder.setIcon(getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    Toast.makeText(this, R.string.registro_salvo, Toast.LENGTH_SHORT).show();
                });
        builder.create();
        builder.show();
    }

    public void agendarConsulta(Medico medico){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_consulta_form, null);

        EditText dataConsulta = inflate.findViewById(R.id.data_consulta);
        EditText horaConsulta = inflate.findViewById(R.id.hora_consulta);
        ImageButton buttonData = inflate.findViewById(R.id.btn_datapicker);
        ImageButton buttonTime = inflate.findViewById(R.id.btn_timepicker);

        buttonData.setOnClickListener(v-> { setupData(dataConsulta); });

        buttonTime.setOnClickListener(v-> { setupTime(horaConsulta);});

        builder.setTitle(getString(R.string.marcar_consulta));
        builder.setIcon(getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try {
                        Box<Consulta> consultaBox = ((App) getApplication()).getBoxStore().boxFor(Consulta.class);
                        Consulta consulta = new Consulta();
                        consulta.medico.setTarget(medico);

                        SimpleDateFormat formato =  new SimpleDateFormat("dd-MM-yyyy");
                        Date data = formato.parse(Validator.validade(dataConsulta));
                        consulta.setData(data);
                        consulta.setHorario(Validator.validade(horaConsulta));
                        consulta.usuario.setTarget(login.getUsuarioLogado());
                        ;
                        if(idEspecialidade>0){
                            consulta.especialidadeToOne.setTarget(((App)getApplication()).getBoxStore().boxFor(Especialidade.class).get(idEspecialidade));
                        }
                        consultaBox.put(consulta);
                        Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    } catch (Exception exemption) {
                        Toast.makeText(this, exemption.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

    private void setupTime(EditText horaConsulta) {
        TimePickerDialog timePickerDialog;
        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> horaConsulta.setText(TextoFormato.LPad(String.valueOf(selectedHour),2, '0') + ":" + TextoFormato.LPad(String.valueOf(selectedMinute),2, '0')), hora, minuto, true);
        timePickerDialog.setTitle("Horário");
        timePickerDialog.show();
    }

    private void setupData(EditText dataConsulta) {
        DatePickerDialog datePickerDialog;
        final Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this, (datePicker, year, monthOfYear, dayOfMonth) ->
                dataConsulta.setText(
                        TextoFormato.LPad(String.valueOf(dayOfMonth),2, '0')  + "-" +  TextoFormato.LPad(String.valueOf(monthOfYear + 1),2, '0') + "-" + year),ano,mes,dia);
        datePickerDialog.setTitle("Data");
        datePickerDialog.show();
    }

    private void initialize() {
        recyclerViewMedicos = findViewById(R.id.rv_medicos);
        this.medicoBox = ((App) getApplication()).getBoxStore().boxFor(Medico.class);

        login = new Login(this);
        List<Medico> medicos = this.medicoBox.getAll();
        Intent intent = getIntent();
        idEspecialidade =  intent.getLongExtra("especialidadeId",0);

        if(idEspecialidade>0) {
            Box<Especialidade> especialidadeBox = ((App) getApplication()).getBoxStore().boxFor(Especialidade.class);
            List<Medico> medicosAux =  new ArrayList<>();
            for (int i = 0; i < medicos.size() ; i++) {
                List<Especialidade> listEspec = medicos.get(i).especialidade;
                for (int j = 0; j < listEspec.size()  ; j++) {
                    if (listEspec.get(j).getId() == idEspecialidade){
                        medicosAux.add(medicos.get(i));
                    }
                }
            }
            medicos = medicosAux;


        }
        medicoAdapter = new MedicoRVAdapter(medicoBox, medicos, this);
    }


}
