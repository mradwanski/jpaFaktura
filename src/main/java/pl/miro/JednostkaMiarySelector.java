package pl.miro;

import org.vaadin.addon.customfield.CustomField;

import pl.miro.domain.JednostkaMiary;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;

/**
 * 
 * @author mradwan
 *
 */
public class JednostkaMiarySelector extends CustomField {
    
	private static final long serialVersionUID = 6589166139780369107L;

	private ComboBox jednostkaMiary = new ComboBox();

    private JPAContainer<JednostkaMiary> container;
   
    public JednostkaMiarySelector() {
        container = JPAContainerFactory.make(JednostkaMiary.class, Faktura.PERSISTENCE_UNIT);
        
        setCaption("Jednostka miary");
       // container.setApplyFiltersImmediately(false);
        jednostkaMiary.setContainerDataSource(container);
        jednostkaMiary.setItemCaptionPropertyId("nazwa");

        jednostkaMiary.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify the actual value of the custom field.
                 */
                if (jednostkaMiary.getValue() == null) {
                    setValue(null, false);
                } else {
                    JednostkaMiary entity = container
                            .getItem(jednostkaMiary.getValue()).getEntity();
                    setValue(entity, false);
                }
            }
        });

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(jednostkaMiary);
        setCompositionRoot(cssLayout);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        setJednostkaMiary(newDataSource.getValue());
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        setJednostkaMiary(newValue);
    }

    private void setJednostkaMiary(Object newValue) {
        JednostkaMiary value = (JednostkaMiary) newValue;
        jednostkaMiary.setValue(value != null ? value.getId() : null);
    }

    @Override
    public Class<?> getType() {
        return JednostkaMiary.class;
    }

}
