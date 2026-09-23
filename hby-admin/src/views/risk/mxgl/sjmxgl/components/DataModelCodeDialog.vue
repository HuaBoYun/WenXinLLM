<template>
  <el-dialog
    title="生成代码"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div class="code-generator-container">
      <div class="config-section">
        <el-form :model="codeForm" label-width="100px" inline>
          <el-form-item label="代码类型">
            <el-select v-model="codeForm.codeType" placeholder="请选择代码类型" @change="generateCode">
              <el-option label="Java Entity" value="JAVA_ENTITY" />
              <el-option label="Java Mapper" value="JAVA_MAPPER" />
              <el-option label="Java Service" value="JAVA_SERVICE" />
            </el-select>
          </el-form-item>
          <el-form-item label="包名">
            <el-input v-model="codeForm.packageName" placeholder="com.huabo.fxgl" style="width: 200px" />
          </el-form-item>
          <el-form-item label="类名">
            <el-input v-model="codeForm.className" placeholder="DataModel" style="width: 150px" @input="generateCode" />
          </el-form-item>
        </el-form>
      </div>
      <div class="preview-section">
        <div class="preview-header">
          <span>代码预览</span>
          <div class="preview-actions">
            <el-button size="small" @click="copyCode">复制代码</el-button>
            <el-button size="small" type="primary" @click="downloadCode">下载文件</el-button>
          </div>
        </div>
        <div class="code-content">
          <pre v-if="generatedCode" class="code-block">{{ generatedCode }}</pre>
          <div v-else class="empty-state">
            <i class="el-icon-document"></i>
            <p>请选择代码类型开始生成</p>
          </div>
        </div>
      </div>
    </div>
  </el-dialog>
</template>

<script>
import { generateDataModelCode } from '@/api/mxgl'

export default {
  name: 'DataModelCodeDialog',
  props: {
    visible: { type: Boolean, default: false },
    modelData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      codeForm: { codeType: '', packageName: 'com.huabo.fxgl', className: '' },
      generatedCode: ''
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.modelData.modelName) {
        this.codeForm.className = this.toPascalCase(this.modelData.modelName)
        this.generateCode()
      }
    },
    dialogVisible(val) { this.$emit('update:visible', val) }
  },
  methods: {
    async generateCode() {
      if (!this.codeForm.codeType || !this.codeForm.className) {
        this.generatedCode = ''
        return
      }
      try {
        const response = await generateDataModelCode(this.modelData.id || 'default')
        if (response.code === 1 && response.data) {
          this.generatedCode = response.data
          return
        }
      } catch (error) {
        console.warn('后端代码生成失败，使用模板代码:', error)
      }
      const { packageName, className } = this.codeForm
      const modelName = this.modelData.modelName || className
      switch (this.codeForm.codeType) {
        case 'JAVA_ENTITY':
          this.generatedCode = this.generateJavaEntity(packageName, className, modelName)
          break
        case 'JAVA_MAPPER':
          this.generatedCode = this.generateJavaMapper(packageName, className, modelName)
          break
        case 'JAVA_SERVICE':
          this.generatedCode = this.generateJavaService(packageName, className, modelName)
          break
        default:
          this.generatedCode = '// 请选择代码类型'
      }
    },
    generateJavaEntity(packageName, className, modelName) {
      const currentDate = new Date().toISOString().split('T')[0]
      const upperClassName = className.toUpperCase()
      return [
        'package ' + packageName + '.entity;',
        'import com.baomidou.mybatisplus.annotation.TableField;',
        'import com.baomidou.mybatisplus.annotation.TableId;',
        'import com.baomidou.mybatisplus.annotation.TableName;',
        'import lombok.Data;',
        'import java.time.LocalDateTime;',
        '@Data',
        '@TableName("TBL_' + upperClassName + '")',
        'public class ' + className + ' {',
        '    @TableId("ID")',
        '    private String id;',
        '    @TableField("MODEL_NAME")',
        '    private String modelName;',
        '    @TableField("MODEL_CODE")',
        '    private String modelCode;',
        '    @TableField("CREATE_TIME")',
        '    private LocalDateTime createTime;',
        '    @TableField("UPDATE_TIME")',
        '    private LocalDateTime updateTime;',
        '}'
      ].join('\n')
    },
    generateJavaMapper(packageName, className, modelName) {
      const upperClassName = className.toUpperCase()
      return [
        'package ' + packageName + '.mapper;',
        'import com.baomidou.mybatisplus.core.mapper.BaseMapper;',
        'import ' + packageName + '.entity.' + className + ';',
        'import org.apache.ibatis.annotations.Mapper;',
        '@Mapper',
        'public interface ' + className + 'Mapper extends BaseMapper<' + className + '> {',
        '}'
      ].join('\n')
    },
    generateJavaService(packageName, className, modelName) {
      return [
        'package ' + packageName + '.service;',
        'import com.baomidou.mybatisplus.extension.service.IService;',
        'import ' + packageName + '.entity.' + className + ';',
        'import com.hbfk.util.JsonBean;',
        'public interface ' + className + 'Service extends IService<' + className + '> {',
        '    JsonBean getByModelCode(String modelCode);',
        '    JsonBean saveModel(' + className + ' model, String currentUser);',
        '    JsonBean deleteModel(String id);',
        '}'
      ].join('\n')
    },
    toPascalCase(str) {
      return str.replace(/(?:^|[\s_-])(\w)/g, (match, letter) => letter.toUpperCase()).replace(/[\s_-]/g, '')
    },
    async copyCode() {
      if (!this.generatedCode) {
        this.$message.warning('没有可复制的代码')
        return
      }
      try {
        await navigator.clipboard.writeText(this.generatedCode)
        this.$message.success('代码已复制到剪贴板')
      } catch (error) {
        const textArea = document.createElement('textarea')
        textArea.value = this.generatedCode
        document.body.appendChild(textArea)
        textArea.select()
        document.execCommand('copy')
        document.body.removeChild(textArea)
        this.$message.success('代码已复制到剪贴板')
      }
    },
    downloadCode() {
      if (!this.generatedCode) {
        this.$message.warning('没有可下载的代码')
        return
      }
      const fileName = this.getFileName()
      const blob = new Blob([this.generatedCode], { type: 'text/plain;charset=utf-8' })
      const url = URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
      this.$message.success('文件下载成功')
    },
    getFileName() {
      const { codeType, className } = this.codeForm
      const name = className || 'GeneratedCode'
      switch (codeType) {
        case 'JAVA_ENTITY': return name + '.java'
        case 'JAVA_MAPPER': return name + 'Mapper.java'
        case 'JAVA_SERVICE': return name + 'Service.java'
        default: return name + '.txt'
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.codeForm = { codeType: '', packageName: 'com.huabo.fxgl', className: '' }
      this.generatedCode = ''
    }
  }
}
</script>

<style scoped>
.code-generator-container { height: 600px; display: flex; flex-direction: column; }
.config-section { padding: 20px; border-bottom: 1px solid #ebeef5; }
.preview-section { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.preview-header { display: flex; justify-content: space-between; align-items: center; padding: 15px 20px; border-bottom: 1px solid #ebeef5; background-color: #f5f7fa; }
.preview-actions { display: flex; gap: 10px; }
.code-content { flex: 1; overflow: auto; padding: 20px; }
.code-block { background-color: #f6f8fa; border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; font-family: 'Courier New', Consolas, monospace; font-size: 14px; line-height: 1.45; overflow: auto; white-space: pre-wrap; word-wrap: break-word; margin: 0; }
.empty-state { display: flex; flex-direction: column; align-items: center; justify-content: center; height: 300px; color: #909399; }
.empty-state i { font-size: 48px; margin-bottom: 16px; }
.empty-state p { margin: 0; font-size: 14px; }
</style>
