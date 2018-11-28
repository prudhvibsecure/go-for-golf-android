package com.sharmas.golf_android.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.sharmas.golf_android.R;


public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    private int fontType = -1;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomTextView);

        fontType = a.getInteger(R.styleable.CustomTextView_fontType, -1);
        a.recycle();

        switch (fontType) {

            case 3:
                this.setTypeface(AppFonts.getInstance(context).getOpificioBoldWebfont());
                break;

            case 4:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont());
                break;

            case 5:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont(),
                        Typeface.BOLD);
                break;

            case 6:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont(),
                        Typeface.BOLD_ITALIC);
                break;

            case 7:
                this.setTypeface(AppFonts.getInstance(context).getFutureStdBookFont(),
                        Typeface.ITALIC);
                break;

            default:
                break;
        }

    }

}
