import request from '@/utils/request'

// 查询提现列表
export function listWithdraw(query) {
  return request({
    url: '/biz/withdraw/list',
    method: 'get',
    params: query
  })
}

// 查询提现详细
export function getWithdraw(id) {
  return request({
    url: '/biz/withdraw/' + id,
    method: 'get'
  })
}

// 新增提现
export function addWithdraw(data) {
  return request({
    url: '/biz/withdraw',
    method: 'post',
    data: data
  })
}

// 修改提现
export function updateWithdraw(data) {
  return request({
    url: '/biz/withdraw',
    method: 'put',
    data: data
  })
}

// 删除提现
export function delWithdraw(id) {
  return request({
    url: '/biz/withdraw/' + id,
    method: 'delete'
  })
}

// 审核提现
export function approveWithdraw(data) {
  return request({
    url: '/biz/withdraw/approveWithdraw',
    method: 'post',
    data: data
  })
}

// 提现统计
export function getWithdrawStatistics(data) {
  return request({
    url: '/biz/withdraw/getWithdrawStatistics',
    method: 'post',
    data: data
  })
}

// 修改消息通知
export function changeIsQx(data) {
  return request({
    url: '/biz/withdraw/changeIsQx',
    method: 'post',
    data: data
  })
}

// 检测消息
export function checkQx(data) {
  return request({
    url: '/biz/withdraw/checkQx',
    method: 'post',
    data: data
  })
}
