/**
 *
 */
package de.mpicbg.jug.plugins;

import javax.swing.JFrame;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import de.mpicbg.jug.clearvolume.gui.GenericClearVolumeGui;
import net.imagej.ImgPlus;
import net.imagej.display.DatasetView;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Plugin that opens the active image using the
 * <code>GenericClearVolumeGui</code>.
 *
 * @author jug
 */
@Plugin( menuPath = "Tutorials>ClearVolume>Tutorial Plugin 1", description = "Hello ClearVolume.", headless = false, type = Command.class )
public class TutorialPlugin1< T extends RealType< T > & NativeType< T >> implements Command {

	@Parameter( label = "3D ImgPlus to be shown." )
	private DatasetView datasetView;

	private ImgPlus< T > imgPlus;
	private JFrame frame;
	private GenericClearVolumeGui< T > panelGui;

	/**
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void run() {
		imgPlus = ( ImgPlus< T > ) datasetView.getData().getImgPlus();
		frame = new JFrame( "ClearVolume Tutorial 1" );
		frame.setBounds( 50, 50, 1024, 768 );
		panelGui = new GenericClearVolumeGui< T >( imgPlus );
		frame.add( panelGui );
		frame.setVisible( true );
	}

}
