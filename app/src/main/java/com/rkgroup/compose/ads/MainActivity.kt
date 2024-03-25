package com.rkgroup.compose.ads

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView
import com.rkgroup.compose.admobads.BannerAd
import com.rkgroup.compose.admobads.CollapseDirection
import com.rkgroup.compose.admobads.NativeAd
import com.rkgroup.compose.admobads.getFullAdaptiveBannerAdSize
import com.rkgroup.compose.ads.ui.theme.ComposeAdsTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAdsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        BannerAd(
                            modifier = Modifier.fillMaxWidth(),
                            adId = "ca-app-pub-3940256099942544/9214589741",
                            adSize = getFullAdaptiveBannerAdSize(),
                            placeholder = {
                                Text(text = "Loading banner ad...", color = Color.Green)
                            },
                            onAdEvent = {

                            },
                            collapsible = CollapseDirection.TOP
                        )
                        NativeAd(
                            modifier = Modifier
                                .padding(vertical = 12.dp)
                                .fillMaxWidth(),
                            adID = "ca-app-pub-3940256099942544/2247696110",
                            placeholder = {
                                Text(text = "Loading Ad...")
                            },
                            onAdEvent = {
                                Log.d(TAG, "onCreate: ad event :$it")
                            }
                        ) { nativeAd ->
                            AndroidView(factory = {
                                val layoutInflater = LayoutInflater.from(it)
                                val adView =
                                    layoutInflater.inflate(R.layout.native_big, null, false)
                                val headline: TextView = adView.findViewById(R.id.ad_headline)
                                val body: TextView = adView.findViewById(R.id.ad_body)
                                val actionBtn: TextView =
                                    adView.findViewById(R.id.ad_call_to_action)
                                val mediaView: MediaView = adView.findViewById(R.id.ad_media)
                                val adIcon: ImageView = adView.findViewById(R.id.ad_app_icon)
                                (adView as NativeAdView)
                                adView.mediaView = mediaView
                                adView.headlineView = headline
                                adView.bodyView = body
                                adView.callToActionView = actionBtn
                                adView.iconView = adIcon
                                adView.setNativeAd(nativeAd)
                                actionBtn.text = nativeAd.callToAction
                                headline.text = nativeAd.headline
                                body.text = nativeAd.body
                                mediaView.mediaContent = nativeAd.mediaContent
                                adIcon.setImageDrawable(nativeAd.icon?.drawable)
                                adView
                            }, modifier = Modifier.fillMaxWidth())

                        }
                        HorizontalPager(state = rememberPagerState {
                            5
                        }, modifier = Modifier.weight(1f)) {
                            Card(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Page ${it+1}")
                                }

                            }

                        }

                    }
                }
            }
        }
    }
}


