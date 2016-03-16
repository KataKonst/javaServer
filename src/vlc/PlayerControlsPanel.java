package vlc;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class PlayerControlsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int SKIP_TIME_MS = 10 * 1000;

	private final ScheduledExecutorService executorService = Executors
			.newSingleThreadScheduledExecutor();

	private final EmbeddedMediaPlayer mediaPlayer;

	private JLabel timeLabel;
	private JLabel timeMaxLengthLabel;
	// private JProgressBar positionProgressBar;
	private JSlider positionSlider;

	// private JButton previousChapterButton;
	private JButton rewindButton;
	private JButton stopButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton fastForwardButton;

	private JButton toggleMuteButton;
	private JSlider volumeSlider;


	private boolean mousePressedPlaying = false;

	private Canvas vs;

	protected String youtube_url;

	public PlayerControlsPanel(String url) {
		this.mediaPlayer = new MediaPlayerFactory().newEmbeddedMediaPlayer();
		this.youtube_url = url;
		
		createUI();

		executorService.scheduleAtFixedRate(new UpdateRunnable(mediaPlayer),
				0L, 1L, TimeUnit.SECONDS);
	}

	private void createUI() {
		createControls();
		layoutControls();
		registerListeners();
	}

	private void createControls() {
		timeLabel = new JLabel("hh:mm:ss");
		timeMaxLengthLabel = new JLabel("hh:mm:ss");

		positionSlider = new JSlider();
		positionSlider.setMinimum(0);
		positionSlider.setMaximum(1000);
		positionSlider.setValue(0);
		positionSlider.setToolTipText("Position");

		rewindButton = new JButton("Rewird");
		// rewindButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_rewind_blue.png")));
		rewindButton.setToolTipText("Skip back");

		stopButton = new JButton("Stop");
		// stopButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_stop_blue.png")));
		stopButton.setToolTipText("Stop");

		pauseButton = new JButton("Pause");
		// pauseButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_pause_blue.png")));
		pauseButton.setToolTipText("Play/pause");

		playButton = new JButton("Play");
		// playButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_play_blue.png")));
		playButton.setToolTipText("Play");

		fastForwardButton = new JButton("FastForward");
		// fastForwardButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/control_fastforward_blue.png")));
		fastForwardButton.setToolTipText("Skip forward");

		toggleMuteButton = new JButton("Mute");
		// toggleMuteButton.setIcon(new
		// ImageIcon(getClass().getClassLoader().getResource("icons/sound_mute.png")));
		toggleMuteButton.setToolTipText("Toggle Mute");

		volumeSlider = new JSlider();
		volumeSlider.setOrientation(JSlider.HORIZONTAL);
		volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
		volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
		volumeSlider.setValue(LibVlcConst.MAX_VOLUME);
		volumeSlider.setPreferredSize(new Dimension(100, 40));
		volumeSlider.setToolTipText("Change volume");


	}

	private void layoutControls() {
		setBorder(new EmptyBorder(4, 4, 4, 4));

		setLayout(new BorderLayout());

		JPanel positionPanel = new JPanel();
		positionPanel.setLayout(new GridLayout(1, 1));
		// positionPanel.add(positionProgressBar);
		positionPanel.add(positionSlider);

		vs = new Canvas();
		vs.setBackground(Color.black);
		add(vs, BorderLayout.CENTER);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout(8, 0));

		topPanel.add(timeLabel, BorderLayout.WEST);
		topPanel.add(positionPanel, BorderLayout.CENTER);
		topPanel.add(timeMaxLengthLabel, BorderLayout.EAST);

		add(topPanel, BorderLayout.NORTH);

		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new FlowLayout());

		// bottomPanel.add(previousChapterButton);
		bottomPanel.add(rewindButton);
		bottomPanel.add(stopButton);
		bottomPanel.add(pauseButton);
		bottomPanel.add(playButton);
		bottomPanel.add(fastForwardButton);

		bottomPanel.add(volumeSlider);
		bottomPanel.add(toggleMuteButton);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	/**
	 * Broken out position setting, handles updating mediaPlayer
	 */
	private void setSliderBasedPosition() {
		if (!mediaPlayer.isSeekable()) {
			return;
		}
		float positionValue = positionSlider.getValue() / 1000.0f;
		// Avoid end of file freeze-up
		if (positionValue > 0.99f) {
			positionValue = 0.99f;
		}
		mediaPlayer.setPosition(positionValue);
	}

	private void updateUIState() {
		if (!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current
			// position in video
			mediaPlayer.play();
			if (!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}
		long time = mediaPlayer.getTime();
		int position = (int) (mediaPlayer.getPosition() * 1000.0f);
        updateTime(time);
		updatePosition(position);
	}

	private void skip(int skipTime) {
		// Only skip time if can handle time setting
		if (mediaPlayer.getLength() > 0) {
			mediaPlayer.skip(skipTime);
			updateUIState();
		}
	}

	private void registerListeners() {

		positionSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (mediaPlayer.isPlaying()) {
					mousePressedPlaying = true;
					mediaPlayer.pause();
				} else {
					mousePressedPlaying = false;
				}
				setSliderBasedPosition();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setSliderBasedPosition();
				updateUIState();
			}
		});

		rewindButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(-SKIP_TIME_MS);
			}
		});

		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				mediaPlayer.stop();
			}
		});

		pauseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (!mediaPlayer.isPlaying()) {
					pauseButton.setText("Pause");
				} else {
					pauseButton.setText("RePlay");
				}

				mediaPlayer.pause();

			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				mediaPlayer.enableOverlay(false);
				mediaPlayer.setVideoSurface(new MediaPlayerFactory().newVideoSurface(vs));
				mediaPlayer.setPlaySubItems(true);
				mediaPlayer.playMedia(youtube_url);
				mediaPlayer.enableOverlay(true);

				mediaPlayer.play();
				
			}
		});

		fastForwardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				skip(SKIP_TIME_MS);
			}
		});

		toggleMuteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPlayer.mute();
			}
		});

		volumeSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				// if(!source.getValueIsAdjusting()) {
				mediaPlayer.setVolume(source.getValue());
				// }
			}
		});

	}

	private final class UpdateRunnable implements Runnable {

		private final MediaPlayer mediaPlayer;

		private UpdateRunnable(MediaPlayer mediaPlayer) {
			this.mediaPlayer = mediaPlayer;
		}

		@Override
		public void run() {
			final long time = mediaPlayer.getTime();
			final int position = (int) (mediaPlayer.getPosition() * 1000.0f);


			// Updates to user interface components must be executed on the
			// Event
			// Dispatch Thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					if (mediaPlayer.isPlaying()) {
						updateTime(time);
						updatePosition(position);
						updateMaxTime(mediaPlayer.getLength());
					}
				}
			});
		}
	}
	
	private String getTime(long millis){
		String s = String.format(
				"%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
		
		return s;
	}

	private void updateTime(long millis) {
		
		timeLabel.setText(getTime(millis));
	}
	
	private void updateMaxTime(long millis){
		timeMaxLengthLabel.setText(getTime(millis));
	}

	private void updatePosition(int value) {
		// positionProgressBar.setValue(value);
		positionSlider.setValue(value);
	}

	private void updateVolume(int value) {
		volumeSlider.setValue(value);
	}

	public void close() {
		stopButton.doClick();
		executorService.shutdown();
	}
	
	public void play() {
		playButton.doClick();
	}
}