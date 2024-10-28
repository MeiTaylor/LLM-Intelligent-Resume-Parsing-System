from setup_resume_analysis_folders_and_files import *

from convert_resume_to_txt import *

from baidu_parse_resume import *

from gpt_analyze_resume import *


import sys
import concurrent.futures


# 假定您提供的简历路径
resume_path = r"E:\study\Big_ruan\code\my_code\test_resume\1.docx"

# 假定第一个参数是简历路径
# resume_path = sys.argv[1]

# 从简历路径获取基本路径
base_path = os.path.dirname(resume_path)
print("基础路径：",base_path)

# 调用函数以设置环境
setup_resume_analysis_folders_and_files(base_path)
print("设置环境完成")

convert_resume_to_txt(resume_path)
print("转换完成")

# baidu_parse_resume(resume_path)
# print("百度解析基本信息完成")

# # # 转换简历为文本格式，并获取转换后的文本文件路径
resume_txt_path = convert_resume_to_txt_for_gpt_analysis(resume_path)
print(resume_txt_path)





# gpt_analyze_resume(resume_txt_path, "Basic_Resume_Analysis")
# gpt_analyze_resume(resume_txt_path, "Job_Matching_Guide")

# gpt_analyze_resume(resume_txt_path, "Talent_Portrait_Guide")



# 使用 concurrent.futures 并行运行三行代码
# resume_txt_path = "your_resume_txt_path_here"
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