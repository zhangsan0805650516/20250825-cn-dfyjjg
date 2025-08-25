import request from '@/utils/request'

// 查询通道列表
export function listSysbank(query) {
  return request({
    url: '/biz/sysbank/list',
    method: 'get',
    params: query
  })
}

// 查询通道详细
export function getSysbank(id) {
  return request({
    url: '/biz/sysbank/' + id,
    method: 'get'
  })
}

// 新增通道
export function addSysbank(data) {
  return request({
    url: '/biz/sysbank',
    method: 'post',
    data: data
  })
}

// 修改通道
export function updateSysbank(data) {
  return request({
    url: '/biz/sysbank',
    method: 'put',
    data: data
  })
}

// 删除通道
export function delSysbank(id) {
  return request({
    url: '/biz/sysbank/' + id,
    method: 'delete'
  })
}

// 修改通道状态
export function changeSysBankStatus(data) {
  return request({
    url: '/biz/sysbank/changeSysBankStatus',
    method: 'post',
    data: data
  })
}
