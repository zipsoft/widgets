package com.zipsoft.widgets;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class CollapsableBox extends CustomComponent {

	private final CssLayout root;
	
	private CssLayout bodyLayout;
	
	public CollapsableBox(String title, Component body) {
		super();
		
		root = new CssLayout();
		root.addStyleName("collapseblebox-container");
		setCompositionRoot(root);
		buildLayout(title, body);
	}

	private void buildLayout(String title, Component body) {
		
		CssLayout titleLayout = new CssLayout();
		titleLayout.addStyleName("collapsablebox-title");
		titleLayout.addComponent(new Label(title));
		titleLayout.addLayoutClickListener(new LayoutClickListener() {
			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				toggleBodyVisible();				
			}

		
		});
		
		bodyLayout = new CssLayout();
		bodyLayout.addStyleName("collapsablebox-body");				
				
		root.addComponent(titleLayout);
		bodyLayout.addComponent(body);
		
	}
	
	private void toggleBodyVisible() {
		bodyLayout.setVisible(!bodyLayout.isVisible());		
	}
	
}
