package com.example.juliane.cadecafe.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.MainActivity;
import com.example.juliane.cadecafe.modelo.Cafe;

import java.io.File;

public class MudarFotoDialog extends DialogFragment {
    private static final String TAG = "ChangePhotoDialog";
    private Cafe cafe;


    public interface OnPhotoReceivedListener {
       // public void getBitmapImage(Bitmap bitmap);

        public void getImagePath(String imagePath);
    }

    OnPhotoReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_mudarfoto, container, false);

        //iniciando o textview para começar a camera
       /* TextView takePhoto = (TextView) view.findViewById(R.id.dialogCapturarFoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: iniciando camera.");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Init.CAMERA_REQUEST_CODE);


            }
        }); */



        //iniciando o textview para escolher foto da galeria
        TextView selecionarFoto = (TextView) view.findViewById(R.id.dialogEscolherFoto);
        selecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessar galeria.");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, Init.PICKFILE_REQUEST_CODE);
            }
        });

        // Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: fechar dialog.");
                getDialog().dismiss();
            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnPhotoReceived = (OnPhotoReceivedListener) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        /*
        Resultado quando tira foto da camera
         */

       /* if (requestCode == Init.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onActivityResult: tirando a foto.");

            //get nova imagem bitmap
           Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            Log.d(TAG, "onActivityResult: recebendo bitmap: " + bitmap);

            //mandar bitmap e fragment para interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();


        } */

          /*
        Resultado quando pega a imagem da galeria
         */
        if(requestCode == Init.PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            File file = new File(selectedImageUri.toString());
            Log.d(TAG, "onActivityResult: images: " + file.getPath());


            //Mandar o fragment bitmap pra interface
            mOnPhotoReceived.getImagePath(file.getPath());
            getDialog().dismiss();

        }


    }

}

