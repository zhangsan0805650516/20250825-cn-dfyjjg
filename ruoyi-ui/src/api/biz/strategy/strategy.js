import request from '@/utils/request'

// 查询策略列表
export function listStrategy(query) {
  return request({
    url: '/biz/strategy/list',
    method: 'get',
    params: query
  })
}

// 查询策略详细
export function getStrategy(id) {
  return request({
    url: '/biz/strategy/' + id,
    method: 'get'
  })
}

// 新增策略
export function addStrategy(data) {
  return request({
    url: '/biz/strategy',
    method: 'post',
    data: data
  })
}

// 修改策略
export function updateStrategy(data) {
  return request({
    url: '/biz/strategy',
    method: 'put',
    data: data
  })
}

// 删除策略
export function delStrategy(id) {
  return request({
    url: '/biz/strategy/' + id,
    method: 'delete'
  })
}

// 搜索股票
export function searchStock(data) {
  return request({
    url: '/biz/strategy/searchStock',
    method: 'post',
    data: data
  })
}

// 修改股票状态
export function changeStockStatus(data) {
  return request({
    url: '/biz/strategy/changeStockStatus',
    method: 'post',
    data: data
  })
}

// 修改股票状态
export function submitFormYycz(data) {
  return request({
    url: '/biz/strategy/submitFormYycz',
    method: 'post',
    data: data
  })
}
