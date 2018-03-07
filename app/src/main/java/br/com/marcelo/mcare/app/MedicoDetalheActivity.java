package br.com.marcelo.mcare.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import br.com.marcelo.mcare.dal.App;
import br.com.marcelo.mcare.model.Medico;
import br.com.marcelo.mcare.R;
import io.objectbox.Box;

public class MedicoDetalheActivity extends AppCompatActivity implements
        OnMapReadyCallback{
    GoogleMap googleMap;
    Box<Medico> medicoBox;
    Medico medico;
    TextView  txtNome;
    TextView txtCrm;
    TextView txtContato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_detalhe);

        Intent intent =  getIntent();

        medicoBox = ((App) getApplication()).getBoxStore().boxFor(Medico.class);

        initialize();
        long id = intent.getLongExtra("medicoId",0);
        if (id > 0){
//                Medico medico
        }else
            finish();

        MapFragment mapFragment =  (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = 900;
        mapFragment.getView().setLayoutParams(params);

    }

    private void initialize() {
        txtNome = findViewById(R.id.nome_medico);
        txtContato = findViewById(R.id.fone_medico);
        txtCrm = findViewById(R.id.crm_medico);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap =  googleMap;
        LatLng latLng = new LatLng(-5, -40.00);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Deu bom"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
