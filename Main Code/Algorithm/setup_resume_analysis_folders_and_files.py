import os



# 用于检查和创建所有必要的目录和文件
def setup_resume_analysis_folders_and_files(base_path):
    # 确定上级目录，以检查是否需要创建原始简历文件夹
    parent_directory = os.path.dirname(base_path)

    # 需要创建的目录名称，排除了原始简历文件夹
    # directories = [
    #     "Text_Conversions", 
    #     "Analysis_Results/Baidu_Analysis", 
    #     "Analysis_Results/GPT_Talent_Portraits", 
    #     "Analysis_Results/GPT_Job_Matching", 
    #     "Analysis_Results/Prompt_Texts"
    # ]


    # 加上图片形式的简历
    directories = [
        "Conversions/Text_Conversions", 
        "Conversions/Image_Conversions", 
        "Analysis_Results/Baidu_Analysis", 
        "Analysis_Results/GPT_Talent_Portraits", 
        "Analysis_Results/GPT_Job_Matching", 
        "Analysis_Results/Prompt_Texts"
    ]

    # 检查并创建目录
    for directory in directories:
        # 创建完整路径
        full_path = os.path.join(parent_directory, directory)
        # 检查目录是否存在
        if not os.path.exists(full_path):
            # 如果不存在，创建目录
            os.makedirs(full_path)
            print(f"已创建目录: {full_path}")
        else:
            print(f"目录已存在: {full_path}")

    # 检查并创建Prompt_Texts文件夹下的文件
    prompt_texts_path = os.path.join(parent_directory, "Analysis_Results", "Prompt_Texts")
    os.makedirs(prompt_texts_path, exist_ok=True)
    
    # 定义两个prompt的内容和文件名
    prompts = {
        "Job_Matching_Guide.txt": """
求职者匹配度评估指南

根据求职者的简历信息，对以下六个职位的要求进行详细匹配分析。为每个职位分配一个0到100的分数，以表示求职者的匹配程度。请提供明确的匹配理由，如果求职者不满足特定职位要求，分数应为0，并在匹配理由中详细说明。完成评估后，给出我一个json格式的回答来记录所有评分和理由。

职位及其要求如下：

1. 产品运营/电商运营
   - 要求至少2年的运营经验，电商背景优先。
   - 必须具备数据分析和项目管理能力。
   - 需要有自我驱动力，逻辑思维清晰，以及强沟通能力。

2. 设计与创意（平面设计师）
   - 至少大专学历，1-2年相关工作经验。
   - 需要熟练掌握设计软件和视频剪辑。
   - 需要具备创新思维，良好的沟通能力和责任感。

3. 财务/风控专员
   - 本科及以上学历，金融或相关专业。
   - 必须了解财务、会计、税收政策知识，具备风控经验。
   - 需要具有数据分析能力和EXCEL的熟练操作。

4. 市场营销/项目主管
   - 本科及以上学历，至少3年相关经验。
   - 熟练使用办公软件，能够管理客户关系。
   - 需要有市场策划和拓展经验，以及良好的执行力。

5. 技术开发（开发工程师）
   - 本科及以上学历，计算机相关专业。
   - 至少3年的软件开发经验。
   - 需要熟练使用JAVA，有APP开发经验。

6. 行政支持（文员/人力资源管理）
   - 大专及以上学历，至少1年相关经验。
   - 熟练使用办公软件，具备日常管理和档案处理能力。
   - 需要了解人力资源流程和法规，具有全面的沟通能力。

JSON文件格式如下：

```json
{
  "产品运营/电商运营": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  },
  "设计与创意": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  },
  "财务/风控专员": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  },
  "市场营销/项目主管": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  },
  "技术开发": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  },
  "行政支持": {
    "人岗匹配程度分数": 0,
    "人岗匹配的理由": ""
  }
}
```

请基于此指南和格式，结合求职者的简历信息，完成评估并生成

要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答
""",
        "Talent_Portrait_Guide.txt": """
你是一个人力资源专员。您需要根据提供的简历内容，综合评价求职者的个人特性、技能和经验、成就和亮点。请您详细分析简历中的每一项内容，并依据以下指标和要求，给出您的评分和评价理由。评分系统是1到10分，1分代表非常弱，10分代表非常强。每项评分后需要附上具体的理由，这些理由必须是基于简历中的具体信息。完成评价后，请将结果整理成JSON格式输出。

个人特性评价:
- 自我驱动性: 根据求职者展示的独立性和主动性来评分。请找出简历中反映这一特质的具体例子，并基于这些例子给出分数和理由。
- 适应能力: 根据求职者适应新环境和新挑战的能力来评分。请提供简历中反映这一能力的具体情况，并据此给出分数和理由。
- 社交能力: 根据求职者与人交往的能力来评分。请指出简历中体现这一能力的实例，并给出分数和理由。

技能和经验评价:
- 问题解决能力: 根据求职者解决问题的能力来评分。请指出简历中体现这一技能的具体案例，并基于案例给出分数和理由。
- 团队协作能力: 根据求职者在团队中的表现和协作能力来评分。请找出简历中的相关经历，并据此给出分数和理由。
- 创新思维: 根据求职者展示的创意和创新能力来评分。请提供简历中的具体例证，并基于这些例证给出分数和理由。

成就和亮点评价:
- 领导潜力: 根据求职者的领导经验和潜力来评分。请提供简历中的具体例子，如领导角色、成功的项目等，并给出分数和理由。
- 创新成果: 根据求职者在创新方面的成果来评分。请提供求职者获得的奖项、认可或创新项目的详情，并据此给出分数和理由。
- 行业影响力: 根据求职者在其专业领域的影响力和贡献来评分。请指出简历中的相关成就和经历，并给出分数和理由。

请确保您的评价准确、具体，并且完全基于简历中的信息。最后，请使用以下JSON格式输出您的评价结果：

```json
{
  "个人特性": {
    "自我驱动性": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "适应能力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "社交能力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    }
  },
  "技能和经验": {
    "问题解决能力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "团队协

作能力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "创新思维": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    }
  },
  "成就和亮点": {
    "领导潜力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "创新成果": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    },
    "行业影响力": {
      "分数": "[在此处输入分数]",
      "原因": "[在此处输入评价理由]"
    }
  }
}
```

请根据简历中的实际内容替换"[在此处输入分数]"和"[在此处输入评价理由]"，以生成最终的评价报告。


要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答
""",
        "Basic_Resume_Analysis.txt": """
专业简历分析与JSON格式化输出指南

目标：使用以下指南详细分析求职者的简历，并创建一个详尽的JSON文件，以便用于人力资源管理和职位匹配分析。

步骤：

1. 收集个人基本信息：
   - 姓名：完整的名字，以便与其他个人信息匹配。
   - 年龄：从出生年份推算，确保遵循给定的计算规则。
   - 性别：明确标记，以便了解性别多样性。
   - 联系方式：包括个人邮箱和手机号，确保联系信息的准确性。

2. 评估教育背景：
   - 最高学历：记录学历和授予学位的详细信息。
   - 毕业院校：列出学校的全称和学校等级（例如，985、211等）。
   - 专业：详细记录所学专业以评估与岗位相关性。

3. 详细工作经历：
   - 排除与教育背景重叠的工作经历。
   - 对于每份工作，记录工作地点、开始时间、结束时间、职位和具体任务。
   - 如果存在时间重叠，优先选择时间较长的经历。

4. 计算工作年限：
   - 确定每段工作经历的持续时间，将月份向上取整。
   - 累加所有全职经历的时间，确保准确无误。

5. 技能、证书和荣誉：
   - 列出所有技能证书和获奖荣誉，评估其与岗位的相关性。

6. 个人特点标签
   - 用3到5个，不超过五个字的标签描述求职者的特点
   - 每个标签必须是独立的字符串
   - 标签示例：["积极主动", "善于沟通", "抗压能力强"]

7. 工作稳定性评估：
   - 根据工作经历的持续时间和频率，评估工作稳定性的程度(选择"高"、"中上"、"中"、"中下"、"低")。
   - 提供至少100字的评价，构成工作稳定性判断的理由。

8. 生成JSON格式的回答：
   - 使用提取的信息，遵循给定的JSON结构进行格式化输出内容。
   - 确保所有字段都严格对应简历中的信息。

要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答
要求一定一定一定要直接输出json格式的回答

JSON输出格式模板：

```json
{
  "个人信息": {
    "姓名": "<完整姓名>",
    "年龄": "<计算后的年龄>",
    "性别": "<性别>",
    "电子邮箱": "<邮箱地址>",
    "电话": "<电话号码>"
  },
  "教育背景": {
    "最高学历": "<学位>",
    "毕业院校": "<院校名称>",
    "院校等级": "<985/211/其他>",
    "专业": "<专业领域>"
  },
  "工作经历": [
    {
      "工作地点": "<工作地点>",
      "开始时间": "<开始日期>",
      "结束时间": "<结束日期或'至今'>",
      "职位": "<职位名称>",
      "职责": "<主要职责>"
    }
    // 其他工作经历
  ],
  "工作年限": "<工作总年数>",
  "求职意向": "<期望职位>",
  "技能和证书": [
    "<技能/证书1>",
    "<技能/证书2>"
    // 其他技能和证书
  ],
  "获奖和荣誉": [
    "<奖项/荣誉1>",
    "<奖项/荣誉2>"
    // 其他奖项和荣誉
  ],
  "自我评价": "<自我描述>",
  "个人特点标签": [
    "<特点标签1>",
    "<特点标签2>",
    "<特点标签3>"
    // 最多5个标签
  ]
  "工作稳定性": {
    "等级": "<稳定性等级> (选择"高"、"中上"、"中"、"中下"、"低" 其中一个)",
    "原因": "<详细原因>"
  },
}
```
一定一定一定要直接输出json格式的回答
"""
    }

    # 检查并创建文件
    for file_name, content in prompts.items():
        file_path = os.path.join(prompt_texts_path, file_name)
        if not os.path.exists(file_path):
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
                print(f"文件 '{file_name}' 已创建.")
        else:
            print(f"文件 '{file_name}' 已存在.")






