package net.sf.jttslite.gui.dnd;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.Autoscroll;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 *
 * 
 * 
 * @author Andrew J. Armstrong,Stefano Gallina
 * @web.resource-ref
 *   name=http://www.javaworld.com/javaworld/javatips/jw-javatip114.html
 *   type=
 *   auth=Andrew J. Armstrong
 */

/**
 * Questa classe � da considerarsi un componente in quanto estende 
 * JXTreeTable aggiungengo il supporto della funzionalit� del Drag&Drop.
 * Tramite l'interfaccia IDragAndDrop � possibile gestire il comportamento
 * delle funzionalit� di Drag&Drop.
 * Si dovrebbe documentare meglio per facilitarne l'utilizzo.
 */

public class DDJXTreeTable extends JXTreeTable implements DragSourceListener, 
														  DragGestureListener, 
														  Autoscroll,
														  TreeModelListener {

	// Fields...
	private TreePath		_pathSource;				// The path being dragged
	private BufferedImage	_imgGhost;					// The 'drag image' 
	private Point			_ptOffset = new Point();	// Where, in the drag image, the mouse was clicked
	private IDragAndDrop iDragAndDrop;
	
	
	public DDJXTreeTable() {
		super();
	}
	
	 public DDJXTreeTable(IDragAndDrop iDragAndDrop) {
		 super();
		 this.iDragAndDrop = iDragAndDrop;
		 initComponent();
	 }
	
	 public DDJXTreeTable(TreeTableModel treeModel,IDragAndDrop iDragAndDrop) {
		 super(treeModel);
		 this.iDragAndDrop = iDragAndDrop;
		 initComponent();
	 }
	 
	 /**
	  * Inizializza le caratteristiche del componente e la gestione del Drag and Drop
	  */
	 private void initComponent() {
		 
		 if ( iDragAndDrop != null  ) {
			 
			 // Make this JTree a drag source
			 DragSource dragSource = DragSource.getDefaultDragSource();
			 dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	
			 // Also, make this JTree a drag target
			 DropTarget dropTarget = new DropTarget(this, new CDropTargetListener(this));
			 dropTarget.setDefaultActions(DnDConstants.ACTION_COPY_OR_MOVE);
		 }	
		  
	 }
	 

	// Interface: DragGestureListener
		public void dragGestureRecognized(DragGestureEvent e)
		{
			
			Point ptDragOrigin = e.getDragOrigin();
			TreePath path = getPathForLocation(ptDragOrigin.x, ptDragOrigin.y);
			if (path == null)
				return;
			if (isRootPath(path))
				return;	// Ignore user trying to drag the root node

			int row = this.getRowForPath(this.getPathForLocation(ptDragOrigin.x, ptDragOrigin.y));
		    TreePath pathTemp = this.getPathForRow(row);			
		    
			// Work out the offset of the drag point from the TreePath bounding rectangle origin
			Rectangle raPath =  this.getCellRect(row, 0, false);
			
			_ptOffset.setLocation(ptDragOrigin.x-raPath.x, ptDragOrigin.y-raPath.y);
				
			// Get the cell renderer (which is a JLabel) for the path being dragged
			JLabel lbl = (JLabel)getTreeCellRenderer().getTreeCellRendererComponent
									(
										new JTree(), 									// tree
										path.getLastPathComponent(),					// value
										true,											// isSelected	(dont want a colored background)
										isExpanded(path), 								// isExpanded
										getTreeTableModel().isLeaf(path.getLastPathComponent()), // isLeaf
										0, 												// row			(not important for rendering)
										true											// hasFocus		(dont want a focus rectangle)
									);
			
			
			lbl.setSize((int)raPath.getWidth(), (int)raPath.getHeight()); // <-- The layout manager would normally do this

			// Get a buffered image of the selection for dragging a ghost image
			_imgGhost = new BufferedImage((int)raPath.getWidth(), (int)raPath.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
			Graphics2D g2 = _imgGhost.createGraphics();

			// Ask the cell renderer to paint itself into the BufferedImage
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.5f));		// Make the image ghostlike
			lbl.paint(g2);

			// Now paint a gradient UNDER the ghosted JLabel text (but not under the icon if any)
			// Note: this will need tweaking if your icon is not positioned to the left of the text
			Icon icon = lbl.getIcon();
			int nStartOfText = (icon == null) ? 0 : icon.getIconWidth()+lbl.getIconTextGap();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OVER, 0.5f));	// Make the gradient ghostlike
			g2.setPaint(new GradientPaint(nStartOfText,	0, SystemColor.controlShadow, 
										  getWidth(),	0, new Color(255,255,255,0)));
			g2.fillRect(nStartOfText, 0, getWidth(), _imgGhost.getHeight());
			g2.dispose();
			
			//setSelectionPath(path);	// Select this path in the tree
			
			// Wrap the path being transferred into a Transferable object
			Transferable transferable = new CTransferableTreePath(path);

			// Remember the path being dragged (because if it is being moved, we will have to delete it later)
			_pathSource = path;	
			
			// We pass our drag image just in case it IS supported by the platform
			e.startDrag(null, _imgGhost, new Point(5,5), transferable, this);
		}	
		

	// Interface: DragSourceListener
		public void dragEnter(DragSourceDragEvent e)
		{
		}	
		public void dragOver(DragSourceDragEvent e)
		{
		}	
		public void dragExit(DragSourceEvent e)
		{
		}	
		public void dropActionChanged(DragSourceDragEvent e)
		{
		}	
		public void dragDropEnd(DragSourceDropEvent e)
		{
			if (e.getDropSuccess())
			{
				int nAction = e.getDropAction();
				if (nAction == DnDConstants.ACTION_MOVE)
				{	// The dragged item (_pathSource) has been inserted at the target selected by the user.
					// Now it is time to delete it from its original location.

					// .
					// .. ask your TreeModel to delete the node 
					// .

					
					if (!iDragAndDrop.removeDraggedElement(_pathSource) ) {
						_pathSource = null;
						iDragAndDrop.refresh(false);
					} else {
						_pathSource = null;
						iDragAndDrop.refresh(true);
					}	

				}
			}
		}	


	// DropTargetListener interface object...
		class CDropTargetListener implements DropTargetListener
		{
			// Fields...
			private TreePath		_pathLast		= null;
			
			// Stefano: Rettangolo per la visualizzazione della linea sotto ogni riga in fase di DRAGGING
			//private Rectangle2D 	_raCueLine		= new Rectangle2D.Float();
			
			private Rectangle2D 	_raGhost		= new Rectangle2D.Float();
			
			
			// Stefano: Questa linea appare al di sotto di una riga
			//private Color			_colorCueLine = Color.BLACK;
			
			
			private Point			_ptLast			= new Point();
			//private Timer			_timerHover;  // Gestisce il tempo per espansione/collassamento di un ramo
			private int				_nLeftRight		= 0;	// Cumulative left/right mouse movement
			//private BufferedImage	_imgRight		= new CArrowImage(15,15,CArrowImage.ARROW_RIGHT);
			//private BufferedImage	_imgLeft		= new CArrowImage(15,15,CArrowImage.ARROW_LEFT );
			private int			 	_nShift			= 0;
			
			private DDJXTreeTable tree;
			
			// Constructor...
			public CDropTargetListener(DDJXTreeTable tree)
			{
				
				this.tree = tree;
				/*
				_colorCueLine = new Color(
											SystemColor.controlShadow.getRed(),
											SystemColor.controlShadow.getGreen(),
											SystemColor.controlShadow.getBlue(),
											64
										  );
				*/
				
				
				// Set up a hover timer, so that a node will be automatically expanded or collapsed
				// if the user lingers on it for more than a short time

				/**
				 * Questa parte controlla l'evento di apertura/chiusura di un'intero 
				 * ramo dell'albero.
				 * Il tempo di espansione/collassamento del ramo � di un secondo.
				 * 
				 * @author Stefano
				 */
				
				/*
				_timerHover = new Timer(1000, new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						_nLeftRight = 0;	// Reset left/right movement trend
						if (isRootPath(_pathLast))
							return;	// Do nothing if we are hovering over the root node
						if (isExpanded(_pathLast))
							collapsePath(_pathLast);
						else
							expandPath(_pathLast);
					}
				});
				_timerHover.setRepeats(false);	// Set timer to one-shot mode
				*/
			}
			
			// DropTargetListener interface
			public void dragEnter(DropTargetDragEvent e)
			{
				//System.out.println("DragEnter");
				if (!isDragAcceptable(e))
					e.rejectDrag();
				else
					e.acceptDrag(e.getDropAction());	
			}
			
			public void dragExit(DropTargetEvent e)
			{
				if (!DragSource.isDragImageSupported())
				{
					repaint(_raGhost.getBounds());				
				}
			}

			/**
			* This is where the ghost image is drawn
			*/		
			public void dragOver(DropTargetDragEvent e)
			{
				
				iDragAndDrop.startDrag();
				
				// Even if the mouse is not moving, this method is still invoked 10 times per second
				Point pt = e.getLocation();
				if (pt.equals(_ptLast))
					return;
				
				// Try to determine whether the user is flicking the cursor right or left
				int nDeltaLeftRight = pt.x - _ptLast.x;
				if ( (_nLeftRight > 0 && nDeltaLeftRight < 0) || (_nLeftRight < 0 && nDeltaLeftRight > 0) )
					_nLeftRight = 0;
				_nLeftRight += nDeltaLeftRight;	


				_ptLast = pt;	
				

				Graphics2D g2 = (Graphics2D) getGraphics();

				// If a drag image is not supported by the platform, then draw my own drag image
				if (!DragSource.isDragImageSupported())
				{
					paintImmediately(_raGhost.getBounds());	// Rub out the last ghost image and cue line
					// And remember where we are about to draw the new ghost image
					_raGhost.setRect(pt.x - _ptOffset.x, pt.y - _ptOffset.y, _imgGhost.getWidth(), _imgGhost.getHeight());
					g2.drawImage(_imgGhost, AffineTransform.getTranslateInstance(_raGhost.getX(), _raGhost.getY()), null);				
				}
				else	// Just rub out the last cue line
					; //  paintImmediately(_raCueLine.getBounds()); // Stefano: rettangolo per la linea sotto una riga				
				
				TreePath path = getPathForLocation(pt.x, pt.y);
				if (!(path == _pathLast))			
				{
					_nLeftRight = 0; 	// We've moved up or down, so reset left/right movement trend
					_pathLast = path;
					//_timerHover.restart(); // Stefano
				}

				// In any case draw (over the ghost image if necessary) a cue line indicating where a drop will occur
				
				int row = tree.getRowForPath(tree.getPathForLocation(pt.x, pt.y));
			    TreePath pathTemp = tree.getPathForRow(row);			
			    
			    
			    // Seleziona gli elementi della JXTreeTable in fase di DRAGGING
			    iDragAndDrop.selectTargetElement(pathTemp,tree,row);
			    
			    
			    
				// Work out the offset of the drag point from the TreePath bounding rectangle origin
			    // Stefano: Parte raltiva alla selezione dell'elemento in fase di DRAGGING
				/*
			    Rectangle raPath =  tree.getCellRect(row, 0, false);				
				_raCueLine.setRect(0,  raPath.y+(int)raPath.getHeight(), getWidth(), 2);

				
				g2.setColor(_colorCueLine);
				
				g2.fill(_raCueLine);
				*/
				
				// Now superimpose the left/right movement indicator if necessary
				/* Stefano: Parte relativa alla visualizzazione delle freccette di IN/OUT....non serve...per il momento
				if (_nLeftRight > 20)
				{
					g2.drawImage(_imgRight, AffineTransform.getTranslateInstance(pt.x - _ptOffset.x, pt.y - _ptOffset.y), null);				
					_nShift = +1;
				}
				else if (_nLeftRight < -20)
				{
					g2.drawImage(_imgLeft, AffineTransform.getTranslateInstance(pt.x - _ptOffset.x, pt.y - _ptOffset.y), null);				
					_nShift = -1;
				}
				else
					_nShift = 0;
				*/

				// And include the cue line in the area to be rubbed out next time
			    // Stefano: Aggiunge il rettangolo per selezionare un elemento in fase di DRAGGING 
				//_raGhost = _raGhost.createUnion(_raCueLine);	

					
				// Do this if you want to prohibit dropping onto the drag source
				
				// Stefano : Abilitando questo controllo non richiama il metodo che esegue il DROPPING e 
				//           appare un simbolo "divieto di accesso"....questo funzionamento � da testare
				//           con altre JVM
				
				//if (path.equals(_pathSource)) // originale
			    
			    //System.out.println("DragOVER");
			    
				if ( !iDragAndDrop.canDragElement(_pathSource, path))
					e.rejectDrag();
				else
					e.acceptDrag(e.getDropAction());
			}
			
			public void dropActionChanged(DropTargetDragEvent e)
			{
				
				if (!isDragAcceptable(e))
					e.rejectDrag();
				else
					e.acceptDrag(e.getDropAction());	
			}
			
			public void drop(DropTargetDropEvent e)
			{
				//_timerHover.stop(); // Stefano // Prevent hover timer from doing an unwanted expandPath or collapsePath
				
				iDragAndDrop.startDrop();
				
				if (!isDropAcceptable(e))
				{
					try  {
						e.rejectDrop();
						iDragAndDrop.refresh(false);
					} catch (Exception ex) {
						iDragAndDrop.refresh(false);
					}
					return;
				}
				
				e.acceptDrop(e.getDropAction());
				
				Transferable transferable = e.getTransferable();
				
				DataFlavor[] flavors = transferable.getTransferDataFlavors();
				for (int i = 0; i < flavors.length; i++ )
				{
					DataFlavor flavor = flavors[i];
					if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
					{
						try
						{
							Point pt = e.getLocation();
							TreePath pathTarget = getPathForLocation(pt.x, pt.y);
							TreePath pathSource = (TreePath) transferable.getTransferData(flavor);


							if ( iDragAndDrop.canDropElement(pathSource, pathTarget) ) {
								if ( !iDragAndDrop.insertDroppedElement(pathTarget, pathSource) ) {
									try {
										e.rejectDrop();
										iDragAndDrop.refresh(false);
									} catch ( Exception ex) {
										iDragAndDrop.refresh(false);
									}
									return;
								}
							} else {
								try {
									e.rejectDrop();
									iDragAndDrop.refresh(false);
								} catch (Exception ex) {
									iDragAndDrop.refresh(false);
								}
								
								return;
							}
								
							
							TreePath pathNewChild = null;
							
							if (pathNewChild != null)
								;//setSelectionPath(pathNewChild);	// Mark this as the selected path in the tree
							break; // No need to check remaining flavors
						}
						catch (UnsupportedFlavorException ufe)
						{
							try {
								e.dropComplete(false);
								iDragAndDrop.refresh(false);
							} catch (Exception ex) {
								iDragAndDrop.refresh(false);
							}
							
							return;
						}
						catch (IOException ioe)
						{
							try {
								e.dropComplete(false);
								iDragAndDrop.refresh(false);
							} catch (Exception ex) {
								iDragAndDrop.refresh(false);
							}
							
							return;
						}
					}
				}

				// Esegue il refresh con la rigenerazione del modello
				e.dropComplete(true);
			}
			
			
			
			// Helpers...
			public boolean isDragAcceptable(DropTargetDragEvent e)
			{
				// Only accept COPY or MOVE gestures (ie LINK is not supported)
				if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
					return false;

				// Only accept this particular flavor	
				if (!e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR))
					return false;
					
	/*				
				// Do this if you want to prohibit dropping onto the drag source...
				Point pt = e.getLocation();
				TreePath path = getClosestPathForLocation(pt.x, pt.y);
				if (path.equals(_pathSource))			
					return false;

	*/
					
	/*				
				// Do this if you want to select the best flavor on offer...
				DataFlavor[] flavors = e.getCurrentDataFlavors();
				for (int i = 0; i < flavors.length; i++ )
				{
					DataFlavor flavor = flavors[i];
					if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
						return true;
				}
	*/
				return true;
			}

			public boolean isDropAcceptable(DropTargetDropEvent e)
			{
				// Only accept COPY or MOVE gestures (ie LINK is not supported)
				if ((e.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) == 0)
					return false;

				// Only accept this particular flavor	
				if (!e.isDataFlavorSupported(CTransferableTreePath.TREEPATH_FLAVOR))
					return false;

	/*				
				// Do this if you want to prohibit dropping onto the drag source...
				Point pt = e.getLocation();
				TreePath path = getClosestPathForLocation(pt.x, pt.y);
				if (path.equals(_pathSource))			
					return false;
	*/				
					
					
				// Do this if you want to select the best flavor on offer...
				DataFlavor[] flavors = e.getCurrentDataFlavors();
				for (int i = 0; i < flavors.length; i++ )
				{
					DataFlavor flavor = flavors[i];
					if (flavor.isMimeTypeEqual(DataFlavor.javaJVMLocalObjectMimeType))
						return true;
				}
	
				return true;
			}
		}


	// Autoscroll Interface...
	// The following code was borrowed from the book:
//			Java Swing
//			By Robert Eckstein, Marc Loy & Dave Wood
//			Paperback - 1221 pages 1 Ed edition (September 1998) 
//			O'Reilly & Associates; ISBN: 156592455X 
	//
	// The relevant chapter of which can be found at:
//			http://www.oreilly.com/catalog/jswing/chapter/dnd.beta.pdf
		
		private static final int AUTOSCROLL_MARGIN = 12;
		// Ok, we�ve been told to scroll because the mouse cursor is in our
		// scroll zone.
		public void autoscroll(Point pt) 
		{
			TreePath path = getPathForLocation(pt.x, pt.y);
			
			// Figure out which row we�re on.
			int nRow = getRowForPath(path);
			
			// If we are not on a row then ignore this autoscroll request
			if (nRow < 0)
				return;

			Rectangle raOuter = getBounds();
			// Now decide if the row is at the top of the screen or at the
			// bottom. We do this to make the previous row (or the next
			// row) visible as appropriate. If we�re at the absolute top or
			// bottom, just return the first or last row respectively.
			
			// � meglio esplicitare meglio l'IF per capire meglio il funzionamento
			nRow =	(pt.y + raOuter.y <= AUTOSCROLL_MARGIN)			// Is row at top of screen? 
					 ?	
					(nRow <= 0 ? 0 : nRow - 1)						// Yes, scroll up one row
					 :
					(nRow < getRowCount() - 1 ? nRow + 1 : nRow);	// No, scroll down one row

			scrollRowToVisible(nRow);
		}
		// Calculate the insets for the *JTREE*, not the viewport
		// the tree is in. This makes it a bit messy.
		public Insets getAutoscrollInsets() 
		{
			int AUTO_M = 50; // originale AUTO_M = AUTOSCROLL_MARGIN
			
			Rectangle raOuter = getBounds();
			Rectangle raInner = getParent().getBounds();
			return new Insets(
				raInner.y - raOuter.y + AUTO_M, raInner.x - raOuter.x + AUTO_M,
				raOuter.height - raInner.height - raInner.y + raOuter.y + AUTO_M,
				raOuter.width - raInner.width - raInner.x + raOuter.x + AUTO_M);
		}
	/*	
		// Use this method if you want to see the boundaries of the
		// autoscroll active region. Toss it out, otherwise.
		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			Rectangle raOuter = getBounds();
			Rectangle raInner = getParent().getBounds();
			g.setColor(Color.red);
			g.drawRect(-raOuter.x + 12, -raOuter.y + 12,
				raInner.width - 24, raInner.height - 24);
		}
		
	*/


	// TreeModelListener interface...
		public void treeNodesChanged(TreeModelEvent e)
		{
			//System.out.println("treeNodesChanged");
			sayWhat(e);
			// We dont need to reset the selection path, since it has not moved
		}

		public void treeNodesInserted(TreeModelEvent e)
		{
			//System.out.println("treeNodesInserted ");
			sayWhat(e);

			// We need to reset the selection path to the node just inserted
			int nChildIndex = e.getChildIndices()[0];
			TreePath pathParent = e.getTreePath();
			
			//setSelectionPath(getChildPath(pathParent, nChildIndex));
		}

		public void treeNodesRemoved(TreeModelEvent e)
		{
			//System.out.println("treeNodesRemoved ");
			sayWhat(e);
		}

		public void treeStructureChanged(TreeModelEvent e)
		{
			//System.out.println("treeStructureChanged ");
			sayWhat(e);
		}


	// More helpers...
		private TreePath getChildPath(TreePath pathParent, int nChildIndex)
		{
			TreeModel model =  getTreeTableModel();
			return pathParent.pathByAddingChild(model.getChild(pathParent.getLastPathComponent(), nChildIndex));
		}


		private boolean isRootPath(TreePath path)
		{
			return isRootVisible() && getRowForPath(path) == 0;
		}



		private void sayWhat(TreeModelEvent e)
		{
			//System.out.println(e.getTreePath().getLastPathComponent());
			int[] nIndex = e.getChildIndices();
			for (int i = 0; i < nIndex.length ;i++ )
			{
				;//System.out.println(i+". "+nIndex[i]);
			}
		}
}
