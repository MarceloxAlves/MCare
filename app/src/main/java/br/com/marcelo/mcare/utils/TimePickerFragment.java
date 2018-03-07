package br.com.marcelo.mcare.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

import br.com.marcelo.mcare.R;

/**
 * Created by Marcelo on 04/03/2018.
 */

public  class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener{

    private int hora,minuto;
    EditText editTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  =  super.onCreateView(inflater, container, savedInstanceState);
        editTime = view.findViewById(R.id.hora_consulta);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hora   = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hora,minuto, DateFormat.is24HourFormat(getActivity()));
    }



    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            editTime.setText(hourOfDay);
    }

}
