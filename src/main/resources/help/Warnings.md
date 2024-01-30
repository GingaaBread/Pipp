Warnings
=========

# 1. Inconsistencies

## 11

*Severity:*
High

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

*Severity:*
High

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

*Severity:*
Low

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

## 21

*Severity:*
Critical

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
```

## 22

*Severity:*
High

*Description:*
The bibliography entry type '[TYPE]' is not supported.

*Cause:*
This warning occurs when defining an unknown type for a bibliography entry. A cause could be a simple typo, or
referring to a type, which is not implemented or supported. Unsupported bibliography entry types may be rendered
incorrectly and should be avoided.

*To do:*
Decide if you really want to keep the unsupported bibliography entry type, or if there is a typo in the specified type.

*Example:*

```pipp
bibliography
	id "1"
#       Should be type "Book"
		type "Novel"
```

## 23

*Severity:*
Critical

*Description:*
The bibliography entry with the ID '[ID]' does not have a type.

*Cause:*
This warning occurs when there is no specified bibliography entry type for a certain bibliography entry.
This is NOT recommended as it may be rendered incorrectly, or cannot receive certain configurations.

*To do:*
Define a type for each bibliography entry.

*Example:*

```pipp
bibliography
	id "1"
#       Should be type "Book"
		title "XYZ"
		author "John Doe"
```

# 3. Unlikeliness

## 31

*Severity:*
Critical

*Description:*
Two authors have the same id, which seems unlikely. Check if that is correct. Author 1: (author). Author 2: (author)

*Cause:*
This warning occurs when creating at least two authors
in the `author` configuration, which have the same ID.
As the usual interpretation of an ID is a unique
identifier, this seems like a mistake.
However, since there could be cases like group projects,
in which authors could end up having the same IDs, this
warning is not enforced as an error.

*To do:*
Decide if you really want to have authors with the same ID.

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

## 32

*Severity:*
High

*Description:*
You are using a font size bigger than 96.
Do you really want to use such a large font?

*Cause:*
This warning occurs when using a font, which
has a size of more than 96 points. It seems unlikely
that such a large font is used voluntarily.

*To do:*
Decide whether you really want to have a font
with a size of more than 96 points.

*Example:*

```pipp
config
    font
        size "222992"
```

## 33

*Severity:*
Critical

*Description:*
Two authors have the same name, which seems unlikely.
Check if that is correct. Author 1: (author).
Author 2: (author)

*Cause:*
This warning occurs when creating at least two authors
in the `author` configuration, which have the same name.
Hereby it does not matter how the name is specified,
whether both authors are configured using the `name` configuration,
or only one and another uses the `firstname` and `lastname` configurations, etc.
As long as the names are the same, this warning is shown.
It seems highly unlikely that multiple authors share the same name,
and can be a common issue when copy and pasting an author and forgetting to change
the name. However, since multiple authors can share names in rare cases, this
is not an enforced error.

*To do:*
Decide whether you really want to have authors
with the same name.

*Example:*

```pipp
config
    author
        of
            name "John Doe"
        of "John Doe"
```

## 34

*Severity:*
Critical

*Description:*
Two assessors have the same name, which seems unlikely.
Check if that is correct. Assessor 1: (assessor).
Assessor 2: (assessor)

*Cause:*
This warning occurs when creating at least two assessors
in the `assessor` configuration, which have the same name.
Hereby it does not matter how the name is specified,
whether both assessor are configured using the `name` configuration,
or only one and another uses the `firstname` and `lastname` configurations, etc.
As long as the names are the same, this warning is shown.
It seems highly unlikely that multiple assessors share the same name,
and can be a common issue when copy and pasting an assessor and forgetting to change
the name. However, since multiple assessors can share names in rare cases, this
is not an enforced error.

*To do:*
Decide whether you really want to have assessor
with the same name.

*Example:*

```pipp
config
    assessor
        of
            name "John Doe"
        of "John Doe"
```

## 35

*Severity:*
High

*Description:*
Odd layout dimensions specified.

*Cause:*
This warning occurs when using odd heights, widths, margins, spacing, etc.
For example, a height of only two inches is rather odd, and might be a user typo, hence this warning.
Note that using odd layout dimensions is NOT recommended and should be avoided as it may result in errors during
the rendering state and may throw recursion errors because a text cannot be rendered on a line anymore.

*To do:*
Decide if you really want to have odd dimensions or not.

*Example:*

```pipp
config
    style
        of "MLA9"
        layout
            height "1in"
```

# 4. Self-Checks

## 41

*Severity:*
Low

*Description:*
You are using a style guide that recommends only using emphasis if absolutely necessary. Make sure that is the case.

*Cause:*
This warning occurs when using a style guide like MLA that allows using emphasis, but only if absolutely necessary.
It is your job to assess if the content you wish to emphasise MUST be emphasised or not.

*To do:*
Decide whether you really need to emphasise the content or not.

*Example:*

```pipp
config
    style "MLA9"
emphasise "Example"
```

### 42

*Severity:*
Low

*Description:*
The semester format used appears to deviate from the standard "WS XXXX" or "SS XXXX" where XXXX is the year.
While this may not be an issue in your specific context or country, please self-check the format to ensure it aligns
with your intended representation.

*Cause:*
This warning occurs when using the publication semester configuration, but deviating from the aforementioned standard.
Note that this may not be an issue for you, at all, so it is your decision whether to change this or not.

*To do:*
Decide whether you want to use the "WS/SS XXXX" format or not.

*Example:*

```pipp
config
    publication
        semester "Summer Semester 1993"
```

### 43

*Severity:*
Critical

*Description:*
The bibliography entry with the ID '[ID]' does not have a publication name or year.

*Cause:*
This warning occurs when defining an item in the bibliography, but not using the `publication` configuration to
define a publication name, or a publication year. This may be acceptable in very rare cases, where there is no known
publisher or even no known publication date, so it is up to you to check if this is correct or not.

*To do:*
Decide whether you want to include a publication name / year, or not.

*Example:*

```pipp
bibliography
	id "1"
		type "Book"
		title "XYZ"
		author "John Doe"
```