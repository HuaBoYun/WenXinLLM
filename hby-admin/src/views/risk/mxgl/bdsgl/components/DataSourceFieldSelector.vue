<template>
  <div class="datasource-field-selector">
    <!-- 数据源选择 -->
    <el-form-item label="数据源" prop="dataSourceId">
      <el-select
        v-model="selectedDataSource"
        placeholder="请选择数据源"
        style="width: 100%"
        @change="handleDataSourceChange"
        clearable
      >
        <el-option
          v-for="source in dataSourceList"
          :key="source.sourceId"
          :label="source.sourceName"
          :value="source.sourceId"
        >
          <span style="float: left">{{ source.sourceName }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ source.sourceType }}</span>
        </el-option>
      </el-select>
    </el-form-item>

    <!-- 表选择 -->
    <el-form-item label="数据表" prop="tableName">
      <el-select
        v-model="selectedTable"
        placeholder="请选择数据表"
        style="width: 100%"
        @change="handleTableChange"
        :disabled="!selectedDataSource"
        clearable
        filterable
      >
        <el-option
          v-for="table in tableList"
          :key="table.tableName"
          :label="table.tableName"
          :value="table.tableName"
        >
          <span style="float: left">{{ table.tableName }}</span>
          <span style="float: right; color: #8492a6; font-size: 13px">{{ table.tableComment || '无描述' }}</span>
        </el-option>
      </el-select>
    </el-form-item>

    <!-- 字段选择 -->
    <el-form-item label="字段选择" prop="selectedFields">
      <div class="field-selector">
        <!-- 字段搜索 -->
        <el-input
          v-model="fieldSearchKeyword"
          placeholder="搜索字段名称"
          prefix-icon="el-icon-search"
          size="small"
          style="margin-bottom: 10px"
          :disabled="!selectedTable"
        />
        
        <!-- 字段列表 -->
        <div class="field-list" v-if="selectedTable">
          <el-checkbox-group v-model="selectedFields" @change="handleFieldChange">
            <div class="field-item" v-for="field in filteredFields" :key="field.columnName">
              <el-checkbox :label="field.columnName">
                <div class="field-info">
                  <span class="field-name">{{ field.columnName }}</span>
                  <el-tag size="mini" :type="getFieldTypeTag(field.dataType)">{{ field.dataType }}</el-tag>
                  <span class="field-comment">{{ field.columnComment || '无描述' }}</span>
                </div>
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </div>

        <!-- 空状态 -->
        <el-empty v-else description="请先选择数据源和表" :image-size="80" />
      </div>
    </el-form-item>

    <!-- 已选字段预览 -->
    <el-form-item label="已选字段" v-if="selectedFields.length > 0">
      <div class="selected-fields">
        <el-tag
          v-for="field in selectedFields"
          :key="field"
          closable
          @close="removeField(field)"
          style="margin-right: 8px; margin-bottom: 8px"
        >
          {{ field }}
        </el-tag>
      </div>
    </el-form-item>
  </div>
</template>

<script>
import { getDataSourcesForExpression, getTablesForExpression, getTableFieldsForExpression } from '@/api/mxgl'

export default {
  name: 'DataSourceFieldSelector',
  props: {
    // 初始值
    value: {
      type: Object,
      default: () => ({
        dataSourceId: '',
        tableName: '',
        selectedFields: []
      })
    },
    // 是否多选字段
    multiple: {
      type: Boolean,
      default: true
    },
    // 是否必选
    required: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 数据源列表
      dataSourceList: [],
      // 表列表
      tableList: [],
      // 字段列表
      fieldList: [],
      // 字段搜索关键词
      fieldSearchKeyword: '',
      
      // 选中的值
      selectedDataSource: '',
      selectedTable: '',
      selectedFields: [],
      
      // 加载状态
      loadingDataSources: false,
      loadingTables: false,
      loadingFields: false
    }
  },
  computed: {
    // 过滤后的字段列表
    filteredFields() {
      if (!this.fieldSearchKeyword) {
        return this.fieldList
      }
      return this.fieldList.filter(field => 
        field.columnName.toLowerCase().includes(this.fieldSearchKeyword.toLowerCase()) ||
        (field.columnComment && field.columnComment.toLowerCase().includes(this.fieldSearchKeyword.toLowerCase()))
      )
    }
  },
  watch: {
    value: {
      handler(newVal) {
        if (newVal) {
          this.selectedDataSource = newVal.dataSourceId || ''
          this.selectedTable = newVal.tableName || ''
          this.selectedFields = newVal.selectedFields || []
          
          // 如果有初始值，需要加载对应的数据
          if (this.selectedDataSource) {
            this.loadTables()
            if (this.selectedTable) {
              this.loadFields()
            }
          }
        }
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    this.loadDataSources()
  },
  methods: {
    // 加载数据源列表
    async loadDataSources() {
      this.loadingDataSources = true
      try {
        const response = await getDataSourcesForExpression()
        if (response.code === 1) {
          this.dataSourceList = response.data || []
        } else {
          this.$message.error('获取数据源列表失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('获取数据源列表失败')
        console.error('Load data sources error:', error)
      } finally {
        this.loadingDataSources = false
      }
    },

    // 加载表列表
    async loadTables() {
      if (!this.selectedDataSource) return
      
      this.loadingTables = true
      try {
        const response = await getTablesForExpression(this.selectedDataSource)
        if (response.code === 1) {
          this.tableList = response.data || []
        } else {
          this.$message.error('获取表列表失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('获取表列表失败')
        console.error('Load tables error:', error)
      } finally {
        this.loadingTables = false
      }
    },

    // 加载字段列表
    async loadFields() {
      if (!this.selectedDataSource || !this.selectedTable) return
      
      this.loadingFields = true
      try {
        const response = await getTableFieldsForExpression({
          dataSourceId: this.selectedDataSource,
          tableName: this.selectedTable
        })
        if (response.code === 1) {
          this.fieldList = response.data || []
        } else {
          this.$message.error('获取字段列表失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('获取字段列表失败')
        console.error('Load fields error:', error)
      } finally {
        this.loadingFields = false
      }
    },

    // 数据源变化处理
    handleDataSourceChange(value) {
      this.selectedDataSource = value
      this.selectedTable = ''
      this.selectedFields = []
      this.tableList = []
      this.fieldList = []
      
      if (value) {
        this.loadTables()
      }
      this.emitChange()
    },

    // 表变化处理
    handleTableChange(value) {
      this.selectedTable = value
      this.selectedFields = []
      this.fieldList = []
      
      if (value) {
        this.loadFields()
      }
      this.emitChange()
    },

    // 字段变化处理
    handleFieldChange(value) {
      this.selectedFields = value
      this.emitChange()
    },

    // 移除字段
    removeField(field) {
      const index = this.selectedFields.indexOf(field)
      if (index > -1) {
        this.selectedFields.splice(index, 1)
        this.emitChange()
      }
    },

    // 获取字段类型标签样式
    getFieldTypeTag(dataType) {
      const type = dataType.toLowerCase()
      if (type.includes('varchar') || type.includes('char') || type.includes('text')) {
        return 'success'
      } else if (type.includes('int') || type.includes('number') || type.includes('decimal')) {
        return 'warning'
      } else if (type.includes('date') || type.includes('time')) {
        return 'info'
      } else {
        return ''
      }
    },

    // 发送变化事件
    emitChange() {
      const value = {
        dataSourceId: this.selectedDataSource,
        tableName: this.selectedTable,
        selectedFields: this.selectedFields
      }
      this.$emit('input', value)
      this.$emit('change', value)
    },

    // 验证选择
    validate() {
      if (this.required) {
        if (!this.selectedDataSource) {
          this.$message.warning('请选择数据源')
          return false
        }
        if (!this.selectedTable) {
          this.$message.warning('请选择数据表')
          return false
        }
        if (this.selectedFields.length === 0) {
          this.$message.warning('请至少选择一个字段')
          return false
        }
      }
      return true
    }
  }
}
</script>

<style scoped>
.datasource-field-selector {
  width: 100%;
}

.field-selector {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  max-height: 300px;
  overflow-y: auto;
}

.field-list {
  max-height: 200px;
  overflow-y: auto;
}

.field-item {
  margin-bottom: 8px;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.field-item:hover {
  background-color: #f5f7fa;
}

.field-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.field-name {
  font-weight: 500;
  color: #303133;
}

.field-comment {
  color: #909399;
  font-size: 12px;
  flex: 1;
}

.selected-fields {
  min-height: 32px;
  padding: 5px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fafafa;
}
</style>
