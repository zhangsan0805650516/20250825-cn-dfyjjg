import request from '@/utils/request'

// 查询集合资产列表
export function listCollectiveAsset(query) {
  return request({
    url: '/biz/collectiveAsset/list',
    method: 'get',
    params: query
  })
}

// 查询集合资产详细
export function getCollectiveAsset(id) {
  return request({
    url: '/biz/collectiveAsset/' + id,
    method: 'get'
  })
}

// 新增集合资产
export function addCollectiveAsset(data) {
  return request({
    url: '/biz/collectiveAsset',
    method: 'post',
    data: data
  })
}

// 修改集合资产
export function updateCollectiveAsset(data) {
  return request({
    url: '/biz/collectiveAsset',
    method: 'put',
    data: data
  })
}

// 删除集合资产
export function delCollectiveAsset(id) {
  return request({
    url: '/biz/collectiveAsset/' + id,
    method: 'delete'
  })
}

// 集合资产开始
export function startAsset(data) {
  return request({
    url: '/biz/collectiveAsset/startAsset',
    method: 'post',
    data: data
  })
}

// 集合资产结束
export function endAsset(data) {
  return request({
    url: '/biz/collectiveAsset/endAsset',
    method: 'post',
    data: data
  })
}
