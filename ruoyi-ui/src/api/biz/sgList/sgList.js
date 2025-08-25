import request from '@/utils/request'

// 查询申购列表列表
export function listSgList(query) {
  return request({
    url: '/biz/sgList/list',
    method: 'get',
    params: query
  })
}

// 查询申购列表详细
export function getSgList(id) {
  return request({
    url: '/biz/sgList/' + id,
    method: 'get'
  })
}

// 新增申购列表
export function addSgList(data) {
  return request({
    url: '/biz/sgList',
    method: 'post',
    data: data
  })
}

// 修改申购列表
export function updateSgList(data) {
  return request({
    url: '/biz/sgList',
    method: 'put',
    data: data
  })
}

// 删除申购列表
export function delSgList(id) {
  return request({
    url: '/biz/sgList/' + id,
    method: 'delete'
  })
}

// 提交中签
export function submitAllocation(data) {
  return request({
    url: '/biz/sgList/submitAllocation',
    method: 'post',
    data: data
  })
}

// 后台认缴
export function subscriptionBg(data) {
  return request({
    url: '/biz/sgList/subscriptionBg',
    method: 'post',
    data: data
  })
}

// 一键转持仓
export function transToHold(data) {
  return request({
    url: '/biz/sgList/transToHold',
    method: 'post',
    data: data
  })
}

// 单个转持仓
export function transOneToHold(data) {
  return request({
    url: '/biz/sgList/transOneToHold',
    method: 'post',
    data: data
  })
}
