package com.example.videowatchapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.videowatchapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityMainBinding

    private val videoUrls = listOf(
        "https://www.w3schools.com/html/mov_bbb.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    )

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPlayer()
        setupControls()
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(this).build()
        binding.playerView.player = player

        loadVideo(currentIndex)

        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playNextVideo()
                }
            }
        })
    }

    private fun setupControls() {
        binding.playPauseButton.setOnClickListener {
            if (player?.isPlaying == true) {
                player?.pause()
                binding.playPauseButton.text = "Play"
            } else {
                player?.play()
                binding.playPauseButton.text = "Pause"
            }
        }

        binding.prevButton.setOnClickListener {
            playPreviousVideo()
        }

        binding.nextButton.setOnClickListener {
            playNextVideo()
        }
    }

    private fun loadVideo(position: Int) {
        val safePosition = videoUrls.indices.contains(position)
        if (!safePosition) return

        val mediaItem = MediaItem.fromUri(videoUrls[position])
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
        binding.playPauseButton.text = "Pause"
        binding.videoTitle.text = "Video ${position + 1}"
    }

    private fun playNextVideo() {
        currentIndex = (currentIndex + 1) % videoUrls.size
        loadVideo(currentIndex)
    }

    private fun playPreviousVideo() {
        currentIndex = if (currentIndex == 0) videoUrls.size - 1 else currentIndex - 1
        loadVideo(currentIndex)
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
    }
}
