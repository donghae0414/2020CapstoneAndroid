package com.example.newtattooandroid.gesture;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.example.newtattooandroid.R;
import com.example.newtattooandroid.model.Vector2D;

public class SandboxView extends ImageView implements View.OnTouchListener {

    private Bitmap bitmap;
    private int width;
    private int height;
    private Camera camera;
    private Matrix transform = new Matrix();

    private Vector2D position = new Vector2D();
    private float scale = 1;
    private float angle = 0;

    private TouchManager touchManager = new TouchManager(2);
    private boolean isInitialized = false;

    //아치형 왜곡
    private static final int MESH_WIDTH = 50;
    private static final int MESH_HEIGHT = 50;
    private float mAngle = 0;   //부착 앵글
    private double mRadius;
    private int MESH_COUNT = (MESH_WIDTH + 1) * (MESH_HEIGHT + 1);
    private float[] dst = new float[MESH_COUNT * 2];
    private float[] origin = new float[MESH_COUNT * 2];

    // Debug helpers to draw lines between the two touch points
    private Vector2D vca = null;
    private Vector2D vcb = null;
    private Vector2D vpa = null;
    private Vector2D vpb = null;

    public SandboxView(Context context, Bitmap bitmap) {
        super(context);

        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        camera = new Camera();

        setOnTouchListener(this);
    }

    public SandboxView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int src_resource = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0);
        this.bitmap =  BitmapFactory.decodeResource(getResources(), src_resource);
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        camera = new Camera();

        setOnTouchListener(this);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        this.bitmap = bm;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        invalidate();
    }

    public void setAngle(float angle) {
        this.mAngle = angle;
        invalidate();
    }

    public void initMesh() {
        if(mAngle == 0){
            //왜곡 angle 0일 때
            mAngle = 0.1f;
        }
        //matrix 초기화
        transform.reset();

        mRadius = bitmap.getWidth() / (2 * Math.sin(mAngle / 2 * Math.PI / 180));
        int index = 0;
        float bw = bitmap.getWidth();
        float bh = bitmap.getHeight();
        for (int i = 0; i < MESH_HEIGHT + 1; i++) {
            float fy = bh / MESH_HEIGHT * i;
            for (int j = 0; j < MESH_WIDTH + 1; j++) {
                float fx = bw / MESH_WIDTH * j;

                origin[index * 2 + 0] = fx;
                origin[index * 2 + 1] = fy;
                float d = Math.abs(fx - bw / 2);
                double offsetY = mRadius * (1 - Math.cos(Math.asin(d / mRadius)));
                camera.save();
                camera.translate(0, (float) offsetY, 0);
                camera.getMatrix(transform);
                camera.restore();
                float[] point = null;
                float px = fx;
                float py = fy;

                point = new float[]{px, py};
                //메트릭스 왜곡 -> 원점이동 -> 회전 -> 스케일링 -> 기존 위치로 이동
                transform.postTranslate(-width/2.0f, -height/2.0f);
                transform.postRotate(getDegreesFromRadians(angle));
                transform.postScale(scale, scale);
                transform.postTranslate(position.getX(), position.getY());
                transform.mapPoints(point);

                dst[index * 2 + 0] = point[0];
                dst[index * 2 + 1] = point[1];

                index++;
            }
        }
    }

    private static float getDegreesFromRadians(float angle) {
        return (float)(angle * 180.0 / Math.PI);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        if (!isInitialized) {
            int w = getWidth();
            int h = getHeight();
            position.set(w / 2, h / 2);
            isInitialized = true;
        }

        initMesh();
        canvas.drawBitmapMesh(bitmap, MESH_WIDTH, MESH_HEIGHT, dst, 0, null, 0, null);
//        canvas.drawBitmap(bitmap, transform, paint);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        vca = null;
        vcb = null;
        vpa = null;
        vpb = null;

        try {
            touchManager.update(event);

            if (touchManager.getPressCount() == 1) {
                vca = touchManager.getPoint(0);
                vpa = touchManager.getPreviousPoint(0);
                position.add(touchManager.moveDelta(0));
            }
            else {
                if (touchManager.getPressCount() == 2) {
                    vca = touchManager.getPoint(0);
                    vpa = touchManager.getPreviousPoint(0);
                    vcb = touchManager.getPoint(1);
                    vpb = touchManager.getPreviousPoint(1);

                    Vector2D current = touchManager.getVector(0, 1);
                    Vector2D previous = touchManager.getPreviousVector(0, 1);
                    float currentDistance = current.getLength();
                    float previousDistance = previous.getLength();

                    if (previousDistance != 0) {
                        scale *= currentDistance / previousDistance;
                    }

                    angle -= Vector2D.getSignedAngleBetween(current, previous);
                }
            }

            invalidate();
        }
        catch(Throwable t) {
            // Error
        }
        return true;
    }

}