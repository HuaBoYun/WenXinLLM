<template>
  <el-dialog
    :title="isEdit ? '编辑数据' : '数据录入'"
    :visible.sync="dialogVisible"
    width="900px"
    @close="handleClose"
  >
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据类型" prop="dataType">
            <el-select v-model="form.dataType" placeholder="请选择数据类型" style="width: 100%;">
              <el-option label="财务数据" value="财务数据"></el-option>
              <el-option label="经营数据" value="经营数据"></el-option>
              <el-option label="人力资源" value="人力资源"></el-option>
              <el-option label="风险数据" value="风险数据"></el-option>
              <el-option label="合规数据" value="合规数据"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类别" prop="dataCategory">
            <el-select v-model="form.dataCategory" placeholder="请选择数据类别" style="width: 100%;">
              <el-option label="月度报表" value="月度报表"></el-option>
              <el-option label="季度报表" value="季度报表"></el-option>
              <el-option label="年度报表" value="年度报表"></el-option>
              <el-option label="专项报告" value="专项报告"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告期间" prop="reportPeriod">
            <el-input v-model="form.reportPeriod" placeholder="如：2025-Q1、2025-03"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="录入人" prop="submitter">
            <el-input v-model="form.submitter" placeholder="请输入录入人姓名"></el-input>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属企业" prop="enterpriseId">
            <el-input
              :value="form.enterpriseName"
              placeholder="请选择企业"
              readonly
              @click.native="openCompanyTree"
            >
              <el-button slot="append" icon="el-icon-search" @click="openCompanyTree"></el-button>
            </el-input>
            <CompanyTreeModal
              ref="companyTreeModal"
              @selected="handleCompanySelected"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告年度">
            <el-date-picker
              v-model="form.reportYear"
              type="year"
              placeholder="选择年度"
              format="yyyy"
              value-format="yyyy"
              style="width: 100%;"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
        ></el-input>
      </el-form-item>
      
      <el-divider content-position="left">数据项目</el-divider>
      <el-button type="primary" size="small" @click="addDataItem" style="margin-bottom: 10px;">
        添加数据项
      </el-button>
      
      <el-table :data="form.dataItems" border>
        <el-table-column label="序号" width="60">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="字段名称" width="200">
          <template slot-scope="scope">
            <el-input v-model="scope.row.fieldName" size="small" placeholder="字段名称"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="字段值">
          <template slot-scope="scope">
            <el-input v-model="scope.row.fieldValue" size="small" placeholder="字段值"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="数据类型" width="120">
          <template slot-scope="scope">
            <el-select v-model="scope.row.fieldType" size="small" placeholder="类型">
              <el-option label="文本" value="String"></el-option>
              <el-option label="数字" value="Number"></el-option>
              <el-option label="日期" value="Date"></el-option>
              <el-option label="布尔" value="Boolean"></el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="单位" width="100">
          <template slot-scope="scope">
            <el-input v-model="scope.row.unit" size="small" placeholder="单位"></el-input>
          </template>
        </el-table-column>
        <el-table-column label="必填" width="80">
          <template slot-scope="scope">
            <el-checkbox v-model="scope.row.required"></el-checkbox>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="removeDataItem(scope.$index)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-divider content-position="left">附件上传</el-divider>
      <el-upload
        class="upload-demo"
        :action="uploadUrl"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        multiple
      >
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button @click="handleSaveDraft">保存草稿</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">提交</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addDataEntry, updateDataEntry } from '@/api/enterprise/data'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import request from '@/utils/request'

export default {
  name: 'DataEntryFormDialog',
  components: {
    CompanyTreeModal
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    editData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add'
    }
  },
  data() {
    return {
      loading: false,
      uploadUrl: '/api/upload',
      fileList: [],
      form: {
        dataType: '',
        dataCategory: '',
        enterpriseId: '',
        enterpriseName: '',
        reportPeriod: '',
        reportYear: '',
        submitter: '',
        remark: '',
        dataItems: []
      },
      rules: {
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ],
        dataCategory: [
          { required: true, message: '请选择数据类别', trigger: 'change' }
        ],
        enterpriseId: [
          { required: true, message: '请选择所属企业', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请输入报告期间', trigger: 'blur' }
        ],
        submitter: [
          { required: true, message: '请输入录入人', trigger: 'blur' }
        ]
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
    },
    isEdit() {
      return this.dialogType === 'edit' && this.editData && this.editData.id
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    // 打开企业选择对话框
    openCompanyTree() {
      this.$refs.companyTreeModal.show()
    },

    // 企业选择确认回调
    handleCompanySelected(node) {
      if (node) {
        this.form.enterpriseId = String(node.id)
        this.form.enterpriseName = node.label || node.name || ''
      }
    },

    initForm() {
      if (this.editData && (this.editData.dataId || this.editData.id)) {
        // 编辑模式
        this.form = {
          ...this.editData,
          enterpriseName: this.editData.enterpriseName || '',
          dataItems: this.editData.dataItems || []
        }
      } else {
        // 新增模式
        this.form = {
          dataType: '',
          dataCategory: '',
          enterpriseId: '',
          enterpriseName: '',
          reportPeriod: '',
          reportYear: new Date().getFullYear().toString(),
          submitter: '',
          remark: '',
          dataItems: [
            {
              fieldName: '',
              fieldValue: '',
              fieldType: 'String',
              unit: '',
              required: false
            }
          ]
        }
      }
      this.fileList = []
    },
    addDataItem() {
      this.form.dataItems.push({
        fieldName: '',
        fieldValue: '',
        fieldType: 'String',
        unit: '',
        required: false
      })
    },
    removeDataItem(index) {
      this.form.dataItems.splice(index, 1)
    },
    handlePreview(file) {
      console.log(file)
    },
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    handleClose() {
      this.dialogVisible = false
      this.$refs.form.resetFields()
    },
    async handleSaveDraft() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          try {
            this.loading = true
            this.form.status = '草稿'

            let response
            if (this.isEdit) {
              // 编辑模式 - 调用更新接口
              response = await updateDataEntry(this.form)
            } else {
              // 新增模式 - 调用新增接口
              response = await addDataEntry(this.form)
            }

            if (response.result == 200) {
              this.$message.success(this.isEdit ? '保存成功' : '草稿保存成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          } catch (error) {
            this.$message.error('保存失败：' + (error.message || '未知错误'))
          } finally {
            this.loading = false
          }
        }
      })
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          this.loading = true
          try {
            let response
            if (this.isEdit) {
              // 编辑模式：先更新数据，再提交
              await updateDataEntry(this.form)
              response = await request({
                url: '/monitor/v1/enterprise/data/entry/submit',
                method: 'post',
                headers: { 'Content-Type': 'application/json;charset=UTF-8' },
                data: { id: this.editData.id }
              })
            } else {
              // 新增模式：直接以已提交状态新增
              this.form.status = '已提交'
              this.form.submitTime = new Date().toISOString()
              response = await addDataEntry(this.form)
            }

            if (response.result == 200) {
              this.$message.success('数据提交成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '提交失败')
            }
          } catch (error) {
            this.$message.error('数据提交失败：' + (error.message || '未知错误'))
          } finally {
            this.loading = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.upload-demo {
  margin-top: 10px;
}
</style>
