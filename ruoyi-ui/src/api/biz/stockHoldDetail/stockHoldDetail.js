import request from '@/utils/request'

// 查询持仓明细列表
export function listStockHoldDetail(query) {
  return request({
    url: '/biz/stockHoldDetail/list',
    method: 'get',
    params: query
  })
}

// 查询持仓明细详细
export function getStockHoldDetail(id) {
  return request({
    url: '/biz/stockHoldDetail/' + id,
    method: 'get'
  })
}

// 新增持仓明细
export function addStockHoldDetail(data) {
  return request({
    url: '/biz/stockHoldDetail',
    method: 'post',
    data: data
  })
}

// 修改持仓明细
export function updateStockHoldDetail(data) {
  return request({
    url: '/biz/stockHoldDetail',
    method: 'put',
    data: data
  })
}

// 删除持仓明细
export function delStockHoldDetail(id) {
  return request({
    url: '/biz/stockHoldDetail/' + id,
    method: 'delete'
  })
}

// 锁仓/解锁
export function changeLockStatus(data) {
  return request({
    url: '/biz/stockHoldDetail/changeLockStatus',
    method: 'post',
    data: data
  })
}

// 调整T+N
export function changeTN(data) {
  return request({
    url: '/biz/stockHoldDetail/changeTN',
    method: 'post',
    data: data
  })
}

// 持仓统计
export function getHoldStatistics(data) {
  return request({
    url: '/biz/stockHoldDetail/getHoldStatistics',
    method: 'post',
    data: data
  })
}




