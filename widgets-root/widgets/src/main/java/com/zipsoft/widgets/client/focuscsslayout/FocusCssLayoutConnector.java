package com.zipsoft.widgets.client.focuscsslayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.FastStringMap;
import com.vaadin.client.Profiler;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.LayoutClickEventHandler;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.LayoutClickRpc;
import com.zipsoft.widgets.FocusCssLayout;

@Connect(FocusCssLayout.class)
public class FocusCssLayoutConnector extends AbstractLayoutConnector {

	private static final long serialVersionUID = -6263170214230863666L;
	
	private FocusCssLayoutServerRpc rpc = RpcProxy.create(FocusCssLayoutServerRpc.class, this);
	
	private LayoutClickEventHandler clickEventHandler = new LayoutClickEventHandler(this) {

        @Override
        protected ComponentConnector getChildComponent(Element element) {
            return Util.getConnectorForElement(getConnection(), getWidget(),
                    element);
        }

        @Override
        protected LayoutClickRpc getLayoutClickRPC() {
            return getRpcProxy(FocusCssLayoutServerRpc.class);
        };
    };

    private final FastStringMap<VCaption> childIdToCaption = FastStringMap.create();
    
    @Override
    protected void init() {
    	super.init();
    	
    	registerRpc(FocusCssLayoutClientRpc.class, new FocusCssLayoutClientRpc() {
			private static final long serialVersionUID = 1L;

			@Override
			public void focus() {
//				getWidget().setFocus(true);
				rpc.focused();
			}

			@Override
			public void blur() {
//				getWidget().setFocus(false);
				rpc.blurred();
			}
		});
    	
    	getWidget().addFocusHandler(new FocusHandler() {
			
			@Override
			public void onFocus(FocusEvent event) {
				rpc.focused();				
			}
		});
    	
    	getWidget().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				rpc.blurred();				
			}
		});
    }
    
    
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {    
    	super.onStateChanged(stateChangeEvent);
    	clickEventHandler.handleEventHandlerRegistration();

        for (ComponentConnector child : getChildComponents()) {
            if (!getState().childCss.containsKey(child)) {
                continue;
            }
            String css = getState().childCss.get(child);
            Style style = child.getWidget().getElement().getStyle();
            // should we remove styles also? How can we know what we have added
            // as it is added directly to the child component?
            String[] cssRules = css.split(";");
            for (String cssRule : cssRules) {
                String parts[] = cssRule.split(":");
                if (parts.length == 2) {
                    style.setProperty(makeCamelCase(parts[0].trim()),
                            parts[1].trim());
                }
            }
        }
    }
    
    /**
     * Converts a css property string to CamelCase
     * 
     * @param cssProperty
     *            The property string
     * @return A string converted to camelcase
     */
    private static final String makeCamelCase(String cssProperty) {
        // TODO this might be cleaner to implement with regexp
        while (cssProperty.contains("-")) {
            int indexOf = cssProperty.indexOf("-");
            cssProperty = cssProperty.substring(0, indexOf)
                    + String.valueOf(cssProperty.charAt(indexOf + 1))
                            .toUpperCase() + cssProperty.substring(indexOf + 2);
        }
        if ("float".equals(cssProperty)) {
            if (BrowserInfo.get().isIE()) {
                return "styleFloat";
            } else {
                return "cssFloat";
            }
        }
        return cssProperty;
    }

	@Override
	public void updateCaption(ComponentConnector child) {
		Widget childWidget = child.getWidget();
        int widgetPosition = getWidget().getWidgetIndex(childWidget);

        String childId = child.getConnectorId();
        VCaption caption = childIdToCaption.get(childId);
        if (VCaption.isNeeded(child.getState())) {
            if (caption == null) {
                caption = new VCaption(child, getConnection());
                childIdToCaption.put(childId, caption);
            }
            if (!caption.isAttached()) {
                // Insert caption at widget index == before widget
                getWidget().insert(caption, widgetPosition);
            }
            caption.updateCaption();
        } else if (caption != null) {
            childIdToCaption.remove(childId);
            getWidget().remove(caption);
        }						
	}

	@Override
	public void onConnectorHierarchyChange(ConnectorHierarchyChangeEvent event) {
		Profiler.enter("FocusCssLayoutConnector.onConnectorHierarchyChange");
        Profiler.enter("FocusCssLayoutConnector.onConnectorHierarchyChange add children");
        int index = 0;
        for (ComponentConnector child : getChildComponents()) {
            VCaption childCaption = childIdToCaption
                    .get(child.getConnectorId());
            if (childCaption != null) {
                getWidget().addOrMove(childCaption, index++);
            }
            getWidget().addOrMove(child.getWidget(), index++);
        }
        Profiler.leave("FocusCssLayoutConnector.onConnectorHierarchyChange add children");

        // Detach old child widgets and possibly their caption
        Profiler.enter("FocusCssLayoutConnector.onConnectorHierarchyChange remove old children");
        for (ComponentConnector child : event.getOldChildren()) {
            if (child.getParent() == this) {
                // Skip current children
                continue;
            }
            getWidget().remove(child.getWidget());
            VCaption vCaption = childIdToCaption.get(child.getConnectorId());
            if (vCaption != null) {
                childIdToCaption.remove(child.getConnectorId());
                getWidget().remove(vCaption);
            }
        }
        Profiler.leave("FocusCssLayoutConnector.onConnectorHierarchyChange remove old children");
        Profiler.leave("FocusCssLayoutConnector.onConnectorHierarchyChange");
		
	}
	
	@Override
	protected Widget createWidget() {	
		return GWT.create(VFocusCssLayout.class);
	}
	
	@Override
	public FocusCssLayoutState getState() {
		return (FocusCssLayoutState) super.getState();
	}
	
	@Override
	public VFocusCssLayout getWidget() {
		return (VFocusCssLayout) super.getWidget();
	}

}
