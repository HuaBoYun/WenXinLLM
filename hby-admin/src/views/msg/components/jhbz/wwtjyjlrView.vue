<template>
  <!-- 未委托 新增修改 -->
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
          <el-form-item label="姓名" prop="name" style="height: 29px">
            <el-input
              v-model="formData.name"
              :style="{ width: '100%' }"
              placeholder="请输入姓名"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计退二线时间" prop="retireTime">
            <el-date-picker
              v-model="formData.retireTime"
              type="date"
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
        <template v-if="lazyDom">
          <el-col :span="12">
            <el-form-item label="组长" prop="teamLeaderName">
              <el-input
                v-model="formData.teamLeaderName"
                :style="{ width: '75%' }"
                disabled
                placeholder="请选择组长"
              />
              <el-button
                style="margin-left: 10px"
                type="primary"
                @click="projectManager('teamLeader')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="牵头人" prop="leaderName">
              <el-input
                v-model="formData.leaderName"
                :style="{ width: '75%' }"
                disabled
                placeholder="请选择牵头人"
              />
              <el-button
                style="margin-left: 10px"
                type="primary"
                @click="projectManager('leader')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主审" prop="chiefReviewerName">
              <el-input
                v-model="formData.chiefReviewerName"
                :style="{ width: '75%' }"
                disabled
                placeholder="请选择主审"
              />
              <el-button
                style="margin-left: 10px"
                type="primary"
                @click="projectManager('chiefReviewer')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="助审" prop="deputyReviewerName">
              <el-input
                v-model="formData.deputyReviewerName"
                :style="{ width: '75%' }"
                disabled
                placeholder="请选择助审"
              />
              <el-button
                style="margin-left: 10px"
                type="primary"
                @click="projectManager('deputyReviewer')"
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
        </template>
        <el-col :span="12">
          <el-form-item label="创建人" prop="createUser">
            <el-input
              v-model="formData.createUser.username"
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
    <div style="text-align: right; margin-top: 20px" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
      <el-button type="primary" @click="ymsubmit">提交</el-button>
    </div>
    <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />

    <Resubmit
      ref="resubmit"
      @fetchClose="close"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      :status="status"
    />
  </div>
</template>

<script>
  import projectManage from '@/components/selectPerson.vue'
  import { formatDay } from '@/utils/index'
  import { wwtjyjlrUpdate, wwtjyjlrDetail } from '@/oapi/audit/plan'
  import { baseURL } from '@/config'
  import { isArray } from '@/utils/validate'
  import store from '@/store'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

  // import {
  //   editDataSource,
  //   getDataSourceDefaultInfo,
  //   LinkTest,
  // } from '@/api/setting/org'

  const token = store.getters['user/token']

  export default {
    components: { projectManage, Resubmit, SelectDepartment },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/audit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        personType: '', // 人员类型
        lazyDom: false, // 延迟加载的dom
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
          createUser: { username: '' },
          unitId: '',
          unitName: '',
          // createTime: '99',
        },
        // createUser: { username: '' },
        footer: true,
        rules: {
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
              trigger: 'blur',
            },
          ],
          auditTime: [
            {
              required: true,
              message: '请输入审计时间',
              trigger: 'blur',
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
              type: 'array',
              required: true,
              message: '请选择任职时间区间',
              fields: {
                // tpye类型试情况而定,所以如果返回的是date就改成date
                0: {
                  type: 'date',
                  required: true,
                  message: '请选择开始日期',
                  trigger: 'change',
                },
                1: {
                  type: 'date',
                  required: true,
                  message: '请选择结束日期',
                  trigger: 'change',
                },
              },
            },
          ],
          doAuditTime: [
            {
              required: true,
              message: '请输入审计实施时间',
              trigger: 'blur',
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
        // 流程相关
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      projectManager(type) {
        this.personType = type
        this.$refs['manage'].showEdit()
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistPro(val) {
        // 人员选择回调
        const ids = val.map((res) => res.staffid)
        const names = val.map((res) => res.realname)
        this.formData[this.personType + 'Id'] = ids.toString()
        this.formData[this.personType + 'Name'] = names.toString()
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(
        title,
        row,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {
        // 流程相关
        this.fromId = row
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status

        this.dialogFormVisible = true

        setTimeout(() => {
          this.lazyDom = true
        }, 300)

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          const { username } = JSON.parse(localStorage.getItem('userInfo'))
          this.formData.createUser = { username }
          this.formData.createTime = this.getCurrentDate()
        }

        if (row) {
          const res = await wwtjyjlrDetail({ id: row }) // 请求详情接口 回显
          if (res.code === 1) {
            // 详情数据序列化
            const detailDataArrays = Object.entries(res.data.data)

            // 然后，有多少数据，回填复制formData中多少项
            detailDataArrays.forEach((item) => {
              this.formData[item[0]] = item[1]
            })
            console.log(detailDataArrays)

            this.$set(this.formData, 'workDateRange', [
              new Date(res.data.data.workStartTime),
              new Date(res.data.data.workEndTime),
            ])
            this.$set(
              this.formData.createUser,
              'username',
              res.data.data?.createUser?.username || ''
            ) // 后台数据创建人为对象，会引发显示问题
            this.formData.teamLeaderName = res.data.data.teamLeaderName
            this.formData.leaderName = res.data.data.leaderName
            this.formData.chiefReviewerName = res.data.data.chiefReviewerName
            this.formData.deputyReviewerName = res.data.data.deputyReviewerName
            this.formData.unitName = res.data.data.tblOrganization.orgname
            this.formData.unitId = res.data.data.tblOrganization.orgid
          }
        }
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
          createUser: { username: '' },
        }
        this.$set(this.formData, 'workDateRange', []) // 避免清除失效
        this.dialogFormVisible = false
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
      },
      getDepartmentInfo(val) {
        this.$set(this.formData, 'unitName', val.label)
        this.$set(this.formData, 'unitId', val.id)
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            const params = JSON.parse(JSON.stringify(this.formData))
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

            const res = await wwtjyjlrUpdate(params)
            if (res && res.code === 1) {
              this.$emit('fetchData')
              this.$message({
                message: '成功！',
                type: 'success',
              })
            } else {
              this.$message({
                message: '失败',
                type: 'error',
              })
            }
          }
        })
      },
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
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
