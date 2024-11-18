# Structural Instructions

One essential part of writing documents are of course the many types of additional structures like chapters, 
tables of contents, title pages, document headers, and so on. So let us now consider the structural instructions 
available in Pipp!

## Title Page

Many documents start by offering an entire page dedicated to presenting the author, general background information 
about the document, the date of publication, etc.

In Pipp, you can render a title page using the `titlepage` keyword. You can see that all Pipp keywords are written 
in small capitals without spaces.

> The `titlepage` instruction makes use of the following project configurations: `author` (defines the author or 
> authors of the document), `assessor` (defines the individual or individuals assessing the document), 
> `publication title` (defines the title of the publication. This could be a certain course, an actual publisher, etc.),
> and `publication date` (defines the date that the document has been published. 
> If left out, Pipp will use the date of compilation)

Instruction Definition:
`titlepage`

The instruction takes no arguments

_Additional Notes:_
- If used, there can be no `header` instruction in the document.
- Some style guides (MLA, for example) prefer a simple header over an entire title page in certain scenarios. You can still use a title page, but Pipp will warn you about that

# Header

A header is usually used, if the style guide does not use an entire title page. It introduces the reader to the topic, 
author, and other important information.

> The `header` instruction makes use of the project's configurations, so make sure the `config` instruction is set up 
> properly.

Instruction Definition:
`header`

The instruction takes no arguments

> If used, there can be no `titlepage` instruction in the document.

## Table of Contents

Especially more lengthy documents may also rely on a section that lists all chapters, subchapters and their respective 
page numbers. In Pipp, this is done using the `tableofcontents` keyword.

Pipp automatically creates documents using the proper chapters and their pages as defined in the document!

Instruction Definition:
`tableofcontents`

The instruction accepts no arguments

# Blank Page

A blank page adds no content to the current position of the current page, adds a completely new, empty page after it, 
and sets the document position to after the new, blank page.

Instruction Definition:
`blank`

The instruction accepts no arguments

# Appendix

In Pipp, the appendix is included using the `appendix`.

An appendix lists further material you want to include in your paper. It automatically includes all files in your 
`appendix/` folder.

Instruction Definition:
`appendix`

The instruction accepts no arguments

# Bibliography

Most style guides use some form of bibliography (it may be called something else, like works cited) to list used 
sources. In Pipp, this section is included using the `bibliography` keyword.

It includes only those sources of the `bibliography.bib` file that have been referenced in your document using the
`citation` instruction. 