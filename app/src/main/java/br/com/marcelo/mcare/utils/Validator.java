package br.com.marcelo.mcare.utils;
import android.widget.TextView;

import br.com.marcelo.mcare.R;

public class Validator {
    public static String validade(TextView editText) throws IllegalArgumentException {
        if (editText.getText().toString().trim().isEmpty()) {
            editText.requestFocus();
            throw new IllegalArgumentException(editText.getContext().getString(R.string.exception_campo_obrigatorio));
        }
        return editText.getText().toString().trim();
    }
}
