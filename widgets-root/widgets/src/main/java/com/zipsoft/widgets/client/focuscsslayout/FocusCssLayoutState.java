package com.zipsoft.widgets.client.focuscsslayout;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.shared.Connector;
import com.vaadin.shared.ui.AbstractLayoutState;

public class FocusCssLayoutState extends AbstractLayoutState {
	
	private static final long serialVersionUID = -3029079620745360298L;
	{
        primaryStyleName = "v-csslayout";
    }
    public Map<Connector, String> childCss = new HashMap<Connector, String>();

}
