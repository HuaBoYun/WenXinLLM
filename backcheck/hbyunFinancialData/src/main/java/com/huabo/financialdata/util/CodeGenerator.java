package com.huabo.financialdata.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class CodeGenerator {

    public static void main(String[] args) {
        String password = "REDACTED";
        String username = "REDACTED";
        String url = "jdbc:oracle:thin:@192.0.2.200:1521:orcl";
        DataSourceConfig.Builder dataSourceConfig = new DataSourceConfig.Builder(url, username, password);

        String finalProjectPath = Objects.requireNonNull(CodeGenerator.class.getClassLoader().getResource(""))
                .getPath().replace("/target/classes/", "");

        List<String> tableList = new ArrayList<>();
        tableList.add("user");    // 生成全部table注释add掉就好
        // 写死
        creteModel(dataSourceConfig, finalProjectPath, Boolean.TRUE, tableList);

        // 手动输入（互动式）
//        createSingleModel(dataSourceConfig,finalProjectPath);
    }

    private static void creteModel(DataSourceConfig.Builder dataSourceConfig,
                                   String finalProjectPath,
                                   Boolean isOverride,
                                   List<String> tableList) {
        // dataSourceConfig数据源
        FastAutoGenerator.create(dataSourceConfig)
                // 全局代码配置类
                .globalConfig(builder -> {
                    builder.author("") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir() //禁止打开输出目录
                            .outputDir(finalProjectPath + "/src/main/java"); // 指定输出目录
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.huabo.know") // 设置父包名
                            .entity("entity") //设置entity包名
//                            .controller("controller")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
//                            .other("other")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, finalProjectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径

                })
                // 策略配置
                .strategyConfig(builder -> {

                    if (!CollectionUtils.isEmpty(tableList)) {
                        builder.addInclude(tableList);
                    }

//                    builder.addTablePrefix("wms_");// 设置过滤表前缀

                    builder.entityBuilder() // entity配置
//                            .enableRemoveIsPrefix()
                            .enableTableFieldAnnotation()
                            .enableLombok();

                    builder.controllerBuilder() // controller配置
                            .enableRestStyle();

                    builder.serviceBuilder() // service配置
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .fileOverride();

                    builder.mapperBuilder() // mapper配置
                            .enableBaseResultMap()
                            .enableMapperAnnotation()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper")
                            .enableBaseColumnList();
                    if (isOverride) {
                        builder.entityBuilder() // entity配置
                                .fileOverride();
                        builder.controllerBuilder() // controller配置
                                .fileOverride();
                        builder.serviceBuilder() // service配置
                                .fileOverride();
                        builder.mapperBuilder() // mapper配置
                                .fileOverride();
                    }
                })
                // 自定义模版引擎
                .injectionConfig(consumer -> {
                    Map<String, String> customFile = new HashMap<>();
                    // DTO
//        customFile.put("DTO.java", "/templates/entityDTO.java.ftl"); //自定义模版引擎
                    consumer.customFile(customFile);
                })
                // 选择模板引擎
                .templateEngine(new VelocityTemplateEngine())
                // 执行
                .execute();
    }

    private static void createSingleModel(DataSourceConfig.Builder dataSourceConfig, String finalProjectPath) {
        FastAutoGenerator.create(dataSourceConfig)
                // 全局配置
                .globalConfig((scanner, builder) ->
                        builder.author(scanner.apply("请输入作者名称？"))
                                .fileOverride()
                                .outputDir(finalProjectPath + "/src/main/java"))
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.huabao.know") // 设置父包名
                            .entity("entity") //设置entity包名
                            .other("mapper") // 设置dto包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, finalProjectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径

                })
                // 策略配置
                .strategyConfig((scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .controllerBuilder().enableRestStyle()
                        .entityBuilder().enableLombok()
                        .mapperBuilder().enableBaseResultMap().enableBaseColumnList()
                        .build())

                .execute();


    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}
