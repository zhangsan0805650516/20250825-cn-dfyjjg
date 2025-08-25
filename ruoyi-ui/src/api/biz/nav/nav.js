import request from '@/utils/request'

// 查询导航图标列表
export function listNav(query) {
  return request({
    url: '/biz/nav/list',
    method: 'get',
    params: query
  })
}

// 查询导航图标详细
export function getNav(id) {
  return request({
    url: '/biz/nav/' + id,
    method: 'get'
  })
}

// 新增导航图标
export function addNav(data) {
  return request({
    url: '/biz/nav',
    method: 'post',
    data: data
  })
}

// 修改导航图标
export function updateNav(data) {
  return request({
    url: '/biz/nav',
    method: 'put',
    data: data
  })
}

// 删除导航图标
export function delNav(id) {
  return request({
    url: '/biz/nav/' + id,
    method: 'delete'
  })
}

// 显示隐藏开关修改
export function changeSwitch(data) {
  return request({
    url: '/biz/nav/changeSwitch',
    method: 'post',
    data: data
  })
}
