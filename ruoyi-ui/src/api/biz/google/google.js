import request from '@/utils/request'

// 生成二维码绑定代理
export function getQrcode(data) {
  return request({
    url: '/biz/google/getQrcode',
    method: 'post',
    data: data
  })
}

// 校验验证码
export function checkCode(data) {
  return request({
    url: '/biz/google/checkCode',
    method: 'post',
    data: data
  })
}

// 检查Google校验状态
export function checkAuthStatus(data) {
  return request({
    url: '/biz/google/checkAuthStatus',
    method: 'post',
    data: data
  })
}

// 检查Google校验状态
export function bindingSecret(data) {
  return request({
    url: '/biz/google/bindingSecret',
    method: 'post',
    data: data
  })
}
