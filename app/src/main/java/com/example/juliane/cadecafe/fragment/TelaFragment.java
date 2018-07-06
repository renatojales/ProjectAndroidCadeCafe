package com.example.juliane.cadecafe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.activity.CepActivity;

;

public class TelaFragment extends Fragment {
    private static final String TAG = "TelaFragment";
    private Button btnEntrar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tela, container, false);
        btnEntrar = (Button) view.findViewById(R.id.btnTelaCafe);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoritosFragment fragment = new FavoritosFragment();
                //android.support.v4.app.
                FragmentTransaction transaction = getFragmentManager().beginTransaction();


                //Substitiu o que estiver no fragment_container com isso. transaction do inicio.
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;

    }




}
