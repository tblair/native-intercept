package org.synth.intercept.integration;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.synth.intercept.ClassnameFilter;
import org.synth.intercept.NativeInterceptor;
import org.synth.intercept.NativeInterceptorAgent;
import org.synth.intercept.data.TestWrappingData;

public class InterceptorTest extends TestCase
{
    private static final InvocationResult RESULT = new InvocationResult();
    static
    {
        long start = System.nanoTime();
        NativeInterceptorAgent.enable(new ClassnameFilter() {
            public boolean matches(String classname)
            {
                return classname.indexOf("org/apache/tools/ant") != -1 ||
                       classname.indexOf("junit/framework") != -1 ||
                       classname.indexOf("org/junit") != -1;
            }
        });
        System.out.println("took: " + ((System.nanoTime() - start) / 1000000) + " ms");
        NativeInterceptor.intercept(TestWrappingData.class, RESULT, false);
    }

    @Override
    public void setUp() throws Exception
    {
        RESULT.reset();
    }

    public void testInterceptingStaticVoid() throws Exception
    {
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData.staticVoid();
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
    }

    public void testInterceptingInstanceVoid() throws Exception
    {
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        data.instanceVoid();
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
    }

    public void testInterceptingStaticObject() throws Exception
    {
        Object returnValue = new Object();
        RESULT.setReturnValue(returnValue);
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        Object returned = TestWrappingData.staticObject();
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Object returned should be the one from the proxy", returnValue == returned);
    }

    public void testInterceptingInstanceObject() throws Exception
    {
        Object returnValue = new Object();
        RESULT.setReturnValue(returnValue);
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        Object returned = data.instanceObject();
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Object returned should be the one from the proxy", returnValue == returned);
    }

    public void testInterceptingStaticIntArray() throws Exception
    {
        int[] returnValue = new int[] { 1, 1, 2, 3, 5, 8, 13, 21 };
        RESULT.setReturnValue(returnValue);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        int[] returned = TestWrappingData.staticIntArray(a, b, c);
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
        assertTrue("Object returned should be the one from the proxy", returnValue == returned);
    }

    public void testInterceptingInstanceIntArray() throws Exception
    {
        int[] returnValue = new int[] { 1, 1, 2, 3, 5, 8, 13, 21 };
        RESULT.setReturnValue(returnValue);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        int[] returned = data.instanceIntArray(a, b, c);
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
        assertTrue("Object returned should be the one from the proxy", returnValue == returned);
    }

    public void testInterceptingStaticIntArrayThrowingDeclaredException() throws Exception
    {
        IOException toThrow = new IOException("declared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        try
        {
            TestWrappingData.staticIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IOException e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingInstanceIntArrayThrowingDeclaredException() throws Exception
    {
        IOException toThrow = new IOException("declared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        try
        {
            data.instanceIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IOException e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingStaticIntArrayThrowingRuntimeException() throws Exception
    {
        RuntimeException toThrow = new UnsupportedOperationException("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        try
        {
            TestWrappingData.staticIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (UnsupportedOperationException e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingInstanceIntArrayThrowingRuntimeException() throws Exception
    {
        RuntimeException toThrow = new UnsupportedOperationException("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        try
        {
            data.instanceIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (UnsupportedOperationException e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingStaticIntArrayThrowingError() throws Exception
    {
        Error toThrow = new IllegalAccessError("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        try
        {
            TestWrappingData.staticIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IllegalAccessError e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingInstanceIntArrayThrowingError() throws Exception
    {
        Error toThrow = new IllegalAccessError("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        try
        {
            data.instanceIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IllegalAccessError e)
        {
            assertTrue("Correct exception should have been thrown", e == toThrow);
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingStaticIntArrayThrowingIllegalException() throws Exception
    {
        InterruptedException toThrow = new InterruptedException("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        try
        {
            TestWrappingData.staticIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IllegalStateException e)
        {
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("When intercepting static methods, the proxy object should be the declaring class",
                   TestWrappingData.class == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    public void testInterceptingInstanceIntArrayThrowingIllegalException() throws Exception
    {
        InterruptedException toThrow = new InterruptedException("undeclared");
        RESULT.setToThrow(toThrow);
        Object a = new Object();
        float[] b = new float[] { 1.0f, 2.2f, 3.9f };
        String c = "TestString";
        assertFalse("Invocation method should not be called prior to invocation", RESULT.isInvoked());
        TestWrappingData data = new TestWrappingData();
        try
        {
            data.instanceIntArray(a, b, c);
            fail("Exception should have been thrown");
        }
        catch (IllegalStateException e)
        {
        }
        catch (Throwable t)
        {
            fail("Incorrect exception thrown: " + t);
        }
        assertTrue("Invocation method should be called upon invocation", RESULT.isInvoked());
        assertTrue("Incorrect proxy object", data == RESULT.getProxy());
        assertTrue("Arguments passed to proxy should be those passed to method invocation", a == RESULT.getArgs()[0]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", b == RESULT.getArgs()[1]);
        assertTrue("Arguments passed to proxy should be those passed to method invocation", c == RESULT.getArgs()[2]);
    }

    static class InvocationResult implements InvocationHandler
    {
        private boolean invoked = false;
        private Object proxy;
        private Method method;
        private Object[] args;
        private Object returnValue;
        private Throwable toThrow;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            this.invoked = true;
            this.proxy = proxy;
            this.method = method;
            this.args = args;
            if (this.toThrow != null)
                throw this.toThrow;
            return this.returnValue;
        }
        public void reset()
        {
            this.invoked = false;
            this.proxy = null;
            this.method = null;
            this.args = null;
            this.returnValue = null;
        }
        public boolean isInvoked()
        {
            return this.invoked;
        }
        public void setInvoked(boolean invoked)
        {
            this.invoked = invoked;
        }
        public Object getProxy()
        {
            return this.proxy;
        }
        public void setProxy(Object proxy)
        {
            this.proxy = proxy;
        }
        public Method getMethod()
        {
            return this.method;
        }
        public void setMethod(Method method)
        {
            this.method = method;
        }
        public Object[] getArgs()
        {
            return this.args;
        }
        public void setArgs(Object[] args)
        {
            this.args = args;
        }
        public Object getReturnValue()
        {
            return this.returnValue;
        }
        public void setReturnValue(Object returnValue)
        {
            this.returnValue = returnValue;
        }
        public Throwable getToThrow()
        {
            return this.toThrow;
        }
        public void setToThrow(Throwable toThrow)
        {
            this.toThrow = toThrow;
        }
    }
}