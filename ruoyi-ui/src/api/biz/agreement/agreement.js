import request from '@/utils/request'

// 查询关闭通道协议列表
export function listAgreement(query) {
  return request({
    url: '/biz/agreement/list',
    method: 'get',
    params: query
  })
}

// 查询关闭通道协议详细
export function getAgreement(id) {
  return request({
    url: '/biz/agreement/' + id,
    method: 'get'
  })
}

// 新增关闭通道协议
export function addAgreement(data) {
  return request({
    url: '/biz/agreement',
    method: 'post',
    data: data
  })
}

// 修改关闭通道协议
export function updateAgreement(data) {
  return request({
    url: '/biz/agreement',
    method: 'put',
    data: data
  })
}

// 删除关闭通道协议
export function delAgreement(id) {
  return request({
    url: '/biz/agreement/' + id,
    method: 'delete'
  })
}

// 获取数据
export function getAgreementData(data) {
  return request({
    url: '/biz/agreement/getAgreementData',
    method: 'post',
    data: data
  })
}

