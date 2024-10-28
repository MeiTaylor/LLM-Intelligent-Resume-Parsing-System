import base64
import requests
import json
import hmac
import hashlib
from datetime import datetime
from urllib.parse import quote_plus
import os

def baidu_parse_resume(file_path):
    """
    给定简历文件路径，解析简历并返回JSON格式的解析响应。

    :param file_path: 要解析的简历文件的路径。
    :return: API的解析JSON响应。
    """

    # 我自己的API Key和Secret Key
    AK = '1GtWHtx95APqAhl6V94Pk6hc'
    SK = 'Y9dcM5HG7Lhfe31I07ZTGMIusrvT7YmK'

    # 确定文件类型
    _, file_extension = os.path.splitext(file_path)
    file_type = file_extension[1:].lower()  # 移除点并转换为小写

    
    # 读取简历文件并转换为Base64编码
    with open(file_path, "rb") as resume_file:
        base64_encoded_data = base64.b64encode(resume_file.read()).decode('utf-8')

    # 构建请求体
    request_body = {
        "resume": {
            "filename": file_path,
            "filetype": file_type,
            "filedata": base64_encoded_data
        }
    }

    # 请求的相关参数
    method = 'POST'
    host = 'aip.baidubce.com'
    uri = '/rpc/2.0/recruitment/v1/cvparser'

    # 生成签名的函数
    def sign(key, msg):
        if isinstance(key, str):  # 如果key是字符串，将其转换为字节串
            key = key.encode('utf-8')
        return hmac.new(key, msg.encode('utf-8'), hashlib.sha256).hexdigest()

    # 获取UTC时间并格式化
    x_bce_date = datetime.utcnow().strftime('%Y-%m-%dT%H:%M:%SZ')

    # 构建标准化HTTP请求
    canonical_request = f"{method}\n{uri}\n\nhost:{host}\nx-bce-date:{x_bce_date}"

    # 签名密钥
    signing_key = sign(SK, x_bce_date)

    # 签名字符串
    signature = sign(signing_key, canonical_request)

    # 构建授权头
    auth_header = f'bce-auth-v1/{AK}/{x_bce_date}/1800/host;x-bce-date/{signature}'

    # 获取access_token
    access_token = '24.6586db5e615f44ec08b2254211e115cd.2592000.1702001973.282335-42482618'

    # 设置请求头
    headers = {
        'Content-Type': 'application/json',
        'Authorization': auth_header
    }

    # 发送请求
    response = requests.post(f'https://{host}{uri}?access_token={access_token}', headers=headers, json=request_body)

    # 将响应转换为JSON格式
    response_json = response.json()

    # 获取简历文件名（无扩展名）
    resume_filename = os.path.splitext(os.path.basename(file_path))[0]

    # 构造JSON输出文件路径
    base_dir = os.path.dirname(os.path.dirname(file_path))
    output_json_dir = os.path.join(base_dir, "Analysis_Results", "Baidu_Analysis")
    os.makedirs(output_json_dir, exist_ok=True)  # 确保目录存在
    output_json_path = os.path.join(output_json_dir, resume_filename + ".json")

    # 保存响应到JSON文件
    with open(output_json_path, 'w', encoding='utf-8') as json_file:
        json.dump(response_json, json_file, ensure_ascii=False, indent=2)
    
    print(f"解析结果已保存到: {output_json_path}")

    # 返回JSON响应以便于可能的进一步处理
    return response_json


