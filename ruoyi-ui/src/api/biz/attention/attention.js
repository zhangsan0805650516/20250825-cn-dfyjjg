import request from '@/utils/request'

// 查询广告图列表
export function listAttention(query) {
  return request({
    url: '/biz/attention/list',
    method: 'get',
    params: query
  })
}

// 查询广告图详细
export function getAttention(id) {
  return request({
    url: '/biz/attention/' + id,
    method: 'get'
  })
}

// 新增广告图
export function addAttention(data) {
  return request({
    url: '/biz/attention',
    method: 'post',
    data: data
  })
}

// 修改广告图
export function updateAttention(data) {
  return request({
    url: '/biz/attention',
    method: 'put',
    data: data
  })
}

// 删除广告图
export function delAttention(id) {
  return request({
    url: '/biz/attention/' + id,
    method: 'delete'
  })
}
