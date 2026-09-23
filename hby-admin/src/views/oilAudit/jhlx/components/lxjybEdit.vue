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
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12">
          <el-form-item label="审计项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入审计项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="立项理由及审计目的" prop="projectPurpose">
            <el-input
              v-model="formData.projectPurpose"
              clearable
              placeholder="请输入立项理由及审计目的"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="重点关注内容" prop="concernsContent">
            <el-input
              v-model="formData.concernsContent"
              clearable
              placeholder="请输入重点关注内容"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4"
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
          <el-form-item label="填报单位" prop="tborgname">
            <el-input
              v-model="formData.tborgname"
              placeholder="请选择填报单位"
              :style="{ width: '75%' }"
              readonly
              disabled
            />
            <el-button
              type="primary"
              size="mini"
              style="margin-left: 10px"
              @click="$refs.audiTree.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间范围" prop="timeRange">
            <el-input
              v-model="formData.timeRange"
              clearable
              placeholder="请输入时间范围"
              :style="{ width: '100%' }"
            />
            <!-- <el-date-picker
              v-model="formData.timeRange"
              placeholder="请选择时间范围"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              range-separator="~"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            /> -->
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="工程/财务" prop="projectType">
            <el-select
              v-model="formData.projectType"
              placeholder="工程/财务"
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
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <!-- 单位选择 -->
    <SelectDepartment ref="audiTree" @submit="handleUnitSelected" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { saveOrUpdate, detail } from '@/api/oilAudit/jhgl/lxjyb'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    name: 'lxjybEdit',
    inheritAttrs: false,
    components: { SelectDepartment, ProcessList },
    data() {
      return {
        loading: false,
        formData: {
          projectName: '',
          projectPurpose: '',
          concernsContent: '',
          unitRange: '',
          timeRange: '',
          projectType: '',
          remark: '',
          createUserName: '',
          createTime: '',
          tborgname: '',
          tborgid: '',
        },
        id: '',
        formDisabled: true,
        rules: {
          projectName: [
            {
              required: true,
              message: '请输入审计项目名称',
              trigger: 'blur',
            },
          ],
          projectPurpose: [
            {
              required: true,
              message: '请输入立项理由及审计目的',
              trigger: 'blur',
            },
          ],
          unitRange: [
            {
              required: true,
              message: '请输入单位范围',
              trigger: 'blur',
            },
          ],
          tborgname: [
            {
              required: true,
              message: '请选择填报单位',
              trigger: 'change',
            },
          ],
          timeRange: [
            {
              required: true,
              message: '请输入时间范围',
              trigger: 'blur',
            },
          ],
          projectType: [
            {
              required: true,
              message: '请选择工程/财务',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    methods: {
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
          this.editId = row.id
          const res = await detail({ id: row.id })
          this.title = disabled ? '详细' : '编辑'
          this.id = row.id
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.data[key]
          })
          this.formData.createUserName = res.data.data.createUser?.realname
          this.formData.timeRange = res.data.data.timeRange
          // this.formData.timeRange = res.data.data.timeRange?.split('~')
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUserName = createUserName
          this.formData.createTime = createTime
          this.formData.tborgname = userInfo.linkOrg.orgname
          this.formData.tborgid = userInfo.linkOrg.orgid
        }
      },
      close() {
        this.formData = {
          projectName: '',
          projectPurpose: '',
          concernsContent: '',
          unitRange: '',
          timeRange: '',
          projectType: '',
          remark: '',
          creator: '',
          createTime: '',
          tborgname: '',
          tborgid: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = true
        this.id = ''
        this.editId = ''
        this.$emit('fetchData')
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            let params = { ...this.formData }
            // params.timeRange = params.timeRange?.join('~')
            params.timeRange = params.timeRange
            if (this.title === '编辑') params.id = this.id
            const res = await saveOrUpdate(params)
            this.loading = false
            if (res && res.code == 1) {
              this.$message.success('保存成功！')
              this.editId = res.data.id
              this.$emit('fetchData')
              this.close()
            } else {
              this.$message.error(res.msg || '保存失败！')
            }
          }
        })
      },
      /**
       * @description: 选择单位
       * @param {*}
       * @return {*}
       */
      handleUnitSelected(node) {
        this.formData.tborgname = node.label
        this.formData.tborgid = node.id
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(102, this.editId)
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
