package br.com.marcelo.mcare.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Usuario;
import br.com.marcelo.mcare.model.Usuario_;
import br.com.marcelo.mcare.model.Login;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;
    private Button btnLogar;
    private Box<Usuario> usuarioBox;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuarioBox = ((App)getApplication()).getBoxStore().boxFor(Usuario.class);
        login =  new Login(this);
        setupViews();

        btnLogar.setOnClickListener(v -> {
                autenticar();
            }
        );
    }


    public void cadastrar(View view) {
        startActivity(new Intent(this,SignActivity.class));
        finish();
    }

    /**
     * inicia a autenticação
     */
    private void autenticar() {
        try{
            String email = Validator.validade(editEmail);
            String senha = Validator.validade(editSenha);
            ProgressDialog progressDialog = new ProgressDialog(this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.autenticando));
            progressDialog.show();
            new android.os.Handler().postDelayed(
                () -> {
                    logar(email,senha);
                    progressDialog.dismiss();
                }, 2000);

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


     private boolean logar(String email, String senha){
            try{
                    List<Usuario> listaUsuario = usuarioBox.query()
                    .equal(Usuario_.email,email)
                    .equal(Usuario_.senha,senha)
                    .build()
                    .find();
            if (!listaUsuario.isEmpty()){
                login.salvarUsuario(listaUsuario.get(0).getId());
                startActivity(new Intent(this,MainActivity.class));
            }else
                throw new IllegalArgumentException(getString(R.string.exception_usuario_invalido));
            }catch (Exception ex){
                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        return false;
    }

    private void setupViews() {
        editEmail = findViewById(R.id.edit_email);
        editSenha = findViewById(R.id.edit_senha);
        btnLogar = findViewById(R.id.btn_login);
    }
}
