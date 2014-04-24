package com.zipsoft.widgets.client.sizeinfo;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.LayoutManager;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.ui.Connect;
import com.zipsoft.widgets.SizeInfo;

@Connect(SizeInfo.class)
public class SizeInfoConnector extends AbstractExtensionConnector {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4603371629273034438L;

	private Widget widget;		
	
	@Override
	protected void init() {	
		super.init();
	}
	
	@Override
	public SizeInfoState getState() {
		return (SizeInfoState) super.getState();
	}

	
	@Override
	protected void extend(ServerConnector target) {
		if (target instanceof ComponentConnector)	 {
			widget = ((ComponentConnector) target).getWidget();
			
			if (widget.getOffsetWidth() > -1 && widget.getOffsetHeight() > -1) {
				getState().currentHeight = widget.getOffsetHeight();
				getState().currentWidth = widget.getOffsetWidth();
				
				
			}
			
			LayoutManager.get(getConnection()).addElementResizeListener(widget.getElement(), new ElementResizeListener() {
				
				@Override
				public void onElementResize(ElementResizeEvent e) {
					getState().currentHeight = widget.getOffsetHeight();
					getState().currentWidth = widget.getOffsetWidth();
				}
			});
		}
	}

}
