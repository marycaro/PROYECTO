package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class OpcionesActivity extends AppCompatActivity {

    Spinner vel;
    Button juego;
    int velocidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        vel = (Spinner) findViewById(R.id.spVelocidad);
        List<String> lista = new ArrayList<>();
        lista.add("1");
        lista.add("2");
        lista.add("3");
        lista.add("4");
        lista.add("5");
        lista.add("6");
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, lista);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        vel.setAdapter(adapter);
        SharedPreferences s = getPreferences(MODE_PRIVATE);
        velocidad = s.getInt("velocidad", 1);
        vel.setSelection(velocidad - 1);
        juego = (Button) findViewById(R.id.btnJuego);
        juego.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(OpcionesActivity.this, JuegoActivity.class);
                int v = Integer.valueOf(vel.getSelectedItem().toString());
                i.putExtra("velocidad", v);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        SharedPreferences.Editor e = getPreferences(MODE_PRIVATE).edit();
        int v = Integer.valueOf(vel.getSelectedItem().toString());
        e.putInt("velocidad", v);
        e.commit();
        super.onPause();
    }
}
