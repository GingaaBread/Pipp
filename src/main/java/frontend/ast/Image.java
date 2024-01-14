package frontend.ast;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Image extends BodyNode {

    /**
     * The id of the image, relative to the img/ folder.
     * For example, "Dog.png".
     */
    private String id;

    /**
     * The width of the image in either millimeters or inches.
     */
    private String width;

    /**
     * The height of the image in either millimeters or inches.
     */
    private String height;

    /**
     * The size of the image, as an alternative to setting the width and height.
     * If used, this is a relative percentage of the image's default size.
     */
    private String size;

    @Override
    public void handleBodyElement() {

    }

    @Override
    protected void checkForWarnings() {

    }
}
