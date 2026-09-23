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
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="135px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="销假人" prop="peopleName">
            <el-input
              v-model="formData.peopleName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择销假人"
            />
            <!-- <el-button
              type="primary"
              style="margin-left: 20px"
              @click="$refs.executor.showEdit()"
            >
              选择
            </el-button> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属部门" prop="peopleWorkUnitName">
            <el-input
              v-model="formData.peopleWorkUnitName"
              :style="{ width: '100%' }"
              disabled
              clearable
              placeholder="请选择所属部门"
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
          <el-form-item label="销假日期" prop="fillFormTime">
            <el-date-picker
              v-model="formData.fillFormTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="请选择销假日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="销假状态" prop="holidayType">
            <el-input
              v-model="formData.holidayType"
              :style="{ width: '100%' }"
              disabled
              placeholder="销假状态"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="销假" prop="cancelHolidayType">
            <el-radio-group v-model="formData.cancelHolidayType">
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
          <el-form-item label="事由" prop="reasons">
            <el-input
              v-model="formData.reasons"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入事由"
              type="textarea"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="footer">
      <el-button @click="close" v-if="!state">取消</el-button>
      <el-button @click="save" type="primary" v-if="!state">确定</el-button>
      <el-button
        @click="handleApproval()"
        v-if="!state && editId"
        type="primary"
      >
        提交审批
      </el-button>
      <el-button @click.native="handleManage(row)" v-if="state" type="primary">
        办理
      </el-button>
    </div>
    <!-- 人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
    <ProcessList ref="process" @fetchData="close" />
    <WfqdDeal ref="wfqddeal" />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/xjd.js'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  import { getFlowPkInfo } from '@/api/setting/system.js'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { changeStatus } from '@/oapi/ypns_zhgl/ryqjd'
  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      ProcessList,
      WfqdDeal,
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
          people: '',
          peopleName: '',
          peopleWorkUnit: '',
          peopleWorkUnitName: '',
          fillFormTime: '',
          cancelHolidayType: '',
          reasons: '',
          id: '',
          cancelType: '1',
          relationId: '',
          holidayType: '',
        },
        radio: '',
        footer: true,
        rules: {
          peopleName: [
            {
              required: true,
              message: '请选择销假人',
              trigger: ['blur', 'change'],
            },
          ],
          peopleWorkUnitName: [
            {
              required: true,
              message: '请选择所属部门',
              trigger: ['blur', 'change'],
            },
          ],
          fillFormTime: [
            {
              required: true,
              message: '选择填表日期',
              trigger: ['blur', 'change'],
            },
          ],
          cancelHolidayType: [
            {
              required: true,
              message: '选择销假类型',
              trigger: ['blur', 'change'],
            },
          ],
          reasons: [
            {
              required: true,
              message: '请输入事由',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        editId: '',
        state: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async handleExecutorSelected(node) {
        this.$set(this.formData, 'peopleName', node[0].realname)
        this.$set(this.formData, 'people', node[0].staffid)
        this.$forceUpdate()
        const res = await getUserDetailInfo({ id: node[0].staffid })
        this.formData.peopleWorkUnitName = res.data.workUnitName
        this.formData.peopleWorkUnit = res.data.orgId
        this.$forceUpdate()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, `peopleWorkUnitName`, node.label)
        this.$set(this.formData, `peopleWorkUnit`, node.id)
        this.$forceUpdate()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, type) {
        this.dialogFormVisible = true
        this.formData.cancelType = type
        this.formData.peopleName = row.staff.realName
        this.formData.people = row.staff.staffId
        this.formData.peopleWorkUnitName = row.staff.workUnitName
        this.formData.peopleWorkUnit = row.staff.orgId
        this.formData.relationId = row.id
        this.formData.cancelHolidayType = row.leaveReason

        this.title = '新增'
        if (row.relationId) {
          this.title = '修改'
          const res = await getInfoDetail({ id: row.relationId })
          if (res.data) {
            this.formData.people = res.data.people
            this.formData.peopleName = res.data.peopleName
            this.formData.peopleWorkUnit = res.data.peopleWorkUnit
            this.formData.peopleWorkUnitName = res.data.peopleWorkUnitName
            this.formData.fillFormTime = res.data.fillFormTime
            this.formData.cancelHolidayType = res.data.cancelHolidayType
            this.formData.reasons = res.data.reasons
            this.formData.id = res.data.id
            this.formData.holidayType = res.data.holidayType
            this.editId = res.data.id
            this.state = res.data.state
          }
        }
      },
      close() {
        this.loading = false
        this.formData = {
          people: '',
          peopleName: '',
          peopleWorkUnit: '',
          peopleWorkUnitName: '',
          fillFormTime: '',
          cancelHolidayType: '',
          reasons: '',
          id: '',
          cancelType: '1',
          relationId: '',
          holidayType: '',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.editId = res.data.id
              this.formData.holidayType = res.data.holidayType
              this.$emit('fetchData')
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
          }
        })
      },
      handleApproval() {
        this.loading = true
        //提交审批
        this.$refs['process'].save(137, this.editId)
        changeStatus({ onDutyStatus: 3, userId: this.formData.people })
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: this.editId,
          tableId: 137,
        })
        this.$refs.wfqddeal.show(res.data, false)
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
