package pl.miro;


import pl.miro.KontrahentEditor.EditorSavedEvent;
import pl.miro.KontrahentEditor.EditorSavedListener;
import pl.miro.domain.Kontrahent;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class KontrahentView extends VerticalLayout implements ComponentContainer {
	
	private static final long serialVersionUID = 4663122176973326618L;


    private Table kontrahentTable;

    private TextField searchField;

    private Button newButton;
    private Button deleteButton;
    private Button editButton;

    private JPAContainer<Kontrahent> kontrahents;

    private String textFilter;

    public KontrahentView() {
  
        kontrahents = JPAContainerFactory.make(Kontrahent.class, Faktura.PERSISTENCE_UNIT);
       // buildTree();
        buildMainArea();
       

    }

    private void buildMainArea() {
       
        kontrahentTable = new Table(null, kontrahents);
        kontrahentTable.setSelectable(true);
        kontrahentTable.setImmediate(true);
        kontrahentTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });

        kontrahentTable.setSizeFull();
        // personTable.setSelectable(true);
        kontrahentTable.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    kontrahentTable.select(event.getItemId());
                }
            }
        });

        kontrahentTable.setVisibleColumns(new Object[] { "nazwa", "nip", "email", "telefon", "miejscowosc", "kodpocztowy" });

        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Dodaj");
        newButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<Kontrahent> newKontrahentItem = new BeanItem<Kontrahent>(
                        new Kontrahent());
                KontrahentEditor personEditor = new KontrahentEditor(newKontrahentItem);
                personEditor.addListener(new EditorSavedListener() {
                    @Override
                    public void editorSaved(EditorSavedEvent event) {
                        kontrahents.addEntity(newKontrahentItem.getBean());
                    }
                });
                getApplication().getMainWindow().addWindow(personEditor);
            }
        });

        deleteButton = new Button("Usuń");
        deleteButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                kontrahents.removeItem(kontrahentTable.getValue());
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Edytuj");
        editButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getApplication().getMainWindow().addWindow(
                        new KontrahentEditor(kontrahentTable.getItem(kontrahentTable
                                .getValue())));
            }
        });
        editButton.setEnabled(false);

        searchField = new TextField();
        searchField.setInputPrompt("Search by name");
        searchField.addListener(new TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {
                textFilter = event.getText();
                updateFilters();
            }
        });

        toolbar.addComponent(newButton);
        toolbar.addComponent(deleteButton);
        toolbar.addComponent(editButton);
        toolbar.addComponent(searchField);
        toolbar.setWidth("100%");
        toolbar.setExpandRatio(searchField, 1);
        toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

        addComponent(toolbar);
        addComponent(kontrahentTable);
        setExpandRatio(kontrahentTable, 1);
        setSizeFull();

    }

    private void updateFilters() {
        kontrahents.setApplyFiltersImmediately(false);
        kontrahents.removeAllContainerFilters();
 
        if (textFilter != null && !textFilter.equals("")) {
            Or or = new Or(new Like("nazwa", textFilter + "%", false),
                    new Like("miejscowosc", textFilter + "%", false));
            kontrahents.addContainerFilter(or);
        }
        kontrahents.applyFilters();
    }
}
