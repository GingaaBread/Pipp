# Configurations

The project can be configured using the `config` keyword.
There are two things you might want to configure:

1. project specific configurations (this can include the title of your document, the names of the authors, etc.)
2. style guide specific configurations

## 1. Project Configurations

The root of all project configurations is the `config` keyword, which is the parent of all configurations in Pipp. 
Note that if declared, you **MUST** define at least one configuration.

### Document Author(s)

This allows you to specify the names of the authors of the document. This is rendered when you use the `header` or 
`titlepage`instructions, and is automatically included in the metadata of the created document. 
Depending on the numeration style, this is also rendered in the numeration (next to the page number).

It uses the `author` keyword, which **MUST** be a direct child of the `config` keyword. 
It accepts the following options:

- Text Block: used for a single author. Pipp will use spaces to parse the name. The section before the last space will be the first name of the author, and the rest will be the last name. This configuration can be useful if the name of the author just consists of a single last name and if there is no title (Prof., Dr., etc.). An example would be `author "John Doe"`
- `name`: this has the same use as a text block, but it allows the additional use of the `title` configuration listed below
- `firstname`: used to specify the first name of a single author. If used the `name` configuration cannot be used. Must appear before the `lastname` configuration.
- `lastname`: used to specify the last name of a single author. If used the `name` configuration cannot be used.
- `title`: used to specify the title of a single author. This can be anything like Prof., Dr., etc. Must appear before name configurations.
- `id`: used to identify an author. This could be a matriculation number, student ID, company ID, etc.
- `of`: used when there are multiple authors. Lists one author by increasing tabulation

_Examples:_

```
config
	author "John Doe"
```
```
config
	author
 		name "John Doe"
```
```
config
	author
 		title "Dr."
 		name "John Doe"
 		id "22914"
```
```
config
	author
 		title "Dr."
 		firstname "John"
		lastname "Doe"
```
```
config
	author
		of
 			title "Dr."
 			firstname "John"
			lastname "Doe"
```
```
config
	author
		of "John Doe"
		of "Evil Eve"
```

### Document Assessor(s)

Analogously, you can define the assessors of the document. Assessors receive an optional `role` configuration, which 
describes the role of the assessor. Assessors are individuals that assess the document in order to rate it, edit it, 
proofread it, etc.

It uses the `assessor` keyword, which **MUST** be a direct child of the `config` keyword.
It accepts the following options:

- Text Block: used for a single assessor. Pipp will use spaces to parse the name. The section before the last space will be the first name of the assessor, and the rest will be the last name. This configuration can be useful if the name of the assessor just consists of a single last name and if there is no title (Prof., Dr., etc.). An example would be `assessor "John Doe"`
- `name`: this has the same use as a text block, but it allows the additional use of the `title` configuration listed below
- `firstname`: used to specify the first name of a single assessor. If used, the `name` configuration cannot be used. Must appear before the `lastname` configuration.
- `lastname`: used to specify the last name of a single assessor. If used, the `name` configuration cannot be used.
- `title`: used to specify the title of a single assessor. This can be anything like Prof., Dr., etc. and must appear before name configurations.
- `role`: used to specify the role of an assessor. This could be "Instructor", "Editor", etc.
- `of`: used when there are multiple assessors. Lists one assessor and increments the tabulation level

_Examples:_

```
config
	assessor "John Doe"
```
```
config
	assessor
 		name "John Doe"
```
```
config
	assessor
 		title "Dr."
 		name "John Doe"
 		role "Instructor"
```
```
config
	assessor
 		title "Dr."
 		firstname "John"
		lastname "Doe"
```
```
config
	assessor
		of
 			title "Dr."
 			firstname "John"
			lastname "Doe"
```
```
config
	assessor
		of "John Doe"
		of
		    name "Evil Eve"
```

### Document Title

The title of the document can be defined in the `title` configuration. It is a direct child of the `config` keyword.
This is rendered in `title` blocks, and is automatically included in the document's metadata.

The title can consist of a simple text block, or of a mix of text, work reference and emphasis blocks.
Using a work reference or emphasis block increments the tabulation level. Simple text blocks can follow immediately
after the `title` keyword.

_Examples:_
```
config
	title "My First Document"
```

```
config
	title 
	    "Character Relations In "
        work "The Hobbit"
```

```
config
	title 
	    "French Words And Their "
        emphasise "L'effet"
```

```
config
	title 
	    "The Use Of The Phrase "
        emphasise "Winter Is Coming"
        "In The Series"
        work "Game Of Thrones"
```

### Document Publication

The `publication` keyword is a subgroup that configures information about the publication of the document. 
It **MUST** be a direct child of the `config` keyword.

#### Publication Context Title

The `title` keyword can be used to define the title of the publication. For example, the name of a course.

_Examples:_
```
config
	publication
		title "Statistics 24"
```
```
config
	publication
		title "Pipp Publications"
```

#### Publication Date

The `date` keyword can be used to define the date of publication. If left out, the compiler will use the date of
compilation. If no date should be supplied, use the `date "None"` option. A custom date must be specified as an option
using the British Date Format `dd/MM/YYYY`.

_Examples:_
```
config
	publication
		date "19/03/1995"
```
```
config
	publication
		date "None"
```

### Document Type

The document type is currently a reserved configuration with no effect. 
It uses the `type` keyword as a direct child of the `config` keyword and currently only accepts the `type "Paper"` 
option.

> This reserved configuration should currently not be overridden or explicitly defined

### Style Guide

To define the style guide of your choice, use the `style` configuration. It is a direct child of the `config` keyword 
and currently only accepts the `style "MLA9"` option.

## 2. Style Configurations

The style configuration allows setting the style guide and overriding its values.
To override a base style, the `style` keyword is used as a direct child of the `config` keyword.
The base style **MUST** follow immediately using the `of` keyword, and provides the set of styles that can be overridden.
If the `of` keyword is used to declare that a style should be overridden, at least one style **MUST** be overridden.

_Example:_
```
config
	style
		of "MLA9"
```

### Layout Override

The `layout` keyword is used as a direct child of the `style` keyword. It accepts the following options:

#### Layout Height

To change the height of the layout, the `height` keyword is used as a direct child of the `layout` keyword.
This configuration specifies the height of a page in the document. The height is specified either in _millimeters_ or
in _inches_. To use millimeters, specify a positive floating point or integer numeral (2, 5.5, 15.125).
To use inches, end the numeral with the `in` suffix.

_Example:_
```
config
	style
		of "MLA9"
		layout
		    height "11in"
```

#### Layout Width

To change the width of the layout, the `width` keyword is used as a direct child of the `layout` keyword.
This configuration specifies the width of a page in the document. The width is specified either in _millimeters_ or
in _inches_. To use millimeters, specify a positive floating point or integer numeral (2, 5.5, 15.125).
To use inches, end the numeral with the `in` suffix.

_Example:_
```
config
	style
		of "MLA9"
		layout
		    width "8"
```

#### Layout Margin

To change the margin of the layout, the `margin` keyword is used as a direct child of the `layout` keyword.
This configuration specifies the margin from all four sides of all pages in the document to the text blocks.
The margin is specified either in _millimeters_ or in _inches_. To use millimeters, specify a positive floating point 
or integer numeral (2, 5.5, 15.125).
To use inches, end the numeral with the `in` suffix.

_Example:_
```
config
	style
		of "MLA9"
		layout
		    margin "1in"
```

#### Layout Spacing

To change the spacing of the layout, the `spacing` keyword is used as a direct child of the `layout` keyword.
This configuration specifies the spacing between lines in all pages of the document.
The spacing is specified as a positive integer or floating point numeral.

> Note that this value is not a unit of measurement converted to points!
> The spacing value 2 describes double-spacing, etc. 

_Example:_
```
config
	style
		of "MLA9"
		layout
		    spacing "2"
```

### Numeration Override

The `numeration` keyword is used as a direct child of the `style` keyword. It accepts the following options:

#### Numeration Alphabet

The `in` configuration sets the alphabet used for numerating all pages. This accepts the following options:
1. Arabic: numerates pages using arabic page numerals (1, 2, 3, ...)
2. Roman: numerates pages using roman page numerals (I, II, III, ...)

_Examples:_
```
config
	style
		of "MLA9"
		numeration
		    in "Arabic"
```

```
config
	style
		of "MLA9"
		numeration
		    in "Roman"
```

#### Numeration Display Position

The `display` keyword defines the position of all page numerations. This accepts the following options:
1. Top Left
2. Top
3. Top Right
4. Bottom Left
5. Bottom
6. Bottom Right

_Examples:_
```
config
	style
		of "MLA9"
		numeration
		    display "Bottom"
```

```
config
	style
		of "MLA9"
		numeration
		    display "Top Left"
```

> Note that right alignments are reserved and not yet implemented

#### Numeration Margin

Sets the margin of the numeration to all sides of the page.
The margin is specified either in _millimeters_ or in _inches_. To use millimeters, specify a positive floating point
or integer numeral (2, 5.5, 15.125).
To use inches, end the numeral with the `in` suffix.

_Examples:_
```
config
	style
		of "MLA9"
		numeration
		    margin "2"
```

```
config
	style
		of "MLA9"
		numeration
		    margin "5.5"
```

```
config
	style
		of "MLA9"
		numeration
		    margin "3in"
```

#### Numeration Author Display

The `author` keyword defines how authors should be displayed in the numeration.
It accepts the following options:
1. `name`: Uses the name of the author without any title (John Doe)
2. `firstname`: Uses the firstname of the author (John)
3. `lastname`: Uses the lastname of the author (Doe)
4. `"Full Name"`: Uses the full name of the author including any title (Prof. John Doe)
5. `"None"`: Does not render the name of the author

_Examples:_
```
config
	style
		of "MLA9"
		numeration
		    author lastname
```

```
config
	style
		of "MLA9"
		numeration
		    author "Full Name"
```

```
config
	style
		of "MLA9"
		numeration
		    author "None"
```

#### Ignored Pages

The `skip` keyword defines the pages that should not be numerated. The next page that will be numerated again, uses
that skipped page number and continues the default numeration flow. For example, if there are five pages and page two 
should not be numerated,
page one will receive page number one, two will receive no page number, three will receive page number two, and so on.
To skip a page during numeration, the `skip` keyword is used as a direct child of the `numeration` keyword.
It allows the following options:
1. single page number: the page number as a positive integer (page numbers start at 1)
2. page number span: the page number range separated by a hyphen (for example, 5-12, 6-40, etc.)
3. a list of the prior options separated by the list separator token (for example, "4", "5-12", "9")

> If a page number span is used, the left page number **MUST** be less than the right page number.

_Examples:_
```
config
	style
		of "MLA9"
		numeration
		    skip "2"
```

```
config
	style
		of "MLA9"
		numeration
		    skip "4-12"
```

```
config
	style
		of "MLA9"
		numeration
		    skip "1", "3", "5-12"
```

### Structure Override

The `structure` keyword is used as a direct child of the `style` keyword. It allows changing the following structures:
1. sentences, using the `sentence` keyword
2. emphasised text structures, using the `emphasise` keyword
3. work reference structures, using the `work` keyword
4. paragraphs, using the `paragraph` keyword

#### Sentence Structure

The `sentence` keyword is used as a direct child of the `structure` keyword. It allows changing the following structures:

##### Sentence Font

The `font` configuration allows changing the font used for default sentences.
This accepts the following three attributes:
1. `name`: the name of the desired font
2. `size`: the size of the desired font
3. `colour`: the colour of the desired font as a hexadecimal 6-digit string prefaced by the `#` hash character

_Example:_
```
config
	style
		of "MLA9"
		structure
		    sentence
		        font
		            name "Times Roman"
		            size "13"
		            colour "#000000"
```

##### Sentence Prefix

The `before` configuration allows changing the text that should be inserted before each sentence.

_Example:_
```
config
	style
		of "MLA9"
		structure
            sentence
                before " "
```

#### Paragraph Structure

The paragraph structure allows changing the indentation.
It uses the `indentation` keyword followed by a text block of the non-negative indentation amount in millimeters, or
if the `in` suffix follows the integer, in inches.

#### Work Structure

The `work` keyword is used as a direct child of the `structure` keyword. It allows changing the following structures:

##### Work Font

The `font` configuration allows changing the font used for work references.
This accepts the following three attributes:
1. `name`: the name of the desired font
2. `size`: the size of the desired font
3. `colour`: the colour of the desired font as a hexadecimal 6-digit string prefaced by the `#` hash character

_Example:_
```
config
	style
		of "MLA9"
		structure
		    sentence
		        font
		            name "Times Roman"
		            size "13"
		            colour "#000000"
```

#### Emphasis Structure

The `emphasise` keyword is used as a direct child of the `structure` keyword. It allows changing the following structures:

##### Emphasis Font

The `font` configuration allows changing the font used for default sentences.
This accepts the following three attributes:
1. `name`: the name of the desired font
2. `size`: the size of the desired font
3. `colour`: the colour of the desired font as a hexadecimal 6-digit string prefaced by the `#` hash character

   _Example:_
```
config
	style
		of "MLA9"
		structure
		    sentence
		        font
		            name "Times Roman"
		            size "13"
		            colour "#000000"
```