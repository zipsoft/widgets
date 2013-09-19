package com.zipsoft.widgets;

import com.zipsoft.widgets.client.ZipsoftWidgetsClientRpc;
import com.zipsoft.widgets.client.ZipsoftWidgetsServerRpc;
import com.zipsoft.widgets.client.ZipsoftWidgetsState;

import com.vaadin.shared.MouseEventDetails;

// This is the server-side UI component that provides public API 
// for ZipsoftWidgets
public class ZipsoftWidgets extends com.vaadin.ui.AbstractComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6628852114492473822L;

	private int clickCount = 0;

	// To process events from the client, we implement ServerRpc
	private ZipsoftWidgetsServerRpc rpc = new ZipsoftWidgetsServerRpc() {

		// Event received from client - user clicked our widget
		public void clicked(MouseEventDetails mouseDetails) {
			
			// Send nag message every 5:th click with ClientRpc
			if (++clickCount % 5 == 0) {
				getRpcProxy(ZipsoftWidgetsClientRpc.class)
						.alert("Ok, that's enough!");
			}
			
			// Update shared state. This state update is automatically 
			// sent to the client. 
			getState().text = "You have clicked " + clickCount + " times";
		}
	};

	public ZipsoftWidgets() {

		// To receive events from the client, we register ServerRpc
		registerRpc(rpc);
	}

	// We must override getState() to cast the state to ZipsoftWidgetsState
	@Override
	public ZipsoftWidgetsState getState() {
		return (ZipsoftWidgetsState) super.getState();
	}
}
