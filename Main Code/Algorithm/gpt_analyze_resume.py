import os
import openai
import random
import time
import codecs
import sys
import re
import json
import regex

def extract_complete_json(response_text):
    # 使用正则表达式模式匹配嵌套的JSON结构，使用`regex`模块
    json_pattern = r'(\{(?:[^{}]|(?1))*\})'
    matches = regex.findall(json_pattern, response_text)
    if matches:
        try:
            # 尝试解析每个匹配项以找到第一个有效的JSON
            for match in matches:
                json_data = json.loads(match)
                # 返回第一个有效的JSON数据
                return json_data
        except json.JSONDecodeError as e:
            print(f"JSON解析错误: {e}")
    return None





def llm_call(messages):
    import requests
    import json
    
    url = "https://cn2us02.opapi.win/v1/chat/completions"
    
    payload = json.dumps({
        "model": "gpt-4o-mini",
        "messages": messages
    })

    key = "sk-HPl4EATP60B3E73fb251T3BLbkFJ02E42fB642Ab4748BBd9"
    
    headers = {
        'User-Agent': 'Apifox/1.0.0 (https://apifox.com)',
        'Content-Type': 'application/json',
        'Authorization': f'Bearer {key}'
    }
    
    response = requests.request("POST", url, headers=headers, data=payload)
    # 将响应转换为Python字典
    response_dict = json.loads(response.text)
    # 只返回content内容
    return response_dict['choices'][0]['message']['content']









# 有重试机制的版本
def gpt_analyze_resume(resume_txt_path, prompt_filename):
    # 记录分析开始时间
    start_time = time.time()

    # 假设resume_txt_path位于'Text_Conversions'目录中
    # 我们需要向上跳两级以获取基本目录
    base_dir = os.path.dirname(os.path.dirname(resume_txt_path))
    # 在此基础上再往上跳一级
    base_dir = os.path.dirname(base_dir)

    
    # 构建提示文本的路径
    # 它位于'Analysis_Results/Prompt_Texts'中
    prompt_txt_path = os.path.join(base_dir, "Analysis_Results", "Prompt_Texts", f'{prompt_filename}.txt')

    # 根据提示文本确定JSON的输出目录
    if 'Basic_Resume_Analysis' in prompt_filename:
        output_json_dir = "Basic_Resume_Analysis"
    elif 'Talent_Portrait' in prompt_filename:
        output_json_dir = "GPT_Talent_Portraits"
    elif 'Job_Matching' in prompt_filename:
        output_json_dir = "GPT_Job_Matching"
    else:
        raise ValueError("未知的提示类型。")
    
    # 如果目录不存在，则创建输出目录
    json_output_path = os.path.join(base_dir, "Analysis_Results", output_json_dir)
    os.makedirs(json_output_path, exist_ok=True)

    # 读取简历文本
    with open(resume_txt_path, 'r', encoding='utf-8') as file:
        resume_text = file.read()

    # 读取提示文本
    with open(prompt_txt_path, 'r', encoding='utf-8') as file:
        prompt = file.read().replace('\n', '')

    # 准备用于OpenAI API的消息
    message = [
        {"role": "system", "content": prompt},
        {"role": "user", "content": f"我这里有一份简历，我需要获取其中的一些信息。简历如下：{resume_text}"}
    ]

    max_retries = 3
    retry_count = 0
    extracted_json = None

    # 尝试调用 llm_call，最多重试三次
    while retry_count < max_retries:
        # 发送请求并获取答案
        answer = llm_call(message)

        # 尝试提取响应中的JSON
        extracted_json = extract_complete_json(answer)
        
        # 如果成功提取JSON，则跳出循环
        if extracted_json is not None:
            break
        
        # 如果提取失败，则增加重试次数并输出提示
        retry_count += 1
        print(f"未找到JSON响应。正在进行第 {retry_count} 次重试...")
    
    # 如果成功提取JSON，则将其保存到文件中
    if extracted_json is not None:
        json_file_name = os.path.splitext(os.path.basename(resume_txt_path))[0] + ".json"
        json_file_path = os.path.join(json_output_path, json_file_name)
        with open(json_file_path, 'w', encoding='utf-8') as json_file:
            json.dump(extracted_json, json_file, ensure_ascii=False, indent=2)
        print(f"JSON输出已保存到 {json_file_path}")
    else:
        print("重试次数已用尽，仍未找到JSON响应。")
    
    # 输出答案
    sys.stdout.write(answer + "\n")
    sys.stdout.flush()

    # 打印分析所花费的时间
    print(f"分析耗时 {time.time() - start_time} 秒")










# 无重试机制的版本
# def gpt_analyze_resume(resume_txt_path, prompt_filename):
#     # 记录分析开始时间
#     start_time = time.time()



#     # 假设resume_txt_path位于'Text_Conversions'目录中
#     # 我们需要向上跳两级以获取基本目录
#     base_dir = os.path.dirname(os.path.dirname(resume_txt_path))
    
#     # 构建提示文本的路径
#     # 它位于'Analysis_Results/Prompt_Texts'中
#     prompt_txt_path = os.path.join(base_dir, "Analysis_Results", "Prompt_Texts", f'{prompt_filename}.txt')

#     # 根据提示文本确定JSON的输出目录
#     if 'Basic_Resume_Analysis' in prompt_filename:
#         output_json_dir = "Basic_Resume_Analysis"
#     elif 'Talent_Portrait' in prompt_filename:
#         output_json_dir = "GPT_Talent_Portraits"
#     elif 'Job_Matching' in prompt_filename:
#         output_json_dir = "GPT_Job_Matching"
#     else:
#         raise ValueError("未知的提示类型。")
    
#     # 如果目录不存在，则创建输出目录
#     json_output_path = os.path.join(base_dir, "Analysis_Results", output_json_dir)
#     os.makedirs(json_output_path, exist_ok=True)

#     # 读取简历文本
#     with open(resume_txt_path, 'r', encoding='utf-8') as file:
#         resume_text = file.read()

#     # 读取提示文本
#     with open(prompt_txt_path, 'r', encoding='utf-8') as file:
#         prompt = file.read().replace('\n', '')

#     # 准备用于OpenAI API的消息
#     message = [
#         {"role": "system", "content": prompt},
#         {"role": "user", "content": f"我这里有一份简历，我需要获取其中的一些信息。简历如下：{resume_text}"}
#     ]


   
#     # 发送请求并获取答案
#     answer = llm_call(message)

#     # 提取响应中的JSON
#     extracted_json = extract_complete_json(answer)
    
#     # 如果成功提取JSON，则将其保存到文件中
#     if extracted_json is not None:
#         json_file_name = os.path.splitext(os.path.basename(resume_txt_path))[0] + ".json"
#         json_file_path = os.path.join(json_output_path, json_file_name)
#         with open(json_file_path, 'w', encoding='utf-8') as json_file:
#             json.dump(extracted_json, json_file, ensure_ascii=False, indent=2)
#         print(f"JSON输出已保存到 {json_file_path}")
#     else:
#         print("未找到JSON响应。")
    
#     # 输出答案
#     sys.stdout.write(answer + "\n")
#     sys.stdout.flush()

#     # 打印分析所花费的时间
#     print(f"分析耗时 {time.time() - start_time} 秒")

