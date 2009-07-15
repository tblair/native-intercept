package org.synth.intercept;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;

/**
 * A {@link ClassFileTransformer} implementation that will replace the code that forwards to the
 * prefixed native method with code that intercepts the invocation and calls back to Java.
 */
public class NativeInterceptingTransformer implements ClassFileTransformer
{
    /**
     * A log for communicating any errors generated while transforming the class. Since this method
     * runs during classloading, exceptions can be swallowed silently causing strange behavior at
     * runtime.
     */
    private static final Logger LOG =
        Logger.getLogger(NativeInterceptingTransformer.class.getPackage().getName());

    /**
     * The main transformer method which handles the application of the proper class adapter.
     */
    @Override
    public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
                            final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException
    {
        /*
         * Do a quick check to see if the transform can be applied. If classBeingRedefined is null,
         * that means that this is not a re-transformation. Since the intercepting transformation
         * requires the wrapping transformation, only classes being re-transformed will be
         * transformed by this class. Also, consult the excluded classes list to prevent
         * instrumentation of core system libraries.
         */
        if (classBeingRedefined == null ||
            classBeingRedefined.getAnnotation(HasInterceptedNatives.class) != null ||
            NativeInterceptorAgent.isExcluded(className))
            return null;
        try
        {
            // Create the reader that reads from the class bytes.
            final ClassReader reader = new ClassReader(classfileBuffer);
            // Create the writer that writes the transformed class. Use COMPUTE_MAXS to allow
            // the adapter to not keep track of the maximum stack size.
            final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            // Create the adapter between the reader and writer.
            final NativeInterceptingClassAdapter adapter =
                new NativeInterceptingClassAdapter(writer, Type.getType(classBeingRedefined));
            // Run the class through the adapter
            reader.accept(adapter, ClassReader.EXPAND_FRAMES);
            // Check to see if the transformation found any valid targets. Returning null here
            // signals that no changes were made.
            return adapter.intercepted() ? writer.toByteArray() : null;
        }
        catch (final Throwable t)
        {
            // throw an expected exception type
            if (t instanceof IllegalClassFormatException)
                throw (IllegalClassFormatException)t;
            // coerce the exception to a string to log.
            final StringWriter toString = new StringWriter();
            t.printStackTrace(new PrintWriter(toString));
            // log the error.
            NativeInterceptingTransformer.LOG.severe(
                "Error while transforming class for intercepting:\n" + toString);
            // runtime exceptions don't need to be wrapped, checked exceptions do
            throw t instanceof RuntimeException ?
                (RuntimeException)t : new RuntimeException("Error while transforming class for intercepting", t);
        }
    }
}