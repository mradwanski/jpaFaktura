package pl.miro.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.miro.Faktura;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;

/**
 * 
 * @author mradwan
 * Table dla Locale = pl
 * 
 */
public class TablePL extends Table {

	private static final long serialVersionUID = 4047932814628337617L;
	
	public TablePL(String caption, Container dataSource) {
	        super(caption, dataSource);
	    }

	@Override
	protected String formatPropertyValue(Object rowId, Object colId, Property property) {
		Object v = property.getValue();
		if (v instanceof Double) {
			DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(getApplication().getLocale()); 
			df.applyPattern("#,##0.00#"); 
			return df.format(v);
		} else if (v instanceof BigDecimal) {
			return Faktura.ZLOTOWKI.format(v);
		} else if (v instanceof Date) {
			Date dateValue = (Date) v;
			return new SimpleDateFormat("d MMMM yyyy").format(dateValue);
		}
		return super.formatPropertyValue(rowId, colId, property);

	}
}
