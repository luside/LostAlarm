package com.sidel.indoor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by lsd20 on 06/10/2017.
 */

public class Map extends ImageView {

    private Bitmap bitmap;
    private ArrayList<ApPoint> apPoints;

    public Map(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wilson);
        this.apPoints = new ArrayList<ApPoint>();
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int w = this.getWidth();
        int h = this.getHeight();
        Bitmap resizeBmp = Bitmap.createScaledBitmap(this.bitmap, w, h, true);
        canvas.drawBitmap(resizeBmp, 0, 0, null);
        Log.d("TAG","onDraw");
        for (ApPoint point : this.apPoints) {
            point.drawApPoints(canvas);
        }


    }

    public ApPoint createNewWifiPointOnMap(PointF location) {
        ApPoint apPoint = new ApPoint(getContext());
        apPoint.setLocation(location);
        apPoints.add(apPoint);
        return apPoint;
    }

    public ApPoint createNewWifiPointOnMap(Fingerprint fingerprint, boolean visible) {
        ApPoint ap = createNewWifiPointOnMap(fingerprint);
        ap.setVisible(visible);
        return ap;
    }

    public ApPoint createNewWifiPointOnMap(Fingerprint fingerprint) {
        ApPoint ap = new ApPoint(getContext());
        ap.setFingerprint(fingerprint);
        apPoints.add(ap);
        return ap;
    }

    public void setWifiPointViewPosition(ApPoint ap, PointF location) {
        ap.setLocation(location);
    }
}
