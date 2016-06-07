package com.example.proyectofinal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JuegoActivity extends AppCompatActivity {

    Imagen gato;
    Imagen raton;
    List<Imagen> quesos;
    Random rn;
    Handler h;
    boolean juego;
    ImageView img;
    Thread hilo;
    int vel, cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        vel = getIntent().getIntExtra("velocidad", 1);
        cont = 0;
        rn = new Random();
        Bitmap g;
        g = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
        gato = new Imagen(g, 100, 300);
        Bitmap r;
        r = BitmapFactory.decodeResource(getResources(), R.drawable.m);
        raton = new Imagen(r, 0, 0);

        Bitmap q;
        q = BitmapFactory.decodeResource(getResources(), R.drawable.cheese);
        quesos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            quesos.add(new Imagen(q, rn.nextInt(240), rn.nextInt(480)));
        }

        juego = true;
        img = new ImageView(this) {
            float x;
            float y;

            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                for (int i = 0; i < 10; i++) {
                    quesos.get(i).draw(canvas);
                }
                raton.draw(canvas);
                gato.draw(canvas);
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        raton.mover((event.getX() - x) / 50, (event.getY() - y) / 50);
                        break;
                }
                return super.onTouchEvent(event);
            }
        };
        img.setLongClickable(true);
        FrameLayout jg;
        jg = (FrameLayout) findViewById(R.id.jg);
        jg.addView(img);
        hilos();
    }

    void hilos() {
        hilo = new Thread(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                while (juego) {
                    try {
                        if (cont == 9) {
                            Message m = new Message();
                            Bundle b = new Bundle();
                            b.putInt("accion", 2);
                            m.setData(b);
                            h.sendMessage(m);
                        }
                        for (int i = 0; i < 10; i++) {
                            if (raton.colision(quesos.get(i))) {
                                if (quesos.get(i).isVisible()) {
                                    quesos.get(i).setVisible(false);
                                    cont++;
                                }
                            }
                        }
                        if (raton.colision(gato)) {
                            Message m = new Message();
                            Bundle b = new Bundle();
                            b.putInt("accion", 1);
                            m.setData(b);
                            h.sendMessage(m);
                        }
                        if (i == 70 - (vel * 10)) {
                            gato.mover(getDir(gato.left, raton.left),
                                    getDir(gato.top, raton.top));
                            i = 0;
                        }
                        i++;
                        Message m = new Message();
                        Bundle b = new Bundle();
                        b.putInt("accion", 0);
                        m.setData(b);
                        h.sendMessage(m);
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        h = new Handler() {
            boolean smsg = false;

            @Override
            public void handleMessage(Message msg) {
                switch (msg.getData().getInt("accion", 0)) {
                    case 0:
                        img.invalidate();
                        break;
                    case 1:
                        if (!smsg) {
                            Toast.makeText(JuegoActivity.this, "Te comio el gato", Toast.LENGTH_SHORT).show();
                            smsg = true;
                        }
                        juego = false;
                        finish();
                        break;
                    case 2:
                        if (!smsg) {
                            Toast.makeText(JuegoActivity.this, "Fin del juego", Toast.LENGTH_SHORT).show();
                            smsg = true;
                        }
                        juego = false;
                        finish();
                        break;
                }
            }
        };
        hilo.start();
    }

    float getDir(float a, float b) {
        if (a > b) {
            return -1;
        }
        return 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hilos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        juego = false;
    }
}
