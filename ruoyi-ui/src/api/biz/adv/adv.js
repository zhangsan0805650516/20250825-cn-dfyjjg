import request from '@/utils/request'

// 查询广告图列表
export function listAdv(query) {
  return request({
    url: '/biz/adv/list',
    method: 'get',
    params: query
  })
}

// 查询广告图详细
export function getAdv(id) {
  return request({
    url: '/biz/adv/' + id,
    method: 'get'
  })
}

// 新增广告图
export function addAdv(data) {
  return request({
    url: '/biz/adv',
    method: 'post',
    data: data
  })
}

// 修改广告图
export function updateAdv(data) {
  return request({
    url: '/biz/adv',
    method: 'put',
    data: data
  })
}

// 删除广告图
export function delAdv(id) {
  return request({
    url: '/biz/adv/' + id,
    method: 'delete'
  })
}
