![logo](logo.png)
# easydoc-gradle-plugin

easy-doc的gradle插件，用于存储过滤后的源文件，文件会在resouce中生成

# 引入依赖方法
在build.gradle中引入
```gradle
buildscript {
    dependencies {
        classpath 'com.stalary:easydoc-gradle-plugin-${version}' // 依赖远程插件
    }
}
apply plugin: 'EasyDocPlugin'
easydoc {
    path = 'com.easydoc' // 需要扫描的包路径
}
```

# 使用方法
gradle easydoc即可生成easydoc.txt
