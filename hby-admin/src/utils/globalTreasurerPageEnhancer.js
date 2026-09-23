/**
 * 全球司库系统页面增强工具
 * 用于批量为页面添加错误处理和模拟数据功能
 */

import fs from 'fs'
import path from 'path'

/**
 * 为Vue页面添加混入和模拟数据支持
 * @param {string} filePath - Vue文件路径
 * @param {string} mockDataKey - 模拟数据生成器的键名
 */
export function enhanceVuePage(filePath, mockDataKey) {
  try {
    let content = fs.readFileSync(filePath, 'utf8')
    
    // 检查是否已经添加了混入
    if (content.includes('globalTreasurerMixin')) {
      console.log(`${filePath} 已经包含混入，跳过处理`)
      return
    }
    
    // 添加导入语句
    const importRegex = /(<script>\s*\n)(import.*from.*\n)*/
    const importReplacement = `$1import globalTreasurerMixin from '@/mixins/globalTreasurerMixin'
import { mockDataGenerators } from '@/utils/mockData/globalTreasurerMockData'

$2`
    
    content = content.replace(importRegex, importReplacement)
    
    // 添加混入到export default
    const exportRegex = /(export default \{[\s\n]*)/
    const exportReplacement = `$1  mixins: [globalTreasurerMixin],
`
    
    content = content.replace(exportRegex, exportReplacement)
    
    // 替换API调用为带错误处理的调用
    content = enhanceApiCalls(content, mockDataKey)
    
    // 写回文件
    fs.writeFileSync(filePath, content, 'utf8')
    console.log(`已增强页面: ${filePath}`)
    
  } catch (error) {
    console.error(`增强页面失败 ${filePath}:`, error.message)
  }
}

/**
 * 增强API调用，添加错误处理和模拟数据回退
 * @param {string} content - 文件内容
 * @param {string} mockDataKey - 模拟数据键名
 * @returns {string} 增强后的内容
 */
function enhanceApiCalls(content, mockDataKey) {
  // 常见的API调用模式
  const patterns = [
    // 分页查询
    {
      regex: /const response = await (\w+)\(([^)]*)\)/g,
      replacement: `const response = await this.callApiWithFallback(
          () => $1($2),
          mockDataGenerators.${mockDataKey},
          '获取数据失败'
        )`
    },
    // 简单查询
    {
      regex: /(\w+)\(([^)]*)\)\.then\(response => \{/g,
      replacement: `this.callApiWithFallback(
          () => $1($2),
          mockDataGenerators.${mockDataKey},
          '操作失败'
        ).then(response => {`
    }
  ]
  
  patterns.forEach(pattern => {
    content = content.replace(pattern.regex, pattern.replacement)
  })
  
  return content
}

/**
 * 批量增强全球司库系统的所有页面
 */
export function enhanceAllGlobalTreasurerPages() {
  const pageConfigs = [
    // 现金管理
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/xjgl/fkgl.vue', mockKey: 'paymentManagement' },
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/xjgl/skgl.vue', mockKey: 'receiptManagement' },
    
    // 账户管理
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/zhgl/zhxx.vue', mockKey: 'accountManagement' },
    
    // 资金计划
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/zjjh/zjjh.vue', mockKey: 'fundPlanning' },
    
    // 投资理财
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/tzlc/tzcp.vue', mockKey: 'investmentManagement' },
    
    // 风险管理
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/fxgl/fxgl.vue', mockKey: 'riskManagement' },
    
    // 衍生品
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/yspx/yspx.vue', mockKey: 'derivativesManagement' },
    
    // 票证管理
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/pzgl/index.vue', mockKey: 'billManagement' },
    
    // 资金归集
    { path: 'hbyun-pre/hby-admin/src/views/globalTreasurer/zjjz/index.vue', mockKey: 'fundConcentration' }
  ]
  
  pageConfigs.forEach(config => {
    if (fs.existsSync(config.path)) {
      enhanceVuePage(config.path, config.mockKey)
    } else {
      console.warn(`页面文件不存在: ${config.path}`)
    }
  })
}

/**
 * 为页面添加错误边界组件
 * @param {string} content - 页面内容
 * @returns {string} 添加错误边界后的内容
 */
export function addErrorBoundary(content) {
  // 在template的根div中添加错误处理
  const templateRegex = /(<template>\s*<div[^>]*>)/
  const templateReplacement = `$1
    <!-- 错误提示 -->
    <div v-if="useMockData" class="mock-data-notice">
      <el-alert
        title="当前使用模拟数据"
        type="warning"
        description="检测到网络连接问题，当前显示的是模拟数据，请检查网络连接后刷新页面"
        show-icon
        :closable="false"
        style="margin-bottom: 20px">
      </el-alert>
    </div>`
  
  return content.replace(templateRegex, templateReplacement)
}

/**
 * 添加加载状态和空数据处理
 * @param {string} content - 页面内容
 * @returns {string} 增强后的内容
 */
export function addLoadingAndEmptyStates(content) {
  // 为表格添加加载状态
  const tableRegex = /(<el-table[^>]*>)/g
  const tableReplacement = `$1
      v-loading="loading"
      element-loading-text="数据加载中..."
      element-loading-spinner="el-icon-loading"`
  
  content = content.replace(tableRegex, tableReplacement)
  
  // 添加空数据状态
  const emptyRegex = /(<el-table[^>]*>[\s\S]*?)(<\/el-table>)/g
  const emptyReplacement = `$1
      <template slot="empty">
        <div class="empty-data">
          <i class="el-icon-document"></i>
          <p>暂无数据</p>
          <el-button type="text" @click="getList">重新加载</el-button>
        </div>
      </template>
    $2`
  
  return content.replace(emptyRegex, emptyReplacement)
}

export default {
  enhanceVuePage,
  enhanceAllGlobalTreasurerPages,
  addErrorBoundary,
  addLoadingAndEmptyStates
}
