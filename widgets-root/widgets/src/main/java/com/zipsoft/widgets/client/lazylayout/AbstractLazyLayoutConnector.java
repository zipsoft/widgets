/*
 * Copyright 2012 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zipsoft.widgets.client.lazylayout;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ConnectorHierarchyChangeEvent;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractLayoutConnector;
import com.vaadin.client.ui.layout.ElementResizeEvent;
import com.vaadin.client.ui.layout.ElementResizeListener;
import com.vaadin.shared.Connector;

@SuppressWarnings("serial")
public abstract class AbstractLazyLayoutConnector extends AbstractLayoutConnector {

    private final LazyLayoutServerRpc rpc = RpcProxy.create(LazyLayoutServerRpc.class, this);

    private final ElementResizeListener elementResizeListener = new ElementResizeListener() {
        @Override
        public void onElementResize(final ElementResizeEvent e) {
            getWidget().refreshPageHeight();
            log("[LazyLayout] ElementResizeHandler - OnResize");
        }
    };

    private HandlerRegistration resizeHandler;

    @Override
    protected void init() {
        super.init();
        registerRpc(LazyLayoutClientRpc.class, new LazyLayoutClientRpc() {
                    @Override
                    public void sendComponents(final Map<Integer, Connector> components) {
                        swapLazyComponents(components);
                    }
                });

        getLayoutManager().addElementResizeListener(getWidget().getElement(), elementResizeListener);
        
        resizeHandler = Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(final ResizeEvent event) {
                getWidget().refreshPageHeight();
                log("[LazyLayout] WindowResizeHandler - OnResize");
            }
        });                
                
    }

    @Override
    public void onUnregister() {
        getLayoutManager().removeElementResizeListener(getWidget().getElement(), elementResizeListener);
        resizeHandler.removeHandler();
        super.onUnregister();
    }

    @Override
    protected VLazyLayout createWidget() {
        final VLazyLayout lazyLayout = GWT.create(VLazyLayout.class);
        
        lazyLayout.setFetcher(new VLazyLayout.ComponentFetcher() {
            
        	@Override
            public void fetchIndices(final List<Integer> indicesToFetch) {
                rpc.fetchComponentsForIndices(indicesToFetch);
            }
        	
        });
        return lazyLayout;
    }

    @Override
    public VLazyLayout getWidget() {
        return (VLazyLayout) super.getWidget();
    }

    @Override
    public LazyLayoutState getState() {
        return (LazyLayoutState) super.getState();
    }

    @Override
    public void updateCaption(final ComponentConnector connector) {
        // not supported
    }

    @Override
    public void onStateChanged(final StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        attachScrollHandlersIfNeeded();

        // order matters
        getWidget().setPlaceholderSize(getState().placeholderHeight, getState().placeholderWidth);
        getWidget().setAmountOfComponents(getState().amountOfComponents);
    }

    /** Called on the very first {@link #onStateChanged(StateChangeEvent)} call. */
    protected void onFirstStateChanged(final StateChangeEvent stateChangeEvent) {
        // don't force an implementation
    }

    private void attachScrollHandlersIfNeeded() {
//        final Widget rootWidget = getConnection().getUIConnector().getWidget();
    	final Widget rootWidget = getWidget();
    	log("[LazyLayout] root component style name:" + rootWidget.getStyleName());
//        if (VLazyLayout.DEBUG) {        	
//        	VConsole.error("[LazyLayout] root component style name:" + rootWidget.getStyleName());
//        }
        getWidget().attachScrollHandlersIfNeeded(rootWidget);
    }

    @Override
    public void onConnectorHierarchyChange(final ConnectorHierarchyChangeEvent event) {
    }

    private void swapLazyComponents(final Map<Integer, Connector> components) {
        
    	if (components == null || components.isEmpty()) {
    		log("No components to swap in (unnecessary method call)");
//            VConsole.error("No components to swap in (unnecessary method call)");
        } else {
            final Duration duration = new Duration();

            final int[] indices = new int[components.size()];
            final Widget[] widgets = new Widget[components.size()];
            int i = 0;

            for (final Map.Entry<Integer, Connector> entry : components.entrySet()) {
                
            	final int index = entry.getKey();
                final Connector connector = entry.getValue();
                
                if (connector instanceof ComponentConnector) {
                    final ComponentConnector cConnector = (ComponentConnector) connector;
                    final Widget widget = cConnector.getWidget();

                    indices[i] = index;
                    widgets[i] = widget;

                    i++;

                } else {
                	log("Expected a ComponentConnector, got something else instead (at index " + index + ")");
//                    VConsole.error("Expected a ComponentConnector, got something else instead (at index " + index + ")");
                }
            }

            getWidget().replaceComponents(indices, widgets);

            log("Replace components took "
                    + duration.elapsedMillis() + "ms (n="
                    + components.size() + ")");
//            if (VLazyLayout.DEBUG) {
//                VConsole.error("[LazyLayout] Replace components took "
//                        + duration.elapsedMillis() + "ms (n="
//                        + components.size() + ")");
//            }
        }
    }
    
    public static void log(String msg) {
        if (VLazyLayout.DEBUG) {
            Logger.getLogger("AbstractLazyLayoutConnector").log(Level.INFO, msg);
        }
    }
}
