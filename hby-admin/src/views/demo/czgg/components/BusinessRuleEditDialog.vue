<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="dataForm"
      :rules="rules"
      :model="temp"
      label-position="left"
      label-width="120px"
      style="width: 800px; margin-left:50px;"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则编码" prop="ruleCode">
            <el-input v-model="temp.ruleCode" placeholder="请输入规则编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则名称" prop="ruleName">
            <el-input v-model="temp.ruleName" placeholder="请输入规则名称" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则类型" prop="ruleType">
            <el-select
              v-model="temp.ruleType"
              placeholder="请选择规则类型"
              style="width: 100%"
            >
              <el-option label="风险控制" value="RISK_CONTROL" />
              <el-option label="业务审批" value="APPROVAL" />
              <el-option label="数据验证" value="VALIDATION" />
              <el-option label="计算规则" value="CALCULATION" />
              <el-option label="通知规则" value="NOTIFICATION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则引擎" prop="ruleEngine">
            <el-select
              v-model="temp.ruleEngine"
              placeholder="请选择规则引擎"
              style="width: 100%"
              @change="handleEngineChange"
            >
              <el-option label="Drools" value="DROOLS" />
              <el-option label="Groovy" value="GROOVY" />
              <el-option label="JavaScript" value="JAVASCRIPT" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="优先级" prop="priority">
            <el-input-number
              v-model="temp.priority"
              :min="1"
              :max="100"
              placeholder="优先级"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="版本号" prop="version">
            <el-input-number
              v-model="temp.version"
              :min="1"
              :max="999"
              placeholder="版本号"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="isEnabled">
            <el-radio-group v-model="temp.isEnabled">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效时间" prop="effectiveTime">
            <el-date-picker
              v-model="temp.effectiveTime"
              type="datetime"
              placeholder="请选择生效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效时间" prop="expiryTime">
            <el-date-picker
              v-model="temp.expiryTime"
              type="datetime"
              placeholder="请选择失效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      
      <el-form-item label="规则描述" prop="ruleDescription">
        <el-input
          v-model="temp.ruleDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入规则描述"
        />
      </el-form-item>
      
      <el-form-item label="规则脚本" prop="ruleScript">
        <div class="script-editor">
          <div class="script-toolbar">
            <el-button-group>
              <el-button size="mini" @click="formatScript">格式化</el-button>
              <el-button size="mini" @click="validateScript">验证语法</el-button>
              <el-button size="mini" @click="showScriptHelp">帮助</el-button>
            </el-button-group>
          </div>
          <el-input
            v-model="temp.ruleScript"
            type="textarea"
            :rows="12"
            :placeholder="getScriptPlaceholder()"
            class="script-textarea"
          />
        </div>
      </el-form-item>
      
      <el-form-item label="输入参数" prop="inputParameters">
        <el-input
          v-model="temp.inputParameters"
          type="textarea"
          :rows="3"
          placeholder="请输入JSON格式的输入参数定义，例如：{&quot;amount&quot;: &quot;number&quot;, &quot;currency&quot;: &quot;string&quot;}"
        />
      </el-form-item>
      
      <el-form-item label="输出参数" prop="outputParameters">
        <el-input
          v-model="temp.outputParameters"
          type="textarea"
          :rows="3"
          placeholder="请输入JSON格式的输出参数定义，例如：{&quot;result&quot;: &quot;boolean&quot;, &quot;message&quot;: &quot;string&quot;}"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">
        取消
      </el-button>
      <el-button type="primary" @click="handleSave">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateBusinessRule, validateRuleScript } from '@/api/globalTreasurer/czgg'

export default {
  name: 'BusinessRuleEditDialog',
  data() {
    return {
      dialogVisible: false,
      dialogTitle: '',
      temp: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        ruleEngine: 'DROOLS',
        priority: 50,
        version: 1,
        ruleDescription: '',
        ruleScript: '',
        inputParameters: '',
        outputParameters: '',
        effectiveTime: '',
        expiryTime: '',
        isEnabled: 1,
        orgId: null
      },
      rules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        ruleEngine: [
          { required: true, message: '请选择规则引擎', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请输入优先级', trigger: 'blur' }
        ],
        version: [
          { required: true, message: '请输入版本号', trigger: 'blur' }
        ],
        ruleScript: [
          { required: true, message: '请输入规则脚本', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    show(row) {
      this.dialogVisible = true
      this.resetTemp()
      
      if (row) {
        this.dialogTitle = '编辑业务规则'
        this.temp = Object.assign({}, row)
        // 处理时间格式
        if (this.temp.effectiveTime) {
          this.temp.effectiveTime = this.temp.effectiveTime.replace('T', ' ').substring(0, 19)
        }
        if (this.temp.expiryTime) {
          this.temp.expiryTime = this.temp.expiryTime.replace('T', ' ').substring(0, 19)
        }
      } else {
        this.dialogTitle = '新增业务规则'
        this.temp.orgId = this.$store.getters.orgId
        // 设置默认生效时间为当前时间
        const now = new Date()
        this.temp.effectiveTime = this.formatDateTime(now)
      }
      
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    
    resetTemp() {
      this.temp = {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        ruleEngine: 'DROOLS',
        priority: 50,
        version: 1,
        ruleDescription: '',
        ruleScript: '',
        inputParameters: '',
        outputParameters: '',
        effectiveTime: '',
        expiryTime: '',
        isEnabled: 1,
        orgId: null
      }
    },
    
    formatDateTime(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    
    handleEngineChange(value) {
      // 根据规则引擎类型设置默认脚本模板
      if (value === 'DROOLS') {
        this.temp.ruleScript = `rule "示例规则"
when
    $fact : Object()
then
    // 规则逻辑
end`
      } else if (value === 'GROOVY') {
        this.temp.ruleScript = `// Groovy脚本示例
def execute(params) {
    // 规则逻辑
    return [result: true, message: "执行成功"]
}`
      } else if (value === 'JAVASCRIPT') {
        this.temp.ruleScript = `// JavaScript脚本示例
function execute(params) {
    // 规则逻辑
    return {result: true, message: "执行成功"};
}`
      }
    },
    
    getScriptPlaceholder() {
      const placeholders = {
        'DROOLS': 'rule "规则名称"\nwhen\n    // 条件\nthen\n    // 动作\nend',
        'GROOVY': 'def execute(params) {\n    // Groovy脚本逻辑\n    return [result: true]\n}',
        'JAVASCRIPT': 'function execute(params) {\n    // JavaScript脚本逻辑\n    return {result: true};\n}'
      }
      return placeholders[this.temp.ruleEngine] || '请输入规则脚本'
    },
    
    formatScript() {
      if (!this.temp.ruleScript) {
        this.$message.warning('请先输入规则脚本')
        return
      }
      
      // 简单的格式化处理
      let formatted = this.temp.ruleScript
      if (this.temp.ruleEngine === 'JAVASCRIPT' || this.temp.ruleEngine === 'GROOVY') {
        // 简单的代码格式化
        formatted = formatted.replace(/;/g, ';\n').replace(/{/g, '{\n').replace(/}/g, '\n}')
      }
      this.temp.ruleScript = formatted
      this.$message.success('格式化完成')
    },
    
    validateScript() {
      if (!this.temp.ruleScript) {
        this.$message.warning('请先输入规则脚本')
        return
      }
      
      validateRuleScript({
        ruleEngine: this.temp.ruleEngine,
        ruleScript: this.temp.ruleScript
      }).then(response => {
        if (response.success) {
          this.$message.success('语法验证通过')
        } else {
          this.$message.error('语法验证失败：' + response.message)
        }
      }).catch(() => {
        this.$message.error('语法验证失败')
      })
    },
    
    showScriptHelp() {
      const helpContent = {
        'DROOLS': 'Drools规则语法：\n1. rule "规则名称" - 定义规则\n2. when - 条件部分\n3. then - 动作部分\n4. end - 规则结束',
        'GROOVY': 'Groovy脚本语法：\n1. def execute(params) - 定义执行函数\n2. params - 输入参数\n3. return - 返回结果',
        'JAVASCRIPT': 'JavaScript脚本语法：\n1. function execute(params) - 定义执行函数\n2. params - 输入参数\n3. return - 返回结果'
      }
      
      this.$alert(helpContent[this.temp.ruleEngine] || '请选择规则引擎', '脚本帮助', {
        confirmButtonText: '确定'
      })
    },
    
    handleSave() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 验证失效时间必须大于生效时间
          if (this.temp.expiryTime && this.temp.effectiveTime) {
            if (new Date(this.temp.expiryTime) <= new Date(this.temp.effectiveTime)) {
              this.$message.error('失效时间必须大于生效时间')
              return
            }
          }
          
          // 验证JSON格式的参数
          if (this.temp.inputParameters) {
            try {
              JSON.parse(this.temp.inputParameters)
            } catch (e) {
              this.$message.error('输入参数格式不正确，请输入有效的JSON格式')
              return
            }
          }
          
          if (this.temp.outputParameters) {
            try {
              JSON.parse(this.temp.outputParameters)
            } catch (e) {
              this.$message.error('输出参数格式不正确，请输入有效的JSON格式')
              return
            }
          }
          
          // 设置创建/更新用户
          if (this.temp.ruleId) {
            this.temp.updateUser = this.$store.getters.userId
          } else {
            this.temp.createUser = this.$store.getters.userId
            this.temp.updateUser = this.$store.getters.userId
          }
          
          saveOrUpdateBusinessRule(this.temp).then(response => {
            if (response.success) {
              this.$message.success(this.temp.ruleId ? '更新成功' : '创建成功')
              this.dialogVisible = false
              this.$emit('refresh')
            } else {
              this.$message.error(response.message || '保存失败')
            }
          })
        }
      })
    },
    
    handleClose() {
      this.resetTemp()
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.script-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

.script-toolbar {
  padding: 8px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #dcdfe6;
}

.script-textarea {
  border: none;
}

.script-textarea >>> .el-textarea__inner {
  border: none;
  border-radius: 0;
  font-family: 'Courier New', monospace;
}
</style>
