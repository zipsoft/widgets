package com.zipsoft.widgets.client.collapsablepanel;

import com.vaadin.shared.communication.ServerRpc;

public interface CollapsablePanelServerRpc extends ServerRpc {
	
	public void hasCollapsed();
	
	public void hasExpanded();

}
