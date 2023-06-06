package processing;

/**
 *  The numeration position class determines where the page numbers are located
 *
 *  @author Gino Glik
 *  @since 1.0
 *  @version 1.0
 */
public enum NumerationPosition {

    /**
     *  The numbers are located in the top left corner, using the style guide's margin to the left and top.
     */
    TOP_LEFT,

    /**
     *  The numbers are located in the top centre, using the style guide's margin to the top.
     */
    TOP,

    /**
     *  The numbers are located in the top right corner, using the style guide's margin to the right and top.
     */
    TOP_RIGHT,

    /**
     *  The numbers are located in the bottom left corner, using the style guide's margin to the left and bottom.
     */
    BOTTOM_LEFT,

    /**
     *  The numbers are located in the bottom centre, using the style guide's margin to the bottom.
     */
    BOTTOM,

    /**
     *  The numbers are located in the bottom right corner, using the style guide's margin to the right and bottom.
     */
    BOTTOM_RIGHT

}
