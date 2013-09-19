package com.zipsoft.widgets.client.expandingtextarea;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.VTextArea;
import com.vaadin.client.ui.textarea.TextAreaConnector;
import com.vaadin.shared.ui.Connect;
import com.zipsoft.widgets.ExpandingTextArea;
import com.zipsoft.widgets.client.expandingtextarea.VExpandingTextArea.HeightChangedListener;


@Connect(ExpandingTextArea.class)
public class ExpandingTextAreaConnector extends TextAreaConnector implements HeightChangedListener {

	private static final long serialVersionUID = -7155139500776202020L;
	
	private ExpandingTextAreaServerRpc rpc = RpcProxy.create(ExpandingTextAreaServerRpc.class, this);
	
	public ExpandingTextAreaConnector() {
		super();		
	}
	
    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (uidl.hasAttribute("maxRows")) {
            getWidget().setMaxRows(uidl.getIntAttribute("maxRows"));
        } else {
        	getWidget().setMaxRows(null);
        }

        getWidget().addStyleName(VTextArea.CLASSNAME);

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        	public void execute() {
        		getWidget().checkHeight();
        	}
        });
    }

    @Override
    protected VExpandingTextArea createWidget() {
    	VExpandingTextArea widget = GWT.create(VExpandingTextArea.class);
    	widget.addHeightChangedListener(this);
        return widget;
    }

    @Override
    public VExpandingTextArea getWidget() {
        return (VExpandingTextArea) super.getWidget();
    }

	public void heightChanged(int newHeight) {
		rpc.setRows(newHeight);
		getLayoutManager().setNeedsMeasure(this);
	}

}
