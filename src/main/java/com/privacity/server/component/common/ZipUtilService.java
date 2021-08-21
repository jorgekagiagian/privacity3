package com.privacity.server.component.common;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.springframework.stereotype.Service;

import com.privacity.common.enumeration.ExceptionReturnCode;
import com.privacity.server.exceptions.ProcessException;


@Service
public class ZipUtilService {

	public byte[] compress(String data) throws ProcessException{
		byte[] compressed;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(data.getBytes());
			gzip.close();
			compressed = bos.toByteArray();
		
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ProcessException( ExceptionReturnCode.ZIP_COMPRESS);
		}
		return compressed;
	}
	
	public String decompress(byte[] compressed) throws ProcessException {
		StringBuilder sb;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
			GZIPInputStream gis = new GZIPInputStream(bis);
			BufferedReader br = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
			sb = new StringBuilder();
			String line;
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			gis.close();
			bis.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ProcessException( ExceptionReturnCode.ZIP_DESCOMPRESS);
		}
		return sb.toString();
	}
}