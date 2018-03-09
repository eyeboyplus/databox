package databox.util;

import org.junit.Test;

import java.io.*;

public class ZipFileHelperTest {

    @Test
    public void testGetInputStream() throws IOException {
        MyZipFile myZipFile = new MyZipFile("C:/Users/eyeboy/Desktop/test1.jar");
        InputStream in = myZipFile.getFileAsInputStream("tasklist.xml");
        System.out.println(new BufferedReader(new InputStreamReader(in)).readLine());
        myZipFile.closeAll();
    }
}
