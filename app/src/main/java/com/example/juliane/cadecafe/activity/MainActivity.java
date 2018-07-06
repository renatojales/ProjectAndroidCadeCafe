package com.example.juliane.cadecafe.activity;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.fragment.AddCafeFragment;
import com.example.juliane.cadecafe.fragment.CafeFragment;
import com.example.juliane.cadecafe.fragment.EditCafeFragment;
import com.example.juliane.cadecafe.fragment.FavoritosFragment;
import com.example.juliane.cadecafe.fragment.TelaFragment;
import com.example.juliane.cadecafe.modelo.Cafe;
import com.example.juliane.cadecafe.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;


public class MainActivity extends AppCompatActivity implements
        FavoritosFragment.OnCafeSelectedListener, CafeFragment.onEditCafeListener, FavoritosFragment.OnAddCafeListener {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;




    private EditText cafeNome;
    private EditText cafeEndereco;
    private Button saveBtn;




    @Override //Metodo que chama o editcafefragment
    public void onEditcafeSelected(Cafe cafe) {
        Log.d(TAG, "OnContactSelected: cafe selecionado de "
                + getString(R.string.edit_cafe_fragment)
                + " " + cafe.getNome());

        EditCafeFragment fragment = new EditCafeFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.cafe), cafe);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.edit_cafe_fragment));
        transaction.commit();

    }

    @Override
    public void OnCafeSelected(Cafe cafe) {
        Log.d(TAG, "OnContactSelected: cafe selecionado de "
                + getString(R.string.favoritos_fragment)
                + " " + cafe.getNome());

        CafeFragment fragment = new CafeFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.cafe), cafe);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.cafe_fragment));
        transaction.commit();


    }

    @Override
    public void onAddCafe() {
        Log.d(TAG, "OnContactSelected: indo para "
                + getString(R.string.add_cafe_fragment));



        AddCafeFragment fragment = new AddCafeFragment();
      /*  Não precisa passar nada no bundle, vai ser tudo nessa teka
      Bundle args = new Bundle();
        args.putParcelable(getString(R.string.cafe), cafe);
        fragment.setArguments(args);*/

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.add_cafe_fragment));
        transaction.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initImageLoader();
        init();


    }


    //Iniciar o primeiro fragment
    private void init() {
        TelaFragment fragment = new TelaFragment();
        //android.support.v4.app.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        //Substitiu o que estiver no fragment_container com isso. transaction do inicio.
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }



    /*
     * Comprimir a bitmap pelo @param "quality"
     * Qualidade pode ser 1-100 : 100 começando da maior qualidade
     * @param bitmap
     * @param quality
     * @return*/

   public Bitmap compressBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        return bitmap;
    }
/*
    public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }*/


    public void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: pedir ao usuario permissao.");
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                REQUEST_CODE
        );
    }

    //Verifica se a permissão ta garantida pelos parametros passados
    public boolean checkPermission(String[] permission) {
        Log.d(TAG, "checkPermission: checking permissions for:" + permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                MainActivity.this,
                permission[0]);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: \n Permissions was not granted for: " + permission[0]);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);

        switch (requestCode) {
            case REQUEST_CODE:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[i]);
                    } else {
                        break;
                    }
                }
                break;
        }
    }
}













