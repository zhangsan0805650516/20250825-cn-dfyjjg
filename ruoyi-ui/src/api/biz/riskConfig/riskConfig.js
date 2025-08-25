import request from '@/utils/request'

// 查询风控设置列表
export function listRiskConfig(query) {
  return request({
    url: '/biz/riskConfig/list',
    method: 'get',
    params: query
  })
}

// 查询风控设置详细
export function getRiskConfig(id) {
  return request({
    url: '/biz/riskConfig/' + id,
    method: 'get'
  })
}

// 查询风控设置详细
export function getInfoByName(data) {
  return request({
    url: '/biz/riskConfig/getInfoByName',
    method: 'post',
    data: data
  })
}

// 新增风控设置
export function addRiskConfig(data) {
  return request({
    url: '/biz/riskConfig',
    method: 'post',
    data: data
  })
}

// 修改风控设置
export function updateRiskConfig(data) {
  return request({
    url: '/biz/riskConfig',
    method: 'put',
    data: data
  })
}

// 删除风控设置
export function delRiskConfig(id) {
  return request({
    url: '/biz/riskConfig/' + id,
    method: 'delete'
  })
}

// 获取字典分类
export function getConfiggroup(data) {
  return request({
    url: '/biz/riskConfig/getConfiggroup',
    method: 'post',
    data: data
  })
}

// 根据分类获取字典列表
export function getConfigListByGroup(data) {
  return request({
    url: '/biz/riskConfig/getConfigListByGroup',
    method: 'post',
    data: data
  })
}

// 修改风控设置列表
export function updateRiskConfigList(data) {
  return request({
    url: '/biz/riskConfig/updateRiskConfigList',
    method: 'post',
    data: data
  })
}

// 查询风控设置详细
export function getTitle() {
  return request({
    url: '/biz/riskConfig/getTitle',
    method: 'get'
  })
}

// 查询风控设置详细
export function getLogo() {
  return request({
    url: '/biz/riskConfig/getLogo',
    method: 'get'
  })
}
