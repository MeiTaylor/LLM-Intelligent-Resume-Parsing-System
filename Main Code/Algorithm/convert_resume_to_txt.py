
import os
import openai
import re  # 导入正则表达式模块
import sys
import time
import json
import docx2txt
import sys
import codecs

import shutil




import os

def convert_resume_to_txt(input_resume_path):
    """
    将简历文件转换为文本格式并保存到 'Conversions/Text_Conversions' 目录中。
    
    参数:
    - input_resume_path: 简历文件的路径。
    """
    # 基础目录是简历目录的上一级
    base_directory = os.path.dirname(os.path.dirname(input_resume_path))

    # 定义文本转换的输出目录
    text_conversions_directory = os.path.join(base_directory, "Conversions/Text_Conversions")
    image_conversions_directory = os.path.join(base_directory, "Conversions/Image_Conversions")

    # 确保文本转换目录存在
    os.makedirs(text_conversions_directory, exist_ok=True)

    # 准备输出文本文件路径
    file_name = os.path.basename(input_resume_path)
    output_txt_path = os.path.join(text_conversions_directory, os.path.splitext(file_name)[0] + ".txt")

    # 确定输入文件的类型
    file_extension = os.path.splitext(input_resume_path)[-1].lower()

    # 根据文件类型调用相应的转换函数
    if file_extension == '.docx':
        docx_to_txt(input_resume_path, output_txt_path)
        print("DOCX 文件已成功转换为文本。")
        
        convert_docx_to_image(input_resume_path, image_conversions_directory)

    elif file_extension == '.pdf':
        pdf_to_txt(input_resume_path, output_txt_path)
        print("PDF 文件已成功转换为文本。")

        convert_pdf_to_image(input_resume_path, image_conversions_directory)

    elif file_extension in ['.jpg', '.jpeg', '.png', '.bmp', '.tiff']:
        img_to_txt(input_resume_path, output_txt_path)
        print("图像文件已成功转换为文本。")
        convert_image_to_jpg(input_resume_path, image_conversions_directory)


    elif file_extension == '.doc':
        doc_to_txt(input_resume_path, output_txt_path)
        print("DOC 文件已成功转换为文本。")

    else:
        raise ValueError("不支持的文件类型进行转换。")
    
    # image_conv_result = convert_docx_to_jpg_long(input_resume_path, image_conversions_directory)


    # if image_conv_result:
    #     print(f"转换成功！图片路径: {image_conv_result}")
    # else:
    #     print("转换失败！")




def convert_resume_to_txt_for_gpt_analysis(resume_path, text_conversions_dir="Conversions/Text_Conversions"):
    # 获取文件扩展名
    _, file_extension = os.path.splitext(resume_path)
    file_extension = file_extension.lower()

    # 构造文本文件的输出路径
    resume_filename_without_extension = os.path.basename(resume_path).split('.')[0]
    
    # 更新这行代码，使其构造在上级目录的 Conversions/Text_Conversions 下的路径
    resume_txt_path = os.path.join(os.path.dirname(os.path.dirname(resume_path)), text_conversions_dir, resume_filename_without_extension + ".txt")

    # 确保文本转换目录存在
    # os.makedirs(os.path.join(os.path.dirname(resume_path), text_conversions_dir), exist_ok=True)

    # # 根据文件扩展名，调用相应的转换函数
    # if file_extension == '.docx':
    #     docx_to_txt(resume_path, resume_txt_path)
    # elif file_extension == '.pdf':
    #     pdf_to_txt(resume_path, resume_txt_path)
    # elif file_extension == '.doc':
    #     doc_to_txt(resume_path, resume_txt_path)
    # # 如果是图片格式
    # elif file_extension in ['.jpg', '.jpeg', '.png', '.bmp', '.tiff']:
    #     img_to_txt(resume_path, resume_txt_path)
    # else:
    #     raise ValueError(f"Unsupported file type: {file_extension}")

    return resume_txt_path







def docx_to_txt(input_docx_path, output_txt_path):
    # 使用docx2txt库读取docx文件内容
    text = docx2txt.process(input_docx_path)

    # 删除重复行
    lines = text.split('\n')
    unique_lines = []
    for line in lines:
        if line not in unique_lines:
            unique_lines.append(line)
    text = '\n'.join(unique_lines)

    # 将读取到的问题内容写入txt文件
    with open(output_txt_path, 'w', encoding='utf-8') as file:
        file.write(text)





'''
pip install pdfminer.six
'''

# 导入所需的库

from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage


# from pdfminer.pdfparser import PDFParser, PDFDocument
from pdfminer.pdfdevice import PDFDevice
from pdfminer.pdfinterp import PDFResourceManager, PDFPageInterpreter
from pdfminer.converter import PDFPageAggregator
from pdfminer.layout import LTTextBoxHorizontal, LAParams
# from pdfminer.pdfinterp import PDFTextExtractionNotAllowed
from pdfminer.pdfpage import PDFTextExtractionNotAllowed


def pdf_to_txt(input_pdf_path, output_txt_path):
    open(output_txt_path, 'w').close()  # 清空txt文件
    
    def parse(input_pdf_file, output_txt_file):
        # 用文件对象创建一个PDF文档分析器
        parser = PDFParser(input_pdf_file)
        # 创建一个PDF文档并传入parser
        doc = PDFDocument(parser)
        
        # 检查文档是否可以转成TXT，如果不可以就忽略
        if not doc.is_extractable:
            raise PDFTextExtractionNotAllowed
        else:
            # 创建PDF资源管理器，来管理共享资源
            rsrcmgr = PDFResourceManager()
            # 创建一个PDF设备对象
            laparams = LAParams()
            # 将资源管理器和设备对象聚合
            device = PDFPageAggregator(rsrcmgr, laparams=laparams)
            # 创建一个PDF解释器对象
            interpreter = PDFPageInterpreter(rsrcmgr, device)
            
            # 循环遍历列表，每次处理一个page内容
            for page in PDFPage.create_pages(doc):
                interpreter.process_page(page)
                # 接收该页面的LTPage对象
                layout = device.get_result()
                
                for x in layout:
                    try:
                        if isinstance(x, LTTextBoxHorizontal):
                            with open(output_txt_file, 'a', encoding='utf-8-sig') as f:
                                result = x.get_text()
                                # 删除任何前导/尾随的空格
                                result = result.strip()
                                # 如果行不为空，则写入文件
                                if result != '':
                                    f.write(result + "\n")
                    except:
                        print("Failed")
                        
    # 打开并处理PDF文件
    with open(input_pdf_path, 'rb') as pdf_file:
        parse(pdf_file, output_txt_path)








        


import requests
import base64
import json

API_KEY = "mELvNVjiTmifXWZcemfCze99"  # 填入你的API_KEY
SECRET_KEY = "4K4rxBpqjHlGmUOsAPXNI00E06cRFxMm"  # 填入你的SECRET_KEY

def get_access_token():
    """
    使用 AK，SK 生成鉴权签名（Access Token）
    :return: access_token，或是None(如果错误)
    """
    url = "https://aip.baidubce.com/oauth/2.0/token"
    params = {
        "grant_type": "client_credentials",
        "client_id": API_KEY,
        "client_secret": SECRET_KEY
    }
    try:
        response = requests.post(url, params=params)
        if response.status_code == 200:
            return str(response.json().get("access_token"))
        else:
            print(f"获取access_token失败: {response.text}")
            return None
    except Exception as e:
        print(f"获取access_token异常: {str(e)}")
        return None

def img_to_txt(input_img_path, output_txt_path):
    """
    将图片转换为文本并保存到指定文件
    :param input_img_path: 输入图片路径
    :param output_txt_path: 输出文本路径
    :return: 是否转换成功
    """
    print(f"正在从图片转化为txt")
    print(f"输入图片路径: {input_img_path}")
    print(f"输出文本路径: {output_txt_path}")
    
    # 获取access_token
    access_token = get_access_token()
    if not access_token:
        print("获取access_token失败")
        return False
    
    # 读取图片文件
    try:
        with open(input_img_path, 'rb') as f:
            image_data = base64.b64encode(f.read())
    except Exception as e:
        print(f"读取图片文件失败: {str(e)}")
        return False
    
    # 构建请求参数
    url = f"https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token={access_token}"
    
    payload = {
        'image': image_data,
        'detect_direction': 'false',
        'paragraph': 'false',
        'probability': 'false',
        'multidirectional_recognize': 'false'
    }
    
    headers = {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Accept': 'application/json'
    }
    
    try:
        # 发送OCR请求
        response = requests.post(url, headers=headers, data=payload)
        
        if response.status_code != 200:
            print(f"OCR识别请求失败: {response.text}")
            return False
        
        # 解析响应结果
        result = response.json()
        if 'error_code' in result:
            print(f"OCR识别错误: {result}")
            return False
        
        # 提取文本内容
        words_result = result.get('words_result', [])
        text_content = '\n'.join([item['words'] for item in words_result])
        
        # 保存到文件
        with open(output_txt_path, 'w', encoding='utf-8') as f:
            f.write(text_content)
        
        print(f"文字识别完成，已保存到: {output_txt_path}")
        return True
        
    except Exception as e:
        print(f"OCR识别过程发生异常: {str(e)}")
        return False





import os
import win32com.client as win32
from docx import Document

import os
import tempfile

def doc_to_txt(input_doc_path, output_txt_path):
    # 生成临时的图片路径
    output_image_path = tempfile.mktemp(suffix='.png')

    # 将.doc文档转为图片
    convert_doc_to_image(input_doc_path, output_image_path)

    # 将图片转为.txt文件
    img_to_txt(output_image_path, output_txt_path)

    # 删除临时的图片文件
    os.remove(output_image_path)

    return output_txt_path











def convert_docx_to_image(docx_path, output_folder):
    """
    将Word文档转换为图片
    
    Args:
        docx_path (str): Word文档的完整路径
        output_folder (str): 输出图片的文件夹路径
    
    Returns:
        str: 生成的图片路径
    """
    try:
        # 验证输入路径
        if not os.path.exists(docx_path):
            raise FileNotFoundError(f"Word文件不存在: {docx_path}")
        
        # 确保输出文件夹存在
        os.makedirs(output_folder, exist_ok=True)
        
        # 获取文件名（不含扩展名）
        base_name = os.path.splitext(os.path.basename(docx_path))[0]
        
        # 创建完整的输出图片路径
        image_path = os.path.join(output_folder, f"{base_name}.jpg")
        
        # 创建临时文件
        temp_word_path = tempfile.mktemp(suffix='.docx')
        temp_pdf_path = tempfile.mktemp(suffix='.pdf')
        
        try:
            # 复制Word文档到临时位置
            shutil.copy2(docx_path, temp_word_path)
            
            # 转换为PDF
            print("正在转换为PDF...")
            convert(temp_word_path, temp_pdf_path)
            
            # 使用PyMuPDF转换为图像
            print("正在转换为图像...")
            pdf_document = fitz.open(temp_pdf_path)
            images = []
            
            for page_num in range(pdf_document.page_count):
                page = pdf_document[page_num]
                zoom_x = 1.33333333  # 增加分辨率
                zoom_y = 1.33333333
                mat = fitz.Matrix(zoom_x, zoom_y)
                pix = page.get_pixmap(matrix=mat, alpha=False)
                
                # 转换为PIL图像
                img = Image.frombytes("RGB", [pix.width, pix.height], pix.samples)
                images.append(img)
            
            # 关闭PDF文档
            pdf_document.close()
            
            if images:
                # 计算总高度和最大宽度
                widths, heights = zip(*(i.size for i in images))
                max_width = max(widths)
                total_height = sum(heights)
                
                # 创建新图像
                final_image = Image.new('RGB', (max_width, total_height))
                
                # 拼接所有页面
                y_offset = 0
                for img in images:
                    final_image.paste(img, (0, y_offset))
                    y_offset += img.height
                
                # 保存最终图像
                final_image.save(image_path, 'JPEG', quality=95)
                print(f"图像已保存至: {image_path}")
                
                return image_path
            else:
                raise Exception("没有页面被转换")
            
        finally:
            # 清理临时文件
            for temp_file in [temp_word_path, temp_pdf_path]:
                if os.path.exists(temp_file):
                    os.remove(temp_file)
                    print(f"已删除临时文件: {temp_file}")
    
    except Exception as e:
        print(f"转换过程中出错: {str(e)}")
        raise







import os
from PIL import Image
import fitz

def convert_pdf_to_image(input_resume_path: str, output_folder: str) -> bool:
    """
    将PDF文件转换为JPG图片并保存在指定文件夹中
    
    Args:
        input_resume_path (str): 输入PDF文件的路径
        output_folder (str): 输出图片的文件夹路径
        
    Returns:
        bool: 转换成功返回True，失败返回False
    """
    try:
        # 确保输出文件夹存在
        os.makedirs(output_folder, exist_ok=True)
        
        # 获取PDF文件名（不含扩展名）并添加jpg扩展名
        pdf_name = os.path.splitext(os.path.basename(input_resume_path))[0]
        output_image_path = os.path.join(output_folder, f"{pdf_name}.jpg")
        
        # 打开PDF文件
        pdf_doc = fitz.open(input_resume_path)
        
        try:
            # 将PDF转换为图像
            images = []
            for page_num in range(pdf_doc.page_count):
                page = pdf_doc[page_num]
                # 设置缩放因子以提高图像质量
                zoom_x = 2.0
                zoom_y = 2.0
                mat = fitz.Matrix(zoom_x, zoom_y)
                
                # 获取页面的像素图
                pix = page.get_pixmap(matrix=mat, alpha=False)
                
                # 转换为PIL Image，确保是RGB模式
                img = Image.frombytes("RGB", [pix.width, pix.height], pix.samples)
                images.append(img)
                
            # 如果PDF只有一页
            if len(images) == 1:
                # 直接保存为JPG
                images[0].save(output_image_path, 'JPEG', quality=95, optimize=True)
            else:
                # 合并多页图像
                widths, heights = zip(*(i.size for i in images))
                total_width = max(widths)
                total_height = sum(heights)
                
                # 创建新的合并图像（使用RGB模式）
                merged_image = Image.new('RGB', (total_width, total_height), 'white')
                
                # 粘贴所有图像
                y_offset = 0
                for img in images:
                    merged_image.paste(img, (0, y_offset))
                    y_offset += img.height
                
                # 保存为JPG格式
                merged_image.save(
                    output_image_path, 
                    'JPEG', 
                    quality=95,  # 高质量
                    optimize=True,  # 优化文件大小
                    subsampling=0  # 最好的色度子采样
                )
            
            print(f"PDF已成功转换为JPG: {output_image_path}")
            return True
            
        finally:
            # 确保PDF文档被关闭
            pdf_doc.close()
            
    except FileNotFoundError:
        print(f"错误：找不到文件 {input_resume_path}")
        return False
    except PermissionError:
        print(f"错误：没有权限访问文件或文件夹")
        return False
    except Image.DecompressionBombError:
        print("错误：图像尺寸过大，可能会消耗过多内存")
        return False
    except Exception as e:
        print(f"转换过程中出错: {str(e)}")
        return False










import os
import tempfile
import win32com.client as win32
from docx import Document
from docx2pdf import convert
from PIL import Image
import fitz

def convert_doc_to_image(input_doc_path, output_image_path):
    # 初始化 Word 对象
    word = win32.gencache.EnsureDispatch('Word.Application')

    # 隐藏 Word 程序窗口
    word.Visible = False

    # 打开 .doc 文件
    doc = word.Documents.Open(input_doc_path)

    # 将 .doc 文件另存为 .docx 文件
    docx_path = os.path.splitext(input_doc_path)[0] + ".docx"
    doc.SaveAs(docx_path, FileFormat=16)  # 16 表示 .docx 文件格式

    # 关闭原始 .doc 文件
    doc.Close()

    # 将 .docx 文件转换为 PDF
    pdf_path = tempfile.mktemp(suffix='.pdf')
    convert(docx_path, pdf_path)

    # 将 PDF 转换为图像
    pdfDoc = fitz.open(pdf_path)
    images = []
    for pg in range(pdfDoc.page_count):
        page = pdfDoc[pg]
        zoom_x = 1.33333333
        zoom_y = 1.33333333
        mat = fitz.Matrix(zoom_x, zoom_y)
        pix = page.get_pixmap(matrix=mat, alpha=False)
        mode = "RGBA" if pix.alpha else "RGB"
        img = Image.frombytes(mode, [pix.width, pix.height], pix.samples)
        images.append(img)

    # 关闭 PyMuPDF 对 PDF 文件的引用
    pdfDoc.close()

    # 清理生成的 PDF 文件
    os.remove(pdf_path)

    # 合并所有的图像
    widths, heights = zip(*(i.size for i in images))
    total_width = max(widths)
    total_height = sum(heights)

    new_img = Image.new('RGB', (total_width, total_height))

    y_offset = 0
    for img in images:
        new_img.paste(img, (0, y_offset))
        y_offset += img.height

    # 保存图像
    new_img.save(output_image_path)


















import os
from win32com import client
from pdf2image import convert_from_path
import pythoncom
from PIL import Image
import numpy as np

def convert_docx_to_jpg_long(docx_path, output_folder):
    """
    将docx文件转换为单张长图片
    
    Args:
        docx_path (str): docx文件的完整路径
        output_folder (str): 输出图片的文件夹路径
        
    Returns:
        str: 成功返回图片路径，失败返回None
    """
    try:
        # 确保输入路径存在
        if not os.path.exists(docx_path):
            raise FileNotFoundError(f"Word文件不存在: {docx_path}")
            
        # 确保输出文件夹存在，如果不存在则创建
        os.makedirs(output_folder, exist_ok=True)
            
        # 获取文件名（不含扩展名）
        file_name = os.path.splitext(os.path.basename(docx_path))[0]
        
        # 创建临时PDF文件路径
        pdf_path = os.path.join(output_folder, f"{file_name}_temp.pdf")
        
        try:
            # 初始化COM环境
            pythoncom.CoInitialize()
            
            # 转换DOCX为PDF
            print("正在将DOCX转换为PDF...")
            word = client.Dispatch("Word.Application")
            doc = word.Documents.Open(docx_path)
            doc.SaveAs(pdf_path, FileFormat=17)  # 17 represents PDF format
            doc.Close()
            word.Quit()
            
            # 转换PDF为图片
            print("正在将PDF转换为图片...")
            images = convert_from_path(pdf_path)
            
            if not images:
                raise Exception("PDF转换失败，未生成图片")
                
            # 计算总高度和获取最大宽度
            total_height = sum(img.size[1] for img in images)
            max_width = max(img.size[0] for img in images)
            
            # 创建新的长图
            long_image = Image.new('RGB', (max_width, total_height), 'white')
            
            # 垂直拼接图片
            current_height = 0
            for img in images:
                # 确保每个图片宽度一致
                if img.size[0] != max_width:
                    img = img.resize((max_width, int(img.size[1] * max_width / img.size[0])))
                
                long_image.paste(img, (0, current_height))
                current_height += img.size[1]
            
            # 保存最终的长图
            jpg_path = os.path.join(output_folder, f"{file_name}.jpg")
            long_image.save(jpg_path, 'JPEG', quality=95)
            print(f"已保存长图: {jpg_path}")
            
            # 删除临时PDF文件
            os.remove(pdf_path)
            
            return jpg_path
            
        except Exception as e:
            raise Exception(f"转换过程出错: {str(e)}")
            
        finally:
            # 清理COM环境
            pythoncom.CoUninitialize()
            
    except Exception as e:
        print(f"转换文件时出错: {str(e)}")
        return None











from PIL import Image
import os
import shutil

def convert_image_to_jpg(input_image_path: str, output_folder: str) -> bool:
    """
    将图片格式的简历转换/复制为JPG格式并保存到指定路径
    
    Args:
        input_image_path (str): 输入图片文件的路径
        output_folder (str): 输出文件夹路径
        
    Returns:
        bool: 转换成功返回True，失败返回False
    """
    try:
        # 确保输出文件夹存在
        os.makedirs(output_folder, exist_ok=True)
        
        # 获取原文件名（不含扩展名）
        file_name = os.path.splitext(os.path.basename(input_image_path))[0]
        output_path = os.path.join(output_folder, f"{file_name}.jpg")
        
        # 如果输入文件已经是jpg格式
        if input_image_path.lower().endswith(('.jpg', '.jpeg')):
            # 直接复制文件
            shutil.copy2(input_image_path, output_path)
            print(f"文件已复制到: {output_path}")
            return True
            
        # 对于其他格式的图片，转换为jpg
        try:
            # 打开图片
            with Image.open(input_image_path) as img:
                # 如果图片不是RGB模式，转换为RGB
                if img.mode != 'RGB':
                    img = img.convert('RGB')
                
                # 保存为jpg格式
                img.save(
                    output_path,
                    'JPEG',
                    quality=95,  # 高质量
                    optimize=True,  # 优化文件大小
                    subsampling=0  # 最佳的色度子采样
                )
                
            print(f"图片已转换并保存到: {output_path}")
            return True
            
        except Image.DecompressionBombError:
            print("错误：图片尺寸过大，可能会消耗过多内存")
            return False
            
    except FileNotFoundError:
        print(f"错误：找不到文件 {input_image_path}")
        return False
    except PermissionError:
        print(f"错误：没有权限访问文件或文件夹")
        return False
    except Exception as e:
        print(f"处理过程中出错: {str(e)}")
        return False
