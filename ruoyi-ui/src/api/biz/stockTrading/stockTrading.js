import request from '@/utils/request'

// 查询成交记录列表
export function listStockTrading(query) {
  return request({
    url: '/biz/stockTrading/list',
    method: 'get',
    params: query
  })
}

// 查询成交记录详细
export function getStockTrading(id) {
  return request({
    url: '/biz/stockTrading/' + id,
    method: 'get'
  })
}

// 新增成交记录
export function addStockTrading(data) {
  return request({
    url: '/biz/stockTrading',
    method: 'post',
    data: data
  })
}

// 修改成交记录
export function updateStockTrading(data) {
  return request({
    url: '/biz/stockTrading',
    method: 'put',
    data: data
  })
}

// 删除成交记录
export function delStockTrading(id) {
  return request({
    url: '/biz/stockTrading/' + id,
    method: 'delete'
  })
}

// 平仓 汇总
export function closingPosition(data) {
  return request({
    url: '/biz/stockTrading/closingPosition',
    method: 'post',
    data: data
  })
}

// 平仓 明细
export function closingPositionDetail(data) {
  return request({
    url: '/biz/stockTrading/closingPositionDetail',
    method: 'post',
    data: data
  })
}

// 交易统计
export function getTradingStatistics(data) {
  return request({
    url: '/biz/stockTrading/getTradingStatistics',
    method: 'post',
    data: data
  })
}
