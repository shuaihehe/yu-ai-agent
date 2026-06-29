package com.yupi.yuaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import com.yupi.yuaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类(提供文件读写功能)
 */
public class FileOperationTool {

    private final String FILE_DIR = FileConstant.FILE_SAVE_DIR + "/file";

    @Tool(description = "读取文件内容")
    public String readFile(@ToolParam(description = "文件名称") String fileName) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "文件读取失败：" + e.getMessage();
        }

    }

    @Tool(description = "写入文件内容")
    public String writeFile(@ToolParam(description = "文件名称") String fileName, @ToolParam(description = "文件内容") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "文件写入成功！" + filePath;
        } catch (Exception e) {
            return "文件写入失败：" + e.getMessage();
        }
    }
}
