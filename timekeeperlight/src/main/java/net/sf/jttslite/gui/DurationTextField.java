/*
 * DurationTextField.java
 *
 * Created on November 28, 2006, 12:30 AM
 *
 */

package net.sf.jttslite.gui;

import net.sf.jttslite.core.util.DurationUtils;
import net.sf.jttslite.core.util.DurationImpl;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import javax.swing.JFormattedTextField;
import javax.swing.text.InternationalFormatter;

/**
 * Campo di editazione della durata.
 *
 * @author Davide Cavestro
 */
public class DurationTextField extends JFormattedTextField {
	
	/**
	 * Costruttore.
	 */
	public DurationTextField () {
		super (new DurationFormatter ());
	}
//	public DurationTextField () {
//		super ();
////		setFormatter (new DurationFormatter ());
////		addKeyListener (new KeyAdapter (){
////                public void keyPressed(KeyEvent e) {
////                    if (e.getKeyCode ()==KeyEvent.VK_UP || e.getKeyCode ()==KeyEvent.VK_KP_UP){
////						select (getCaretPosition (), getCaretPosition ());
////						e.consume ();
////                    }
////                }
////            });		
//	}
	
	/**
	 * Costruttore.
	 */
	public DurationTextField (final DurationImpl d) {
		this ();
		setValue (d);
	}
	
	private static class DurationFormatter extends InternationalFormatter {
		public DurationFormatter () {
			super (	new Format () {
				@Override
					public StringBuffer format (Object obj, StringBuffer toAppendTo, FieldPosition pos) {
					final StringBuffer sb = new StringBuffer ();
					final DurationImpl d = (DurationImpl)obj;
					if (d==null){
						return sb;
					}
					final String f = DurationUtils.format (d);
					
					int newEndIdx = pos.getBeginIndex ()+8;
					if (d.getSeconds ()<0) {
						newEndIdx++;
					}
					if (d.getMinutes ()<0) {
						newEndIdx++;
					}
					if (d.getTotalHours ()<0) {
						newEndIdx++;
					}
					pos.setEndIndex (newEndIdx);
					sb.append (f.substring (pos.getBeginIndex (), pos.getEndIndex ()));
					return sb;
				}
				
				@Override
					public Object parseObject (String source, ParsePosition pos) {
					final String[] s = source.split (":");
					if (s.length<3) {
						pos.setErrorIndex (pos.getIndex ());
						return null;
					}
					
					int i = 0;
					
					int newPos = pos.getIndex ();
					
					final int hours = Integer.parseInt (s[i]);
					newPos+=s[i].length ()+1;
					i++;
					
					final int minutes = Integer.parseInt (s[i]);
					if (minutes>59) {
						pos.setErrorIndex (newPos);
						return null;
					}
					newPos+=s[i].length ()+1;
					i++;
					
					try {
						final int seconds = Integer.parseInt (s[i]);
						if (seconds>59) {
							pos.setErrorIndex (newPos);
							return null;
						}
						newPos+=s[i].length ();
						i++;
						
						pos.setIndex (newPos);
						return new DurationImpl (hours, minutes, seconds, 0);
					} catch (final NumberFormatException nfe) {
						throw new RuntimeException (java.util.ResourceBundle.getBundle("net.sf.jttslite.gui.res").getString("Invalid_data:_")+source);
					}
				}
				
			});
		}
		
		
		
//   /**
//     * Subclasses supporting incrementing must override this to handle
//     * the actual incrementing. <code>value</code> is the current value,
//     * <code>attributes</code> gives the field the cursor is in (may be
//     * null depending upon <code>canIncrement</code>) and 
//     * <code>direction</code> is the amount to increment by.
//     */
//    Object adjustValue(Object value, Map attributes, Object field,
//                           int direction) throws
//                      BadLocationException, ParseException {
//        return null;
//    }
//
//    /**
//     * Returns false, indicating InternationalFormatter does not allow
//     * incrementing of the value. Subclasses that wish to support 
//     * incrementing/decrementing the value should override this and
//     * return true. Subclasses should also override
//     * <code>adjustValue</code>.
//     */
//    boolean getSupportsIncrement() {
//        return true;
//    }		
//		
		
		
//		/**
//		 * Returns true, as DateFormatterFilter will support
//		 * incrementing/decrementing of the value.
//		 */
//			@Override
//			public boolean getSupportsIncrement () {
//			return true;
//		}
//		
//			@Override
//    public Action[] getActions() {
//        return super.getActions();
//    }
//		/**
//		 * Adjusts the Date if FieldPosition identifies a known calendar
//		 * field.
//		 */
//			public Object adjustValue (Object value, Map attributes, Object key, int direction) throws BadLocationException, ParseException {
//			if (key != null) {
//				int field;
//				
////// HOUR1 has no corresponding calendar field, thus, map
////// it to HOUR0 which will give the correct behavior.
////					if (key == DateFormat.Field.HOUR1) {
////						key = DateFormat.Field.HOUR0;
////					}
////					field = ((DateFormat.Field)key).getCalendarField ();
////
////					Calendar calendar = getCalendar ();
////
////					if (calendar != null) {
////						calendar.setTime ((Date)value);
////
////						int fieldValue = calendar.get (field);
////
////						try {
////							calendar.add (field, direction);
////							value = calendar.getTime ();
////						} catch (Throwable th) {
////							value = null;
////						}
////						return value;
////					}
//				return null;
//			}
//			return null;
//		}
	
	
	
	
	
	
	
	
	
	}

	@Override
	public DurationImpl getValue () {
		return (DurationImpl)super.getValue ();
	}
	
}
