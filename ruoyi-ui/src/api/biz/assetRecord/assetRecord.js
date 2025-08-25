import request from '@/utils/request'

// 查询集合资产记录列表
export function listAssetRecord(query) {
  return request({
    url: '/biz/assetRecord/list',
    method: 'get',
    params: query
  })
}

// 查询集合资产记录详细
export function getAssetRecord(id) {
  return request({
    url: '/biz/assetRecord/' + id,
    method: 'get'
  })
}

// 新增集合资产记录
export function addAssetRecord(data) {
  return request({
    url: '/biz/assetRecord',
    method: 'post',
    data: data
  })
}

// 修改集合资产记录
export function updateAssetRecord(data) {
  return request({
    url: '/biz/assetRecord',
    method: 'put',
    data: data
  })
}

// 删除集合资产记录
export function delAssetRecord(id) {
  return request({
    url: '/biz/assetRecord/' + id,
    method: 'delete'
  })
}

// 审核赎回
export function approveRedeem(data) {
  return request({
    url: '/biz/assetRecord/approveRedeem',
    method: 'post',
    data: data
  })
}
