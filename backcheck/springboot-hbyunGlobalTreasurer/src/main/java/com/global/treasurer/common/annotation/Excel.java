package com.global.treasurer.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel注解 (兼容性注解,实际使用ExcelField)
 * 
 * @author global-treasurer
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {
    
    /**
     * 导出字段名称
     */
    String name() default "";
    
    /**
     * 日期格式
     */
    String dateFormat() default "";
    
    /**
     * 读取内容转表达式 (如: 0=男,1=女,2=未知)
     */
    String readConverterExp() default "";
    
    /**
     * 分隔符,读取字符串组内容
     */
    String separator() default ",";
    
    /**
     * BigDecimal 精度 默认:-1(默认不开启BigDecimal格式化)
     */
    int scale() default -1;
    
    /**
     * BigDecimal 舍入规则 默认:BigDecimal.ROUND_HALF_EVEN
     */
    int roundingMode() default 4;
    
    /**
     * 导出类型(0数字 1字符串)
     */
    int cellType() default 1;
    
    /**
     * 导出时在excel中每个列的高度 单位为字符
     */
    double height() default 14;
    
    /**
     * 导出时在excel中每个列的宽 单位为字符
     */
    double width() default 16;
    
    /**
     * 文字后缀,如% 90 变成90%
     */
    String suffix() default "";
    
    /**
     * 当值为空时,字段的默认值
     */
    String defaultValue() default "";
    
    /**
     * 提示信息
     */
    String prompt() default "";
    
    /**
     * 设置只能选择不能输入的列内容
     */
    String[] combo() default {};
    
    /**
     * 是否需要纵向合并单元格,应对需求:含有list集合单元格)
     */
    boolean needMerge() default false;
    
    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写
     */
    boolean isExport() default true;
    
    /**
     * 另一个类中的属性名称,支持多级获取,以小数点隔开
     */
    String targetAttr() default "";
    
    /**
     * 是否自动统计数据,在最后追加一行统计数据总和
     */
    boolean isStatistics() default false;
    
    /**
     * 导出字段对齐方式(0:默认,1:靠左,2:居中,3:靠右)
     */
    int align() default 0;
    
    /**
     * 自定义数据处理器
     */
    Class<?> handler() default void.class;
    
    /**
     * 字典类型
     */
    String dictType() default "";
    
    /**
     * 排序
     */
    int sort() default Integer.MAX_VALUE;
}

