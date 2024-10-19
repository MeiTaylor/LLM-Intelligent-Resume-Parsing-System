
import { defineStore } from "pinia";
import { getAllResumeParser } from "../api/resume";

export const useResumeDetail = defineStore('ResumeDetails', {
    state: () => ({
        Resumedetail: {},
        ResumeList: [],
        file: {}
    }),
    actions: {
        pushResume(info) {
            this.ResumeList.push(info)
        },
        delete(info) {
            this.ResumeList = this.ResumeList.filter(item => {
                item.name === info
            })
        },
        getAllResumes(info) {
            return new Promise((resolve, reject) => {
                getAllResumeParser(info)
                    .then((response) => {
                        // 把响应的数据更新到state里
                        this.ResumeList = response.simpleResumes
                        // console.log(this.ResumeList)
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })

        },
        set_file(data){
            this.file = data
        }
    },
    persist:true
})