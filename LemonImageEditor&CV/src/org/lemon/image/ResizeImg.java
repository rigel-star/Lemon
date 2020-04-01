package org.lemon.image;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ResizeImg {

	private BufferedImage img;
	
	public ResizeImg(BufferedImage img) {
		this.img = img;
	}
	
	public BufferedImage getScaledImage() {
		
		BufferedImage out = new BufferedImage(this.img.getWidth(), this.img.getHeight(), this.img.getType());
		
		if(img.getHeight() < 600 && img.getWidth() < 600)
			return this.img;
		
		Graphics2D g = out.createGraphics();
        AffineTransform at = AffineTransform.getScaleInstance(0.7, 0.7);
        g.drawRenderedImage(this.img, at);
		
		return out;
	}
}
