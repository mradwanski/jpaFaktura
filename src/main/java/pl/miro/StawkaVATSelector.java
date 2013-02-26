package pl.miro;

import org.vaadin.addon.customfield.CustomField;

import pl.miro.domain.StawkaVAT;

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
public class StawkaVATSelector extends CustomField {
    
	private static final long serialVersionUID = 9138773703640665701L;

	private ComboBox stawkaVAT = new ComboBox();

    private JPAContainer<StawkaVAT> container;
   
    public StawkaVATSelector() {
        container = JPAContainerFactory.make(StawkaVAT.class, Faktura.PERSISTENCE_UNIT);
        
        setCaption("Stawka VAT");
       // container.setApplyFiltersImmediately(false);
        stawkaVAT.setContainerDataSource(container);
        stawkaVAT.setItemCaptionPropertyId("nazwa");

        stawkaVAT.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify the actual value of the custom field.
                 */
                if (stawkaVAT.getValue() == null) {
                    setValue(null, false);
                } else {
                    StawkaVAT entity = container
                            .getItem(stawkaVAT.getValue()).getEntity();
                    setValue(entity, false);
                }
            }
        });

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(stawkaVAT);
        setCompositionRoot(cssLayout);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        setStawkaVAT(newDataSource.getValue());
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        setStawkaVAT(newValue);
    }

    private void setStawkaVAT(Object newValue) {
        StawkaVAT value = (StawkaVAT) newValue;
        stawkaVAT.setValue(value != null ? value.getId() : null);
    }

    @Override
    public Class<?> getType() {
        return StawkaVAT.class;
    }

}
