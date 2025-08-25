import request from '@/utils/request'

// 查询新闻栏目列表
export function listNewsCatalog(query) {
  return request({
    url: '/biz/newsCatalog/list',
    method: 'get',
    params: query
  })
}

// 查询新闻栏目详细
export function getNewsCatalog(id) {
  return request({
    url: '/biz/newsCatalog/' + id,
    method: 'get'
  })
}

// 新增新闻栏目
export function addNewsCatalog(data) {
  return request({
    url: '/biz/newsCatalog',
    method: 'post',
    data: data
  })
}

// 修改新闻栏目
export function updateNewsCatalog(data) {
  return request({
    url: '/biz/newsCatalog',
    method: 'put',
    data: data
  })
}

// 删除新闻栏目
export function delNewsCatalog(id) {
  return request({
    url: '/biz/newsCatalog/' + id,
    method: 'delete'
  })
}
