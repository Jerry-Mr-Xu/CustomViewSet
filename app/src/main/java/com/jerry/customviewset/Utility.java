package com.jerry.customviewset;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

public class Utility {
    public static int dp2Px(@NonNull Context context, float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());
    }
}
