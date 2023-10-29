# Text Blocks

When working with Pipp, text blocks are the fundamental building blocks of Pipp documents.
They are enclosed by quotation marks `"` and are used for two distinct purposes:
1. they configure an element or a project configuration
2. they represent textual content rendered in your document.

## 1. Configurations

When using text blocks for configurations, make sure to only supply an option that is available for the configuration.
No worries, you do not have to learn all options by heart, this documentation has an amazing overview of all 
configurations and their options that you can review at any time!

Consider this example, which tells Pipp to use the MLA style in its ninth edition:

```
config
	style "MLA9"
```

Also note how the compilation will halt with an error if the text `MLA9` was instead any other text not included in the 
list of options for the `style` configuration. For example, the configuration `style "aaaa"` will not compile.

Similarly, text blocks can be used to configure other blocks, without being limited to a set of options. 
A very straight-forward example for this are chapters: you can freely name chapters whatever you like. 
Consider this Pipp document, which defines a chapter using a text block:

```
chapter "Introduction"
```

## 2. Textual Content

Consider this example, which uses a text block to render your first sentence using Pipp:

_document.pipp:_
```
"Hello World!"
```

This will render the sentence `Hello World!` to your document.
Note that the quotation marks `"` are not rendered! They merely tell Pipp that it should render the textual content to
the document.

If you ever find yourself in the need to render quotation marks in your document, preface them with the backslash `\` 
character. This is called _escaping_ the quotation marks, and lets Pipp know that the quotation mark is intended to be 
rendered, instead of beginning or ending a text block.

Texts can consist of multiple lines. This makes it very convenient to write readable Pipp documents because the code 
editor of your choice may not automatically wrap lines. I still recommend that if the code editor you are using does 
have an auto-wrap feature, you make use of it. In my opinion, it increases writing speed and removes the hassle of 
trying to "find the right passage to wrap a line". In many code editors, this will also scale to your screen size, 
which can be immensely useful.

To wrap a line manually, simply press enter. If this is done, the new line character is not rendered. 
Consider the following example:

_document.pipp:_
```
"This text 
covers three 
lines!"
```

_Renders:_
```
This text covers multiple lines!
```

> In Pipp, you generally do not want to render new line characters yourself. The guiding principle of Pipp is that you 
> should focus on the content and let Pipp take care of the formalities and generate the document for you.

# Whitespace & Punctuation

Pipp removes all other white space (characters internally used when using Space, Tab or Enter, for example) in texts 
according to the used style guide. If you do not end your text with either a full-stop `.`, exclamation mark `!` or 
question mark `?`, Pipp will automatically insert a full-stop:

_document.pipp:_
```
"This is a text"
```

_Renders:_
```
This is a text.
```

These punctuation characters also indicate the end of a sentence within a text, which Pipp will process internally 
according to the used style guide. The MLA style guide, for example, inserts exactly one space after a sentence. 
Consider the following example with multiple sentences in a text:

> You can escape punctuation characters the same way you can escape the quotation marks, simply by prefacing them with 
> the backslash `\\` character.

_document.pipp:_
```
"This is a text. It also features a very thought-provoking question, doesn’t it? Finally, the last sentence omits the full-stop, knowing that Pipp is smart enough to include it on its own"
```

_Renders:_
```
This is a text. It also features a very thought-provoking question, doesn’t it? Finally, the last sentence omits the full-stop, knowing that Pipp is smart enough to include it on its own.
```

The second main philosophy of Pipp is that the input automatically adjusts every single sentence to your style guide.

Imagine if there was a (rather odd) custom style guide, demanding a new line and hyphen after each sentence, instead of 
a space. The same input would render the following result:

_document.pipp:_
```
"This is a text. It also features a very thought-provoking question, doesn’t it? Finally, the last sentence omits the full-stop, knowing that Pipp is smart enough to include it on its own"
```

_Renders:_
```
This is a text.
-It also features a very thought-provoking question, doesn’t it?
-Finally, the last sentence omits the full-stop, knowing that Pipp is smart enough to include it on its own.
```

# Markdown & Rich Text

Texts in Pipp do not support markdown or rich text (bold, italic, strikethrough, etc.).
This is because the style guide defines how certain passages should be emphasised. In other words, you should let Pipp 
take care of how it should render emphasised text, and let it know you want a passage to be emphasised using the 
`emphasise` keyword.