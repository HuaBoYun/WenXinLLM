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
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="单位" prop="orgName">
            <el-input
              v-model="formData.orgName"
              readonly
              clearable
              placeholder="请选择单位"
              :style="{ width: '78%' }"
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
          <el-form-item label="现任领导名称" prop="ldname">
            <el-input
              v-model="formData.ldname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入现任领导名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="职务" prop="ldzw">
            <el-input
              v-model="formData.ldzw"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入职务"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="出生年月" prop="csym">
            <el-input
              v-model="formData.csym"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入出生年月"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计二线时间" prop="yjexsj">
            <el-input
              v-model="formData.yjexsj"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入预计二线时间"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任职时间" prop="rzsj">
            <el-input
              v-model="formData.rzsj"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入任职时间"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="审计情况" prop="auditInfo">
            <el-input
              v-model="formData.auditInfo"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计情况"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计时间" prop="auditTime">
            <el-date-picker
              v-model="formData.auditTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择审计时间"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截至本年底未审年限" prop="unauditYear">
            <el-input
              v-model="formData.unauditYear"
              clearable
              placeholder="请输入截至本年底未审年限"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计范围时间" prop="sjfwstarttime">
            <el-date-picker
              v-model="formData.sjfwstarttime"
              type="monthrange"
              class="sjfwstarttime"
              :style="{ width: '100%' }"
              format="yyyy-MM"
              value-format="yyyy-MM"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="未审计月数" prop="unauditMonth">
            <el-input
              v-model="formData.unauditMonth"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入未审计月数"
            />
          </el-form-item>
        </el-col> -->
        <el-col :span="24">
          <el-divider>最后一次审计情况</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="zjprojectname">
            <el-input
              v-model="formData.zjprojectname"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入项目名称"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目类型" prop="projectlx">
            <el-input
              v-model="formData.projectlx"
              readonly
              clearable
              placeholder="请选择"
              :style="{ width: '78%' }"
            />
            <el-button
              style="margin-left: 15px"
              type="primary"
              @click.native="showtypeView"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 47px">
          <el-form-item label="任职时间(审计范围)" prop="workDateRange">
            <el-date-picker
              v-model="formData.workDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计实施时间" prop="doAuditTime">
            <el-date-picker
              v-model="formData.doAuditTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择审计实施时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="clear: both">
          <el-form-item label="组长" prop="teamLeaderId">
            <el-input
              v-model="formData.teamLeaderId"
              clearable
              placeholder="请选择组长"
              readonly
              :style="{ width: '78%' }"
            />
            <el-button
              @click="projectManager('teamLeader')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="副组长" prop="subTeamLeaderId">
            <el-input
              v-model="formData.subTeamLeaderId"
              clearable
              placeholder="请选择副组长"
              readonly
              :style="{ width: '78%' }"
            />
            <el-button
              @click="projectManager('subTeamLeader')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="牵头人">
            <el-input
              v-model="formData.leaderId"
              clearable
              placeholder="请选择牵头人"
              readonly
              :style="{ width: '78%' }"
            />
            <el-button
              @click="projectManager('leader')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" prop="chiefReviewerId">
            <el-input
              v-model="formData.chiefReviewerId"
              clearable
              placeholder="请选择主审"
              readonly
              :style="{ width: '78%' }"
            />
            <el-button
              @click="projectManager('chiefReviewer')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助审" prop="deputyReviewerId">
            <el-input
              v-model="formData.deputyReviewerId"
              clearable
              placeholder="请选择助审"
              readonly
              :style="{ width: '78%' }"
            />
            <el-button
              @click="projectManager('deputyReviewer')"
              style="margin-left: 15px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否建议审计" prop="projectType">
            <el-select
              v-model="formData.projectType"
              placeholder="是否建议审计"
              :style="{ width: '100%' }"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
              <el-option label="待定" value="待定" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser"
              disabled
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker
              v-model="formData.createTime"
              type="date"
              disabled
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="原因" prop="textarea">
            <el-input
              type="textarea"
              :rows="8"
              placeholder="原因"
              :maxlength="4000"
              v-model="formData.textarea"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              type="textarea"
              :rows="4"
              placeholder="原因"
              :maxlength="4000"
              v-model="formData.remarks"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
    <project-manage
      @projectManage="getChildlistPro"
      :multiple="false"
      ref="manage"
    />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
    <typeView ref="typeView" @submit="setType" />
  </el-dialog>
</template>

<script>
  import {
    saveOrUpdate,
    detail,
    findbyOrgiddetail,
  } from '@/api/oilAudit/jhgl/rzsjmx'
  // import projectManage from '@/components/selectPerson.vue'
  import projectManage from '@/components/selectPerson.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import typeView from '@/views/oilAudit/lrjjzr/components/type'
  export default {
    components: { projectManage, SelectDepartment, typeView },
    inheritAttrs: false,
    data() {
      const validatorYear = (_rule, value, callback) => {
        if (value == '') {
          callback(new Error('请输入截至本年底未审年限'))
        } else if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      const validatorMonth = (_rule, value, callback) => {
        if (value == '') {
          callback(new Error('请输入未审计月数'))
        } else if (value !== '' && isNaN(value)) {
          callback(new Error('请输入数字值'))
        } else {
          callback()
        }
      }
      return {
        loading: false,
        personType: '', // 人员类型
        formData: {
          id: '',
          orgId: '',
          orgName: '',
          auditInfo: '',
          ldname: '',
          ldzw: '',
          csym: '',
          yjexsj: '',
          rzsj: '',
          unauditYear: '',
          sjfwstarttime: [],
          sjfwendtime: [],
          unauditMonth: '',
          auditTime: '',
          zjprojectname: '',
          workStartTime: '',
          workEndTime: '',
          workDateRange: [],
          doAuditTime: '',
          teamLeaderId: '',
          subTeamLeaderId: '',
          leaderId: '',
          chiefReviewerId: '',
          deputyReviewerId: '',
          createUser: '',
          createTime: '',
          textarea: '',
          projectType: '',
          projectlx: '',
          remarks: '',
        },
        formDisabled: true,
        rules: {
          orgName: [
            {
              required: true,
              message: '请输入单位',
              trigger: 'change',
            },
          ],
          auditInfo: [
            {
              required: true,
              message: '请输入审计情况',
              trigger: 'blur',
            },
          ],
          ldname: [
            {
              required: true,
              message: '请输入现任领导名称',
              trigger: 'blur',
            },
          ],
          ldzw: [
            {
              required: true,
              message: '请输入职务',
              trigger: 'blur',
            },
          ],
          auditTime: [
            {
              required: true,
              message: '请选择审计时间',
              trigger: 'change',
            },
          ],
          unauditYear: [
            {
              required: true,
              message: '请输入截至本年底未审年限',
              // validator: validatorYear,
              trigger: 'blur',
            },
          ],
          sjfwstarttime: [
            {
              required: true,
              message: '请选择审计范围时间',
              trigger: 'blur',
            },
          ],
          projectType: [
            {
              required: true,
              message: '请选择是否建议审计',
              trigger: 'blur',
            },
          ],
          zjprojectname: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          workDateRange: [
            {
              required: true,
              message: '请选择任职时间(审计范围)',
              trigger: 'change',
            },
          ],
          doAuditTime: [
            {
              required: true,
              message: '请选择审计实施时间',
              trigger: 'change',
            },
          ],
          teamLeaderId: [
            {
              required: true,
              message: '请输入组长',
              trigger: 'change',
            },
          ],

          leaderId: [
            {
              required: true,
              message: '请输入牵头人',
              trigger: 'change',
            },
          ],
          deputyReviewerId: [
            {
              required: true,
              message: '请输入主审',
              trigger: 'change',
            },
          ],
          chiefReviewerId: [
            {
              required: true,
              message: '请输入助审',
              trigger: 'change',
            },
          ],
          projectlx: [
            {
              required: true,
              message: '请选择项目类型',
              trigger: 'change',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      getDepartmentInfo(val) {
        this.formData.orgId = val.id
        this.formData.orgName = val.label
        this.getOrgId(val.id)
      },
      async getOrgId(id) {
        const res = await findbyOrgiddetail({ orgid: id })
        console.log(res)
        if (res.data && res.data.data) {
          this.formData.zjprojectname = res.data.data.projectName
          this.formData.projectlx = res.data.data.sjlxName
          this.formData.teamLeaderId = res.data.data.xmqd.groupLeader
          // this.formData.teamLeaderId = res.data.data.xmqd.groupLeaderId
          this.formData.subTeamLeaderId = res.data.data.xmqd.fzzName
          // this.formData.subTeamLeaderId = res.data.data.xmqd.fzzStafffId
          this.formData.chiefReviewerId = res.data.data.xmqd.zsname
          // this.formData.chiefReviewerId = res.data.data.xmqd.approverId
          this.formData.deputyReviewerId = res.data.data.xmqd.assistApprover
          // this.formData.deputyReviewerId = res.data.data.xmqd.assistApproverId
        }
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      projectManager(type) {
        this.personType = type
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        // const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        this.formData[this.personType + 'Id'] = names.toString()
        // this.formData[this.personType + 'Name'] = names
      },
      async showEdit(row, disabled) {
        console.log(row, disabled)
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
          this.title = disabled ? '详细' : '编辑'
          this.fgldhzid = row.fgldhzid
          // Object.keys(this.formData).forEach((key) => {
          //   this.formData[key] = row[key]
          // })
          const res = await detail({ id: row.id })
          Object.assign(this.formData, res.data.data)
          this.formData.createUser = res.data.data.createUser?.username
          this.formData.orgName = res.data.data.org.orgname
          this.formData.orgId = res.data.data.org.orgid

          // this.formData.teamLeaderId = res.data.data.teamLeaderId
          // this.formData.subTeamLeaderId = res.data.data.subTeamLeaderId
          // this.formData.leaderId = res.data.data.leaderId
          // this.formData.deputyReviewerId = res.data.data.deputyReviewerId
          // this.formData.chiefReviewerId = res.data.data.chiefReviewerId
          this.formData.workDateRange = [
            res.data.data.workStartTime,
            res.data.data.workEndTime,
          ]
          this.formData.sjfwstarttime = [
            res.data.data.sjfwstarttime,
            res.data.data.sjfwendtime,
          ]
          // this.formData.orgName = row.org?.orgname
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUser = createUserName
          this.formData.createTime = createTime
        }
      },
      close() {
        this.formData = {
          id: '',
          orgId: '',
          orgName: '',
          auditInfo: '',
          ldname: '',
          ldzw: '',
          csym: '',
          yjexsj: '',
          rzsj: '',
          unauditYear: '',
          sjfwstarttime: [],
          sjfwendtime: [],
          unauditMonth: '',
          auditTime: '',
          zjprojectname: '',
          workStartTime: '',
          workEndTime: '',
          workDateRange: [],
          doAuditTime: '',
          teamLeaderId: '',
          subTeamLeaderId: '',
          leaderId: '',
          chiefReviewerId: '',
          deputyReviewerId: '',
          createUser: '',
          createTime: '',
          projectlx: '',
          remarks: '',
          projectType: '',
        }
        this.dialogFormVisible = false
        this.formDisabled = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = { ...this.formData }
            params.sjfwendtime = params.sjfwstarttime[1]
            params.sjfwstarttime = params.sjfwstarttime[0]

            params.workStartTime = params.workDateRange[0]
            params.workEndTime = params.workDateRange[1]
            // params.chiefReviewerId = params.chiefReviewerId
            // params.deputyReviewerId = params.deputyReviewerId
            // params.leaderId = params.leaderId
            // params.subTeamLeaderId = params.subTeamLeaderId
            // params.teamLeaderId = params.teamLeaderId
            delete params.createUser
            delete params.workDateRange
            this.loading = true
            const res = await saveOrUpdate(params)
            this.loading = false
            if (res && res.code === 1) {
              this.close()
              this.$emit('fetchData')
              this.$emit('selected', res.data)
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
      showtypeView() {
        this.$refs['typeView'].showEdit()
      },
      setType(e) {
        this.formData.projectlx = e.auditType
        this.$forceUpdate()
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  ::v-deep .sjfwstarttime {
    .el-range-separator {
      padding: 0;
    }
  }
</style>
