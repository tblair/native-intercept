package org.synth.intercept;

import java.lang.instrument.Instrumentation;

public class NativeInterceptorAgent
{
    private static Instrumentation INSTRUMENTATION;

    /**
     * By loading these classes with the agent, they won't pass through the class transformers.
     */
    @SuppressWarnings("unused")
    private static final Class<?>[] PRELOADED_CLASSES = new Class<?>[] {
        HasInterceptedNatives.class, HasNatives.class, WasNative.class,
        NativeInvocationHandler.class, NativeInterceptor.class,
        NativeWrappingTransformer.class, NativeInterceptingTransformer.class,
        NativeWrappingClassAdapter.class, NativeInterceptingClassAdapter.class,
        NativeWrappingMethodAdapter.class, NativeInterceptingMethodAdapter.class
    };

    public static void premain(final String agentArgs, final Instrumentation instrumentation)
    {
        System.out.println("Loading agent premain");
        NativeInterceptorAgent.init(instrumentation);
    }

    public static void agentmain(final String agentArgs, final Instrumentation instrumentation)
    {
        System.out.println("Loading agent agentmain");
        NativeInterceptorAgent.init(instrumentation);
    }

    private static void init(final Instrumentation instrumentation)
    {
        System.out.println("initializing: " + instrumentation);
        NativeInterceptorAgent.INSTRUMENTATION = instrumentation;
        final NativeWrappingTransformer transformer = new NativeWrappingTransformer();
        // Add the intercepting transformer first to prevent initial intercepting
        instrumentation.addTransformer(new NativeInterceptingTransformer(), true);
        instrumentation.addTransformer(transformer, false);
        instrumentation.setNativeMethodPrefix(transformer, Constants.NATIVE_METHOD_PREFIX);
    }

    static boolean isExcluded(final String className)
    {
        return className == null || className.startsWith("java/") ||
               className.startsWith("com/sun") || className.startsWith("sun");
    }

    public static Instrumentation getInstrumentation()
    {
        return NativeInterceptorAgent.INSTRUMENTATION;
    }
}