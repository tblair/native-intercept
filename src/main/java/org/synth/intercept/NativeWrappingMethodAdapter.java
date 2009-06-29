package org.synth.intercept;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

/**
 * A method adapter that prefixes the names of native methods in classes and creates forwarding
 * methods to those native methods.
 */
public class NativeWrappingMethodAdapter extends GeneratorAdapter
{
    private final boolean instance;
    private final Type type;
    private final String name;
    private final String desc;

    public NativeWrappingMethodAdapter(final MethodVisitor mv, final Type type, final int access, final String name, final String desc)
    {
        super(mv, access, name, desc);
        this.type = type;
        this.instance = (access & Opcodes.ACC_STATIC) == 0;
        this.name = name;
        this.desc = desc;
        this.visitCode();
    }

    @Override
    public void visitCode()
    {
        super.visitCode();
        // Annotate accordingly so that the synthetic method can be recognized as such.
        this.mv.visitAnnotation(Constants.WAS_NATIVE_DESCRIPTOR, true);
        // The reference to the prefixed native method.
        final Method m = new Method(Constants.NATIVE_METHOD_PREFIX + this.name, this.desc);
        // If instance, load this and invokeVirtual, otherwise invokeStatic.
        if (this.instance)
        {
            super.loadThis();
            super.loadArgs();
            super.invokeVirtual(this.type, m);
        }
        else
        {
            super.loadArgs();
            super.invokeStatic(this.type, m);
        }
        // Return
        this.returnValue();
        // Put stub here to allow ASM to compute max stack value (values ignored).
        this.visitMaxs(0, 0);
    }

    @Override
    public void visitLineNumber(final int line, final Label start)
    {
        // No-op the line numbers since the bytecode no longer corresponds to any source code.
    }
}