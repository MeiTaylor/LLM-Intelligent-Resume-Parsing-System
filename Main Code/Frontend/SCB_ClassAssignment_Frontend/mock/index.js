import { useFormDisabled } from 'element-plus'
import Mock from 'mockjs'


const users = {
  'admin-token': {
    roles: ['admin'],
    introduction: 'I am a super administrator',
    // avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    // avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
    name: 'Normal Editor'
  }
}

const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}


// 测试/Login/Login
Mock.mock('http://127.0.0.1:8392/api/Login/Login', 'post', () => {
  
  const username = 'admin'
  const token = tokens[username]

  if (!token) {
    console.log("该请求没有token验证")
    return {
      code: 60204,
      message: 'Account and password are incorrect.'
    }
  }

  return {
    code: 20000,
    data: token,
    userId: 1
  }
})

Mock.mock('http://127.0.0.1:8392/Login/GetInfo', 'post', () => {
  const username = 'admin'
  const token = tokens[username]
  const info = {
      user_type: ['admin'],
      user_info: 'I am a super administrator',
      // avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
      username: 'Super Admin'
  }

  // mock error
  if (!info) {
    return {
      code: 50008,
      message: 'Login failed, unable to get user details.'
    }
  }

  return {
    code: 20000,
    data: info
  }
})

Mock.mock('http://localhost:5168/api/ResumeView/UploadResume?userId=2', 'post', (options)=>{
  console.log(options.body)
    return {
      code: 20000,
      data: 'Fine, a file is here.'
    }
})

Mock.mock('http://127.0.0.1:8392/Resumes/Allresumes', 'post', (options)=>{
  console.log("这是获取所有简历信息",options.body)
  return{
    code: 20000,
    "simpleResumes": [
      {
        "name": '毛子昊',
        "age": 21,
        "phoneNumber": "1855615",
        "jobIntention": 'Java 后端工程师',
        "gender": '男',
        "matchingScore": 80,
        "education": '本科'
      },
      {
        "name": '牛锴鹏',
        "age": 20,
        "phoneNumber": "1145141919",
        "jobIntention": 'Java 后端工程师',
        "gender": '男',
        "matChingScore": 90,
        "education": '本科'
      },
    ]
  }
})