package com.multipiston.coremod;

public class Constants
{
    public static final String MOD_ID   = "multipiston";
    public static final String MOD_NAME = "Piston unlimited";

    public static final double HALF_BLOCK   = 0.5D;
    public static final int    TICKS_SECOND = 20;
    public static final String VERSION      = "@VERSION@";
    public static final String MC_VERSION   = "[1.12,1.13]";
    public static final String INVENTORY    = "inventory";

    /**
     * NBT tag constants for MultiPiston tileEntities.
     */
    public static final String TAG_INPUT            = "input";
    public static final String TAG_RANGE            = "range";
    public static final String TAG_DIRECTION        = "direction";
    public static final String TAG_PROGRESS         = "progress";
    public static final String TAG_OUTPUT_DIRECTION = "outputDirection";
    public static final String TAG_SPEED            = "speed";

    /**
     * Move the structure preview forward.
     */
    public static final String BUTTON_FORWARD = "up";

    /**
     * Move the structure preview back.
     */
    public static final String BUTTON_BACKWARD = "down";

    /**
     * Move the structure preview left.
     */
    public static final String BUTTON_LEFT = "left";

    /**
     * Move the structure preview right.
     */
    public static final String BUTTON_RIGHT = "right";

    /**
     * Id of the up button in the GUI.
     */
    public static final String BUTTON_UP = "plus";

    /**
     * Id of the up button in the GUI.
     */
    public static final String BUTTON_DOWN = "minus";

    /**
     * Standard pitch value.
     */
    public static final double PITCH = 0.9D;

    /**
     * Volume to play at.
     */
    public static final double VOLUME = 0.5D;
}
