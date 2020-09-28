package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatosselActivity extends AppCompatActivity implements OnSuccessListener<Location>, OnFailureListener {
    private ViewPager2 viewPager2;
    TextView endereco;
    double latitude, longitude, latitude2, longitude2;
    public final static int CODIGO_LOCALIZA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patossel);
        endereco = (TextView) findViewById(R.id.txtendereco);

        //viewpager
        viewPager2 = findViewById(R.id.viewPagerslider);
        List<Slider> sliders = new ArrayList<>();
        sliders.add(new Slider(R.drawable.carrossel));
        sliders.add(new Slider(R.drawable.carrossel1));
        sliders.add(new Slider(R.drawable.carrosel2));
        viewPager2.setAdapter(new Adapter(sliders, viewPager2));

        //localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODIGO_LOCALIZA);
            Toast toast = Toast.makeText(this, "Permissão para acesso negada. Requerimento novo", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            final FusedLocationProviderClient fusedlocationpc = LocationServices.getFusedLocationProviderClient(this);
            fusedlocationpc.getLastLocation().addOnSuccessListener(this);
            fusedlocationpc.getLastLocation().addOnFailureListener(this);
        }
    }
    // localização
    @Override
    public void onFailure(@NonNull Exception e) {

    }
    float[] results = new float[1];
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(PatosselActivity.this, Locale.getDefault());
            try {
                // endereço da montanha
                List<Address> enderecos =  geocoder.getFromLocationName("Jardim Vista Alegre, Louveira - SP, 13290-000",1);
                Address endderecos = enderecos.get(0);
                latitude2 = endderecos.getLatitude();
                longitude2 = endderecos.getLongitude();
                //distância entre os pontos e resultado convertido para quilômetros
                Location.distanceBetween(latitude,longitude,latitude2,longitude2, results);
                float resultados = results[0] / 1000;
                // resultado adicionado à textview
                endereco.setText(String.valueOf(resultados+"km"));
            } catch (Exception e) {
                Log.e("Exceção", "errors",e );
            }
        }

    }
}
