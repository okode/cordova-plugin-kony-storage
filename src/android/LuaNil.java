package com.konylabs.vm;

import android.content.Context;

import java.io.Serializable;

public class LuaNil implements Serializable {
    private static String a = "nil";
    public static LuaNil nil = new LuaNil(null);

    private static Context context;

    public LuaNil(Context c) {
        this.context = c;
    }

    public String toString() {
        return a;
    }

    public int hashCode() {
        return "nil".hashCode();
    }

    public boolean equals(Object var1) {
        return var1 == nil;
    }

    static {
        if(context != null) {
            a = "null";
        }

    }
}
