# ftp_hangapp
Code for description of supposed Camel bug.

This repo contains code for Java application causing hang in Apache Camel.
When in happens:
  streamDownload and stepwise must be turned on;
  we try to download a file with size exceeding InputStream cache (on my pc approx. 1mb is the limit) FtpOperations:373
  the server is responding 150 and opening data connection;
  the data connection does not end because InputStream is waiting for reads and it has not cached whole file (no 2xx response from server);
  we try to change directory FtpOperations:387;
  Camel hangs as the server is still in the data connection and we are waiting for response from CWD command.  
