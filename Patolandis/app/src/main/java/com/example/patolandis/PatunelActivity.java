package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatunelActivity extends AppCompatActivity implements OnSuccessListener<Location>, OnFailureListener {
    private ViewPager2 viewPager2;
    Adapter adapter;
    ImageView coracao;
    TextView endereco;
    double latitude, longitude, latitude2, longitude2;
    public final static int CODIGO_ADICIONAR = 0;
    public static final String IMG_PRE = "img_";
    public final static int CODIGO_LOCALIZA = 1;
    public String PATUNEL_COD = "com.example.patolandis.PatunelActivity";
    List<SliderItem> sliderItems = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patunel);
        endereco = (TextView) findViewById(R.id.txtenderecopt);
        coracao= (ImageView) findViewById(R.id.imgfavpatunel);
        new SharedFav( this, coracao, PATUNEL_COD);
        //viewpager
        viewPager2 = findViewById(R.id.viewPagerslider);
        sliderItems.add(new SliderItem(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.tunel)
                + '/' + getResources().getResourceTypeName(R.drawable.tunel) + '/' + getResources().getResourceEntryName(R.drawable.tunel))));
        sliderItems.add(new SliderItem(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.tuneltunel)
                + '/' + getResources().getResourceTypeName(R.drawable.tuneltunel) + '/' + getResources().getResourceEntryName(R.drawable.tuneltunel))));
        sliderItems.add(new SliderItem(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.tuneltuneltunel)
                + '/' + getResources().getResourceTypeName(R.drawable.tuneltuneltunel) + '/' + getResources().getResourceEntryName(R.drawable.tuneltuneltunel))));
        sliderItems.add(new SliderItem(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + getResources().getResourcePackageName(R.drawable.tuneltuneltuneltunel)
                + '/' + getResources().getResourceTypeName(R.drawable.tuneltuneltuneltunel) + '/' + getResources().getResourceEntryName(R.drawable.tuneltuneltuneltunel))));
        adicionarImagensSalvas(sliderItems);
        adapter = new Adapter(sliderItems, viewPager2);
        viewPager2.setAdapter(this.adapter);

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
        Log.e("Falha onFailure", "FALHOU");
    }
    float[] results = new float[1];
    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(PatunelActivity.this, Locale.getDefault());
            try {
                // endereço da montanha
                List<Address> enderecos =  geocoder.getFromLocationName("Rod. dos Bandeirantes, KM 72",1);
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
    public void adicionar(View view)
    {
        Intent adicionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(adicionarFoto, CODIGO_ADICIONAR);
    }
    public void adicionarImagensSalvas(List<SliderItem> sliderItems)
    {
        String[] nomes = getFilesDir().list();
        for (int i = 0; i < nomes.length; i++)
        {
            sliderItems.add(new SliderItem(Uri.fromFile(new File(getFilesDir(), nomes[i]))));
        }


    }
    public void salvarArquivo(@NonNull Uri uri ) throws IOException
    {
        try {
            int arquivos = getFilesDir().list().length;
            File result = new File(getFilesDir(), IMG_PRE + arquivos + 1);
            if (result.exists()) {
                return;
            }
            OutputStream outputStream = new FileOutputStream(result);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[4096];
            int size;
            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
            }
            inputStream.close();
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult (int RequestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(RequestCode,ResultCode, data);
        if (ResultCode == RESULT_OK)
        {

            Uri uri = data.getData();
            try {
                salvarArquivo(uri);
            }
            catch (IOException io)
            {
                io.printStackTrace();
            }
            adapter.addImage(uri);




        }


    }
}
