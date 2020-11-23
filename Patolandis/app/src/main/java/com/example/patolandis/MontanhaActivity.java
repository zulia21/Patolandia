package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MontanhaActivity extends AppCompatActivity implements OnSuccessListener<Location>, OnFailureListener {
    private ViewPager2 viewPager2;
    ImageView coracao, adiciona;
    TextView endereco;
    double latitude, longitude, latitude2, longitude2;
    public final static int CODIGO_LOCALIZA = 1;
    public final static int CODIGO_ADICIONAR = 0;
    public String MONTANHA_COD = "com.example.patolandis.MontanhaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_montanha);
        adiciona = (ImageView)findViewById(R.id.imgadiciona);
        coracao = (ImageView) findViewById(R.id.imgfavmontanha);
        new SharedFav( this, coracao, MONTANHA_COD);
        endereco = (TextView) findViewById(R.id.txtenderecomr);
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
            Toast toast = Toast.makeText(this, "Permissão pedida!", Toast.LENGTH_SHORT);
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
                Log.e("Falha OnSuccess", "errors",e );
            }
        }

    }
    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e("Falha onFailure", "FALHOU");
    }

    public void adicionar(View view)
    {
        Intent adicionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(adicionarFoto, CODIGO_ADICIONAR);
    }
    @Override
    protected void onActivityResult (int RequestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(RequestCode,ResultCode, data);
        if (ResultCode == RESULT_OK)
        {
            Cursor cursor = null;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri,null, null, null, null);
            cursor.moveToFirst();
            List<Slider> sliders = new ArrayList<>();
            sliders.add(new Slider(uri.hashCode()));
            viewPager2.setAdapter(new Adapter(sliders, viewPager2));

        }
    }



        }

