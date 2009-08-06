package org.synth.intercept;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class NativeInvocationHandler
{
    private static final Object[] EMPTY_ARGS = new Object[] {};
    private static final Map<Class<?>,InvocationHandler> HANDLERS = new HashMap<Class<?>,InvocationHandler>();

    public static void registerHandler(final Class<?> type, final InvocationHandler handler)
    {
        if (type == null)
            return;
        NativeInvocationHandler.HANDLERS.put(type, handler);
    }

    public static void unregisterHandler(final Class<?> type)
    {
        if (type == null)
            return;
        NativeInvocationHandler.HANDLERS.remove(type);
    }

    public static <T> T handleObject(final Object instance, final Class<T> returnType, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        if (instance == null)
            throw new IllegalArgumentException("Invalid object instance (null) passed to invocation handler");
        if (returnType == null)
            throw new IllegalArgumentException("Invalid return type (null) passed to invocation handler");
        if (name == null)
            throw new IllegalArgumentException("Invalid method name (null) passed to invocation handler");
        final Method method =
            NativeInvocationHandler.findMethod(instance.getClass(), name, argTypes);
        final InvocationHandler handler = NativeInvocationHandler.HANDLERS.get(instance.getClass());
        if (handler == null)
            throw new UnsatisfiedLinkError(method.toString());
        return NativeInvocationHandler.typeCheck(returnType, handler.invoke(instance, method, args == null ? NativeInvocationHandler.EMPTY_ARGS : args));
    }

    public static <T> T handleStaticObject(final Class<?> type, final Class<T> returnType, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        if (type == null)
            throw new IllegalArgumentException("Invalid class reference (null) passed to invocation handler");
        if (returnType == null)
            throw new IllegalArgumentException("Invalid return type (null) passed to invocation handler");
        if (name == null)
            throw new IllegalArgumentException("Invalid method name (null) passed to invocation handler");
        final Method method = NativeInvocationHandler.findMethod(type, name, argTypes);
        final InvocationHandler handler = NativeInvocationHandler.HANDLERS.get(type);
        if (handler == null)
            throw new UnsatisfiedLinkError(method.toString());
        return NativeInvocationHandler.typeCheck(returnType, handler.invoke(null, method, args == null ? NativeInvocationHandler.EMPTY_ARGS : args));
    }

    public static boolean handleBoolean(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, boolean.class, name, argTypes, args);
    }

    public static boolean handleStaticBoolean(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, boolean.class, name, argTypes, args);
    }

    public static byte handleByte(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, byte.class, name, argTypes, args);
    }

    public static byte handleStaticByte(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, byte.class, name, argTypes, args);
    }

    public static char handleChar(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, char.class, name, argTypes, args);
    }

    public static char handleStaticChar(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, char.class, name, argTypes, args);
    }

    public static double handleDouble(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, double.class, name, argTypes, args);
    }

    public static double handleStaticDouble(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, double.class, name, argTypes, args);
    }

    public static float handleFloat(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, float.class, name, argTypes, args);
    }

    public static float handleStaticFloat(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, float.class, name, argTypes, args);
    }

    public static int handleInt(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, int.class, name, argTypes, args);
    }

    public static int handleStaticInt(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, int.class, name, argTypes, args);
    }

    public static long handleLong(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, long.class, name, argTypes, args);
    }

    public static long handleStaticLong(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, long.class, name, argTypes, args);
    }

    public static short handleShort(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleObject(instance, short.class, name, argTypes, args);
    }

    public static short handleStaticShort(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        return NativeInvocationHandler.handleStaticObject(type, short.class, name, argTypes, args);
    }

    public static void handleVoid(final Object instance, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        NativeInvocationHandler.handleObject(instance, void.class, name, argTypes, args);
    }

    public static void handleStaticVoid(final Class<?> type, final String name, final Class<?>[] argTypes, final Object[] args) throws Throwable
    {
        NativeInvocationHandler.handleStaticObject(type, void.class, name, argTypes, args);
    }

    @SuppressWarnings("unchecked")
    private static final <T> T typeCheck(final Class<T> type, final Object o)
    {
        if (type == null)
            throw new IllegalArgumentException("Cannot type check null type");
        if (o == null && (!type.isPrimitive() || type == void.class))
            return null;
        if (o == null)
            throw new IllegalStateException(
                "Attempt to return null when intercepting " + type + " method");
        Class<?> actual = type;
        if (type.isPrimitive())
        {
            if (type == boolean.class)
                actual = Boolean.class;
            if (type == byte.class)
                actual = Byte.class;
            if (type == char.class)
                actual = Character.class;
            if (type == double.class)
                actual = Double.class;
            if (type == float.class)
                actual = Float.class;
            if (type == int.class)
                actual = Integer.class;
            if (type == long.class)
                actual = Long.class;
            if (type == short.class)
                actual = Short.class;
        }
        if (!actual.isInstance(o))
            throw new IllegalStateException("Attempt to return type " + o.getClass() +
                                            " when intercepting " + type + " method");
        return (T)o;
    }

    private static final Method findMethod(final Class<?> type, final String name, final Class<?>[] argTypes)
    {
        Class<?> search = type;
        IllegalStateException ex = null;
        while (search != null && !NativeInterceptorAgent.isExcluded(search.getName()))
        {
            try
            {
                if (search.getAnnotation(HasInterceptedNatives.class) != null)
                    return search.getDeclaredMethod(name, argTypes);
            }
            catch (final SecurityException e)
            {
                if (ex == null)
                    ex = new IllegalStateException("Security exception thrown while determining proxied native method", e);
            }
            catch (final NoSuchMethodException e)
            {
                if (ex == null)
                    ex = new IllegalStateException("Attempt to intercept non-existent native method", e);
            }
            search = search.getSuperclass();
        }
        throw ex == null ? new IllegalStateException("Unable to determine native method") : ex;
    }
}