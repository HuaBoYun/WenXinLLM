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
              disabled
              clearable
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
          <el-form-item label="部门/单位" prop="applyBelongGroupName">
            <el-input
              v-model="formData.applyBelongGroupName"
              :style="{ width: '75%' }"
              disabled
              clearable
              placeholder="请选择部门/单位"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="handleObject('dept')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="科室" prop="applyWorkUnitName">
            <el-input
              v-model="formData.applyWorkUnitName"
              :style="{ width: '75%' }"
              clearable
              disabled
              placeholder="请选择科室"
            />
            <el-button
              type="primary"
              style="margin-left: 20px"
              @click="selectDept('keshi')"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="网络接口(T)" prop="networkInterface">
            <el-input
              v-model="formData.networkInterface"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入网络接口(T)"
            />
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="办公区" prop="officeArea">
            <el-input
              v-model="formData.officeArea"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入办公区"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="房间号" prop="roomNumber">
            <el-input
              v-model="formData.roomNumber"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入房间号"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备类型" prop="equipmentType">
            <el-input
              v-model="formData.equipmentType"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入设备类型"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用途" prop="purpose">
            <el-input
              v-model="formData.purpose"
              :style="{ width: '100%' }"
              clearable
              placeholder="请输入用途"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用期限" prop="applyPeriod">
            <el-date-picker
              v-model="applyPeriod"
              type="daterange"
              range-separator="至"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              start-placeholder="使用期限开始日期"
              end-placeholder="使用期限结束日期"
              @change="changeApplyPeriod"
              :style="{ width: '100%' }"
            ></el-date-picker>
            <!-- <el-input
              v-model="formData.applyPeriodTimeStart"
              :style="{ width: '100%' }"
              clearable
              placeholder="请选择使用期限"
              @change="changeApplyPeriod"
            /> -->
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外网权限" prop="externalNetworkPermissions">
            <el-input
              v-model="formData.externalNetworkPermissions"
              :style="{ width: '100%' }"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              clearable
              placeholder="请输入外网权限"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请时间" prop="applyTime">
            <el-date-picker
              v-model="formData.applyTime"
              type="date"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              placeholder="选择申请时间"
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
    <!-- 申请人 -->
    <executor-options ref="executor" @projectManage="handleExecutorSelected" />
    <!-- 部门 -->
    <department-options ref="department" @submit="handleDepartmentSelected" />
    <Company ref="audiTree" @submit="getChildlistObj"></Company>
    <ProcessList ref="process" @fetchData="close" />
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
  </el-dialog>
</template>

<script>
  import { baseURL } from '@/config'
  import store from '@/store'
  import DepartmentOptions from '@/views/oilAudit/jhlx/components/department.vue'
  import ExecutorOptions from '@//components/danxuanPerson.vue'
  import { editIp, getInfoDetail } from '@/oapi/ypns_zhgl/ipAddressManager.js'
  import Company from '@/views/oilAudit/jhlx/components/department.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const token = store.getters['user/token']
  import { ymWorkCandidates, getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'

  export default {
    components: {
      ExecutorOptions,
      DepartmentOptions,
      Company,
      Resubmit,
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
          applyBelongGroup: '',
          applyBelongGroupName: '',
          applyWorkUnit: '',
          applyWorkUnitName: '',
          networkInterface: '',
          officeArea: '',
          roomNumber: '',
          equipmentType: '',
          purpose: '',
          externalNetworkPermissions: '',
          applyTime: '',
          applyPeriodTimeStart: '',
          applyPeriodTimeEnd: '',
          applyPeriodEnd: '',
          id: '',
        },
        applyPeriod: [],
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
          applyBelongGroupName: [
            {
              required: true,
              message: '请选择部门/单位',
              trigger: ['blur', 'change'],
            },
          ],
          applyWorkUnitName: [
            {
              required: true,
              message: '请选择科室',
              trigger: ['blur', 'change'],
            },
          ],
          networkInterface: [
            {
              required: true,
              message: '请输入网络接口(T)',
              trigger: 'blur',
            },
          ],
          officeArea: [
            {
              required: true,
              message: '请输入办公区',
              trigger: 'blur',
            },
          ],
          roomNumber: [
            {
              required: true,
              message: '请输入房间号',
              trigger: 'blur',
            },
          ],
          equipmentType: [
            {
              required: true,
              message: '请输入设备类型',
              trigger: 'blur',
            },
          ],
          purpose: [
            {
              required: true,
              message: '请输入用途',
              trigger: 'blur',
            },
          ],
          applyPeriodTimeStart: [
            {
              required: true,
              message: '请选择使用期限',
              trigger: ['blur', 'change'],
            },
          ],
          externalNetworkPermissions: [
            {
              required: true,
              message: '请输入外网权限',
              trigger: 'blur',
            },
          ],
          applyTime: [
            {
              required: true,
              message: '选择申请时间',
              trigger: ['blur', 'change'],
            },
          ],
        },
        dialogFormVisible: false,
        title: '新增',
        deptType: '',
        flowtaskinfoflowid: '',
        fromId: '',
        ymFromId: '',
        fromIdcopy: '',
        status: '',
        candidateType: '',
        editId: '',
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      selectDept(type) {
        this.deptType = type
        this.$refs.department.showEdit()
      },
      handleDepartmentSelected(node) {
        this.$set(this.formData, `applyWorkUnitName`, node.label)
        //保存名称对应的ID
        this.$set(this.formData, `applyWorkUnit`, node.id)
      },
      changeApplyPeriod(val) {
        if (val && val.length) {
          this.$set(this.formData, 'applyPeriodTimeStart', val[0])
          this.$set(this.formData, 'applyPeriodTimeEnd', val[1])
        } else {
          this.$set(this.formData, 'applyPeriodTimeStart', '')
          this.$set(this.formData, 'applyPeriodTimeEnd', '')
        }
      },
      handleExecutorSelected(node) {
        this.$set(this.formData, 'applyPeopleName', node[0].realname)
        this.$set(this.formData, 'applyPeople', node[0].staffid)
        this.$set(this.formData, 'applyBelongGroupName', node[0].orgname)
        this.$set(this.formData, 'applyBelongGroup', node[0].orgid)
      },
      getCurrentDate() {
        return new Date(+new Date() + 8 * 3600 * 1000)
          .toJSON()
          .substr(0, 19)
          .replace('T', ' ')
      },
      async showEdit(row, title, tableId) {
        this.dialogFormVisible = true
        this.editId = row.id
        //流程相关方法
        // this.aboutLiuCheng(row.id, tableId, row.state)
        if (row) {
          const res = await getInfoDetail({ id: row.id })

          this.formData.applyPeople = res.data.applyPeople
          this.formData.applyPeopleName = res.data.applyPeopleName
          this.formData.applyBelongGroup = res.data.applyBelongGroup
          this.formData.applyBelongGroupName = res.data.applyBelongGroupName
          this.formData.applyWorkUnit = res.data.applyWorkUnit
          this.formData.applyWorkUnitName = res.data.applyWorkUnitName
          this.formData.networkInterface = res.data.networkInterface
          this.formData.officeArea = res.data.officeArea
          this.formData.roomNumber = res.data.roomNumber
          this.formData.equipmentType = res.data.equipmentType
          this.formData.purpose = res.data.purpose
          this.formData.externalNetworkPermissions =
            res.data.externalNetworkPermissions
          this.formData.applyTime = res.data.applyTime
          this.formData.applyPeriodTimeStart = res.data.applyPeriodTimeStart
          this.formData.applyPeriodTimeEnd = res.data.applyPeriodTimeEnd
          if (res.data.applyPeriodTimeStart && res.data.applyPeriodTimeEnd) {
            this.applyPeriod = [
              res.data.applyPeriodTimeStart,
              res.data.applyPeriodTimeEnd,
            ]
          }
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
          this.formData = {
            ...this.formData,
            createdUser: resL,
            createdTime: this.getCurrentDate(),
          }
        }
      },
      close() {
        this.formData.applyPeople = ''
        this.formData.applyPeopleName = ''
        this.formData.applyBelongGroup = ''
        this.formData.applyBelongGroupName = ''
        this.formData.applyWorkUnit = ''
        this.formData.applyWorkUnitName = ''
        this.formData.networkInterface = ''
        this.formData.officeArea = ''
        this.formData.roomNumber = ''
        this.formData.equipmentType = ''
        this.formData.purpose = ''
        this.formData.externalNetworkPermissions = ''
        this.formData.applyTime = ''
        this.formData.applyPeriodTimeStart = ''
        this.formData.applyPeriodTimeEnd = ''
        this.formData.applyPeriodEnd = ''
        this.formData.id = ''
        this.applyPeriod = []
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
            const res = await editIp(params)
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
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistObj(val) {
        this.$set(this.formData, 'applyBelongGroup', val.id)
        this.$set(this.formData, 'applyBelongGroupName', val.label)
      },
      // 流程相关-提交
      async ymsubmit() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.loading = true
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
      aboutLiuCheng(id, tableId, state) {
        getFlowPkInfo({
          formId: id,
          tableId,
        }).then((res) => {
          this.flowId = res.data.flowId
          this.flowtaskinfoflowid = res.data.flowId
          this.ymFromId = res.data.id
          this.fromId = id
          ymWorkCandidates({
            flowId: this.flowId,
            // ymFromId: this.ymFromId,
            fromId: this.fromId,
            flowTaskOperatorId: '',
          }).then((item) => {
            if (item.code == 1) {
              this.candidateType = res.data.candidateType
              this.status = state + 1
            }
          })
        })
      },
      handleApproval() {
        //提交审批
        this.$refs['process'].save(133, this.editId)
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
