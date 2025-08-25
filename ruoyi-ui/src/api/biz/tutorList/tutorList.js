import request from '@/utils/request'

// 查询分类列表
export function listTutorList(query) {
  return request({
    url: '/biz/tutorList/list',
    method: 'get',
    params: query
  })
}

// 查询分类详细
export function getTutorList(id) {
  return request({
    url: '/biz/tutorList/' + id,
    method: 'get'
  })
}

// 新增分类
export function addTutorList(data) {
  return request({
    url: '/biz/tutorList',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateTutorList(data) {
  return request({
    url: '/biz/tutorList',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delTutorList(id) {
  return request({
    url: '/biz/tutorList/' + id,
    method: 'delete'
  })
}

// 搜索导师
export function searchTutor(data) {
  return request({
    url: '/biz/tutorList/searchTutor',
    method: 'post',
    data: data
  })
}
