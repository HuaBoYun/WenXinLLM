package com.global.treasurer.common.utils.poi;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Excel工具类
 */
public class ExcelUtil<T> {
    private Class<T> clazz;
    
    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * 导出Excel
     */
    public void exportExcel(List<T> list, String sheetName) {
        // 这里可以实现具体的Excel导出逻辑
        // 为了简化，暂时留空
    }

    /**
     * 导出Excel（带响应对象）
     */
    public void exportExcel(HttpServletResponse response, List<T> list, String sheetName) {
        // 这里可以实现具体的Excel导出逻辑
        // 为了简化，暂时留空
    }
    
    /**
     * 导入Excel
     */
    public List<T> importExcel(String filePath) {
        // 这里可以实现具体的Excel导入逻辑
        // 为了简化，暂时返回空列表
        return null;
    }
}
