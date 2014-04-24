package com.zipsoft.widgets.client.collapsablepanel;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.ui.LayoutClickRpc;

public interface CollapsablePanelServerRpc extends ServerRpc {
	
	public void hasCollapsed();
	
	public void hasExpanded();

}
