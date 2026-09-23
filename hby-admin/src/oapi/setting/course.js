/*
 * @Date: 2022-02-25 11:49:36
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-03-10 11:04:49
 * @FilePath: /hb-admin/src/api/setting/course.js
 */

import request from '@/utils/request'
import { transData } from '@/utils/requestData'

/**
 * @description: 课程类别-列表
 * @param {*}
 * @return {*}
 */
export function getCourseTypeList(data) {
  return request({
    url: '/setting/videoType/list',
    method: 'post',
    data: transData(data),
  })
}
/**
 * @description: 课程类别-保存
 * @param {*} data
 * @return {*}
 */
export function saveCourseType(data) {
  return request({
    url: '/setting/videoType_save',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程类别-修改
 * @param {*} data
 * @return {*}
 */
// export function updateCourseType(data) {
//   return request({
//     url: '/setting/videoType_update',
//     method: 'post',
//     data: transData(data),
//   })
// }

/**
 * @description: 课程类别-删除
 * @param {*} data
 * @return {*}
 */
export function deleteCourseType(data) {
  return request({
    url: '/setting/videoType_delete',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程-列表
 * @param {*}
 * @return {*}
 */
export function getCourseList(data) {
  return request({
    url: '/setting/videolist',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程-保存
 * @param {*} data
 * @return {*}
 */
export function saveCourse(data) {
  return request({
    url: '/setting/video_save',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程-修改
 * @param {*} data
 * @return {*}
 */
export function updateCourse(data) {
  return request({
    url: '/setting/video_videomodi',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程-删除判断？
 * @param {*} data
 * @return {*}
 */
export function deleteCourses(data) {
  return request({
    url: '/setting/video/deletes',
    method: 'post',
    data: transData(data),
  })
}

/**
 * @description: 课程-删除
 * @param {*} data
 * @return {*}
 */
export function deleteCourse(data) {
  return request({
    url: '/setting/video_del',
    method: 'post',
    data: transData(data),
  })
}
