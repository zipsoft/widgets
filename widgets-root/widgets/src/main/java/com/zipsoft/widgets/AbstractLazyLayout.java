/*
 * Copyright 2011 Vaadin Ltd.
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.vaadin.shared.Connector;
import com.vaadin.shared.communication.SharedState;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.SelectiveRenderer;
import com.zipsoft.widgets.client.lazylayout.LazyLayoutClientRpc;
import com.zipsoft.widgets.client.lazylayout.LazyLayoutServerRpc;
import com.zipsoft.widgets.client.lazylayout.LazyLayoutState;


public abstract class AbstractLazyLayout extends AbstractLayout implements SelectiveRenderer {

	private static final long serialVersionUID = -5692267088590594285L;
	/**
     * Custom layout slots containing the components.
     */
    protected final List<Component> components = new LinkedList<Component>();
    protected final List<Connector> connectors = new LinkedList<Connector>();
    protected final Set<Component> loadedComponents = new HashSet<Component>();

    private final LazyLayoutServerRpc rpc = new LazyLayoutServerRpc() {

		private static final long serialVersionUID = -448946778419326950L;

		@Override
        public void fetchComponentsForIndices(final List<Integer> indicesToFetch) {
            loadingHook(indicesToFetch);

            final HashMap<Integer, Connector> components = new HashMap<Integer, Connector>();
            for (final Integer index : indicesToFetch) {
                final Component component = getComponent(index);

                loadedComponents.add(component);
                component.markAsDirtyRecursive();
                components.put(index, component);
            }

            markAsDirty();
            getRpc().sendComponents(components);
        }
    };

    public AbstractLazyLayout() {
        registerRpc(rpc);
    }

    abstract protected LazyLayoutClientRpc getRpc();

    protected void loadingHook(final List<Integer> indicesToFetch) {
        // Override to make useful.
    }

    @Override
    public boolean isRendered(final Component childComponent) {
        return loadedComponents.contains(childComponent);
    }

    /**
     * Add a component into this container. The component is added to the right
     * or under the previous component.
     * 
     * @param c
     *            the component to be added.
     */
    @Override
    public void addComponent(final Component c) {
        _addComponent(c, null);
    }

    public void _addComponent(final Component c, final Integer index) {
        if (index != null) {
            components.add(index, c);
            connectors.add(index, c);
        } else {
            components.add(c);
            connectors.add(c);
        }

        try {
            super.addComponent(c);
            getState().amountOfComponents = getComponentCount();
        } catch (final IllegalArgumentException e) {
            components.remove(c);
            connectors.remove(c);
            throw e;
        }
    }

    protected void addComponent(final Component component, final int index) {
        _addComponent(component, index);
    }

    /**
     * Removes the component from this container.
     * 
     * @param c
     *            the component to be removed.
     */
    @Override
    public void removeComponent(final Component c) {
        components.remove(c);
        connectors.remove(c);
        loadedComponents.remove(c);
        super.removeComponent(c);
        getState().amountOfComponents = getComponentCount();
        markAsDirty();
    }

    /**
     * Gets the component container iterator for going trough all the components
     * in the container.
     * 
     * @return the Iterator of the components inside the container.
     */
    @Override
    public Iterator<Component> getComponentIterator() {
        return components.iterator();
    }

    @Override
    public Iterator<Component> iterator() {
        return getComponentIterator();
    }

    /**
     * Gets the number of contained components. Consistent with the iterator
     * returned by {@link #getComponentIterator()}.
     * 
     * @return the number of contained components
     */
    @Override
    public int getComponentCount() {
        return components.size();
    }

    @Override
    public LazyLayoutState getState() {
        return (LazyLayoutState) super.getState();
    }

    /**
     * Returns styles to be applied to given component. Override this method to
     * inject custom style rules to components.
     * 
     * <p>
     * Note that styles are injected over previous styles before actual child
     * rendering. Previous styles are not cleared, but overridden.
     * 
     * <p>
     * Note that one most often achieves better code style, by separating
     * styling to theme (with custom theme and {@link #addStyleName(String)}.
     * With own custom styles it is also very easy to break browser
     * compatibility.
     * 
     * @param c
     *            the component
     * @return css rules to be applied to component
     */
    protected String getCss(final Component c) {
        return null;
    }

    @Override
    public abstract void replaceComponent(Component oldComponent, Component newComponent);

    protected void _replaceComponent(final int oldIndex, final Component newComponent) {
        final Component oldComponent = components.remove(oldIndex);
        components.add(oldIndex, newComponent);

        connectors.remove(oldIndex);
        connectors.add(oldIndex, newComponent);

        super.removeComponent(oldComponent);
        super.addComponent(newComponent);
        markAsDirty();
    }

    /**
     * Returns the index of the given component.
     * 
     * @param component
     *            The component to look up.
     * @return The index of the component or -1 if the component is not a child.
     */
    public int getComponentIndex(final Component component) {
        return components.indexOf(component);
    }

    /**
     * Returns the component at the given position.
     * 
     * @param index
     *            The position of the component.
     * @return The component at the given index.
     * @throws IndexOutOfBoundsException
     *             If the index is out of range.
     */
    public Component getComponent(final int index) throws IndexOutOfBoundsException {
        return components.get(index);
    }

    public void setPlaceholderSize(final String placeholderHeight, final String placeholderWidth) {
        getState().placeholderHeight = placeholderHeight;
        getState().placeholderWidth = placeholderWidth;
    }

    /** How far the rendering should occur past the page length */
    public void setRenderDistanceMultiplier(final double renderDistanceMultiplier) {
        getState().renderDistanceMultiplier = renderDistanceMultiplier;
    }

    public void setRenderDelay(final int renderDelayMillis) {
        getState().renderDelay = renderDelayMillis;
    }

    @Override
    public Class<? extends SharedState> getStateType() {
        return LazyLayoutState.class;
    }
}
