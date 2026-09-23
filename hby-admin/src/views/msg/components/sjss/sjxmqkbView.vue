<template>
  <div>
    <el-row :gutter="14" v-loading="loading">
      <el-form
        ref="ruleForm"
        label-width="210px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="!footer"
      >
        <el-col :span="12">
          <el-form-item label="项目名称" prop="name">
            <el-input
              v-model="formData.name"
              clearable
              placeholder="请输入项目名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计组" prop="auditGroup">
            <el-input
              v-model="formData.auditGroup"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入审计组"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="被审计单位" prop="auditUnit">
            <el-input
              v-model="formData.auditUnit"
              clearable
              placeholder="请选择被审计单位"
              :style="{ width: '196px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('auditUnit')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实施单位" prop="exePhraseUnit">
            <el-input
              v-model="formData.exePhraseUnit"
              clearable
              placeholder="请选择实施单位"
              :style="{ width: '196px' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="openDep('exePhraseUnit')"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="组长" prop="groupLeader">
            <el-input
              v-model="formData.groupLeader"
              clearable
              placeholder="请选择组长"
              style="width: 196px"
              disabled
            />
            <el-button
              @click="handleObject('groupLeader')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="主审" prop="approver">
            <el-input
              v-model="formData.approver"
              clearable
              placeholder="请选择主审"
              style="width: 196px"
              disabled
            />
            <el-button
              @click="handleObject('approver')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="助审" prop="assistApprover">
            <el-input
              v-model="formData.assistApprover"
              clearable
              placeholder="请选择助审"
              style="width: 196px"
              disabled
            />
            <el-button
              @click="handleObject('assistApprover')"
              style="margin-left: 10px"
              type="primary"
              size="small"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>

        <el-col :span="12" style="clear: both">
          <el-form-item
            label="计划现场工作时间"
            prop="planSceneApproveStaerTime"
          >
            <el-date-picker
              v-model="formData.planSceneApproveStaerTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="计划现场结束日期" prop="planSceneApproveEndTime">
            <el-date-picker
              v-model="formData.planSceneApproveEndTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="现场审计开始时间" prop="sceneApproveStaerTime">
            <el-date-picker
              v-model="formData.sceneApproveStaerTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="实际现场结束日期" prop="sceneApproveEndTime">
            <el-date-picker
              v-model="formData.sceneApproveEndTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creater">
            <el-input
              v-model="formData.creater"
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
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择时间"
              disabled
              :style="{ width: '100%' }"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24">
        <div style="text-align: right; margin-bottom: 5px" v-if="footer">
          <el-button type="success" @click="handleAdd">增加一行</el-button>
        </div>
        <!-- 新增可编辑表格 -->
        <el-table border :data="formData.auditProjectDetailEntityList">
          <el-table-column align="center" label="填报时间" prop="fillInTime">
            <template slot-scope="scope">
              <el-date-picker
                v-model="scope.row.fillInTime"
                type="date"
                :disabled="!footer"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                placeholder="选择时间"
                :style="{ width: '100%' }"
              ></el-date-picker>
            </template>
          </el-table-column>
          <el-table-column align="center" label="填报内容" prop="fillInConten">
            <template slot-scope="scope">
              <el-input
                v-model="scope.row.fillInConten"
                size="mini"
                :disabled="!footer"
                style="width: 90%"
              />
            </template>
          </el-table-column>

          <el-table-column v-if="footer" align="center" label="操作">
            <template slot-scope="scope">
              <el-button
                type="text"
                @click="handleDelete(scope.$index, scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <!-- <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </div> -->
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
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
    <!-- 人员组件 -->
    <Company ref="audiTree" @selected="getChildlistObj"></Company>

    <!-- 部门 -->
    <DepartmentOptions ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import {
    projectSaveOrUpdate,
    projectAuditProjectById,
  } from '@/oapi/audit/implement'
  import Company from '@/components/CompanySelectUserByTree'
  import DepartmentOptions from '@/views/oilAudit/report/components/options/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']

  export default {
    components: {
      DepartmentOptions,
      Resubmit,
      Company,
    },
    inheritAttrs: false,
    props: ['fetchData'],
    data() {
      return {
        loading: false,
        baseURL: baseURL,
        uploadApi: '/oiaudit/fileManage/upload',
        headers: { token: token },
        tableData: [],
        formData: {
          id: '',
          sceneApproveStaerTime: '',
          name: undefined,
          auditGroup: undefined,
          auditUnit: undefined,
          exePhraseUnit: undefined,
          groupLeader: undefined,
          approver: undefined,
          assistApprover: undefined,
          planSceneApproveStaerTime: undefined,
          planSceneApproveEndTime: undefined,
          sceneApproveEndTime: undefined,
          creater: undefined,
          createTime: undefined,
          auditProjectDetailEntityList: undefined,
        },
        footer: true,
        depType: '',
        manType: '',
        rules: {
          auditGroup: [
            {
              required: true,
              message: '请输入审计组',
              trigger: 'blur',
            },
          ],
          auditUnit: [
            {
              required: true,
              message: '请输入被审计单位名称',
              trigger: 'blur',
            },
          ],
          exePhraseUnit: [
            {
              required: true,
              message: '请输入实施单位',
              trigger: 'blur',
            },
          ],
          name: [
            {
              required: true,
              message: '请输入项目名称',
              trigger: 'blur',
            },
          ],
          groupLeader: [
            {
              required: true,
              message: '请输入组长',
              trigger: 'blur',
            },
          ],
          approver: [
            {
              required: true,
              message: '请输入主审',
              trigger: 'blur',
            },
          ],
          assistApprover: [
            {
              required: true,
              message: '请输入助审',
              trigger: 'blur',
            },
          ],
          sceneApproveStaerTime: [
            {
              required: true,
              message: '请输入现场审计开始时间',
              trigger: 'blur',
            },
          ],
          planSceneApproveStaerTime: [
            {
              required: true,
              message: '请输入计划现场结束日期',
              trigger: 'blur',
            },
          ],
          planSceneApproveEndTime: [
            {
              required: true,
              message: '请输入计划现场工作时间',
              trigger: 'blur',
            },
          ],
          sceneApproveEndTime: [
            {
              required: true,
              message: '请输入实际现场结束日期',
              trigger: 'blur',
            },
          ],
          auditmoney: [
            {
              required: true,
              message: '请输入审计认定金额(元)',
              trigger: 'blur',
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        // auditProjectDetailEntityList: [],
        // 流程相关
        fromId: '',
        fromIdcopy: '',
        flowtaskinfoflowid: '',
        ymFromId: '',
        status: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      openDep(type) {
        this.depType = type
        this.$refs['department'].show()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, this.depType, node.label)
        if (this.depType == 'exePhraseUnit') {
          this.$set(this.formData, `exePhraseUnitId`, node.id)
        } else {
          this.$set(this.formData, `auditUnitId`, node.id)
        }
      },
      handleObject(type) {
        this.manType = type
        this.$refs['audiTree'].show()
      },
      getChildlistObj(val) {
        this.$set(this.formData, this.manType, val.realname)
        if (this.manType == 'approver') {
          this.$set(this.formData, 'approverId', val.staffid)
        } else if (this.manType == 'assistApprover') {
          this.$set(this.formData, 'assistApproverId', val.staffid)
        } else {
          this.$set(this.formData, 'groupLeaderId', val.staffid)
        }
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
        this.dialogFormVisible = true
        // 流程相关
        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        if (formId) {
          const res = await projectAuditProjectById({ projectId: formId })
          Object.assign(this.formData, res.data)
        }

        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'detail') {
          this.title = '详细'
          this.footer = false
        } else if (title == 'add') {
          this.title = '新增'
          let resL = JSON.parse(localStorage.getItem('userInfo')).realname
          this.formData = {
            ...this.formData,
            creater: resL,
            createTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.$bus.$emit('updateMsg', 0)
        this.formData = {
          id: '',
          sceneApproveStaerTime: '',
          name: undefined,
          auditGroup: undefined,
          auditUnit: undefined,
          exePhraseUnit: undefined,
          groupLeader: undefined,
          approver: undefined,
          assistApprover: undefined,
          planSceneApproveStaerTime: undefined,
          planSceneApproveEndTime: undefined,
          sceneApproveEndTime: undefined,
          creater: undefined,
          createTime: undefined,
          auditProjectDetailEntityList: undefined,
        }
        this.dialogFormVisible = false
        this.footer = true
      },
      async save() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            console.log(JSON.stringify(this.formData))
            const { data, code } = await projectSaveOrUpdate({
              ...this.formData,
            })
            if (code == 1) {
              this.$emit('fetchData')
              this.$baseMessage(data, 'success')
            } else {
              this.$baseMessage(data, 'error')
            }
          }
        })
      },
      // 添加点击按钮
      handleAdd() {
        this.formData.auditProjectDetailEntityList.push({
          auditProjectId: this.formData.id || '',
          fillInConten: '',
          fillInTime: '',
          show: true,
        })
      },
    },
  }
</script>
<style scoped>
  .el-form-item__contractname span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
