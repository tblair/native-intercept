package org.synth.intercept.data.transformed;

import java.io.IOException;
import java.util.EmptyStackException;

import org.synth.intercept.HasInterceptedNatives;
import org.synth.intercept.HasNatives;
import org.synth.intercept.Intercepted;
import org.synth.intercept.NativeInvocationHandler;
import org.synth.intercept.WasNative;

/**
 * The Java source transformed version of {@link org.synth.intercept.data.TestInterceptingData}
 * after the intercepting transformation. Used mostly for bytecode comparisons with generated
 * versions when debugging.
 */
@HasNatives
@HasInterceptedNatives
public class TestInterceptingData
{
    @WasNative
    @Intercepted
    public void instanceVoid()
    {
        try
        {
            NativeInvocationHandler.handleVoid(this, "instanceVoid", new Class<?>[] {}, new Object[] {});
        }
        catch (final Throwable t)
        {
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public native void wrapped_$$_instanceVoid();

    @WasNative
    @Intercepted
    public static void staticVoid()
    {
        try
        {
            NativeInvocationHandler.handleStaticVoid(TestInterceptingData.class, "staticVoid", new Class<?>[] {}, new Object[] {});
        }
        catch (final Throwable t)
        {
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public static native void wrapped_$$_staticVoid();

    @WasNative
    @Intercepted
    public Object instanceObject()
    {
        try
        {
            return NativeInvocationHandler.handleObject(this, Object.class, "instanceObject", new Class<?>[] {}, new Object[] {});
        }
        catch (final Throwable t)
        {
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public native Object wrapped_$$_instanceObject();

    @WasNative
    @Intercepted
    public static Object staticObject()
    {
        try
        {
            return NativeInvocationHandler.handleStaticObject(TestInterceptingData.class, Object.class, "staticObject", new Class<?>[] {}, new Object[] {});
        }
        catch (final Throwable t)
        {
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public static native Object wrapped_$$_staticObject();

    @WasNative
    @Intercepted
    public int[] instanceIntArray(final Object a, final float[] b, final String c) throws IOException, EmptyStackException
    {
        try
        {
            return NativeInvocationHandler.handleObject(this, int[].class, "instanceIntArray", new Class[] { Object.class, float[].class, String.class }, new Object[] { a, b, c });
        }
        catch (final Throwable t)
        {
            if (t instanceof IOException)
                throw (IOException)t;
            if (t instanceof EmptyStackException)
                throw (EmptyStackException)t;
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public native int[] wrapped_$$_instanceIntArray(Object a, float[] b, String c);

    @WasNative
    @Intercepted
    public static int[] staticIntArray(final Object a, final float[] b, final String c) throws IOException, EmptyStackException
    {
        try
        {
            return NativeInvocationHandler.handleStaticObject(TestInterceptingData.class, int[].class, "instanceIntArray", new Class[] { Object.class, float[].class, String.class }, new Object[] { a, b, c });
        }
        catch (final Throwable t)
        {
            if (t instanceof IOException)
                throw (IOException)t;
            if (t instanceof EmptyStackException)
                throw (EmptyStackException)t;
            if (t instanceof RuntimeException)
                throw (RuntimeException)t;
            throw new IllegalStateException("Unexpected exception thrown in interceptor", t);
        }
    }
    public static native int[] wrapped_$$_staticIntArray(Object a, float[] b, String c) throws IOException, EmptyStackException;
}
