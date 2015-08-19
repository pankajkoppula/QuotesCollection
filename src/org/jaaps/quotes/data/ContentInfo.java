/**
 * 
 */
package org.jaaps.quotes.data;

import java.util.ArrayList;
import java.util.List;

import org.jaaps.quotes.BaseClass;

import android.content.Context;

/**
 * @author Pankaj
 *
 */
public class ContentInfo extends BaseClass{
	
	private Context context;
	private String name;
	private String title;
	private String subTitle;
	private String fileName;
	private int imageId;
	private List<String> quoteList = new ArrayList<String>();
	
	private static String shareInfo = "";
	private static List<ContentInfo> contentInfoList = new ArrayList<ContentInfo>();
	private List<Integer> imageIds = new ArrayList<Integer>();
	
	public ContentInfo(){
	}
	
	public ContentInfo(Context context){
		super(context);
		this.context = context;
		try {
			if(contentInfoList == null){
				contentInfoList = new ArrayList<ContentInfo>();
			}else if(contentInfoList != null && contentInfoList.size() <= 0){
				System.out.println("before adding to list.");
				//Name, Title, subtitle, fileName, imageId
				System.out.println("super.appDataList : " + super.appDataList.size());
				for(String content : super.appDataList){
					ContentInfo contentInfo = new ContentInfo();
					System.out.println("content : " + content);
					String temp[] = content.split("=");
					contentInfo.setName(temp[0]);
					contentInfo.setTitle(temp[1]);
					contentInfo.setSubTitle(temp[2]);
					contentInfo.setFileName(temp[3]);
					String imageName = temp[4];
					int imageId = this.context.getResources().getIdentifier(imageName, "drawable", this.context.getPackageName());
					contentInfo.setImageId(imageId);
					imageIds.add(imageId);
					
					contentInfoList.add(contentInfo);
//					System.out.println("content info size : " + contentInfoList.size());
				}
				
			}
			if(shareInfo != null && shareInfo.equals("")){
				shareInfo = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ContentInfo(String name, String title, String subTitle, String fileName, int imageId){
		this.name = name;
		this.title = title;
		this.subTitle = subTitle;
		this.fileName = fileName;
		this.imageId = imageId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public List<ContentInfo> getContentInfoList() {
		return contentInfoList;
	}

	public List<String> getQuoteList(int index) {
		return this.quoteList = readPropertiesFile(getContentInfoList().get(index).getFileName());
	}

	public void setQuoteList(List<String> quoteList) {
		this.quoteList = quoteList;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public List<Integer> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<Integer> imageIds) {
		this.imageIds = imageIds;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
}
