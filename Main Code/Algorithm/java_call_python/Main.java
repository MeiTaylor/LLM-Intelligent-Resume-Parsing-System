import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    /**
     * 运行指定的Python脚本并传递参数
     *
     * @param pythonInterpreterPath Python解释器的绝对路径
     * @param pythonScriptPath      Python脚本的绝对路径
     * @param args                  传递给Python脚本的参数
     */
    public static void runPythonScript(String pythonInterpreterPath, String pythonScriptPath, String... args) {
        // 将Python解释器路径、脚本路径和参数整合到一个命令数组中
        String[] command = new String[2 + args.length];
        command[0] = pythonInterpreterPath;
        command[1] = pythonScriptPath;
        System.arraycopy(args, 0, command, 2, args.length);

        try {
            // 启动进程执行Python脚本
            Process process = Runtime.getRuntime().exec(command);

            // 获取Python脚本的输出
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 打印Python脚本的标准输出
            String s;
            System.out.println("Python脚本输出:");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // 打印Python脚本的错误输出（如果有）
            System.out.println("Python脚本错误输出（如果有）:");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            // 等待进程完成
            int exitCode = process.waitFor();
            System.out.println("Python脚本退出码: " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 设置Python解释器的绝对路径
        String pythonInterpreterPath = "E:\\software\\Anaconda3\\install\\envs\\resume\\python.exe";

        // 设置Python脚本的绝对路径
        String pythonScriptPath = "E:\\study\\Big_ruan\\code\\github_code\\LLM-Intelligent-Resume-Parsing-System\\Main Code\\Algorithm\\all.py";

        // 设置要传入的参数
        String resumePath = "E:\\study\\Big_ruan\\code\\my_code\\test_resume\\1.docx";

        // 调用runPythonScript方法并传入参数
        runPythonScript(pythonInterpreterPath, pythonScriptPath, resumePath);
    }
}
