# Paragraphs

In most types of academic documents, you probably want to divide your text into several passages.
A common tool used to achieve this is the use of paragraphs. However, different style guides require different formal 
styles for paragraphs.
Some prefer the use of indentation, whilst others do not.

Let us once again remember Pipp's core philosophy: you should let Pipp take care of the boring formalities and focus 
on the content. Pipp will dynamically apply the proper style depending on the style guide you tell it to use.

## Defining Paragraphs

Paragraphs in Pipp are simply elements chained together until an empty line marks the end of the paragraph.
Note that it does not matter what types of elements are used (text blocks, work references, citations, etc.).

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