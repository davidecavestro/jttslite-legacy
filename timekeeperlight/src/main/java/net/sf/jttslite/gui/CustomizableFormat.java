package net.sf.jttslite.gui;

import net.sf.jttslite.common.util.GenericUtils;
import net.sf.jttslite.common.util.settings.SettingsSupport;
import net.sf.jttslite.ApplicationContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ResourceBundle;

/**
 * A customizable format.
 * It is some data with a default and the possibility to override it.
 */
public enum CustomizableFormat {
	/**
	 * A date without time
	 */
	SHORT_DATE {
		public String getKey (){
			return "date_format_short";
		}
	},
	/**
	 * A date with time
	 */
	LONG_DATE {
		public String getKey (){
			return "from_to_format_long";
		}
	},
	/**
	 * A date without time but with the week day name
	 */
	EXTENDED_DATE {
		public String getKey (){
			return "extended_date_format";
		}
	};
	
	
	final PropertyChangeSupport pcs = new PropertyChangeSupport (this);
	
	
	public String getDescription () {
		return ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString (getKey ()+"/Description");
	}
	public String getName () {
		return ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString (getKey ()+"/Name");
	}
	public String getDefaultValue () {
		return ResourceBundle.getBundle ("net.sf.jttslite.gui.res").getString (getKey ());
	}
	public String getCustomValue (final ApplicationContext context) {
		return SettingsSupport.getStringProperty (context.getUserSettings ().getProperties (), getKey ());
	}
	public String getValue (final ApplicationContext context) {
		final String custom = getCustomValue (context);
		if (custom!=null && custom.length()>0) {
			return custom;
		}
		return getDefaultValue ();
	}
	public void setCustomValue (final ApplicationContext context, final String customValue) {
		final String oldValue = getCustomValue (context);
		SettingsSupport.setStringProperty (context.getUserSettings ().getProperties (), getKey (), customValue);
		if (!GenericUtils.equals (oldValue, customValue)) {
			pcs.firePropertyChange (new PropertyChangeEvent (this, "customvalue", oldValue, customValue));
		}
	}
	public void addPropertyChangeListener (final PropertyChangeListener l) {
		pcs.addPropertyChangeListener (l);
	}
	
	public abstract String getKey ();

}