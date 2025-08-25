import request from '@/utils/request'

// 查询搜索用户记录列表
export function listSearchMember(query) {
  return request({
    url: '/biz/searchMember/list',
    method: 'get',
    params: query
  })
}

// 查询搜索用户记录详细
export function getSearchMember(id) {
  return request({
    url: '/biz/searchMember/' + id,
    method: 'get'
  })
}

// 新增搜索用户记录
export function addSearchMember(data) {
  return request({
    url: '/biz/searchMember',
    method: 'post',
    data: data
  })
}

// 修改搜索用户记录
export function updateSearchMember(data) {
  return request({
    url: '/biz/searchMember',
    method: 'put',
    data: data
  })
}

// 删除搜索用户记录
export function delSearchMember(id) {
  return request({
    url: '/biz/searchMember/' + id,
    method: 'delete'
  })
}

// 搜索用户
export function getSearchList(data) {
  return request({
    url: '/biz/searchMember/getSearchList',
    method: 'post',
    data: data
  })
}

// 剩余搜索次数
export function getSearchTimesLeft(data) {
  return request({
    url: '/biz/searchMember/getSearchTimesLeft',
    method: 'post',
    data: data
  })
}
