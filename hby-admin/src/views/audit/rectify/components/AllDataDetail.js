import AllDataDetail from '@/views/audit/rectify/components/AllDataDetail.vue'
import Vue from 'vue'
let s = 0

Vue.prototype.$showAllDataDetailDialog = obj => {
  // 页面载体
  const id = 'custom-dialog-' + s
  const div = document.createElement('div')
  div.id = id
  document.body.appendChild(div)

  // 继承原组件
  const dialogComponent = Vue.extend(AllDataDetail)

  // 实例化
  const DialogInstant = new dialogComponent()

  // 挂载
  DialogInstant.$mount(`#${id}`)

  // 打开
  DialogInstant.showEdit({ ...obj })
  s++
}