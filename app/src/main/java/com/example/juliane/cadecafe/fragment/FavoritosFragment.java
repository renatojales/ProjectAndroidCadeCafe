package com.example.juliane.cadecafe.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.juliane.cadecafe.R;

import com.example.juliane.cadecafe.modelo.Cafe;
import com.example.juliane.cadecafe.utils.CafeListAdapter;

import com.example.juliane.cadecafe.utils.DatabaseHelper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritosFragment extends Fragment {

    private static final String TAG = "FavoritosFragment";
    private String testImageURL = "https://img.ibxk.com.br/2012/1/materias/10451707228103046.jpg?w=700&h=393&mode=crop";

    public interface OnCafeSelectedListener {
        public void OnCafeSelected(Cafe cafe);
    }

    OnCafeSelectedListener mCafeListener;

    public interface OnAddCafeListener {
        public void onAddCafe();
    }

    OnAddCafeListener mOnAddCafe;


    // Variáveis e widgets
    private static final int STANDARD_APPBAR = 0;
    private static final int PESQUISA_APPBAR = 1;
    private int mAppBarState;

    private AppBarLayout favoritosCafesBar, pesquisaBar;
    private CafeListAdapter adapter;
    private ListView cafesList;
    private EditText mProcurarCafes;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);
        favoritosCafesBar = (AppBarLayout) view.findViewById(R.id.viewCafesToolbar);
        pesquisaBar = (AppBarLayout) view.findViewById(R.id.viewPesquisaCafesToolbar);
        cafesList = (ListView) view.findViewById(R.id.cafe_list);
        mProcurarCafes = (EditText) view.findViewById(R.id.etPesquisa);
        Log.d(TAG, "onCreate: started.");



      //  setAppBarState(STANDARD_APPBAR);
        setupContactsList();

        // Ir para add cafés fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddCafe);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou no fab.");
                mOnAddCafe.onAddCafe();
            }
        });

        ImageView imgSair = (ImageView) view.findViewById(R.id.fechar);
        imgSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou para sair.");
                deslogarUsuario();

            }
        });

        ImageView imgBusca = (ImageView) view.findViewById(R.id.lupa);
        imgBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou para pesquisar.");
                toggleToolbarState();

            }
        });


        ImageView setaVoltar = (ImageView) view.findViewById(R.id.setaVoltar);
        setaVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicou para voltar pesquisa.");
                toggleToolbarState();
            }
        });


        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCafeListener = (OnCafeSelectedListener) getActivity();
            mOnAddCafe = (OnAddCafeListener) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    private void setupContactsList() {
        final ArrayList<Cafe> cafes = new ArrayList<>();
        //cafes.add(new Cafe("Gary the Guy", "rua", "34622174", testImageURL));
        // cafes.add(new Cafe("juju", "av", "34622174", testImageURL));

        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllCafes();

        //interage com tudo do banco
        if (!cursor.moveToNext()) {
            Toast.makeText(getActivity(), "Não há café para exibir", Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()) {
            cafes.add(new Cafe(
                    cursor.getString(1),//nome
                    cursor.getString(2),//telefone
                    cursor.getString(3),//device
                    cursor.getString(4),//endereco
                    cursor.getString(5)//profile image uri
            ));
        }

        //Mostrar o array baseado no nome
        Collections.sort(cafes, new Comparator<Cafe>() {
            @Override
            public int compare(Cafe o1, Cafe o2) {
                return o1.getNome().compareToIgnoreCase(o2.getNome());
            }
        });


        adapter = new CafeListAdapter(getActivity(), R.layout.layout_favoritoscafe, cafes, "");


        mProcurarCafes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mProcurarCafes.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cafesList.setAdapter(adapter);

        cafesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onClick: navegando cafe edit fragment" + getString(R.string.cafe_fragment));
                //Passar o contato a interface e mandar pro mainactivity
                mCafeListener.OnCafeSelected(cafes.get(position));

            }
        });


        mProcurarCafes.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mProcurarCafes.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void deslogarUsuario() {
        getActivity().finish();
    }


       //Iniciar o appbar status no toggle
        private void toggleToolbarState () {
            Log.d(TAG, "toggleToolbarState: toggle AppBarState");
            if (mAppBarState == STANDARD_APPBAR) {
                setAppBarState(PESQUISA_APPBAR);
            } else {
                setAppBarState(STANDARD_APPBAR);
            }
        }



        //Status do AppBar para busca

        private void setAppBarState (int state){
            Log.d(TAG, "setAppBarState: mudando o status do appbar para:" + state);
            mAppBarState = state;

            //Logica para esconder appbar
            if (mAppBarState == STANDARD_APPBAR) {
                pesquisaBar.setVisibility(View.GONE);
                favoritosCafesBar.setVisibility(View.VISIBLE);
                View view = getView();

                //Esconder o teclado

                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               // try {
                    //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                //} catch (NullPointerException e) {
                    //Log.d(TAG, "setAppBarState: NullPointerException: " + e.getMessage());
                } else if (mAppBarState == PESQUISA_APPBAR) {
                favoritosCafesBar.setVisibility(View.GONE);
                pesquisaBar.setVisibility(View.VISIBLE);

                //Abrir o teclado
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               // imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}

