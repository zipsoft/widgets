package com.zipsoft.widgets;

import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.AbstractComponent;
import com.zipsoft.widgets.client.sizeinfo.SizeInfoState;

public class SizeInfo extends AbstractExtension {

	private static final long serialVersionUID = -7486200628742084957L;
	
	private final AbstractComponent component;
		
	public SizeInfo(AbstractComponent component) {
		this.component = component;
		extend(this.component);	
	}
	
	@Override
	public boolean isConnectorEnabled() {	
		return true;
	}
	
	public int getCurrentHeight() {
		return getState().currentHeight;
	}
	
	public int getCurrentWidth() {
		return getState().currentWidth;
	}
	
	@Override
	protected SizeInfoState getState() {
		return (SizeInfoState) super.getState();
	}

}
