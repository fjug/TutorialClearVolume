/**
 *
 */
package de.mpicbg.jug.plugins;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import de.mpicbg.jug.clearvolume.gui.ClearVolumeSplashFrame;
import de.mpicbg.jug.clearvolume.gui.GenericClearVolumeGui;
import net.imagej.ImgPlus;
import net.imagej.display.DatasetView;
import net.imglib2.display.ColorTable;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

/**
 * Plugin that opens the active image using the
 * <code>GenericClearVolumeGui</code>.
 * Several enhancements are demoed along the way.
 * If you are interested in a minimal example please check TutorialPlugin1.
 *
 * @author jug
 */
@Plugin( menuPath = "Tutorials>ClearVolume>Tutorial Plugin 2", description = "Advaned hello ClearVolume.", headless = false, type = Command.class )
public class TutorialPlugin2< T extends RealType< T > & NativeType< T >> implements Command {

	@Parameter( label = "3D ImgPlus to be shown." )
	private DatasetView datasetView;

	private ImgPlus< T > imgPlus;

	private final int windowWidth = 1200;
	private final int windowHeight = 900;

	@Parameter( label = "Max texture size", min = "16", max = "3840", stepSize = "100", columns = 5, description = "Max texture resolution (per axis)." )
	private int textureResolution = 2048;

	private JFrame frame;
	private GenericClearVolumeGui< T > panelGui;

	/**
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void run() {

		imgPlus = ( ImgPlus< T > ) datasetView.getData().getImgPlus();
		final List< ColorTable > luts = datasetView.getColorTables();

		final boolean isShowable = checkIfShowable( frame, imgPlus, true );

		if ( isShowable ) {
			final Dimension screenDims = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

			textureResolution = Math.min( textureResolution, screenDims.width );

			frame = new JFrame( "ClearVolume Tutorial 2" );
			frame.setLayout( new BorderLayout() );
			frame.setBounds(
					( screenDims.width - windowWidth ) / 2,
					( screenDims.height - windowHeight ) / 2,
					windowWidth,
					windowHeight );

			final ClearVolumeSplashFrame loadFrame = new ClearVolumeSplashFrame();

			panelGui = new GenericClearVolumeGui< T >( imgPlus, luts, textureResolution, false );
			frame.add( panelGui );
			frame.setVisible( true );

			loadFrame.dispose();
		}
	}

	/**
	 * Checks if a given image has an compatible format to be shown.
	 * @return true, if image is of supported type and structure.
	 */
	public static boolean checkIfShowable(
			final Component parent,
			final ImgPlus< ? > imgPlus,
			final boolean showErrorDialogs ) {
		boolean isOk = true;
		String message = "";

		if ( imgPlus == null ) {
			message = "ClearVolume cannot be initialized with a null image!";
			isOk = false;
		} else if ( imgPlus.numDimensions() < 2 || imgPlus.numDimensions() > 5 ) {
			message =
					"Only images with 2(X,Y), 3 (X,Y,Z) or 4 (X,Y,C,Z) and 5 (X,Y,C,Z,T) dimensions\ncan be shown, current image has " + imgPlus
							.numDimensions() + " dimensions.";
			isOk = false;
		}

		if ( !message.equals( "" ) ) {
			JOptionPane.showMessageDialog(
					parent,
					message,
					"Image Format Error",
					JOptionPane.ERROR_MESSAGE );
		}

		return isOk;
	}
}
