package com.cmg5461.jb4u.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;

public class GraphView extends View {

    public static final int SIZE = 300;

    private Bitmap mBackground;
    private Paint mBackgroundPaint;
    private Paint mBorderPaint;
    private Paint mPlotAxisPaint;
    private Paint mPlotTextPaint;

    private final int mBackgroundColor = 0xFF000000;
    private final int mBorderColor = 0xFFAAAAAA;
    private final int mPlotAxisColor = 0xFFBFBFBF;
    private final int mPlotTextColor = 0xFFBFBFBF;

    public GraphSeries mign1 = new GraphSeries();
    public GraphSeries mign2 = new GraphSeries();
    public GraphSeries mign3 = new GraphSeries();
    public GraphSeries mign4 = new GraphSeries();
    public GraphSeries mign5 = new GraphSeries();
    public GraphSeries mign6 = new GraphSeries();


    public GraphView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GraphView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(final Context context) {
        this(context, null, 0);
    }

    @TargetApi(11)
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initDrawingTools();
    }

    private void initDrawingTools() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setFilterBitmap(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setFilterBitmap(true);

        mPlotAxisPaint = new Paint();
        mPlotAxisPaint.setStyle(Paint.Style.STROKE);
        mPlotAxisPaint.setColor(mPlotAxisColor);
        mPlotAxisPaint.setFilterBitmap(true);

        mPlotTextPaint = new Paint();
        mPlotTextPaint.setColor(mPlotTextColor);
        mPlotTextPaint.setTextSize(getPxForDP(18));
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        final Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        return state;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int chosenWidth = chooseDimension(widthMode, widthSize);
        final int chosenHeight = chooseDimension(heightMode, heightSize);
        setMeasuredDimension(chosenWidth, chosenHeight);
    }

    private int chooseDimension(final int mode, final int size) {
        switch (mode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
            default:
                return getDefaultDimension();
        }
    }

    private int getDefaultDimension() {
        return SIZE;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        drawGauge();
    }

    private void drawGauge() {
        if (null != mBackground) {
            // Let go of the old background
            mBackground.recycle();
        }
        // Create a new background according to the new width and height
        mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mBackground);
        final float scale = Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        // Create a new background according to the new width and height

        drawBackground(canvas);
        //drawBorder(canvas);
        drawAxis(canvas);
        drawSeries(canvas);

        final float scale = Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
    }

    private void drawBackground(final Canvas canvas) {
        if (mBackground != null) canvas.drawBitmap(mBackground, 0, 0, mBackgroundPaint);
    }

    private void drawBorder(final Canvas canvas) {
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, mBorderPaint);
    }

    private void drawAxis(final Canvas canvas) {
        float x1Plot = getPxForDP(30);
        float y1Plot = getPxForDP(9);
        float x2Plot = getWidth() - getPxForDP(5);
        float y2Plot = getHeight() - getPxForDP(24);
        canvas.drawLine(x1Plot, y2Plot, x1Plot, y1Plot, mPlotAxisPaint);
        canvas.drawLine(x1Plot, y2Plot, x2Plot, y2Plot, mPlotAxisPaint);

        float yMin = 0;
        float yMax = 10;
        float xMin = -10;
        float xMax = 0;

        int nMax = 11;
        for (int i = 0; i < nMax; i++) {
            float val = xMin + (xMax - xMin) * i / (nMax - 1);
            float xval = x1Plot + (x2Plot - x1Plot) * i / (nMax - 1);
            canvas.drawLine(xval, y2Plot, xval, y1Plot, mPlotAxisPaint);
            canvas.drawText(Math.round(val) + "", xval - getPxForDP(10), getHeight() - getPxForDP(5), mPlotTextPaint);
        }

        nMax = 6;
        for (int i = 0; i < nMax; i++) {
            float val = yMax - (yMax - yMin) * i / (nMax - 1);
            float yval = y1Plot + (y2Plot - y1Plot) * i / (nMax - 1);
            canvas.drawLine(x1Plot, yval, x2Plot, yval, mPlotAxisPaint);
            canvas.drawText(Math.round(val) + "", getPxForDP(5), yval + getPxForDP(6), mPlotTextPaint);
        }
    }

    private void drawSeries(Canvas canvas) {


        GraphPoint gpPrev = null;

        float x1Plot = getPxForDP(30);
        float y1Plot = getPxForDP(9);
        float x2Plot = getWidth() - getPxForDP(5);
        float y2Plot = getHeight() - getPxForDP(24);

        float yMin = 0;
        float yMax = 10;
        float xMin = -10;
        float xMax = 0;

        long now = System.currentTimeMillis();
        float dx = x2Plot - x1Plot;
        float dy = y2Plot - y1Plot;
        if (mign1.dataList.size() > 0) {
            for (int i = mign1.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign1.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign1.paint);
                    }
                }
                gpPrev = gp;
            }
        }
        gpPrev = null;
        if (mign2.dataList.size() > 0) {
            for (int i = mign2.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign2.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign2.paint);
                    }
                }
                gpPrev = gp;
            }
        }
        gpPrev = null;
        if (mign3.dataList.size() > 0) {
            for (int i = mign3.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign3.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign3.paint);
                    }
                }
                gpPrev = gp;
            }
        }
        gpPrev = null;
        if (mign4.dataList.size() > 0) {
            for (int i = mign4.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign4.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign4.paint);
                    }
                }
                gpPrev = gp;
            }
        }
        gpPrev = null;
        if (mign5.dataList.size() > 0) {
            for (int i = mign5.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign5.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign5.paint);
                    }
                }
                gpPrev = gp;
            }
        }
        gpPrev = null;
        if (mign6.dataList.size() > 0) {
            for (int i = mign6.dataList.size() - 1; i > -1; i--) {
                GraphPoint gp = mign6.dataList.get(i);
                if (gpPrev != null) {
                    if ((gpPrev.timestamp - now / 1000) >= xMin) {
                        float x1 = x2Plot + (gpPrev.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float x2 = x2Plot + (gp.timestamp - now) / 1000F / (xMax - xMin) * dx;
                        float y1 = Math.min(Math.max(yMax - gpPrev.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        float y2 = Math.min(Math.max(yMax - gp.value, yMin), yMax) / (yMax - yMin) * dy + y1Plot;
                        canvas.drawLine(x1, y1, x2, y2, mign6.paint);
                    }
                }
                gpPrev = gp;
            }
        }
    }

    private float getPxForDP(float dp) {
        Resources r = getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public class GraphSeries {
        public Paint paint = new Paint();
        public String name = "";
        public ArrayList<GraphPoint> dataList = new ArrayList<>();

        public void purge(long ts) {
            Iterator it = dataList.iterator();
            while (it.hasNext()) {
                GraphPoint gp = (GraphPoint) it.next();
                if (gp.timestamp < ts) it.remove();
            }
        }

        public void addValue(long ts, float value) {
            dataList.add(new GraphPoint(ts, value));
        }
    }

    private class GraphPoint {
        public GraphPoint(long timestamp, float value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public long timestamp = 0L;
        public float value = 0;
    }
}
