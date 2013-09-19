package com.zipsoft.widgets;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TwoColumnLayout extends HorizontalLayout {
	
	private static final String CSS_CLASS = "fixed-twocoll-layout";
	private static final String CSS_LAYOUT_PANEL = "layout-panel";
	private static final String CSS_LEFT = "left-side";
	private static final String CSS_RIGHT = "right-side";
	
	
	private final VerticalLayout leftSide;
	
	private final CssLayout leftSideContainer;
	
	private final VerticalLayout rightSide;
	
	private final CssLayout rightSideContainer;
	
	public TwoColumnLayout(float leftSideExpandRatio, float rightSideExpandRatio, String leftSideCaption, String rightSideCaption) {
		super();
		setSizeFull();
        setMargin(new MarginInfo(true, true, false, true));
        setSpacing(true);
        
        addStyleName(CSS_CLASS);
        
        leftSide = new VerticalLayout();
        rightSide = new VerticalLayout();                
                
        leftSide.setCaption(leftSideCaption);
        leftSide.addStyleName(CSS_LEFT);
               
        leftSideContainer = createPanel(leftSide);
        addComponent(leftSideContainer);
        setExpandRatio(leftSideContainer, leftSideExpandRatio);
                
        rightSide.setCaption(rightSideCaption);
        rightSide.addStyleName(CSS_RIGHT);
        
        
        rightSideContainer = createPanel(rightSide);
        addComponent(rightSideContainer);
        setExpandRatio(rightSideContainer, rightSideExpandRatio);                
	}
	
	public TwoColumnLayout(float leftSideExpandRatio, float rightSideExpandRatio) {
		super();
		setSizeFull();
        setMargin(new MarginInfo(true, true, false, true));
        setSpacing(true);
        
        addStyleName(CSS_CLASS);
        
        leftSide = new VerticalLayout();
        rightSide = new VerticalLayout();                
                        
        leftSide.addStyleName(CSS_LEFT);
               
        leftSideContainer = createPanel(leftSide);
        addComponent(leftSideContainer);
        setExpandRatio(leftSideContainer, leftSideExpandRatio);
                        
        rightSide.addStyleName(CSS_RIGHT);        
        
        rightSideContainer = createPanel(rightSide);
        addComponent(rightSideContainer);
        setExpandRatio(rightSideContainer, rightSideExpandRatio);  
	}
	
	private CssLayout createPanel(Component content) {
        CssLayout panel = new CssLayout();
        panel.addStyleName(CSS_LAYOUT_PANEL);
        panel.setSizeFull();

        Button configure = new Button();
        configure.addStyleName("configure");
        configure.addStyleName("icon-cog");
        configure.addStyleName("icon-only");
        configure.addStyleName("borderless");
        configure.setDescription("Configure");
        configure.addStyleName("small");
        configure.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Notification.show("Not implemented in this demo");
            }
        });
        panel.addComponent(configure);

        panel.addComponent(content);
        return panel;
    }

	public VerticalLayout getLeftSide() {
		return leftSide;
	}

	public VerticalLayout getRightSide() {
		return rightSide;
	}
	
	

}
