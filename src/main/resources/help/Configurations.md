# Configurations

In Pipp, you have full customisation about how you would like to configure your document. 
There are two things you might want to configure:

1. project specific configurations (this can include the title of your document, the names of the authors, etc.)
2. style guide specific configurations

## 1. Project Configurations

The root of all project configurations is the `config` keyword, which is the parent of all configurations in Pipp. 
Note that if declared, you **MUST** define at least one configuration.

### Document Author(s)

The first configuration you might want to include is the author configuration. It allows you to specify the names of 
the authors of the document. This is rendered when you use the `header` or `titlepage`instructions, and is
automatically included in the metadata of the created document.

It uses the `author` keyword, which **MUST** be a direct child of the `config` keyword. 
It accepts the following options:

- Text Block: used for a single author. Gino will use spaces to parse the name. The section before the last space will be the first name of the author, and the rest will be the last name. This configuration can be useful if the name of the author just consists of a single last name and if there is no title (Prof., Dr., etc.). An example would be `author "John Doe"`
- `name`: this has the same use as a text block, but it allows the additional use of the `title` configuration listed below
- `firstname`: used to specify the first name of a single author. If used the `name` configuration cannot be used
- `lastname`: used to specify the last name of a single author. If used the `name` configuration cannot be used.
- `title`: used to specify the title of a single author. This can be anything like Prof., Dr., etc.
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
 		name "John Doe"
 		title "Dr."
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

Analogously, you can define the assessors of the document. They accept the exact same options as authors.
Assessors are individuals that assess the document in order to rate it, edit it, proofread it, etc.

## Document Title

The title of the document can be defined in the `title` configuration. It is a direct child of the `config` keyword.
It only accepts a text block, which is the title of the document.

_Example:_
```
config
	title "My First Document"
```

## Document Publication

The `publication` keyword is a subgroup that configures information about the publication of the document. 
It **MUST** be a child of the `config` keyword.

### Publication Context Title

The `title` keyword can be used to define the title of the publication. This can be an actual publication, a class ID, 
etc. The `date` keyword can be used to define the date of publication. If left out, the compiler will use the date of 
compilation. If no date should be supplied, use the `date "None"` option. A custom date must be specified as an option 
using the British Date Format `dd/MM/YYYY`.

_Examples:_
```
config
	publication
		title "Statistics 24"
		date "19/03/1995"
```
```
config
	publication
		title "Pipp Publications"
		date "None"
```

## Document Type

The document type is currently a reserved configuration with no effect. 
It uses the `type` keyword as a direct child of the `config` keyword and currently only accepts the `type "Paper"` 
option.

## Style Guide

To define the style guide of your choice, use the `style` configuration. It is a direct child of the `config` keyword 
and currently only accepts the `style "MLA9"` option.