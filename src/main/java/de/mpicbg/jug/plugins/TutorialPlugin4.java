/**
 *
 */
package de.mpicbg.jug.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import de.mpicbg.jug.clearvolume.gui.ClearVolumeManager;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.display.DatasetView;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

/**
 * Plugin that opens the active image using the
 * <code>GenericClearVolumeGui</code>.
 * Here we include only a clearvolume jogl window without the generic UI around
 * it...
 *
 * @author jug
 */
@Plugin( menuPath = "Tutorials>ClearVolume>Tutorial Plugin 4", description = "Without GenericClearVolumeGui.", headless = false, type = Command.class )
public class TutorialPlugin4< T extends RealType< T > & NativeType< T >> implements Command {

	@Parameter( label = "3D ImgPlus to be shown." )
	private DatasetView datasetView;

	private ImgPlus< T > imgPlus;
	ClearVolumeManager< T > cvManager;
	private JFrame frame;

	/**
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings( "unchecked" )
	@Override
	public void run() {
		imgPlus = ( ImgPlus< T > ) datasetView.getData().getImgPlus();
		frame = new JFrame( "ClearVolume Tutorial 4 - use without GenericClearVolumeGUI" );
		frame.setBounds( 50, 50, 1100, 800 );

		final List< RandomAccessibleInterval< T > > images = getImageChannels( imgPlus, imgPlus.dimensionIndex( Axes.CHANNEL ) );
		startClearVolumeManager( images );

		// Ask the image which dimensions are X, Y, and Z
		final int dX = imgPlus.dimensionIndex( Axes.X );
		final int dY = imgPlus.dimensionIndex( Axes.Y );
		final int dZ = imgPlus.dimensionIndex( Axes.Z );
		// In case the image knew...
		if ( dX != -1 && dY != -1 && dZ != -1 ) {
			// ...set averageScale of these dimensions.
			cvManager.setVoxelSize(
					imgPlus.averageScale( imgPlus.dimensionIndex( Axes.X ) ),
					imgPlus.averageScale( imgPlus.dimensionIndex( Axes.Y ) ),
					imgPlus.averageScale( imgPlus.dimensionIndex( Axes.Z ) ) );
		} else if ( imgPlus.numDimensions() >= 3 ) {
			// Otherwise assume dimension order X, Y, Z and set averageScale of those dimensions
			cvManager.setVoxelSize(
					imgPlus.averageScale( 0 ),
					imgPlus.averageScale( 1 ),
					imgPlus.averageScale( 2 ) );
		}

		frame.add( cvManager.getClearVolumeRendererInterface().getNewtCanvasAWT() );
		frame.setVisible( true );
	}

	/**
	 * Instantiates and runs a <code>ClearVolumeManager</code> on the given list
	 * of images.
	 *
	 * @param images
	 *            a <code>List</code> of channel images.
	 */
	private void startClearVolumeManager( final List< RandomAccessibleInterval< T > > images ) {
		final Runnable todo = new Runnable() {

			@Override
			public void run() {
				cvManager = new ClearVolumeManager< T >( images );
			}
		};

		if ( javax.swing.SwingUtilities.isEventDispatchThread() ) {
			todo.run(); // in this example this would be enough
		} else {
			// the stuff down there you'd need if this code here would NOT be running in the Swing thread.
			try {
				SwingUtilities.invokeAndWait( todo );
			} catch ( InvocationTargetException | InterruptedException e ) {
				e.printStackTrace();
			}
		}
		cvManager.run();
	}

	/**
	 * Takes an multi-channel image and returns a list of images containing each
	 * channel separately.
	 *
	 * @param imgPlus
	 *            the multi-channel image.
	 * @param channelDimension
	 *            the dimension containing the channels.
	 */
	private List< RandomAccessibleInterval< T > > getImageChannels( final RandomAccessibleInterval< T > imgPlus, final int channelDimension ) {
		final List< RandomAccessibleInterval< T > > newimages =
				new ArrayList< RandomAccessibleInterval< T > >();

		for ( int channel = 0; channel < imgPlus.dimension( channelDimension ); channel++ ) {
			final RandomAccessibleInterval< T > rai =
					Views.hyperSlice( imgPlus, channelDimension, channel );
			newimages.add( rai );
		}
		return newimages;
	}
}
