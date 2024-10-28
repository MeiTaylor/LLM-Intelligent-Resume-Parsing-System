# example.py
import sys
import io

# 强制使用UTF-8编码输出
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')


from setup_resume_analysis_folders_and_files import *
from convert_resume_to_txt import *
from baidu_parse_resume import *
from gpt_analyze_resume import *

import sys
import concurrent.futures

def analyze_resume(resume_path):
    # 从简历路径获取基本路径
    base_path = os.path.dirname(resume_path)
    print("基础路径：", base_path)

    # 调用函数以设置环境
    setup_resume_analysis_folders_and_files(base_path)
    print("设置环境完成")

    # 转换简历为文本格式，并获取转换后的文本文件路径
    convert_resume_to_txt(resume_path)
    print("转换完成")

    resume_txt_path = convert_resume_to_txt_for_gpt_analysis(resume_path)
    print(resume_txt_path)

    # 使用 concurrent.futures 并行运行三行代码
    start_time = time.time()
    with concurrent.futures.ThreadPoolExecutor() as executor:
        futures = [
            executor.submit(gpt_analyze_resume, resume_txt_path, "Basic_Resume_Analysis"),
            executor.submit(gpt_analyze_resume, resume_txt_path, "Job_Matching_Guide"),
            executor.submit(gpt_analyze_resume, resume_txt_path, "Talent_Portrait_Guide")
        ]
        
        # 等待所有线程完成
        for future in concurrent.futures.as_completed(futures):
            try:
                future.result()
                end_time = time.time()
                print(f"线程执行耗时 {end_time - start_time} 秒")
            except Exception as exc:
                print(f"线程执行时出错: {exc}")

def main():
    # 从命令行参数获取简历路径
    if len(sys.argv) < 2:
        print("请提供简历路径作为参数。")
        sys.exit(1)
    
    resume_path = sys.argv[1]
    analyze_resume(resume_path)

if __name__ == "__main__":
    main()
