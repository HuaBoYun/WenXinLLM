<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="4">
      <el-form
        ref="ruleForm"
        label-width="120px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="序号" prop="requirementNo">
            <el-input
              v-model="formData.requirementNo"
              clearable
              disabled
              placeholder="请输入序号"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计事项" prop="auditItem">
            <el-input
              v-model="formData.auditItem"
              clearable
              placeholder="请输入审计事项"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计目的" prop="auditPurpose">
            <el-input
              v-model="formData.auditPurpose"
              clearable
              placeholder="请输入审计目的"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              readonly
              clearable
              placeholder="请选择单位"
              :style="{ width: '75%' }"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 15px"
              type="primary"
              size="mini"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重点关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入重点关注内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位范围" prop="unitRange">
            <el-input
              v-model="formData.unitRange"
              clearable
              placeholder="请输入单位范围"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间范围" prop="timeRange">
            <el-date-picker
              v-model="formData.timeRange"
              placeholder="请选择时间范围"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              range-separator="~"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%', height: '29px' }"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目类别" prop="projectType">
            <el-select
              v-model="formData.projectType"
              placeholder="项目类别"
              :style="{ width: '100%' }"
            >
              <el-option label="工程类" value="工程类" />
              <el-option label="财务类" value="财务类" />
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUserName">
            <el-input
              v-model="formData.createUserName"
              disabled
              placeholder="请输入创建人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              placeholder="请输入创建时间"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary" :loading="loading">
        确定
      </el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
  </el-dialog>
</template>

<script>
  import SelectDepartment from './department.vue'
  import { saveOrUpdate, detail, getAutoNo } from '@/api/oilAudit/jhgl/fwxqb'

  export default {
    name: 'gcjgysjhEdit',
    inheritAttrs: false,
    components: { SelectDepartment },
    data() {
      return {
        loading: false,
        formData: {
          requirementNo: '',
          auditItem: '',
          auditPurpose: '',
          organizationId: '',
          organizationName: '',
          concernsContent: '',
          unitRange: '',
          timeRange: [],
          projectType: '',
          remark: '',
          createUserName: '',
          createTime: '',
        },
        id: '',
        formDisabled: true,
        rules: {
          auditItem: [
            {
              required: true,
              message: '请输入审计事项',
              trigger: 'blur',
            },
          ],
          // auditPurpose: [
          //   {
          //     required: true,
          //     message: '请输入建设单位',
          //     trigger: 'blur',
          //   },
          // ],
          organizationName: [
            {
              required: true,
              message: '请选择单位',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      async getNumber() {
        const { msg, code, data } = await getAutoNo({})
        if (code == 1) {
          this.formData.requirementNo = data
          this.$forceUpdate()
        } else {
          this.$baseMessage(msg, 'error')
        }
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
          const res = await detail({ id: row.id })
          this.title = disabled ? '详细' : '编辑'
          this.id = row.id
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.data[key]
          })
          this.formData.requirementNo = res.data.data.requirementNo
          this.formData.createUserName = res.data.data.createUser?.realname
          this.formData.organizationName = res.data.data.organization?.orgname
          this.formData.timeRange = res.data.data.timeRange?.split('~')
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUserName = createUserName
          this.formData.createTime = createTime
          this.getNumber()
        }
      },
      close() {
        this.formData = {
          auditItem: '',
          auditPurpose: '',
          organizationName: '',
          concernsContent: '',
          unitRange: '',
          timeRange: [],
          projectType: '',
          remark: '',
          createUserName: '',
          createTime: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = true
        this.id = ''
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            params.timeRange = params.timeRange?.join('~')
            if (this.title === '编辑') params.id = this.id
            const res = await saveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getDepartmentInfo(val) {
        this.formData.organizationId = val.id
        this.formData.organizationName = val.label
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
