package org.lemon.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lemon.graphics.Texture;
import org.lemon.gui.texture.TextureList;
import org.lemon.gui.texture.TexturePanel;
import org.lemon.lang.LemonObject;


/**
 * 
 * Texture chooser helps choosing texture for image.
 * 
 * */
@LemonObject( type = LemonObject.GUI_CLASS )
public class TextureChooser extends JDialog {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	
	private final static int V_GAP = 10;
	private final static int H_GAP = 10;
	private final static int BORDER = 12;
	
	private static TextureList list = null;
	private JPanel buttonPanel = null;
	
	private JButton okBttn = null, cancelBttn = null;
	private JLabel txtrHolder = null;
	
	private Texture texture;
	
	static {
		list = new TextureList();
	}
	
	/**
	 * @param txtrHolder 	{@code JLabel} holding texture.
	 * */
	public TextureChooser( JLabel txtrHolder ) {
		
		this.txtrHolder = txtrHolder;
		this.init();
		
		setTitle( "Textures" );
		setSize( 500, 400 );
		setModalityType( ModalityType.DOCUMENT_MODAL );
		setDefaultCloseOperation( DISPOSE_ON_CLOSE );
		setLocationRelativeTo( null );	
		
		buttonPanel.add( okBttn );
		buttonPanel.add( cancelBttn );
		
		var main = new JPanel();
		main.setBorder( BorderFactory.createEmptyBorder( BORDER, BORDER, BORDER, BORDER ) );
		
		main.setLayout( new BorderLayout( H_GAP, V_GAP ));
		main.add( list, BorderLayout.CENTER );
		main.add( buttonPanel, BorderLayout.EAST );
		
		add( main, BorderLayout.CENTER );	
		setVisible( true );
	}
	
	/**
	 * 
	 * Initialize the widgets.
	 * 
	 * */
	private void init() {
		
		okBttn = new JButton( "Ok" );
		okBttn.setPreferredSize( new Dimension( 90, 30 ));
		
		okBttn.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				TexturePanel selected = list.getSelectedValue();
				if ( selected != null ) {
					txtrHolder.setIcon( new ImageIcon( selected.getTexture().getDrawable().getAsBufferedImage() ));
					txtrHolder.repaint();
					texture = selected.getTexture();
				}
				dispose();
			}
		});
		
		cancelBttn = new JButton( "Cancel" );
		cancelBttn.setPreferredSize( new Dimension( 90, 30 ));
		cancelBttn.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize( new Dimension( 100, HEIGHT ));
		buttonPanel.setLayout( new FlowLayout( FlowLayout.CENTER, H_GAP, V_GAP ) );
	}
	
	public Texture getChoosenTexture() {
		return texture;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension( 300, 200 );
	}
	
	class WindowHandler extends WindowAdapter {
		
		@Override
		public void windowClosing( WindowEvent e ) {
			super.windowClosed( e );
			TexturePanel selected = list.getSelectedValue();
			
			if ( selected != null ) {
				txtrHolder.setIcon( new ImageIcon( selected.getTexture().getDrawable().getAsBufferedImage() ));
				txtrHolder.repaint();
			}
		}
	}
}
