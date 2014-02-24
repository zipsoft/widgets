package com.zipsoft.widgets;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.zipsoft.widgets.client.collapsablepanel.CollapsablePanelClientRpc;
import com.zipsoft.widgets.client.collapsablepanel.CollapsablePanelServerRpc;
import com.zipsoft.widgets.client.collapsablepanel.CollapsablePanelState;

public class CollapsablePanel extends AbstractComponent {

	private static final long serialVersionUID = -6725720782778577747L;
	
	private boolean isCollapsed = true;
	
	private String expanderCollapsedText = "Prikaži još";
	
	private String expanderExpandedText = "Umanji";

	private final CollapsablePanelClientRpc clientRpc;
	
	private CollapsablePanelServerRpc serverRpc = new CollapsablePanelServerRpc() {

		private static final long serialVersionUID = 1180044443950522627L;

		@Override
		public void hasExpanded() {
			fireCollapsExpandEvent(false);
		}
		
		@Override
		public void hasCollapsed() {
			fireCollapsExpandEvent(true);
		}
	};
	
	public CollapsablePanel() {
		clientRpc = getRpcProxy(CollapsablePanelClientRpc.class);
		registerRpc(serverRpc);
		setWidth(100, Unit.PERCENTAGE);        
	}		
	
	@Override
	public CollapsablePanelState getState() {
		return (CollapsablePanelState) super.getState();
	}
	
	public void setAnimationEnabled(boolean enabled) {
		getState().isAnimationEnabled = enabled;
	}
	
	public void expand() {
		clientRpc.expand();
	}
	
	public void collapse() {
		clientRpc.collapse();
	}
	
	public void setContent(String content) {
		getState().contentHtml = content;
	}
	
	public String getContent() {
		return getState().contentHtml;
	}
	
	public void setMaxCollapsedHeight(int maxHeight) {
		getState().maxCollapsedHeight = maxHeight;
	}
	
	public static class CollapsExpandEvent extends Component.Event {

		private static final long serialVersionUID = -6562989408413288373L;
		
		private final boolean collapsed;
		
		public CollapsExpandEvent(Component source, boolean collapsed) {
			super(source);
			this.collapsed = collapsed;
		}

		public boolean isCollapsed() {
			return collapsed;
		}
		
	}
	
	public interface CollapsExpandListener extends Serializable {
		public void onCollapsExpand(CollapsExpandEvent event);
	}
	
	public void addCollapsExpandListener(CollapsExpandListener listener) {
		try {
            Method method = CollapsExpandListener.class.getDeclaredMethod("onCollapsExpand", new Class[] { CollapsExpandEvent.class });
            
            addListener(CollapsExpandEvent.class, listener, method);
            
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
	}
	
	public void removeCollapsExpandListener(CollapsExpandListener listener) {
		removeListener(CollapsExpandEvent.class, listener);
	}
	
	private void fireCollapsExpandEvent(boolean collapsed) {
		setCollapsed(collapsed);
		fireEvent(new CollapsExpandEvent(CollapsablePanel.this, collapsed));
		markAsDirty();
	}

	public boolean isCollapsed() {
		return isCollapsed;
	}

	public void setCollapsed(boolean isCollapsed) {
		this.isCollapsed = isCollapsed;
	}

	public String getExpanderCollapsedText() {
		return expanderCollapsedText;
	}

	public void setExpanderCollapsedText(String expanderCollapsedText) {
		this.expanderCollapsedText = expanderCollapsedText;
		getState().expanderCaptionCollapsed = expanderCollapsedText;
	}

	public String getExpanderExpandedText() {
		return expanderExpandedText;
	}

	public void setExpanderExpandedText(String expanderExpandedText) {
		this.expanderExpandedText = expanderExpandedText;
		getState().expanderCaptionExpanded = expanderExpandedText;
	}
	
}
