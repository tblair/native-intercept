package org.synth.intercept;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class NativeWrappingClassAdapter extends ClassAdapter
{
    public static final int NON_NATIVE_MASK = 0xFFFFFF ^ Opcodes.ACC_NATIVE;
    private final Type type;
    private boolean foundNativeMethod = false;

    public NativeWrappingClassAdapter(final ClassVisitor visitor, final Type type)
    {
        super(visitor);
        this.type = type;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces)
    {
        super.visit(version, access, name, signature, superName, interfaces);
        this.cv.visitAnnotation(Constants.HAS_NATIVES_DESCRIPTOR, true);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions)
    {
        // TODO: copy annotations from native method to wrapper method
        if ((access & Opcodes.ACC_NATIVE) == 0)
            return super.visitMethod(access, name, desc, signature, exceptions);
        this.foundNativeMethod = true;
        final MethodVisitor wrapper = super.visitMethod(access & NativeWrappingClassAdapter.NON_NATIVE_MASK, name, desc, signature, exceptions);
        new NativeWrappingMethodAdapter(wrapper, this.type, access, name, desc);
        return super.visitMethod(access, Constants.NATIVE_METHOD_PREFIX + name, desc, signature, exceptions);
    }

    public boolean foundNativeMethod()
    {
        return this.foundNativeMethod;
    }
}