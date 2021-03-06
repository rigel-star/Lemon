package org.lemon.gui.menus;

import java.awt.Composite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.lemon.composite.AddComposite;
import org.lemon.filter.CompositeFilter;
import org.lemon.filter.ResizeImageFilter;
import org.lemon.graphics.Texture;
import org.lemon.gui.ImageView;
import org.lemon.gui.ImageViewSetup;
import org.lemon.gui.LayerContainer;
import org.lemon.gui.Workspace;
import org.lemon.gui.image.ChooseImage;
import org.lemon.gui.image.ImagePanel;
import org.lemon.gui.layers.ViewLayer;
import org.lemon.image.LImage;

/**
 * 
 * Contains commands for opening and saving images and creating new canvas.
 * 
 * */
public class FileMenu extends JMenu implements ActionListener {
	
	/**
	 * Serial UID
	 * */
	private static final long serialVersionUID = 1L;
	
	private JMenuItem openFile, saveFile, newFile;
	
	private Workspace wks;
	private LayerContainer lyrs;
	
	public FileMenu( final Workspace workspace, final LayerContainer layers ) {
		
		this.wks = workspace;
		this.lyrs = layers;
		
		setText( "File" );
		init();
		addAll();
		actionEvents();
		
	}
	
	/**
	 * init every component of this menu
	 * */
	private void init() {
		this.newFile = new JMenuItem( "New..." );
		this.openFile = new JMenuItem( "Open..." );
		this.saveFile = new JMenuItem( "Save..." );
	}
	
	/**
	 * add every menu item to this menu
	 * */
	private void addAll() {
		add( newFile );
		add( openFile );
		add( saveFile );
	}
	
	/**
	 * init action event for every component
	 * */
	private void actionEvents() {
		newFile.addActionListener( this );
		openFile.addActionListener( this );
		saveFile.addActionListener( this );
	}

	@Override
	public void actionPerformed( ActionEvent e ) {
		
		if( e.getSource() == newFile )
			newFile();
		
		else if( e.getSource() == openFile )
			openFile();
	}
	
	
	/**
	 * Create {@code CanvasView}.
	 * */
	private void newFile() {
		new ImageViewSetup( wks );
	}
	
	
	/** 
	 * Open image from user computer.
	 * */
	private void openFile() {
		ChooseImage imgChoose = new ChooseImage();
		BufferedImage img = imgChoose.getChoosenImage();
		
		Composite cc = new AddComposite( .5f );
		CompositeFilter cf = new CompositeFilter( cc );
		LImage out = cf.compose( 
				new ResizeImageFilter( img.getWidth(), img.getHeight()).filter( Texture.WOOD.getDrawable()),
						new LImage( img ));
		
		img = out.getAsBufferedImage();
		
		String title = imgChoose.getChoosenFile().getName();
		ImageView imgView = new ImageView( img, title, true, ImagePanel.PanelMode.SNAP_MODE, lyrs );
		
		wks.add( imgView );
		wks.fetchNodes();
		wks.refreshListeners();
		wks.revalidate();
		
		lyrs.addLayer( new ViewLayer( imgView, title, ViewLayer.BACKGROUND_LAYER ));
	}
}