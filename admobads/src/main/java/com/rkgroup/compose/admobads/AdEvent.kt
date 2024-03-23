package com.rkgroup.compose.admobads

import com.google.android.gms.ads.AdError

/**
 * This sealed class defines the different events that can occur during the lifecycle of a AD.
 */
sealed class AdEvent {
    /**
     * Represents a click event on the AD.
     */
    data object Clicked : AdEvent()

    /**
     * Represents a close event on the AD.
     */
    data object Closed : AdEvent()

    /**
     * Represents a failure event on the AD, with an AdError object containing information about the error.
     */
    data class Failed(val error: AdError) : AdEvent()

    /**
     * Represents an impression event on the AD, which is when the ad is displayed on the screen.
     */
    data object Impression : AdEvent()

    /**
     * Represents a load event on the AD, which is when the ad is successfully loaded.
     */
    data object Loaded : AdEvent()

    /**
     * Represents an open event on the AD, which is when the user clicks on the ad and it opens in a new screen.
     */
    data object Opened : AdEvent()

    /**
     * Represents a swipe gesture click event on the banner ad.
     */
    data object SwipeGestureClicked : AdEvent()
}