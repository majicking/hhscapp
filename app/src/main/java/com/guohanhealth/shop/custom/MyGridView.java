package com.guohanhealth.shop.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import com.guohanhealth.shop.R;

public class MyGridView extends GridView {
    public MyGridView(Context context,
                      AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public MyGridView(Context context) {
        super(context);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        return false;
    }
    private int rownum;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int colnum = getNumColumns(); //获取列数
        int total = getChildCount();  //获取Item总数

        //计算行数
        if (total % colnum == 0) {
            rownum = total / colnum;
        } else {
            rownum = (total / colnum) + 1; //当余数不为0时，要把结果加上1
        }
        Paint localPaint; //设置画笔
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
        localPaint.setColor(getContext().getResources().getColor(R.color.gray));//画笔颜色

        View view0 = getChildAt(0); //第一个view
        View viewColLast = getChildAt(colnum - 1);//第一行最后一个view
        View viewRowLast = getChildAt((rownum - 1) * colnum); //第一列最后一个view


        for (int i = 1, c = 1; i < rownum || c < colnum; i++, c++) {
            //画横线
            canvas.drawLine(view0.getLeft(), view0.getBottom() * i, viewColLast.getRight(), viewColLast.getBottom() * i, localPaint);
            //画竖线
            canvas.drawLine(view0.getRight() * c, view0.getTop(), viewRowLast.getRight() * c, viewRowLast.getBottom(), localPaint);
        }
    }
}