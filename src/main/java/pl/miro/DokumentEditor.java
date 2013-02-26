package pl.miro;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import pl.miro.domain.Dokument;
import pl.miro.domain.PozycjaDokumentu;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class DokumentEditor extends Window implements Button.ClickListener, FormFieldFactory {

    private final Item dokumentItem;
    private TypdokumentuSelector typdokumentuSelector;
    private KontrahentSelector kontrahentSelector;
    private PozycjeDokumentuField pozycjeDokumentuField;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;
    
    public DokumentEditor(Item dokumentItem) {
        this.dokumentItem = dokumentItem;
        editorForm = new BeanValidationForm<Dokument>(Dokument.class);
        editorForm.setFormFieldFactory(this);
        editorForm.setWriteThrough(false);
        editorForm.setImmediate(true);
        
        editorForm.setItemDataSource(dokumentItem, Arrays.asList("typDokumentu", "nazwa", "kontrahent", "dataWystawienia", "miejcsceWystawienia", "dataSprzedazy", "terminPlatnosci", "formaPlatnosci", "pozycjaDokumentus", "wydrukowany", "opis"));
        
        saveButton = new Button("Zapisz", this);
        cancelButton = new Button("Anuluj", this);

        editorForm.getFooter().addComponent(saveButton);
        editorForm.getFooter().addComponent(cancelButton);
        getContent().setSizeUndefined();
        addComponent(editorForm);
        setCaption(buildCaption());
        
    }
    
    

    /**
     * @return the caption of the editor window
     */
    private String buildCaption() {
    	return String.format("%s %s", dokumentItem.getItemProperty("nazwa")
                .getValue(), dokumentItem.getItemProperty("kontrahent").getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.
     * ClickEvent)
     */
    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
        	
        	try {
    		 editorForm.commit();
             /** Zapisuję nowy dokument do bazy  */
             fireEvent(new ZapisDokumentuEvent(this, dokumentItem, pozycjeDokumentuField.getPozycjaDokumentusBuffer()));
			} catch (com.vaadin.data.Buffered.SourceException sourceEx) {
				// TODO: Dodać logowanie błędów - Błąd pojawia się tylko czasami i jest związany najpewniej z BeanItemContainer<PozycjaDokumentu>
				System.out.println(sourceEx);
			}
                               
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.FormFieldFactory#createField(com.vaadin.data.Item,
     * java.lang.Object, com.vaadin.ui.Component)
     */
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
    	
    	if("typDokumentu".equals(propertyId)) {
    		typdokumentuSelector = new TypdokumentuSelector();
    		return typdokumentuSelector;
    	}
    	
    	if ("kontrahent".equals(propertyId)) {  
    		kontrahentSelector = new KontrahentSelector();
    		return kontrahentSelector;
    	}
    	
    	if ("pozycjaDokumentus".equals(propertyId)) {
    		pozycjeDokumentuField = new PozycjeDokumentuField(dokumentItem);
    		return pozycjeDokumentuField;
    	}
       
        Field field = DefaultFieldFactory.get().createField(item, propertyId,
                uiContext);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }
        return field;
    }

    public void addListener(ZapisDokumentuListener listener) {
        try {
            Method method = ZapisDokumentuListener.class.getDeclaredMethod(
                    "dokumentEditorSaved", new Class[] { ZapisDokumentuEvent.class });
            addListener(ZapisDokumentuEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }

    public void removeListener(ZapisDokumentuListener listener) {
        removeListener(ZapisDokumentuEvent.class, listener);
    }

    public static class ZapisDokumentuEvent extends Component.Event {
        
        private Item savedItem;
        private BeanItemContainer<PozycjaDokumentu> pozycjeBuffer;

        public ZapisDokumentuEvent(Component source, Item savedItem, BeanItemContainer<PozycjaDokumentu> beanItemContainer) {
            super(source);
            this.savedItem = savedItem;
            this.pozycjeBuffer = beanItemContainer;
        }

        public Item getSavedItem() {
            return savedItem;
        }

		public BeanItemContainer<PozycjaDokumentu> getPozycjeBuffer() {
			return pozycjeBuffer;
		}
    }

    public interface ZapisDokumentuListener extends Serializable {
        public void dokumentEditorSaved(ZapisDokumentuEvent event);
    }

}
