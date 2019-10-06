package com.av.verticalchipgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MarginLayoutParamsCompat;

import com.google.android.material.chip.ChipGroup;

import java.util.Deque;
import java.util.LinkedList;

public class CustomChipGroup extends ChipGroup {

    int rows = 2;
    Deque<Integer> deque = new LinkedList<>();

    public CustomChipGroup(Context context) {
        super(context);
    }

    public CustomChipGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractAttributeValues(context, attrs);
    }

    public CustomChipGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttributeValues(context,attrs);
    }

    private void extractAttributeValues(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomChipGroup, 0, 0);
        try {
            rows = ta.getInteger(R.styleable.CustomChipGroup_rows, 2);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onLayout(boolean sizeChanged, int left, int top, int right, int bottom) {
        deque.clear();
        if (this.getChildCount() != 0) {
            int paddingStart = this.getPaddingLeft();
            int paddingEnd = this.getPaddingRight();
            int childStart = paddingStart;
            int childTop = this.getPaddingTop();
            int childBottom = childTop;
            int maxChildEnd = right - left - paddingEnd;

            boolean secondColumn = false;
            int childCountCurrentColumn =0;
            for(int i = 0; i < this.getChildCount(); ++i) {
                View child = this.getChildAt(i);
                if (child.getVisibility() != GONE) {
                    ViewGroup.LayoutParams lp = child.getLayoutParams();
                    int startMargin = 0;
                    int endMargin = 0;
                    if (lp instanceof ViewGroup.MarginLayoutParams) {
                        ViewGroup.MarginLayoutParams marginLp = (ViewGroup.MarginLayoutParams)lp;
                        startMargin = MarginLayoutParamsCompat.getMarginStart(marginLp);
                        endMargin = MarginLayoutParamsCompat.getMarginEnd(marginLp);
                    }

                    if ( childCountCurrentColumn >= rows) {
                        childCountCurrentColumn = 0;
                        childStart = deque.pollFirst()+ getChipSpacingHorizontal();
                        childTop = this.getPaddingTop();
                        secondColumn = true;
                    }else if(secondColumn){
                        childStart = deque.pollFirst()+ getChipSpacingHorizontal();
                    }
                    int childEnd = childStart + startMargin + child.getMeasuredWidth() + endMargin;
                    childBottom = childTop + child.getMeasuredHeight();

                    child.layout(childStart + startMargin, childTop, childEnd, childBottom);

                    childStart = paddingStart;
                    childTop = childBottom + getChipSpacingVertical();
                    deque.addLast(childEnd);
                    childCountCurrentColumn++;
                }
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        deque.clear();
        int width = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int maxWidth = widthMode != View.MeasureSpec.AT_MOST && widthMode != View.MeasureSpec.EXACTLY ? Integer.MAX_VALUE : width;
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();
        int childBottom = childTop;
        int maxChildRight = 0;
        int maxRight = maxWidth - this.getPaddingRight();

        boolean secondColumn = false;
        int finalWidth;
        int count =0;
        int childHeight=0;
        for(finalWidth = 0; finalWidth < this.getChildCount(); ++finalWidth) {
            View child = this.getChildAt(finalWidth);
            if (child.getVisibility() != GONE) {
                childHeight = child.getMeasuredHeight();
                this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
                ViewGroup.LayoutParams lp = child.getLayoutParams();
                int leftMargin = 0;
                int rightMargin = 0;
                if (lp instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLp = (ViewGroup.MarginLayoutParams)lp;
                    leftMargin += marginLp.leftMargin;
                    rightMargin += marginLp.rightMargin;
                }

                if (count >= rows ) {
                    count =0;
                    childLeft = deque.pollFirst()+ getChipSpacingHorizontal();
                    childTop = this.getPaddingTop();
                    secondColumn=true;
                }else if(secondColumn){
                    childLeft = deque.pollFirst()+ getChipSpacingHorizontal();
                }
                int childRight = childLeft + leftMargin + child.getMeasuredWidth() + rightMargin;
                if(childRight > maxChildRight){
                    maxChildRight = childRight;
                }
                childBottom = childTop + child.getMeasuredHeight();

                childLeft += this.getPaddingLeft();
                childTop = childBottom + getChipSpacingVertical();
                deque.addLast(childRight);
                count++;
            }
        }

        childHeight+= getChipSpacingVertical();
        this.setMeasuredDimension(maxChildRight, (rows*childHeight) - getChipSpacingVertical());
    }

}

