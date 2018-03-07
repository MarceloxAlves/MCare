package br.com.marcelo.mcare.app;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.adapter.EspecialidadeRVAdapter;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.model.Especialidade_;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class EspecialidadeActivity extends AppCompatActivity {

    private EditText editDescricao;
    private Box<Especialidade> especialidadeBox;
    RecyclerView recyclerViewEspecialidades;
    EspecialidadeRVAdapter especialidadeRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidade);
        recyclerViewEspecialidades = findViewById(R.id.rv_especialidades);
        initialize();
        pesquisar(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater  =  getMenuInflater();
        menuInflater.inflate(R.menu.menu_default,menu);

        // seta as configurações ao serachView
        SearchManager searchManager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.pesquisar).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        pesquisar(intent);
    }

    private void pesquisar(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
               especialidadeRVAdapter.setEspecialidades(
                    especialidadeBox
                            .query()
                            .contains(Especialidade_.descricao, query.trim())
                            .build()
                            .find()
            );
               especialidadeRVAdapter.notifyDataSetChanged();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewEspecialidades.setAdapter(especialidadeRVAdapter);
        recyclerViewEspecialidades.setLayoutManager(new LinearLayoutManager(this));


    }

    public void addEspecialidade(View view) {
        this.addEspecialidade(view,new Especialidade());
    }

    public void addEspecialidade(View view,  Especialidade especialidade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_especialidade_form, null);
        editDescricao =  inflate.findViewById(R.id.edit_decricao);
        if (especialidade.getId()  > -1){
            editDescricao.setText(especialidade.getDescricao());
        }
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try{
                        especialidade.setDescricao(Validator.validade(editDescricao));
                        especialidadeBox.put(especialidade);
                        especialidadeRVAdapter.setEspecialidades(especialidadeBox.getAll());
                        especialidadeRVAdapter.notifyDataSetChanged();


                        Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
                    }catch (Exception exemption){
                        Toast.makeText(this, exemption.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

    private void initialize() {
        this.especialidadeBox = ((App) getApplication()).getBoxStore().boxFor(Especialidade.class);
        especialidadeRVAdapter = new EspecialidadeRVAdapter(especialidadeBox,this.especialidadeBox.getAll(), this);
    }


}
