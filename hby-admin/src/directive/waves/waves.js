import './waves.css'

const context = '@@wavesContext'

function isAvailableClick(el) {
  return el.tagName === 'BUTTON' || el.tagName === 'A' || el.tagName === 'INPUT' || (el.classList && el.classList.contains('waves-effect'))
}

function show(e, el) {
  // Disable right click
  if (e.button === 2) {
    return false
  }

  let tmp = []
  if (el.classList.contains('waves-circle')) {
    tmp.push('waves-circle')
    tmp.push('waves-float')
  } else {
    tmp.push('waves-float')
  }

  // Find .waves-color-xxx
  let hasColorClass = false
  for (let i = 0, len = el.classList.length; i < len; i++) {
    const cls = el.classList[i]
    if (cls.indexOf('waves-color-') !== -1) {
      tmp.push(cls)
      hasColorClass = true
      break
    }
  }

  if (!hasColorClass) {
    tmp.push('waves-color-classic')
  }

  // Remove previous effect
  let effectDiv = el.querySelector('.waves-ripple')
  if (effectDiv) {
    effectDiv.remove()
  }

  effectDiv = document.createElement('div')
  effectDiv.className = 'waves-ripple ' + tmp.join(' ')

  const pos = el.getBoundingClientRect()
  const relativeY = e.pageY - pos.top
  const relativeX = e.pageX - pos.left
  const scale = 'scale(' + ((el.clientWidth / 100) * 3) + ')'

  effectDiv.setAttribute('data-hold', Date.now())
  effectDiv.setAttribute('data-x', relativeX)
  effectDiv.setAttribute('data-y', relativeY)
  effectDiv.setAttribute('data-scale', scale)

  const cssText = 'top:' + relativeY + 'px;left:' + relativeX + 'px;transform:' + scale + ';opacity:1;transform-origin:50% 50%;'
  effectDiv.setAttribute('style', cssText)

  el.appendChild(effectDiv)

  return false
}

function hide(e, el) {
  const ripples = el.querySelectorAll('.waves-ripple')
  for (let i = 0, len = ripples.length; i < len; i++) {
    removeRipple(e, el, ripples[i])
  }
}

function removeRipple(e, el, ripple) {
  // Check if the ripple still exist
  if (!ripple) {
    return
  }

  ripple.classList.remove('waves-rippling')

  const relativeX = ripple.getAttribute('data-x')
  const relativeY = ripple.getAttribute('data-y')
  const scale = ripple.getAttribute('data-scale')

  const diff = Date.now() - Number(ripple.getAttribute('data-hold'))
  let delay = 350 - diff

  if (delay < 0) {
    delay = 0
  }

  setTimeout(() => {
    const style = 'top:' + relativeY + 'px;left:' + relativeX + 'px;transform:' + scale + ';opacity:0;'

    ripple.setAttribute('style', style)

    setTimeout(() => {
      try {
        el.removeChild(ripple)
      } catch (e) {
        return false
      }
    }, 350)
  }, delay)
}

function wrapperEvent(el, type) {
  return function(e) {
    if (isAvailableClick(el)) {
      if (type === 'mousedown' && e.button !== 2) {
        show(e, el)
      } else if (type === 'mouseup') {
        hide(e, el)
      }
    }
  }
}

function bindEvents(el) {
  const mousedown = wrapperEvent(el, 'mousedown')
  const mouseup = wrapperEvent(el, 'mouseup')

  el.addEventListener('mousedown', mousedown, false)
  el.addEventListener('mouseup', mouseup, false)
  el.addEventListener('mouseleave', mouseup, false)

  el[context] = {
    removeMouseDown: mousedown,
    removeMouseUp: mouseup
  }
}

function unbindEvents(el) {
  const ctx = el[context]
  if (!ctx) {
    return
  }

  el.removeEventListener('mousedown', ctx.removeMouseDown, false)
  el.removeEventListener('mouseup', ctx.removeMouseUp, false)
  el.removeEventListener('mouseleave', ctx.removeMouseUp, false)

  el[context] = null
  delete el[context]
}

export default {
  bind(el, binding) {
    el.classList.add('waves-effect')
    bindEvents(el)
  },
  unbind(el) {
    unbindEvents(el)
    el.classList.remove('waves-effect')
  }
}
