<template>
  <el-dialog
    title="数据清洗"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success">
      <el-step title="选择数据" description="选择需要清洗的数据"></el-step>
      <el-step title="配置规则" description="配置清洗规则"></el-step>
      <el-step title="预览结果" description="预览清洗结果"></el-step>
      <el-step title="执行清洗" description="执行数据清洗"></el-step>
    </el-steps>
    
    <!-- 步骤1：选择数据 -->
    <div v-if="currentStep === 0" style="margin-top: 30px;">
      <el-form :model="selectForm" label-width="120px">
        <el-form-item label="数据范围">
          <el-radio-group v-model="selectForm.dataRange">
            <el-radio label="all">全部数据</el-radio>
            <el-radio label="period">指定期间</el-radio>
            <el-radio label="enterprise">指定企业</el-radio>
            <el-radio label="type">指定类型</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="时间范围" v-if="selectForm.dataRange === 'period'">
          <el-date-picker
            v-model="selectForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
        
        <el-form-item label="企业选择" v-if="selectForm.dataRange === 'enterprise'">
          <el-select v-model="selectForm.enterpriseIds" multiple placeholder="请选择企业">
            <el-option label="示例云科技有限公司" value="1"></el-option>
            <el-option label="示例云投资有限公司" value="2"></el-option>
            <el-option label="示例云控股有限公司" value="3"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="数据类型" v-if="selectForm.dataRange === 'type'">
          <el-checkbox-group v-model="selectForm.dataTypes">
            <el-checkbox label="financial">财务数据</el-checkbox>
            <el-checkbox label="operation">经营数据</el-checkbox>
            <el-checkbox label="personnel">人员数据</el-checkbox>
            <el-checkbox label="asset">资产数据</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      
      <div style="margin-top: 20px;">
        <el-button type="primary" @click="nextStep">下一步</el-button>
      </div>
    </div>
    
    <!-- 步骤2：配置规则 -->
    <div v-if="currentStep === 1" style="margin-top: 30px;">
      <el-alert
        title="清洗规则配置"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <template slot="description">
          请配置数据清洗规则，系统将根据这些规则对数据进行清洗处理
        </template>
      </el-alert>
      
      <el-form :model="cleanRules" label-width="150px">
        <el-form-item label="空值处理">
          <el-checkbox-group v-model="cleanRules.nullHandling">
            <el-checkbox label="removeNull">删除空值记录</el-checkbox>
            <el-checkbox label="fillDefault">用默认值填充</el-checkbox>
            <el-checkbox label="fillAverage">用平均值填充</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="重复数据处理">
          <el-radio-group v-model="cleanRules.duplicateHandling">
            <el-radio label="keep">保留重复数据</el-radio>
            <el-radio label="removeAll">删除所有重复</el-radio>
            <el-radio label="keepFirst">保留第一条</el-radio>
            <el-radio label="keepLast">保留最后一条</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="异常值处理">
          <el-checkbox-group v-model="cleanRules.outlierHandling">
            <el-checkbox label="removeOutlier">删除异常值</el-checkbox>
            <el-checkbox label="capOutlier">异常值截断</el-checkbox>
            <el-checkbox label="markOutlier">标记异常值</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="数据格式化">
          <el-checkbox-group v-model="cleanRules.formatting">
            <el-checkbox label="trimSpace">去除空格</el-checkbox>
            <el-checkbox label="upperCase">转换大写</el-checkbox>
            <el-checkbox label="lowerCase">转换小写</el-checkbox>
            <el-checkbox label="standardDate">标准化日期</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="数据验证">
          <el-checkbox-group v-model="cleanRules.validation">
            <el-checkbox label="validateEmail">邮箱格式验证</el-checkbox>
            <el-checkbox label="validatePhone">电话格式验证</el-checkbox>
            <el-checkbox label="validateNumber">数字格式验证</el-checkbox>
            <el-checkbox label="validateDate">日期格式验证</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      
      <div style="margin-top: 20px;">
        <el-button @click="prevStep">上一步</el-button>
        <el-button type="primary" @click="nextStep">下一步</el-button>
      </div>
    </div>
    
    <!-- 步骤3：预览结果 -->
    <div v-if="currentStep === 2" style="margin-top: 30px;">
      <el-alert
        :title="`预计处理 ${previewResult.totalCount} 条数据`"
        type="success"
        :closable="false"
        style="margin-bottom: 20px;"
      ></el-alert>
      
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="删除记录" :value="previewResult.deleteCount" suffix="条">
            <template slot="prefix">
              <i class="el-icon-delete" style="color: #F56C6C"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="修改记录" :value="previewResult.updateCount" suffix="条">
            <template slot="prefix">
              <i class="el-icon-edit" style="color: #E6A23C"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="保留记录" :value="previewResult.keepCount" suffix="条">
            <template slot="prefix">
              <i class="el-icon-check" style="color: #67C23A"></i>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="异常记录" :value="previewResult.errorCount" suffix="条">
            <template slot="prefix">
              <i class="el-icon-warning" style="color: #909399"></i>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
      
      <el-divider content-position="left">清洗详情</el-divider>
      <el-table :data="previewResult.details" border max-height="300">
        <el-table-column prop="operation" label="操作类型" width="100">
          <template slot-scope="scope">
            <el-tag size="small" :type="getOperationType(scope.row.operation)">
              {{ scope.row.operation }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fieldName" label="字段名称" width="150"></el-table-column>
        <el-table-column prop="oldValue" label="原值" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="newValue" label="新值" width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="reason" label="处理原因"></el-table-column>
      </el-table>
      
      <div style="margin-top: 20px;">
        <el-button @click="prevStep">上一步</el-button>
        <el-button type="primary" @click="nextStep">执行清洗</el-button>
      </div>
    </div>
    
    <!-- 步骤4：执行清洗 -->
    <div v-if="currentStep === 3" style="margin-top: 30px;">
      <el-result
        :icon="cleanResult.success ? 'success' : 'warning'"
        :title="cleanResult.title"
        :sub-title="cleanResult.subtitle"
      >
        <template slot="extra">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="处理总数">
              {{ cleanResult.totalProcessed }}
            </el-descriptions-item>
            <el-descriptions-item label="成功处理">
              <span style="color: #67C23A;">{{ cleanResult.successCount }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="处理失败">
              <span style="color: #F56C6C;">{{ cleanResult.failedCount }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="处理时间">
              {{ cleanResult.processTime }}
            </el-descriptions-item>
          </el-descriptions>
          
          <div style="margin-top: 20px;">
            <el-button @click="handleClose">关闭</el-button>
            <el-button type="primary" @click="downloadReport">下载报告</el-button>
          </div>
        </template>
      </el-result>
    </div>
    
    <div slot="footer" class="dialog-footer" v-if="currentStep < 3">
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'DataCleanDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentStep: 0,
      selectForm: {
        dataRange: 'all',
        dateRange: [],
        enterpriseIds: [],
        dataTypes: []
      },
      cleanRules: {
        nullHandling: [],
        duplicateHandling: 'keep',
        outlierHandling: [],
        formatting: [],
        validation: []
      },
      previewResult: {
        totalCount: 0,
        deleteCount: 0,
        updateCount: 0,
        keepCount: 0,
        errorCount: 0,
        details: []
      },
      cleanResult: {
        success: true,
        title: '',
        subtitle: '',
        totalProcessed: 0,
        successCount: 0,
        failedCount: 0,
        processTime: ''
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.resetDialog()
      }
    }
  },
  methods: {
    resetDialog() {
      this.currentStep = 0
      this.selectForm = {
        dataRange: 'all',
        dateRange: [],
        enterpriseIds: [],
        dataTypes: []
      }
      this.cleanRules = {
        nullHandling: [],
        duplicateHandling: 'keep',
        outlierHandling: [],
        formatting: [],
        validation: []
      }
    },
    nextStep() {
      if (this.currentStep === 0) {
        // 验证数据选择
        this.currentStep = 1
      } else if (this.currentStep === 1) {
        // 生成预览结果
        this.generatePreview()
        this.currentStep = 2
      } else if (this.currentStep === 2) {
        // 执行清洗
        this.executeClean()
        this.currentStep = 3
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    generatePreview() {
      // 模拟生成预览结果
      this.previewResult = {
        totalCount: 1000,
        deleteCount: 50,
        updateCount: 200,
        keepCount: 750,
        errorCount: 0,
        details: [
          {
            operation: '删除',
            fieldName: '营业收入',
            oldValue: '',
            newValue: '',
            reason: '空值删除'
          },
          {
            operation: '修改',
            fieldName: '企业名称',
            oldValue: ' 示例云科技 ',
            newValue: '示例云科技',
            reason: '去除空格'
          }
        ]
      }
    },
    executeClean() {
      // 模拟执行清洗
      setTimeout(() => {
        this.cleanResult = {
          success: true,
          title: '数据清洗完成',
          subtitle: '数据清洗操作已成功完成',
          totalProcessed: 1000,
          successCount: 950,
          failedCount: 50,
          processTime: '2分30秒'
        }
        this.$emit('refresh')
      }, 2000)
    },
    getOperationType(operation) {
      const typeMap = {
        '删除': 'danger',
        '修改': 'warning',
        '保留': 'success',
        '异常': 'info'
      }
      return typeMap[operation] || 'info'
    },
    downloadReport() {
      // 生成清洗报告内容并下载为CSV
      const lines = [
        '数据清洗报告',
        '',
        '处理总数,' + (this.cleanResult.totalProcessed || 0),
        '成功处理,' + (this.cleanResult.successCount || 0),
        '处理失败,' + (this.cleanResult.failedCount || 0),
        '处理时间,' + (this.cleanResult.processTime || '-'),
        '',
        '清洗规则配置:',
        '空值处理,' + (this.cleanRules.nullHandling || []).join(';'),
        '重复处理,' + (this.cleanRules.duplicateHandling || ''),
        '异常值处理,' + (this.cleanRules.outlierHandling || []).join(';'),
        '格式化,' + (this.cleanRules.formatting || []).join(';'),
        '',
        '预览详情:',
        '操作,字段,原值,新值'
      ]
      if (this.previewResult.details && this.previewResult.details.length > 0) {
        this.previewResult.details.forEach(item => {
          lines.push([item.operation || '', item.field || '', item.oldValue || '', item.newValue || ''].join(','))
        })
      }
      const csvContent = '\uFEFF' + lines.join('\n')
      const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `数据清洗报告_${new Date().getTime()}.csv`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)
      this.$message.success('清洗报告下载成功')
    },
    handleClose() {
      this.dialogVisible = false
      this.resetDialog()
    }
  }
}
</script>
