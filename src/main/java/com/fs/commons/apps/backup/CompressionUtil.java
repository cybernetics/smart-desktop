///**
// * Modification history
// * ====================================================
// * Version    Date         Developer        Purpose 
// * ====================================================
// * 1.1      30/06/2008     Jamil Shreet    -Add the following class : 
// */
//
//package com.fs.commons.apps.backup;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipException;
//import java.util.zip.ZipOutputStream;
//
//import com.fs.commons.util.GeneralUtility;
//
//import de.idyl.crypto.zip.AesZipOutputStream;
//
///**
// * @1.1
// * @author ASUS
// * 
// */
//public class CompressionUtil {
//
//	/**
//	 * @return
//	 * @throws CompressionException
//	 * 
//	 */
//	public static void compress(String fileNameSource, String fileNameDestination) throws CompressionException {
//		FileOutputStream outputStream;
//		try {
//			outputStream = new FileOutputStream(fileNameDestination);
//			FileInputStream inputStream = new FileInputStream(fileNameSource);
//			File file=new File(fileNameSource);			
//			compress(inputStream, outputStream,file.getName());
//		} catch (FileNotFoundException e) {
//			throw new CompressionException("FILE_NOT_FOUND", e);
//		}
//	}
//
//	/**
//	 * 
//	 * @param string 
//	 * @param inputStream
//	 * @param outputStream
//	 * @param fileName
//	 * @throws CompressionException
//	 */
//	public static void compress( FileInputStream in, FileOutputStream out, String entryName)throws CompressionException {
//		byte[] buf = new byte[1024];
//		try {
//			ZipOutputStream zout = new ZipOutputStream(out);
//			zout.putNextEntry(new ZipEntry(entryName));
//			int length;
//			while ((length = in.read(buf)) > 0) {
//				zout.write(buf, 0, length);
//			}
//			zout.closeEntry();
//			in.close();
//			zout.close();
//		} catch (FileNotFoundException e) {
//			throw new CompressionException("FILE_NOT_FOUND", e);
//		} catch (IOException e) {
//			throw new CompressionException("IO_EXCEPTION", e);
//		}
//	}
//	
//	
//	/**
//	 * 
//	 */
//	public static void compressFiles(String[] sourceFiles,String compressedFileName) {
//	    byte[] buf = new byte[1024];	    
//	    try {	      
//	        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(compressedFileName));
//	        for (int i=0; i<sourceFiles.length; i++) {
//	        	String fileName = sourceFiles[i];
//	        	System.err.println("Compressing "+fileName);	            
//				FileInputStream in = new FileInputStream(fileName);
//	            out.putNextEntry(new ZipEntry(fileName));
//	            int len;
//	            while ((len = in.read(buf)) > 0) {
//	                out.write(buf, 0, len);
//	            }
//	            out.closeEntry();
//	            in.close();
//	        }
//	        out.close();
//	        System.err.println("Compression finished succ ");
//	    } catch (IOException e) {
//	    }
//
//	}
//	
//	public static void compressAndSetPassword(String fileName, String databasePassword) throws IOException {
//		compressAndSetPassword(fileName.replace(GeneralUtility.getExtension(fileName,true), "") ,GeneralUtility.getExtension(fileName,false) , databasePassword);
//	}
//	/**
//	 * 
//	 * @param args
//	 * @throws CompressionException
//	 * @throws IOException 
//	 */
//
//	public static void compressAndSetPassword(String fileName,String extension, String databasePassword) throws IOException {
//		try {
//			AesZipOutputStream.zipAndEcrypt(fileName ,extension, databasePassword);
//			new File(fileName).delete();
//		} catch (ZipException e) {
//			if(e.getMessage().contains("(encrypted entry)")){
//				throw new ZipException("FILE_IS_ALREADY_PROTECTED_BY_PASSWORD");
//			}else{
//				throw e;
//			}
//		}
//	}
//	public static void main(String[] args) throws IOException {
//		try {
//			String fileName = "D:\\workspace\\Smart-University\\backups\\2011-12-05.sql";
//			AesZipOutputStream.zipAndEcrypt(fileName.replace(GeneralUtility.getExtension(fileName,true), "") ,GeneralUtility.getExtension(fileName,false), "123");
//			new File(fileName).delete();
//		} catch (ZipException e) {
//			if(e.getMessage().contains("(encrypted entry)")){
//				throw new ZipException("FILE_IS_ALREADY_PROTECTED_BY_PASSWORD");
//			}else{
//				throw e;
//			}
//		}
//	}
//
//}
