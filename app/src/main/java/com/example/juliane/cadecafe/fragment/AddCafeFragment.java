package com.example.juliane.cadecafe.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.CepActivity;
import com.example.juliane.cadecafe.activity.MainActivity;
import com.example.juliane.cadecafe.modelo.Cafe;
import com.example.juliane.cadecafe.utils.DatabaseHelper;
import com.example.juliane.cadecafe.utils.Init;
import com.example.juliane.cadecafe.utils.MudarFotoDialog;
import com.example.juliane.cadecafe.utils.UniversalImageLoader;


import de.hdodenhof.circleimageview.CircleImageView;

public class AddCafeFragment extends Fragment implements MudarFotoDialog.OnPhotoReceivedListener {

    private static final String TAG = "AddContactFragment";

   // private Cafe mCafe;
    private EditText mTelefone, mNome, mEndereco;
    private CircleImageView mCafeImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;
    private Button btnCep;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcafe, container, false);
        mTelefone = (EditText) view.findViewById(R.id.etCafeTelefone);
        mNome = (EditText) view.findViewById(R.id.etCafeNome);
        mEndereco = (EditText) view.findViewById(R.id.etCafeEndereco);
        mCafeImage = (CircleImageView) view.findViewById(R.id.cafeImage);
        mSelectDevice = (Spinner) view.findViewById(R.id.selectDevice);
        toolbar = (Toolbar) view.findViewById(R.id.editCafeToolbar);
        Log.d(TAG, "onCreateView: started.");

        mSelectedImagePath = null;




        btnCep = (Button) view.findViewById(R.id.buscarCep);
        btnCep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CepActivity.class);
                startActivity(intent);

            }
        });

        //load the default images by causing an error
        UniversalImageLoader.setImage(null, mCafeImage, null, "");

        //titulo toolbar
        TextView heading = (TextView) view.findViewById(R.id.textCafeToolbar);
        heading.setText(getString(R.string.add_cafe));

        //configuraç~eos toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //Voltar tela
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.setaVoltar);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Log.d(TAG, "onClick: clicked seta voltar.");
        //remove previous fragment from the backstack (therefore navigating back)
        getActivity().getSupportFragmentManager().popBackStack();
        }
        });


        // initiate the dialog box for choosing an image
        ImageView ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                /*
                Make sure all permissions have been verified before opening the dialog
                 */
        for(int i = 0; i < Init.PERMISSIONS.length; i++){
            String[] permission = {Init.PERMISSIONS[i]};
            if(((MainActivity)getActivity()).checkPermission(permission)){
               if(i == Init.PERMISSIONS.length - 1){
                   Log.d(TAG, "onClick: opening the 'image selection dialog box'.");
                    MudarFotoDialog dialog = new MudarFotoDialog();
                    dialog.show(getFragmentManager(), getString(R.string.mudar_foto_dialog));
                    dialog.setTargetFragment(AddCafeFragment.this, 0);
            }
            }else{
                ((MainActivity)getActivity()).verifyPermissions(permission);
                }
            }
        }
        });

        //set onclicklistenr to the 'checkmark' icon para salvar cafe
            ImageView confirmNovoCafe = (ImageView) view.findViewById(R.id.ivCheck);
            confirmNovoCafe.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: salvar novo cafe.");

           if(checkStringIfNull(mNome.getText().toString())){
                Log.d(TAG, "onClick: salvando cafe. " + mNome.getText().toString() );

                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cafe cafe = new Cafe(mNome.getText().toString(),
                        mTelefone.getText().toString(),
                        mSelectDevice.getSelectedItem().toString(),
                        mEndereco.getText().toString(),
                        mSelectedImagePath);
                if(databaseHelper.addCafe(cafe)){
                    Toast.makeText(getActivity(), "Cafe Salvo", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(), "Error para Salvar", Toast.LENGTH_SHORT).show();
                }
            }

        }
    });

        return view;
    }


    private boolean checkStringIfNull(String string){
        if(string.equals("")){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cafe_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contact.");
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieves the selected image from the bundle (coming from ChangePhotoDialog)
     * @param bitmap
     */


  /*  @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: " + bitmap);
        //get the bitmap from 'ChangePhotoDialog'
        if(bitmap != null) {
            //compress the image (if you like)
            ((MainActivity)getActivity()).compressBitmap(bitmap, 70);



            mCafeImage.setImageBitmap(bitmap);
            Bitmap mCafeImage = BitmapFactory.decodeFile(mSelectedImagePath);
            //mSelectedImagePath =((MainActivity) getActivity()).bitMapToString(bitmap).replace(":/", "://");
        }
    } */

    @Override
    public void getImagePath(String imagePath) {
        Log.d(TAG, "getImagePath: got the image path: " + imagePath);

        if( !imagePath.equals("")){
            imagePath = imagePath.replace(":/", "://");
            mSelectedImagePath = imagePath;
            UniversalImageLoader.setImage(imagePath, mCafeImage, null, "");


        }
    }
    public void carregarFoto(){

    }



}


