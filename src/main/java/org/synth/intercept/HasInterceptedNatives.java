package org.synth.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A marker annotation added during bytecode instrumentation to signal classes that contain native
 * methods that have been intercepted prior to calling through to native code.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HasInterceptedNatives
{
}