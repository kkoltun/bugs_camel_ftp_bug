# ftp_hangapp
## This repo contains code for Java application causing hang in Apache Camel.
### Prerequisites:
  - Apache Camel 2.21;
  - Java 8 or newer (didn't test on older ones);
  - streamDownload and stepwise turned on.
### Reproduction:
  1. Start downloading a file with size exceeding InputStream cache (on my pc approx. 1mb is the limit).
  ```java
  InputStream is = this.client.retrieveFileStream(remoteName); \\ FtpOperations:373
  ```
  2. The server responds 150 and opens data connection;
  ```
  [user_ftp] FTP response: Client "127.0.0.1", "150 Opening BINARY mode data connection for x (1048576 bytes)."
  ```
  3. The data connection does not end because InputStream is waiting for reads and it has not cached whole file. No "226 Transfer complete" response from server.
  4. We try to change directory as stepwise is turned on.
  ```java
  this.changeCurrentDirectory(currentDir); \\ FtpOperations:387
  ``` 
  5. Camel hangs as the server is still in the data connection and we are waiting for response from CWD command.  
