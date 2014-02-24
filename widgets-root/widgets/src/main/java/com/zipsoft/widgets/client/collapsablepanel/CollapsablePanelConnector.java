package com.zipsoft.widgets.client.collapsablepanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;
import com.zipsoft.widgets.CollapsablePanel;

@Connect(CollapsablePanel.class)
public class CollapsablePanelConnector extends AbstractComponentConnector {

	private static final long serialVersionUID = -612819496017276394L;
	
	private CollapsablePanelServerRpc rpc = RpcProxy.create(CollapsablePanelServerRpc.class, this);
		
	@Override
	protected void init() {	
		super.init();
		
		registerRpc(CollapsablePanelClientRpc.class, new CollapsablePanelClientRpc() {
			
			private static final long serialVersionUID = 1934634375910244765L;

			@Override
			public void expand() {
				getWidget().setOpen(true);
				setSizeChanged();								
			}
			
			@Override
			public void collapse() {
				getWidget().setOpen(false);
				setSizeChanged();				
			}
		});
		
		getWidget().addOpenHandler(new OpenHandler<VCollapsablePanel>() {
			
			@Override
			public void onOpen(OpenEvent<VCollapsablePanel> event) {
				rpc.hasExpanded();				
			}
		});
		
		getWidget().addCloseHandler(new CloseHandler<VCollapsablePanel>() {
			
			@Override
			public void onClose(CloseEvent<VCollapsablePanel> event) {
				rpc.hasCollapsed();				
			}
		});
				
	}
	
	public void setSizeChanged() {
		getLayoutManager().setNeedsMeasure(this);
	}

	
	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		
		getWidget().setAnimationEnabled(getState().isAnimationEnabled);
		getWidget().setContentHtml(getState().contentHtml);
		getWidget().setExpandedCaption(getState().expanderCaptionExpanded);
		getWidget().setCollapsedCaption(getState().expanderCaptionCollapsed);
		getWidget().setMaxCollapsedHeight(getState().maxCollapsedHeight);
	}
	
	@Override
	protected Widget createWidget() {	
		return GWT.create(VCollapsablePanel.class);
	}
	
	@Override
	public VCollapsablePanel getWidget() {
		return (VCollapsablePanel) super.getWidget();
	}
	
	@Override
	public CollapsablePanelState getState() {
		return (CollapsablePanelState) super.getState();
	}

}
