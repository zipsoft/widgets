package com.zipsoft.widgets;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickNotifier;
import com.vaadin.shared.Connector;
import com.vaadin.shared.EventId;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.zipsoft.widgets.client.focuscsslayout.FocusCssLayoutClientRpc;
import com.zipsoft.widgets.client.focuscsslayout.FocusCssLayoutServerRpc;
import com.zipsoft.widgets.client.focuscsslayout.FocusCssLayoutState;

public class FocusCssLayout extends AbstractLayout implements LayoutClickNotifier {

	private static final long serialVersionUID = -5272336944177385809L;
	
	private final FocusCssLayoutClientRpc focusCssLayoutClientRpc;
	
	private FocusCssLayoutServerRpc rpc = new FocusCssLayoutServerRpc() {
		
		private static final long serialVersionUID = -2442397289953859039L;

		@Override
		public void layoutClick(MouseEventDetails mouseDetails,
				Connector clickedConnector) {
			fireEvent(LayoutClickEvent.createEvent(FocusCssLayout.this,
                    mouseDetails, clickedConnector));
			
		}
		
		@Override
		public void focused() {
			setFocused(true);
//			fireEvent(new FocusEvent(FocusCssLayout.this));
			
		}
		
		@Override
		public void blurred() {
			setFocused(false);
//			fireEvent(new BlurEvent(FocusCssLayout.this));
		}
	};
	
	 /**
     * Custom layout slots containing the components.
     */
    protected LinkedList<Component> components = new LinkedList<Component>();
    
    
    private boolean isFocused = false;
    
    public FocusCssLayout() {
    	focusCssLayoutClientRpc = getRpcProxy(FocusCssLayoutClientRpc.class);
    	registerRpc(rpc);
    }
    
    /**
     * Constructs a FocusCssLayout with the given components in the given order.
     * 
     * @see #addComponents(Component...)
     * 
     * @param children
     *            Components to add to the container.
     */
    public FocusCssLayout(Component... children) {
        this();
        addComponents(children);
    }
    
    @Override
    public void addComponent(Component c) {
    	// Add to components before calling super.addComponent
        // so that it is available to AttachListeners
        components.add(c);
        try {
            super.addComponent(c);
            markAsDirty();
        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }
    
    /**
     * Adds a component into this container. The component is added to the left
     * or on top of the other components.
     * 
     * @param c
     *            the component to be added.
     */
    public void addComponentAsFirst(Component c) {
        // If c is already in this, we must remove it before proceeding
        // see ticket #7668
        if (c.getParent() == this) {
            removeComponent(c);
        }
        components.addFirst(c);
        try {
            super.addComponent(c);
            markAsDirty();
        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }
    
    /**
     * Adds a component into indexed position in this container.
     * 
     * @param c
     *            the component to be added.
     * @param index
     *            the index of the component position. The components currently
     *            in and after the position are shifted forwards.
     */
    public void addComponent(Component c, int index) {
        // If c is already in this, we must remove it before proceeding
        // see ticket #7668
        if (c.getParent() == this) {
            // When c is removed, all components after it are shifted down
            if (index > getComponentIndex(c)) {
                index--;
            }
            removeComponent(c);
        }
        components.add(index, c);
        try {
            super.addComponent(c);
            markAsDirty();
        } catch (IllegalArgumentException e) {
            components.remove(c);
            throw e;
        }
    }
    
    @Override
    public void removeComponent(Component c) {
    	components.remove(c);
        super.removeComponent(c);
        markAsDirty();
    }
    
    @Override
    protected FocusCssLayoutState getState() {
    	return (FocusCssLayoutState) super.getState();
    }

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		 // Gets the locations
        int oldLocation = -1;
        int newLocation = -1;
        int location = 0;
        for (final Iterator<Component> i = components.iterator(); i.hasNext();) {
            final Component component = i.next();

            if (component == oldComponent) {
                oldLocation = location;
            }
            if (component == newComponent) {
                newLocation = location;
            }

            location++;
        }

        if (oldLocation == -1) {
            addComponent(newComponent);
        } else if (newLocation == -1) {
            removeComponent(oldComponent);
            addComponent(newComponent, oldLocation);
        } else {
            if (oldLocation > newLocation) {
                components.remove(oldComponent);
                components.add(newLocation, oldComponent);
                components.remove(newComponent);
                components.add(oldLocation, newComponent);
            } else {
                components.remove(newComponent);
                components.add(oldLocation, newComponent);
                components.remove(oldComponent);
                components.add(newLocation, oldComponent);
            }

            markAsDirty();
        }
		
	}

	@Override
	public int getComponentCount() {
		return components.size();
	}

	@Override
	public Iterator<Component> iterator() {
		return components.iterator();
	}

	@Override
	public void addLayoutClickListener(LayoutClickListener listener) {
		addListener(EventId.LAYOUT_CLICK_EVENT_IDENTIFIER,
                LayoutClickEvent.class, listener,
                LayoutClickListener.clickMethod);
		
	}

	@Deprecated
	@Override
	public void addListener(LayoutClickListener listener) {
		
	}

	@Override
	public void removeLayoutClickListener(LayoutClickListener listener) {
		 removeListener(EventId.LAYOUT_CLICK_EVENT_IDENTIFIER,
	                LayoutClickEvent.class, listener);
		
	}

	@Deprecated
	@Override
	public void removeListener(LayoutClickListener listener) {		
		
	}
	
	/**
     * Returns the index of the given component.
     * 
     * @param component
     *            The component to look up.
     * @return The index of the component or -1 if the component is not a child.
     */
    public int getComponentIndex(Component component) {
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
    public Component getComponent(int index) throws IndexOutOfBoundsException {
        return components.get(index);
    }

	public boolean isFocused() {
		return isFocused;
	}

	public void setFocused(boolean isFocused) {
		this.isFocused = isFocused;
		if (isFocused) {
//			setFocused(true);
			fireEvent(new FocusEvent(FocusCssLayout.this));
		} else {
//			focusCssLayoutClientRpc.blur();
			fireEvent(new BlurEvent(FocusCssLayout.this));
		}
	}
	
	@Override
	protected void focus() {
//		super.focus();
		setFocused(true);		
	}
	
	
	public static class FocusEvent extends Component.Event {

		private static final long serialVersionUID = -4761220955074383721L;

		public FocusEvent(Component source) {
			super(source);		
		}
		
	}
	
	public static class BlurEvent extends Component.Event {

		private static final long serialVersionUID = -7778799391542466810L;

		public BlurEvent(Component source) {
			super(source);
		}
		
	}
	
	public interface FocusEventListener extends Serializable {
		public void onFocus(FocusEvent event);
	}
	
	public interface BlurEventListener extends Serializable {
		public void onBlur(BlurEvent event);
	}
	
	public void addFocusEventListener(FocusEventListener listener) {
		try {
            Method method = FocusEventListener.class.getDeclaredMethod("onFocus", new Class[] { FocusEvent.class });
            
            addListener(FocusEvent.class, listener, method);
            
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
	}
	
	public void removeFocusEventListener(FocusEventListener listener) {
		removeListener(FocusEvent.class, listener);
	}
	
	public void addBlurEventListener(BlurEventListener listener) {
		try {
            Method method = BlurEventListener.class.getDeclaredMethod("onBlur", new Class[] { BlurEvent.class });
            
            addListener(BlurEvent.class, listener, method);
            
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error, editor saved method not found");
        }
	}
	
	public void removeBlurEventListener(BlurEventListener listener) {
		removeListener(BlurEvent.class, listener);
	}

}
