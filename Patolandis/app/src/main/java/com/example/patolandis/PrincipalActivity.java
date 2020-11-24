package com.example.patolandis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PrincipalActivity extends AppCompatActivity implements SensorEventListener {
    public final static int CAMERA_LANTERNA = 1;
    ImageView flashlight;
    private Sensor luzes;
    private SensorManager sensorManager;
    TextView nomelogin;
    boolean temLanterna, lanternaLigada = false;
    public static final int EXTRA_COD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        flashlight = (ImageView) findViewById(R.id.imgflashlight);

        // lanterninha
        temLanterna = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        luzes = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        nomelogin = (TextView) findViewById(R.id.txtnomelog);
        Intent intent = getIntent();
        String Login =intent.getStringExtra(LoginActivity.EXTRA_MESSAGE_LOGIN);
        nomelogin.setText(Login);
    }
    //sensor que muda a lanterna
    @Override
    public void onSensorChanged(SensorEvent event) {
        float resultados = event.values[0];
        if (resultados < 7.0)
        {
            flashlight.setClickable(true);
            flashlight.setVisibility(View.VISIBLE);
            flashlight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(PrincipalActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(PrincipalActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_LANTERNA);
                    }
                    else {
                        Lanterninha();
                    }
                }
            });

        }
        else
        {
            flashlight.setClickable(false);
            flashlight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    // onclick do botão que flutua, com action_pick
    public void Chamar (View view)
    {
        Intent chameIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(Intent.createChooser(chameIntent,"Selecione um contato"), EXTRA_COD);
    }
    //onclick de cada botão.
    public void rodaGigantar (View view)
    {
        Intent rodaGigantar = new Intent(this, RodaGiganteActivity.class);
        startActivity(rodaGigantar);
    }
    public void Montanhar(View view)
    {
        Intent montanhar = new Intent(this, MontanhaActivity.class);
        startActivity(montanhar);
    }
    public void Patunar (View view)
    {
        Intent patossar = new Intent(this, PatunelActivity.class);
        startActivity(patossar);
    }
    public void Patossar(View view)
    {
        Intent intent = new Intent(this,PatosselActivity.class);
        startActivity(intent);
    }
    // resultado do requerimento do botão que flutua, abrindo a tela de discagem com o numero do contato escolhido.
    @Override
    protected void onActivityResult (int RequestCode, int ResultCode, Intent data)
    {
        super.onActivityResult(RequestCode, ResultCode, data);
        if(ResultCode == RESULT_OK)
        {
                    Cursor cursor = null;

                        String numero = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int telefones = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        numero = cursor.getString(telefones);
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            Uri chamada = Uri.parse("tel:"+ numero);
                            Intent intentes = new Intent(Intent.ACTION_CALL);
                            intentes.setData(chamada);
                            startActivity(intentes);
                        }
                       else {
                            ActivityCompat.requestPermissions(this, new String[] {"android.permission.CALL_PHONE"}, 2);
                        }



                }
            }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == CAMERA_LANTERNA)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                temLanterna = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                Lanterninha();
            }
            else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //sensor
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,luzes,SensorManager.SENSOR_DELAY_NORMAL);
}
    @Override
    public  void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void Lanterninha()
    {
        if (temLanterna)
        {
            if(lanternaLigada)
            {
                Desligar();
                lanternaLigada = false;
            }
            else {
                Ligar();
                lanternaLigada = true;
            }
        }
    }
    public void Ligar()
    {

        CameraManager cameraManager = (CameraManager) getSystemService((Context.CAMERA_SERVICE));
        try {
            String lanterna = cameraManager.getCameraIdList()[0];

                cameraManager.setTorchMode(lanterna, true);

        }
        catch (Exception e) {
            Log.e("Lanterna", "Problemas com a lanterna", e);
        }


    }
    public void Desligar()
    {

        CameraManager cameraManager = (CameraManager) getSystemService((Context.CAMERA_SERVICE));
        try {
            String lanterna = cameraManager.getCameraIdList()[0];

            cameraManager.setTorchMode(lanterna, false);

        }
        catch (Exception e) {
            Log.e("Lanterna", "Problemas com a lanterna", e);
        }


    }

        }






