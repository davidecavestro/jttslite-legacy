package net.sf.jttslite.common.log;

import java.awt.Color;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import net.sf.jttslite.common.util.CalendarUtils;

/**
 * Classe di utilità che dato uno {@link StyledDocument} va a scriverci sopra i
 * messaggi di log prodotti dall'applicazione. E' necessario che la classe che
 * si occupa di impostare il Logger applicativo vada a registrare questo tipo
 * di {@link StreamHandler} personalizzato con il metodo {@link Logger#addHandler(java.util.logging.Handler) }
 *
 * @author g-caliendo
 */
public class SwingHandler extends StreamHandler {

   private StyledDocument _document;
   private final Hashtable<String, Style> runAttr = new Hashtable<String, Style> ();
   private final StyleContext styles = new StyleContext ();

   /**
    * Costruttore di un Handler specifico per componenti Swing. dato uno {@link StyledDocument} va a scriverci sopra i
    * messaggi di log prodotti dall'applicazione. E' necessario che la classe che
    * si occupa di impostare il Logger applicativo vada a registrare questo tipo
    * di {@link StreamHandler} personalizzato con il metodo {@link Logger#addHandler(java.util.logging.Handler) }
    * @param document
    */
   public SwingHandler(final StyledDocument document) {
	  super ();
	  this._document = document;
	  createStyles ();
   }

   @Override
   public synchronized void publish(LogRecord record) {
	  final String timeStamp = CalendarUtils.toTSString (Calendar.getInstance ().getTime ());
	  final String message = record.getMessage ();
	  final String level = record.getLevel ().getLocalizedName ();
	  addParagraph (new Paragraph ("normal",
			  new Run[]{
				 new Run ("messagetype", level + "\t"),
				 new Run ("timestamp", timeStamp),
				 new Run ("none", " " + message)
			  }));
   }

   /**
	* Imposta lo stile di visualizzazione del componente che conterrà l'handler
	*/
   private void createStyles() {
	  // no attributes defined
	  Style s = styles.addStyle (null, null);
	  StyleConstants.setForeground (s, Color.WHITE);
	  runAttr.put ("none", s);
	  s = styles.addStyle (null, null);
	  StyleConstants.setBold (s, true);
	  StyleConstants.setForeground (s, new Color (153, 153, 102));
	  runAttr.put ("messagetype", s);

	  s = styles.addStyle (null, null);
	  StyleConstants.setFontFamily (s, "Monospaced");
	  StyleConstants.setBold (s, true);
	  StyleConstants.setForeground (s, Color.WHITE);
	  runAttr.put ("timestamp", s);

	  Style def = styles.getStyle (StyleContext.DEFAULT_STYLE);

	  Style heading = styles.addStyle ("heading", def);
	  StyleConstants.setBold (heading, true);
	  StyleConstants.setAlignment (heading, StyleConstants.ALIGN_CENTER);
	  StyleConstants.setSpaceAbove (heading, 10);
	  StyleConstants.setSpaceBelow (heading, 10);
	  StyleConstants.setFontSize (heading, 18);

	  // Title
	  Style sty = styles.addStyle ("title", heading);
	  StyleConstants.setFontSize (sty, 32);

	  // edition
	  sty = styles.addStyle ("edition", heading);
	  StyleConstants.setFontSize (sty, 16);

	  // author
	  sty = styles.addStyle ("author", heading);
	  StyleConstants.setItalic (sty, true);
	  StyleConstants.setSpaceBelow (sty, 25);

	  // subtitle
	  sty = styles.addStyle ("subtitle", heading);
	  StyleConstants.setSpaceBelow (sty, 35);

	  // normal
	  sty = styles.addStyle ("normal", def);
	  StyleConstants.setLeftIndent (sty, 10);
	  StyleConstants.setRightIndent (sty, 10);
	  //StyleConstants.setFontFamily(sty, "SansSerif");
	  StyleConstants.setFontSize (sty, 14);
	  StyleConstants.setSpaceAbove (sty, 1);
	  StyleConstants.setSpaceBelow (sty, 1);
   }

   private void addParagraph(Paragraph p) {
	  try {
		 Style s = null;
		 for (int i = 0; i < p.data.length; i++) {
			Run run = p.data[i];
			s = runAttr.get (run.attr);
			for (final String content : run.content) {
			   _document.insertString (_document.getLength (), content, s);
			}
		 }
		 // set logical style
		 Style ls = styles.getStyle (p.logical);
		 _document.setLogicalStyle (_document.getLength () - 1, ls);
		 _document.insertString (_document.getLength (), "\n", null);
	  } catch (BadLocationException e) {
		 System.err.println ("Internal error: " + e);
	  }
   }

   static class Paragraph {

	  Paragraph(String logical, Run[] data) {
		 this.logical = logical;
		 this.data = data;
	  }
	  String logical;
	  Run[] data;
   }

   static class Run {

	  Run(String attr, String... content) {
		 this.attr = attr;
		 this.content = content;
	  }
	  String attr;
	  String[] content;
   }

   public Document getDocument() {
	  return this._document;
   }
}
