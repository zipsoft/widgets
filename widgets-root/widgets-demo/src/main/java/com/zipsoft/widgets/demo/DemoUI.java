package com.zipsoft.widgets.demo;

import com.zipsoft.widgets.CollapsablePanel;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("ZipsoftWidgets Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "com.zipsoft.widgets.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
//        final ZipsoftWidgets component = new ZipsoftWidgets();
    	
    	Label label = new Label();
    	
    	label.setValue("You widget's Connector will transfer the components from the server side as child widgets to our widget. The connector will feed the children to the panel trough it's standard API, namely add(Widget), remove(Widget) and clear();\n" + 
    			"Instead of going the standard route of extending AbstractComponentConnector as your connector, here we can take use of Vaadin's internal features and extend AbstractComponentContainerConnector. Additionally to implementing the getWidget() -method from AbstractComponentConnector, we also have to supply the class with an implementation to a method called updateCaption(ComponentConnector). This method is there if we want the container to take care of the captions for all the components. We don't need to take care of these captions in this example so we can leave the implementation empty.\n" + 
    			"\n" + 
    			"The real benefit of extending AbstractComponentContainerConnector is that we can now extend a method called onConnectorHierarchyChange(ConnectorHierarchyChangeEvent). This method will be called every time that the server side calls markAsDirty() if the component hierarchy has been changed. From within it we can call on getChildComponents to get a list of all the child components, and populate our widget with those.");    	    	
    	
    	CollapsablePanel panel = new CollapsablePanel();
    	panel.setContent(label.getValue());

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
//        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(panel);
//        layout.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
        setContent(layout);

    }

}
