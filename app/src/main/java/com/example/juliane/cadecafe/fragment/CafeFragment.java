package com.example.juliane.cadecafe.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.MapActivity;
import com.example.juliane.cadecafe.modelo.Cafe;
import com.example.juliane.cadecafe.utils.CafePropriedadesListAdapter;
import com.example.juliane.cadecafe.utils.DatabaseHelper;
import com.example.juliane.cadecafe.utils.UniversalImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CafeFragment extends Fragment {
    private static final String TAG = "CafeFragment";


    //nullpointer exception quando adicionar um novo bundle a MainActivity
    public CafeFragment(){
        super();
        setArguments(new Bundle());

    }

    public interface onEditCafeListener{
        public void onEditcafeSelected(Cafe cafe);
    }

    onEditCafeListener mOnEditCafeListener;



    private Toolbar toolbar;
    private Cafe mCafe;
    private TextView mCafetNome;
    private CircleImageView mCafeImage;
    private ListView mListView;
    private Button btnCafe;
    private Spinner mSelectDevice;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cafe, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.cafeToolbar);
        mCafe = getCafeFromBundle();
        mCafetNome = (TextView) view.findViewById(R.id.cafeNome);
        mCafeImage = (CircleImageView) view.findViewById(R.id.cafeImage);
        mListView = (ListView) view.findViewById(R.id.lvCafeProperties);
        mSelectDevice = (Spinner) view.findViewById(R.id.selectDevice);
        Log.d(TAG, "onCreateView: started.");
        mCafe = getCafeFromBundle();

        btnCafe = (Button) view.findViewById(R.id.btnCafeProximo);
        btnCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);

            }
        });


        if (mCafe != null){
            Log.d(TAG, "onCreateView: recebeu cafe" + mCafe.getNome());
        }

        //required for setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        init();

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
        // ir para edição da cafeteria
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: click icone editar.");
                mOnEditCafeListener.onEditcafeSelected(mCafe);

            }
        });




        return view;

    }



    private void init(){
        mCafetNome.setText(mCafe.getNome());
        UniversalImageLoader.setImage(mCafe.getProfileImage(), mCafeImage, null, "");





        //Escolhendo no spinner
        ArrayAdapter<CharSequence> adapterDevice = ArrayAdapter.createFromResource(getActivity(), R.array.device_options,
                android.R.layout.simple_spinner_item);
        adapterDevice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectDevice.setAdapter(adapterDevice);


        int position = adapterDevice.getPosition(mCafe.getDevice());
        mSelectDevice.setSelection(position);

        ArrayList<String> propriedades = new ArrayList<>();
        propriedades.add(mCafe.getTelefone());
        propriedades.add(mCafe.getEndereco());


        CafePropriedadesListAdapter adapter = new CafePropriedadesListAdapter(getActivity(), R.layout.layout_cardview, propriedades);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
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


    @Override
    public void onResume() {
        super.onResume();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor  = databaseHelper.getCafeID(mCafe);

        int cafeID = -1;
        while(cursor.moveToNext()){
            cafeID = cursor.getInt(0);
        }
        if(cafeID > -1){
            init();
        }else{
            this.getArguments().clear(); //optional clear arguments
            getActivity().getSupportFragmentManager().popBackStack();
        }
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

//Chama  a interface que vai mandar pro main que vai mandar pro editfragment
     @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mOnEditCafeListener = (onEditCafeListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }


    }





}