import request from "../utils/request"

export function getResumeParser(data) {
    return request({
        url: '/applicant/detailedInfo',
        method: 'post',
        data
    })
}
export function getAllResumeParser(data) {
    return request({
        url: '/Resume/AllSimpleReusmes',
        method: 'post',
        data,
    })
}