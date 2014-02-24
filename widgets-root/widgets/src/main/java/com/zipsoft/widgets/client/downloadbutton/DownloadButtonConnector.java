package com.zipsoft.widgets.client.downloadbutton;

import com.google.gwt.dom.client.Element;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import com.vaadin.shared.ui.Connect.LoadStyle;
import com.zipsoft.widgets.DownloadButton;

@Connect(value = DownloadButton.class, loadStyle = LoadStyle.EAGER)
public class DownloadButtonConnector extends ButtonConnector {

	private static final long serialVersionUID = 2929613964408798121L;
	
	public DownloadButtonConnector() {
		registerRpc(DownloadButtonClientRpc.class, new DownloadButtonClientRpc() {
			
			private static final long serialVersionUID = -8938979861127530294L;

			@Override
			public void doDownload() {				
//				getWidget().fireEvent(new com.google.gwt.event.dom.client.ClickEvent(){});
				clickElement(getWidget().getElement());
				
			}
		});
	}
	
	public static native void clickElement(Element elem) /*-{
    	elem.click();
	}-*/;
	
	@Override
	public VButton getWidget() {	
		return super.getWidget();
	}

}
