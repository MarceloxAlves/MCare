package br.com.marcelo.mcare.app;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Clinica;
import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.adapter.ClinicaRVAdapter;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class ClinicaActivity extends AppCompatActivity {

    TextView editNome;
    Box<Clinica> clinicaBox;
    RecyclerView recyclerViewClinicas;
    ClinicaRVAdapter clinicaRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinica);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewClinicas.setAdapter(clinicaRVAdapter);
        recyclerViewClinicas.setLayoutManager(new LinearLayoutManager(this));

    }

    public void addClinica(View view) {
        addClinica(view, new Clinica());
    }

    public void addClinica(View itemView, Clinica clinica) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_clinica_form, null);

        editNome = inflate.findViewById(R.id.edit_nome);


        if (clinica.getId() > 0) {
            editNome.setText(clinica.getNome());

        }
        builder.setTitle(getString(R.string.add_clinica));
        builder.setIcon(getResources().getDrawable(R.drawable.ic_person_add));
        builder.setView(inflate)
                .setPositiveButton(R.string.cancelar, (dialog, id) -> {

                })
                .setNegativeButton(R.string.salvar, (dialog, id) -> {
                    try {
                        clinica.setNome(Validator.validade(editNome));
                        clinicaBox.put(clinica);
                        clinicaRVAdapter.setClinicas(clinicaBox.getAll());
                        clinicaRVAdapter.notifyDataSetChanged();


                        Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
                    } catch (Exception exemption) {
                        Toast.makeText(this, exemption.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }


    private void initialize() {
        recyclerViewClinicas = findViewById(R.id.rv_clinicas);
        this.clinicaBox = ((App) getApplication()).getBoxStore().boxFor(Clinica.class);
        clinicaRVAdapter = new ClinicaRVAdapter(clinicaBox, this.clinicaBox.getAll(), this);
    }
}
