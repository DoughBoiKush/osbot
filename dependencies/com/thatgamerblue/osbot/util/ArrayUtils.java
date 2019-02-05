package com.thatgamerblue.osbot.util;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

public class ArrayUtils {

    public static boolean isValidIndex(@NotNull ArrayList list, int index) {
        return !(index >= list.size());
    }

}
