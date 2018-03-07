package br.com.marcelo.mcare.model;

/**
 * Created by Marcelo on 02/03/2018.
 */

public interface Autenticavel {

     default boolean autenticar() {
        return false;
    }

}
