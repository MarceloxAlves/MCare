package br.com.marcelo.mcare.app;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import br.com.marcelo.mcare.adapter.TipoExameRVAdapter;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Especialidade;
import br.com.marcelo.mcare.model.Especialidade_;
import br.com.marcelo.mcare.model.TipoExame;
import br.com.marcelo.mcare.model.TipoExame_;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class TipoExameActivity extends AppCompatActivity {

    private EditText editDescricao;
    private Box<TipoExame> tipoExameBox;
    RecyclerView recyclerViewTipoExame;
    TipoExameRVAdapter tipoExameRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_exame);
        recyclerViewTipoExame = findViewById(R.id.rv_tipo_exames);
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
               tipoExameRVAdapter.settipoExames(
                    tipoExameBox
                            .query()
                            .contains(TipoExame_.descricao, query.trim())
                            .build()
                            .find()
            );
            tipoExameRVAdapter.notifyDataSetChanged();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerViewTipoExame.setAdapter(tipoExameRVAdapter);
        recyclerViewTipoExame.setLayoutManager(new LinearLayoutManager(this));


    }

    public void addTipoExame(View view) {
        this.addTipoExame(view,new TipoExame());
    }

    public void addTipoExame(View view,  TipoExame tipoExame) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.activity_especialidade_form, null);
        editDescricao =  inflate.findViewById(R.id.edit_decricao);
        if (tipoExame.getId()  > -1){
            editDescricao.setText(tipoExame.getDescricao());
        }
        builder.setView(inflate)
                .setNegativeButton(R.string.cancelar, (dialog, id) -> {

                })
                .setPositiveButton(R.string.salvar, (dialog, id) -> {
                    try{
                        tipoExame.setDescricao(Validator.validade(editDescricao));
                        tipoExameBox.put(tipoExame);
                        tipoExameRVAdapter.settipoExames(tipoExameBox.getAll());
                        tipoExameRVAdapter.notifyDataSetChanged();


                        Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
                    }catch (Exception exemption){
                        Toast.makeText(this, exemption.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                });
        builder.create();
        builder.show();
    }

    private void initialize() {
        this.tipoExameBox = ((App) getApplication()).getBoxStore().boxFor(TipoExame.class);
        tipoExameRVAdapter = new TipoExameRVAdapter(tipoExameBox,this.tipoExameBox.getAll(), this);
    }


}
