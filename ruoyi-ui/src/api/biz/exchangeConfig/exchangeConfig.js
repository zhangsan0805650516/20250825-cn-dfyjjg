import request from '@/utils/request'

// 查询交易所配置列表
export function listExchangeConfig(query) {
  return request({
    url: '/biz/exchangeConfig/list',
    method: 'get',
    params: query
  })
}

// 查询交易所配置详细
export function getExchangeConfig(id) {
  return request({
    url: '/biz/exchangeConfig/' + id,
    method: 'get'
  })
}

// 新增交易所配置
export function addExchangeConfig(data) {
  return request({
    url: '/biz/exchangeConfig',
    method: 'post',
    data: data
  })
}

// 修改交易所配置
export function updateExchangeConfig(data) {
  return request({
    url: '/biz/exchangeConfig',
    method: 'put',
    data: data
  })
}

// 删除交易所配置
export function delExchangeConfig(id) {
  return request({
    url: '/biz/exchangeConfig/' + id,
    method: 'delete'
  })
}

// 获取交易所列表
export function getExchangeList(data) {
  return request({
    url: '/biz/exchangeConfig/getExchangeList',
    method: 'post',
    data: data
  })
}
