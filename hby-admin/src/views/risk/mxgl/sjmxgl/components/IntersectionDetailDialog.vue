<template>
  <!-- 🔥 独立的交集详情对话框组件 - 确保在最上层显示 -->
  <el-dialog
    title="交集分析详情"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :modal-append-to-body="true"
    :append-to-body="true"
    :z-index="10000"
    class="intersection-detail-dialog sjmxgl-dialog-scope intersection-detail-top-layer"
    custom-class="intersection-detail-dialog-wrapper intersection-detail-top-layer"
    :before-close="handleClose"
    @open="handleOpen"
    @opened="handleOpened"
    @close="handleDialogClose"
  >
    <div v-if="intersectionResult && intersectionResult.intersectionData" class="intersection-detail-content">
      <div class="detail-header">
        <el-descriptions :column="4" size="mini" border>
          <el-descriptions-item label="总记录数">
            {{ intersectionResult.intersectionData.length }}
          </el-descriptions-item>
          <el-descriptions-item label="分析ID">
            {{ intersectionResult.analysisId }}
          </el-descriptions-item>
          <el-descriptions-item label="分析时间">
            {{ intersectionResult.analysisTime }}
          </el-descriptions-item>
          <el-descriptions-item label="分析类型">
            {{ intersectionResult.intersectionType }}
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="detail-table" style="margin-top: 16px;">
        <el-table
          :data="processedIntersectionData"
          border
          size="mini"
          height="400"
          style="width: 100%"
        >
          <!-- 序号列 -->
          <el-table-column prop="序号" label="序号" width="60" align="center" fixed="left" />

          <!-- 交集键列 -->
          <el-table-column prop="intersectionKey" label="交集键" width="100" show-overflow-tooltip fixed="left" />

          <!-- 🔥 动态生成各个指标的查询结果字段列 -->
          <el-table-column
            v-for="column in dynamicColumns"
            :key="column.prop"
            :prop="column.prop"
            :label="column.label"
            :width="column.width"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span :style="getFieldValueStyle(column.prop, scope.row[column.prop])">
                {{ formatFieldValue(scope.row[column.prop]) }}
              </span>
            </template>
          </el-table-column>
        </el-table>

        <div v-if="intersectionResult.intersectionData.length > 100" style="text-align: center; margin-top: 10px; color: #909399; font-size: 12px;">
          注：仅显示前100条记录，总共{{ intersectionResult.intersectionData.length }}条记录
        </div>
      </div>
    </div>

    <div v-else class="no-data">
      <h3 style="color: #999; margin: 0 0 8px 0;">暂无交集分析数据</h3>
      <p style="color: #ccc; margin: 0;">请确保交集分析执行成功并生成了结果数据</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">关闭</el-button>
      <el-button type="primary" @click="exportToExcelLocal">
        <i class="el-icon-download"></i> 导出Excel
      </el-button>
      <!-- 🔥 隐藏后端导出按钮 -->
      <!-- <el-button type="success" @click="exportIntersectionResult">
        <i class="el-icon-upload2"></i> 导出完整数据 (接口)
      </el-button> -->
    </div>
  </el-dialog>
</template>

<script>
import { exportAnalysisResult } from '@/api/mxgl'

export default {
  name: 'IntersectionDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    intersectionResult: {
      type: Object,
      default: () => null
    }
  },
  data() {
    return {
      dialogVisible: false
    }
  },
  computed: {
    // 🔥 新增：动态生成交集详情表格列配置
    dynamicColumns() {
      if (!this.intersectionResult || !this.intersectionResult.intersectionData || this.intersectionResult.intersectionData.length === 0) {
        return []
      }

      const columns = []
      const allFields = new Set()

      // 🔥 通用方法：收集所有指标数据中的字段
      this.intersectionResult.intersectionData.forEach(row => {
        // 收集指标1数据的字段
        if (row.indicator1Data && typeof row.indicator1Data === 'object' && !row.indicator1Data.$ref) {
          Object.keys(row.indicator1Data).forEach(field => {
            allFields.add(`指标1_${field}`)
          })
        }

        // 收集指标2数据的字段
        if (row.indicator2Data && typeof row.indicator2Data === 'object' && !row.indicator2Data.$ref) {
          Object.keys(row.indicator2Data).forEach(field => {
            allFields.add(`指标2_${field}`)
          })
        }

        // 如果有更多指标，可以继续扩展
        if (row.indicator3Data && typeof row.indicator3Data === 'object' && !row.indicator3Data.$ref) {
          Object.keys(row.indicator3Data).forEach(field => {
            allFields.add(`指标3_${field}`)
          })
        }

        // 支持更多指标（通用扩展）
        Object.keys(row).forEach(key => {
          if (key.startsWith('indicator') && key.endsWith('Data') && key !== 'indicator1Data' && key !== 'indicator2Data' && key !== 'indicator3Data') {
            const indicatorNum = key.match(/indicator(\d+)Data/)?.[1]
            if (indicatorNum && row[key] && typeof row[key] === 'object' && !row[key].$ref) {
              Object.keys(row[key]).forEach(field => {
                allFields.add(`指标${indicatorNum}_${field}`)
              })
            }
          }
        })
      })

      // 🔥 生成列配置
      Array.from(allFields).sort().forEach(fieldKey => {
        const [indicatorPrefix, fieldName] = fieldKey.split('_')
        columns.push({
          prop: fieldKey,
          label: `${indicatorPrefix}(${fieldName})`,
          width: this.getColumnWidth(fieldName)
        })
      })

      return columns
    },

    // 🔥 新增：处理后的交集数据（扁平化处理）
    processedIntersectionData() {
      if (!this.intersectionResult || !this.intersectionResult.intersectionData || this.intersectionResult.intersectionData.length === 0) {
        return []
      }

      return this.intersectionResult.intersectionData.slice(0, 100).map((row, index) => {
        const processedRow = {
          序号: index + 1,
          intersectionKey: row.intersectionKey || row.交集键 || `交集${index + 1}`
        }

        // 🔥 通用方法：扁平化指标数据
        // 处理指标1数据
        if (row.indicator1Data && typeof row.indicator1Data === 'object' && !row.indicator1Data.$ref) {
          Object.keys(row.indicator1Data).forEach(field => {
            processedRow[`指标1_${field}`] = row.indicator1Data[field]
          })
        }

        // 处理指标2数据
        if (row.indicator2Data && typeof row.indicator2Data === 'object' && !row.indicator2Data.$ref) {
          Object.keys(row.indicator2Data).forEach(field => {
            processedRow[`指标2_${field}`] = row.indicator2Data[field]
          })
        }

        // 处理指标3数据（如果存在）
        if (row.indicator3Data && typeof row.indicator3Data === 'object' && !row.indicator3Data.$ref) {
          Object.keys(row.indicator3Data).forEach(field => {
            processedRow[`指标3_${field}`] = row.indicator3Data[field]
          })
        }

        // 支持更多指标（通用扩展）
        Object.keys(row).forEach(key => {
          if (key.startsWith('indicator') && key.endsWith('Data') && key !== 'indicator1Data' && key !== 'indicator2Data' && key !== 'indicator3Data') {
            const indicatorNum = key.match(/indicator(\d+)Data/)?.[1]
            if (indicatorNum && row[key] && typeof row[key] === 'object' && !row[key].$ref) {
              Object.keys(row[key]).forEach(field => {
                processedRow[`指标${indicatorNum}_${field}`] = row[key][field]
              })
            }
          }
        })

        return processedRow
      })
    }
  },
  watch: {
    visible: {
      immediate: true,
      handler(newVal) {
        this.dialogVisible = newVal
      }
    },
    dialogVisible(newVal) {
      this.$emit('update:visible', newVal)
    }
  },
  methods: {
    // 🔥 对话框打开时的处理
    handleOpen() {
      console.log('🔍 交集详情对话框开始打开')
    },

    // 🔥 对话框完全打开后的处理
    handleOpened() {
      console.log('✅ 交集详情对话框完全打开')
      this.ensureDialogOnTop()
    },

    // 🔥 确保对话框在最上层
    ensureDialogOnTop() {
      try {
        console.log('🔝 确保交集详情对话框在最上层')

        this.$nextTick(() => {
          setTimeout(() => {
            // 方法1: 查找交集详情对话框的包装器
            const dialogWrapper = document.querySelector('.intersection-detail-dialog-wrapper')
            if (dialogWrapper) {
              // 设置极高的z-index确保在最上层
              dialogWrapper.style.setProperty('z-index', '10000', 'important')
              dialogWrapper.style.setProperty('position', 'fixed', 'important')
              dialogWrapper.style.setProperty('top', '0', 'important')
              dialogWrapper.style.setProperty('left', '0', 'important')
              dialogWrapper.style.setProperty('width', '100%', 'important')
              dialogWrapper.style.setProperty('height', '100%', 'important')
              console.log('✅ 设置交集详情对话框包装器 z-index: 10000')

              // 同时设置对话框本身的z-index
              const dialog = dialogWrapper.querySelector('.el-dialog')
              if (dialog) {
                dialog.style.setProperty('z-index', '10001', 'important')
                dialog.style.setProperty('position', 'relative', 'important')
                console.log('✅ 设置交集详情对话框 z-index: 10001')
              }

              // 设置遮罩层的z-index
              const modal = document.querySelector('.v-modal:last-child')
              if (modal) {
                modal.style.setProperty('z-index', '9999', 'important')
                console.log('✅ 设置遮罩层 z-index: 9999')
              }

              console.log('✅ 交集详情对话框层级设置完成')
            } else {
              console.warn('⚠️ 未找到交集详情对话框包装器，使用备用方案')
              this.setDialogZIndexByTitle()
            }

            // 方法2: 强制设置所有相关的对话框元素
            this.forceSetDialogZIndex()
          }, 100)
        })
      } catch (error) {
        console.error('❌ 设置交集详情对话框层级失败:', error)
      }
    },

    // 🔥 新增：强制设置对话框层级
    forceSetDialogZIndex() {
      try {
        // 查找所有可能的交集详情对话框元素
        const selectors = [
          '.intersection-detail-dialog-wrapper',
          '.intersection-detail-top-layer',
          '.el-dialog__wrapper[aria-label*="交集分析详情"]'
        ]

        selectors.forEach(selector => {
          const elements = document.querySelectorAll(selector)
          elements.forEach((element, index) => {
            element.style.setProperty('z-index', '10000', 'important')
            element.style.setProperty('position', 'fixed', 'important')
            element.style.setProperty('top', '0', 'important')
            element.style.setProperty('left', '0', 'important')
            element.style.setProperty('width', '100%', 'important')
            element.style.setProperty('height', '100%', 'important')
            console.log(`✅ 强制设置元素 ${selector}[${index}] z-index: 10000`)

            // 设置内部对话框
            const dialog = element.querySelector('.el-dialog')
            if (dialog) {
              dialog.style.setProperty('z-index', '10001', 'important')
              dialog.style.setProperty('position', 'relative', 'important')
            }
          })
        })
      } catch (error) {
        console.error('❌ 强制设置对话框层级失败:', error)
      }
    },

    // 🔥 备用方案：通过标题查找对话框
    setDialogZIndexByTitle() {
      const allWrappers = document.querySelectorAll('.el-dialog__wrapper')
      allWrappers.forEach((wrapper, index) => {
        const title = wrapper.querySelector('.el-dialog__title')
        if (title && title.textContent.includes('交集分析详情')) {
          wrapper.style.setProperty('z-index', '10000', 'important')
          wrapper.style.setProperty('position', 'fixed', 'important')
          wrapper.style.setProperty('top', '0', 'important')
          wrapper.style.setProperty('left', '0', 'important')
          wrapper.style.setProperty('width', '100%', 'important')
          wrapper.style.setProperty('height', '100%', 'important')

          const dialog = wrapper.querySelector('.el-dialog')
          if (dialog) {
            dialog.style.setProperty('z-index', '10001', 'important')
            dialog.style.setProperty('position', 'relative', 'important')
          }
          console.log('✅ 使用备用方案设置交集详情对话框层级')
        }
      })
    },

    // 🔥 对话框关闭前的处理
    handleClose(done) {
      console.log('🔒 关闭交集详情对话框')
      this.dialogVisible = false
      done()
    },

    // 🔥 对话框关闭后的处理
    handleDialogClose() {
      console.log('✅ 交集详情对话框完全关闭')
      this.$emit('close')
    },

    // 🔥 关闭对话框
    closeDialog() {
      this.dialogVisible = false
    },

    // 🔥 导出交集结果
    async exportIntersectionResult() {
      if (!this.intersectionResult || !this.intersectionResult.analysisId) {
        this.$message.warning('没有可导出的交集分析结果')
        return
      }

      try {
        this.$message.info('正在准备导出文件...')

        // 🔥 修复：使用后端期望的参数字段名
        const exportData = {
          resultId: this.intersectionResult.analysisId,  // 修复：使用 resultId 而不是 analysisId
          format: 'EXCEL',
          fileName: `交集分析结果_${this.intersectionResult.analysisId}_${new Date().getTime()}`  // 添加文件名
        }

        console.log('📤 导出参数:', exportData)
        console.log('📊 交集结果数据:', this.intersectionResult)

        const response = await exportAnalysisResult(exportData)

        console.log('📥 导出响应:', response)

        if (response && response.code === 1) {
          // 🔥 处理下载链接
          if (response.data && response.data.downloadUrl) {
            // 创建下载链接
            const link = document.createElement('a')
            link.href = response.data.downloadUrl
            link.download = response.data.fileName || `交集分析结果_${this.intersectionResult.analysisId}.xlsx`
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)

            this.$message.success('导出成功！文件已开始下载')
          } else {
            this.$message.success('导出成功！')
          }
        } else {
          this.$message.error(response?.msg || '导出失败')
        }
      } catch (error) {
        console.error('❌ 导出交集结果失败:', error)

        // 🔥 增强错误处理
        if (error.response && error.response.data) {
          const errorMsg = error.response.data.msg || error.response.data.message || '导出失败'
          this.$message.error(`导出失败: ${errorMsg}`)
        } else {
          this.$message.error('导出失败: ' + (error.message || '网络错误'))
        }
      }
    },

    // 🔥 新增：获取列宽度（通用方法）
    getColumnWidth(fieldName) {
      // 特殊字段的固定宽度
      const fixedWidthMap = {
        '交集键': 100,
        'intersectionKey': 100,
        '序号': 60
      }

      if (fixedWidthMap[fieldName]) {
        return fixedWidthMap[fieldName]
      }

      // 根据字段名称智能判断宽度
      const fieldLower = fieldName.toLowerCase()

      if (fieldLower.includes('id') || fieldLower.includes('code')) {
        return 120
      } else if (fieldLower.includes('name') || fieldLower.includes('title')) {
        return 180
      } else if (fieldLower.includes('time') || fieldLower.includes('date')) {
        return 160
      } else if (fieldLower.includes('amount') || fieldLower.includes('money') || fieldLower.includes('price')) {
        return 120
      } else if (fieldLower.includes('desc') || fieldLower.includes('remark') || fieldLower.includes('note')) {
        return 200
      } else if (fieldLower.includes('status') || fieldLower.includes('state')) {
        return 100
      } else if (fieldLower.includes('type') || fieldLower.includes('category')) {
        return 120
      } else if (fieldLower.includes('url') || fieldLower.includes('link')) {
        return 200
      } else if (fieldLower.includes('email') || fieldLower.includes('phone')) {
        return 150
      } else {
        // 默认宽度
        return 120
      }
    },

    // 🔥 新增：获取字段值样式（通用方法）
    getFieldValueStyle(prop, value) {
      if (value === null || value === undefined || value === '') {
        return { color: '#ccc', fontStyle: 'italic' }
      }

      if (typeof value === 'number') {
        if (value < 0) {
          return { color: '#f56c6c', fontWeight: '500' } // 负数用红色
        } else if (value > 0) {
          return { color: '#67c23a', fontWeight: '500' } // 正数用绿色
        } else {
          return { color: '#909399', fontWeight: '500' } // 零用灰色
        }
      }

      if (typeof value === 'boolean') {
        return {
          color: value ? '#67c23a' : '#f56c6c',
          fontWeight: '500'
        }
      }

      // 根据字段名称设置特殊样式
      const propLower = prop.toLowerCase()
      if (propLower.includes('status') || propLower.includes('state')) {
        if (String(value).includes('成功') || String(value).includes('正常') || String(value).includes('active')) {
          return { color: '#67c23a', fontWeight: '500' }
        } else if (String(value).includes('失败') || String(value).includes('错误') || String(value).includes('error')) {
          return { color: '#f56c6c', fontWeight: '500' }
        }
      }

      if (propLower.includes('amount') || propLower.includes('money') || propLower.includes('price')) {
        return { color: '#409eff', fontWeight: '500', textAlign: 'right' }
      }

      return {}
    },

    // 🔥 新增：格式化字段值（通用方法）
    formatFieldValue(value) {
      if (value === null || value === undefined) {
        return '(空)'
      }

      if (value === '') {
        return '(空字符串)'
      }

      if (typeof value === 'boolean') {
        return value ? '是' : '否'
      }

      if (typeof value === 'number') {
        // 如果是金额相关字段，格式化为货币格式
        if (Math.abs(value) > 1000) {
          return value.toLocaleString()
        }
        return String(value)
      }

      if (typeof value === 'object') {
        // 不再显示JSON字符串，而是显示对象类型提示
        if (Array.isArray(value)) {
          return `[数组:${value.length}项]`
        } else {
          return `[对象:${Object.keys(value).length}个字段]`
        }
      }

      // 字符串长度限制
      const strValue = String(value)
      if (strValue.length > 50) {
        return strValue.substring(0, 47) + '...'
      }

      return strValue
    },

    // 🔥 新增：本地导出Excel功能
    async exportToExcelLocal() {
      try {
        console.log('📊 开始本地导出交集分析Excel')

        if (!this.intersectionResult || !this.intersectionResult.intersectionData || this.intersectionResult.intersectionData.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }

        // 🔥 动态导入xlsx库
        const XLSX = await import('xlsx')
        const FileSaver = await import('file-saver')

        // 🔥 使用当前组件已经处理好的数据
        const exportData = this.processedIntersectionData
        const columns = this.dynamicColumns

        console.log('📋 导出数据量:', exportData.length)
        console.log('📋 导出列数:', columns.length)

        // 🔥 创建Excel工作簿
        const workbook = this.createExcelWorkbook(XLSX, exportData, columns)

        // 🔥 生成文件名
        const fileName = `交集分析结果_${this.intersectionResult.analysisId}_${new Date().getTime()}.xlsx`

        // 🔥 下载文件
        this.downloadExcelFile(XLSX, FileSaver, workbook, fileName)

        this.$message.success('Excel导出成功！')

      } catch (error) {
        console.error('❌ 本地导出Excel失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      }
    },

    // 🔥 创建Excel工作簿
    createExcelWorkbook(XLSX, data, columns) {
      // 🔥 准备表头
      const headers = ['序号', '交集键', ...columns.map(col => col.label)]

      // 🔥 准备数据行
      const rows = data.map(row => {
        return [
          row.序号,
          row.intersectionKey || '',
          ...columns.map(col => {
            const value = row[col.prop]
            if (value === null || value === undefined) {
              return ''
            }
            return this.formatExcelValue(value)
          })
        ]
      })

      // 🔥 合并表头和数据
      const worksheetData = [headers, ...rows]

      // 🔥 创建工作表
      const worksheet = XLSX.utils.aoa_to_sheet(worksheetData)

      // 🔥 设置列宽
      const colWidths = headers.map((header, index) => {
        if (index === 0) return { wch: 8 }  // 序号列
        if (index === 1) return { wch: 15 } // 交集键列
        return { wch: 20 } // 其他列
      })
      worksheet['!cols'] = colWidths

      // 🔥 创建工作簿
      const workbook = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(workbook, worksheet, '交集分析结果')

      return workbook
    },

    // 🔥 格式化Excel值
    formatExcelValue(value) {
      if (typeof value === 'object') {
        if (Array.isArray(value)) {
          return `[数组:${value.length}项]`
        } else {
          return `[对象:${Object.keys(value).length}个字段]`
        }
      }
      return String(value)
    },

    // 🔥 下载Excel文件
    downloadExcelFile(XLSX, FileSaver, workbook, fileName) {
      // 🔥 生成Excel文件
      const excelBuffer = XLSX.write(workbook, {
        bookType: 'xlsx',
        type: 'array'
      })

      // 🔥 创建Blob对象
      const blob = new Blob([excelBuffer], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })

      // 🔥 使用FileSaver下载
      FileSaver.saveAs(blob, fileName)

      console.log('✅ Excel文件下载完成:', fileName)
    }
  }
}
</script>

<style scoped>
/* 🔥 交集详情对话框样式 */
.intersection-detail-content {
  padding: 0;
}

.intersection-detail-content .detail-header {
  margin-bottom: 16px;
}

.intersection-detail-content .detail-table {
  border: 1px solid #ebeef5;
  border-radius: 4px;
}

.intersection-detail-content .el-table {
  border: none;
}

.intersection-detail-content .el-table th {
  background-color: #f5f7fa;
  font-weight: 600;
}

.intersection-detail-content .el-table td {
  padding: 8px 12px;
}

.no-data {
  text-align: center;
  padding: 40px 20px;
}

.dialog-footer {
  text-align: right;
}

.dialog-footer .el-button {
  margin-left: 8px;
}

/* 🔥 确保对话框在最上层 - 使用极高的z-index */
::v-deep .intersection-detail-dialog-wrapper {
  z-index: 10000 !important;
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
}

::v-deep .intersection-detail-dialog-wrapper .el-dialog {
  z-index: 10001 !important;
  position: relative !important;
}

/* 🔥 新增：交集详情对话框顶层样式类 */
::v-deep .intersection-detail-top-layer {
  z-index: 10000 !important;
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
}

::v-deep .intersection-detail-top-layer .el-dialog {
  z-index: 10001 !important;
  position: relative !important;
}

/* 🔥 新增：强制设置对话框包装器样式 */
::v-deep .el-dialog__wrapper.intersection-detail-dialog-wrapper {
  z-index: 10000 !important;
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
}

/* 🔥 新增：强制设置对话框本身样式 */
::v-deep .intersection-detail-dialog-wrapper .el-dialog {
  z-index: 10001 !important;
  position: relative !important;
}

/* 🔥 新增：确保遮罩层正确显示 */
::v-deep .intersection-detail-dialog-wrapper + .v-modal,
::v-deep .intersection-detail-top-layer + .v-modal {
  z-index: 9999 !important;
  background-color: rgba(0, 0, 0, 0.5) !important;
}
</style>
