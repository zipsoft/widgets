package com.zipsoft.widgets.client.focuscsslayout;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.Profiler;
import com.vaadin.client.StyleConstants;
import com.vaadin.client.ui.FocusableFlowPanel;

public class VFocusCssLayout extends FocusableFlowPanel {
	
	public static final String CLASSNAME = "v-focuscsslayout";
	
	public VFocusCssLayout() {		 
        super();
        setStyleName(CLASSNAME);
        addStyleName(StyleConstants.UI_LAYOUT);		   
	}
	
	/**
     * For internal use only. May be removed or replaced in the future.
     */
    public void addOrMove(Widget child, int index) {
        Profiler.enter("VFocusCssLayout.addOrMove");
        if (child.getParent() == this) {
            Profiler.enter("VFocusCssLayout.addOrMove getWidgetIndex");
            int currentIndex = getWidgetIndex(child);
            Profiler.leave("VFocusCssLayout.addOrMove getWidgetIndex");
            if (index == currentIndex) {
                Profiler.leave("VFocusCssLayout.addOrMove");
                return;
            }
        } else if (index == getWidgetCount()) {
            // optimized path for appending components - faster especially for
            // initial rendering
            Profiler.enter("VFocusCssLayout.addOrMove add");
            add(child);
            Profiler.leave("VFocusCssLayout.addOrMove add");
            Profiler.leave("VFocusCssLayout.addOrMove");
            return;
        }
        Profiler.enter("VFocusCssLayout.addOrMove insert");
        insert(child, index);
        Profiler.leave("VFocusCssLayout.addOrMove insert");
        Profiler.leave("VFocusCssLayout.addOrMove");
    }

}
