<template>
  <el-dialog
    title="SQL参数设置"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <div class="parameter-config">
      <div class="config-header">
        <h4><i class="el-icon-setting"></i> 参数配置</h4>
        <p class="config-desc">为SQL语句中的参数设置类型、默认值和描述信息</p>
      </div>

      <div v-if="parameterList.length === 0" class="empty-state">
        <i class="el-icon-info"></i>
        <p>当前SQL中没有检测到参数</p>
        <p class="empty-tip">参数格式：${参数名}</p>
      </div>

      <div v-else class="parameter-table">
        <el-table :data="parameterList" border style="width: 100%">
          <el-table-column prop="name" label="参数名" width="120">
            <template slot-scope="scope">
              <el-tag type="primary" size="mini">${{ scope.row.name }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="数据类型" width="120">
            <template slot-scope="scope">
              <el-select v-model="scope.row.type" size="mini" style="width: 100%">
                <el-option label="字符串" value="String" />
                <el-option label="数字" value="Number" />
                <el-option label="日期" value="Date" />
                <el-option label="布尔值" value="Boolean" />
              </el-select>
            </template>
          </el-table-column>
          
          <el-table-column label="默认值" width="150">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.defaultValue"
                size="mini"
                :placeholder="getPlaceholder(scope.row.type)"
              />
            </template>
          </el-table-column>
          
          <el-table-column label="是否必填" width="100">
            <template slot-scope="scope">
              <el-switch v-model="scope.row.required" size="mini" />
            </template>
          </el-table-column>
          
          <el-table-column label="参数描述">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.description"
                size="mini"
                placeholder="请输入参数描述"
              />
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="80">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="mini"
                icon="el-icon-delete"
                @click="removeParameter(scope.$index)"
                circle
              />
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 添加参数 -->
      <div class="add-parameter">
        <el-button type="primary" size="small" @click="addParameter" icon="el-icon-plus">
          添加参数
        </el-button>
        <el-button type="success" size="small" @click="autoDetectParameters" icon="el-icon-search">
          自动检测参数
        </el-button>
      </div>

      <!-- 参数预览 -->
      <div v-if="parameterList.length > 0" class="parameter-preview">
        <h5><i class="el-icon-view"></i> 参数预览</h5>
        <div class="preview-content">
          <pre>{{ JSON.stringify(parameterList, null, 2) }}</pre>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'SqlParameterDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    parameters: {
      type: Array,
      default: () => []
    },
    sqlContent: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      parameterList: []
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initParameters()
      }
    },
    parameters: {
      handler(val) {
        if (val && val.length > 0) {
          this.parameterList = JSON.parse(JSON.stringify(val))
        }
      },
      immediate: true
    }
  },
  methods: {
    initParameters() {
      if (this.parameters && this.parameters.length > 0) {
        this.parameterList = JSON.parse(JSON.stringify(this.parameters))
      } else {
        this.parameterList = []
      }
    },

    getPlaceholder(type) {
      const placeholders = {
        'String': '请输入字符串',
        'Number': '请输入数字',
        'Date': 'YYYY-MM-DD',
        'Boolean': 'true/false'
      }
      return placeholders[type] || '请输入默认值'
    },

    addParameter() {
      this.parameterList.push({
        name: `param${this.parameterList.length + 1}`,
        type: 'String',
        defaultValue: '',
        description: '',
        required: true
      })
    },

    removeParameter(index) {
      this.parameterList.splice(index, 1)
    },

    autoDetectParameters() {
      if (!this.sqlContent) {
        this.$message.warning('请先输入SQL语句')
        return
      }

      const paramRegex = /\$\{(\w+)\}/g
      const foundParams = new Set()
      let match

      while ((match = paramRegex.exec(this.sqlContent)) !== null) {
        foundParams.add(match[1])
      }

      if (foundParams.size === 0) {
        this.$message.info('未检测到参数')
        return
      }

      // 合并检测到的参数
      foundParams.forEach(paramName => {
        const existing = this.parameterList.find(p => p.name === paramName)
        if (!existing) {
          this.parameterList.push({
            name: paramName,
            type: 'String',
            defaultValue: '',
            description: '',
            required: true
          })
        }
      })

      this.$message.success(`检测到 ${foundParams.size} 个参数`)
    },

    handleConfirm() {
      // 验证参数配置
      const invalidParams = this.parameterList.filter(param => !param.name.trim())
      if (invalidParams.length > 0) {
        this.$message.error('参数名不能为空')
        return
      }

      // 检查参数名重复
      const paramNames = this.parameterList.map(p => p.name)
      const uniqueNames = new Set(paramNames)
      if (paramNames.length !== uniqueNames.size) {
        this.$message.error('参数名不能重复')
        return
      }

      this.$emit('confirm', this.parameterList)
    },

    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.parameter-config {
  padding: 10px 0;
}

.config-header {
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.config-header h4 {
  margin: 0 0 5px 0;
  color: #303133;
}

.config-desc {
  margin: 0;
  color: #909399;
  font-size: 12px;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.empty-state i {
  font-size: 48px;
  margin-bottom: 10px;
}

.empty-tip {
  font-size: 12px;
  color: #c0c4cc;
}

.parameter-table {
  margin-bottom: 20px;
}

.add-parameter {
  margin-bottom: 20px;
  text-align: center;
}

.add-parameter .el-button {
  margin: 0 5px;
}

.parameter-preview {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
}

.parameter-preview h5 {
  margin: 0 0 10px 0;
  color: #303133;
}

.preview-content {
  background-color: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.preview-content pre {
  margin: 0;
  font-size: 12px;
  color: #606266;
}

.dialog-footer {
  text-align: right;
}
</style>
