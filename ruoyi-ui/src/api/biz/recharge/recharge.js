import request from '@/utils/request'

// 查询充值列表
export function listRecharge(query) {
  return request({
    url: '/biz/recharge/list',
    method: 'get',
    params: query
  })
}

// 查询充值详细
export function getRecharge(id) {
  return request({
    url: '/biz/recharge/' + id,
    method: 'get'
  })
}

// 新增充值
export function addRecharge(data) {
  return request({
    url: '/biz/recharge',
    method: 'post',
    data: data
  })
}

// 修改充值
export function updateRecharge(data) {
  return request({
    url: '/biz/recharge',
    method: 'put',
    data: data
  })
}

// 删除充值
export function delRecharge(id) {
  return request({
    url: '/biz/recharge/' + id,
    method: 'delete'
  })
}

// 审核充值
export function approveRecharge(data) {
  return request({
    url: '/biz/recharge/approveRecharge',
    method: 'post',
    data: data
  })
}

// 充值统计
export function getRechargeStatistics(data) {
  return request({
    url: '/biz/recharge/getRechargeStatistics',
    method: 'post',
    data: data
  })
}
