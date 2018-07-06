package com.example.juliane.cadecafe.fragment;

import android.content.Context;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.MainActivity;
import com.example.juliane.cadecafe.modelo.Cafe;
import com.example.juliane.cadecafe.utils.DatabaseHelper;
import com.example.juliane.cadecafe.utils.Init;
import com.example.juliane.cadecafe.utils.MudarFotoDialog;
import com.example.juliane.cadecafe.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditCafeFragment extends Fragment implements MudarFotoDialog.OnPhotoReceivedListener {
    private static final String TAG = "EditCafeFragment";

    //nullpointer exception quando adicionar um novo bundle a MainActivity
    public EditCafeFragment(){
        super();
        setArguments(new Bundle());

    }

    private Cafe mCafe;
    private EditText mNome, mTelefone, mEndereco;
    private CircleImageView mCafeImage;
    private Spinner mSelectDevice;
    private Toolbar toolbar;
    private String mSelectedImagePath;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editcafe, container, false);
        mNome = (EditText) view.findViewById(R.id.etCafeNome);
        mTelefone = (EditText) view.findViewById(R.id.etTelefone);
        mSelectDevice = (Spinner) view.findViewById(R.id.selectDevice);
        mEndereco = (EditText) view.findViewById(R.id.etCafeEndereco);
        mCafeImage = (CircleImageView) view.findViewById(R.id.cafeImage);
        toolbar = (Toolbar) view.findViewById(R.id.editCafeToolbar);

        mSelectedImagePath = null;
        //o titulo do toolbar
        TextView heading = (TextView) view.findViewById(R.id.textCafeToolbar);
        heading.setText(getString(R.string.edit_cafe));

        //required for setting up the toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        //pegar o cafe do bundle
        mCafe = getCafeFromBundle();

        if (mCafe != null) {
            init();
        }


        //ir para seta voltar
        ImageView setaVoltar = (ImageView) view.findViewById(R.id.setaVoltar);
        setaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click seta voltar.");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // salvar alterações da edição do café
        ImageView ivCheckMark = (ImageView) view.findViewById(R.id.ivCheck);
        ivCheckMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click icone ok.");
                //Executar o método salvar no banco de dados
                if (checkStringIfNull(mNome.getText().toString())) {
                    Log.d(TAG, "onClick: alterações café. " + mNome.getText().toString());

                    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                    Cursor cursor = databaseHelper.getCafeID(mCafe);

                    int cafeID = -1;
                    while(cursor.moveToNext()) {
                        cafeID = cursor.getInt(0);
                    }
                    if(cafeID > -1){

                        if(mSelectedImagePath != null){
                            mCafe.setProfileImage(mSelectedImagePath);
                        }
                    mCafe.setNome(mNome.getText().toString());
                    mCafe.setTelefone(mTelefone.getText().toString());
                    mCafe.setDevice(mSelectDevice.getSelectedItem().toString());
                    mCafe.setEndereco(mEndereco.getText().toString());


                    databaseHelper.updateCafe(mCafe, cafeID);
                    Toast.makeText(getActivity(), "Cafe atualizado", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Error para Salvar", Toast.LENGTH_SHORT).show();
                }
            }
        }
    });



    //Clicar na câmera
        ImageView camera = (ImageView) view.findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checar se tem permissão antes de abrir a dialog//

                for (int i = 0; i < Init.PERMISSIONS.length; i++) {
                    String[] permission = {Init.PERMISSIONS[i]};
                    if (((MainActivity) getActivity()).checkPermission(permission)) {
                        if (i == Init.PERMISSIONS.length - 1) {
                            Log.d(TAG, "onClick: opening the 'image selection dialog box'.");
                            MudarFotoDialog dialog = new MudarFotoDialog();
                            dialog.show(getFragmentManager(), getString(R.string.mudar_foto_dialog));
                            dialog.setTargetFragment(EditCafeFragment.this, 0);
                        }
                    } else {
                        ((MainActivity) getActivity()).verifyPermissions(permission);
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

    private void init(){

        mNome.setText(mCafe.getNome());
        mTelefone.setText(mCafe.getTelefone());
        mEndereco.setText(mCafe.getEndereco());

        UniversalImageLoader.setImage(mCafe.getProfileImage(), mCafeImage, null , "");

        //Escolhendo no spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.device_options,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectDevice.setAdapter(adapter);

        int position = adapter.getPosition(mCafe.getDevice());
        mSelectDevice.setSelection(position);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cafe_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_delete:
                Log.d(TAG, "onOptionsItemSelected: deletando cafe.");
                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getCafeID(mCafe);

                int cafeID = -1;
                while(cursor.moveToNext()) {
                    cafeID = cursor.getInt(0);
                }
                if(cafeID > -1 ){
                    if(databaseHelper.deleteCafe(cafeID) > 0){
                        Toast.makeText(getActivity(), "Cafe deletado", Toast.LENGTH_SHORT).show();

                        //Limpar dados do bundle
                        this.getArguments().clear();

                        //remover fragments anteriores
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }

        }
        return super.onOptionsItemSelected(item);
    }


    private Cafe getCafeFromBundle(){
        Log.d(TAG, "getCafeFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.cafe));
        }else{
            return null;
        }
    }

  /*  @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: " + bitmap);
        //pegar bitmap de 'MudarFotoDialog'
        if(bitmap != null) {
            //comprimir a imagem
            ((MainActivity)getActivity()).compressBitmap(bitmap, 70);
            mCafeImage.setImageBitmap(bitmap);
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



}
