package br.com.marcelo.mcare.app;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import br.com.marcelo.mcare.R;
import br.com.marcelo.mcare.model.Login;
import br.com.marcelo.mcare.adapter.TabPageAdapter;
import br.com.marcelo.mcare.model.Autenticavel;

public class MainActivity extends AppCompatActivity
        implements Autenticavel, NavigationView.OnNavigationItemSelectedListener {
    Login login;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle barDrawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login =  new Login(this);
        this.autenticar();
        initialize();




        barDrawerToggle  = new ActionBarDrawerToggle(this, drawerLayout,R.string.acessar,R.string.deletar);
        drawerLayout.addDrawerListener(barDrawerToggle);
        barDrawerToggle.setToolbarNavigationClickListener( v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        barDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(4);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(barDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_clinica:
                startActivity(new Intent(this,ClinicaActivity.class));
                break;
            case R.id.nav_medico:
                startActivity(new Intent(this,MedicoActivity.class));
                break;
            case R.id.nav_especialidades:
                startActivity(new Intent(this,EspecialidadeActivity.class));
                break;
            case R.id.nav_tipo_exames:
                startActivity(new Intent(this,TipoExameActivity.class));
                break;
            case R.id.nav_sair:
                login.logout();
                break;
        }

        return true;
    }

    @Override
    public boolean autenticar() {
            if(!login.logado())
                login.logout();

        return true;
    }

    private void initialize() {
        viewPager = findViewById(R.id.container_page);
        tabLayout = findViewById(R.id.sliding_tabs);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        TextView apelido = navigationView.getHeaderView(0).findViewById(R.id.txt_apelido);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.txt_email);

        setupUser(apelido, email);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new TabPageAdapter(getSupportFragmentManager()));


    }

    private void setupUser(TextView apelido, TextView email) {
        try {
            apelido.setText(login.getUsuarioLogado().getNome());
            email.setText(login.getUsuarioLogado().getEmail());
        }catch (Exception e){
            login.logout();
        }
    }


}
