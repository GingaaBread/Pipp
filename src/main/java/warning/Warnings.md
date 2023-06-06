Warnings
=========

# 1. Inconsistencies

High Severity

## 11

*Description:*
At least one assessor has a role, but at least one
assessor does not have a role. Make sure you really do
not want all assessors to have a role.

*Cause:*
This warning occurs when adding at least two assessors.
If one assessor has got a role, and at least one other 
assessor has not got a role, this warning will be
displayed.

*To do:*
Decide whether you really want to have one assessor with
a role and at least one assessor without a role. It would
be more consistent to give all assessors a role, or give
no assessor a role in the first place. However, there may
be cases in which you want to omit a role, which is why
this warning is not an enforced error.

*Example:*
```pipp
config
    assessor
        of "John Doe"
        of
            name "Alice Alison"
            role "Instructor"
```

## 12

*Description:*
At least one author has an id, but at least one
author does not have an id. Make sure you really do
not want all authors to have an id.

*Cause:*
This warning occurs when adding at least two authors.
If one author has got an id, and at least one other
author has not got an id, this warning will be
displayed.

*To do:*
Decide whether you really want to have one author with
an id and at least one author without an id. It would
be more consistent to give all authors an id, or give
no author an id in the first place. However, there may
be cases in which you want to omit an id, which is why
this warning is not an enforced error.

*Example:*
```pipp
config
    author
        of "John Doe"
        of
            name "Alice Alison"
            id "MD20025"
```

## 13

*Description:*
The style configuration uses both inches and millimeters.

*Cause:*
This warning occurs when your style configuration uses
inches for at least one configuration and millimeters
for at least one configuration.

*To do:*
Decide whether you really want to have both units of
measurement in your style, or if you want to confine
yourself to only use either inches or millimeters.

*Example:*
```pipp
config
    style
        layout
            width "5"
            height "5in"
```

# 2. Missing Members

Critical Severity

## 21

*Description:*
There is no specified author. Check if you really
want to omit an author specification.

*Cause:*
This warning occurs when leaving out an author
configuration. The author configuration determines
how Pipp will display the name and ID of the authors of
the document.

*To do:*
Decide whether you really want to omit the authors
of the document. It is recommended that you include
the names of all authors in academic writing.

*Example:*
```pipp
config
    of "MLA9"
    
"This is the only sentence"
```

# 3. Unlikeliness

Low Severity

## 31

*Description:*
Two authors have the same id, which seems unlikely.
Check if that is correct. Author 1: (author).
Author 2: (author)

*Cause:*
This warning occurs when creating at least two authors
in the `author` configuration, which have the same ID.
As the usual interpretation of an ID is a unique 
identifier, this seems like a mistake.
However, since there could be cases like group projects,
in which authors could end up having the same IDs, this
warning is not enforced as an error.

*To do:*
Decide whether you really want to have authors
with the same ID.

*Example:*
```pipp
config
    author
        of
            name "John Doe"
            id "1"
        of
            name "Alice Alison"
            id "1"
```