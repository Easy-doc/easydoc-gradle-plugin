![logo](logo.png)
# easydoc-gradle-plugin

easy-doc的gradle插件，用于存储过滤后的源文件，文件会在resouce中生成

# 引入依赖方法
在build.gradle中引入
```gradle
buildscript {
    repositories {
        maven {
            url 'https://oss.sonatype.org/content/repositories/releases/'
        }
    }
    dependencies {
        classpath 'com.stalary:easydoc-gradle-plugin-0.0.1-SNAPSHOT' // 依赖远程插件
        // classpath files('easydoc-gradle-plugin-0.0.1.jar') // 依赖本地文件，需要将jar文件放到项目根目录
    }
}
apply plugin: 'EasyDocPlugin'
easydoc {
    path = 'com.easydoc' // 需要扫描的包路径
}
```

# 使用方法
gradle easydoc即可生成easydoc.txt