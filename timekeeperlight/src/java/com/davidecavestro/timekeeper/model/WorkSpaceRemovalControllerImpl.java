package com.davidecavestro.timekeeper.model;

import com.davidecavestro.timekeeper.ApplicationContext;

/**
 * Determina se un progetto è eliminabile.
 * 
 * @author davide
 */
public class WorkSpaceRemovalControllerImpl implements WorkSpaceRemovalController {
    private ApplicationContext context;

    public boolean isRemoveable (final WorkSpace workspace) {
        if (context==null) {
            throw new NullPointerException ("Context not initialised yet");
        }
        return context.getModel ().getWorkSpace ().equals (workspace);
//        return true;
    }

    /**
     * @return the context
     */
    public ApplicationContext getContext () {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext (final ApplicationContext context) {
        this.context = context;
    }

}
