package org.lemon.filters.transformations.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lemon.filters.transformations.PerspectivePlane;
import org.lemon.filters.transformations.VanishingPointFilter;
import org.lemon.gui.image.ChooseImage;
import org.lemon.utils.Utils;

public class TransformationToolPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	
	private JButton openFileButton;
	private JButton drawPlaneButton;
	private JButton eraserToolButton;
	private JButton brushToolButton;
	
	private JComponent context = null;
	
	private Rectangle imgBounds = null;
	private List<PerspectivePlane> persPlanes = null;
	private PerspectivePlane overlappedPersPlane = null;
	
	/*Image which user is currently dragging*/
	private BufferedImage target = null;
	private BufferedImage targetCopy = null;
	
	/*check if image is already in perspective*/
	private boolean alreadyInPerspective = false;
	
	private Graphics2D g2d;
	
	
	public TransformationToolPanel(JComponent context, Graphics2D g2d, List<PerspectivePlane> persPlanes) {
		
		this.g2d = g2d;
		this.context = context;
		this.persPlanes = persPlanes;
		
		try {
			var img = ImageIO.read(new FileInputStream(new File("C:\\Users\\Ramesh\\Desktop\\opencv\\sponge.jpg")));
			target = Utils.getImageCopy(img);
			targetCopy = Utils.getImageCopy(img);
			
			var lbl = new JLabel(new ImageIcon(img));
			
			var dml = new DragMouseHandler();
			lbl.addMouseListener(dml);
			lbl.addMouseMotionListener(dml);
			
			context.add(lbl);
			context.revalidate();
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		var boxL = new FlowLayout(FlowLayout.TRAILING, 0, 10);
		var border = BorderFactory.createLineBorder(Color.black, 1);
		
		setLayout(boxL);
		setBorder(border);
		
		add(createOpenFileButton());
		add(createDrawPlaneButton());
		add(createEraserToolButton());
		add(createBrushToolButton());
	}
	
	
	
	private JButton createOpenFileButton() {
		this.openFileButton = new JButton("Open...");
		
		openFileButton.addActionListener(action -> {
			
			var chooser = new ChooseImage();
			var img = chooser.getChoosenImage();
			
			var mh = new DragMouseHandler();
			
			var lbl = new JLabel(new ImageIcon(img));
			lbl.addMouseListener(mh);
			lbl.addMouseMotionListener(mh);
			
			context.add(lbl);
			
			context.repaint();
		});
		
		return openFileButton;
	}
	
	
	
	private JButton createDrawPlaneButton() {
		this.drawPlaneButton = new JButton();
		drawPlaneButton.setIcon(new ImageIcon("icons/tools/transformation/perspective.png"));
		
		drawPlaneButton.addActionListener(action -> {
			
		});
		
		return drawPlaneButton;
	}
	
	
	
	private JButton createEraserToolButton() {
		this.eraserToolButton = new JButton();
		eraserToolButton.setIcon(new ImageIcon("icons/tools/eraser.png"));
		return eraserToolButton;
	}
	
	
	
	private JButton createBrushToolButton() {
		this.brushToolButton = new JButton();
		brushToolButton.setIcon(new ImageIcon("icons/tools/brush.png"));
		return brushToolButton;
	}

	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(65, 500);
	}
	
	
	
	private class DragMouseHandler extends MouseAdapter {
		
		private Point offset = null;
		private BufferedImage computedImg = null;
		
		@Override
		public void mousePressed(MouseEvent e) {
			super.mousePressed(e);
			
            offset = e.getPoint();
		}
		
		
		@Override
		public void mouseDragged(MouseEvent e) {
			super.mouseDragged(e);
			
			int x = e.getPoint().x - offset.x;
            int y = e.getPoint().y - offset.y;
            Component component = e.getComponent();
            Point location = component.getLocation();
            location.x += x;
            location.y += y;
            component.setLocation(location);
            imgBounds = component.getBounds();
            
            if(checkOverlap(imgBounds, persPlanes)) {
            	
            	targetCopy = Utils.getImageCopy(target);
            	
            	if(!alreadyInPerspective) {
	            	computedImg = VanishingPointFilter.Pseudo3D.computeImage(targetCopy,
	            			overlappedPersPlane.p0, overlappedPersPlane.p1,
	            			overlappedPersPlane.p2, overlappedPersPlane.p3);
	            	alreadyInPerspective = true;
	            	System.out.println("Computing image");
	            	
//	            	try {
//	            		ImageIO.write(computedImg, "png", new File("C:\\Users\\Ramesh\\Desktop\\lalala.png"));
//	            	} catch (IOException ex) {
//	            		ex.printStackTrace();
//	            	}
            	}
            	
            	if(computedImg != null)
            		((JLabel) component).setIcon(new ImageIcon(computedImg));

            }
            else {
            	((JLabel) component).setIcon(new ImageIcon(target));
            	alreadyInPerspective = false;
            	computedImg = null;
            }
            
            component.repaint();
            
		}
		
		
		/*check if imgBounds overlaps any user perspective plane*/
		private boolean checkOverlap(Rectangle r1, List<PerspectivePlane> pplanes) {
			
			for(PerspectivePlane pp: pplanes) {
				if(r1.intersects(pp.getBound())) {
					overlappedPersPlane = pp;
					return true;
				}
			}
			
			return false;
		}
		
	}
	
	

}
