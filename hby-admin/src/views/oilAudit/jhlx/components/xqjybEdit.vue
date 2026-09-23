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
          <el-form-item label="序号">
            <el-input
              v-model="formData.suggestionNo"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label-width="230px"
            label="重点关注领域、项目、事项和风险"
            prop="concerns"
          >
            <el-input
              v-model="formData.concerns"
              clearable
              placeholder="请输入重点关注领域、项目、事项和风险"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入关注内容"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="organizationName">
            <el-input
              v-model="formData.organizationName"
              clearable
              readonly
              placeholder="请选择单位"
              :style="{ width: '80%' }"
            />
            <el-button
              @click="handleObject"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectType">
            <el-select
              v-model="formData.projectType"
              placeholder="请选择项目类型"
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
  import { saveOrUpdate, getAutoNo, detail } from '@/api/oilAudit/jhgl/xqjyb'
  import SelectDepartment from './department.vue'

  export default {
    name: 'xqjybEdit',
    inheritAttrs: false,
    components: { SelectDepartment },
    data() {
      return {
        loading: false,
        formData: {
          concerns: '',
          concernsContent: '',
          organizationId: '',
          organizationName: '',
          remark: '',
          projectType: '',
          createUserName: '',
          createTime: '',
          suggestionNo: '',
        },
        id: '',
        formDisabled: true,
        rules: {
          // concerns: [
          //   {
          //     required: true,
          //     message: '请输入底稿编号',
          //     trigger: 'blur',
          //   },
          // ],
          // field2: [
          //   {
          //     required: true,
          //     message: '请输入建设单位',
          //     trigger: 'blur',
          //   },
          // ],
          // projectType: [
          //   {
          //     required: true,
          //     message: '请输入项目类别',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
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
          this.formData.createUserName = res.data.data.createUser?.realname
          this.formData.organizationName = res.data.data.organization?.orgname
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUserName = createUserName
          this.formData.createTime = createTime

          this.getAutoNo()
        }
      },
      close() {
        this.formData = {
          concerns: '',
          concernsContent: '',
          organizationId: '',
          organizationName: '',
          remark: '',
          projectType: '',
          createUserName: '',
          createTime: '',
          suggestionNo: '',
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      async getAutoNo() {
        const { code, msg, data } = await getAutoNo()
        if (code != 1) return this.$message.error(msg)
        this.formData.suggestionNo = data
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
