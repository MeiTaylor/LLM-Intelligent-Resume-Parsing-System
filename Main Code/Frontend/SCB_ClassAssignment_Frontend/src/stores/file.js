import { defineStore } from "pinia";
import { uploadFile } from "../api/file";

export const useFileStore = defineStore('filePreview', {
    state: () => ({
        filelink: 'http://ashuai.work:10000/getDoc'
    }),
    actions: {
        set_file_link(fileLink) {
            this.filelink = fileLink

        },
        uploadFile(fileObj, userid, jobid) {
            console.log("先查一查上传文件传进来的是什么东西 ", fileObj)
            let formdata = new FormData();
            formdata.set("file", fileObj)
            console.log(formdata)
            return new Promise((resolve, reject) => {
                uploadFile(formdata, userid, jobid)
                    .then((response) => {

                        const filepath = document.getElementsByClassName("el-upload__input")[0].value
                        console.log(filepath)

                        console.log(response)
                        // 这里写要对返回的请求做什么事

                        resolve(response)
                    })
                    .catch(error => {
                        console.log(error)
                        reject(error)
                    })
            })
        }
    }
})