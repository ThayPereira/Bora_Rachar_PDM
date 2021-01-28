package com.example.boraracharpdm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static com.example.boraracharpdm.R.id.result;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText ins_valor, ins_qt;
    TextView result;
    ImageButton shareB, ttsB;
    TextToSpeech falar;
    int qtPessoa;
    double valor ;
    String resultado= "0,00";
    double pagar;
    DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.result);

        ins_valor = (EditText) findViewById(R.id.ins_valor);
        ins_valor.addTextChangedListener(this);

        ins_qt = (EditText) findViewById(R.id.ins_qt);
        ins_qt.addTextChangedListener(this);

        shareB = (ImageButton) findViewById(R.id.shareB);
        shareB.setOnClickListener(this);

        ttsB = (ImageButton) findViewById(R.id.ttsB);
        ttsB.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void afterTextChanged(Editable s) {
      try {
         valor = Double.parseDouble(ins_valor.getText().toString());
         qtPessoa = Integer.parseInt(ins_qt.getText().toString());
         pagar=divide(valor,qtPessoa);
         result.setText("R$  "+df.format(pagar));

         } catch (NumberFormatException e) {
         result.setText("R$ 0.00");
     }

    }
protected double divide(double valor1, int qt){
    return  valor1 / qt;
    }

    @Override
    public void onClick(View view) {
        if(view == shareB) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Valor à Pagar: " + result.getText().toString() + " Por Pessoa.");
            startActivity(intent);
        }

        if(view == ttsB){
            if(falar != null){
                falar.speak("O valor à pagar é: "+result.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,"ID1");

            }
        }
  }
    protected void onActivityResult (int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == 1122){

                if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                    falar = new TextToSpeech(this, this);

                }else{

                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }

            }
    }


    @Override
    public void onInit(int iniStatus) {
        if(iniStatus == TextToSpeech.SUCCESS){
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show();
        }else if (iniStatus == TextToSpeech.ERROR){
            Toast.makeText(this,"Sem TTS habilitado...",Toast.LENGTH_LONG).show();
        }
    }
}