package com.sidel.indoor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.view.View;

/**
 * Created by lsd20 on 06/10/2017.
 */

public class ApPoint extends View {

    private boolean active;
    private boolean visible;
    private PointF location;
    private Paint paint;
    private float radius;


    private Fingerprint fingerprint;

    public ApPoint(Context context) {
        super(context);
        active = false;
        visible = true;
        location = new PointF(0, 0);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(25);
        paint.setAntiAlias(true);
        radius = 10f;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void drawApPoints(Canvas canvas) {
        if (this.visible) {
            if (this.active) {
                this.paint.setColor(Color.GREEN);
            } else {
                this.paint.setColor(Color.RED);
            }
        }
        Log.d("TAG",this.location.toString());
        canvas.drawCircle(this.location.x, this.location.y, this.radius, this.paint);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(PointF location) {
        this.location = location;
    }


    public Fingerprint getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Fingerprint fingerprint) {
        this.fingerprint = fingerprint;
        this.location = fingerprint.getLocation();
    }
}
