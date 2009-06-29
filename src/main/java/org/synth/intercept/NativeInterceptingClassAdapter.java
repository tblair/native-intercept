package org.synth.intercept;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * Class adapter to transform a class so that methods that were originally native are intercepted
 * and passed back to the {@link NativeInvocationHandler}. This class can only transform classes
 * that were previously transformed to wrap native methods.
 *
 * This class does not handle the majority of the bytecode generation. It delegates that task
 * to a {@link MethodAdapter} it creates for each method found in the class.
 *
 * Note: This class is designed to operate on a single class. Behavior when re-using instances of
 *       this class is undefined. It's advisable to create a new instance of this class for each
 *       class that needs to be transformed.
 *
 * @see NativeWrappingClassAdapter
 */
public class NativeInterceptingClassAdapter extends ClassAdapter
{
    /**
     * The type of the class being transformed.
     */
    private final Type type;

    /**
     * A boolean to indicate whether the class needs to be transformed. This allows the
     * {@link NativeInterceptingTransformer} to return null to indicate that no transformation
     * took place.
     */
    private boolean intercepted = false;

    /**
     * Create a new class adapter.
     *
     * @param cv The underlying visitor to adapt.
     * @param type The type of the class being transformed.
     */
    public NativeInterceptingClassAdapter(final ClassVisitor cv, final Type type)
    {
        super(cv);
        this.type = type;
    }

    /**
     * The main visit for the type. This simply forwards the call through and adds the
     * {@link HasInterceptedNatives} marker annotation.
     */
    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces)
    {
        super.visit(version, access, name, signature, superName, interfaces);
        this.cv.visitAnnotation(Constants.HAS_INTERCEPTED_NATIVES_DESCRIPTOR, true);
    }

    /**
     * Wrapper around the method visiting to add the custom adapter layer.
     */
    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions)
    {
        return new NativeInterceptingMethodAdapter(super.visitMethod(access, name, desc, signature, exceptions),
                                                   this, this.type, access, name, desc, exceptions);
    }

    /**
     * Getter for the state that indicates whether the visitor found any interceptable methods.
     *
     * @return Whether any interceptable methods were found and intercepting code was generated.
     */
    public boolean intercepted()
    {
        return this.intercepted;
    }

    /**
     * Setter for the intercepted state returned by {@link #intercepted()}. This method is intended
     * solely for the {@link MethodAdapter} that generates the intercepting code as a means of
     * recording that it did so.
     */
    void setIntercepted()
    {
        this.intercepted = true;
    }
}