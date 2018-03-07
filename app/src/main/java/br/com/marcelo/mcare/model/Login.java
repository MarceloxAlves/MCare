package br.com.marcelo.mcare.model;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.app.LoginActivity;
import io.objectbox.Box;


public class Login {
    private Context contexto;
    private SharedPreferences preferences;
    private  final String NOME_ARQUIVO = "login.preferencias";
    public  final int MODE = 0;
    private SharedPreferences.Editor editor;
    private final  String usuarioId = "usuarioId";
    Box<Usuario> usuarioBox;

    public Login(Context contexto) {
        this.contexto = contexto;
        preferences = this.contexto.getSharedPreferences(NOME_ARQUIVO,MODE);
        this.editor = preferences.edit();
        this.usuarioBox = ((App)((Activity)contexto).getApplication()).getBoxStore().boxFor(Usuario.class);
    }

    /**
     *
     * @return as prefencias
     */
    public SharedPreferences getPreferences() {
        return preferences;
    }

    /**
     *
     * @param id
     */
    public void salvarUsuario(long id){
        editor.putLong(this.usuarioId,id);
        editor.commit();
    }

    /**
     *
     * @return o id do usuário caso contrário retorna -1
     */
    public long getIdUsuarioLogado(){
         long id = this.preferences.getLong(this.usuarioId, -1);
        return  id;
    }

    /**
     *
     * @return retorna verdadeiro caso exista um id;
     */
    public boolean logado(){
        return getIdUsuarioLogado() != -1;
    }


    /**
     * limpa as preferencias e redirenciona para tela de login
     */
    public void logout() {

        editor.clear();
        editor.commit();
        contexto.startActivity(new Intent(contexto.getApplicationContext(), LoginActivity.class));
        ((Activity)contexto).finish();
    }


    public Usuario getUsuarioLogado () {

       try {
           if (logado())
               return (Usuario) usuarioBox.get(getIdUsuarioLogado());
       }catch (Exception e){
           logout();
       }
       return  null;
}


}
