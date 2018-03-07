package br.com.marcelo.mcare.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Login;
import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.model.Usuario;
import br.com.marcelo.mcare.model.Usuario_;
import br.com.marcelo.mcare.utils.Validator;
import io.objectbox.Box;

public class SignActivity extends AppCompatActivity {

    private EditText editEmail;
    private EditText editSenha;
    private EditText editNome;
    private Button btnCadastrar;
    private Box<Usuario> usuarioBox;
    private Login login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        usuarioBox = ((App)getApplication()).getBoxStore().boxFor(Usuario.class);
        login = new Login(this);
        setupViews();

        btnCadastrar.setOnClickListener(
              v -> {
                  cadastrar();
              }
        );

    }

    private boolean cadastrar() {
        try{
            Usuario usuario = new Usuario();
            usuario.setNome(Validator.validade(editNome));
            usuario.setEmail(Validator.validade(editEmail));
            usuario.setSenha(Validator.validade(editSenha));
            if (emailValido(usuario.getEmail())) {
                usuarioBox.put(usuario);
                login.salvarUsuario(usuario.getId());
                startActivity(new Intent(this, MainActivity.class));
            }else{
                throw new IllegalArgumentException(getString(R.string.exception_email_invalido));
            }

        }catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

     return false;
    }

    private boolean emailValido(String email){
        List<Usuario> usuario = usuarioBox.query()
                .equal(Usuario_.email , email)
                .build()
                .find();
        if (!usuario.isEmpty())
            return false;
        return true;
    }

    private void setupViews() {
        editEmail =  findViewById(R.id.edit_email);
        editSenha =  findViewById(R.id.edit_senha);
        editNome =  findViewById(R.id.edit_nome);
        btnCadastrar =  findViewById(R.id.btn_cadastrar);
    }



}
