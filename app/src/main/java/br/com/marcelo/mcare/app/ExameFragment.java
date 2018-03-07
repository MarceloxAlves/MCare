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
import br.com.marcelo.mcare.adapter.ExameRVAdapter;
import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Exame;
import br.com.marcelo.mcare.model.Exame_;
import br.com.marcelo.mcare.model.Login;
import io.objectbox.Box;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExameFragment extends Fragment {

    RecyclerView recyclerViewExames;
    ExameRVAdapter exameRVAdapter;
    Box<Exame> exameBox;
    private Context context;
    FloatingActionButton fbAddexame;
    Login login;


    public ExameFragment() {
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
        recyclerViewExames.setAdapter(exameRVAdapter);
        recyclerViewExames.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_exame, container, false);
        initialize(view);



        return view;
    }

    private void initialize(View view) {
        recyclerViewExames = view.findViewById(R.id.rv_exames);
        exameBox = ((App)((FragmentActivity)context).getApplication()).getBoxStore().boxFor(Exame.class);
        exameRVAdapter = new ExameRVAdapter(exameBox,
                exameBox.query()
                .filter(entity -> entity.usuarioToOne.getTarget().getId() == login.getIdUsuarioLogado())
                .orderDesc(Exame_.data).build().find(),context);
    }

    private void cadastrarMedico(View view) {
        startActivity(new Intent(view.getContext(),MedicoActivity.class));
    }


    private void  cadastrarexame(View view){
        startActivity(new Intent(view.getContext(),EspecialidadeActivity.class));
    }
}
