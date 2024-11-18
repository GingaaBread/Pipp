Error Codes:
=============

## 3.1 Configuration Errors

### 311

*Description:*
An assessor requires a name configuration, but neither name, firstname nor lastname
has been configured.

*Cause*:
This error occurs when trying to create an assessor in the configuration, but
not supplying an appropriate `name` or `firstname` and `lastname` configuration.

*Example:*

```pipp
config
    assessor
        role "Instructor"
```

*Fix:*
Supply either `firstname` and `lastname`, or `name`.

### 312

*Description:*
An assessor can only be given a name configuration OR a firstname and lastname
configuration.

*Cause*:
This error occurs when trying to create an assessor in the configuration, but
supplying both a `name` and `firstname` and `lastname` configuration.
The `name`configuration is used to automatically generate the `firstname` and `lastname`
configurations. If these are used,
the `name` configuration should be omitted (or vice-versa).

*Example:*

```pipp
config
    assessor
        firstname "John"
        lastname "Doe"
        name "John Doe"
```

*Fix:*
Remove either `firstname` and `lastname`, or `name`.

### 313

*Description:*
An assessor cannot only have a firstname configuration.
Either also provide a lastname configuration or only use the name configuration.

*Cause*:
This error occurs when trying to create an assessor in the configuration, but
only supplying the firstname configuration without supplying the lastname configuration.
Since it does not suffice to only supply one's first name in academic writing, this error
is supposed to prevent oversights.

*Example:*

```pipp
config
    assessor
        firstname "John"
```

*Fix:*
Remove either `firstname` and add `name`, or add `lastname`.

### 314

*Description:*
An assessor cannot only have a lastname configuration.
Either also provide a firstname configuration or only use the name configuration.

*Cause*:
This error occurs when trying to create an assessor in the configuration, but
only supplying the lastname configuration without supplying the firstname configuration.
Since it does not suffice to only supply one's last name in academic writing, this error
is supposed to prevent oversights.

*Example:*

```pipp
config
    assessor
        lastname "Doe"
```

*Fix:*
Remove either `lastname` and add `name`, or add `firstname`.

### 315

*Description:*
An assessor cannot have a role configuration without also having a
proper name configuration. Please use a name configuration or both a
firstname and lastname configuration.

*Cause*:
This error occurs when trying to create an assessor in the configuration, but
supplying a role without properly supplying the assessor's name. That means that
either:

- only the `firstname`, but not the `lastname` configuration is given
- only the `lastname`, but not the `firstname` configuration is given
- no name configuration is given

*Example:*

```pipp
config
    assessor
        lastname "Doe"
        role "Instructor"
```

*Fix:*
Remove either `lastname` and add `name`, or add `firstname`.

### 316

*Description:*
An author requires a name configuration, but neither name, firstname nor lastname
has been configured.

*Cause*:
This error occurs when trying to create an author in the configuration, but
not supplying an appropriate `name` or `firstname` and `lastname` configuration.

*Example:*

```pipp
config
    author
        id "MD110025"
```

*Fix:*
Supply either `firstname` and `lastname`, or `name`.

### 317

*Description:*
An author can only be given a name configuration OR a firstname and lastname
configuration.

*Cause*:
This error occurs when trying to create an author in the configuration or bibliography, but
supplying both a `name` and `firstname` or `lastname` configuration.
The `name`configuration is used to automatically generate the `firstname` and `lastname`
configurations. If these are used,
the `name` configuration should be omitted (or vice-versa).

*Example:*

```pipp
config
    author
        firstname "John"
        lastname "Doe"
        name "John Doe"
```

*Fix:*
Remove either `firstname` and `lastname`, or `name`.

### 318

*Description:*
An author cannot only have a firstname configuration.
Either also provide a lastname configuration or only use the name configuration.

*Cause*:
This error occurs when trying to create an author in the configuration, but
only supplying the firstname configuration without supplying the lastname configuration.
Since it does not suffice to only supply one's first name in academic writing, this error
is supposed to prevent oversights.

*Example:*

```pipp
config
    author
        firstname "John"
```

*Fix:*
Remove either `firstname` and add `name`, or add `lastname`.

## 319

*Description:*
The style guide does not allow the use of emphasis, but you are trying to emphasise text nonetheless.

*Cause*:
This error occurs when trying to use the `emphasis` keyword to emphasise a text passage in your document, but
the style guide you are using, or its overridden configuration, does not allow the use of emphasis.

*Example:*

```pipp
config
    style "Some Style Example That Does Not Allow Emphasis"
emphasise "Example"            
```

*Fix:*
You should follow the style guide and remove the emphasis and use a normal text block, instead.

### 3110

*Description:*
The specified paragraph indentation exceeds the page width.

*Cause*:
This error occurs when the width of the layout is less than the specified paragraph indentation. Both types are
compared in points as their units.

*Example:*

```pipp
config
	style
		of "MLA9"
		layout
			width "8in"
		structure
			paragraph
				indentation "10in"
```

*Fix:*
Change the layout width to be more than the paragraph indentation, or change the indentation to be less than the
layout width.

### 3111

*Description:*
The specified chapter depth exceeds the maximum allowed by the used style guide.

*Cause*:
This error occurs when configuring a chapter with a depth that exceeds the maximum depth.
For example, a style guide that allows a maximum chapter nesting depth of five subchapters will cause an issue when
you attempt to configure the sixth subchapter as it should not exist.

*Example:*

```pipp
config
	style
		of "MLA9"
		structure
			chapter "999"
#               ...
```

*Fix:*
Change the layout width to be more than the paragraph indentation, or change the indentation to be less than the
layout width.

## 3.2 Missing Member Errors

### 321

*Description:*
A text component cannot be blank

*Cause*:
This error occurs when trying to create a text, but only adding whitespace or no
content at all to the text.

*Example:*

```pipp
config
    title "  "
```

*Fix:*
Add actual text to the text component.

### 322

*Description:*
Cannot render a title if no title has been configured.

*Cause*:
This error occurs when trying to use the `title` instruction, but no title has been configured.

*Example:*

```pipp
title
```

*Fix:*
Remove the title instruction or include the title in the configuration.

### 323

*Description:*
The specified style guide is either missing or does not exist.
Check if it has been imported correctly, or if you misspelled
the style guide name in the configuration.

*Cause*:
This error occurs when trying to use a style guide name, which
the `StyleTable` cannot translate to an actual style guide object.
If you are using a pre-existing style guide, make sure it is
not spelled wrong. If it is a custom style guide, make sure it is
imported correctly.

*Example:*

```pipp
config
    style "Mla9"
```

*Fix:*
Use a proper style guide name

### 324

*Description:*
The specified page numeration does not exist.

*Cause*:
This error occurs when trying to use a numeration type, which
the `Processor` cannot translate to an actual numeration type
enumeration constant.

*Example:*

```pipp
config
    numeration
        in "roman"
```

*Fix:*
Use a proper page numeration name like "Arabic" or "Roman"

### 325

*Description:*
The specified page position does not exist.

*Cause*:
This error occurs when trying to use a numeration position, which
the `Processor` cannot translate to an actual numeration position
enumeration constant.

*Example:*

```pipp
config
    numeration
        display "Center"
```

*Fix:*
Use a proper page position name like "Top", "Bottom Left" or "Top Right"

### 326

*Description:*
The specified font is missing or does not exist.

*Cause*:
This error occurs when trying to use a custom font, which the compiler
cannot use as an actual font for the document. If you have used a
font supported by Pipp, check that it is spelled correctly.

*Example:*

```pipp
config
    style
        of "MLA9"
        structure
            sentence
                font
                    name "Does Not Exist"
```

*Fix:*
Use a proper font name

### 327

*Description:*
The specified windows font cannot be located.

*Cause*:
This error occurs when trying to use a custom font imported from a windows operating system, but the desired font
cannot be located in the font folder.

*Example:*

```pipp
config
    style
        of "MLA9"
        structure
            sentence
                font
                    name "@Does Not Exist"
```

*Fix:*
The compiler uses the following path to windows fonts: C:\Windows\Fonts. Check if the specified font name exists at
that path, and install it if it does not exist. Also make sure that it is a .ttf file and that the compiler has
read access to the font folder. Make sure the font name is spelt exactly like in the font folder and only uses a single
@ prefix to indicate that it is a "windows"-font.

### 328

*Description:*
Cannot use a publication chair if no publication institution is defined.

*Cause*:
This error occurs when trying to use a publication chair without having defined a publication institution.

*Example:*

```pipp
config
    publication
        chair "No Institution"
```

*Fix:*
A chair is a subtype of an institution (for example, a faculty of a university), therefore you **MUST** declare an
institution if you want to use the chair attribute. To fix this error, either remove the publication chair configuration
or include a publication institution configuration.

### 329

*Description:*
Image with the image id '[ID]' does not exist in the img/ folder. Make sure it also has its file ending defined.

*Cause*:
This error occurs when trying to use render an image using the `image` instruction, but its identifier does not exist
in the `img/` image source folder. Note that the identifier must also include the file ending, including the `.`, in
order to be referenced.

*Example:*

```pipp
img "I do not exist in img/"
```

*Fix:*
Check if the identifier you have defined in the `image` instruction exactly matches the name of the image file in the
`img/` folder. Then make sure both file endings are the same. Note the case sensitivity.

### 3210

*Description:*
Cannot use a citation without referencing a source from the bibliography.

*Cause*:
This error occurs when trying to use a citation using the `citation` keyword, but leaving out the `id` keyword.
The identifier is used to specify which source from the bibliography should be referenced, so it must not be left out.

*Example:*

```pipp
citation
    of "Hello World"
```

*Fix:*
Add an `id` to the citation instruction

## 3.3 Incorrect Format Errors

### 331

*Description:*
The specified date is not `None` and does not adhere to the
British date format: `dd/MM/yyyy` For example, June 3, 2023, is 03/06/2023.

*Cause*:
This error occurs when trying to create a date, but using an illegal date format,
such as the American `(MM/dd/yyyy)` or International `(yyyy-dd-mm)` date format, and
the date is not set to `None`.

*Example:*

```pipp
config
    publication
        title "Example"
        date "June 3, 2023"
```

*Fix:*
Use the British date format `dd/MM/yyyy` or `None`.

### 332

*Description:*
Non-negative decimal expected.

*Cause*:
This error occurs when trying to represent a number, which is not a non-negative decimal.
An example is a `-0.5` padding.

*Example:*

```pipp
config
    structure
        paragraph
            indentation "This is not a number"
```

*Fix:*
Use a non-negative decimal (0.0, 23.5, 105.125, ...)

### 333

*Description:*
Non-negative integer expected.

*Cause*:
This error occurs when trying to represent a number, which is not a non-negative integer.
An example is a `-5` font size.

*Example:*

```pipp
config
    font
        size "25.5"
```

*Fix:*
Use a non-negative decimal (0.0, 23.5, 105.125, ...)

### 334

*Description:*
Colour expected.

*Cause*:
This error occurs when trying to represent a hexadecimal colour.
Make sure the colour begins with a '#' and is followed by exactly six hexadecimals.

*Example:*

```pipp
config
    font
        colour "#eee"
```

*Fix:*
Use the `#` hash character followed by exactly six hexadecimals (0,1,2,3,4,5,6,7,8,9,0,a,b,c,d,e, or f).

### 335

*Description:*
Allowance type expected.

*Cause*:
This error occurs when trying to set an allowance type, but not supplying either
"Yes", "No" or "If Necessary". Check if you misspelled either of these.

*Example:*

```pipp
config
    structure
        sentence
            bold "yes"
```

*Fix:*
Use either "Yes", "No" or "If Necessary"

### 336

*Description:*
Content alignment type expected.

*Cause*:
This error occurs when trying to set a content alignment which is neither `Left`, `Right` or `Center`.

*Example:*

```pipp
img
    id "Dog.png"
    display "Middle"
#   Should be "Center"
```

*Fix:*
Use either "Left", "Right" or "Center".

### 337

*Description:*
Deprecated.

*Cause*:
/

*Example:*

```pipp
/
```

*Fix:*
/

### 338

*Description:*
Document type expected.

*Cause*:
This error occurs when trying to refer to a document type.
Make sure you spelled the type correctly.

*Example:*

```pipp
config
    type "Novel"
```

*Fix:*
Use a correct document type

### 339

*Description:*
Could not parse name property.

*Cause*:
This error occurs when trying to use the `name` configuration,
instead of using the `firstname` and `lastname` configurations,
but not providing a space. Pipp uses a space to determine
what is part of the first and what is part of the last name.
For example, "John Doe" results in the firstname `John`,
and the last name `Doe`.
The error also occurs when providing a blank string.

*Example:*

```pipp
config
    author "Doe"
```

*Fix:*
Provide a space or use the `firstname` and `lastname` configurations,
instead, and provide a non-blank string.

### 3310

*Description:*
Incorrect spacing constant.

*Cause*:
This error occurs when trying to use a custom spacing,
but neither supplying `1`, `1.5` or `2` (with our without the optional
`in` ending if you want to use inches, instead of millimeters).

*Example:*

```pipp
config
    style
        layout
            spacing "3"
```

*Fix:*
Use either `1`, `1.5` or `2` (with our without the optional
`in` ending).

### 3311

*Description:*
A page span must include exactly two page-numbers.

*Cause*:
This error occurs when trying to use a page span, but either
supplying too many or too little pages in the span.
For example, `5-12` is a legal page span, whereas
`5` (only one page is specified) and `5-12-25`
(three pages are specified) are not.

*Example:*

```pipp
config
    style
        numeration
            skip "5-12-25"
```

*Fix:*
Use exactly two page-numbers in a page span.

### 3312

*Description:*
The second page-number must be greater than the first page-number
in a page span.

*Cause*:
This error occurs when trying to use a page span, but specifying
that the destination number is larger than the origin number.
For example, `3-5` is legal (the destination is larger than the origin),
whereas `5-3` (the origin is larger than the destination) and `5-5`
(the origin and destination are the same) are not.

*Example:*

```pipp
config
    style
        numeration
            skip "5-3"
```

*Fix:*
Increase the second page-number, so that it is greater than the first.

### 3313

*Description:*
Integer larger than zero expected.

*Cause*:
This error occurs when trying to refer to a page number, which is not
an integer greater than 0. Note that the first page number is always 1.
It can also occur when trying to use a custom font size, which is 0 or less.

*Example:*

```pipp
config
    style
        numeration
            skip "0"
```

*Fix:*
Use an integer greater than 0.

### 3314

*Description:*
Author numeration name expected.

*Cause*:
This error occurs when trying to specify how the names of the authors
should be displayed before the page number, but not specifying either
the first, last or full name OR specifying that no name should be displayed.
Use `firstname` if you want to display the first names of all authors,
`lastname` if you want to display the last names of all authors,
`name` if you want to display the full names of all authors, or the text
`"None"` if you do not want to display the authors' names at all.

*Example:*

```pipp
config
    style
        numeration
            author "none"
```

*Fix:*
Use either `firstname`, `lastname`, `name` or `"None"`.

### 3315

*Description:*
Positive integer percentage expected.

*Cause*:
This error occurs when a positive integer percentage expected, but the input is zero, negative, not an integer, or
does not end with the `%` percentage character.

*Examples:*

```pipp
img
    id "Dog.png"
    size "25 percent"
#   Should be "25%"
```

```pipp
img
    id "Dog.png"
    size "0%"
#   Should be more than 0
```

```pipp
img
    id "Dog.png"
    size "-25%"
#   Should be more than 0
```

*Fix:*
Use the proper percentage format `x%`, where x is a positive integer.

## 3.4 Content Errors

### 341

*Description:*
Image with ID '[ID]' is too large to fit on a single page.

*Cause*:
This error occurs when trying to render an image that would be too large to fit on a single page. This takes into
consideration leading and layout margins.

*Example:*

```pipp
img "VeryLarge.png"
```

*Fix:*
Resize the image using either `size` or `width` / `height`, or change the `layout` dimensions.

### 342

*Description:*
Image with ID '[ID]' is too wide to fit on a page.

*Cause*:
This error occurs when trying to render an image that would be too wide to fit on the current page. This takes into
consideration leading and layout margins. The available width is less than the width of the image.

*Example:*

```pipp
img "VeryWide.png"
```

*Fix:*
Resize the image using either `size` or `width` / `height`, or change the `layout` dimensions.

### 343

*Description:*
Bibliography entry with ID '[ID]' already exists.

*Cause*:
This error occurs when defining a bibliography item with an ID that is already used for another bibliography item.
Bibliography entries must all have a unique identifier.

*Example:*

```pipp
bibliography
    id "My ID"
#   ...
    id "My ID"
```

*Fix:*
Change the ID to be a unique ID.

### 344

*Description:*
Bibliography entry with ID '[ID]' does not exist.

*Cause*:
This error occurs when using a citation using the `citation` instruction, but leaving referencing a source from the
bibliography that does not exist.

*Example:*

```pipp
#   In Bibliography:
bibliography
    id "My ID"
#   In document:
citation "My IDD", "XYZ"
```

*Fix:*
Change the ID to be the correct ID of the desired bibliography source.

### 345

*Description:*
Chapter '[Name]' is one or more chapter levels too deep.

*Cause*:
This error occurs when using a chapter with a level that would exceed the current chapter level by two or more.
For example, after a chapter "1" may follow a subchapter "1.1", but not a sub-subchapter "1.1.1" or sub-sub-subchapter
"1.1.1.1".

*Example:*

```pipp
chapter "Introduction"
				chapter "Too deep"
```

*Fix:*
Change the chapter level by reducing indentation until its level is one higher than the current chapter level or less.

### 346

*Description:*
The page format is too narrow to render author details.

*Cause*:
This error occurs when using a page layout that cannot display at least one detail of an author. All details **must** be displayed in a single line.

*Example:*

```pipp
config
    author "John Doe"
    style
        of "MLA9"
        layout
#           too narrow to fit "John Doe" in a single line:
            width "0.01in"
```

*Fix:*
Increase the width of the document to allow the author details to be rendered in a single line.