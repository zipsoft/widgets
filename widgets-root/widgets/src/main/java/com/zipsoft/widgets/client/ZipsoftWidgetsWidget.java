package com.zipsoft.widgets.client;

import com.google.gwt.user.client.ui.Label;

// Extend any GWT Widget
public class ZipsoftWidgetsWidget extends Label {

	public ZipsoftWidgetsWidget() {

		// CSS class-name should not be v- prefixed
		setStyleName("widgets");

		// State is set to widget in ZipsoftWidgetsConnector		
	}

}