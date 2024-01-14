package creation;

import error.ContentException;
import error.MissingMemberException;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import processing.Processor;

import java.io.File;
import java.io.IOException;

public class ImageRenderer {

    private static final String IMAGE_INPUT_PATH = "src/main/resources/img/";

    public static void render(@NonNull String imageId, @NonNull ContentAlignment imageAlignment, Integer imageSize) {
        try {
            final float
                    leading = Processor.getLeading(),
                    availableWidth = Processor.getAvailableContentWidth();

            PDImageXObject imageObject;
            try {
                var file = new File(IMAGE_INPUT_PATH + imageId);

                imageObject = PDImageXObject.createFromFileByExtension(file, PageAssembler.getDocument());

                if (imageSize != null) {
                    float scale = imageSize / 100.0f;
                    imageObject.setHeight(Math.round(imageObject.getHeight() * scale));
                    imageObject.setWidth(Math.round(imageObject.getWidth() * scale));
                }
            } catch (IllegalArgumentException | IOException e) {
                throw new MissingMemberException("9: Image with the image id '" + imageId + "' does not exist in the " +
                        "img/ folder. Make sure it also has its file ending defined.");
            }

            var targetYPosition = PageCreator.currentYPosition - imageObject.getHeight();

            // Check if the image does not fit on the current page anymore
            if (targetYPosition < Processor.margin) {
                PageCreator.createNewPage();
                targetYPosition = PageCreator.currentYPosition - imageObject.getHeight();

                // Check if the image is too large to even fit on an empty page
                if (targetYPosition < Processor.margin)
                    throw new ContentException("1: Image with ID '" + imageId + "' is too large to fit on a single page.");
            }

            if (imageObject.getWidth() > availableWidth)
                throw new ContentException("2: Image with ID '" + imageId + "' is too wide to fit on a page.");

            final var contentStream = new PDPageContentStream(PageAssembler.getDocument(), PageCreator.getCurrent(),
                    PDPageContentStream.AppendMode.APPEND, false);

            contentStream.drawImage(imageObject, Processor.margin, targetYPosition);

            PageCreator.currentPageIsEmpty = false;
            contentStream.close();
        } catch (IOException e) {

        }
    }

}
