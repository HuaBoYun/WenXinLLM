package com.huabo.system.utils;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.huabo.system.entity.TblProcessAnalysis;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class TblAnalysisXml {
	
	public static List<TblProcessAnalysis> analsisiXml(File file, String processid, String processname){
		 List<TblProcessAnalysis> list=new ArrayList<TblProcessAnalysis>();
		 try {   
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();   
		      DocumentBuilder builder = factory.newDocumentBuilder();   
		      Document doc = builder.parse(file);   
		      NodeList nl = doc.getElementsByTagName("userTask");   
		      for (int i = 0; i < nl.getLength(); i++) {
				  TblProcessAnalysis one=new TblProcessAnalysis();
		    	  one.setProcessid(processid);
		    	  one.setProcessname(processname);
		    	  Node node = nl.item(i);
		    	  NamedNodeMap namedNodeMap = node.getAttributes(); 
		    	  String textContent = namedNodeMap.getNamedItem("activiti:candidateGroups").getTextContent();
		    	  String taskid = namedNodeMap.getNamedItem("id").getTextContent();
		    	  if(textContent.indexOf("{")>0){
		    		  System.out.println(textContent.substring(textContent.indexOf("{"),textContent.lastIndexOf("}")));
		    		  one.setUserid(textContent.substring(textContent.indexOf("{")+1,textContent.lastIndexOf("}")));
		    		  System.out.println(taskid);
		    	  }else{
		    		  one.setRolename(textContent);
		    	  }
		    	  one.setUsertaskid(taskid);
		    	  list.add(one);
		      }   
		  } catch (Exception e) {   
		      e.printStackTrace();   
		  }   
		
		
		return list;
	}
	
	
	public static File zhfile(File file){
		File f=null;
		 try {
				 ZipFile zf = new ZipFile(file);
				 InputStream in = new BufferedInputStream(new FileInputStream(file));
				 Charset gbk = Charset.forName("gbk");
				 ZipInputStream zin = new ZipInputStream(in,gbk);
				 ZipEntry ze;
				 while((ze = zin.getNextEntry()) != null){
					 if(ze.toString().substring((ze.toString().lastIndexOf("."))).equals(".bpmn") || ze.toString().substring((ze.toString().lastIndexOf("."))).equals(".BPMN") 
				    		 ||ze.toString().substring((ze.toString().lastIndexOf("."))).equals(".XML")){
				    	 InputStream inputStream = zf.getInputStream(ze);
				    	 f=new File(ze.toString());
					 	  try {
					 		   OutputStream os = new FileOutputStream(f);
					 		   int bytesRead = 0;
					 		   byte[] buffer = new byte[8192];
					 		   while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
					 		      os.write(buffer, 0, bytesRead);
					 		   }
					 		   os.close();
					 		  inputStream.close();
					 		  } catch (Exception e) {
					 		   e.printStackTrace();
					 		  }
				     }
				     System.out.println();
				 }
				 zin.closeEntry();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		 return f;
	}
	

	public static void main(String arge[]) { 
		 try {
				String path = "C:\\Users\\thinkpad\\Desktop\\Registrationprocess.bpmn";
				File file =new File(path);
				 
				 analsisiXml(file, "Registrationprocess", "Registrationprocess");
				
			
			} catch (Exception e) {
				e.printStackTrace();
			}
	} 
}
