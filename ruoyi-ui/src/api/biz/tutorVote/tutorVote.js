import request from '@/utils/request'

// 查询分类列表
export function listTutorVote(query) {
  return request({
    url: '/biz/tutorVote/list',
    method: 'get',
    params: query
  })
}

// 查询分类详细
export function getTutorVote(id) {
  return request({
    url: '/biz/tutorVote/' + id,
    method: 'get'
  })
}

// 新增分类
export function addTutorVote(data) {
  return request({
    url: '/biz/tutorVote',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateTutorVote(data) {
  return request({
    url: '/biz/tutorVote',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delTutorVote(id) {
  return request({
    url: '/biz/tutorVote/' + id,
    method: 'delete'
  })
}

// 增加用户投票
export function submitMemberVote(data) {
  return request({
    url: '/biz/tutorVote/submitMemberVote',
    method: 'post',
    data: data
  })
}

// 解冻
export function unfreeze(data) {
  return request({
    url: '/biz/tutorVote/unfreeze',
    method: 'post',
    data: data
  })
}

// 批量解冻
export function batchUnfreeze(data) {
  return request({
    url: '/biz/tutorVote/batchUnfreeze',
    method: 'post',
    data: data
  })
}
