package com.example.juliane.cadecafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.juliane.cadecafe.R;
import com.example.juliane.cadecafe.fragment.AddCafeFragment;
import com.example.juliane.cadecafe.modelo.Cep;
import com.example.juliane.cadecafe.utils.HttpService;

import java.util.concurrent.ExecutionException;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class CepActivity extends AppCompatActivity {

    private EditText mCep;
    private TextView mResposta;
    private Button btnBuscar;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cep);
        mCep = (EditText) findViewById(R.id.etMain_cep);
        mResposta = (TextView) findViewById(R.id.etMain_resposta);
        btnBuscar = (Button) findViewById(R.id.buscarCep);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Cep retorno = new HttpService(mCep.getText().toString()).execute().get();
                    mResposta.setText(retorno.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

       //Voltar tela
        ImageView ivBackArrow = (ImageView) findViewById(R.id.setaVoltar);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked seta voltar.");
                finish();
                return;





            }
        });


    }
}

