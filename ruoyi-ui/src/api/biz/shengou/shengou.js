import request from '@/utils/request'

// 查询新股列表列表
export function listShengou(query) {
  return request({
    url: '/biz/shengou/list',
    method: 'get',
    params: query
  })
}

// 查询新股列表详细
export function getShengou(id) {
  return request({
    url: '/biz/shengou/' + id,
    method: 'get'
  })
}

// 新增新股列表
export function addShengou(data) {
  return request({
    url: '/biz/shengou',
    method: 'post',
    data: data
  })
}

// 修改新股列表
export function updateShengou(data) {
  return request({
    url: '/biz/shengou',
    method: 'put',
    data: data
  })
}

// 删除新股列表
export function delShengou(id) {
  return request({
    url: '/biz/shengou/' + id,
    method: 'delete'
  })
}

// 修改申购配售开关
export function changeSwitch(data) {
  return request({
    url: '/biz/shengou/changeSwitch',
    method: 'post',
    data: data
  })
}

// 搜索新股
export function searchNewStock(data) {
  return request({
    url: '/biz/shengou/searchNewStock',
    method: 'post',
    data: data
  })
}

// 提交申购配售配置
export function submitExchange(data) {
  return request({
    url: '/biz/shengou/submitExchange',
    method: 'post',
    data: data
  })
}
