package pl.miro;


import javax.persistence.EntityManager;

import pl.miro.DokumentEditor.ZapisDokumentuEvent;
import pl.miro.DokumentEditor.ZapisDokumentuListener;
import pl.miro.domain.Dokument;
import pl.miro.domain.PozycjaDokumentu;
import pl.miro.util.TablePL;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
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

public class DokumentView extends VerticalLayout implements ComponentContainer {
	
	private static final long serialVersionUID = 8881308733988513642L;

	private Table dokumentTable;

    private TextField searchField;

    private Button newButton;
    private Button deleteButton;
    private Button editButton;

    private JPAContainer<Dokument> dokuments;

    private String textFilter;

    public DokumentView() {
  
        dokuments = JPAContainerFactory.make(Dokument.class, Faktura.PERSISTENCE_UNIT);
       // buildTree();
        buildMainArea();
       

    }

    private void buildMainArea() {
       
        dokumentTable = new TablePL(null, dokuments);
        dokumentTable.setSelectable(true);
        dokumentTable.setImmediate(true);
        dokumentTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });

        dokumentTable.setSizeFull();
        // personTable.setSelectable(true);
        dokumentTable.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    dokumentTable.select(event.getItemId());
                }
            }
        });

        dokumentTable.setVisibleColumns(new Object[] { "nazwa", "typDokumentu", "kontrahent", "dataWystawienia", "terminPlatnosci", "wydrukowany" });

        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Nowy dokument");
        newButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<Dokument> newDokumentItem = new BeanItem<Dokument>(new Dokument());
                DokumentEditor dokumentEditor = new DokumentEditor(newDokumentItem);
                dokumentEditor.addListener(new ZapisDokumentuListener() {
                    @Override
                    public void dokumentEditorSaved(ZapisDokumentuEvent event) {
                    	
                    	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit(Faktura.PERSISTENCE_UNIT);
                    	em.getTransaction().begin(); 
                    	
                        Object newObj = dokuments.addEntity(newDokumentItem.getBean());
                        Dokument nowyDokument = dokuments.getItem(newObj).getEntity();
                        BeanItemContainer<PozycjaDokumentu> newPozycje = event.getPozycjeBuffer();
                        for (PozycjaDokumentu nowaPozycja : newPozycje.getItemIds()) {
							nowaPozycja.setDokument(nowyDokument);
							nowyDokument.getPozycjaDokumentus().add(nowaPozycja);
							em.persist(nowaPozycja);
						}
                       
                        em.getTransaction().commit();
                    }
                });
                getApplication().getMainWindow().addWindow(dokumentEditor);
            }
        });

        deleteButton = new Button("Usuń dokument");
        deleteButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                dokuments.removeItem(dokumentTable.getValue());
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Edytuj dokument");
        editButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	
            	DokumentEditor dokumentEditor = new DokumentEditor( dokumentTable.getItem(dokumentTable.getValue()));
                dokumentEditor.addListener(new ZapisDokumentuListener() {
                    @Override
                    public void dokumentEditorSaved(ZapisDokumentuEvent event) {
                    	
                    	EntityManager em = JPAContainerFactory.createEntityManagerForPersistenceUnit(Faktura.PERSISTENCE_UNIT);
                    	em.getTransaction().begin(); 
                    	
                        Dokument edytowanyDokument = dokuments.getItem(dokumentTable.getValue()).getEntity();
                        
                        //Najpierw usuwam stare pozycje dokumentu
                        for (PozycjaDokumentu usunPozycja : edytowanyDokument.getPozycjaDokumentus()) {
                        	usunPozycja = em.merge(usunPozycja);
							em.remove(usunPozycja);
						}
                        //Pobieram i dodaję nowe pozycje
                        BeanItemContainer<PozycjaDokumentu> newPozycje = event.getPozycjeBuffer();
                        for (PozycjaDokumentu nowaPozycja : newPozycje.getItemIds()) {
							nowaPozycja.setDokument(edytowanyDokument);
							edytowanyDokument.getPozycjaDokumentus().add(nowaPozycja);
							em.persist(nowaPozycja);
						}
                       
                        em.getTransaction().commit();
                    }
                });
            	
                getApplication().getMainWindow().addWindow(dokumentEditor);
            }
        });
        editButton.setEnabled(false);

        searchField = new TextField();
        searchField.setInputPrompt("Wyszukaj dokument");
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
        addComponent(dokumentTable);
        setExpandRatio(dokumentTable, 1);
        setSizeFull();

    }

    private void updateFilters() {
        dokuments.setApplyFiltersImmediately(false);
        dokuments.removeAllContainerFilters();
 
        if (textFilter != null && !textFilter.equals("")) {
            Or or = new Or(new Like("nazwa", textFilter + "%", false),
                    new Like("kontrahent.nazwa", textFilter + "%", false));
            dokuments.addContainerFilter(or);
        }
        dokuments.applyFilters();
    }
}
