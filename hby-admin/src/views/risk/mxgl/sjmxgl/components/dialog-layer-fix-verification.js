/**
 * 交集分析详情对话框层级修复验证脚本
 * 在浏览器控制台中运行此脚本来验证修复是否有效
 */

// 验证函数
function verifyIntersectionDialogFix() {
  console.log('🔍 开始验证交集分析详情对话框层级修复...')
  
  // 1. 检查CSS文件是否正确加载
  const cssLoaded = Array.from(document.styleSheets).some(sheet => {
    try {
      return Array.from(sheet.cssRules || []).some(rule => 
        rule.selectorText && rule.selectorText.includes('intersection-detail-dialog-wrapper')
      )
    } catch (e) {
      return false
    }
  })
  
  console.log(cssLoaded ? '✅ CSS样式已正确加载' : '❌ CSS样式未找到')
  
  // 2. 检查页面作用域类名
  const scopeElement = document.querySelector('.risk-mxgl-sjmxgl-page')
  console.log(scopeElement ? '✅ 页面作用域类名存在' : '❌ 页面作用域类名不存在')
  
  // 3. 检查执行对话框
  const executionDialog = document.querySelector('.execution-dialog-wrapper')
  if (executionDialog) {
    const executionZIndex = window.getComputedStyle(executionDialog).zIndex
    console.log(`✅ 执行对话框存在，z-index: ${executionZIndex}`)
  } else {
    console.log('ℹ️ 执行对话框当前未显示')
  }
  
  // 4. 检查交集详情对话框
  const intersectionDialog = document.querySelector('.intersection-detail-dialog-wrapper')
  if (intersectionDialog) {
    const intersectionZIndex = window.getComputedStyle(intersectionDialog).zIndex
    const position = window.getComputedStyle(intersectionDialog).position
    console.log(`✅ 交集详情对话框存在，z-index: ${intersectionZIndex}, position: ${position}`)
    
    // 检查是否在最上层
    if (parseInt(intersectionZIndex) >= 10000) {
      console.log('✅ 交集详情对话框z-index正确 (>=10000)')
    } else {
      console.log('❌ 交集详情对话框z-index过低 (<10000)')
    }
  } else {
    console.log('ℹ️ 交集详情对话框当前未显示')
  }
  
  // 5. 检查所有对话框的层级关系
  const allDialogs = document.querySelectorAll('.el-dialog__wrapper')
  const dialogInfo = Array.from(allDialogs).map(dialog => {
    const zIndex = window.getComputedStyle(dialog).zIndex
    const title = dialog.querySelector('.el-dialog__title')?.textContent || '未知'
    return { title, zIndex: parseInt(zIndex) || 0, element: dialog }
  }).sort((a, b) => b.zIndex - a.zIndex)
  
  console.log('📊 当前所有对话框层级排序:')
  dialogInfo.forEach((info, index) => {
    console.log(`${index + 1}. ${info.title}: z-index ${info.zIndex}`)
  })
  
  // 6. 提供修复建议
  console.log('\n🔧 修复验证结果:')
  if (intersectionDialog && parseInt(window.getComputedStyle(intersectionDialog).zIndex) >= 10000) {
    console.log('✅ 交集详情对话框层级修复成功！')
  } else if (intersectionDialog) {
    console.log('⚠️ 交集详情对话框存在但层级不正确，请检查CSS加载')
  } else {
    console.log('ℹ️ 请先打开交集详情对话框再进行验证')
  }
  
  return {
    cssLoaded,
    scopeElement: !!scopeElement,
    executionDialog: !!executionDialog,
    intersectionDialog: !!intersectionDialog,
    dialogInfo
  }
}

// 自动运行验证
if (typeof window !== 'undefined') {
  // 等待页面加载完成后运行
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', verifyIntersectionDialogFix)
  } else {
    verifyIntersectionDialogFix()
  }
}

// 导出验证函数供手动调用
window.verifyIntersectionDialogFix = verifyIntersectionDialogFix

console.log('📝 验证脚本已加载，可以在控制台中调用 verifyIntersectionDialogFix() 进行验证')
