<template>
  <!-- 未委托 新增修改 -->
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
        :disabled="formDisabled"
      >
        <el-col :span="12" style="height: 29px">
          <el-form-item label="单位" prop="unitName">
            <el-input
              v-model="formData.unitName"
              readonly
              clearable
              disabled
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
          <el-form-item label="姓名" prop="name">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12" style="height: 29px">
          <el-form-item label="预计退二线时间" prop="retireTime">
            <el-date-picker
              v-model="formData.retireTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择预计退二线时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="审计时间" prop="auditTime">
            <el-date-picker
              v-model="formData.auditTime"
              type="date"
              placeholder="选择审计时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目名称" prop="projectName">
            <el-input
              v-model="formData.projectName"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="任职时间"
            prop="workDateRange"
            style="height: 29px"
          >
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
              placeholder="选择审计实施时间"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="teamLeaderName">
            <el-input
              v-model="formData.teamLeaderName"
              clearable
              placeholder="请选择组长"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="projectManager('teamLeader')"
              style="margin-left: 11px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="牵头人" prop="leaderName">
            <el-input
              v-model="formData.leaderName"
              clearable
              placeholder="请选择牵头人"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="projectManager('leader')"
              style="margin-left: 11px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="主审" prop="chiefReviewerName">
            <el-input
              v-model="formData.chiefReviewerName"
              clearable
              placeholder="请选择主审"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="projectManager('chiefReviewer')"
              style="margin-left: 11px"
              type="primary"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="助审" prop="deputyReviewerName">
            <el-input
              v-model="formData.deputyReviewerName"
              clearable
              placeholder="请选择助审"
              disabled
              :style="{ width: '80%' }"
            />
            <el-button
              @click="projectManager('deputyReviewer')"
              style="margin-left: 11px"
              type="primary"
            >
              选择
            </el-button>
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
      </el-form>
    </el-row>
    <!-- 人员 -->
    <project-manage
      @projectManage="getChildlistPro"
      :multiple="false"
      ref="manage"
    />
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />

    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'
  import projectManage from '@/components/selectPerson.vue'
  import { formatDay } from '@/utils/index'
  import { wwtjyjlrUpdate, wwtjyjlrDetail } from '@/oapi/audit/plan'
  import { isArray } from '@/utils/validate'

  export default {
    components: { projectManage, SelectDepartment },
    inheritAttrs: false,
    data() {
      return {
        loading: false,
        personType: '', // 人员类型
        formData: {
          id: '',
          name: '',
          retireTime: '',
          auditTime: '',
          projectName: '',
          workStartTime: '',
          workEndTime: '',
          workDateRange: [],
          doAuditTime: '',
          teamLeaderId: '',
          teamLeaderName: '',
          leaderId: '',
          leaderName: '',
          chiefReviewerId: '',
          chiefReviewerName: '',
          deputyReviewerId: '',
          deputyReviewerName: '',
          createUser: '',
          createTime: '',
          unitId: '',
          unitName: '',
        },

        formDisabled: true,
        rules: {
          dw: [
            {
              required: true,
              message: '请输入单位',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入姓名',
              trigger: 'blur',
            },
          ],
          retireTime: [
            {
              required: true,
              message: '请输入预计退二线时间',
              trigger: 'change',
            },
          ],
          auditTime: [
            {
              required: true,
              message: '请输入审计时间',
              trigger: 'change',
            },
          ],
          projectName: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          workDateRange: [
            {
              required: true,
              message: '请选择任职时间区间',
              trigger: 'change',
            },
          ],
          doAuditTime: [
            {
              required: true,
              message: '请输入审计实施时间',
              trigger: 'change',
            },
          ],
          // teamLeaderName: [
          //   {
          //     required: true,
          //     message: '请输入组长',
          //     trigger: 'blur',
          //   },
          // ],
          // leaderName: [
          //   {
          //     required: true,
          //     message: '请输入牵头人',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '新增',
      }
    },
    methods: {
      projectManager(type) {
        this.personType = type
        this.$refs['manage'].showEdit()
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistPro(val) {
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        this.formData[this.personType + 'Id'] = ids.toString()
        this.formData[this.personType + 'Name'] = names.toString()
      },
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = !!disabled
        if (row) {
          const res = await wwtjyjlrDetail({ id: row.id })
          this.title = disabled ? '详细' : '编辑'
          this.fgldhzid = row.fgldhzid
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.data[key]
          })
          this.formData.createUser = res.data.data.createUser?.username
          this.formData.teamLeaderName = res.data.data.teamLeaderName
          this.formData.leaderName = res.data.data.leaderName
          this.formData.chiefReviewerName = res.data.data.chiefReviewerName
          this.formData.deputyReviewerName = res.data.data.deputyReviewerName
          this.formData.workDateRange = res.data.data?.workStartTime
            ? [res.data.data?.workStartTime, res.data.data?.workEndTime]
            : []
          this.formData.unitName = res.data.data.tblOrganization.orgname
          this.formData.unitId = res.data.data.tblOrganization.orgid
        } else {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const createUserName = userInfo.realname
          const createTime = new Date().toJSON().split('T')[0]
          this.formData.createUser = createUserName
          this.formData.createTime = createTime
        }
      },
      getDepartmentInfo(val) {
        this.$set(this.formData, 'unitName', val.label)
        this.$set(this.formData, 'unitId', val.id)
      },
      close() {
        this.lazyDom = false
        this.formData = {
          id: '',
          name: '',
          retireTime: '',
          auditTime: '',
          projectName: '',
          workStartTime: '',
          workEndTime: '',
          workDateRange: [],
          doAuditTime: '',
          teamLeaderId: '',
          teamLeaderName: '',
          leaderId: '',
          leaderName: '',
          chiefReviewerId: '',
          chiefReviewerName: '',
          deputyReviewerId: '',
          deputyReviewerName: '',
          createUser: '',
          createTime: '',
        }
        this.$set(this.formData, 'workDateRange', []) // 避免清除失效
        this.dialogFormVisible = false
        this.formDisabled = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
            console.log(
              "🚀 ~ this.$refs['ruleForm'].validate ~ params:",
              params
            )
            params.workStartTime = formatDay(params.workDateRange[0])
            params.workEndTime = formatDay(params.workDateRange[1])
            params.teamLeaderName = isArray(params.teamLeaderName)
              ? params.teamLeaderName.toString()
              : params.teamLeaderName
            params.leaderName = isArray(params.leaderName)
              ? params.leaderName.toString()
              : params.leaderName
            params.chiefReviewerName = isArray(params.chiefReviewerName)
              ? params.chiefReviewerName.toString()
              : params.chiefReviewerName
            params.deputyReviewerName = isArray(params.deputyReviewerName)
              ? params.deputyReviewerName.toString()
              : params.deputyReviewerName
            delete params['createUser']
            delete params['workDateRange']
            this.loading = true
            const res = await wwtjyjlrUpdate(params)
            this.loading = false
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
