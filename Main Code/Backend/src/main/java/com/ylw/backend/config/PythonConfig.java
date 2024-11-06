package com.ylw.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@Configuration
public class PythonConfig {

    // Python解释器路径
    private static final String PYTHON_INTERPRETER_PATH = "E:\\software\\Anaconda3\\install\\envs\\resume\\python.exe";

    // Python脚本路径
    private static final String PYTHON_SCRIPT_PATH = "E:\\study\\Big_ruan\\code\\github_code\\LLM-Intelligent-Resume-Parsing-System\\Main Code\\Algorithm\\all.py";

    @Bean
    public String pythonInterpreterPath() {
        return PYTHON_INTERPRETER_PATH;
    }

    @Bean
    public String pythonScriptPath() {
        return PYTHON_SCRIPT_PATH;
    }

    // 如果需要在其他地方注入使用，可以这样获取：
    // @Autowired
    // private String pythonInterpreterPath;
    // @Autowired
    // private String pythonScriptPath;
}