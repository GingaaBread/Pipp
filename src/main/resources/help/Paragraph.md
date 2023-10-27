# Paragraphs

In most types of academic documents, you probably want to divide your text into several passages.
A common tool used to achieve this is the use of paragraphs. However, different style guides require different formal 
styles for paragraphs.
Some prefer the use of indentation, whilst others do not.

Let us once again remember Pipp's core philosophy: you should let Pipp take care of the boring formalities and focus 
on the content. Pipp will dynamically apply the proper style depending on the style guide you tell it to use.

## Defining Paragraphs

Paragraphs in Pipp are simply elements chained together until an empty line marks the end of the paragraph.
Note only paragraph instructions can be used in paragraphs. A list of all paragraph instructions can be found below.

_Example Paragraph:_
```
"I'm a text"
"So am I. This is awesome!"
"I also belong to the same paragraph"
```

Note how these three text blocks are not separated by an empty line, thus belonging to the same paragraphs. 
Of course, here a multiline string could be used as well, this is just for demonstrating purposes. 
But let us consider an example with three paragraphs:

_document.pipp:_
```
"Famous author John Doe once said that"
citation "EX", "1-2", "Pipp is an awesome tool!"

"A paragraph can also just consist of one element"

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

### Citation