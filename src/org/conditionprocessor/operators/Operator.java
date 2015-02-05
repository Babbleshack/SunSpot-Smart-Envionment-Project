/*
 * Defines a 'Command' style operator class.
 */
package org.conditionprocessor.operators;

/**
 *
 * @author babbleshack
 */
public interface Operator {
    public boolean operate(boolean r1, boolean r2);
}