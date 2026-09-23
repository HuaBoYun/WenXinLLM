import request from '@/utils/request'

/**
 * 生成抵消凭证
 */
export function generateVouchers(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/generateVouchers',
    method: 'post',
    data
  })
}

/**
 * 查询抵消凭证列表
 */
export function getVoucherList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/getVoucherList',
    method: 'post',
    data
  })
}

/**
 * 根据凭证号查询抵消凭证列表
 */
export function getVouchersByNo(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/getVouchersByNo',
    method: 'post',
    data
  })
}

/**
 * 根据模型ID和期间查询凭证号列表
 */
export function getVoucherNoList(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/getVoucherNoList',
    method: 'post',
    data
  })
}

/**
 * 删除抵消凭证
 */
export function deleteVouchers(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/deleteVouchers',
    method: 'post',
    data
  })
}

/**
 * 确认抵消凭证
 */
export function confirmVouchers(data) {
  return request({
    url: '/cwgxAi/consolidationReport/eliminationVoucher/confirmVouchers',
    method: 'post',
    data
  })
}

