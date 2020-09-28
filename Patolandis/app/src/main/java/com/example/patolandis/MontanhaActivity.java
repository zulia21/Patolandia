package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
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

public class MontanhaActivity extends AppCompatActivity implements OnSuccessListener<Location>, OnFailureListener {
    private ViewPager2 viewPager2;
    TextView endereco;
    double latitude, longitude, latitude2, longitude2;
    public final static int CODIGO_LOCALIZA = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montanha);
        endereco = (TextView) findViewById(R.id.txtendereco);
        // carousel com viewpager2
        viewPager2 = findViewById(R.id.viewPagerslider);
        List<Slider> sliders = new ArrayList<>();
        sliders.add(new Slider(R.drawable.lololol));
        sliders.add(new Slider(R.drawable.montanhasis));
        sliders.add(new Slider(R.drawable.montanhasss));
        sliders.add(new Slider(R.drawable.montanhus));

        viewPager2.setAdapter(new Adapter(sliders, viewPager2));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, CODIGO_LOCALIZA);
        Toast toast = Toast.makeText(this, "Permissão negada!", Toast.LENGTH_SHORT);
        toast.show();
    } else {
        final FusedLocationProviderClient fusedlocationpc = LocationServices.getFusedLocationProviderClient(this);
        fusedlocationpc.getLastLocation().addOnSuccessListener(this);
        fusedlocationpc.getLastLocation().addOnFailureListener(this);
    }
        }
    // localização
    float[] results = new float[1];
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(MontanhaActivity.this, Locale.getDefault());
            try {
                // endereço da montanha
                List<Address> enderecos =  geocoder.getFromLocationName("Itupeva - SP, 13295-000",1);
                Address endderecos = enderecos.get(0);
                latitude2 = endderecos.getLatitude();
                longitude2 = endderecos.getLongitude();
                Location.distanceBetween(latitude,longitude,latitude2,longitude2, results);
                float resultados = results[0] / 1000;

                endereco.setText(String.valueOf(resultados+"km"));
            } catch (Exception e) {
                Log.e("rolas", "errors",e );
            }
        }

    }
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e("Falha", "FALHOU");
    }
        }
