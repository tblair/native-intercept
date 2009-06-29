package org.synth.intercept;

import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationHandler;


/**
 * The main interface to the Native Interceptor library.
 */
public class NativeInterceptor
{
    /**
     * Indicate that a class should have its native methods intercepted and delegated to the
     * supplied invocation handler.
     *
     * Note: The {@link InvocationHandler} interface is intended for use in Java dynamic proxies
     *       and is not designed to handle static method invocations. However some of the native
     *       methods intercepted may be static. In those cases, the first argument of type Object
     *       will be passed the class that declares the static method.
     *
     * @param type The type whose native methods should be intercepted.
     * @param handler The handler to delegate the intercepted methods to.
     * @throws IllegalArgumentException When the type is null.
     * @throws IllegalStateException When the class has not previously been transformed to
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