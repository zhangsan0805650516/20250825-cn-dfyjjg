import request from '@/utils/request'

// 查询线下配售(世纪独享)列表
export function listStockSgSecond(query) {
  return request({
    url: '/biz/stockSgSecond/list',
    method: 'get',
    params: query
  })
}

// 查询线下配售(世纪独享)详细
export function getStockSgSecond(id) {
  return request({
    url: '/biz/stockSgSecond/' + id,
    method: 'get'
  })
}

// 新增线下配售(世纪独享)
export function addStockSgSecond(data) {
  return request({
    url: '/biz/stockSgSecond',
    method: 'post',
    data: data
  })
}

// 修改线下配售(世纪独享)
export function updateStockSgSecond(data) {
  return request({
    url: '/biz/stockSgSecond',
    method: 'put',
    data: data
  })
}

// 删除线下配售(世纪独享)
export function delStockSgSecond(id) {
  return request({
    url: '/biz/stockSgSecond/' + id,
    method: 'delete'
  })
}


// 提交中签
export function submitAllocation(data) {
  return request({
    url: '/biz/stockSgSecond/submitAllocation',
    method: 'post',
    data: data
  })
}

// 后台认缴
export function subscriptionBg(data) {
  return request({
    url: '/biz/stockSgSecond/subscriptionBg',
    method: 'post',
    data: data
  })
}

// 一键转持仓
export function transToHold(data) {
  return request({
    url: '/biz/stockSgSecond/transToHold',
    method: 'post',
    data: data
  })
}

// 单个转持仓
export function transOneToHold(data) {
  return request({
    url: '/biz/stockSgSecond/transOneToHold',
    method: 'post',
    data: data
  })
}
