package org.synth.intercept.data;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.util.EmptyStackException;

import org.synth.intercept.TestInstrumentation;

/**
 * Compiled version of this class serves as input data for {@link TestInstrumentation} when testing
 * the native wrapping transformation.
 */
public class TestWrappingData
{
    /**
     * A simple void instance method. Tests that instrumentation works correctly when not
     * returning a value in the instance context.
     */
    public native void instanceVoid();

    /**
     * A simple void static method. Tests that instrumentation works correctly when not
     * returning a value in the static context.
     */
    public static native void staticVoid();

    /**
     * A simple Object instance method. Tests that instrumentation works correctly when returning
     * a value in the instance context.
     *
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     */
    public native Object instanceObject();

    /**
     * A simple Object static method. Tests that instrumentation works correctly when returning
     * a value in the static context.
     *
     * @return Whatever is returned by the {@link InvocationHandler} registered to this class.
     */
    public static native Object staticObject();

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
    public native int[] instanceIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;

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
    public static native int[] staticIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;
}