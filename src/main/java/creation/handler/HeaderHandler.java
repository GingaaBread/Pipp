package creation.handler;

import creation.stamp.HeaderStamp;
import creation.stamp.TitlePageStamp;
import processing.Processor;
import processing.constant.HeaderType;

public class HeaderHandler {

    public static void handlePageNumeration() {
        if (Processor.getHeaderType() == HeaderType.FULL_TITLE_PAGE)
            Processor.getSkippedPages().add(1);
    }

    public static void handleHeader() {
        if (Processor.getHeaderType() == HeaderType.NONE) return;

        if (Processor.getHeaderType() == HeaderType.FULL_TITLE_PAGE) {
            TitlePageStamp.renderTitlePage();
        } else if (Processor.getHeaderType() == HeaderType.SIMPLE_HEADER) {
            HeaderStamp.renderHeader();
        }
    }

}
