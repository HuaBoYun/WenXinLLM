/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-08-30 20:47:15
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-09-01 22:04:04
 * @FilePath: \hb-admin\src\main.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import '@/vab'
import FcDesigner from '@form-create/designer'
import formCreate from '@form-create/element-ui'
import Tinymce from '@form-generator/components/tinymce/index.vue'
import 'bootstrap'
import Vue from 'vue'
import VueBus from 'vue-bus'
import App from './App'
import i18n from './i18n'
import { translateTitle } from '@/utils/i18n'
import './registerServiceWorker'
import router from './router'
import store from './store'
import Print from 'vue-print-nb'
import '@/components/previewIFrameModal/index'
import '@/components/AI/index'
import VabAiTest from '@/vab/components/VabAiEdit/components/index.vue'
Vue.component('Tinymce', Tinymce)
Vue.use(VueBus)
Vue.use(formCreate)
Vue.use(FcDesigner)
Vue.use(Print)
Vue.component('VabAiTest', VabAiTest)


import { hasAuth, parseTime } from '@/utils'
Vue.prototype.parseTime = parseTime
Vue.prototype.hasAuth = hasAuth
Vue.prototype.$translateTitle = translateTitle

// 挂载HTTP客户端到Vue原型
import http from '@/utils/request'
Vue.prototype.$http = http

// 指标控制全局拦截插件
import IndicatorControlPlugin from '@/utils/indicatorControlPlugin'
Vue.use(IndicatorControlPlugin)

window.key = 'b8e9a1c7d4f265a830e7b1f4d8a9c6e2'; // AES解密密钥
window.iv = 'a1b2c3d4e5f6g7h8'; // AES解密密钥

/**
 * @description 正式环境默认使用mock，正式项目记得注释后再打包
 */
import { isExternal } from '@/utils/validate'
import { baseURL } from './config'
window.key = 'b8e9a1c7d4f265a830e7b1f4d8a9c6e2'; // AES解密密钥
window.iv = 'a1b2c3d4e5f6g7h8'; // AES解密密钥
if (process.env.NODE_ENV === 'production' && !isExternal(baseURL)) {
  const { mockXHR } = require('@/utils/static')
  mockXHR()
}
let time = null;
// 在Vue实例化之前定义全局混入
Vue.mixin({
  mounted() {
    // 使用 nextTick 确保 DOM 完全渲染后再操作
    this.$nextTick(() => {
      // 检查 $el 是否存在，避免访问 null
      if (!this.$el) {
        return;
      }

      // 在Vue实例化之前定义全局混入
      if ((this.$options.name || this.$options._componentTag) == 'ElInput') {
        if (this.type == 'textarea') {
          try {
            var icon = document.createElement('i');
            //点击图标的时候，唤起ai弹框
            icon.addEventListener('click', (e) => {
              //唤起弹框
              this.$AiDialog({ dom: this, source: 'textarea' })
            });
            icon.textContent = 'Ai';
            icon.className = 'el-input__count textarea_ai'; // 设置图标样式，这里使用了Font Awesome图标库中的star图标
            // 将图标元素添加到目标DOM元素中
            this.$el.append(icon);
          } catch (error) {
            console.error('添加 AI 图标时出错:', error);
          }
        }
      }
    });

    //防抖
    // if (time !== null) {
    //   clearTimeout(time);
    // }
    // time = setTimeout(() => {
    //   // 获取页面上所有的输入框元素
    //   const inputElements = document.querySelectorAll('textarea');
    //   // 遍历输入框元素数组，为每个文本域框添加焦点事件监听器
    //   inputElements.forEach((input) => {

    //     var icon = document.createElement('i');
    //     //点击图标的时候，唤起ai弹框
    //     icon.addEventListener('click', (e) => {
    //       //唤起弹框
    //       this.$AiDialog()
    //     });
    //     icon.textContent = 'Ai';
    //     icon.className = 'el-input__count textarea_ai'; // 设置图标样式，这里使用了Font Awesome图标库中的star图标
    //     // 将图标元素添加到目标DOM元素中
    //     input.insertAdjacentElement('afterend', icon);
    //     input.addEventListener('click', (e) => {
    //       const blurPre = e.srcElement.selectionStart;
    //       const blurNext = e.srcElement.selectionEnd;
    //       e.target.value = e.target.value.substring(0, blurPre)
    //         + 11111
    //         + e.target.value.substring(blurNext, e.target.value.length);
    //       // 将光标移到新插入的字符串后面
    //       e.srcElement.selectionStart = blurPre + 5;
    //       e.srcElement.selectionEnd = blurPre + 5;
    //       // e.focus();
    //     });
    //   });
    // }, 500)
  }
});


console.log('测试自动更新1111111222222')



// 创建派发消息的Vue实例
// Vue.prototype.$bus = new Vue()
Vue.config.productionTip = false
new Vue({
  el: '#app',
  i18n,
  store,
  router,
  render: (h) => h(App)
})
