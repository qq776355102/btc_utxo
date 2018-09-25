package com.cmc.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface YunLianMethodAnnotation {
    public String getMethodSimpCode();
    public String getMethodSimpDesc();
    public int getAppPerm(); 
}
