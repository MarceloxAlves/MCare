package br.com.marcelo.mcare.app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Exame;
import br.com.marcelo.mcare.model.Resultado;
import br.com.marcelo.mcare.model.Resultado_;
import br.com.marcelo.mcare.model.TipoExame;
import br.com.marcelo.mcare.model.TipoResultado;
import br.com.marcelo.mcare.model.TipoResultado_;
import br.com.marcelo.mcare.model.Usuario;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;
import io.objectbox.query.QueryFilter;
import io.objectbox.relation.ToOne;


public class ResultadoDetalheActivity extends AppCompatActivity {

    TableLayout tableLayout;
    TextView txtNomeExame;
    TextView txtData;
    Box<Exame> exameBox;
    Box<Resultado> resultadoBox;
    TipoExame tipoExame;
    Exame exame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_detalhe);
        setupViews();

        initialize();

        carregaResultado();

    }

    private void carregaResultado() {
        Date date = exame.getData();
        String formato = new SimpleDateFormat("dd-MM-yyyy").format(date);
        txtData.setText(formato);
        txtNomeExame.setText(tipoExame.getDescricao());

        for (Resultado resultado:  exame.resultados) {

            TableRow tableRow = new TableRow(tableLayout.getContext());
            TextView textView = new TextView(tableLayout.getContext());

            textView.setGravity(Gravity.LEFT);
            textView.setPadding(10,10,10,10);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            params.weight = 2;
            textView.setLayoutParams(params);
            textView.setText(resultado.getDescricao());
            textView.setClickable(true);
            tableRow.addView(textView);

             textView = new TextView(tableLayout.getContext());
            textView.setPadding(10,10,10,10);
            params.weight = 1;
            textView.setTag(resultado);
            textView.setLayoutParams(params);
            if (resultado != null) {
                textView.setText(String.valueOf(resultado.getValor()));
            }else
            {
                textView.setText("--");
            }
            tableRow.addView(textView);



            textView = new TextView(tableLayout.getContext());
            textView.setPadding(10,10,10,10);
            params.weight = 1;
            textView.setLayoutParams(params);
            textView.setText("---");
            tableRow.addView(textView);

            tableLayout.addView(tableRow);
        }
    }



    private void initialize() {
        Intent intent = getIntent();
        long id  =  intent.getLongExtra("exameId",0);
        if (id < 0)
            finish();

        exameBox = ((App) getApplication()).getBoxStore().boxFor(Exame.class);
        resultadoBox = ((App) getApplication()).getBoxStore().boxFor(Resultado.class);
        exame = exameBox.get(id);
        tipoExame = exame.tipoExame.getTarget();
    }

    private void setupViews() {
        tableLayout = findViewById(R.id.tabela_resultado);
        txtNomeExame = findViewById(R.id.txt_descricao);
        txtData = findViewById(R.id.txt_data);
    }
}
