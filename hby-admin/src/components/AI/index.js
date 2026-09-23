import AIEditModal from '@/components/AI/index.vue'
import Vue from 'vue'

Vue.prototype.$AiDialog = obj => {

  // 页面载体
  const id = 'ai-dialog'
  const div = document.createElement('div')
  div.id = id
  document.body.appendChild(div)

  // 继承原组件
  const dialogComponent = Vue.extend(AIEditModal)

  // 实例化
  const DialogInstant = new dialogComponent()

  // 挂载
  DialogInstant.$mount(`#${id}`)

  // 打开
  DialogInstant.show({ ...obj })
}