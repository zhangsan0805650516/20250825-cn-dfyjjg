import request from '@/utils/request'

// 查询用户合同列表
export function listContractList(query) {
  return request({
    url: '/biz/contractList/list',
    method: 'get',
    params: query
  })
}

// 查询用户合同详细
export function getContractList(id) {
  return request({
    url: '/biz/contractList/' + id,
    method: 'get'
  })
}

// 新增用户合同
export function addContractList(data) {
  return request({
    url: '/biz/contractList',
    method: 'post',
    data: data
  })
}

// 修改用户合同
export function updateContractList(data) {
  return request({
    url: '/biz/contractList',
    method: 'put',
    data: data
  })
}

// 删除用户合同
export function delContractList(id) {
  return request({
    url: '/biz/contractList/' + id,
    method: 'delete'
  })
}
