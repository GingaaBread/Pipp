# Documents

In Pipp, documents are represented by a single file in the root of the project folder named `document.pipp`.

You can use any code editor of your choice to write Pipp documents: Notepad, VSCode, or even fully-fledged IDEs 
work well. I recommend the use of a code editor that supports syntax highlighting, such as VSCode. 
This makes it easy to differentiate keywords and text blocks. Usually they will also allow you to easily embed version 
control.

## Document Structure

Pipp documents follow a simple document structure:

1. You can optionally define document configurations.
2. Then follow elements you wish to render in the document.

If you want to include configurations, these must be defined at the top of the file.

The elements you define are rendered one after another, from top to bottom. Consider this example:

_document.pipp:_
```
header
title
chapter "Introduction"
``` 

Pipp will first render the document header, then the title and at the end the chapter _Introduction_.

## Whitespace Sensitive

Note that Pipp is mostly whitespace sensitive. This means that you cannot use new-line (ENTER) or tabulation (TAB) 
characters whenever you may want to.
Consider the following **WRONG** example, which will **NOT COMPILE**!

_document.pipp:_
```
header

title
chapter "Introduction"
```

The reason this does not compile is the empty line after the header instruction.

The same is true for text blocks used in configurations like here:

_document.pipp:_
```
config
	style
	"MLA9"
```

The same is also true for tabulation like here:

_document.pipp:_
```
config
	style 	"MLA9"
```

The reason is the tabulation after the `style` instruction.

Note that you _can_ include additional spaces separating configurations from their values or before the end of a line.
However, I do not recommend you do this and instead follow the convention of only using one space for configurations 
and no spaces before the end of a line.

# Tabulation / Indentation

Pipp is an indented language, which means that it heavily makes use of the tabulation character `\t` to organise your 
document.
Elements must be placed in the correct positions in the indentation hierarchy in order to work properly.

This allows you to define `parents` and `children` in your document. For example, a chapter instruction could be the 
parent of several paragraphs.
Another example is the `config` keyword, which is the parent of all configurations.

The amount of tabs you need to use for elements are always one more than their parents. Thus, if the parent is a 
`config` keyword with no tabulation, the `style` child requires one tab before it. That style configuration could have 
a `font` child, which would require two tabs before it. And as a last example, that font configuration could have a 
`name "Times Roman"` child, which would require three tabs before it.

This way of organising your elements into hierarchies is readable and allows you to write Pipp documents fast and 
efficiently.

# Comments

Pipp supports start-of-line comments using the hash `#` character, which allows you to ignore an entire line during 
compilation.

> Note that the hash must be placed at the start of the line and can only ignore exactly one line  