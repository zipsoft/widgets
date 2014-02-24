package com.zipsoft.widgets;

import com.vaadin.ui.AbstractComponent;
import com.zipsoft.widgets.client.formdisplay.FormDisplayState;

public class FormDisplay extends AbstractComponent {

	private static final long serialVersionUID = 5466851749740726852L;
	
	public FormDisplay() {
		super();
		setWidth(100, Unit.PERCENTAGE);
	}
	
	@Override
	protected FormDisplayState getState() {
		return (FormDisplayState) super.getState();
	}
	
	public void setFormHtml(String formHtml) {
		getState().form = formHtml;
	}

}
