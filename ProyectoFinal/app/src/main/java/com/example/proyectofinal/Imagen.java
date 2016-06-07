package com.example.proyectofinal;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Parcelable;

/**
 * Created by yessy on 02/06/2016.
 */
public class Imagen extends RectF {
    static Parcelable.Creator CREATOR;
    Bitmap imagen;
    boolean visible;

    public Imagen(Bitmap imagen, float x, float y) {
        visible = true;
        this.imagen = imagen;
        set(0, 0, imagen.getWidth(), imagen.getHeight());
        offsetTo(x, y);
    }

    public void mover(float x, float y) {
        left += x;
        right = left + imagen.getWidth();
        top += y;
        bottom = top + imagen.getHeight();
    }

    public void setPosicion(float x, float y) {
        left = x;
        right = left + imagen.getWidth();
        top = y;
        bottom = top + imagen.getHeight();
    }

    public boolean colision(Imagen imagen) {
        return imagen.left >= left && imagen.left <= right &&
                imagen.top >= top && imagen.top <= bottom ||
                imagen.right >= left && imagen.right <= right &&
                        imagen.bottom >= top && imagen.bottom <= bottom;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void draw(Canvas c) {
        if (visible) {
            c.drawBitmap(imagen, null, this, null);
        }
    }
}
