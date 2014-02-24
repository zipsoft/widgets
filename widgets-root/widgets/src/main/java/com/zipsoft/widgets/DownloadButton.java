package com.zipsoft.widgets;

import com.vaadin.ui.Button;
import com.zipsoft.widgets.client.downloadbutton.DownloadButtonClientRpc;

public class DownloadButton extends Button {
		
	private static final long serialVersionUID = -2069976836870106143L;

	@Override
	public void click() {	
		super.click();
		getRpcProxy(DownloadButtonClientRpc.class).doDownload();
	}

}
