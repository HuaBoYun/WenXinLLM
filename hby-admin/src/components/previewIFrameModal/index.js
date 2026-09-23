/*
 * @Author: raincoat dev@example.com
 * @Date: 2023-08-12 14:01:28
 * @LastEditors: raincoat dev@example.com
 * @LastEditTime: 2024-06-22 13:12:11
 * @FilePath: \hb-admin(master)\src\components\previewIFrameModal\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import previewIFrameModal from '@/components/previewIFrameModal/index.vue'
import Vue from 'vue'

Vue.prototype.$iFrameDialog = obj => {

  // 页面载体
  const id = 'custom-dialog'
  const div = document.createElement('div')
  div.id = id
  document.body.appendChild(div)

  // 继承原组件
  const dialogComponent = Vue.extend(previewIFrameModal)

  // 实例化
  const DialogInstant = new dialogComponent()

  // 挂载
  DialogInstant.$mount(`#${id}`)

  // 打开
  DialogInstant.show({ ...obj })
}