package databox.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileHelper {
	public static boolean exists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}
	public static String getFileNameSuffix(String fileName) {
		String suffix = "";
		int idx = fileName.lastIndexOf(".");
		if(idx != -1)
			suffix = fileName.substring(idx + 1);
		
		return suffix;
	}
	
	public static String getFileName(String fileName) {
		int idx = fileName.lastIndexOf(".");
		if(idx == -1) {
			return fileName;
		} else {
			return fileName.substring(0, idx);
		}
	}
	
	public static boolean move(String src, String dest) {
		return copy(src, dest) & remove(src);
	}
	
	// bug
	public static boolean remove(String src) {
		File file = new File(src);
		if(file.isFile()) {
			return file.delete();
		} else if(file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for(int i=0; i<subFiles.length; ++i) {
				File p = subFiles[i];
				if(p.isFile()) {
					p.deleteOnExit();
				} else if(p.isDirectory()) {
					return remove(src + File.separator + p.getName());
				}
			}
		}
		return true;
	}
	
	public static boolean copy(String src, String dest) {
		try {
			File cur = new File(src);
			if(cur.isFile()) {
				FileInputStream in = new FileInputStream(cur);
				FileOutputStream out = new FileOutputStream(dest);
				byte[] buffer = new byte[2048];
				int len = in.read(buffer);
				while(len > 0) {
					out.write(buffer, 0, len);
					len = in.read(buffer);
				}
				out.flush();
				out.close();
				in.close();
			} else if(cur.isDirectory()) {
				new File(dest).mkdirs();
				File[] subFile = cur.listFiles();
				for(int i=0; i<subFile.length; i++) {
					File p = subFile[i];
					if(p.isFile()) {
						FileInputStream in = new FileInputStream(p);
						FileOutputStream out = new FileOutputStream(dest + File.separator + p.getName());
						byte[] buffer = new byte[2048];
						int len = in.read(buffer);
						while(len > 0) {
							out.write(buffer, 0, len);
							len = in.read(buffer);
						}
						out.flush();
						out.close();
						in.close();
					} else if(subFile[i].isDirectory()) {
						copy(src + File.separator + p.getName(), dest + File.separator + p.getName());
					}
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
