package pl.miro;

import org.vaadin.addon.customfield.CustomField;

import pl.miro.domain.Kontrahent;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;

/**
 * A custom field that allows selection of a department.
 */
public class KontrahentSelector extends CustomField {
    
	private static final long serialVersionUID = 6223135478496567779L;

	private ComboBox kontrahent = new ComboBox();

    private JPAContainer<Kontrahent> container;
   
    public KontrahentSelector() {
        container = JPAContainerFactory.make(Kontrahent.class, Faktura.PERSISTENCE_UNIT);
        
        setCaption("Kontrahent");
       // container.setApplyFiltersImmediately(false);
        kontrahent.setContainerDataSource(container);
        kontrahent.setItemCaptionPropertyId("nazwa");

        kontrahent.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify the actual value of the custom field.
                 */
                if (kontrahent.getValue() == null) {
                    setValue(null, false);
                } else {
                    Kontrahent entity = container
                            .getItem(kontrahent.getValue()).getEntity();
                    setValue(entity, false);
                }
            }
        });

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(kontrahent);
        setCompositionRoot(cssLayout);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        setKontrahent(newDataSource.getValue());
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        setKontrahent(newValue);
    }

    private void setKontrahent(Object newValue) {
        Kontrahent value = (Kontrahent) newValue;
        kontrahent.setValue(value != null ? value.getId() : null);
    }

    @Override
    public Class<?> getType() {
        return Kontrahent.class;
    }

}
