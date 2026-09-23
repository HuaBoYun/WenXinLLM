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
          <el-form-item label="申请人" prop="applyPeopleName">
            <el-input
              v-model="formData.applyPeopleName"
              :style="{ width: '75%' }"
              clearable
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
              clearable
              disabled
              placeholder="请选择部门"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.department.show()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="岗位" prop="post">
            <el-input
              v-model="formData.post"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入岗位"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用人单位" prop="useBelongGroup">
            <el-input
              v-model="formData.useBelongGroup"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用人单位"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="安排外出时间" prop="applyExpatriateTime">
            <el-date-picker
              v-model="formData.applyExpatriateTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择安排外出时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="安排返回时间" prop="applyReturnTime">
            <el-date-picker
              v-model="formData.applyReturnTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择安排返回时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="实际返回时间" prop="actualReturnTime">
            <el-date-picker
              v-model="formData.actualReturnTime"
              type="datetime"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择实际返回时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="12">
          <el-form-item label="预计外派天数" prop="leavedays">
            <el-input
              v-model="formData.leavedays"
              :style="{ width: '100%' }"
              disabled
              placeholder="预计请假天数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际外派天数" prop="actualleavedays">
            <el-input
              v-model="formData.actualleavedays"
              :style="{ width: '100%' }"
              disabled
              placeholder="实际请假天数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="外出事由" prop="expatriateReason">
            <el-input
              v-model="formData.expatriateReason"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入外出事由"
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
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/wpsqd.js'
  const token = store.getters['user/token']
  import * as dayjs from 'dayjs'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      ProcessList,
    },
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
          applyPeople: '',
          applyPeopleName: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          post: '',
          applyExpatriateTime: '',
          applyReturnTime: '',
          actualReturnTime: '',
          expatriateReason: '',
          id: '',
          useBelongGroup: '',
          leavedays: '',
          actualleavedays: '',
        },
        radio: '',
        footer: true,
        rules: {
          applyPeopleName: [
            {
              required: true,
              message: '请选择申请人',
              trigger: ['blur', 'change'],
            },
          ],
          applyWorkUnitName: [
            {
              required: true,
              message: '请选择部门',
              trigger: ['blur', 'change'],
            },
          ],
          post: [
            {
              required: true,
              message: '请输入岗位',
              trigger: 'blur',
            },
          ],
          applyExpatriateTime: [
            {
              required: true,
              message: '选择申请外出时间',
              trigger: ['blur', 'change'],
            },
          ],
          applyReturnTime: [
            {
              required: true,
              message: '选择申请返回时间',
              trigger: ['blur', 'change'],
            },
          ],
          actualReturnTime: [
            {
              required: true,
              message: '选择实际返回时间',
              trigger: ['blur', 'change'],
            },
          ],
          expatriateReason: [
            {
              required: true,
              message: '请输入外出事由',
              trigger: 'blur',
            },
          ],
          useBelongGroup: [
            {
              required: true,
              message: '请输入用人单位',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'applyPeopleName', node[0].realname)
        this.$set(this.formData, 'applyPeople', node[0].staffid)
        this.$forceUpdate()

        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.applyWorkUnitName = res.data.workUnitName
        this.formData.applyWorkUnit = res.data.orgId
        this.$forceUpdate()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, `applyWorkUnitName`, node.label)
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

          this.formData.applyPeople = res.data.applyPeople
          this.formData.leavedays = res.data.leavedays
          this.formData.actualleavedays = res.data.actualleavedays
          this.formData.applyPeopleName = res.data.applyPeopleName
          this.formData.applyWorkUnit = res.data.applyWorkUnit
          this.formData.applyWorkUnitName = res.data.applyWorkUnitName
          this.formData.post = res.data.post
          this.formData.useBelongGroup = res.data.useBelongGroup
          // this.formData.applyExpatriateTime = dayjs(
          //   res.data.applyExpatriateTime
          // ).format('YYYY-MM-DD HH:mm:ss')
          // this.formData.applyReturnTime = dayjs(
          //   res.data.applyReturnTime
          // ).format('YYYY-MM-DD HH:mm:ss')
          // this.formData.actualReturnTime = dayjs(
          //   res.data.actualReturnTime
          // ).format('YYYY-MM-DD HH:mm:ss')
          this.formData.applyExpatriateTime = res.data.applyExpatriateTime
          this.formData.applyReturnTime = res.data.applyReturnTime
          this.formData.actualReturnTime = res.data.actualReturnTime
          this.formData.expatriateReason = res.data.expatriateReason
          this.formData.id = res.data.id

          // this.formData = JSON.parse(JSON.stringify(row))
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详情'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.formData = {
            ...this.formData,
            applyPeopleName: userInfo.realname,
            applyPeople: userInfo.staffid,
            applyWorkUnitName: userInfo.linkDetp?.orgname,
            applyWorkUnit: userInfo.linkDetp?.orgid,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          applyPeople: '',
          applyPeopleName: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          post: '',
          applyExpatriateTime: '',
          applyReturnTime: '',
          actualReturnTime: '',
          expatriateReason: '',
          useBelongGroup: '',
          id: '',
        }
        this.editId = ''
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
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
      async checkLink() {
        return
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const res = await LinkTest({
              dataBaseConnectionAddress:
                this.formData.dataBaseConnectionAddress,
              dataBasePassWord: this.formData.dataBasePassWord,
              dataBaseType: this.formData.dataBaseType,
              dataBaseUsers: this.formData.dataBaseUsers,
            })
            if (res.code == 1) {
              this.$message({
                message: res.msg,
                type: 'success',
              })
            } else {
              this.$message({
                message: res.msg,
                type: 'error',
              })
            }
          }
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(136, this.editId)
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
