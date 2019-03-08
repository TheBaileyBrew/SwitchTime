package com.thebaileybrew.switchtime.objects;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.TextViewCompat;

public class SwitchTextView extends TextView {
    public SwitchTextView(Context context) {
        super(context);
        setFont();
    }

    public SwitchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/couture_bld.otf");
        setTypeface(font, Typeface.BOLD);
    }
}
