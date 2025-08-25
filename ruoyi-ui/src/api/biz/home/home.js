import request from '@/utils/request'

// 首页统计
export function getHomeStatistics(data) {
  return request({
    url: '/biz/home/getHomeStatistics',
    method: 'post',
    data: data
  })
}
