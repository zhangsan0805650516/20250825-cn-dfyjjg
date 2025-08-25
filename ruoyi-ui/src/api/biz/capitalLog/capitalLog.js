import request from '@/utils/request'

// 查询资金记录列表
export function listCapitalLog(query) {
  return request({
    url: '/biz/capitalLog/list',
    method: 'get',
    params: query
  })
}

// 查询资金记录详细
export function getCapitalLog(id) {
  return request({
    url: '/biz/capitalLog/' + id,
    method: 'get'
  })
}

// 新增资金记录
export function addCapitalLog(data) {
  return request({
    url: '/biz/capitalLog',
    method: 'post',
    data: data
  })
}

// 修改资金记录
export function updateCapitalLog(data) {
  return request({
    url: '/biz/capitalLog',
    method: 'put',
    data: data
  })
}

// 删除资金记录
export function delCapitalLog(id) {
  return request({
    url: '/biz/capitalLog/' + id,
    method: 'delete'
  })
}

// 获取资金池信息
export function getFundPoolInfo(data) {
  return request({
    url: '/biz/fundPool/getFundPoolInfo',
    method: 'post',
    data: data
  })
}
