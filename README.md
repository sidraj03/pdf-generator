
# Dynamic PDF Generator

Allows the user to generate PDFs dynamically based on the provided input and download the generated file.

# Installation

Clone the repository and you are good to go! This uses the [H2 database](https://www.h2database.com/html/main.html) which is embedded within the application and will not require any additional setup. I have used the file based persistence mode for this which stores the data within a directory in the application.  
The directory to store the data can be configured in the application properties by modifying the below property.

```
spring.datasource.url=jdbc:h2:file:../../testdb
```

# API endpoints

```
http://localhost:8080/generator - Allows the generation and download of PDF

http://localhost:8080/download?dataId={dataID} - A utility endpoint that allows download of an existing file if its dataId is known

```

