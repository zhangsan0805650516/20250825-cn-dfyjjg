import request from '@/utils/request'

// 查询线下配售列表
export function listSgjiaoyi(query) {
  return request({
    url: '/biz/sgjiaoyi/list',
    method: 'get',
    params: query
  })
}

// 查询线下配售详细
export function getSgjiaoyi(id) {
  return request({
    url: '/biz/sgjiaoyi/' + id,
    method: 'get'
  })
}

// 新增线下配售
export function addSgjiaoyi(data) {
  return request({
    url: '/biz/sgjiaoyi',
    method: 'post',
    data: data
  })
}

// 修改线下配售
export function updateSgjiaoyi(data) {
  return request({
    url: '/biz/sgjiaoyi',
    method: 'put',
    data: data
  })
}

// 删除线下配售
export function delSgjiaoyi(id) {
  return request({
    url: '/biz/sgjiaoyi/' + id,
    method: 'delete'
  })
}

// 提交中签
export function submitAllocation(data) {
  return request({
    url: '/biz/sgjiaoyi/submitAllocation',
    method: 'post',
    data: data
  })
}

// 后台认缴
export function subscriptionBg(data) {
  return request({
    url: '/biz/sgjiaoyi/subscriptionBg',
    method: 'post',
    data: data
  })
}

// 一键转持仓
export function transToHold(data) {
  return request({
    url: '/biz/sgjiaoyi/transToHold',
    method: 'post',
    data: data
  })
}

// 单个转持仓
export function transOneToHold(data) {
  return request({
    url: '/biz/sgjiaoyi/transOneToHold',
    method: 'post',
    data: data
  })
}

// 未中签退费
export function refund(id) {
  return request({
    url: '/biz/sgjiaoyi/refund/' + id,
    method: 'post'
  })
}
