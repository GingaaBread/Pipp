package processing;

/**
 *  The StructureType enumeration declares all structural keywords that can be used in Pipp.
 *  Structures typically create a particular type of structure without requiring a specific text, and
 *  "stand on their own". Note that the constants do not use UPPER_CASE_SNAKE case to make the parsing
 *  in the processing phase easier.
 *
 *  @author Gino Glink
 *  @version 1.0
 *  @since 1.0
 */
public enum StructureType {

    /**
     *  The table of contents usually lists the chapters and sections of a document
     */
    TABLEOFCONTENTS,

    /**
     *  The title page usually provides the most general information about the document and author to the reader
     */
    TITLEPAGE,

    /**
     *  The title generates the title of the document. This is used to separate the header from the title.
     */
    TITLE,

    /**
     *  The appendix usually lists material that hase been used in the document
     */
    APPENDIX,

    /**
     *  A blank page adds an empty page without any content, but is usually included in the page numeration
     */
    BLANKPAGE,

    /**
     *  An abstract usually contains the most vital aspects of a document in a concise manner
     */
    ABSTRACT,

    /**
     *  An endnotes section usually contains the author's last words
     */
    ENDNOTES,

    /**
     *  A preface section usually introduces the reader to the document by having introductory first sentences
     */
    PREFACE,

    /**
     *  An acknowledgements section usually allows the author to acknowledgements other people and sources
     */
    ACKNOWLEDGEMENTS,

    /**
     *  An abbreviations section usually lists all abbreviations and their meanings
     */
    ABBREVIATIONS,

    /**
     *  A figures section usually lists all figures used in a document
     */
    FIGURES,

    /**
     *  A tables section usually lists all tables used in a document
     */
    TABLES,

    /**
     *  A glossary section usually lists all technical or foreign terms and their meanings
     */
    GLOSSARY,

    /**
     *  An academic integrity section is usually included to show that the author is aware of their work
     *  being of "good academic nature", which, among other things, means that all ideas that do not stem from
     *  the author have been marked as such and have been cited in an appropriate manner.
     */
    ACADEMICINTEGRITY,

    HEADER,
    /**
     *  A bibliography or works cited section usually lists all cited material
     */
    BIBLIOGRAPHY

}
