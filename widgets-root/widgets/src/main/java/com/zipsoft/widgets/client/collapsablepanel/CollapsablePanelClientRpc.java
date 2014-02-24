package com.zipsoft.widgets.client.collapsablepanel;

import com.vaadin.shared.communication.ClientRpc;

public interface CollapsablePanelClientRpc extends ClientRpc {
	
	public void collapse();
	
	public void expand();

}
