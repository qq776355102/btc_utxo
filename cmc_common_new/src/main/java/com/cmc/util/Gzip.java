package com.cmc.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Gzip {
	public static byte[] gzip(byte[] data)
	{
		ByteArrayInputStream bins = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        try {    
	            //打开需压缩文件作为文件输入流    
	            InputStream fin=bins;    
	            //建立压缩文件输出流    
	            OutputStream fout=bos;    
	            //建立gzip压缩输出流    
	            GZIPOutputStream gzout=new GZIPOutputStream(fout);    
	            byte[] buf=new byte[1024];//设定读入缓冲区尺寸    
	            int num;    
	    
	            while ((num=fin.read(buf)) != -1) {    
	                gzout.write(buf,0,num);    
	            }    
	            gzout.close();  
	            fout.close();    
	            fin.close();  
	            
	        }catch(IOException e) {    
	             throw new RuntimeException(e);
	        }   
	        return bos.toByteArray();
	}
	
	public static byte[] ungzip(byte[] data)
	{
		ByteArrayInputStream bins = new ByteArrayInputStream(data);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
	        try {    
	            //建立gzip压缩文件输入流    
	            InputStream fin=bins;    
	            //建立gzip解压工作流    
	            GZIPInputStream gzin=new GZIPInputStream(fin);    
	            //建立解压文件输出流    
	            OutputStream fout=bos;    
	            byte[] buf=new byte[1024];    
	            int num;    
	                
	            while ((num=gzin.read(buf,0,buf.length)) != -1) {    
	                fout.write(buf,0,num);    
	            }    
	            gzin.close();    
	            fout.close();    
	            fin.close();    
	        }catch(IOException e) {    
	        	throw new RuntimeException(e);
	        }
	        return bos.toByteArray();
	}
	
	
}
