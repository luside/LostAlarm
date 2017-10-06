package com.sidel.indoor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
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


    }
}
