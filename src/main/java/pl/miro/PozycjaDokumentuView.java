package pl.miro;


import java.math.BigDecimal;

import pl.miro.PozycjaDokumentuEditor.PozycjaEditorSavedEvent;
import pl.miro.PozycjaDokumentuEditor.PozycjaEditorSavedListener;
import pl.miro.domain.PozycjaDokumentu;
import pl.miro.util.TablePL;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class PozycjaDokumentuView extends VerticalLayout implements ComponentContainer {

	private static final long serialVersionUID = 522745787277333040L;
	private Table pozycjaDokumentuTable;
    private Button newButton;
    private Button deleteButton;
    private Button editButton;
    
    private BeanItemContainer<PozycjaDokumentu> pozycjaDokumentusBuffer;


    public PozycjaDokumentuView(Item dokumentItem) {
    	 
    	pobierzPozycjeDoBuffora(dokumentItem);
        buildMainArea();
    }

    private void pobierzPozycjeDoBuffora(Item dokumentItem) {
    	JPAContainer<PozycjaDokumentu> pozycjaDokumentusJPA;
    	pozycjaDokumentusJPA = JPAContainerFactory.make(PozycjaDokumentu.class, Faktura.PERSISTENCE_UNIT);
    	pozycjaDokumentusJPA.addContainerFilter(new Equal("dokument.id", (Long) dokumentItem.getItemProperty("id").getValue() ));
    	
    	pozycjaDokumentusBuffer = new BeanItemContainer<PozycjaDokumentu>(PozycjaDokumentu.class);
    	     
        for (Object  obj : pozycjaDokumentusJPA.getItemIds()) {
        	EntityItem<PozycjaDokumentu> pozycjaItem = pozycjaDokumentusJPA.getItem(obj);
        	pozycjaItem.getEntity().setId(null);
        	pozycjaDokumentusBuffer.addBean(pozycjaItem.getEntity());
		}
    }
    
    private void buildMainArea() {
       
        pozycjaDokumentuTable = new TablePL(null, pozycjaDokumentusBuffer);
        
        pozycjaDokumentuTable.setSelectable(true);
        pozycjaDokumentuTable.setImmediate(true);
        pozycjaDokumentuTable.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(ValueChangeEvent event) {
                setModificationsEnabled(event.getProperty().getValue() != null);
            }

            private void setModificationsEnabled(boolean b) {
                deleteButton.setEnabled(b);
                editButton.setEnabled(b);
            }
        });
        
        pozycjaDokumentuTable.addListener(new ItemClickEvent.ItemClickListener() {
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    //((Table) event.getSource()).select(event.getItemId());
                    
                	if (pozycjaDokumentuTable.getValue() != null) {
                		PozycjaDokumentuEditor pozycjaEditor =  new PozycjaDokumentuEditor(pozycjaDokumentuTable.getItem(pozycjaDokumentuTable.getValue()));
                        pozycjaEditor.addListener(new PozycjaEditorSavedListener() {
                            @Override
                            public void editorSaved(PozycjaEditorSavedEvent event) {
                            	pokazStopkeTabeli();
                            }
                        });
                        getApplication().getMainWindow().addWindow(pozycjaEditor);
                	}
                    
                }
            }
        });

        pozycjaDokumentuTable.setSizeFull();
        pozycjaDokumentuTable.setPageLength(pozycjaDokumentuTable.size());
        
        //Stopka tabeli
        pokazStopkeTabeli();
        pozycjaDokumentuTable.addListener(new ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.isDoubleClick()) {
                    pozycjaDokumentuTable.select(event.getItemId());
                }
            }
        });

        pozycjaDokumentuTable.setVisibleColumns(new Object[] { "lp", "nazwa", "pkwiu", "ilosc", "jednostkaMiary", "cenaNetto", "wartoscNetto", "stawkaVAT", "wartoscVAT", "wartoscBrutto" });
        pozycjaDokumentuTable.setColumnAlignment("ilosc", Table.ALIGN_RIGHT);
        pozycjaDokumentuTable.setColumnAlignment("jednostkaMiary", Table.ALIGN_CENTER);
        pozycjaDokumentuTable.setColumnAlignment("cenaNetto", Table.ALIGN_RIGHT);
        pozycjaDokumentuTable.setColumnAlignment("wartoscNetto", Table.ALIGN_RIGHT);
        pozycjaDokumentuTable.setColumnAlignment("stawkaVAT", Table.ALIGN_RIGHT);
        pozycjaDokumentuTable.setColumnAlignment("wartoscVAT", Table.ALIGN_RIGHT);
        pozycjaDokumentuTable.setColumnAlignment("wartoscBrutto", Table.ALIGN_RIGHT);
        
        HorizontalLayout toolbar = new HorizontalLayout();
        newButton = new Button("Dodaj");
        newButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                final BeanItem<PozycjaDokumentu> newPozycjaItem = new BeanItem<PozycjaDokumentu>(new PozycjaDokumentu(pozycjaDokumentuTable.size() + 1));
                PozycjaDokumentuEditor pozycjaEditor = new PozycjaDokumentuEditor(newPozycjaItem);
                pozycjaEditor.addListener(new PozycjaEditorSavedListener() {
                    @Override
                    public void editorSaved(PozycjaEditorSavedEvent event) {
                    	pozycjaDokumentusBuffer.addBean(newPozycjaItem.getBean());
                    	pozycjaDokumentuTable.setPageLength(pozycjaDokumentuTable.size());
                    	pokazStopkeTabeli();
                    }
                });
                getApplication().getMainWindow().addWindow(pozycjaEditor);
            }
        });

        deleteButton = new Button("Usuń");
        deleteButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
               pozycjaDokumentusBuffer.removeItem(pozycjaDokumentuTable.getValue());
               pozycjaDokumentuTable.setPageLength(pozycjaDokumentuTable.size());
               //Ponowne ponumerowanie pozycji
               int lp = 1;
               for (PozycjaDokumentu pozDokum : pozycjaDokumentusBuffer.getItemIds() ) {
            	   pozDokum.setLp(lp++);
               }
               pokazStopkeTabeli();
               editButton.setEnabled(false); 
               deleteButton.setEnabled(false);
               pozycjaDokumentuTable.refreshRowCache();
            }
        });
        deleteButton.setEnabled(false);

        editButton = new Button("Edytuj");
        editButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
            	PozycjaDokumentuEditor pozycjaEditor =  new PozycjaDokumentuEditor(pozycjaDokumentuTable.getItem(pozycjaDokumentuTable.getValue()));
                pozycjaEditor.addListener(new PozycjaEditorSavedListener() {
                    @Override
                    public void editorSaved(PozycjaEditorSavedEvent event) {
                    	pokazStopkeTabeli();
                    }
                });
                getApplication().getMainWindow().addWindow(pozycjaEditor);
              
            }
        });
        editButton.setEnabled(false);


        toolbar.addComponent(newButton);
        toolbar.addComponent(deleteButton);
        toolbar.addComponent(editButton);
        toolbar.setWidth("100%");

        addComponent(toolbar);
        addComponent(pozycjaDokumentuTable);
        setExpandRatio(pozycjaDokumentuTable, 1);
        setSizeFull();

    }
    
    private void pokazStopkeTabeli() {
    	BigDecimal sumaWartoscNetto = new BigDecimal(0d);
    	BigDecimal sumaWartoscVAT = new BigDecimal(0d);
    	BigDecimal sumaWartoscBrutto = new BigDecimal(0d);
    	for (PozycjaDokumentu pozDokum : pozycjaDokumentusBuffer.getItemIds() ) {
     	  sumaWartoscNetto = sumaWartoscNetto.add(pozDokum.getWartoscNetto());
     	  sumaWartoscVAT = sumaWartoscVAT.add(pozDokum.getWartoscVAT());
     	  sumaWartoscBrutto = sumaWartoscBrutto.add(pozDokum.getWartoscBrutto());
        }
    	
		 pozycjaDokumentuTable.setFooterVisible(true);
	     pozycjaDokumentuTable.setColumnFooter("wartoscNetto", Faktura.ZLOTOWKI.format(sumaWartoscNetto));
	     pozycjaDokumentuTable.setColumnFooter("wartoscVAT", Faktura.ZLOTOWKI.format(sumaWartoscVAT));
	     pozycjaDokumentuTable.setColumnFooter("wartoscBrutto", Faktura.ZLOTOWKI.format(sumaWartoscBrutto));
    }

	public BeanItemContainer<PozycjaDokumentu> getPozycjaDokumentusBuffer() {
		
		return pozycjaDokumentusBuffer;
	}
}
