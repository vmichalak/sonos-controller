package com.vmichalak.sonoscontroller.model;

public enum PlayState {
    /**
     * Player has an error.
     */
    ERROR,

    /**
     * Player is stopped.
     */
    STOPPED,

    /**
     * Player is playing.
     */
    PLAYING,

    /**
     * Player is paused.
     */
    PAUSED_PLAYBACK,

    /**
     * Player is loading.
     */
    TRANSITIONING
}
