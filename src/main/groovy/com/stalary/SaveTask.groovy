package com.stalary

import groovy.json.JsonOutput
import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.charset.Charset
import java.util.regex.Matcher

/**
 * @author Stalary* @description
 * @date 2018/10/17
 */
@Slf4j
class SaveTask extends DefaultTask {

    private static final Map<String, String> PATH_MAP = new HashMap<>()

    private String path

    private String includeFile

    private String excludeFile

    private List<String> includeList = new ArrayList<>()

    private List<String> excludeList = new ArrayList<>()

    @TaskAction
    void save() {
        // 开始处理
        path = "${project.easydoc.path}"
        includeFile = "${project.easydoc.includeFile}"
        excludeFile = "${project.easydoc.excludeFile}"
        List<File> fileList = new ArrayList<>()
        String fileName = System.getProperty("user.dir") + "/src/main/java/" + path.replaceAll("\\.", "/")
        if (includeFile != '') {
            String[] includeSplit = includeFile.split(",")
            includeList.addAll(Arrays.asList(includeSplit))
        }
        if (excludeFile != '') {
            String[] includeSplit = excludeFile.split(",")
            excludeList.addAll(Arrays.asList(includeSplit))
        }
        File file = new File(fileName)
        getFile(file, fileList)
        file2String(fileList)
    }

    private void getFile(File file, List<File> fileList) {
        if (file.exists()) {
            if (file.isFile()) {
                // 获取去掉后缀的文件名
                String name = file.getName().split("\\.")[0]
                // 排除掉不需要的文件
                if (!excludeList.contains(name)) {
                    if (!includeList.isEmpty()) {
                        if (includeList.contains(name)) {
                            fileList.add(file)
                        }
                    } else {
                        fileList.add(file)
                    }
                }
            } else if (file.isDirectory()) {
                File[] files = file.listFiles()
                if (files != null) {
                    for (File single : files) {
                        getFile(single, fileList)
                    }
                }
            }
        }
    }

    private void file2String(List<File> fileList) {
        StringBuilder sb = new StringBuilder()
        pathMapper(fileList)
        sb.append(JsonOutput.toJson(PATH_MAP)).append(",,,")
        for (File file : fileList) {
            String fileName = file.getName()
            String name = fileName.substring(0, fileName.indexOf("."))
            sb.append(name).append("~~")
            BufferedReader reader = null
            // 读取单个文件
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")))
                String temp
                StringBuilder curSb = new StringBuilder()
                while ((temp = reader.readLine()) != null) {
                    curSb.append(temp)
                }
                StringBuilder cur = matching(curSb, name)
                sb.append(cur)
                // 每个文件以@@@分割
                sb.append("@@@")
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                reader.close()
            }
        }
        String fileName = System.getProperty("user.dir").replaceAll("\\.", "/") + "/src/main/resources/easydoc.txt"
        File file = new File(fileName)
        // 文件存在时，先删除
        if (file.exists()) {
            file.delete()
        }
        // 直接使用FileWriter默认使用（ISO-8859-1 or US-ASCII）西方编码，中文会乱码
        Writer writer = null
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), Charset.forName("UTF-8")))
            // 退出时存储消息
            writer.write(sb.toString())
            writer.flush()
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            writer.close()
        }
    }

    private StringBuilder matching(StringBuilder str, String name) {
        String regex = "\\/\\*(\\s|.)*?\\*\\/"
        Matcher matcher = RegularExpressionUtils.createMatcherWithTimeout(str.toString(), regex, 200)
        StringBuilder sb = new StringBuilder()
        try {
            while (matcher.find()) {
                String temp = matcher
                        .group()
                        .replaceAll("\\/\\*\\*", "")
                        .replaceAll("\\*\\/", "")
                        .replaceAll("\\*", "")
                        .replaceAll(" +", " ")
                // 每次匹配以~~分割
                sb.append(temp).append("~~")
            }
        } catch(Exception e) {
            log.warn("easydoc matching error, name={}, info={}", name, e.getMessage())
        }
        return sb
    }

    /**
     * 生成pathMapper映射
     */
    private void pathMapper(List<File> fileList) {
        // !!! 插件用lambda报错
        for (File file : fileList) {
            NamePack namePack = path2Pack(file.getPath())
            PATH_MAP.put(namePack.getName(), namePack.getPackPath())
        }
    }

    /**
     * 将文件路径生成 文件名:包路径 的映射
     */
    private NamePack path2Pack(String filePath) {
        String temp = filePath.replaceAll("/", ".")
        String packPath = temp.substring(temp.indexOf(path))
        packPath = packPath.substring(0, packPath.lastIndexOf("."))
        return new NamePack(packPath.substring(packPath.lastIndexOf(".") + 1), packPath)
    }
}
