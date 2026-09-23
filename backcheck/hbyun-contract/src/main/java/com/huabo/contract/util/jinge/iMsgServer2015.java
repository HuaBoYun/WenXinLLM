package com.huabo.contract.util.jinge;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.fileupload.DefaultFileItemFactory;
//import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import net.sf.json.JSONObject;

public class iMsgServer2015 {
	private Hashtable<String, String> saveFormParam = new Hashtable<String, String>();  //淇濆瓨form琛ㄥ崟鏁版嵁
    private Hashtable<String, String> sendFormParam = new Hashtable<String, String>();  //淇濆瓨form琛ㄥ崟鏁版嵁
	private List list = new ArrayList<String>();   //淇濆瓨涔︾鍊?
	private InputStream fileContentStream;
	private String fileName = "";
	public byte[] mFileBody = null;
	private String sendType ="";
	private int FFileSize = 0;
	
	private static final String MsgError = "404"; //璁剧疆甯搁噺404锛岃鏄庢病鏈夋壘鍒板搴旂殑鏂囨。
	
	private String ReturnValue;
	


	public String getReturnValue() {
		return ReturnValue;
	}

	public void setReturnValue(String returnValue) {
		ReturnValue = returnValue;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	/**
	 * @throws FileUploadException 
	 * @throws IOException 
	 * @deprecated:鍚庡彴绫昏В鏋愭帴鍙?
	 * @time:2015-01-09
	 */
	public void Load(HttpServletRequest request) throws Exception{
		 request.setCharacterEncoding("gb2312");
		 DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
		 ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
		 //DefaultFileItemFactory diskFileItemFactory = new DefaultFileItemFactory();
		//DiskFileUpload fileUpload = new DiskFileUpload(diskFileItemFactory);
		List fileList =  fileUpload.parseRequest(request);
		 //List<FileItem> fileList =  fileUpload.parseRequest(request);
		 //Iterator iter = fileList.iterator();
		System.out.println("iMsgServer2015.Load...");
		 if (fileList != null && fileList.size() > 0) {
			    for (int i=0; i<fileList.size(); i++) {
			        FileItem item = (FileItem)fileList.get(i);
			        if(item.isFormField()) {
			        	 processFormField(item);
			        }else{
			        	processUploadedFile(item);
			        }
			    }
		 }
	}
   
	/**
	 * @deprecated锛氳В鏋愯〃杈炬暟鎹?	
	 * @param item:琛ㄥ崟鏁版嵁
	 * @throws UnsupportedEncodingException 
	 * @time:2015-01-09
	 */
	public void processFormField(FileItem item) throws UnsupportedEncodingException{
		String fieldName = item.getFieldName();
		String fieldValue = "";
		fieldValue = item.getString("utf-8");
		if(this.sendType.equalsIgnoreCase("JSON")){
			JSONObject json = JSONObject.fromObject(fieldValue);
			Iterator iter = json.keySet().iterator();
			 while (iter.hasNext()) {   
			   fieldName = (String) iter.next();
			   fieldValue = json.getString(fieldName);
			   saveFormParam.put(fieldName, fieldValue);
			}
			 return;
		}
		saveFormParam.put(fieldName, fieldValue);
	}
	
	
	/**
	 * @deprecated锛氳В鏋愭枃妗ｆ暟鎹?
	 * @param item:鏂囨。鏁版嵁
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @time:2015-01-09 
	 */	
	public void processUploadedFile(FileItem item) throws IOException{
		fileName = item.getName();
		if(fileName.indexOf("/")>=0){
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);	
		}else if(fileName.indexOf("\\")>=0){
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
		}
	    fileContentStream =  item.getInputStream();
	
	}
	/**
	 * @deprecated锛氳В鏋愭枃妗ｆ暟鎹?
	 * @param fieldName:鍙傛暟鍚嶇О
	 * @return锛氬弬鏁板浜庣殑鍊?
	 * @time:2015-01-09
	 */	

	public String GetMsgByName(String fieldName){
		return saveFormParam.get(fieldName);
	}
	
	public String  SetMsgByName(String fieldName,String nameValue){
		return saveFormParam.put(fieldName, nameValue);
	}
	/**
	 * 娓呴櫎鎵?鏈塖etMsgByName鎵?鏈夊唴瀹?
	 * @time:2015-01-09
	 */
	public void MsgTextClear(){
		saveFormParam.clear();
	}

	/**
	 * 娓呴櫎鎵?鏈塋ist鎵?鏈夊唴瀹?
	 * @time:2017-09-01
	 */
	public void ListClear(){
		list.clear();
	}
	
	public int MsgFileSize()
	{
	    return this.FFileSize;
	}


	
	 //鍒犻櫎鏂囦欢
    public boolean DelFile(String FileName) {
        File mFile = new File(FileName);
        if (mFile.exists()) {
                mFile.delete();
        }else{
        	SetMsgByName("DelFileState", "澶辫触");
        	return false;
        }
        SetMsgByName("DelFileState", "鎴愬姛");
        return true;
    }

    //saveFormParam鏁伴噺
	public int GetFieldCount(){
 		return saveFormParam.size();
	}
	
	//鍒涘缓鏂囦欢澶?
	public boolean MakeDirectory(String FilePath) {
        File mFile = new File(FilePath);
        mFile.mkdirs();
        return (mFile.isDirectory());
    }


	//鏁版嵁瀛樻斁鍒發ist
	public String GetFieldName(int mIndex){
		int tag = list.size();
		if(tag == 0){
			saveFormParam.remove("OPTION");
			saveFormParam.remove("TEMPLATE");
			Iterator<String> iterator = saveFormParam.keySet().iterator();
			while(iterator.hasNext()){
				String Key = iterator.next();
				list.add(Key);
			}
		}
		String BookMarkName =list.get(mIndex).toString();
		System.out.println(BookMarkName);
			return BookMarkName;
	}

	
	public byte[] MsgFileBody() throws IOException{
		 mFileBody = null;
		 ByteArrayOutputStream output = new ByteArrayOutputStream();
		 byte[] buffer = new byte[4096];
		 int n = 0;
		 while (-1 != (n = fileContentStream.read(buffer))) {
		        output.write(buffer, 0, n);
		 }
	    mFileBody = output.toByteArray();
	    fileContentStream.close();
		return mFileBody;
	}
	
	public void MsgFileBody(byte[] body) {
	    if (body != null) {
	      this.FFileSize = body.length;
	      this.mFileBody = body;
	    } else {
	      this.mFileBody = body;
	    }
   }
	
	
	/** 
     * 鎶婂瓧鑺傛暟缁勪繚瀛樹负涓?涓枃浠? 
     *  
     * @param b 
     * @param outputFile 
     * @return 
     */  
    public  boolean MsgFileSave(String outputFile) {  
    	 try {
    	File f = new File(outputFile);
    	FileOutputStream fos = null;    
        BufferedInputStream bis = null;    
        int BUFFER_SIZE = 1024; 
        byte[] buf = new byte[BUFFER_SIZE];    
        int size = 0;    
        bis = new BufferedInputStream(fileContentStream);    
		fos = new FileOutputStream(f);
        while ( (size = bis.read(buf)) != -1)     
          fos.write(buf, 0, size);   
        bis.close();
        fos.close();    
        return true;
    	 } catch (Exception e) {
 			e.printStackTrace();
 			return false;
 		}    
    }  
 
    public boolean MsgFileLoad(String fileName) throws IOException{
    	File file = new File(fileName);
    	if(file.exists()){
    	fileContentStream = new FileInputStream(new File(fileName));
    	MsgFileBody();
    	}else{
    		mFileBody = new byte[0];
    	}
    	return true;
    }
    
/*    public boolean MsgFileLoad(String fileName) throws IOException{
    	File f = new File(fileName);  
        if (!f.exists()) {  
            throw new FileNotFoundException(fileName);  
        }
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());  
        BufferedInputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(f));  
            int buf_size = 1024;  
            byte[] buffer = new byte[buf_size];  
            int len = 0;  
            while (-1 != (len = in.read(buffer, 0, buf_size))) {  
                bos.write(buffer, 0, len);  
            } 
            mFileBody = bos.toByteArray(); 
            isLoadFile = true;
            return true;
        } catch (IOException e) {  
            e.printStackTrace();  
            throw e;  
        } finally {  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            bos.close();  
        } 
    }*/
    
    //鑾峰彇setMsgByName鐨勫?煎苟鎶婂皝瑁呮垚JSON鏁版嵁
    public String GetHashToJson(){
    	JSONObject json = new JSONObject();
    	for(Iterator<String> iterator = saveFormParam.keySet().iterator();iterator.hasNext();){
			String key = iterator.next();
			json.put(key, saveFormParam.get(key));
		}
		return json.toString();
	}

    // char[]杞琤yte[]
    public static byte[] getBytes (char[] chars) 
    {
    	Charset cs = Charset.forName ("UTF-8");
    	CharBuffer cb = CharBuffer.allocate (chars.length);
    	cb.put (chars);
    	cb.flip ();
    	ByteBuffer bb = cs.encode (cb);
    	return bb.array();
    }

    /**
     * @deprecated:灏嗘枃浠剁殑浜岃繘鍒舵暟鎹缃埌淇℃伅鍖呬腑
     * @param response
     * @throws IOException
     */
    public void Send(HttpServletResponse response, int codec) throws IOException{
    	try{
    		String getJsonStr = GetHashToJson();
    		response.reset();
    		response.setHeader("RName", new String( getJsonStr.getBytes("gb2312"), "ISO8859-1" )); 
				if (mFileBody.length != 0) {
					response.setCharacterEncoding("utf-8");
					response.setContentType("application/x-msdownload;charset=utf-8");
					if(codec == 0)
					{
						response.setContentLength(mFileBody.length);
						response.getOutputStream().write(mFileBody, 0, mFileBody.length);
					}
					else if(codec == 1)
					{
						//char[] charsFileBody = Base64.encode(mFileBody);
						//response.setContentLength(charsFileBody.length);
						//System.out.println("mFileBody.length=" + mFileBody.length + " charsFileBody.length=" + charsFileBody.length);
						//response.getOutputStream().write(getBytes(charsFileBody), 0, charsFileBody.length);
					}
					
					response.getOutputStream().flush();
					response.getOutputStream().close();

				} else {
					response.setHeader("MsgError", iMsgServer2015.MsgError);
				}
			response.flushBuffer();
    	}catch(Exception e){
    		//e.printStackTrace();
    	}
    } 	
}
