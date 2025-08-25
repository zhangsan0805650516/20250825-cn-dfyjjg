import request from '@/utils/request'

// 查询交易审核列表
export function listTradeApprove(query) {
  return request({
    url: '/biz/tradeApprove/list',
    method: 'get',
    params: query
  })
}

// 查询交易审核详细
export function getTradeApprove(id) {
  return request({
    url: '/biz/tradeApprove/' + id,
    method: 'get'
  })
}

// 新增交易审核
export function addTradeApprove(data) {
  return request({
    url: '/biz/tradeApprove',
    method: 'post',
    data: data
  })
}

// 修改交易审核
export function updateTradeApprove(data) {
  return request({
    url: '/biz/tradeApprove',
    method: 'put',
    data: data
  })
}

// 删除交易审核
export function delTradeApprove(id) {
  return request({
    url: '/biz/tradeApprove/' + id,
    method: 'delete'
  })
}
