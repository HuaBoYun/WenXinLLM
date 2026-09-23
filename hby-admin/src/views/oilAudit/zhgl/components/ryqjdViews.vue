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
        <el-col :span="12" style="height: 29px">
          <el-form-item label="员工姓名" prop="staffName">
            <el-input
              v-model="formData.staffName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请输入员工姓名"
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
          <el-form-item label="员工编号" prop="staffNumber">
            <el-input
              v-model="formData.staffNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入员工编号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" prop="staffsex">
            <el-input
              v-model="formData.staffsex"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入性别"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生年月" prop="birthday">
            <el-date-picker
              v-model="formData.birthday"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择出生年月"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="参工时间" prop="parworkdate">
            <el-date-picker
              v-model="formData.parworkdate"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择参工时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="累计工作时间" prop="acotworktime">
            <el-input
              v-model="formData.acotworktime"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入累计工作时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位及职务" prop="orgdutie">
            <el-input
              v-model="formData.orgdutie"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入单位及职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="休假地点" prop="holidaylocation">
            <el-input
              v-model="formData.holidaylocation"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入休假地点"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="部门" prop="deptName" style="height: 28px">
            <el-input
              v-model="formData.deptName"
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
          <el-form-item label="请假期限" prop="leavePeriodTimeStart">
            <el-date-picker
              v-model="leavePeriodTime"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择请假期限"
              :style="{ width: '100%' }"
              @change="selectTimeLimit"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计请假天数" prop="leavedays">
            <el-input
              v-model="formData.leavedays"
              :style="{ width: '100%' }"
              disabled
              placeholder="预计请假天数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际请假天数" prop="actualleavedays">
            <el-input
              v-model="formData.actualleavedays"
              :style="{ width: '100%' }"
              disabled
              placeholder="实际请假天数"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="请假事由" prop="leaveReason">
            <el-radio-group v-model="formData.leaveReason">
              <el-radio label="事假">事假</el-radio>
              <el-radio label="病假">病假</el-radio>
              <el-radio label="带薪年休假">带薪年休假</el-radio>
              <el-radio label="疗养假">疗养假</el-radio>
              <el-radio label="婚假">婚假</el-radio>
              <el-radio label="产假">产假</el-radio>
              <el-radio label="节育假">节育假</el-radio>
              <el-radio label="哺乳假">哺乳假</el-radio>
              <el-radio label="哺育延长假">哺育延长假</el-radio>
              <el-radio label="探亲假">探亲假</el-radio>
              <el-radio label="护理假">护理假</el-radio>
              <el-radio label="陪护假">陪护假</el-radio>
              <el-radio label="生理假">生理假</el-radio>
              <el-radio label="育儿假">育儿假</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-form-item label="简要说明" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :style="{ width: '100%' }"
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
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  // import ExecutorOptions from '@/views/oilAudit/report/components/options/executor.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'

  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/ryqjd'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'

  const token = store.getters['user/token']

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
          leaveReason: '',
          staffNumber: '',
          leavePeriodTimeStart: '',
          leavePeriodTimeEnd: '',
          remark: '',
          id: '',
          staffId: '',
          staffName: '',
          deptName: '',
          staffsex: '',
          parworkdate: '',
          acotworktime: '',
          birthday: '',
          orgdutie: '',
          holidaylocation: '',
          leavedays: '',
        },
        leavePeriodTime: [],
        radio: '',
        footer: true,
        rules: {
          staffName: [
            {
              required: true,
              message: '请选择员工',
              trigger: ['blur', 'change'],
            },
          ],
          staffNumber: [
            {
              required: true,
              message: '请输入员工编号',
              trigger: 'blur',
            },
          ],
          leavePeriodTimeStart: [
            {
              required: true,
              message: '请选择请假期限',
              trigger: ['blur', 'change'],
            },
          ],
          leaveReason: [
            {
              required: true,
              message: '请选择请假事由',
              trigger: ['blur', 'change'],
            },
          ],
          parworkdate: [
            {
              required: true,
              message: '请选择参工时间',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
      }
    },
    methods: {
      selectTimeLimit(val) {
        if (val && val.length) {
          this.$set(this.formData, 'leavePeriodTimeStart', val[0])
          this.$set(this.formData, 'leavePeriodTimeEnd', val[1])
        }
      },
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'staffName', node[0].realname)
        this.$set(this.formData, 'staffId', node[0].staffid)

        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.deptName = res.data.workUnitName
        this.$forceUpdate()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, `deptName`, node.label)
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
          this.formData.staffsex = res.data.staffsex
          this.formData.leavedays = res.data.leavedays
          this.formData.actualleavedays = res.data.actualleavedays
          this.formData.parworkdate = res.data.parworkdate
          this.formData.acotworktime = res.data.acotworktime
          this.formData.birthday = res.data.birthday
          this.formData.orgdutie = res.data.orgdutie
          this.formData.holidaylocation = res.data.holidaylocation
          this.formData.leaveReason = res.data.leaveReason
          this.formData.staffNumber = res.data.staffNumber
          this.formData.staffName = res.data.staff.realName
          this.formData.staffId = res.data.staffId
          this.formData.deptName = res.data.staff.workUnitName
          this.formData.leavePeriodTimeStart = res.data.leavePeriodTimeStart
          this.formData.leavePeriodTimeEnd = res.data.leavePeriodTimeEnd
          if (res.data.leavePeriodTimeStart && res.data.leavePeriodTimeEnd) {
            this.leavePeriodTime = [
              res.data.leavePeriodTimeStart,
              res.data.leavePeriodTimeEnd,
            ]
          }
          this.formData.remark = res.data.remark
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
            staffName: userInfo.realname,
            staffNumber: userInfo.username,
            staffId: userInfo.staffid,
            deptName: userInfo.linkDetp?.orgname,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData = {
          leaveReason: '',
          staffNumber: '',
          staffName: '',
          leavePeriodTimeStart: '',
          leavePeriodTimeEnd: '',
          remark: '',
          deptName: '',
          staffId: '',
          id: '',
          staffsex: '',
          parworkdate: '',
          acotworktime: '',
          birthday: '',
          orgdutie: '',
          holidaylocation: '',
          leavedays: '',
        }
        this.editId = ''
        this.leavePeriodTime = []
        this.dialogFormVisible = false
        this.loading = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          console.log(this.formData)
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
        this.$refs['process'].save(37, this.editId)
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
