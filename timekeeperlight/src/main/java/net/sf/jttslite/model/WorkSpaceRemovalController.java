package net.sf.jttslite.model;

import net.sf.jttslite.core.model.WorkSpace;

/**
 * Controlla se un progeto è cancellabile.
 *
 * @author davide
 */
public interface WorkSpaceRemovalController {
    /**
     * Indica se il progetto specificato è rimovibile (cancellabile).
     * @return <tt>true</tt> se il progetto specificato è cancellabile.
     */
    boolean isRemoveable (WorkSpace workspace);
}
