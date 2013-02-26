package pl.miro.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.TextField;

public class FieldUtil {
	
	   public static TextField createDecimalTextField(String validatorMessage) {
	        TextField textField = new TextField();
	        textField.setRequired(true);
	        textField.setImmediate(true);
	        textField.setRequiredError("Pole wymagane");
	        textField.addValidator(new RegexpValidator("[1-9][0-9]*(\\,[0-9]{1,2}){0,1}", validatorMessage));
	        textField.setNullSettingAllowed(false);
	        DecimalFormat nf = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("pl","PL"));
	        nf.applyPattern("###########0.00"); 
	        textField.setNullRepresentation(nf.format(BigDecimal.ZERO));
	        textField.setPropertyDataSource(new PropertyFormatter(new ObjectProperty<BigDecimal>(new BigDecimal(0d))) {
	            
	            @Override
	            public Object parse(String formattedValue) throws Exception {
	            	DecimalFormat nf = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("pl","PL"));
	            	nf.applyPattern("###########0.00"); 
	                return new BigDecimal(nf.parseObject(formattedValue).toString());
	            }
	            
	            @Override
	            public String format(Object value) {
	            	DecimalFormat nf = (DecimalFormat) DecimalFormat.getNumberInstance(new Locale("pl","PL"));
	            	nf.applyPattern("###########0.00"); 
	                nf.setMinimumFractionDigits(2);
	                nf.setMaximumFractionDigits(2);
	                return nf.format(value);
	            }
	        });
	        return textField;
	    }
	   
}
