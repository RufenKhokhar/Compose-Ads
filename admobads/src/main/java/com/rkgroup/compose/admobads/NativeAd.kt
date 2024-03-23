package com.rkgroup.compose.admobads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

/**
 * This function composes a NativeAd, which is provided by Google's Mobile Ads SDK.
 * It is used to display native ads inside the app.
 *
 * @param modifier An optional Modifier to apply to the container Box.
 * @param adID A required ad unit ID for the ad.
 * @param adoptions An optional NativeAdOptions parameter to customize the ad rendering.
 * @param adRequest An AdRequest object to specify targeting options and ad formats.
 * @param onAdEvent An optional callback for ad events, such as clicks, impressions, and loading status.
 * @see AdEvent
 * @param placeholder An optional Composable to display while the ad is loading.
 * @param onAdAvailable A required Composable to define the UI for the loaded native ad.
 */
@Composable
fun NativeAd(
    modifier: Modifier = Modifier,
    adID: String,
    adoptions: NativeAdOptions? = null,
    adRequest: AdRequest = AdRequest.Builder().build(),
    onAdEvent: ((AdEvent) -> Unit)? = null,
    placeholder: @Composable BoxScope.() -> Unit = {},
    onAdAvailable: @Composable BoxScope.(NativeAd) -> Unit
) {
    val context = LocalContext.current
    var nativeAd: NativeAd? by remember {
        mutableStateOf(null)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (nativeAd == null) {
            Box(
                content = placeholder,
                contentAlignment = Alignment.Center
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                content = {
                    nativeAd?.let {
                        onAdAvailable.invoke(this, it)
                    }
                }
            )
        }
    }

    DisposableEffect(Unit) {
        val adLoader = AdLoader.Builder(context, adID)
        adLoader.forNativeAd {
            nativeAd = it
        }
        adLoader.withAdListener(object : AdListener() {
            override fun onAdClicked() {
                onAdEvent?.invoke(AdEvent.Clicked)
            }

            override fun onAdClosed() {
                onAdEvent?.invoke(AdEvent.Closed)
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                onAdEvent?.invoke(AdEvent.Failed(p0))
            }

            override fun onAdImpression() {
                onAdEvent?.invoke(AdEvent.Impression)
            }

            override fun onAdLoaded() {
                onAdEvent?.invoke(AdEvent.Loaded)
            }

            override fun onAdOpened() {
                onAdEvent?.invoke(AdEvent.Opened)
            }

            override fun onAdSwipeGestureClicked() {
                onAdEvent?.invoke(AdEvent.SwipeGestureClicked)
            }
        })
        adoptions?.let {
            adLoader.withNativeAdOptions(it)
        }
        adLoader.build().loadAd(adRequest)
        onDispose {
            nativeAd?.destroy()
        }
    }
}