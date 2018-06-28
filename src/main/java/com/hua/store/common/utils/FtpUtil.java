package com.hua.store.common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

public class FtpUtil {
    private static final String HOST_NAME = "10.0.0.100";
    private static final String USER_NAME = "ftpadmin";
    private static final String PASSWORD = "ftpadmin";
    private static final String WORKING_DIR = "/home/ftpadmin/www/images";
    private static final int PORT = 21;

    public static void upload(String fileName, InputStream inputStream) throws IOException {

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect(HOST_NAME, PORT);
        ftpClient.login(USER_NAME, PASSWORD);
        System.out.println("Status: " + ftpClient.getStatus());
        ftpClient.changeWorkingDirectory(WORKING_DIR);
        System.out.println(ftpClient.getReplyString());
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        ftpClient.storeFile(fileName, inputStream);

        // Set permission, otherwise can't not access from url
        ftpClient.sendSiteCommand("chmod 744 " + fileName);
        System.out.println(ftpClient.getReplyString());
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public static boolean upload(String host, int port, String username, String password, String basePath,
                                     String filePath, String fileName, InputStream input) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);

            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }

            //ftp.changeWorkingDirectory(WORKING_DIR);

            if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)) continue;
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                            ftp.sendSiteCommand("chmod 755 " + tempPath);
                        }
                    }
                }
            }


            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            if (!ftp.storeFile(fileName, input)) {
                return result;
            }
            ftp.sendSiteCommand("chmod 744 " + fileName);
            input.close();
            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static boolean download(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            ftp.login(username, password);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());

                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}
