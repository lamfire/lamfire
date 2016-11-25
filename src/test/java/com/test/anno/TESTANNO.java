package com.test.anno;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.TYPE ,ElementType.METHOD})
public @interface TESTANNO {
	public abstract String path();
    public abstract boolean singleton() default true;
}
