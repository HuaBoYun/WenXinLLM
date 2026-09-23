package com.management.accountant.util;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zh 静态化方法
 **/
public class FreeMarkerUtil {
	/**
	 * 
	 * 生成HTML静态页面的公公方法
	 * 
	 * @param fmc
	 * @param templateName 模板的名称
	 * @param request
	 * @param map          生成模板需要的数据
	 * @param filePath     相对于web容器的路径
	 * @param fileName     要生成的文件的名称，带扩展名
	 * @param advicenote
	 * @param orgname
	 * 
	 *//*
		 * public static void createHtml(FreeMarkerConfig fmc, String templateName,
		 * HttpServletRequest request, Map<?, ?> map, String filePath, String fileName,
		 * TblNbsjAdvicenote advicenote, String orgname) throws Exception{ Writer out =
		 * null; OutputStream outputStream = null; String midlePath = filePath + "/" +
		 * DateUtils.dateToUnixTimestamp(DateUtils.getNowTime(),"yyyy-MM-dd HH:mm:ss")+
		 * ".doc"; try { Template template = fmc.getConfiguration()
		 * .getTemplate(templateName); template.setEncoding("UTF-8"); String htmlPath =
		 * filePath + "/" + fileName; File htmlFile = new File(midlePath); if
		 * (!htmlFile.getParentFile().exists()) { htmlFile.getParentFile().mkdirs(); }
		 * if (!htmlFile.exists()) { htmlFile.createNewFile(); } outputStream = new
		 * FileOutputStream(midlePath); out = new
		 * OutputStreamWriter(outputStream,"UTF-8"); template.process(map, out);
		 * 
		 * out.flush(); outputStream.flush(); Document doc= new Document(midlePath);
		 * Section sec = doc.getSections().get(0); AddHeaderFooter(sec,filePath+
		 * "/zhdoclogo.png",advicenote,orgname); doc.saveToFile(htmlPath);
		 * System.out.println("生成成功"); } catch (UnsupportedEncodingException e) {
		 * e.printStackTrace(); throw e; } catch (FileNotFoundException e) {
		 * e.printStackTrace(); throw e; } catch (IOException e) { e.printStackTrace();
		 * throw e; } catch (TemplateException e) { e.printStackTrace(); throw e; }
		 * finally { try { if(out!=null) out.close(); // out = null; } catch
		 * (IOException e) { e.printStackTrace(); throw e; } if(outputStream!=null){ try
		 * { outputStream.close(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); throw e; } } FileUtil.deleteFile(midlePath);
		 * } }
		 */

	/**
	 * @param request
	 * @param filePath 文件存放的路径
	 * @param fileName 文件的名称，需要扩展名
	 * @return
	 */
	public static Map<String, Object> htmlFileHasExist(HttpServletRequest request, String filePath, String fileName) {
		Map<String, Object> map = new HashMap<String, Object>();
		String htmlPath = filePath + "doc/" + fileName;
		File htmlFile = new File(htmlPath);
		if (htmlFile.exists()) {
			map.put("exist", true);
		} else {
			map.put("exist", false);
		}
		return map;
	}


	/*
	 * private static void AddHeaderFooter(Section sec, String picSrc,
	 * TblNbsjAdvicenote advicenote, String orgname) { // 加载图片添加到页眉，并设置图片在段落中的对齐方式
	 * HeaderFooter header = sec.getHeadersFooters().getHeader(); Paragraph hpara =
	 * header.addParagraph(); DocPicture pic = hpara.appendPicture(picSrc);
	 * pic.setHorizontalAlignment(ShapeHorizontalAlignment.Left);
	 * pic.setVerticalOrigin(VerticalOrigin.Top_Margin_Area);
	 * pic.setVerticalAlignment(ShapeVerticalAlignment.Bottom); pic.setHeight(26);
	 * pic.setWidth(120); pic.setDistanceLeft(20);
	 * 
	 * // 添加文字到页眉，并设置字体、字号、字体加粗、对齐方式 TextRange txt = hpara.appendText(orgname + " "
	 * + advicenote.getTblCreater().getTblOrganization().getOrgname() +
	 * "                           机密文件");
	 * txt.getCharacterFormat().setUnderlineStyle(UnderlineStyle.None);
	 * txt.getCharacterFormat().setTextColor(Color.GRAY);
	 * txt.getCharacterFormat().setFontName("宋体");
	 * txt.getCharacterFormat().setFontSize(9f);
	 * txt.getCharacterFormat().setBold(false);
	 * hpara.getFormat().setHorizontalAlignment(HorizontalAlignment.Right); //
	 * 设置图片的文本环绕方式、页眉底部边线（粗细、间距） pic.setTextWrappingStyle(TextWrappingStyle.Behind);
	 * pic.setDistanceTop(40);
	 * hpara.getFormat().getBorders().getBottom().setBorderType(BorderStyle.Single);
	 * hpara.getFormat().getBorders().getBottom().setLineWidth(0.5f);
	 * hpara.getFormat().getBorders().setSpace(6f);
	 * hpara.getFormat().getBorders().setColor(Color.GRAY);
	 * hpara.getFormat().setLeftIndentEx(1f);
	 * hpara.getFormat().setRightIndentEx(1f);
	 * sec.getPageSetup().setHeaderDistance(60);
	 * 
	 * // 添加页码到页脚，并设置页脚对齐方式，顶部边线粗细、间距 HeaderFooter footer =
	 * sec.getHeadersFooters().getFooter(); Table table = footer.addTable();
	 * 
	 * table.resetCells(1, 3);// 指定表格行数、列数
	 * table.applyStyle(DefaultTableStyle.Light_Shading);// 设置表格样式
	 * 
	 * TableRow dataRow = table.getRows().get(0); dataRow.setHeight(35);
	 * dataRow.setHeightType(TableRowHeightType.Exactly);
	 * dataRow.getCells().get(0).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(0).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); TextRange range = dataRow.getCells().get(0).addParagraph()
	 * .appendText(advicenote.getProject().getPlanYear() + "年" +
	 * advicenote.getProject().getProjectName());
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * dataRow.getCells().get(1).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(1).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); Paragraph fpara =
	 * dataRow.getCells().get(1).addParagraph(); range =
	 * fpara.appendText("审计通知书\r第"); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendField("",
	 * FieldType.Field_Page); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendText("页 共");
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendField("",
	 * FieldType.Field_Num_Pages); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendText("页");
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * dataRow.getCells().get(2).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(2).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); range =
	 * dataRow.getCells().get(2).addParagraph().appendText("通知书编号\r" +
	 * advicenote.getAdvicecoed()); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center); sec.getPageSetup().setFooterDistance(30); }
	 */

	
	public static void createHtml(FreeMarkerConfig fmc, String templateName, HttpServletRequest request,
			Map<String, String> map, String filePath, String fileName) throws Exception {
		Writer out = null;
        OutputStream outputStream = null;
        try {
            Template template = fmc.getConfiguration()
                    .getTemplate(templateName);
            template.setEncoding("UTF-8");
            String htmlPath = filePath
                    + "/" + fileName;
            File htmlFile = new File(htmlPath);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            if (!htmlFile.exists()) {
                htmlFile.createNewFile();
            }
            outputStream = new FileOutputStream(htmlPath);
            out = new OutputStreamWriter(outputStream,"UTF-8");
            template.process(map, out);
            
            out.flush();
            System.out.println("生成成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (TemplateException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
            	if(out!=null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
            if(outputStream!=null){
            	try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
            }
        }
		
	}
	 

	/*
	 * public static void createHtml(FreeMarkerConfig fmc, String templateName,
	 * HttpServletRequest request, Map<String, String> map, String filePath, String
	 * fileName, TblReport report, TblnbsjProject project) throws Exception { Writer
	 * out = null; OutputStream outputStream = null; String midlePath = filePath +
	 * "\\" + DateUtils.dateToUnixTimestamp(DateUtils.getNowTime(),
	 * "yyyy-MM-dd HH:mm:ss") + ".doc"; try { Template template =
	 * fmc.getConfiguration().getTemplate(templateName);
	 * template.setEncoding("UTF-8"); String htmlPath = filePath + "\\" + fileName;
	 * File htmlFile = new File(midlePath); if (!htmlFile.getParentFile().exists())
	 * { htmlFile.getParentFile().mkdirs(); } if (!htmlFile.exists()) {
	 * htmlFile.createNewFile(); } outputStream = new FileOutputStream(midlePath);
	 * out = new OutputStreamWriter(outputStream, "UTF-8"); template.process(map,
	 * out);
	 * 
	 * out.flush(); outputStream.flush(); Document doc = new Document(midlePath);
	 * Section sec = doc.getSections().get(0); // AddHeaderFooter(sec,filePath+
	 * "\\zhdoclogo.png",report,project);//doc文档页眉页脚 doc.saveToFile(htmlPath);
	 * System.out.println("生成成功"); } catch (UnsupportedEncodingException e) {
	 * e.printStackTrace(); throw e; } catch (FileNotFoundException e) {
	 * e.printStackTrace(); throw e; } catch (IOException e) { e.printStackTrace();
	 * throw e; } catch (TemplateException e) { e.printStackTrace(); throw e; }
	 * finally { try { if (out != null) out.close(); // out = null; } catch
	 * (IOException e) { e.printStackTrace(); throw e; } if (outputStream != null) {
	 * try { outputStream.close(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); throw e; } } FileUtil.deleteFile(midlePath);
	 * } }
	 */
	/*
	 * private static void AddHeaderFooter(Section sec, String picSrc, TblReport
	 * report, TblnbsjProject project) {
	 * sec.getPageSetup().setDifferentFirstPageHeaderFooter(true);
	 * 
	 * // 加载图片添加到页眉，并设置图片在段落中的对齐方式 HeaderFooter header =
	 * sec.getHeadersFooters().getHeader(); Paragraph hpara = header.addParagraph();
	 * DocPicture pic = hpara.appendPicture(picSrc);
	 * pic.setHorizontalAlignment(ShapeHorizontalAlignment.Left);
	 * pic.setVerticalOrigin(VerticalOrigin.Top_Margin_Area);
	 * pic.setVerticalAlignment(ShapeVerticalAlignment.Bottom); pic.setHeight(26);
	 * pic.setWidth(120); pic.setDistanceLeft(20);
	 * 
	 * // 添加文字到页眉，并设置字体、字号、字体加粗、对齐方式 TextRange txt = hpara.appendText("机密文件");
	 * txt.getCharacterFormat().setUnderlineStyle(UnderlineStyle.None);
	 * txt.getCharacterFormat().setTextColor(Color.GRAY);
	 * txt.getCharacterFormat().setFontName("宋体");
	 * txt.getCharacterFormat().setFontSize(10f);
	 * txt.getCharacterFormat().setBold(false);
	 * hpara.getFormat().setHorizontalAlignment(HorizontalAlignment.Right); //
	 * 设置图片的文本环绕方式、页眉底部边线（粗细、间距） pic.setTextWrappingStyle(TextWrappingStyle.Behind);
	 * pic.setDistanceTop(40);
	 * hpara.getFormat().getBorders().getBottom().setBorderType(BorderStyle.Single);
	 * hpara.getFormat().getBorders().getBottom().setLineWidth(0.5f);
	 * hpara.getFormat().getBorders().setSpace(6f);
	 * hpara.getFormat().getBorders().setColor(Color.GRAY);
	 * sec.getPageSetup().setHeaderDistance(60);
	 * 
	 * // 添加页码到页脚，并设置页脚对齐方式，顶部边线粗细、间距 HeaderFooter footer =
	 * sec.getHeadersFooters().getFooter(); Table table = footer.addTable();
	 * 
	 * table.resetCells(1, 4);// 指定表格行数、列数
	 * 
	 * table.applyStyle(DefaultTableStyle.Light_Shading);// 设置表格样式
	 * 
	 * TableRow dataRow = table.getRows().get(0); dataRow.setHeight(35);
	 * dataRow.setHeightType(TableRowHeightType.Exactly);
	 * dataRow.getCells().get(0).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(0).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); TextRange range = null; if (project == null) { Calendar c
	 * = Calendar.getInstance(); c.setTime(report.getReporttime()); range =
	 * dataRow.getCells().get(0).addParagraph() .appendText(c.get(Calendar.YEAR) +
	 * "年" + report.getReportname()); } else { range =
	 * dataRow.getCells().get(0).addParagraph() .appendText(project.getPlanYear() +
	 * "年" + project.getProjectName()); }
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(9f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * dataRow.getCells().get(1).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(1).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); Paragraph fpara =
	 * dataRow.getCells().get(1).addParagraph();
	 * 
	 * if ("工作日志".equals(report.getReporttype())) { range =
	 * fpara.appendText(report.getReporttype() + "\r第"); } else { range =
	 * fpara.appendText(report.getReporttype() + "\r第"); }
	 * 
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendField("",
	 * FieldType.Field_Page); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendText("页 共");
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendField("",
	 * FieldType.Field_Num_Pages); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false); range = fpara.appendText("页");
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * dataRow.getCells().get(2).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(2).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); String no = ""; if (report.getReportmode() != null &&
	 * !"".equals(report.getReportmode())) { no = report.getReportmode(); } range =
	 * dataRow.getCells().get(2).addParagraph().appendText("报告方式\r" + no);
	 * range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * dataRow.getCells().get(3).getCellFormat().setVerticalAlignment(
	 * VerticalAlignment.Middle);
	 * dataRow.getCells().get(3).getCellFormat().getBorders().setBorderType(
	 * BorderStyle.None); range = dataRow.getCells().get(3).addParagraph()
	 * .appendText("报告日期\r" + DateUtils.parseDate(report.getReporttime(),
	 * "yyyy-MM-dd")); range.getCharacterFormat().setFontName("宋体");
	 * range.getCharacterFormat().setFontSize(10f);
	 * range.getCharacterFormat().setBold(false);
	 * range.getOwnerParagraph().getFormat().setHorizontalAlignment(
	 * HorizontalAlignment.Center);
	 * 
	 * // 添加首页 header = sec.getHeadersFooters().getFirstPageHeader(); hpara =
	 * header.addParagraph(); pic = hpara.appendPicture(picSrc);
	 * pic.setHorizontalAlignment(ShapeHorizontalAlignment.Left);
	 * pic.setVerticalOrigin(VerticalOrigin.Top_Margin_Area);
	 * pic.setVerticalAlignment(ShapeVerticalAlignment.Bottom); pic.setHeight(26);
	 * pic.setWidth(120); pic.setDistanceLeft(20);
	 * 
	 * // 添加文字到页眉，并设置字体、字号、字体加粗、对齐方式 txt = hpara.appendText("机密文件");
	 * txt.getCharacterFormat().setUnderlineStyle(UnderlineStyle.None);
	 * txt.getCharacterFormat().setTextColor(Color.GRAY);
	 * txt.getCharacterFormat().setFontName("宋体");
	 * txt.getCharacterFormat().setFontSize(10f);
	 * txt.getCharacterFormat().setBold(true);
	 * hpara.getFormat().setHorizontalAlignment(HorizontalAlignment.Right); //
	 * 设置图片的文本环绕方式、页眉底部边线（粗细、间距） pic.setTextWrappingStyle(TextWrappingStyle.Behind);
	 * pic.setDistanceTop(40);
	 * hpara.getFormat().getBorders().getBottom().setBorderType(BorderStyle.Single);
	 * hpara.getFormat().getBorders().getBottom().setLineWidth(0.5f);
	 * hpara.getFormat().getBorders().setSpace(6f);
	 * hpara.getFormat().getBorders().setColor(Color.GRAY);
	 * sec.getPageSetup().setHeaderDistance(60);
	 * sec.getPageSetup().setFooterDistance(30); }
	 */
}