package org.jaaps.quotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.jaaps.quotes.utils.AssetsPropertyReader;

import android.content.Context;
import android.content.res.AssetManager;
/**
 * 
 * @author Pankaj
 *
 */
public abstract class BaseClass{

	protected static AssetsPropertyReader assetsPropertyReader;
	protected Context context;
	protected static Properties p = new Properties();
	protected List<String> appDataList = new ArrayList<String>();
	
	public BaseClass() {}
	
	public BaseClass(Context context) {
		this.context = context;
		assetsPropertyReader = new AssetsPropertyReader(context);
//		p = assetsPropertyReader.getProperties("data.properties");
//		System.out.println("this.context in base class : " + this.context.toString());
		this.appDataList = readAppData(this.context);
	}
	
	protected List<String> readPropertiesFile(String fileName){
		List<String> quoteList = new ArrayList<String>();
		Properties prop = new Properties();
		prop = assetsPropertyReader.getProperties(fileName);
		Enumeration<Object> enuKeys = (Enumeration<Object>) prop.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = prop.getProperty(key);
			//String value = prop.getProperty(enuKeys.nextElement().toString());
	      quoteList.add(value);
	     }
		return quoteList;
	}
	
	protected List<String> readAppData(Context context){
		List<String> appDataList = new ArrayList<String>();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try{
			AssetManager am = context.getAssets();
			inputStream = am.open("appdata.txt");
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
//	            System.out.println("line : " + line);
	            appDataList.add(line);
	         } 
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(bufferedReader != null)
					bufferedReader.close();
				if(inputStreamReader != null)
					inputStreamReader.close();
				if(inputStream != null)
					inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return appDataList;
	}
}
