package com.zipsoft.widgets.client.collapsablepanel;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.HasOpenHandlers;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class VCollapsablePanel extends Composite implements HasAnimation,
		HasOpenHandlers<VCollapsablePanel>, HasCloseHandlers<VCollapsablePanel>, ResizeHandler {

	/**
	 * Used to wrap widgets in the header to provide click support. Effectively
	 * wraps the widget in an <code>anchor</code> to get automatic keyboard
	 * access.
	 */
	private final class ClickablePanel extends SimplePanel {

		private ClickablePanel() {
			// Anchor is used to allow keyboard access.
			super(DOM.createAnchor());
			Element elem = getElement();
			DOM.setElementProperty(elem, "href", "javascript:void(0);");
			// Avoids layout problems from having blocks in inlines.
			DOM.setStyleAttribute(elem, "display", "block");
			sinkEvents(Event.ONCLICK);
			setStyleName(STYLENAME_EXPANDER);
		}
		
		public ClickablePanel(String text) {
			this();
			Element element = getElement();
			element.setInnerText(text);
		}
		
		public void setText(String text) {
			getElement().setInnerText(text);
		}

		@Override
		public void onBrowserEvent(Event event) {
			// no need to call super.
			switch (DOM.eventGetType(event)) {
			case Event.ONCLICK:
				// Prevent link default action.
				DOM.eventPreventDefault(event);
				setOpen(!isOpen);
			}
		}
	}

	/**
	 * An {@link Animation} used to open the content.
	 */
	private static class ContentAnimation extends Animation {
		/**
		 * Whether the item is being opened or closed.
		 */
		private boolean opening;

		/**
		 * The {@link VCollapsablePanel} being affected.
		 */
		private VCollapsablePanel curPanel;

		/**
		 * Open or close the content.
		 * 
		 * @param panel
		 *            the panel to open or close
		 * @param animate
		 *            true to animate, false to open instantly
		 */
		public void setOpen(VCollapsablePanel panel, boolean animate) {
			// Immediately complete previous open
			cancel();

			// Open the new item
			if (animate) {
				curPanel = panel;
				opening = panel.isOpen;
				run(ANIMATION_DURATION);
			} else {
//				panel.contentWrapper.setVisible(panel.isOpen);
				if (panel.isOpen) {
					// Special treatment on the visible case to ensure LazyPanel
					// works
					DOM.setStyleAttribute(panel.contentWrapper.getElement(), "maxHeight", "auto");
//					panel.getContent().setVisible(true);
				} else {
					DOM.setStyleAttribute(panel.contentWrapper.getElement(), "maxHeight", panel.getMaxCollapsedHeight() + "px");
				}
			}
		}

		@Override
		protected void onComplete() {
			if (!opening) {
//				curPanel.contentWrapper.setVisible(false);
				DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "height", "");
				DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "maxHeight", curPanel.getMaxCollapsedHeight() + "px");
			} else {
				DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "height", "auto");
				DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "maxHeight", "");
			}
			curPanel = null;
		}

		@Override
		protected void onStart() {
			super.onStart();
			if (opening) {
//				curPanel.contentWrapper.setVisible(true);
				// Special treatment on the visible case to ensure LazyPanel
				// works
//				curPanel.content.setVisible(true);
//				curPanel.getContent().setVisible(true);
				DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "maxHeight", "");
			}
		}

		@Override
		protected void onUpdate(double progress) {
			int scrollHeight = DOM.getElementPropertyInt(curPanel.contentWrapper.getElement(), "scrollHeight");

			int height;
			if (opening) {
				//ako se otvara - visina objekta je vec maksimalna visina
				int delta = (int) (progress * (scrollHeight - curPanel.maxCollapsedHeight));
				delta = Math.max(delta, 1);
				height = curPanel.maxCollapsedHeight + delta;
			} else {
				//ako se zatvara - nulta vrednost je maxHeight - trenutna vrednost je nepoznata - scrollHeight
				int delta = (int) (progress * (scrollHeight - curPanel.maxCollapsedHeight));
				delta = Math.max(delta, 1);
				height = scrollHeight - delta;
			}
			
//			originalni kod
//			int height = (int) (progress * scrollHeight);
//			if (!opening) {
//				height = scrollHeight - curPanel.maxCollapsedHeight - height;
//			}
//			height = Math.max(height, 1);		

			DOM.setStyleAttribute(curPanel.contentWrapper.getElement(),	"height", height + "px");
			DOM.setStyleAttribute(curPanel.contentWrapper.getElement(), "width", "auto");
		}
	}

	/**
	 * The duration of the animation.
	 */
	private static final int ANIMATION_DURATION = 350;

	// Stylename constants.
	private static final String STYLENAME_DEFAULT = "v-collapsable-panel";

	private static final String STYLENAME_SUFFIX_OPEN = "open";

	private static final String STYLENAME_SUFFIX_CLOSED = "closed";

	private static final String STYLENAME_EXPANDER = "v-collapsable-panel-expander";

	private static final String STYLENAME_CONTENT = "v-collapsable-panel-content";		

	/**
	 * The {@link Animation} used to open and close the content.
	 */
	private static ContentAnimation contentAnimation;

	/**
	 * top level widget. The first child will be a reference to
	 * {@link #expander}. The second child will be a reference to
	 * {@link #contentWrapper}.
	 */
	private final HTMLPanel mainPanel = new HTMLPanel("");

	/**
	 * The wrapper around the content widget.
	 */
	private final SimplePanel contentWrapper = new SimplePanel();

	/**
	 * holds the header widget.
	 */
	private final ClickablePanel expander = new ClickablePanel("Pogledaj više");
	
	private final HTML content = new HTML();

	private boolean isAnimationEnabled = true;

	private boolean isOpen = false;
	
	private String expandedCaption = "Umanji";
	
	private String collapsedCaption = "Pogledaj još";
	
	private int maxCollapsedHeight = 100;

	public VCollapsablePanel() {
		initWidget(mainPanel);
		content.setStyleName(STYLENAME_CONTENT);
		contentWrapper.add(content);
		mainPanel.add(contentWrapper);
		mainPanel.add(expander);
		DOM.setStyleAttribute(contentWrapper.getElement(), "padding", "0px");
		DOM.setStyleAttribute(contentWrapper.getElement(), "overflow", "hidden");
		DOM.setStyleAttribute(contentWrapper.getElement(), "maxHeight", maxCollapsedHeight + "px");
		setStyleName(STYLENAME_DEFAULT);
		setContentDisplay(false);
		Window.addResizeHandler(this);
	}
	
	@Override
	public void onResize(ResizeEvent event) {		
		setExpanderVisibility();
	}
	
	@Override
	protected void onAttach() {	
		super.onAttach();				
	}
			
	@Override
	protected void onLoad() {		
		super.onLoad();	
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				setExpanderVisibility();				
			}
		});
	}
	
	public void setExpanderVisibility() {
		int currentHeight = contentWrapper.getElement().getScrollHeight();
		if (!isOpen) {
			/*
			 * Ako je nije otvoren - ima maxHeight
			 */
			if (currentHeight <= maxCollapsedHeight) {
				DOM.setStyleAttribute(expander.getElement(), "display", "none");
			} else {
				DOM.setStyleAttribute(expander.getElement(), "display", "block");
			}
		}
	}
	
	@Override
	protected void onDetach() {	
		super.onDetach();		
	}

	/**
	 * Changes the visible state of this <code>DisclosurePanel</code>.
	 * 
	 * @param isOpen
	 *            <code>true</code> to open the panel, <code>false</code> to
	 *            close
	 */
	public void setOpen(boolean isOpen) {
		if (this.isOpen != isOpen) {
			this.isOpen = isOpen;
			setContentDisplay(true);
			fireEvent();
		}
	}


	@Override
	public HandlerRegistration addCloseHandler(
			CloseHandler<VCollapsablePanel> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	@Override
	public HandlerRegistration addOpenHandler(
			OpenHandler<VCollapsablePanel> handler) {
		return addHandler(handler, OpenEvent.getType());
	}

	@Override
	public boolean isAnimationEnabled() {		
		return isAnimationEnabled;
	}

	@Override
	public void setAnimationEnabled(boolean enable) {
		isAnimationEnabled = enable;
	}

	
	
	public boolean isOpen() {
		return isOpen;
	}

	/**
	 * Gets the widget that was previously set in {@link #setContent(Widget)}.
	 * 
	 * @return the panel's current content widget
	 */
	public Widget getContent() {
		return contentWrapper.getWidget();
	}
	
	 
	public void setContentHtml(String html) {
		if (html == null) {
			content.setText("");
		} else {
			content.setHTML(html);
		}
	}
	
	/**
	 * <b>Affected Elements:</b>
	 * <ul>
	 * <li>-header = the clickable header.</li>
	 * </ul>
	 * 
	 * @see UIObject#onEnsureDebugId(String)
	 */
	@Override
	protected void onEnsureDebugId(String baseID) {
		super.onEnsureDebugId(baseID);
		expander.ensureDebugId(baseID + "-expander");
	}

	private void fireEvent() {
		if (isOpen) {
			OpenEvent.fire(this, this);
		} else {
			CloseEvent.fire(this, this);
		}
	}

	private void setContentDisplay(boolean animate) {
		if (isOpen) {
			removeStyleDependentName(STYLENAME_SUFFIX_CLOSED);
			addStyleDependentName(STYLENAME_SUFFIX_OPEN);
			expander.setText(this.expandedCaption);
		} else {
			removeStyleDependentName(STYLENAME_SUFFIX_OPEN);
			addStyleDependentName(STYLENAME_SUFFIX_CLOSED);
			expander.setText(this.collapsedCaption);
		}

		if (getContent() != null) {
			if (contentAnimation == null) {
				contentAnimation = new ContentAnimation();
			}
			contentAnimation.setOpen(this, animate && isAnimationEnabled);
		}
	}

	public String getExpandedCaption() {
		return expandedCaption;
	}

	public void setExpandedCaption(String expandedCaption) {
		this.expandedCaption = expandedCaption;
		if (isOpen) {
			expander.setText(this.expandedCaption);
		}
	}

	public String getCollapsedCaption() {
		return collapsedCaption;		
	}

	public void setCollapsedCaption(String collapsedCaption) {
		this.collapsedCaption = collapsedCaption;
		if (!isOpen) {
			expander.setText(this.collapsedCaption);
		}
	}

	public int getMaxCollapsedHeight() {
		return maxCollapsedHeight;
	}

	public void setMaxCollapsedHeight(int maxCollapsedHeight) {
		this.maxCollapsedHeight = maxCollapsedHeight;
		if (!isOpen) {
			DOM.setStyleAttribute(contentWrapper.getElement(), "maxHeight", maxCollapsedHeight + "px");
		}
	}

	

}
