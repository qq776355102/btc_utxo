package com.cmc.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) 
public @interface YunLianParamValidateAnnotation {
    public String getPNotNullNames();
    public String getDNotNullNames();
}
