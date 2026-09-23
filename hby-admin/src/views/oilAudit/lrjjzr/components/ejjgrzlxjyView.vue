<template>
  <!-- 二级机构中立 edit -->
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
        style="display: flex;flex-wrap: wrap;"
      >
        <el-col :span="12">
          <el-form-item label="单位">
            <el-input
              v-model="formData.orgname"
              clearable
              placeholder="请输入单位"
              :style="{ width: '266px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="showGroupLeader"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" prop="job">
            <el-input
              v-model="formData.job"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="行政级别" prop="level">
            <el-input
              v-model="formData.level"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入行政级别"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任职时间" prop="OfficeDateRange">
            <el-date-picker
              v-model="formData.OfficeDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="任职时间开始日期"
              end-placeholder="任职时间结束日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截至目前任职时间" prop="workDuration">
            <el-input
              v-model="formData.workDuration"
              :style="{ width: '100%' }"
              placeholder="请输入截至目前任职时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否存在重大财务异常" prop="hasFinanceProblem">
            <el-radio-group v-model="formData.hasFinanceProblem">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否发生重大经济事项" prop="unit">
            <el-radio-group v-model="formData.hasEconomicProblem">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否受到投诉举报" prop="officeDate">
            <el-radio-group v-model="formData.hasBeenComplain">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="次年是否离任" prop="isLeaveNextYear">
            <el-radio-group v-model="formData.isLeaveNextYear">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否开展任中审计" prop="needAudit">
            <el-radio-group v-model="formData.needAudit">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="项目类型" prop='projectType'>
            <el-input
              v-model="formData.projectType"
              disabled
              placeholder="请选择"
              :style="{ width: '272px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click.native="showtypeView"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              type="date"
              readonly
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注"  prop="remarks">
            <el-input
              v-model="formData.remarks"
              clearable
              placeholder="请输入备注"
              :style="{ width: '100%' }"
              type="textarea"
              :rows="4" 
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- 选择单位（公司） -->
    <!-- <company-select-modal
      ref="companySelect"
      @selected="handleCompanyTreeSelected"
    /> -->
    <typeView ref="typeView" @submit="setType" />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { ejjgrzlxjyDetail, ejjgrzlxjyUpdate } from '@/oapi/audit/plan'
  import { formatDay } from '@/utils/index'
  // import CompanySelectModal from '@/components/CampanySelectModal'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import { baseURL } from '@/config'
  import store from '@/store'
    import typeView from '@/views/oilAudit/lrjjzr/components/type'
  // import {
  //   editDataSource,
  //   getDataSourceDefaultInfo,
  //   LinkTest,
  // } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: { SelectDepartment,typeView },
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
          id: '',
          orgid: '',
          orgname: '',
          name: '',
          job: '',
          level: '',
          workStartTime: '',
          workEndTime: '',
          OfficeDateRange: [],
          workDuration: null,
          hasFinanceProblem: '',
          hasEconomicProblem: '',
          hasBeenComplain: '',
          isLeaveNextYear: '',
          needAudit: '',
          createUser: '',
          createTime: '',
          projectType:'',
          remarks:'',
        },
        footer: true,
        rules: {
          org: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入原职务',
              trigger: 'blur',
            },
          ],
          job: [
            {
              required: true,
              message: '请输入原行政级别',
              trigger: 'blur',
            },
          ],
          level: [
            {
              required: true,
              message: '请输入原单位',
              trigger: 'blur',
            },
          ],
          OfficeDateRange: [
            {
              type: 'array',
              required: true,
              message: '请选择任职时间范围',
              fields: {
                // tpye类型试情况而定,所以如果返回的是date就改成date
                0: { type: 'date', required: true, message: '请选择开始日期' },
                1: { type: 'date', required: true, message: '请选择结束日期' }
              }
            },
          ],
          hasFinanceProblem: [
            {
              required: true,
              message: '请选择是否存在重大财务异常',
              trigger: 'blur',
            },
          ],
          hasEconomicProblem: [
            {
              required: true,
              message: '请选择是否发生重大经济事项',
              trigger: 'blur',
            },
          ],
          hasBeenComplain: [
            {
              required: true,
              message: '请选择是否收到投诉举报',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      setType(e){
      this.formData.projectType = e.auditType
      this.$forceUpdate()
      },
      showtypeView(){
        this.$refs['typeView'].showEdit()
      },
      // 选公司
      showGroupLeader() {
        this.$refs['audiTree'].showEdit()
        // this.$refs.companySelect.show({
        //   labelKey: 'orgname',
        //   // idKey: 'company',
        //   title: '单位',
        // })
      },
      // 选公司后处理
      // 选公司后处理
      getDepartmentInfo(val) {
        console.log('getDepartmentInfo val', val)
        this.formData.orgname = val.name
        this.formData.orgId = val.id
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title) {
        console.log('row', row)
        this.dialogFormVisible = true
        if (row) {
          this.loading = true
          ejjgrzlxjyDetail({id: row.id}).then(res => {
            Object.assign(this.formData, res.data.data)
            this.formData.orgid = res.data.data.org ? res.data.data.org.orgid : ''
            this.formData.orgname = res.data.data.org ? res.data.data.org.orgname : ''
            this.formData.createUser = res.data.data.createUser ? res.data.data.createUser.realname : ''
            this.$set(this.formData, 'OfficeDateRange', [new Date(res.data.data.workStartTime), new Date(res.data.data.workEndTime)])
          }).finally(() => {
            this.loading = false
          })
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData.createUser = resL
          this.formData.createTime = this.getCurrentDate()
        }
      },
      close() {
        this.formData = {
          id: '',
          orgid: '',
          orgname: '',
          name: '',
          job: '',
          level: '',
          workStartTime: '',
          workEndTime: '',
          OfficeDateRange: [],
          workDuration: null,
          hasFinanceProblem: '',
          hasEconomicProblem: '',
          hasBeenComplain: '',
          isLeaveNextYear: '',
          needAudit: '',
          createUser: '',
          createTime: '',
          projectType:'',
          remarks:'',
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            params.workStartTime = formatDay(this.formData.OfficeDateRange[0])
            params.workEndTime = formatDay(this.formData.OfficeDateRange[1])
            delete params['createUser']
            const res = await ejjgrzlxjyUpdate(params)
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$message({
                message: '提交成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '提交失败',
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
