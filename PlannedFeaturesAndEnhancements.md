## Planned features and enhancements ##

  * The ability to traverse class references to intercept native methods that may be called indirectly. For instance, if A.nonNativeMethod() calls B.nativeMethod(), a call to intercept methods in A should give the option of intercepting native methods in B.
  * Support for remapping transformed native method names when using Java 1.5 via a native method that calls RegisterNatives with the new method name.
  * The ability to intercept specific native methods rather than all methods in a class.


---

Please use the comments in this page to suggest other desired functionality.