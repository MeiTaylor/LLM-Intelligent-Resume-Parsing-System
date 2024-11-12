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

        # 如果是Basic_Resume_Analysis，则自动修正大学等级
        if output_json_dir == "Basic_Resume_Analysis":
            correct_university_level(json_file_path)

        print(f"JSON输出已保存到 {json_file_path}")
    else:
        print("重试次数已用尽，仍未找到JSON响应。")
    


    
    # 输出答案
    sys.stdout.write(answer + "\n")
    sys.stdout.flush()

    # 打印分析所花费的时间
    print(f"分析耗时 {time.time() - start_time} 秒")














def check_university_level(university_name):
    """
    检查大学是否为985或211院校，并返回相应的等级
    
    Args:
        university_name (str): 大学名称
        
    Returns:
        str: 大学等级 ('985', '211' 或 '其他')
    """
    universities_json = """
    {
        "universities": {
            "985": [
                "清华大学", "北京大学", "中国科学技术大学", "复旦大学", "中国人民大学", 
                "上海交通大学", "南京大学", "同济大学", "浙江大学", "南开大学",
                "北京航空航天大学", "北京师范大学", "武汉大学", "西安交通大学", "天津大学",
                "华中科技大学", "北京理工大学", "东南大学", "中山大学", "华东师范大学",
                "哈尔滨工业大学", "厦门大学", "西北工业大学", "中南大学", "大连理工大学",
                "四川大学", "电子科技大学", "华南理工大学", "吉林大学", "湖南大学",
                "重庆大学", "山东大学", "中国农业大学", "中国海洋大学", "中央民族大学",
                "东北大学", "兰州大学", "西北农林科技大学", "国防科技大学"
            ],
            "211": [
                "上海财经大学", "中央财经大学", "对外经济贸易大学", "北京外国语大学", 
                "中国政法大学", "北京邮电大学", "上海外国语大学", "西南财经大学", 
                "中国传媒大学", "中南财经政法大学", "南京航空航天大学", "北京科技大学", 
                "北京交通大学", "华东理工大学", "西安电子科技大学", "天津医科大学", 
                "南京理工大学", "华中师范大学", "哈尔滨工程大学", "华北电力大学",
                "北京中医药大学", "暨南大学", "苏州大学", "武汉理工大学", 
                "中国药科大学", "东华大学", "河海大学", "北京林业大学",
                "河北工业大学", "北京工业大学", "江南大学", "北京化工大学", 
                "西南交通大学", "上海大学", "南京师范大学", "中国地质大学(武汉)", 
                "中国地质大学(北京)", "西北大学", "东北师范大学", "长安大学",
                "中国矿业大学(北京)", "华中农业大学", "合肥工业大学", "广西大学",
                "中国石油大学(华东)", "陕西师范大学", "南京农业大学", "湖南师范大学",
                "福州大学", "大连海事大学", "西南大学", "中国矿业大学", "云南大学",
                "太原理工大学", "华南师范大学", "北京体育大学", "中国石油大学(北京)",
                "安徽大学", "东北林业大学", "东北农业大学", "辽宁大学", "南昌大学",
                "延边大学", "内蒙古大学", "四川农业大学", "海南大学", "贵州大学",
                "郑州大学", "新疆大学", "宁夏大学", "石河子大学", "青海大学",
                "国防科技大学", "中央音乐学院", "第二军医大学", "第四军医大学", "西藏大学"
            ]
        }
    }
    """
    
    universities = json.loads(universities_json)
    
    if university_name in universities['universities']['985']:
        return "985"
    elif university_name in universities['universities']['211'] or university_name in universities['universities']['985']:
        return "211"
    else:
        return "其他"

def correct_university_level(json_file_path):
    """
    修正JSON文件中的大学等级信息
    
    Args:
        json_file_path (str): JSON文件的路径
    """
    try:
        # 读取JSON文件
        with open(json_file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        
        # 获取大学名称
        university_name = data['教育背景']['毕业院校']
        
        # 检查大学等级
        university_level = check_university_level(university_name)
        
        # 更新大学等级
        data['教育背景']['院校等级'] = university_level
        
        # 保存修改后的JSON文件
        with open(json_file_path, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
            
        print(f"已更新 {university_name} 的等级为: {university_level}")
        
    except Exception as e:
        print(f"处理文件时出错: {str(e)}")









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

