<template>
  <div>
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
          <el-form-item label="申请人" prop="peopleName">
            <el-input
              v-model="formData.peopleName"
              :style="{ width: '100%' }"
              clearable
              disabled
              placeholder="申请人"
            />
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
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际返回时间" prop="fillFormTime">
            <el-date-picker
              v-model="formData.fillFormTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="实际返回时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="返回状态" prop="holidayType">
            <el-input
              v-model="formData.holidayType"
              :style="{ width: '100%' }"
              disabled
              placeholder="返回状态"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="销假" prop="cancelHolidayType">
            <el-radio-group v-model="formData.cancelHolidayType">
              <el-radio label="事假">事假</el-radio>
              <el-radio label="病假">病假</el-radio>
              <el-radio label="婚假">婚假</el-radio>
              <el-radio label="产假">产假</el-radio>
              <el-radio label="丧假">丧假</el-radio>
              <el-radio label="其他">其他</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col> -->

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
    <div slot="footer" v-if="footer" style="text-align: right">
      <el-button @click="save" type="primary">确定</el-button>
      <el-button @click="ymsubmit" type="primary">提交</el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <!-- 人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/wpxjd.js'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'

  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOptions,
      ExecutorOptions,
      Resubmit,
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
          // cancelHolidayType: '',
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
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        // this.dialogFormVisible = true
        // this.formData.cancelType = type
        // this.formData.peopleName = row.applyPeopleName
        // this.formData.people = row.applyPeople
        // this.formData.peopleWorkUnitName = row.applyWorkUnitName
        // this.formData.peopleWorkUnit = row.applyWorkUnit
        // this.formData.relationId = row.id
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.footer = isWfqdedit
        if (formId) {
          this.title = '修改'
          const res = await getInfoDetail({ id: formId })
          if (res.data) {
            this.formData.people = res.data.people
            this.formData.peopleName = res.data.peopleName
            this.formData.peopleWorkUnit = res.data.peopleWorkUnit
            this.formData.peopleWorkUnitName = res.data.peopleWorkUnitName
            this.formData.fillFormTime = res.data.fillFormTime
            // this.formData.cancelHolidayType = res.data.cancelHolidayType
            this.formData.reasons = res.data.reasons
            this.formData.id = res.data.id
            this.formData.holidayType = res.data.holidayType
            this.formData.relationId = res.data.relationId
            this.editId = res.data.id
            this.state = res.data.state
            this.formData.cancelType = 2
          }
        }
        if (title == 'detail') {
          this.footer = false
        }
      },
      close() {
        this.formData = {
          people: '',
          peopleName: '',
          peopleWorkUnit: '',
          peopleWorkUnitName: '',
          fillFormTime: '',
          // cancelHolidayType: '',
          reasons: '',
          id: '',
          cancelType: '1',
          relationId: '',
          holidayType: '',
        }
        this.dialogFormVisible = false
        this.$bus.$emit('updateMsg', 0)
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
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
      //提交审批
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
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
