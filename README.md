# ftp_hangapp
## This repo contains code for Java application causing hang in Apache Camel.
### Prerequisites:
  - Apache Camel 2.21;
  - Java 8 or newer (didn't test on older ones);
  - streamDownload and stepwise turned on.
### Reproduction:
  1. Start downloading a file with size exceeding InputStream cache (on my pc approx. 1mb is the limit). [FtpOperations.java:373](https://github.com/apache/camel/blob/dc6caa696255240a2a27c3bf229fc3aac9014401/components/camel-ftp/src/main/java/org/apache/camel/component/file/remote/FtpOperations.java#L423)
  ```java
  InputStream is = this.client.retrieveFileStream(remoteName);
  ```
  2. The server responds 150 and opens data connection.
  ```
  [user_ftp] FTP response: Client "127.0.0.1", "150 Opening BINARY mode data connection for x (1048576 bytes)."
  ```
  3. The data connection does not end because InputStream is waiting for reads and it has not cached whole file. No "226 Transfer complete" response from server.
  4. Try to change directory as stepwise is turned on. [FtpOperations.java:387](https://github.com/apache/camel/blob/dc6caa696255240a2a27c3bf229fc3aac9014401/components/camel-ftp/src/main/java/org/apache/camel/component/file/remote/FtpOperations.java#L443)
  ```java
  this.changeCurrentDirectory(currentDir);
  ``` 
  5. Camel hangs as the server is still in the data connection and we are waiting for response from CWD command.  
