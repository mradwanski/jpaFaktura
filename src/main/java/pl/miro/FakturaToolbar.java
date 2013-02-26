package pl.miro;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;

public class FakturaToolbar extends HorizontalLayout {
	
	private static final long serialVersionUID = 6725526796111224541L;
	
	private Faktura fakturaApplication;
	private Button sprzedaz;
    private Button towary;
    private Button kontrahenci;
    private Button help;
    
    private Embedded em;

	public FakturaToolbar(Faktura fakturapp) {
		setWidth("100%");
		setStyleName("toolbar");
		
		fakturaApplication = fakturapp;
		
		sprzedaz = new Button("Sprzedaż");
		sprzedaz.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = -8997052523986790122L;
			@Override
			public void buttonClick(ClickEvent event) {
				fakturaApplication.pokazDokumenty();
			}
		});
		//sprzedaz.setIcon(new ThemeResource("icons/32/document-add.png"));

		towary = new Button("Towary");
		//towary.setIcon(new ThemeResource("icons/32/folder-add.png"));
		
		kontrahenci = new Button("Kontrahenci");
		kontrahenci.addListener(new Button.ClickListener() {
			private static final long serialVersionUID = 988861797175784026L;

			@Override
			public void buttonClick(ClickEvent event) {
				fakturaApplication.pokazKontrahetow();
				
			}
		});
		//kontrahenci.setIcon(new ThemeResource("icons/32/users.png"));
		
		help = new Button("Pomoc");
		//help.setIcon(new ThemeResource("icons/32/help.png"));

		addComponent(sprzedaz);
		addComponent(towary);
		addComponent(kontrahenci);
		addComponent(help);
		
		//Logo
		em = new Embedded("", new ThemeResource("images/logo.png"));
		addComponent(em);
		setComponentAlignment(em, Alignment.MIDDLE_RIGHT);
		setExpandRatio(em, 1);
	
	}

}
