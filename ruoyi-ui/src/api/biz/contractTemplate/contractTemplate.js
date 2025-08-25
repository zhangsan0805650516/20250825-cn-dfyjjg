import request from '@/utils/request'

// 查询合同模板列表
export function listContractTemplate(query) {
  return request({
    url: '/biz/contractTemplate/list',
    method: 'get',
    params: query
  })
}

// 查询合同模板详细
export function getContractTemplate(id) {
  return request({
    url: '/biz/contractTemplate/' + id,
    method: 'get'
  })
}

// 新增合同模板
export function addContractTemplate(data) {
  return request({
    url: '/biz/contractTemplate',
    method: 'post',
    data: data
  })
}

// 修改合同模板
export function updateContractTemplate(data) {
  return request({
    url: '/biz/contractTemplate',
    method: 'put',
    data: data
  })
}

// 删除合同模板
export function delContractTemplate(id) {
  return request({
    url: '/biz/contractTemplate/' + id,
    method: 'delete'
  })
}
