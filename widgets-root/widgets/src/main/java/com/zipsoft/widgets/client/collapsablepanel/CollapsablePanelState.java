package com.zipsoft.widgets.client.collapsablepanel;

import com.vaadin.shared.AbstractComponentState;
import com.vaadin.shared.Connector;

public class CollapsablePanelState extends AbstractComponentState {

	private static final long serialVersionUID = 614416282143046461L;
	
	public Connector header;
	public Connector component;
	public boolean isAnimationEnabled = true;
	public String expanderCaptionCollapsed = "Pogledaj više";
	public String expanderCaptionExpanded = "Sakrij";
	public String contentHtml = "";
	public int maxCollapsedHeight = 100;

}
