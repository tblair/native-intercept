package org.synth.intercept;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * The java agent that attaches the necessary {@link ClassFileTransformer ClassFileTransformers}
 * to instrument classes with native methods.
 */
public class NativeInterceptorAgent
{
    /**
     * Log for various logging events.
     */
    private static final Logger LOG =
        Logger.getLogger(NativeInterceptorAgent.class.getPackage().getName());

    /**
     * A saved static reference to the {@link Instrumentation} instance.
     */
    private static Instrumentation INSTRUMENTATION;

    /**
     * prefixes for classes that should not be instrumented.
     */
    private static final Set<String> EXCLUDED_PREFIXES = new HashSet<String>(Arrays.asList(
        "java", "javax", "com/sun", "sun", "org/w3c", "org/xml"
    ));

    /**
     * A cache of the the Java version. Since
     * {@link Instrumentation#setNativeMethodPrefix(ClassFileTransformer,String)} is only available
     * in Java 1.6 or later, Java 1.5 requires a work around.
     */
    public static int JAVA_VERSION = System.getProperty("java.version").charAt(2) - '0';

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

    /**
     * The method that fulfills the agent contract for attaching with the -javaagent VM argument.
     *
     * @param agentArgs The agent arguments (unused).
     * @param instrumentation The instrumentation reference.
     */
    public static void premain(final String agentArgs, final Instrumentation instrumentation)
    {
        NativeInterceptorAgent.LOG.fine("Loading agent premain");
        NativeInterceptorAgent.init(instrumentation);
    }

    /**
     * The method that filfills the agent contract for attaching to a running VM.
     *
     * @param agentArgs The agent arguments (unused).
     * @param instrumentation The instrumentation reference.
     */
    public static void agentmain(final String agentArgs, final Instrumentation instrumentation)
    {
        NativeInterceptorAgent.LOG.fine("Loading agent agentmain");
        NativeInterceptorAgent.init(instrumentation);
    }

    /**
     * Shared init method for {@link #premain(String,Instrumentation)} and
     * {@link #agentmain(String,Instrumentation)}.
     *
     * @param instrumentation The instrumentation reference.
     */
    private static void init(final Instrumentation instrumentation)
    {
        // save the instrumentation reference.
        NativeInterceptorAgent.INSTRUMENTATION = instrumentation;
        // create the class file transformers
        final NativeWrappingTransformer wrapper = new NativeWrappingTransformer();
        final NativeInterceptingTransformer interceptor = new NativeInterceptingTransformer();
        // add them to the instrumentation.
        // note: the wrapper transformer must be added before setNativeMethodPrefix is called.
        instrumentation.addTransformer(wrapper, false);
        instrumentation.addTransformer(interceptor, true);
        // only call setNativeMethodPrefix when the JVM version is 6 or later.
        if (NativeInterceptorAgent.JAVA_VERSION >= 6)
        {
            // tell the jvm what the prefix for wrapped native methods is.
            instrumentation.setNativeMethodPrefix(wrapper, Constants.NATIVE_METHOD_PREFIX);
        }
    }

    /**
     * Called to register the prefixed method to the original function pointer from the native code.
     *
     * @param symbol The symbol in the native code representing the function pointer to assign to
     *               the indicated method.
     * @param type The type of the class defining the native method.
     * @param name The name of the native method.
     * @param signature The signature of the native method.
     */
    public static native void registerNativePrefix(String symbol, Class<?> type, String name, String signature);

    /**
     * A method to determine whether a className is part of an excluded package.
     *
     * @param className The class name, in internal for (e.g. java/lang/Throwable).
     * @return Whether the class should not be transformed.
     */
    static boolean isExcluded(final String className)
    {
        if (className == null)
            return true;
        int idx = className.indexOf('/');
        if (idx == -1)
            return NativeInterceptorAgent.EXCLUDED_PREFIXES.contains(className);
        if (NativeInterceptorAgent.EXCLUDED_PREFIXES.contains(className.substring(0, idx)))
            return true;
        idx = className.indexOf('/', idx + 1);
        return idx == -1 ? false : NativeInterceptorAgent.EXCLUDED_PREFIXES.contains(className.substring(0, idx));
    }

    public static Instrumentation getInstrumentation()
    {
        return NativeInterceptorAgent.INSTRUMENTATION;
    }

    /**
     * This method will attempt to attach the agent to a running VM.
     */
    public static void enable()
    {
        // Technically, each VM could have a different separator. This might have to change,
        // but until it becomes necessary, this is convenient.
        final String separator = System.getProperty("path.separator");
        try
        {
            for (final VirtualMachineDescriptor vmd : VirtualMachine.list())
            {
                final VirtualMachine vm = VirtualMachine.attach(vmd);
                final String classPath = (String)vm.getSystemProperties().get("java.class.path");
                for (final String classpathElement : classPath.split(separator))
                {
                    if (classpathElement != null && classpathElement.indexOf("native-interceptor") != -1)
                    {
                        vm.loadAgent(classpathElement);
                        break;
                    }
                }
            }
        }
        // TODO: better error handling...
        catch (final IOException e)
        {
            throw new IllegalStateException("Error while attaching agent to running VM", e);
        }
        catch (final AttachNotSupportedException e)
        {
            throw new IllegalStateException("Error while attaching agent to running VM", e);
        }
        catch (final AgentInitializationException e)
        {
            throw new IllegalStateException("Error while attaching agent to running VM", e);
        }
        catch (final AgentLoadException e)
        {
            throw new IllegalStateException("Error while attaching agent to running VM", e);
        }
    }
}