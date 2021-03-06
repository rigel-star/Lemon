package org.lemon.gui.image;

import java.awt.Component;
import java.awt.MouseInfo;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.lemon.gui.ImageView;
import org.lemon.tools.brush.gui.BrushGUI;



/**
 * Written for ImageView class. When right button clicked on ImageView 
 * this popup menu will show up.
 * */

public class ImageViewMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	/*container*/
	private ImageView context;
	
	
	private JMenu connect, options;
	private JMenuItem brushes, duplicate, delete;
	
	
	public ImageViewMenu(ImageView context) {
		
		this.context = context;
		
		connect = createConnectionMenu();
		options = createOptionsMenu();
		brushes = createBrushMenu();
		duplicate = createDuplicateMenu();
		delete = createDeleteMenu();
		
		
		add(connect);
		add(options);
		add(brushes);
		add(duplicate);
		add(delete);
		
	}

	
	/**
	 * Creates the option menu for this current ImageView (eg. Blend option, share pixels option etc)
	 * */
	private JMenu createOptionsMenu() {
		var options = new JMenu("Options...");
		
		var sharePixel = new JMenuItem("Share pixels");
		sharePixel.addActionListener(action -> {
			//new SharePixelsDialog(this.context.getActualImage(), this.context.getConnection().getActualImage());
		});
		
		var blend = new JMenuItem("Blend");
		blend.addActionListener(action -> {
			
		});
		
		var disconnect = new JMenuItem("Disconnect");
		disconnect.addActionListener(a -> {
			this.context.setConnection(null);
			this.context.revalidate();
		});
		
		options.add(sharePixel);
		options.add(blend);
		options.add(disconnect);
		
		if(context.getConnection() == null)
			options.setEnabled(false);
		
		return options;
	}
	
	
	
	/**
	 * Creates connection menu i.e lists all the available ImageView and DrawingCanvas for this current ImageView.
	 * */
	private JMenu createConnectionMenu() {
		var connect = new JMenu("Connect...");
		
		/*For each connection option*/
		for(int i=0; i<context.getConOptionsTitles().size(); i++) {
			var st = context.getConOptionsTitles().get(i);
			var item = new JCheckBoxMenuItem(st);
			
			if(context.getConOptionsViews().get(i) != context)
				connect.add(item);
			//connect.setComponentZOrder(item, i);
		}
		
		
		/*On connect items click*/
		for(int i=0;i<connect.getItemCount(); i++) {
			var item = (JCheckBoxMenuItem) connect.getItem(i);
			item.addActionListener(action -> {
				
				if(context.getConnection() != null) {
					JOptionPane.showMessageDialog(context, "Already in connection");
					return;
				}
				
				String itemTitle = item.getText();
				int pos = context.getConOptionsTitles().indexOf(itemTitle);
				if(context.setConnection(context.getConOptionsViews().get(pos))) {
					options.setEnabled(true);
				}
				revalidate();
			});
		}
		
		
		return connect;
	}
	
	
	/**
	 * Option to delete this ImageView
	 * */
	private JMenuItem createDeleteMenu() {
		var delete = new JMenuItem("Delete...");
		
		delete.addActionListener(a -> {
			this.context.dispose();
		});
		return delete;
	}
	
	
	
	/**
	 * BrushMenu 
	 * */
	private JMenuItem createBrushMenu() {
		var brush = new JMenuItem("Brushes...");
		
		if( context.getImagePanel().getPanelMode() == ImagePanel.PanelMode.CANVAS_MODE) {
			brush.addActionListener(action -> {
				var mouse = MouseInfo.getPointerInfo().getLocation();
				//new BrushToolOptions(context, mouse.x, mouse.y);
				new BrushGUI(context, mouse.x, mouse.y);
			});
		} else brush.setEnabled(false);
		
		return brush;
	}
	
	
	
	/**
	 * Duplication option for this ImageView
	 * */
	private JMenuItem createDuplicateMenu() {
		var duplicate = new JMenuItem("Duplicate...");
		
		duplicate.addActionListener(a -> {
			try {
				context.getParent().add((Component) context.clone());
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
		return duplicate;
	}
	
	
}
