package de.geier.citymanager.ui

import android.net.Uri
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import de.geier.citymanager.R
import kotlinx.coroutines.delay
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun StartScreen(
    onContinue: () -> Unit
) {

    val context = LocalContext.current

    var isVideoReady by remember {
        mutableStateOf(false)
    }

    var showOverlay by remember {
        mutableStateOf(false)
    }

    val animatedOverlayAlpha by animateFloatAsState(
        targetValue = if (showOverlay) 1f else 0f,
        animationSpec = tween(
            durationMillis = 2500
        ),
        label = "overlayFade"
    )

    val exoPlayer = remember {

        ExoPlayer.Builder(context).build().apply {

            val videoUri = Uri.parse(
                "android.resource://${context.packageName}/${R.raw.start_background}"
            )

            setMediaItem(
                MediaItem.fromUri(videoUri)
            )

            volume = 0f

            playWhenReady = true

            prepare()

            addListener(object : Player.Listener {

                override fun onPlaybackStateChanged(
                    playbackState: Int
                ) {

                    if (playbackState == Player.STATE_READY) {

                        isVideoReady = true
                    }
                }
            })
        }
    }

    DisposableEffect(Unit) {

        onDispose {

            exoPlayer.release()
        }
    }

    LaunchedEffect(Unit) {

        delay(9000)

        showOverlay = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onContinue()
            }
    ) {

        AndroidView(
            factory = {

                PlayerView(it).apply {

                    player = exoPlayer

                    useController = false

                    resizeMode =
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    setShutterBackgroundColor(
                        android.graphics.Color.BLACK
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        if (showOverlay || animatedOverlayAlpha > 0f) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.Black.copy(
                            alpha = animatedOverlayAlpha
                        )
                    )
            )
        }

        if (isVideoReady) {

            Image(
                painter = painterResource(
                    id = R.drawable.logo
                ),
                contentDescription = "Logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 48.dp)
            )
        }
    }
}