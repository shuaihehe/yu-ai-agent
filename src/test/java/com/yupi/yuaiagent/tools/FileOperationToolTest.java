package com.yupi.yuaiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class FileOperationToolTest {

    @Test
    void readFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "编程导航.txt";
        String result = fileOperationTool.readFile(fileName);
        Assertions.assertNotNull(result, "File content should not be null");
        System.out.println(result);
    }

    @Test
    void writeFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String fileName = "编程导航.txt";
        String content = "程序员学习导航";
        String writeResult = fileOperationTool.writeFile(fileName, content);
        Assertions.assertNotNull(writeResult, "Write file result should not be null");
    }

}