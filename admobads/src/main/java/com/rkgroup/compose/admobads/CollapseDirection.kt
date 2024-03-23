package com.rkgroup.compose.admobads

/**
 * This enum class defines the directions in which a banner ad can be collapsible.
 */
enum class CollapseDirection(val value: String) {
    /**
     * The banner ad is collapsible to the top.
     */
    TOP("top"),
    /**
     * The banner ad is collapsible to the bottom
     */
    BOTTOM("bottom"),
    /**
     * The banner ad is collapsible non.
     */
    NON("non")
}