package com.source.control;

import javafx.scene.image.ImageView;

public class FrameGrabber {
	
	
	public void refreshFrame(ImageView view) {
		RefreshFrame re = new RefreshFrame(view);
		re.run();

	}

	private class RefreshFrame implements Runnable{
		
		private ImageView view;
		
		public RefreshFrame(ImageView view) {
			this.view = view;
		}

		@Override
		public void run() {
			while(view.isVisible() || view.isDisable()) {
				
			}
		}
		
	}
}
