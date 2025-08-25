import request from '@/utils/request'

// 查询用户消息列表
export function listUserNotice(query) {
  return request({
    url: '/biz/userNotice/list',
    method: 'get',
    params: query
  })
}

// 查询用户消息详细
export function getUserNotice(id) {
  return request({
    url: '/biz/userNotice/' + id,
    method: 'get'
  })
}

// 新增用户消息
export function addUserNotice(data) {
  return request({
    url: '/biz/userNotice',
    method: 'post',
    data: data
  })
}

// 修改用户消息
export function updateUserNotice(data) {
  return request({
    url: '/biz/userNotice',
    method: 'put',
    data: data
  })
}

// 删除用户消息
export function delUserNotice(id) {
  return request({
    url: '/biz/userNotice/' + id,
    method: 'delete'
  })
}
