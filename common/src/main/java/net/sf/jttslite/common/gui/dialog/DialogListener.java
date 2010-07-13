/*
 * DialogListener.java
 *
 * Created on 11 dicembre 2005, 12.09
 */

package net.sf.jttslite.common.gui.dialog;

/**
 * Un listener per eventi di tipo DialogEvent.
 *
 * @author  davide
 */
public interface DialogListener<T> extends java.util.EventListener {
    /**
     * Questa notifica informa i listener di modifiche ad una dialog.
     */
    void dialogChanged (DialogEvent<T> e);
}
