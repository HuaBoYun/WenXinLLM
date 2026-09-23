<template>
  <div class="sql-visual-builder">
    <div class="builder-layout">
      <!-- 左侧：数据源和表结构 -->
      <div class="left-panel">
        <div class="panel-header">
          <h4><i class="el-icon-folder-opened"></i> 数据表</h4>
        </div>
        
        <div class="table-list" v-loading="tablesLoading">
          <div v-if="!dataSourceId" class="empty-state">
            <p>请先选择数据源</p>
          </div>
          
          <div v-else-if="tableList.length === 0" class="empty-state">
            <p>暂无数据表</p>
          </div>
          
          <div v-else>
            <div
              v-for="table in tableList"
              :key="table.tableName"
              class="table-item"
              @click="selectTable(table)"
              :class="{ active: selectedTable === table.tableName }"
            >
              <div class="table-header">
                <i class="el-icon-s-grid"></i>
                <span class="table-name">{{ table.tableComment || table.tableName }}</span>
                <el-button
                  type="text"
                  size="mini"
                  @click.stop="addTableToQuery(table)"
                >
                  添加
                </el-button>
              </div>
              <div class="table-meta">
                {{ table.tableName }}
              </div>
            </div>
          </div>
        </div>

        <!-- 字段列表 -->
        <div v-if="selectedTable" class="field-list">
          <div class="panel-header">
            <h4><i class="el-icon-menu"></i> 字段列表</h4>
          </div>
          
          <div class="fields" v-loading="fieldsLoading">
            <div
              v-for="field in fieldList"
              :key="field.columnName"
              class="field-item"
              draggable="true"
              @dragstart="handleFieldDragStart($event, field)"
              @click="addFieldToSelect(field)"
            >
              <div class="field-info">
                <i :class="getFieldIcon(field.columnType)"></i>
                <span class="field-name">{{ field.columnComment || field.columnName }}</span>
                <el-tag size="mini" :type="getFieldTypeColor(field.columnType)">
                  {{ field.columnType }}
                </el-tag>
              </div>
              <div class="field-meta">
                {{ field.columnName }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：SQL构建器 -->
      <div class="right-panel">
        <!-- SELECT 字段 -->
        <div class="sql-section">
          <div class="section-header">
            <h4>SELECT 字段</h4>
            <el-button type="text" size="mini" @click="selectAllFields">全选</el-button>
          </div>
          
          <div class="select-fields">
            <div
              v-for="(field, index) in queryBuilder.selectFields"
              :key="index"
              class="selected-field"
            >
              <span>{{ field.tableName }}.{{ field.fieldName }}</span>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-close"
                @click="removeSelectField(index)"
              />
            </div>
            
            <div
              v-if="queryBuilder.selectFields.length === 0"
              class="drop-zone"
              @drop="handleSelectDrop"
              @dragover.prevent
              @dragenter.prevent
            >
              拖拽字段到此处或点击字段添加
            </div>
          </div>
        </div>

        <!-- FROM 表 -->
        <div class="sql-section">
          <div class="section-header">
            <h4>FROM 表</h4>
          </div>
          
          <div class="from-tables">
            <div
              v-for="(table, index) in queryBuilder.fromTables"
              :key="index"
              class="selected-table"
            >
              <span>{{ table.tableComment || table.tableName }}</span>
              <span class="table-alias" v-if="table.alias">AS {{ table.alias }}</span>
              <el-button
                type="text"
                size="mini"
                icon="el-icon-close"
                @click="removeFromTable(index)"
              />
            </div>
            
            <div
              v-if="queryBuilder.fromTables.length === 0"
              class="drop-zone"
              @drop="handleTableDrop"
              @dragover.prevent
              @dragenter.prevent
            >
              拖拽表到此处或点击表添加
            </div>
          </div>
        </div>

        <!-- WHERE 条件 -->
        <div class="sql-section">
          <div class="section-header">
            <h4>WHERE 条件</h4>
            <el-button type="text" size="mini" @click="addWhereCondition">
              <i class="el-icon-plus"></i> 添加条件
            </el-button>
          </div>
          
          <div class="where-conditions">
            <div
              v-for="(condition, index) in queryBuilder.whereConditions"
              :key="index"
              class="condition-item"
            >
              <el-select v-model="condition.field" placeholder="字段" size="mini">
                <el-option
                  v-for="field in availableFields"
                  :key="field.value"
                  :label="field.label"
                  :value="field.value"
                />
              </el-select>
              
              <el-select v-model="condition.operator" placeholder="操作符" size="mini">
                <el-option label="等于" value="=" />
                <el-option label="不等于" value="!=" />
                <el-option label="大于" value=">" />
                <el-option label="小于" value="<" />
                <el-option label="大于等于" value=">=" />
                <el-option label="小于等于" value="<=" />
                <el-option label="包含" value="LIKE" />
                <el-option label="在范围内" value="IN" />
                <el-option label="为空" value="IS NULL" />
                <el-option label="不为空" value="IS NOT NULL" />
              </el-select>
              
              <el-input
                v-if="!['IS NULL', 'IS NOT NULL'].includes(condition.operator)"
                v-model="condition.value"
                placeholder="值"
                size="mini"
              />
              
              <el-select
                v-if="index > 0"
                v-model="condition.logic"
                size="mini"
                style="width: 80px;"
              >
                <el-option label="AND" value="AND" />
                <el-option label="OR" value="OR" />
              </el-select>
              
              <el-button
                type="text"
                size="mini"
                icon="el-icon-close"
                @click="removeWhereCondition(index)"
              />
            </div>
          </div>
        </div>

        <!-- ORDER BY 排序 -->
        <div class="sql-section">
          <div class="section-header">
            <h4>ORDER BY 排序</h4>
            <el-button type="text" size="mini" @click="addOrderBy">
              <i class="el-icon-plus"></i> 添加排序
            </el-button>
          </div>
          
          <div class="order-by-list">
            <div
              v-for="(order, index) in queryBuilder.orderBy"
              :key="index"
              class="order-item"
            >
              <el-select v-model="order.field" placeholder="字段" size="mini">
                <el-option
                  v-for="field in availableFields"
                  :key="field.value"
                  :label="field.label"
                  :value="field.value"
                />
              </el-select>
              
              <el-select v-model="order.direction" size="mini" style="width: 100px;">
                <el-option label="升序" value="ASC" />
                <el-option label="降序" value="DESC" />
              </el-select>
              
              <el-button
                type="text"
                size="mini"
                icon="el-icon-close"
                @click="removeOrderBy(index)"
              />
            </div>
          </div>
        </div>

        <!-- 生成的SQL预览 -->
        <div class="sql-section">
          <div class="section-header">
            <h4>生成的SQL</h4>
            <el-button type="text" size="mini" @click="copySQL">
              <i class="el-icon-document-copy"></i> 复制
            </el-button>
          </div>
          
          <div class="sql-preview">
            <pre>{{ generatedSQL }}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getTableList, getTableFields } from '@/api/mxgl'

export default {
  name: 'SqlVisualBuilder',
  props: {
    dataSourceId: {
      type: String,
      default: ''
    },
    sqlContent: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 数据表相关
      tableList: [],
      tablesLoading: false,
      selectedTable: '',
      fieldList: [],
      fieldsLoading: false,
      
      // SQL构建器
      queryBuilder: {
        selectFields: [],
        fromTables: [],
        whereConditions: [],
        orderBy: []
      }
    }
  },
  computed: {
    // 可用字段列表（用于下拉选择）
    availableFields() {
      const fields = []
      this.queryBuilder.fromTables.forEach(table => {
        // 这里应该根据表名获取字段，暂时使用当前选中表的字段
        this.fieldList.forEach(field => {
          fields.push({
            label: `${table.tableName}.${field.columnComment || field.columnName}`,
            value: `${table.tableName}.${field.columnName}`
          })
        })
      })
      return fields
    },
    
    // 生成的SQL语句
    generatedSQL() {
      return this.buildSQL()
    }
  },
  watch: {
    dataSourceId: {
      handler(val) {
        if (val) {
          this.loadTables()
        } else {
          this.tableList = []
          this.fieldList = []
          this.selectedTable = ''
        }
      },
      immediate: true
    },
    queryBuilder: {
      handler() {
        this.$emit('sql-change', this.generatedSQL)
      },
      deep: true
    }
  },
  methods: {
    // 加载数据表列表
    async loadTables() {
      if (!this.dataSourceId) return
      
      try {
        this.tablesLoading = true
        // 这里应该调用真实的API，暂时使用模拟数据
        this.tableList = [
          { tableName: 'TBL_ENTERPRISE_INFO', tableComment: '企业基本信息表' },
          { tableName: 'TBL_FINANCIAL_DATA', tableComment: '财务数据表' },
          { tableName: 'TBL_RISK_ASSESSMENT', tableComment: '风险评估表' },
          { tableName: 'TBL_AUDIT_PROJECT', tableComment: '审计项目表' }
        ]
      } catch (error) {
        console.error('加载数据表失败:', error)
      } finally {
        this.tablesLoading = false
      }
    },

    // 选择表
    selectTable(table) {
      this.selectedTable = table.tableName
      this.loadTableFields(table.tableName)
    },

    // 加载表字段
    async loadTableFields(tableName) {
      try {
        this.fieldsLoading = true
        // 这里应该调用真实的API，暂时使用模拟数据
        this.fieldList = [
          { columnName: 'ID', columnComment: '主键ID', columnType: 'VARCHAR' },
          { columnName: 'COMPANY_NAME', columnComment: '企业名称', columnType: 'VARCHAR' },
          { columnName: 'RISK_SCORE', columnComment: '风险评分', columnType: 'DECIMAL' },
          { columnName: 'CREATE_TIME', columnComment: '创建时间', columnType: 'TIMESTAMP' }
        ]
      } catch (error) {
        console.error('加载表字段失败:', error)
      } finally {
        this.fieldsLoading = false
      }
    },

    // 添加表到查询
    addTableToQuery(table) {
      const exists = this.queryBuilder.fromTables.find(t => t.tableName === table.tableName)
      if (!exists) {
        this.queryBuilder.fromTables.push({
          tableName: table.tableName,
          tableComment: table.tableComment,
          alias: ''
        })
      }
    },

    // 添加字段到SELECT
    addFieldToSelect(field) {
      const exists = this.queryBuilder.selectFields.find(f =>
        f.fieldName === field.columnName && f.tableName === this.selectedTable
      )
      if (!exists) {
        this.queryBuilder.selectFields.push({
          tableName: this.selectedTable,
          fieldName: field.columnName,
          fieldComment: field.columnComment,
          alias: ''
        })
      }
    },

    // 全选字段
    selectAllFields() {
      if (!this.selectedTable || this.fieldList.length === 0) return

      this.fieldList.forEach(field => {
        const exists = this.queryBuilder.selectFields.find(f =>
          f.fieldName === field.columnName && f.tableName === this.selectedTable
        )
        if (!exists) {
          this.queryBuilder.selectFields.push({
            tableName: this.selectedTable,
            fieldName: field.columnName,
            fieldComment: field.columnComment,
            alias: ''
          })
        }
      })
    },

    // 移除SELECT字段
    removeSelectField(index) {
      this.queryBuilder.selectFields.splice(index, 1)
    },

    // 移除FROM表
    removeFromTable(index) {
      this.queryBuilder.fromTables.splice(index, 1)
    },

    // 添加WHERE条件
    addWhereCondition() {
      this.queryBuilder.whereConditions.push({
        field: '',
        operator: '=',
        value: '',
        logic: this.queryBuilder.whereConditions.length > 0 ? 'AND' : ''
      })
    },

    // 移除WHERE条件
    removeWhereCondition(index) {
      this.queryBuilder.whereConditions.splice(index, 1)
    },

    // 添加ORDER BY
    addOrderBy() {
      this.queryBuilder.orderBy.push({
        field: '',
        direction: 'ASC'
      })
    },

    // 移除ORDER BY
    removeOrderBy(index) {
      this.queryBuilder.orderBy.splice(index, 1)
    },

    // 字段拖拽开始
    handleFieldDragStart(event, field) {
      event.dataTransfer.setData('text/plain', JSON.stringify({
        type: 'field',
        tableName: this.selectedTable,
        fieldName: field.columnName,
        fieldComment: field.columnComment
      }))
    },

    // SELECT区域拖拽放置
    handleSelectDrop(event) {
      event.preventDefault()
      const data = JSON.parse(event.dataTransfer.getData('text/plain'))
      if (data.type === 'field') {
        this.addFieldToSelect({
          columnName: data.fieldName,
          columnComment: data.fieldComment
        })
      }
    },

    // FROM区域拖拽放置
    handleTableDrop(event) {
      event.preventDefault()
      const data = JSON.parse(event.dataTransfer.getData('text/plain'))
      if (data.type === 'table') {
        this.addTableToQuery(data)
      }
    },

    // 获取字段图标
    getFieldIcon(fieldType) {
      const typeMap = {
        'VARCHAR': 'el-icon-document',
        'CHAR': 'el-icon-document',
        'TEXT': 'el-icon-document',
        'INT': 'el-icon-data-line',
        'INTEGER': 'el-icon-data-line',
        'DECIMAL': 'el-icon-data-line',
        'FLOAT': 'el-icon-data-line',
        'DOUBLE': 'el-icon-data-line',
        'DATE': 'el-icon-date',
        'DATETIME': 'el-icon-date',
        'TIMESTAMP': 'el-icon-date',
        'BOOLEAN': 'el-icon-switch-button'
      }
      return typeMap[fieldType?.toUpperCase()] || 'el-icon-document'
    },

    // 获取字段类型颜色
    getFieldTypeColor(fieldType) {
      const colorMap = {
        'VARCHAR': '',
        'CHAR': '',
        'TEXT': '',
        'INT': 'success',
        'INTEGER': 'success',
        'DECIMAL': 'success',
        'FLOAT': 'success',
        'DOUBLE': 'success',
        'DATE': 'warning',
        'DATETIME': 'warning',
        'TIMESTAMP': 'warning',
        'BOOLEAN': 'info'
      }
      return colorMap[fieldType?.toUpperCase()] || ''
    },

    // 构建SQL语句
    buildSQL() {
      let sql = ''

      // SELECT部分
      if (this.queryBuilder.selectFields.length > 0) {
        const fields = this.queryBuilder.selectFields.map(field => {
          let fieldStr = `${field.tableName}.${field.fieldName}`
          if (field.alias) {
            fieldStr += ` AS ${field.alias}`
          }
          return fieldStr
        })
        sql += `SELECT ${fields.join(',\n       ')}`
      } else {
        sql += 'SELECT *'
      }

      // FROM部分
      if (this.queryBuilder.fromTables.length > 0) {
        const tables = this.queryBuilder.fromTables.map(table => {
          let tableStr = table.tableName
          if (table.alias) {
            tableStr += ` AS ${table.alias}`
          }
          return tableStr
        })
        sql += `\nFROM ${tables.join(',\n     ')}`
      }

      // WHERE部分
      if (this.queryBuilder.whereConditions.length > 0) {
        const conditions = this.queryBuilder.whereConditions.map((condition, index) => {
          let conditionStr = ''
          if (index > 0 && condition.logic) {
            conditionStr += `${condition.logic} `
          }

          if (['IS NULL', 'IS NOT NULL'].includes(condition.operator)) {
            conditionStr += `${condition.field} ${condition.operator}`
          } else {
            let value = condition.value
            if (condition.operator === 'LIKE') {
              value = `'%${value}%'`
            } else if (condition.operator === 'IN') {
              value = `(${value})`
            } else if (isNaN(value)) {
              value = `'${value}'`
            }
            conditionStr += `${condition.field} ${condition.operator} ${value}`
          }

          return conditionStr
        })
        sql += `\nWHERE ${conditions.join('\n  ')}`
      }

      // ORDER BY部分
      if (this.queryBuilder.orderBy.length > 0) {
        const orders = this.queryBuilder.orderBy.map(order =>
          `${order.field} ${order.direction}`
        )
        sql += `\nORDER BY ${orders.join(', ')}`
      }

      return sql
    },

    // 复制SQL
    copySQL() {
      const sql = this.generatedSQL
      if (navigator.clipboard) {
        navigator.clipboard.writeText(sql).then(() => {
          this.$message.success('SQL已复制到剪贴板')
        })
      } else {
        // 兼容旧浏览器
        const textArea = document.createElement('textarea')
        textArea.value = sql
        document.body.appendChild(textArea)
        textArea.select()
        document.execCommand('copy')
        document.body.removeChild(textArea)
        this.$message.success('SQL已复制到剪贴板')
      }
    }
  }
}
</script>

<style scoped>
.sql-visual-builder {
  height: 100%;
  background: #f8f9fa;
}

.builder-layout {
  display: flex;
  height: 100%;
}

.left-panel {
  width: 300px;
  border-right: 1px solid #e4e7ed;
  background: #fff;
  overflow-y: auto;
}

.right-panel {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.panel-header {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.panel-header h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.table-item {
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.table-item:hover,
.table-item.active {
  background: #f0f9ff;
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.table-name {
  font-weight: 500;
  margin-left: 8px;
}

.table-meta {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}

.field-item {
  padding: 8px 16px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;
}

.field-item:hover {
  background: #f0f9ff;
}

.field-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.field-name {
  flex: 1;
  font-size: 13px;
}

.field-meta {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}

.sql-section {
  margin-bottom: 24px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background: #f8f9fa;
}

.section-header h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.drop-zone {
  padding: 20px;
  border: 2px dashed #ddd;
  border-radius: 4px;
  text-align: center;
  color: #999;
  background: #fafafa;
}

.selected-field,
.selected-table,
.condition-item,
.order-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-bottom: 1px solid #f0f0f0;
}

.sql-preview {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-all;
}

.empty-state {
  padding: 40px 20px;
  text-align: center;
  color: #999;
}
</style>
