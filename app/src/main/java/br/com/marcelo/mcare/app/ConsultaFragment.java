package br.com.marcelo.mcare.app;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.adapter.ConsultaRVAdapter;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Consulta;
import br.com.marcelo.mcare.model.Consulta_;
import br.com.marcelo.mcare.model.Login;
import io.objectbox.Box;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultaFragment extends Fragment {

    RecyclerView recyclerViewConsultas;
    ConsultaRVAdapter consultaRVAdapter;
    Box<Consulta> consultaBox;
    private Context context;
    FloatingActionButton fbAddConsulta;
    Login login;


    public ConsultaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        login = new Login(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerViewConsultas.setAdapter(consultaRVAdapter);
        recyclerViewConsultas.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_consulta, container, false);
         initialize(view);


        fbAddConsulta.setOnClickListener(
            v->{
             cadastrarConsulta(v);
            }
        );



        return view;
    }

    private void initialize(View view) {
        fbAddConsulta = view.findViewById(R.id.fb_add_consulta);
        recyclerViewConsultas = view.findViewById(R.id.rv_consultas);
        consultaBox = ((App)((FragmentActivity)context).getApplication()).getBoxStore().boxFor(Consulta.class);
        consultaRVAdapter = new ConsultaRVAdapter(consultaBox,consultaBox
                .query()
                .filter(entity -> entity.usuario.getTarget().getId() == login.getIdUsuarioLogado())
                .orderDesc(Consulta_.data).build().find(),context);
    }

    private void addExame(View view) {
        startActivity(new Intent(view.getContext(),MedicoActivity.class));
    }


    private void  cadastrarConsulta(View view){
        startActivity(new Intent(view.getContext(),EspecialidadeActivity.class));
    }
}
