import request from '@/utils/request'

// 查询会员管理列表
export function listMember(query) {
  return request({
    url: '/biz/member/list',
    method: 'get',
    params: query
  })
}

// 查询会员实名认证列表
export function authMemberList(query) {
  return request({
    url: '/biz/member/authMemberList',
    method: 'get',
    params: query
  })
}

// 查询会员绑卡列表
export function memberBankList(query) {
  return request({
    url: '/biz/member/memberBankList',
    method: 'get',
    params: query
  })
}

// 查询会员管理详细
export function getMember(id) {
  return request({
    url: '/biz/member/' + id,
    method: 'get'
  })
}

// 新增会员管理
export function addMember(data) {
  return request({
    url: '/biz/member',
    method: 'post',
    data: data
  })
}

// 修改会员管理
export function updateMember(data) {
  return request({
    url: '/biz/member',
    method: 'put',
    data: data
  })
}

// 删除会员管理
export function delMember(id) {
  return request({
    url: '/biz/member/' + id,
    method: 'delete'
  })
}

// 删除实名认证
export function delAuthMember(id) {
  return request({
    url: '/biz/member/delAuthMember/' + id,
    method: 'delete'
  })
}

// 删除绑卡
export function delMemberBank(id) {
  return request({
    url: '/biz/member/delMemberBank/' + id,
    method: 'delete'
  })
}

// 修改会员状态
export function changeMemberStatus(data) {
  return request({
    url: '/biz/member/changeMemberStatus',
    method: 'post',
    data: data
  })
}

// 获取代理列表
export function getDailiList(data) {
  return request({
    url: '/system/user/getDailiList',
    method: 'post',
    data: data
  })
}

// 提交实名认证
export function submitAuthMember(data) {
  return request({
    url: '/biz/member/submitAuthMember',
    method: 'post',
    data: data
  })
}

// 提交绑定银行卡
export function submitBindingBank(data) {
  return request({
    url: '/biz/member/submitBindingBank',
    method: 'post',
    data: data
  })
}

// 搜索用户
export function searchMember(data) {
  return request({
    url: '/biz/member/searchMember',
    method: 'post',
    data: data
  })
}

// 提交充值
export function submitRecharge(data) {
  return request({
    url: '/biz/member/submitRecharge',
    method: 'post',
    data: data
  })
}

// 提交余额
export function submitUpdateBalance(data) {
  return request({
    url: '/biz/member/submitUpdateBalance',
    method: 'post',
    data: data
  })
}

// 提交T+1锁定转入转出
export function submitUpdateFreezeProfit(data) {
  return request({
    url: '/biz/member/submitUpdateFreezeProfit',
    method: 'post',
    data: data
  })
}

// 用户统计
export function getMemberStatistics(data) {
  return request({
    url: '/biz/member/getMemberStatistics',
    method: 'post',
    data: data
  })
}

// 批量审核身份认证
export function batchAuthMember(data) {
  return request({
    url: '/biz/member/batchAuthMember',
    method: 'post',
    data: data
  })
}

// 单个用户统计
export function getMemberStatisticsSingle(data) {
  return request({
    url: '/biz/member/getMemberStatisticsSingle',
    method: 'post',
    data: data
  })
}

// 获取用户手机号
export function getMobile(data) {
  return request({
    url: '/biz/member/getMobile',
    method: 'post',
    data: data
  })
}
