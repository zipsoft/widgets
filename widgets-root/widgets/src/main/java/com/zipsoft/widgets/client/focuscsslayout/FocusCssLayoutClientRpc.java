package com.zipsoft.widgets.client.focuscsslayout;

import com.vaadin.shared.communication.ClientRpc;

public interface FocusCssLayoutClientRpc extends ClientRpc {
	
	public void focus();
	
	public void blur();

}
