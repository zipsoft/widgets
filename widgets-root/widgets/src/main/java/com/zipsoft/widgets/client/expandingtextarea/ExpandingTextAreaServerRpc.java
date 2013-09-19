package com.zipsoft.widgets.client.expandingtextarea;

import com.vaadin.shared.communication.ServerRpc;

public interface ExpandingTextAreaServerRpc extends ServerRpc {
	
	void setRows(int rows);

}
