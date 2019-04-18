### Start application
1. run the jar from the project root directory
2. open the following url http://localhost:9000/files/hello.txt in your favourite browser

Or open the project in you favourite IDE and run the main class. 

### System properties used to configure application

- files.root_dir - the root directory used by server to create/get/update/delete files. Default value is `files`

### Not supported:
   - requests with header: <b>TRANSFER_ENCODING</b>
   - connection keep alive header 
   - read request body with content type <b>application/x-www-form-urlencoded</b>
   
### Next steps

- limit the number of files in a directory
- improve validation according with http protocol
- add support for multiple charsets 
- implement the full RFC of HTTP

### Http Endpoints:

- `GET /files/path/to/file.txt` used to retrieve files
    ex: 
    1. http://localhost:9000/files/pictures/kibana.png 
    2. http://localhost:9000/files/AEM.pdf
    3. http://localhost:9000/files/hello.txt
     
- `POST /files` - request with body of type multipart
- `PUT /files/path/to/updated/file.txt` used to update an existing file
- `DELET /files/path/to/file.txt` used to delete a file
- `GET /hello` hello world
