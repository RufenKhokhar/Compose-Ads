package com.rkgroup.compose.admobads

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

/**
 * This Composable function creates a banner ad using Google's Mobile Ads SDK.
 *
 * @param modifier An optional Modifier to apply to the container Box.
 * @param adId A required ad unit ID for the ad.
 * @param adSize A required AdSize parameter to specify the dimensions of the banner ad.
 * @param adRequest An AdRequest object to specify targeting options and ad formats.
 * @param placeholder An optional Composable to display while the ad is loading.
 * @param onAdEvent An optional callback for ad events
 * @see AdSize
 * @see AdEvent
 *
 */
@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
    adId: String,
    adSize: AdSize,
    adRequest: AdRequest = AdRequest.Builder().build(),
    placeholder: @Composable BoxScope.() -> Unit = {},
    onAdEvent: ((AdEvent) -> Unit)? = null
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var bannerAd: AdView? by remember {
        mutableStateOf(null)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (bannerAd == null) {
            Box(
                content = placeholder,
                contentAlignment = Alignment.Center
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                content = {
                    AndroidView(
                        factory = { bannerAd!! }
                    )
                }
            )
        }
    }

    DisposableEffect(Unit) {
        val adView = AdView(context)
        adView.adUnitId = adId
        adView.setAdSize(adSize)
        adView.adListener = object : AdListener() {
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
                bannerAd = adView
                onAdEvent?.invoke(AdEvent.Loaded)
            }

            override fun onAdOpened() {
                onAdEvent?.invoke(AdEvent.Opened)
            }

            override fun onAdSwipeGestureClicked() {
                onAdEvent?.invoke(AdEvent.SwipeGestureClicked)
            }
        }
        adView.loadAd(adRequest)
        val observer = LifecycleEventObserver { _, e ->
            if (e == Lifecycle.Event.ON_RESUME) {
                adView.resume()
            } else if (e == Lifecycle.Event.ON_PAUSE) {
                adView.pause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            adView.destroy()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


}

/**
 * This Composable function creates a banner ad using Google's Mobile Ads SDK.
 * @param modifier An optional Modifier to apply to the container Box.
 * @param adId A required ad unit ID for the ad.
 * @param adSize A required AdSize parameter to specify the dimensions of the banner ad.
 * @param collapsible A required CollapseDirection parameter to specify whether the ad can be collapsible or not.
 * @param placeholder An optional Composable to display while the ad is loading.
 * @param onAdEvent An optional callback for ad events
 * @see AdSize
 * @see AdEvent
 * @see CollapseDirection
 */

@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
    adId: String,
    adSize: AdSize,
    collapsible: CollapseDirection = CollapseDirection.NON,
    placeholder: @Composable BoxScope.() -> Unit = {},
    onAdEvent: ((AdEvent) -> Unit)? = null
) {
    val adRequest by remember {
        derivedStateOf {
            val builder = AdRequest.Builder()
            if (collapsible != CollapseDirection.NON) {
                builder.addNetworkExtrasBundle(AdMobAdapter::class.java, Bundle().apply {
                    putString("collapsible", collapsible.value)
                })
            }
            builder.build()
        }
    }
    BannerAd(
        modifier = modifier,
        adId = adId,
        adSize = adSize,
        adRequest = adRequest,
        placeholder = placeholder,
        onAdEvent = onAdEvent
    )
}

fun Context.getFullAdaptiveBannerAdSize(): AdSize {
    val display = Resources.getSystem().displayMetrics
    val adSize = display.widthPixels / display.density
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
        this,
        adSize.toInt()
    )
}


