package org.synth.intercept.data;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.util.EmptyStackException;

import org.synth.intercept.HasNatives;
import org.synth.intercept.InstrumentationTest;
import org.synth.intercept.WasNative;

/**
 * Compiled version of this class serves as input data for {@link InstrumentationTest} when testing
 * the intercepting transformation of wrapped native methods. This class is the source code
 * transformation that corresponds to the bytecode transformation applied during the native
 * wrapping process.
 */
@HasNatives
public class TestInterceptingData
{
    /**
     * A simple void instance method. Tests that instrumentation works correctly when not
     * returning a value in the instance context.
     */
    @WasNative
    public void instanceVoid()
    {
        this.wrapped_$$_instanceVoid();
    }
    public native void wrapped_$$_instanceVoid();

    /**
     * A simple void static method. Tests that instrumentation works correctly when not
     * returning a value in the static context.
     */
    @WasNative
    public static void staticVoid()
    {
        TestInterceptingData.wrapped_$$_staticVoid();
    }
    public static native void wrapped_$$_staticVoid();

    /**
     * A simple Object instance method. Tests that instrumentation works correctly when returning
     * a value in the instance context.
     *
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     */
    @WasNative
    public Object instanceObject()
    {
        return this.wrapped_$$_instanceObject();
    }
    public native Object wrapped_$$_instanceObject();

    /**
     * A simple Object static method. Tests that instrumentation works correctly when returning
     * a value in the static context.
     *
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     */
    @WasNative
    public static Object staticObject()
    {
        return TestInterceptingData.wrapped_$$_staticObject();
    }
    public static native Object wrapped_$$_staticObject();

    /**
     * A more complex instance method. Tests that instrumentation can return an array, handle
     * arguments and handle exceptions.
     *
     * @param a A parameter.
     * @param b A parameter.
     * @param c A parameter.
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     * @throws IOException Whenever thrown by the {@link InvocationHandler} registered to this class.
     * @throws EmptyStackException Whenever thrown by the {@link InvocationHandler} registered to this class.
     */
    @WasNative
    public int[] instanceIntArray(final Object a, final float[] b, final String c)  throws IOException, EmptyStackException
    {
        return this.wrapped_$$_instanceIntArray(a, b, c);
    }
    public native int[] wrapped_$$_instanceIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;

    /**
     * A more complex static method. Tests that instrumentation can return an array, handle
     * arguments and handle exceptions.
     *
     * @param a A parameter.
     * @param b A parameter.
     * @param c A parameter.
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     * @throws IOException Whenever thrown by the {@link InvocationHandler} registered to this class.
     * @throws EmptyStackException Whenever thrown by the {@link InvocationHandler} registered to this class.
     */
    @WasNative
    public static int[] staticIntArray(final Object a, final float[] b, final String c) throws IOException, EmptyStackException
    {
        return TestInterceptingData.staticIntArray(a, b, c);
    }
    public static native int[] wrapped_$$_staticIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;
}