package com.huabo.finance.unit;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {
	 //main函数
    public static void main(String[] args) {

        // 代码生成器
        AutoGenerator autoGenerator = new AutoGenerator();
 
        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir"); // 获取当前项目的路径
        globalConfig.setOutputDir(projectPath + "/src/main/java"); // 指定输出目录
        globalConfig.setAuthor("L"); // 作者名称
        globalConfig.setOpen(false); // 生成后是否打开文件夹
        globalConfig.setFileOverride(false); // 是否覆盖已有文件
     // 不需要ActiveRecord特性的请改为false
        globalConfig.setActiveRecord(false);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML columList
        globalConfig.setBaseColumnList(false);
        globalConfig.setSwagger2(true);
     // 自定义文件命名，注意 %s 会自动填充表实体属性！
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setServiceName("%sService");
        globalConfig.setControllerName("%sController");
        autoGenerator.setGlobalConfig(globalConfig);
 
        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //设置数据库类型
        dataSourceConfig.setDbType(DbType.DM);

        dataSourceConfig.setDriverName("dm.jdbc.driver.DmDriver");

        //用户名
        dataSourceConfig.setUsername("REDACTED");

        //密码
        dataSourceConfig.setPassword("REDACTED");

        //指定数据库
        dataSourceConfig.setUrl("jdbc:dm://192.0.2.200:5236/REDACTED?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        autoGenerator.setDataSource(dataSourceConfig);
        
 
        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.huabo.finance") // 自定义包路径
                     .setEntity("entity.caiji") // Entity包名
                     .setMapper("mapper") // Mapper包名
                     .setService("service") // Service包名
                     .setController("controller"); // Controller包名
        autoGenerator.setPackageInfo(packageConfig);
 
        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setNaming(NamingStrategy.underline_to_camel); // 数据库表映射到实体的命名策略
        strategyConfig.setInclude("GL_ASS_BALANE"); // 需要生成的表，多个表逗号分隔
        strategyConfig.setEntityLombokModel(true);
        autoGenerator.setStrategy(strategyConfig);
 
        // 执行生成
        autoGenerator.execute();

    }
}


