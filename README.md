![logo](logo.png)
# easydoc-gradle-plugin

easy-doc的gradle插件，用于存储过滤后的源文件，文件会在resouce中生成

# 版本说明
## 1.0.0
- 第一个线上可用版本
- 可以选择解析指定文件以及跳过指定文件

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
    excludeFile = 'A,B,C' // 需要跳过的文件，用,隔开
    includeFile = 'D,F' // 路径下包含的文件，用,隔开
}
```

# 使用方法
gradle easydoc即可生成easydoc.txt
