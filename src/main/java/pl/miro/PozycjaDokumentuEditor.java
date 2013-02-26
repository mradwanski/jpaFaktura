package pl.miro;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import pl.miro.domain.PozycjaDokumentu;
import pl.miro.util.FieldUtil;
import pl.miro.util.NumberFieldPL;

import com.vaadin.addon.beanvalidation.BeanValidationForm;
import com.vaadin.data.Item;
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
public class PozycjaDokumentuEditor extends Window implements Button.ClickListener,
        FormFieldFactory {

    private final Item pozycjaDokumentuItem;
    private JednostkaMiarySelector jednostkaMiarySelector;
    private StawkaVATSelector stawkaVATSelector; 
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;
    private TextField fieldIlosc;
    private TextField fieldCenaNetto;
  
    public PozycjaDokumentuEditor(Item pozycjaItem) {
        this.pozycjaDokumentuItem = pozycjaItem;
        editorForm = new BeanValidationForm<PozycjaDokumentu>(PozycjaDokumentu.class);
        editorForm.setFormFieldFactory(this);
        editorForm.setWriteThrough(false);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(pozycjaItem, Arrays.asList("nazwa", "pkwiu", "ilosc", "jednostkaMiary", "cenaNetto", "stawkaVAT"));

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
        return String.format("%s", pozycjaDokumentuItem.getItemProperty("nazwa"));
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
        	//Jeśli zarejestrowane walidatory nie zwracają błędów to commit formatki
        	if (editorForm.isValid() ) {
        		editorForm.commit();
			    fireEvent(new PozycjaEditorSavedEvent(this, pozycjaDokumentuItem));
			    close();
        	}
			   
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
            close();
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.ui.FormFieldFactory#createField(com.vaadin.data.Item,
     * java.lang.Object, com.vaadin.ui.Component)
     */
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
    	
    	if ("jednostkaMiary".equals(propertyId)) {
    		jednostkaMiarySelector = new JednostkaMiarySelector();
    		return jednostkaMiarySelector;
    	}
    	
    	if ("stawkaVAT".equals(propertyId)) {
    		stawkaVATSelector = new StawkaVATSelector();
    		return stawkaVATSelector;
    	}
    	
        if ("ilosc".equals(propertyId)  ) {
        	getFieldIlosc().setNullRepresentation("");
        	getFieldIlosc().setCaption("Ilość");
        	return getFieldIlosc();
    	}
    	
        if ("cenaNetto".equals(propertyId)) {
        	getFieldCenaNetto().setCaption("Cena Netto");
        	return getFieldCenaNetto();
        } 
        
        Field field = DefaultFieldFactory.get().createField(item, propertyId, uiContext);
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }
        return field;
    }

    public void addListener(PozycjaEditorSavedListener listener) {
        try {
            Method method = PozycjaEditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { PozycjaEditorSavedEvent.class });
            addListener(PozycjaEditorSavedEvent.class, listener, method);
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }

    public void removeListener(PozycjaEditorSavedListener listener) {
        removeListener(PozycjaEditorSavedEvent.class, listener);
    }

    public static class PozycjaEditorSavedEvent extends Component.Event {
        
        private Item savedItem;

        public PozycjaEditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

        public Item getSavedItem() {
            return savedItem;
        }
    }

    public interface PozycjaEditorSavedListener extends Serializable {
        public void editorSaved(PozycjaEditorSavedEvent event);
    }

	public TextField getFieldIlosc() {
		if (fieldIlosc == null) {
			fieldIlosc = FieldUtil.createDecimalTextField("Niepoprawna liczba");
		}
		return fieldIlosc;
	}

	public TextField getFieldCenaNetto() {
		if (fieldCenaNetto == null) {
			fieldCenaNetto = new NumberFieldPL("To nie jest poprawna kwota");
		}
		return fieldCenaNetto;
	}
	
}
