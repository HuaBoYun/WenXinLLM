<template>
  <el-dialog
    title="选择表达式模板"
    :visible.sync="visible"
    width="800px"
    :before-close="handleClose"
    append-to-body
  >
    <div class="template-search">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索模板名称或描述"
        prefix-icon="el-icon-search"
        @input="handleSearch"
        style="width: 300px; margin-bottom: 20px;"
      />
    </div>
    
    <el-table
      :data="filteredTemplates"
      border
      style="width: 100%"
      @row-click="handleRowClick"
      highlight-current-row
    >
      <el-table-column prop="templateName" label="模板名称" width="200" />
      <el-table-column prop="templateType" label="类型" width="120">
        <template slot-scope="scope">
          <el-tag :type="getTypeColor(scope.row.templateType)">
            {{ getTypeText(scope.row.templateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述" />
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button type="text" @click="selectTemplate(scope.row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <el-divider content-position="left">模板预览</el-divider>
    <div v-if="selectedTemplate" class="template-preview">
      <h4>{{ selectedTemplate.templateName }}</h4>
      <p><strong>类型：</strong>{{ getTypeText(selectedTemplate.templateType) }}</p>
      <p><strong>描述：</strong>{{ selectedTemplate.description }}</p>
      <p><strong>表达式内容：</strong></p>
      <el-input
        v-model="selectedTemplate.expression"
        type="textarea"
        :rows="6"
        readonly
      />
      <p><strong>使用说明：</strong></p>
      <div class="usage-info">
        <ul>
          <li v-for="usage in selectedTemplate.usageInfo" :key="usage">{{ usage }}</li>
        </ul>
      </div>
    </div>
    <div v-else class="no-preview">
      <el-empty description="请选择一个模板查看预览" />
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="confirmSelect" :disabled="!selectedTemplate">确定选择</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExpressionTemplateSelectDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      searchKeyword: '',
      selectedTemplate: null,
      templates: [
        {
          id: '1',
          templateName: '数值比较模板',
          templateType: 'NUMERIC',
          description: '用于数值大小比较的表达式模板',
          expression: '${amount} > ${threshold}',
          usageInfo: [
            '${amount} - 要比较的数值字段',
            '${threshold} - 阈值',
            '支持运算符：>、>=、<、<=、==、!='
          ]
        },
        {
          id: '2',
          templateName: '字符串匹配模板',
          templateType: 'STRING',
          description: '用于字符串匹配和包含判断',
          expression: '${text}.contains("${keyword}")',
          usageInfo: [
            '${text} - 要检查的文本字段',
            '${keyword} - 要匹配的关键词',
            '支持方法：contains、startsWith、endsWith、equals'
          ]
        },
        {
          id: '3',
          templateName: '日期范围模板',
          templateType: 'DATE',
          description: '用于日期范围判断',
          expression: '${date} >= "${startDate}" && ${date} <= "${endDate}"',
          usageInfo: [
            '${date} - 日期字段',
            '${startDate} - 开始日期',
            '${endDate} - 结束日期',
            '日期格式：yyyy-MM-dd'
          ]
        },
        {
          id: '4',
          templateName: '复合条件模板',
          templateType: 'LOGIC',
          description: '多条件组合判断',
          expression: '(${amount} > ${minAmount}) && (${type} == "${targetType}") || (${priority} == "HIGH")',
          usageInfo: [
            '支持逻辑运算符：&&（与）、||（或）、!（非）',
            '使用括号控制运算优先级',
            '可以组合多个不同类型的条件'
          ]
        },
        {
          id: '5',
          templateName: '风险评级模板',
          templateType: 'LOGIC',
          description: '基于多个因素的风险评级',
          expression: '${score} >= 80 ? "HIGH" : (${score} >= 60 ? "MEDIUM" : "LOW")',
          usageInfo: [
            '使用三元运算符进行条件判断',
            '${score} - 风险评分',
            '返回风险等级：HIGH、MEDIUM、LOW'
          ]
        },
        {
          id: '6',
          templateName: '数组包含模板',
          templateType: 'LOGIC',
          description: '检查值是否在指定数组中',
          expression: '["URGENT", "HIGH", "CRITICAL"].includes(${priority})',
          usageInfo: [
            '${priority} - 要检查的值',
            '数组中列出所有可能的匹配值',
            '返回布尔值：true或false'
          ]
        }
      ]
    }
  },
  computed: {
    filteredTemplates() {
      if (!this.searchKeyword) {
        return this.templates
      }
      const keyword = this.searchKeyword.toLowerCase()
      return this.templates.filter(template => 
        template.templateName.toLowerCase().includes(keyword) ||
        template.description.toLowerCase().includes(keyword)
      )
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.selectedTemplate = null
        this.searchKeyword = ''
      }
    }
  },
  methods: {
    getTypeText(type) {
      const typeMap = {
        'NUMERIC': '数值',
        'STRING': '字符串',
        'DATE': '日期',
        'LOGIC': '逻辑'
      }
      return typeMap[type] || type
    },
    
    getTypeColor(type) {
      const colorMap = {
        'NUMERIC': 'success',
        'STRING': 'primary',
        'DATE': 'warning',
        'LOGIC': 'danger'
      }
      return colorMap[type] || 'default'
    },
    
    handleSearch() {
      // 搜索逻辑已在computed中实现
    },
    
    handleRowClick(row) {
      this.selectedTemplate = row
    },
    
    selectTemplate(template) {
      this.selectedTemplate = template
    },
    
    confirmSelect() {
      if (this.selectedTemplate) {
        this.$emit('select', this.selectedTemplate)
        this.handleClose()
      }
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.template-search {
  margin-bottom: 20px;
}

.template-preview {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.template-preview h4 {
  color: #409EFF;
  margin-bottom: 10px;
}

.template-preview p {
  margin-bottom: 10px;
}

.usage-info ul {
  margin-left: 20px;
}

.usage-info li {
  margin-bottom: 5px;
  color: #666;
}

.no-preview {
  text-align: center;
  padding: 50px 0;
}

.dialog-footer {
  text-align: right;
}

.el-table__row {
  cursor: pointer;
}
</style>
