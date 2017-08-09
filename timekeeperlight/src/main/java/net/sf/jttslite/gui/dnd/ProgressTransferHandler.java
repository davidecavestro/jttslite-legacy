/*
 * ProgressTransferHandler.java
 *
 * Created on 09 marzo 2005, 00.07
 */

package net.sf.jttslite.gui.dnd;

import net.sf.jttslite.ApplicationContext;
import net.sf.jttslite.core.model.PieceOfWork;
import com.ost.timekeeper.model.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.logging.Level;
import javax.swing.*;

/**
 * Gestore del trasferimento dei dati di tipo {@link com.ost.timekeeper.model.Progress}.
 *
 * @author  davide
 */
public abstract class ProgressTransferHandler extends TransferHandler implements PredictiveTransferhandler {
	
	protected final DataFlavor progressFlavor = DataFlavors.progressFlavor;
	
	private final ApplicationContext _context;
	/**
	 * Costruttore.
	 */
	public ProgressTransferHandler (final ApplicationContext context) {
		_context = context;
	}
	
	protected abstract PieceOfWork[] exportProgresses (JComponent c);
	protected abstract void importProgresses (JComponent c, PieceOfWork[] progress, boolean removeFromSource);
	protected abstract void cleanup (JComponent c, Transferable data, int action);
	
	/**
	 * Crea un oggetto trasferibile.
	 *
	 * @param c l'elemento della UI.
	 * @return un oggetto trasferibile.
	 */	
	@Override
	protected Transferable createTransferable (JComponent c) {
		return new ProgressSelection (exportProgresses (c));
	}
	
	@Override
	public int getSourceActions (JComponent c) {
		return COPY_OR_MOVE;
	}
	
	@Override
	public boolean importData (JComponent c, Transferable t) {
		if (!canImport (c, t.getTransferDataFlavors ())) {
			return false;
		}
		
		try {
			final TransferData<PieceOfWork> td = (TransferData<PieceOfWork>)t.getTransferData (progressFlavor);
			final PieceOfWork[] progresses = td.getData ();
			importProgresses (c, progresses, td.getAction ()!=TransferHandler.COPY);
		} catch (UnsupportedFlavorException ufe) {
			_context.getLogger ().log (Level.WARNING, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Error_transferring_UI_data."),ufe);
			return false;
		} catch (IOException ioe) {
			_context.getLogger ().log (Level.WARNING, java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Error_transferring_UI_data."),ioe);
			return false;
		}
		return true;
	}
	
	@Override
	protected void exportDone (JComponent c, Transferable data, int action) {
		((MemoTransferable)data).setAction (action);
		cleanup (c, data, action);
	}
	
    /**
     * Does the flavor list have a Progress flavor?
     */
    protected boolean hasProgressFlavor(DataFlavor[] flavors) {
        if (progressFlavor == null) {
             return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (progressFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }
	
	/**
     * Overridden to include a check for a Progress flavor.
     */
	@Override
    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        return hasProgressFlavor(flavors);
    }
	
}
