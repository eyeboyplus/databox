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
