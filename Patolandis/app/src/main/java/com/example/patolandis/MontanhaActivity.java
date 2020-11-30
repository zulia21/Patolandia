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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MontanhaActivity extends AppCompatActivity implements OnSuccessListener<Location>, OnFailureListener {

    private ViewPager2 viewPager2;

    Adapter adapter;

    ImageView coracao, adiciona;

    TextView endereco;

    double latitude, longitude, latitude2, longitude2;

    public final static int CODIGO_LOCALIZA = 1;
    public final static int CODIGO_ADICIONAR = 0;

    List<SliderItem> sliderItems = new ArrayList<>();

    public String MONTANHA_COD = "com.example.patolandis.MontanhaActivity";
    public static final String IMG_PRE = "img_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_montanha);

        adiciona = (ImageView) findViewById(R.id.imgadiciona);
        coracao = (ImageView) findViewById(R.id.imgfavmontanha);
        endereco = (TextView) findViewById(R.id.txtenderecomr);

        new SharedFav(this, coracao, MONTANHA_COD);

        // carousel com viewpager2
        viewPager2 = findViewById(R.id.viewPagerslider);

        java.util.function.Function<Integer, SliderItem> sliderFrom = (resource) -> {

            return new SliderItem(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + getResources().getResourcePackageName(resource)
                    + '/' + getResources().getResourceTypeName(resource) + '/'
                    + getResources().getResourceEntryName(resource)));

        };

        sliderItems.add(sliderFrom.apply(R.drawable.lololol));
        sliderItems.add(sliderFrom.apply(R.drawable.montanhasis));
        sliderItems.add(sliderFrom.apply(R.drawable.montanhasss));
        sliderItems.add(sliderFrom.apply(R.drawable.montanhus));


        adapter = new Adapter(sliderItems, viewPager2);
        adicionarImagensSalvas(sliderItems);
        viewPager2.setAdapter(this.adapter);

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
                List<Address> enderecos = geocoder.getFromLocationName("Itupeva - SP, 13295-000", 1);
                Address endderecos = enderecos.get(0);
                latitude2 = endderecos.getLatitude();
                longitude2 = endderecos.getLongitude();
                Location.distanceBetween(latitude, longitude, latitude2, longitude2, results);
                float resultados = results[0] / 1000;

                endereco.setText(String.valueOf(resultados + "km"));
            } catch (Exception e) {
                Log.e("Falha OnSuccess", "errors", e);
            }
        }

    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e("Falha onFailure", "FALHOU");
    }

    public void adicionar(View view) {

        Intent adicionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(adicionarFoto, CODIGO_ADICIONAR);
    }

    private static final String IMAGE_DIRECTORY_NAME = "montanha-images";
    public void adicionarImagensSalvas(List<SliderItem> sliderItems) {

        File directory = new File(getFilesDir(), IMAGE_DIRECTORY_NAME);

        if (directory.exists()) {

            String[] nomes = directory.list();

            for (int i = 0; i < nomes.length; i++) {
                sliderItems.add(new SliderItem(Uri.fromFile(new File(directory, nomes[i]))));
            }
        }
    }


    public void salvarArquivo(@NonNull Uri uri) throws IOException {
        try {

            File directory = new File(getFilesDir(), IMAGE_DIRECTORY_NAME);

            if (!directory.exists()) {
                if (!directory.mkdir()) {
                    throw new IOException("Falha na criação da pasta");
                }
            }

            int arquivos = directory.list().length;

            File result = new File(directory, IMG_PRE + arquivos + 1);

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        super.onActivityResult(RequestCode, ResultCode, data);
        if (ResultCode == RESULT_OK) {

            Uri uri = data.getData();
            try {
                salvarArquivo(uri);
            } catch (IOException io) {
                io.printStackTrace();
            }
            adapter.addImage(uri);


        }


    }
}





