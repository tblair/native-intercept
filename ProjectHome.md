Pure Java agent to intercept native method calls to handle their implementation in Java code rather than native code. This library works by applying bytecode transformations to classes at runtime.

This library requires Java 1.5 or later due to its reliance on the Java Agent functionality. However when using Java 1.5, native method resolution will not work correctly for unintercepted methods on instrumented classes. Java 1.6 is recommended due to added functionality in Java Agents that allows for native method resolution to work on instrumented classes.

The current version is alpha. There have been successful tests on Windows and OS X, but it has not been extensively tested yet.