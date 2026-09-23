package com.huabo.system.entity;

import java.sql.Timestamp;
import java.util.Date;

public class FileEntity2 {
  private String type;
  private String size;
  private String path;
  private String titleOrig;
  private String titleAlter;
  private String videoFirstPicurl;
  private Date uploadTime;
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getSize() {
    return size;
  }
  public void setSize(String size) {
    this.size = size;
  }
  public String getPath() {
    return path;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public String getTitleOrig() {
    return titleOrig;
  }
  public void setTitleOrig(String titleOrig) {
    this.titleOrig = titleOrig;
  }
  public String getTitleAlter() {
    return titleAlter;
  }
  public void setTitleAlter(String titleAlter) {
    this.titleAlter = titleAlter;
  }
  public Date getUploadTime() {
    return uploadTime;
  }
  public void setUploadTime(Timestamp uploadTime) {
    this.uploadTime = uploadTime;
  }
	public String getVideoFirstPicurl() {
		return videoFirstPicurl;
	}
	public void setVideoFirstPicurl(String videoFirstPicurl) {
		this.videoFirstPicurl = videoFirstPicurl;
	}
  
}
