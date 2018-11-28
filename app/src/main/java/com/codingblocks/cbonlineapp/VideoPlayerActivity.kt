package com.codingblocks.cbonlineapp

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.MappingTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.Util

class VideoPlayerActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val KEY_PLAY_WHEN_READY = "play_when_ready"
        private const val KEY_WINDOW = "window"
        private const val KEY_POSITION = "position"
        private const val KEY_ID = "APKAIX3JJRW7RHDSNHGA"
        private const val POLICY_STRING = "eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9kMXFmMG96c3M0OTR4di5jbG91ZGZyb250Lm5ldC8wODg1ZGMzNi0xODNlLTQzMjUtYWY3Ni05NDlmNmI4NzMwZjAwMU9LSHR0cEludHJvbXA0LyoiLCJDb25kaXRpb24iOnsiRGF0ZUxlc3NUaGFuIjp7IkFXUzpFcG9jaFRpbWUiOjE1NDE3ODk0ODN9fX1dfQ=="
        private const val SIGNATURE = "NY1N6wkOWxDxGui2eCzHV9JswFU7PHnjHoXl8Dhz1Fv7a5sJxYzHe5tmNx9Iu0jGUotAki6+HtcsKEGycT0G3tBkp5JCCXo20KHx4ZWvQCcUGscKGmutcshHl8WtJ2gTHQ+LeNyCbpYeXWKsb64PaRYnWO2dg9GCxqAB8saZeSeDV6k6d3WnMH9HfUxCXntdcWJG1Mmtiu/UpfI6iSzwzG4YTMIwe8OvuzTwqjcXPoWnlelSIa798SOX+acWNoSQgvIfwx+mydcH7+y6EVeH9RHN5jamvj+WWh83szN+dd75J0CJNZ+xvQ5FmrUeEG+IZYYlHU/YXmwdljdcXQpkvA=="
    }

    private var player: SimpleExoPlayer? = null
    private val playerView: PlayerView by lazy { findViewById<PlayerView>(R.id.player_view) }


    private var shouldAutoPlay: Boolean = true
    private var trackSelector: DefaultTrackSelector? = null
    private var lastSeenTrackGroupArray: TrackGroupArray? = null
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()

    private var playWhenReady: Boolean = false
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val ivHideControllerButton: ImageView by lazy { findViewById<ImageView>(R.id.exo_controller) }
    private val ivSettings: ImageView by lazy { findViewById<ImageView>(R.id.settings) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_video_player)

        if (savedInstanceState != null) {

            with(savedInstanceState) {
                playWhenReady = getBoolean(KEY_PLAY_WHEN_READY)
                currentWindow = getInt(KEY_WINDOW)
                playbackPosition = getLong(KEY_POSITION)
            }
        }

        shouldAutoPlay = true
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"),
                bandwidthMeter as TransferListener<in DataSource>)
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23 || player == null) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

    override fun onClick(v: View) {

        if (v.id == R.id.settings) {


//            val mappedTrackInfo = trackSelector?.currentMappedTrackInfo
//
//            if (mappedTrackInfo != null) {
//
//                val title = "Video"
//                val rendererIndex = ivSettings.tag as Int
//                val dialogPair = TrackSelectionView.getDialog(this, title, trackSelector, rendererIndex)
//                dialogPair.second.setShowDisableOption(false)
//                dialogPair.second.setAllowAdaptiveSelections(true)
//                dialogPair.first.show()
//            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        updateStartPosition()

        with(outState) {
            putBoolean(KEY_PLAY_WHEN_READY, playWhenReady)
            putInt(KEY_WINDOW, currentWindow)
            putLong(KEY_POSITION, playbackPosition)
        }

        super.onSaveInstanceState(outState)
    }


    private fun initializePlayer() {

        playerView.requestFocus()

        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        lastSeenTrackGroupArray = null

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        playerView.player = player

        with(player!!) {
            addListener(PlayerEventListener())
            playWhenReady = shouldAutoPlay
        }

        // Use Hls, Dash or other smooth streaming media source if you want to test the track quality selection.
        val mainHandler = Handler()
        val mediaSource: MediaSource = HlsMediaSource(Uri.parse("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"),
                mediaDataSourceFactory, mainHandler, null)

//        val mediaSource = ExtractorMediaSource.Factory(mediaDataSourceFactory)
//                .createMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"))

        val haveStartPosition = currentWindow != C.INDEX_UNSET
        if (haveStartPosition) {
            player!!.seekTo(currentWindow, playbackPosition)
        }

        player!!.prepare(mediaSource, !haveStartPosition, false)
        updateButtonVisibilities()

        ivHideControllerButton.setOnClickListener { playerView.hideController() }
    }

    private fun releasePlayer() {
        if (player != null) {
            updateStartPosition()
            shouldAutoPlay = player!!.playWhenReady
            player!!.release()
            player = null
            trackSelector = null
        }
    }

    private fun updateStartPosition() {

        with(player!!) {
            playbackPosition = currentPosition
            currentWindow = currentWindowIndex
            playWhenReady = playWhenReady
        }
    }

    private fun updateButtonVisibilities() {
        ivSettings.visibility = View.GONE
        if (player == null) {
            return
        }

        val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo ?: return

        for (i in 0 until mappedTrackInfo.rendererCount) {
            val trackGroups = mappedTrackInfo.getTrackGroups(i)
            if (trackGroups.length != 0) {
                if (player!!.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
                    ivSettings.visibility = View.VISIBLE
                    ivSettings.setOnClickListener(this)
                    ivSettings.tag = i
                }
            }
        }
    }


    private inner class PlayerEventListener : Player.DefaultEventListener() {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE       // The player does not have any media to play yet.
                -> progressBar.visibility = View.VISIBLE
                Player.STATE_BUFFERING  // The player is buffering (loading the content)
                -> progressBar.visibility = View.VISIBLE
                Player.STATE_READY      // The player is able to immediately play
                -> progressBar.visibility = View.GONE
                Player.STATE_ENDED      // The player has finished playing the media
                -> progressBar.visibility = View.GONE
            }
            updateButtonVisibilities()
        }

        override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            updateButtonVisibilities()
            // The video tracks are no supported in this device.
            if (trackGroups !== lastSeenTrackGroupArray) {
                val mappedTrackInfo = trackSelector!!.currentMappedTrackInfo
                if (mappedTrackInfo != null) {
                    if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO) == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                        Toast.makeText(this@VideoPlayerActivity, "Error unsupported track", Toast.LENGTH_SHORT).show()
                    }
                }
                lastSeenTrackGroupArray = trackGroups
            }
        }
    }

}