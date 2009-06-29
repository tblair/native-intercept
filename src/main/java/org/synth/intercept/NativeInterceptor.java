package org.synth.intercept;

import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationHandler;

/**
 * The main interface to the Native Interceptor library.
 */
public class NativeInterceptor
{
    static final Class<NativeInterceptorAgent> AGENT_CLASS = NativeInterceptorAgent.class;

    /**
     *
     * @param type
     * @param handler
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static void intercept(final Class<?> type, final InvocationHandler handler) throws IllegalArgumentException, IllegalStateException
    {
        if (type == null)
            throw new IllegalArgumentException("Cannot intercept native methods on null type");
        if (handler == null)
        {
            NativeInvocationHandler.unregisterHandler(type);
            return;
        }
        if (type.getAnnotation(HasNatives.class) == null)
            throw new IllegalStateException("Cannot instrument class to intercept native methods...see documentation for causes");

        try
        {
            NativeInterceptorAgent.getInstrumentation().retransformClasses(type);
        }
        catch (final UnmodifiableClassException e)
        {
            e.printStackTrace();
            throw new IllegalStateException("Unable to intercept native method...see documentation for details", e);
        }
        NativeInvocationHandler.registerHandler(type, handler);
    }
}