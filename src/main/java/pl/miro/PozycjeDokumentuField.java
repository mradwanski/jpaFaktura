package pl.miro;

import java.util.HashSet;
import java.util.Set;

import org.vaadin.addon.customfield.CustomField;

import pl.miro.domain.PozycjaDokumentu;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.VerticalLayout;

/**
 * A custom field that allows selection of a department.
 */
public class PozycjeDokumentuField extends CustomField {

	private static final long serialVersionUID = 6775807278037004500L;

	private PozycjaDokumentuView pozycjaDokumentuView;

	public PozycjeDokumentuField(Item dokumentItem) {

		VerticalLayout layout = new VerticalLayout();
		pozycjaDokumentuView = new PozycjaDokumentuView(dokumentItem);
		layout.addComponent(pozycjaDokumentuView);

		setCompositionRoot(layout);
	}

	/*public Set<PozycjaDokumentu> getPozycjeDokumentu() {
		// TODO Auto-generated method stu
		
		HashSet<PozycjaDokumentu> value = new HashSet<PozycjaDokumentu>(); 

		

		return value; 
	}*/
	
	@Override
	public Class<?> getType() {
		return HashSet.class;
	}
	
    public BeanItemContainer<PozycjaDokumentu> getPozycjaDokumentusBuffer() {
		
		return pozycjaDokumentuView.getPozycjaDokumentusBuffer();
	}

}
