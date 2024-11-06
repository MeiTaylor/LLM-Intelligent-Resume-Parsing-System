package com.ylw.backend.service.impl;

import com.ylw.backend.config.PythonConfig;
import com.ylw.backend.service.ResumeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PreDestroy;  // 添加这行导入

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ResumeService implements ResumeServiceInterface {

    private final String pythonInterpreterPath;
    private final String pythonScriptPath;

    // 创建线程池
    private final ExecutorService executorService;

    // 用于生成线程名称
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    @Autowired
    public ResumeService(PythonConfig pythonConfig) {
        this.pythonInterpreterPath = pythonConfig.pythonInterpreterPath();
        this.pythonScriptPath = pythonConfig.pythonScriptPath();

        // 创建自定义线程池
        this.executorService = new ThreadPoolExecutor(
                2,  // 核心线程数
                4,  // 最大线程数
                60L, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(10), // 任务队列
                r -> new Thread(r, "ResumeParser-" + threadNumber.getAndIncrement()), // 线程工厂
                new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略
        );
    }

    @Override
    public void parseResume(String resumePath) {
        CompletableFuture.runAsync(() -> runPythonScript(resumePath), executorService)
                .exceptionally(throwable -> {
                    System.err.println("简历解析发生异常: " + throwable.getMessage());
                    throwable.printStackTrace();
                    return null;
                });

        // 读取json文件内容，存数据库

    }

    /**
     * 内部方法：运行Python脚本并传递参数
     * @param args 传递给Python脚本的参数
     */
    private void runPythonScript(String... args) {
        String[] command = new String[2 + args.length];
        command[0] = pythonInterpreterPath;
        command[1] = pythonScriptPath;
        System.arraycopy(args, 0, command, 2, args.length);

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            final Process finalProcess = process;

            // 创建两个独立的线程来读取输出流和错误流
            CompletableFuture<Void> outputFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(finalProcess.getInputStream(), "UTF-8"))) {
                    String s;
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println("输出: " + s);
                    }
                } catch (IOException e) {
                    System.err.println("读取输出流时发生错误: " + e.getMessage());
                }
            }, executorService);

            CompletableFuture<Void> errorFuture = CompletableFuture.runAsync(() -> {
                try (BufferedReader stdError = new BufferedReader(new InputStreamReader(finalProcess.getErrorStream(), "UTF-8"))) {
                    String s;
                    while ((s = stdError.readLine()) != null) {
                        System.err.println("错误: " + s);
                    }
                } catch (IOException e) {
                    System.err.println("读取错误流时发生错误: " + e.getMessage());
                }
            }, executorService);

            // 等待两个流都读取完成
            CompletableFuture.allOf(outputFuture, errorFuture).join();

            // 等待进程完成
            int exitCode = finalProcess.waitFor();
            System.out.println("Python脚本退出码: " + exitCode);

        } catch (IOException | InterruptedException e) {
            System.err.println("执行Python脚本时发生异常: " + e.getMessage());
            e.printStackTrace();
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    // 在Spring容器销毁时关闭线程池
    @PreDestroy
    public void destroy() {
        try {
            System.out.println("正在关闭简历解析服务的线程池...");
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}