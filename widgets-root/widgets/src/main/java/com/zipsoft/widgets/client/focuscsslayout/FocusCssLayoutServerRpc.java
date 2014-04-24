package com.zipsoft.widgets.client.focuscsslayout;

import com.vaadin.shared.communication.ServerRpc;
import com.vaadin.shared.ui.LayoutClickRpc;

public interface FocusCssLayoutServerRpc extends LayoutClickRpc, ServerRpc {

	public void blurred();
	
	public void focused();
	
}
