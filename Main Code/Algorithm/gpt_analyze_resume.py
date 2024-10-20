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

def gpt_analyze_resume(resume_txt_path, prompt_filename):
    # 记录分析开始时间
    start_time = time.time()

    # OpenAI的API密钥列表，我们的代码中随机选择一个key
    api_keys = [
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
        "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",
    ]

    # 假设resume_txt_path位于'Text_Conversions'目录中
    # 我们需要向上跳两级以获取基本目录
    base_dir = os.path.dirname(os.path.dirname(resume_txt_path))
    
    # 构建提示文本的路径
    # 它位于'Analysis_Results/Prompt_Texts'中
    prompt_txt_path = os.path.join(base_dir, "Analysis_Results", "Prompt_Texts", f'{prompt_filename}.txt')

    # 根据提示文本确定JSON的输出目录
    if 'Talent_Portrait' in prompt_filename:
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

    # 设置API端点
    os.environ["OPENAI_API_BASE"] = "https://api.openai.com/v1/chat/completions"

    # 随机选择一个API密钥
    openai.api_key = random.choice(api_keys)

    # 发送OpenAI API请求的函数
    def openai_request(message):
        try:
            response = openai.ChatCompletion.create(
                model="gpt-3.5-turbo-1106",
                messages=message,
                temperature=0.01,
                max_tokens=3000,
                top_p=1,
                frequency_penalty=0,
                presence_penalty=0,
            )
            return response['choices'][0]['message']['content']
        except Exception as e:
            print(f"发生错误: {e}")
            return None

    # 发送请求并获取答案
    answer = openai_request(message)

    # 提取响应中的JSON
    extracted_json = extract_complete_json(answer)
    
    # 如果成功提取JSON，则将其保存到文件中
    if extracted_json is not None:
        json_file_name = os.path.splitext(os.path.basename(resume_txt_path))[0] + ".json"
        json_file_path = os.path.join(json_output_path, json_file_name)
        with open(json_file_path, 'w', encoding='utf-8') as json_file:
            json.dump(extracted_json, json_file, ensure_ascii=False, indent=2)
        print(f"JSON输出已保存到 {json_file_path}")
    else:
        print("未找到JSON响应。")
    
    # 输出答案
    sys.stdout.write(answer + "\n")
    sys.stdout.flush()

    # 打印分析所花费的时间
    print(f"分析耗时 {time.time() - start_time} 秒")


# gpt_analyze_resume(r"E:\study\C#\Algorithm\Text_Conversions\2.txt", "Job_Matching_Guide")