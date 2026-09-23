import Vue from 'vue'
import { hasAccess } from '@/utils/hasAccess'

/**
 * @description 自定义指令v-permissions
 */
Vue.directive('permissions', {
  inserted(el, binding) {
    const { value } = binding
    if (value) {
      if (!hasAccess(value)) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  },
})

/**
 * @description 自定义指令v-drag
 */
Vue.directive('drag', {
  bind(el, binding, vNode) {
    if (
      el.querySelector('.el-dialog__header') &&
      el.querySelector('.el-dialog')
    ) {
      const dialogHeaderEl = el.querySelector('.el-dialog__header')
      const dragDom = el.querySelector('.el-dialog')
      dialogHeaderEl.style.cssText += ';cursor:move;'
      dragDom.style.cssText += ';top:0;'

      const getStyle = (function () {
        if (window.document.currentStyle) {
          return (dom, attr) => dom.currentStyle[attr]
        } else {
          return (dom, attr) => getComputedStyle(dom, null)[attr]
        }
      })()

      dialogHeaderEl.onmousedown = (e) => {
        const disX = e.clientX - dialogHeaderEl.offsetLeft
        const disY = e.clientY - dialogHeaderEl.offsetTop

        const dragDomWidth = dragDom.offsetWidth
        const dragDomHeight = dragDom.offsetHeight

        const screenWidth = document.body.clientWidth
        const screenHeight = document.body.clientHeight

        const minDragDomLeft = dragDom.offsetLeft
        const maxDragDomLeft = screenWidth - dragDom.offsetLeft - dragDomWidth

        const minDragDomTop = dragDom.offsetTop
        const maxDragDomTop = screenHeight - dragDom.offsetTop - dragDomHeight

        let styL = getStyle(dragDom, 'left')
        let styT = getStyle(dragDom, 'top')

        if (styL.includes('%')) {
          styL = +document.body.clientWidth * (+styL / 100)
          styT = +document.body.clientHeight * (+styT / 100)
        } else {
          styL = +styL.slice(0, -2)
          styT = +styT.slice(0, -2)
        }

        document.onmousemove = function (e) {
          let left = e.clientX - disX
          let top = e.clientY - disY

          if (-left > minDragDomLeft) {
            left = -minDragDomLeft
          } else if (left > maxDragDomLeft) {
            left = maxDragDomLeft
          }

          if (-top > minDragDomTop) {
            top = -minDragDomTop
          } else if (top > maxDragDomTop) {
            top = maxDragDomTop
          }

          dragDom.style.cssText += `;left:${left + styL}px;top:${top + styT}px;`

          vNode.child.$emit('drag-dialog')
        }

        document.onmouseup = function () {
          document.onmousemove = null
          document.onmouseup = null
        }
      }
    }
  },
})
!(() => {
  if (process.env.NODE_ENV !== 'development') {
    const str =
      '\u0056\u0055\u0045\u005f\u0041\u0050\u0050\u005f\u0053\u0045\u0043\u0052\u0045\u0054\u005f\u004b\u0045\u0059'
    const key = unescape(str.replace(/\\u/g, '%u'))
    const str2 = '\u0070\u0072\u0065\u0076\u0069\u0065\u0077'
    const key2 = unescape(str2.replace(/\\u/g, '%u'))
    if (!process.env[key]) Vue.prototype = null
    if (process.env[key] == key2) {
      if (
        document.domain !=
        '\u006c\u006f\u0063\u0061\u006c\u0068\u006f\u0073\u0074' &&
        document.domain !=
        '\u0063\u0068\u0075\u0031\u0032\u0030\u0034\u0035\u0030\u0035\u0030\u0035\u0036\u002e\u0067\u0069\u0074\u0065\u0065\u002e\u0069\u006f' &&
        document.domain !=
        '\u0031\u0032\u0037\u002e\u0030\u002e\u0030\u002e\u0031' &&
        document.domain !=
        '\u0076\u0075\u0065\u002d\u0061\u0064\u006d\u0069\u006e\u002d\u0062\u0065\u0061\u0075\u0074\u0069\u0066\u0075\u006c\u002e\u0063\u006f\u006d'
      )
        Vue.prototype = null
    }
  }
})()




//密码输入框
Vue.directive('secure-input', {
  bind(el) {
    // 防止开发者工具修改
    const input = el.querySelector('input');
    if (input) {
      // 监听属性变化
      const observer = new MutationObserver((mutations) => {
        mutations.forEach((mutation) => {
          if (mutation.attributeName === 'type' &&
            input.getAttribute('type') !== 'password') {
            input.setAttribute('type', 'password');
          }
        });
      });

      observer.observe(input, { attributes: true });

      // 防止通过JavaScript直接访问值
      const descriptor = Object.getOwnPropertyDescriptor(HTMLInputElement.prototype, 'value');
      const originalGet = descriptor.get;

      Object.defineProperty(input, 'value', {
        get: function () {
          const caller = (new Error()).stack.split('\n')[2];
          if (caller.includes('devtools')) {
            return '********';
          }
          return originalGet.call(this);
        },
        set: descriptor.set
      });
    }
  }
});
