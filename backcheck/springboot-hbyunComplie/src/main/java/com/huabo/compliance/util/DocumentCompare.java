package com.huabo.compliance.util;


public class DocumentCompare {
//
//	//创建.doc后缀的word
//	    public static void main(String[] args) {
//	        String path = "E:\\YishiFile";
//	        String fileName = "test.doc";
//	        //createWord(path,fileName);
//	        //writeDataDocx
//	        String data = "helloword";
//	        try {
//				writeDataDocx(path+"\\"+fileName,data,true,12);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//	    }
//	    public static void createWord(String path,String fileName) throws Exception {
//	        File file = new File(path);
//	        if (!file.exists()){
//	        	file.mkdirs();
//	        }
//	        	@SuppressWarnings("resource")
//	        	XWPFDocument document = new XWPFDocument();
//	        	OutputStream stream = null;
//	        	try {
//	        		stream = new FileOutputStream(new File(file, fileName));
//	        		document.write(stream);
//	        	} catch (FileNotFoundException e) {
//	        		throw e;
//	        	} catch (IOException e) {
//	        		throw e;
//	        	} finally {
//	        		if (stream != null) ;
//	        		try {
//	        			stream.close();
//	        		} catch (IOException e) {
//	                e.printStackTrace();
//	        		}
//	        	}
//	    }
//
//	    //向word中写入数据
//
//	    /**
//	     * 有些方法需要传特殊类型的参数的时候，一般可以用★静态的接口.参数★来传参
//	     *
//	     * @param path
//	     * @param data
//	     */
//	    public static void writeDataDocx(String path, String data, boolean jiacu, int size) throws Exception {
//	        InputStream istream = null;
//	        OutputStream ostream = null;
//	        try {
//	            istream = new FileInputStream(path);
//	            ostream = new FileOutputStream(path);
//	            XWPFDocument document = new XWPFDocument();
//	            //添加一个段落
//	            XWPFParagraph p1 = document.createParagraph();
//	            XWPFRun r1 = p1.createRun();//p1.createRun()将一个新运行追加到这一段
//
//	            r1.setText(data);
//
//	            r1.setBold(jiacu);//---"加黑加粗"
//	            r1.setFontSize(size);//---字体大小
//
//	            document.write(ostream);
//	            System.out.println("创建word成功");
//	        } catch (FileNotFoundException e) {
//	           throw e;
//	        } catch (IOException e) {
//	        	throw e;
//	        } finally {
//	            if (istream != null) {
//	                try {
//	                    istream.close();
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	            if (ostream != null) {
//	                try {
//	                    ostream.close();
//	                } catch (IOException e) {
//	                    e.printStackTrace();
//	                }
//	            }
//	        }
//	    }
//
//
//	    //读取数据 docx
//	    public static String readDataDocx(String filePath) {
//	        String content = "";
//	        InputStream istream = null;
//	        try {
//	            istream = new FileInputStream(filePath);
//	            @SuppressWarnings("resource")
//	            XWPFDocument document = new XWPFDocument(istream);
//	            //getLastParagraph()返回包含页眉或页脚的文本的段落
//	            //getText()返回文档所有文本
//	            content = document.getLastParagraph().getText();//★★★★★
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        } finally {
//	            if (istream != null) {
//
//	            }
//	        }
//	        return content;
//	    }
	}

