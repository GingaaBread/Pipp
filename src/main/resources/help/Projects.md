# Pipp Project Structure

A Pipp project consists of a Pipp document and a special project structure. 
The Pipp document is the file that contains the keywords and text blocks representing your document. 
It is located in the root folder of your project and must be named `document.pipp`.

Your bibliography is represented by defining a `bibliography.pipp` file in the root of your project.
During compilation, if Pipp cannot locate the bibliography file, but there is a citation referencing a work of the
bibliography, the compilation will fail with an error.

An `appendix/` folder contains all files you want to include in your appendix. Supported files are images and PDFs.
During compilation, if Pipp cannot locate the appendix folder, but the `appendix` structure is used, 
the compilation will fail with an error. Similarly, if the appendix folder can be found, but a file is referenced,
which Pipp cannot find or does not support, the compilation will fail as well.

An `img` folder contains all images you want to include in your document. Supported files are jpg, jpeg, tif, tiff, 
gif, bmp and png image files. During compilation, if Pipp cannot locate the img folder, but an image is referenced,
the compilation will fail with an error. Similarly, if the img folder can be found, but an image is referenced,
which Pipp cannot find or does not support, the compilation will fail as well.

# Version Control

I recommend the additional use of a version control system like `Git` in order to track changes, create backups and 
analyse work progress. However, this is completely optional, of course.

# Project Hierarchy Visualisation

A sample project could look like this:

_Folder:_ `My First Pipp Document`
```
appendix/
   dog.png
   shelter.pdf
img/
    cat.png
    dolphin.jpg
bibliography.pipp
document.pipp
```