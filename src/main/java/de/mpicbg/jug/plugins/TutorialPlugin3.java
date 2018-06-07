/**
 *
 */
package de.mpicbg.jug.plugins;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import clearvolume.utils.AppleMac;
import de.mpicbg.jug.clearvolume.gui.ClearVolumeManager;
import de.mpicbg.jug.clearvolume.gui.GenericClearVolumeGui;
import net.imagej.ImgPlus;
import net.imagej.display.DatasetView;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Plugin that opens the active image using the
 * <code>GenericClearVolumeGui</code>.
 * Sets the UI programmatically.
 *
 * @author jug
 */
@Plugin( menuPath = "Tutorials>ClearVolume>Tutorial Plugin 3", description = "Configure GenericClearVolumeGui.", headless = false, type = Command.class )
public class TutorialPlugin3< T extends RealType< T > & NativeType< T >> implements Command {

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
		frame = new JFrame( "ClearVolume Tutorial 3 - configure GUI programmatically" );
		frame.setBounds( 50, 50, 1100, 800 );
		panelGui = new GenericClearVolumeGui< T >( imgPlus );

		setClearVolumeIcon( frame );
		final ClearVolumeManager< T > cvManager = panelGui.getClearVolumeManager();
		cvManager.toggleBox();

		System.out.println( "Will set brightness from " + cvManager.getBrightness( 0 ) + " to 0.8." );
		cvManager.setBrightness( 0, 0.8 );

		frame.add( panelGui );
		frame.setVisible( true );
	}

	/**
	 * An example for how to set an app icon.
	 * We use the default ClearVolume icon.
	 *
	 * @param frame
	 */
	private void setClearVolumeIcon( final JFrame frame ) {
		try {
			final URL lImageURL = getClass().getResource( "/clearvolume/icon/ClearVolumeIcon256.png" );
			final ImageIcon lImageIcon = new ImageIcon( lImageURL );

			if ( AppleMac.isMac() ) {
				AppleMac.setApplicationIcon( lImageIcon.getImage() );
				AppleMac.setApplicationName( "ClearVolume Tutorial 3" );
			}

			frame.setIconImage( lImageIcon.getImage() );
		} catch ( final Throwable e ) {
			e.printStackTrace();
		}
	}
}
