import request from "../utils/request"

export function getFile(data){
    return request({
        url: '/getfile',
        method: 'post',
        responseType: 'blob',
        data
    })
}
export function uploadFile(data, userId, jobId){
    return request({
        url: '/Resume/upload',
        method: 'post',
        headers: {
            "Content-type": "multipart/form-data"
        },
        params:{ jobId, userId },
        data
    })
}