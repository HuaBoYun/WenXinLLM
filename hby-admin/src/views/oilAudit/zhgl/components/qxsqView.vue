<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="申请人" prop="applyName">
            <el-input
              v-model="formData.applyName"
              :style="{ width: '75%' }"
              disabled
              placeholder="请选择申请人"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="applyWorkUnitName">
            <el-input
              v-model="formData.applyWorkUnitName"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择部门"
            />
            <!-- <el-button type="primary" style="margin-left: 20px">选择</el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请角色" prop="applyRole">
            <el-input
              v-model="formData.applyRole"
              :style="{ width: '100%' }"
              placeholder="请选择申请角色"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请时间" prop="applyTime">
            <el-input
              v-model="formData.applyTime"
              :style="{ width: '100%' }"
              disabled
              placeholder="请选择申请时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="情况说明" prop="remark">
            <el-input
              v-model="formData.remark"
              :style="{ width: '100%' }"
              placeholder="请输入情况说明"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" :loading="loading">确定</el-button>
      <el-button
        @click="handleApproval"
        type="primary"
        :disabled="!this.editId"
      >
        提交审批
      </el-button>
    </div>
    <!-- 人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <ProcessList ref="process" @fetchData="close" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/qxsqd.js'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  const token = store.getters['user/token']

  export default {
    components: { DepartmentOptions, ExecutorOptions, ProcessList },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          applyId: '',
          applyRole: '',
          applyName: '',
          applyTime: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          remark: '',
          id: '',
        },
        footer: true,
        rules: {
          applyName: [
            {
              required: true,
              message: '请选择申请人',
              trigger: ['blur', 'change'],
            },
          ],
          applyRole: [
            {
              required: true,
              message: '请输入角色',
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
      // 人员选择
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'applyName', node[0].realname)
        this.$set(this.formData, 'applyId', node[0].staffid)
        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.applyWorkUnitName = res.data.workUnitName
        // this.formData.applyWorkUnit = res.data.applyWorkUnit
        this.$forceUpdate()
      },
      // 部门选择
      handleDepartmentSelected(node) {
        this.$set(this.formData, `applyWorkUnitName`, node.name)
        this.$set(this.formData, `applyWorkUnit`, node.id)
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })
          this.formData.id = res.data.id
          this.formData.applyId = res.data.applyId
          this.formData.applyRole = res.data.applyRole
          this.formData.applyName = res.data.applyName
          this.formData.applyTime = res.data.applyTime
          this.formData.applyWorkUnit = res.data.applyWorkUnit
          this.formData.applyWorkUnitName = res.data.applyWorkUnitName
          this.formData.remark = res.data.remark
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            // createdUser: resL,
            applyTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          applyId: '',
          applyRole: '',
          applyName: '',
          applyTime: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          remark: '',
          id: '',
        }
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.editId = ''
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.id = res.data.id
              this.$message({
                message: '保存成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '保存失败',
                type: 'error',
              })
            }
            this.loading = false
          }
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(147, this.editId)
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
