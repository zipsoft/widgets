package com.zipsoft.widgets.client.formdisplay;

import java.util.LinkedHashMap;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.client.Util;

public class VFormDisplay extends HTML {
	
	public static final String CLASSNAME = "v-formdisplay";
	
	public VFormDisplay() {
		super();
		setStyleName(CLASSNAME);
	}
	
	@Override
	public void onBrowserEvent(Event event) {	
		super.onBrowserEvent(event);
		if (event.getTypeInt() == Event.ONLOAD) {
            Util.notifyParentOfSizeChange(this, true);
            event.stopPropagation();
            return;
        }
	}
	
	public void setValues(LinkedHashMap<String, String> values) {
		for (String key : values.keySet()) {
			final Element element = findChildById(key);
			if (element != null) {
				if (element.getTagName().equals(InputElement.TAG)) {
					final InputElement input = (InputElement) element;
					
					if (input.getType().equals("checkbox")) {
						input.setChecked(Boolean.valueOf(values.get(key)));
					} else if (input.getType().equals("checkbox")) {
						
					}
//					.setValue(values.get(key));
				}
			}
		}
	}
	
	private Element findChildById(String id) {
		Element result = null;
		NodeList<Element> childs = this.getElement().getElementsByTagName("*");
		if (childs != null) {
			for (int i=0; i < childs.getLength(); i++) {
				if (childs.getItem(i).getId() != null && childs.getItem(i).getId().equals(id)) {
					result = childs.getItem(i);
					break;
				}
			}
		}
		
		return result;
	}

}
