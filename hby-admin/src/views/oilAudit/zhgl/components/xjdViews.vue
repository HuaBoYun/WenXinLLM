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
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="请选择销假日期"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
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
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
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

  import { editInfor, getInfoDetail } from '@/oapi/ypns_zhgl/xjd.js'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import ExecutorOptions from '@/components/danxuanPerson.vue'
  import { getUserDetailInfo } from '@/oapi/ypns_zhgl/filePublic'
  const token = store.getters['user/token']
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
          people: '',
          peopleName: '',
          peopleWorkUnit: '',
          peopleWorkUnitName: '',
          fillFormTime: '',
          cancelHolidayType: '',
          reasons: '',
          id: '',
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
      async showEdit(row, title) {
        this.dialogFormVisible = true
        if (row) {
          this.editId = row.id
          const res = await getInfoDetail({ id: row.id })
          this.formData.people = res.data.people
          this.formData.peopleName = res.data.peopleName
          this.formData.peopleWorkUnit = res.data.peopleWorkUnit
          this.formData.peopleWorkUnitName = res.data.peopleWorkUnitName
          this.formData.fillFormTime = res.data.fillFormTime
          this.formData.cancelHolidayType = res.data.cancelHolidayType
          this.formData.reasons = res.data.reasons
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
          console.log('🚀 ~ showEdit ~ userInfo:', userInfo)
          this.formData = {
            ...this.formData,
            peopleName: userInfo.realname,
            people: userInfo.staffid,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
            peopleWorkUnitName: userInfo.linkDetp?.orgname,
            peopleWorkUnit: userInfo.linkDetp?.orgid,
          }
        }
      },
      close() {
        this.formData = {
          people: '',
          peopleName: '',
          peopleWorkUnit: '',
          peopleWorkUnitName: '',
          fillFormTime: '',
          cancelHolidayType: '',
          reasons: '',
          id: '',
        }
        this.editId = ''
        this.dialogFormVisible = false
        this.footer = true
        this.$emit('fetchData')
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            // return
            const params = JSON.parse(JSON.stringify(this.formData))
            const res = await editInfor(params)
            if (res && res.code == 200) {
              this.editId = res.data.id
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
        this.$refs['process'].save(137, this.editId)
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
