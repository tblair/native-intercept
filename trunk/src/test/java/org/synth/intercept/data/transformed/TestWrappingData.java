package org.synth.intercept.data.transformed;

import java.io.IOException;
import java.util.EmptyStackException;

import org.synth.intercept.HasNatives;
import org.synth.intercept.WasNative;
import org.synth.intercept.data.TestInterceptingData;

/**
 * The Java source transformed version of {@link org.synth.intercept.data.TestWrappingData} after
 * the wrapping transformation. Used mostly for bytecode comparisons with generated versions when
 * debugging.
 */
@HasNatives
public class TestWrappingData
{
    @WasNative
    public void instanceVoid()
    {
        this.wrapped_$$_instanceVoid();
    }
    public native void wrapped_$$_instanceVoid();

    @WasNative
    public static void staticVoid()
    {
        TestInterceptingData.wrapped_$$_staticVoid();
    }
    public static native void wrapped_$$_staticVoid();

    @WasNative
    public Object instanceObject()
    {
        return this.wrapped_$$_instanceObject();
    }
    public native Object wrapped_$$_instanceObject();

    @WasNative
    public static Object staticObject()
    {
        return TestInterceptingData.wrapped_$$_staticObject();
    }
    public static native Object wrapped_$$_staticObject();

    @WasNative
    public int[] instanceIntArray(final Object a, final float[] b, final String c) throws IOException, EmptyStackException
    {
        return this.wrapped_$$_instanceIntArray(a, b, c);
    }
    public native int[] wrapped_$$_instanceIntArray(Object a, float[] b, String c);

    @WasNative
    public static int[] staticIntArray(final Object a, final float[] b, final String c) throws IOException, EmptyStackException
    {
        return TestInterceptingData.staticIntArray(a, b, c);
    }
    public static native int[] wrapped_$$_staticIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;
}