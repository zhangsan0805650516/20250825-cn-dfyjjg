import request from '@/utils/request'

// 查询节假日配置列表
export function listHolidayConfig(query) {
  return request({
    url: '/biz/holidayConfig/list',
    method: 'get',
    params: query
  })
}

// 查询节假日配置详细
export function getHolidayConfig(id) {
  return request({
    url: '/biz/holidayConfig/' + id,
    method: 'get'
  })
}

// 新增节假日配置
export function addHolidayConfig(data) {
  return request({
    url: '/biz/holidayConfig',
    method: 'post',
    data: data
  })
}

// 修改节假日配置
export function updateHolidayConfig(data) {
  return request({
    url: '/biz/holidayConfig',
    method: 'put',
    data: data
  })
}

// 删除节假日配置
export function delHolidayConfig(id) {
  return request({
    url: '/biz/holidayConfig/' + id,
    method: 'delete'
  })
}
