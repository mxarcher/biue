package com.mxarcher.biue.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.mxarcher.biue.R;

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/28 20:26
 * @Description:
 */
public class ChartView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = "ChartView";
    private static final int TIME_IN_FRAME = 60;
    private final float xCount = 10;
    private final Paint solidLinePaint = new Paint();
    private final Paint curvePaint = new Paint();
    private final Path curvePath = new Path();
    // 这个参数可以控制显示帧率，减少内存消耗
    ReentrantReadWriteLock lock;
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean isDrawing;
    private int maxSize = 100;
    private List<Short> TList;
    private List<Double> FFList;
    private float width = 0f;
    private float height = 0f;
    private boolean isDouble = false;


    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        initPaint(context);
    }


    private void initPaint(Context context) {
        solidLinePaint.setStyle(Paint.Style.STROKE);
        solidLinePaint.setStrokeWidth(3f);
        solidLinePaint.setColor(ContextCompat.getColor(context, R.color.frost));
        curvePaint.setStyle(Paint.Style.STROKE);
        curvePaint.setStrokeWidth(3f);
        curvePaint.setColor(ContextCompat.getColor(context, R.color.black));
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        this.width = i1;
        this.height = i2;

    }

    public void setTList(List<Short> list, ReentrantReadWriteLock lock) {
        this.TList = list;
        this.lock = lock;
    }

    public void setFList(List<Double> list, ReentrantReadWriteLock lock) {
        this.FFList = list;
        this.lock = lock;
        this.isDouble = true;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.d(TAG, "surfaceDestroyed: ");
        isDrawing = false;
    }


    @Override
    public void run() {
        while (isDrawing) {
            // 获取更新之前的时间
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();
            long diff = endTime - startTime;
            while (diff <= TIME_IN_FRAME) {
                diff = System.currentTimeMillis() - startTime;
                Thread.yield();
            }
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.WHITE);
                drawAxis(canvas);
                if (!isDouble) {
                    drawTCurve(canvas);
                } else drawFCurve(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void drawAxis(Canvas canvas) {
        Log.d(TAG, "drawAxis: width:" + this.width + "height" + this.height);
        float gridLength = (this.width) / xCount;
        canvas.save();
        // 计算空闲
        int yCount = (int) (this.height / gridLength);
        float start = (this.height - yCount * gridLength) / 2;

        // 十个空格显然要十一条线
        for (int i = 0; i <= xCount; i++) {
            float x = gridLength * i;
            canvas.drawLine(x, start, x, this.height - start, solidLinePaint);
        }
        for (int i = 0; i <= yCount; i++) {
            float y = gridLength * i + start;
            canvas.drawLine(0f, y, this.width, y, solidLinePaint);
        }
        canvas.restore();
    }

    private void drawTCurve(Canvas canvas) {
        if (TList != null) {

            canvas.save();
            // 将坐标移动到中心右侧的位置
            canvas.translate(this.width, this.height);
            // 之前的一帧已经有的path因此需要重置
            curvePath.reset();


            Log.d(TAG, "drawCurve: ");

            lock.readLock().lock();

            int sampleCount = TList.size();
            // y 是数列最后一个数,也是七点路径的y坐标
            // 计算起点y坐标
            float yStart = 0;
            if (sampleCount > 0)
                yStart = TList.get(sampleCount - 1);

            // 数列个数

            // 点和点之间的宽度，由于需要把第一个数刚好处在最前面，因此需要除的是count-1
            float dx = this.width / (sampleCount - 1);
            curvePath.moveTo(0f, yStart); // 移动到路径起点
            // 根据起始点计算其他点的坐标
            for (int i = 0; i < TList.size(); i++) {
                float y = -TList.get(sampleCount - i - 1) / 10f;
                float x = -i * dx;
                Log.d(TAG, "drawTCurve: " + " " + x + " " + y + " dx:" + dx);
                curvePath.quadTo(x, y, x, y);// 使用贝塞尔进行优化曲线显示，由于不是很懂，因此控制点选择的是这个点本身
            }
            lock.readLock().unlock();
            canvas.drawPath(curvePath, curvePaint);
            canvas.restore();
        }
    }

    private void drawFCurve(Canvas canvas) {
        if (FFList != null) {
            canvas.save();
            // 将坐标移动到中心右侧的位置
            canvas.translate(this.width, this.height);
            // 之前的一帧已经有的path因此需要重置
            curvePath.reset();

            lock.readLock().lock();

            int sampleCount = FFList.size();
            // y 是数列最后一个数,也是七点路径的y坐标
            // 计算起点y坐标
            Double yStart = (double) 0;
            if (sampleCount > 0)
                yStart = FFList.get(sampleCount - 1);

            // 数列个数

            // 点和点之间的宽度，由于需要把第一个数刚好处在最前面，因此需要除的是count-1
            float dx = this.width / (sampleCount - 1);
            Log.d(TAG, "drawFCurve: " + dx);
            curvePath.moveTo(0f, yStart.floatValue()); // 移动到路径起点
            // 根据起始点计算其他点的坐标
            for (int i = 0; i < FFList.size(); i++) {
                Log.d(TAG, "drawCurve: " + i + "  " + sampleCount);
                float y = -FFList.get(sampleCount - i - 1).floatValue() * 10;
                float x = -i * dx;
                Log.d(TAG, "drawFCurve: " + " " + x + " " + y + " dx:" + dx);
                curvePath.quadTo(x, y, x, y);// 使用贝塞尔进行优化曲线显示，由于不是很懂，因此控制点选择的是这个点本身
            }
            lock.readLock().unlock();
            canvas.drawPath(curvePath, curvePaint);
            canvas.restore();
        }
    }
}
