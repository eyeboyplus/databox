package databox.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MyZipFile {

    private ZipFile zipFile = null;

    public MyZipFile(String fileName) throws IOException {
        zipFile = new ZipFile(fileName);
    }


    public void closeAll() {
        try {
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param zipFilePath zip文件路径
     * @param fileName    zip包中需要以数据流的形式解析的文件名，形如tasklist.xml, org/test/Test.class
     * @return
     */
    // TODO 完成ZIP文件流功能
    public InputStream getFileAsInputStream(String fileName) {
        InputStream in = null;
        ZipInputStream zin = null;
        String zipFileName = this.zipFile.getName();
        try {
            in = new BufferedInputStream(new FileInputStream(zipFileName));
            zin = new ZipInputStream(in);
            ZipEntry ze;
            while((ze = zin.getNextEntry())!= null) {
                if(ze.isDirectory()) {
                    System.out.println("dir " + ze.getName());
                } else {
                    if(ze.getName().equals(fileName)) {
                        // System.out.println("file " + ze.getName());
                        return this.zipFile.getInputStream(ze);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                zin.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void unZip(String fileName, String descDir) throws IOException {
        ZipFile zip = new ZipFile(new File(fileName));//��������ļ�������
        String name = /*zip.getName().substring(zip.getName().lastIndexOf('\\')+1, zip.getName().lastIndexOf('.'))*/"";

        File pathFile = new File(descDir/*+name*/);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }

        for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir + "/" + /*name +"/"+*/zipEntryName).replaceAll("\\*", "/");
            //String outPath = (descDir).replaceAll("\\*", "/");

            // �ж�·���Ƿ����,�������򴴽��ļ�·��
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if (!file.exists()) {
                file.mkdirs();
            }
            // �ж��ļ�ȫ·���Ƿ�Ϊ�ļ���,����������Ѿ��ϴ�,����Ҫ��ѹ
            if (new File(outPath).isDirectory()) {
                continue;
            }
            // ����ļ�·����Ϣ
//			System.out.println(outPath);

            FileOutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
