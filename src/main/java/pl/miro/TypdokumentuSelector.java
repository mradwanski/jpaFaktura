package pl.miro;

import org.vaadin.addon.customfield.CustomField;

import pl.miro.domain.Typdokumentu;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;

/**
 * A custom field that allows selection of a department.
 */
public class TypdokumentuSelector extends CustomField {
    
	private static final long serialVersionUID = 6223135478496567779L;

	private ComboBox typydokumentu = new ComboBox();

    private JPAContainer<Typdokumentu> container;
   
    public TypdokumentuSelector() {
        container = JPAContainerFactory.make(Typdokumentu.class, Faktura.PERSISTENCE_UNIT);
        
        setCaption("Typ dokumentu");
       // container.setApplyFiltersImmediately(false);
        typydokumentu.setContainerDataSource(container);
        typydokumentu.setItemCaptionPropertyId("skrot");

        typydokumentu.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify the actual value of the custom field.
                 */
                if (typydokumentu.getValue() == null) {
                    setValue(null, false);
                } else {
                    Typdokumentu entity = container
                            .getItem(typydokumentu.getValue()).getEntity();
                    setValue(entity, false);
                }
            }
        });

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(typydokumentu);
        setCompositionRoot(cssLayout);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        setTypdokumentu(newDataSource.getValue());
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        setTypdokumentu(newValue);
    }

    private void setTypdokumentu(Object newValue) {
        Typdokumentu value = (Typdokumentu) newValue;
        typydokumentu.setValue(value != null ? value.getId() : null);
    }

    @Override
    public Class<?> getType() {
        return Typdokumentu.class;
    }

}
