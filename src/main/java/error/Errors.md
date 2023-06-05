Error Codes:
=============

*_Note that some errors can already be "intercepted" by the parser 
depending on the used compiler_

# 1. Lexing Errors

# 2. Parsing Errors

# 3. AST Node Errors

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
This error occurs when trying to create an author in the configuration, but
supplying both a `name` and `firstname` and `lastname` configuration.
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
A structure component is missing

*Cause*:
This error occurs when trying to confine a structure to only appear once
a different structure has appeared or will appear, and the specified 
keyword is not a structure or is not allowed to be used in this context.

*Example:*
```pipp
config
    structure
        endnotes
            allow
                before before
```

*Fix:*
Replace the keyword with a legal keyword.

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
The specified page numeration is either missing or does not exist.
Check if it has been imported correctly, or if you have misspelled
the numeration type in the configuration.

*Cause*:
This error occurs when trying to use a numeration type, which
the `Processor` cannot translate to an actual numeration type
enum value.
If you are using a pre-existing numeration type, make sure it is
not spelled incorrectly. If it is a custom type, make sure it is
imported correctly.

*Example:*
```pipp
config
    numeration
        in "roman"
```

*Fix:*
Use a proper page numeration name

### 325

*Description:*
The specified page position is either missing or does not exist.
Check if it has been imported correctly, or if you have misspelled
the numeration position in the configuration.

*Cause*:
This error occurs when trying to use a numeration position, which
the `Processor` cannot translate to an actual numeration position
enum value.
If you are using a pre-existing numeration position, make sure it is
not spelled incorrectly. If it is a custom position, make sure it is
imported correctly.

*Example:*
```pipp
config
    numeration
        display "Center"
```

*Fix:*
Use a proper page position name

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
    font
        name "An unknown font here"
```

*Fix:*
Use a proper font name

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
Make sure the colour begins with a '#' and is followed by exactly six integers.

*Example:*
```pipp
config
    font
        colour "#eee"
```

*Fix:*
Use a non-negative decimal (0.0, 23.5, 105.125, ...)

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
Whitespace allowance type expected.

*Cause*:
This error occurs when trying to set a whitespace allowance type,
but not supplying either "Yes", "Remove" or "Escape".
Check if you misspelled either of these.

*Example:*
```pipp
config
    structure
        sentence
            whitespace "yes"
```

*Fix:*
Use either "Yes", "Remove" or "Escape".

### 337

*Description:*
Structure type expected.

*Cause*:
This error occurs when trying to refer to a structure.
Make sure you spelled the structure correctly.

*Example:*
```pipp
config
    structure
        endnotes
            allow
                before workscited
```

*Fix:*
Use a correct structure

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