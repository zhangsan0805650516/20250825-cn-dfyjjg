import request from '@/utils/request'

// 查询新闻列表
export function listNews(query) {
  return request({
    url: '/biz/news/list',
    method: 'get',
    params: query
  })
}

// 查询新闻详细
export function getNews(id) {
  return request({
    url: '/biz/news/' + id,
    method: 'get'
  })
}

// 新增新闻
export function addNews(data) {
  return request({
    url: '/biz/news',
    method: 'post',
    data: data
  })
}

// 修改新闻
export function updateNews(data) {
  return request({
    url: '/biz/news',
    method: 'put',
    data: data
  })
}

// 删除新闻
export function delNews(id) {
  return request({
    url: '/biz/news/' + id,
    method: 'delete'
  })
}
