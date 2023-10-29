# Paragraphs

In most types of academic documents, writers often want to divide their texts into several passages.
A common tool used to achieve this is the use of paragraphs. However, different style guides require different formal 
styles for paragraphs. Some prefer the use of indentation, whilst others do not.

> Let us once again remember Pipp's core philosophy: you should let Pipp take care of the boring formalities and focus 
> on the content. Pipp will dynamically apply the proper style depending on the style guide you tell it to use.

## Defining Paragraphs

Paragraphs in Pipp are simply elements chained together until an empty line marks the end of the paragraph.
Note that only paragraph instructions can be used in paragraphs. 
A list of all paragraph instructions can be found below.

_Example Paragraph:_
```pipp
"I'm a text."
"So am I. This is awesome!"
emphasise "I also belong to the same paragraph"
```

Note how these three blocks are not separated by an empty line, thus belonging to the same paragraphs. 
Let us consider an example with three paragraphs:

_document.pipp:_
```
"Famous author John Doe once said that"
citation "A1", "1-2", "Pipp is an awesome tool!"

"A paragraph can also just consist of one element."

"Note that you cannot use more than one empty line, however."
```

The example also shows how you cannot use more than one empty line because Pipp is white-space sensitive.

## Paragraph Instructions

In paragraphs, you can use the following instructions:

### Text

As seen above text blocks can be used in paragraphs to add sentences to a document.

_Example:_
```pipp
"The sheep on the hill was looking at the grass. It contemplated whether to eat grass or whether to go to sleep."
"In the end, it decided to do neither, and fly away, instead.
This surprised the farmer who had been watching the sheep."
```

> Note how the second text block in the example above goes over two lines

### Emphasis

To emphasise a text, the `emphasise` keyword is used. The style guide determines how the text will be emphasised.
In MLA, for example, it will be italicised. When used, a text block has to follow, which marks the text that will be
emphasised. The text is allowed to span multiple lines.

_Example:_
```pipp
"Emphasis can be used for foreign words like the word"
emphasise "Fremdwort"
```

### Work

To refer to a work in a document, the `work` keyword is used.

_Example:_
```pipp
work "Harry Potter and the Philosopher's Stone"
"was a huge success."
```

It is also possible to refer to the id of a work defined in the project's bibliography.
This can be an incredibly powerful way to have all references to that work updated at the same time, when the 
name of the work is updated in the bibliography. It also ensures that all references to that work are spelled the same.
To do this, add the `id` keyword right after the `work` keyword, and supply the id of the work as it is defined in the
bibliography.

_Example:_
```pipp
work id "HP"
"was a huge success."
```

### Citation

To cite a work of your bibliography, it first needs to be added to the `bibliography.pipp` file.
If a work is cited that is not in that file, or the file cannot be found in the first place, the compilation halts with
an error.
To cite an existing source, the `citation` keyword is used. It consists of three parts:
1. `id`: the ID of the source in the bibliography.
2. `numeration`: the page number / source numeration / etc. of the citation.
3. `of`: the content cited from the source

As an example, let us consider this `bibliography.pipp` file:

```pipp
bibliography
    id "HP1"
        type "Book"
        author "J.K. Rowling"
        title "Harry Potter and the Philosopher's Stone"
        publication
            name "Bloomsbury (UK)"
            date "06/26/1997"
            in "United Kingdom"
```

Using this bibliography file, we can currently only cite from one source, which is called `HP1`.
The following example shows how that works:

_Example:_
```pipp
work id "HP1"
"famously introduces Mr. and Mrs. Dursley as"
citation
    id "HP1"
    numeration "1"
    of "the last people you'd expect to be involved in anything strange or mysterious"
```

Note how `id "HP1` references the work defined in the bibliography, which has the id `HP1`.
`numeration "1"` means that the citation is from the first page of the source.
The numeration can also be a span in the format of `X-Y` where X and Y are positive numbers, and Y is greater than X.
For example, `3-25` would mean that the citation ranges from page three until page 25.
Finally, `of "the ..."` defines the cited content from the source.

This syntax is a bit cumbersome. Therefore, we can use the `,` list separator for a nicer syntax.
The same example can be written like this:

```pipp
work id "HP1"
"famously introduces Mr. and Mrs. Dursley as"
citation "HP1", "1", "the last people you'd expect to be involved in anything strange or mysterious"
```

Note that after each `,` can follow one optional new line.

#### Cut

Strictly speaking the example above is not properly academic because it cuts out a part of the citation without 
any indication that it does so. Style guides typically use `[...]` to indicate this, but just like citations in Pipp,
this should not be added into text blocks. Instead, the `cut` keyword is used.

_Example:_
```pipp
work id "HP1"
"famously introduces Mr. and Mrs. Dursley as"
citation "HP1", "1",
    cut
    "the last people you'd expect to be involved in anything strange or mysterious"
    cut
```

#### Sic

The `sic` keyword is used to indicate that a passage is taken from another source. This can be used for controversial
citations, when the original author has made a spelling mistake in the passage, etc.

_Example:_
```pipp
citation "A", "1",
    "Greene"
    sic
    "is my favourite colour!"
```