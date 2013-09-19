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

package com.zipsoft.widgets;

import java.util.Collections;

import com.vaadin.shared.Connector;
import com.vaadin.ui.Component;
import com.zipsoft.widgets.client.lazylayout.LazyLayoutClientRpc;

@SuppressWarnings("serial")
public class LazyLayout extends AbstractLazyLayout {
	
	public static final int RENDER_DELAY_MILLIS = 500;
    public static final double RENDER_DISTANCE_MULTIPLIER = 2.0d;
    public static final String PLACEHOLDER_WIDTH = "100%";
    public static final String PLACEHOLDER_HEIGHT = "50px";
    
    public static final int PRELOAD_THRESHHOLD = 4;
    
    public LazyLayout() {
    	super();
    	setRenderDistanceMultiplier(RENDER_DISTANCE_MULTIPLIER);
		setPlaceholderSize(PLACEHOLDER_HEIGHT, PLACEHOLDER_WIDTH);
        setRenderDelay(RENDER_DELAY_MILLIS);
    }
	
    @Override
    protected LazyLayoutClientRpc getRpc() {
        return getRpcProxy(LazyLayoutClientRpc.class);
    }

    public void addComponentEagerly(final Component c) {
        addComponent(c);
        loadedComponents.add(c);

        /*
         * TODO: this maybe needs to be optimized so that it's not individual
         * rpc calls, but a queue that gets built and sent over as a state
         * change.
         */
        getRpcProxy(LazyLayoutClientRpc.class).sendComponents(
                Collections.singletonMap(components.indexOf(c), (Connector) c));
    }

    @Override
    public void replaceComponent(final Component oldComponent, final Component newComponent) {

        if (oldComponent == newComponent) {
            return;
        }

        if (!components.contains(oldComponent)) {
            throw new IllegalArgumentException("old component " + oldComponent
                    + " isn't in the layout");
        }

        final int oldIndex = components.indexOf(oldComponent);
        final boolean oldIsLoaded = loadedComponents.contains(oldComponent);

        if (components.contains(newComponent)) {
            final int newIndex = components.indexOf(newComponent);
            final boolean newIsLoaded = loadedComponents.contains(newComponent);

            components.remove(oldIndex);
            components.add(oldIndex, newComponent);
            components.remove(newIndex);
            components.add(newIndex, oldComponent);

            if (oldIsLoaded || newIsLoaded) {
                loadedComponents.add(oldComponent);
                loadedComponents.add(newComponent);
            }
        } else {
            components.remove(oldComponent);
            components.add(oldIndex, newComponent);

            super.addComponent(newComponent, oldIndex);
            super.removeComponent(oldComponent);

            if (oldIsLoaded) {
                loadedComponents.add(newComponent);
                loadedComponents.remove(oldComponent);
            }
        }

        markAsDirty();
    }
}
