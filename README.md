
# Dynamic PDF Generator

Allows the user to generate PDFs dynamically based on the provided input and donwload the generated file.

# Installation

Clone the repository and you are good to go! This uses the [H2 database](https://www.h2database.com/html/main.html) which is embedded within the application and will not require any additional setup.I have used the file based persistance mode for this which stores the data within a directory in the application. 

# API endpoints

```
http://localhost:8080/generator - Allows the generation and download of PDF

http://localhost:8080/download?dataId={dataID} - A utility endpoint that allows download of an existing file if its dataId is known

```

