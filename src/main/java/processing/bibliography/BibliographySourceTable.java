package processing.bibliography;

public class BibliographySourceTable {

    private BibliographySourceTable() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    public static BibliographySource lookup(frontend.ast.bibliography.BibliographySource entry) {
        final BibliographySource sourceToYield;
        if (entry.getType() == null) sourceToYield = new BibliographySource();
        else if (entry.getType().equals("Book")) sourceToYield = new Book();
        else sourceToYield = new BibliographySource();

        sourceToYield.createFromBibliographyEntry(entry);
        return sourceToYield;
    }

}
