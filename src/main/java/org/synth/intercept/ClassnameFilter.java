package org.synth.intercept;

/**
 * An classname filter allows delegating per-class decisions to the filter.
 */
public interface ClassnameFilter
{
    /**
     * Determine whether the supplied classname matches the filter criteria.
     *
     * @param classname The classname, in internal JVM format (e.g. java/lang/String)
     * @return Whether the classname matches the filter.
     */
    boolean matches(String classname);
}