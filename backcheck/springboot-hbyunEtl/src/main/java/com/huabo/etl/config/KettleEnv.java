package com.huabo.etl.config;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.core.util.EnvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName : KettleEnv
 * @Description : kettle环境
 * @Author : zhibo.cao
 * @Date: 2022-12-01 14:26:34
 */
@Component
public class KettleEnv {
    public static final Logger log = LoggerFactory.getLogger(KettleEnv.class);

    /**
     * 插件位置
     */
    private static String pluginPath;

    @Value("${kettle.plugin}")
    public void setPluginPath(String pluginPath) {
        KettleEnv.pluginPath = pluginPath;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println(pluginPath);
            //增加自定义的插件库
            int lengths = StepPluginType.getInstance().getPluginFolders().size();
            if (!StepPluginType.getInstance().getPluginFolders().stream().anyMatch(a -> a.getFolder().equals(pluginPath))) {
                StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(pluginPath, false, true));
            }
            KettleEnvironment.init();
            EnvUtil.environmentInit();
            log.info("Kettle环境初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Kettle环境初始化失败");
        }
    }

//    @Bean
//    public KettleUtil initKettleUtil(){
//        return new KettleUtil();
//    }


//    /**
//     * 初始化环境
//     */
//    public class StartInit implements InitializingBean {
//
//        @Override
//        public void afterPropertiesSet() throws Exception {
//            KettleEnv.init();
//
//        }
//
//    }
}
