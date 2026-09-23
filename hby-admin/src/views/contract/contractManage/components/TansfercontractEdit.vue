<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="100px" :model="form" :rules="rules" id="aiForm">
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input
              v-model="form.contractNo"
              disabled
              placeholder="请输入合同编号"
              readonly
              :style="{ width: '80%' }"
            />
            <el-button
              :style="{ marginLeft: '10px', position: 'absolute' }"
              type="primary"
              :disabled="disabled"
              @click="$refs.table.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractName">
            <el-input
              v-model="form.contractName"
              disabled
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同份数" prop="contractCnt">
            <el-input
              type="number"
              v-model="form.contractCnt"
              placeholder="请输入合同份数"
              :disabled="disabled"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属单位" prop="tranOrgName">
            <el-input
              v-model="form.tranOrgName"
              disabled
              placeholder="请输入所属单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办部门" prop="handDeptName">
            <el-input
              v-model="form.handDeptName"
              disabled
              placeholder="请输入经办部门"
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经办人" prop="handStaffName">
            <el-input
              v-model="form.handStaffName"
              disabled
              placeholder="请输入经办人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="移交情况" prop="tranStatus2">
            <el-select
              v-model="form.tranStatus2"
              disabled
              placeholder="请选择移交情况"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="item in tranStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="法务意见" prop="legalMemo">
            <el-input
              v-model="form.legalMemo"
              disabled
              type="textarea"
              :rows="3"
              placeholder="请输入法务意见"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button
        v-if="
          (form.tranStatus == 2 || form.tranStatus == 3) &&
          jurisdictionCode == 1
        "
        @click="ymsubmit"
        type="primary"
      >
        提交
      </el-button>
    </template>

    <ContractTable ref="table" @selected="handleTableSelected" />

    <!-- 提交 -->
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
  import { contractTranListSave } from '@/api/contract/manage'
  import ContractTable from '@/views/contract/contractManage/components/ContractTable'
  import {
    getFlowTaskInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import { getFaqiInfo } from '@/api/setting/msg'
  import CandidateUserSelect from '@/components/CandidateUserSelect'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'

  export default {
    name: 'TemplateEdit',
    components: { ContractTable, CandidateUserSelect, Resubmit },
    data() {
      return {
        form: {
          contractId: '',
          contractName: '',
          contractNo: '',
          handDeptId: '',
          handDeptName: '',
          contractCnt: '',
          handStaffId: '',
          handStaffName: '',
          legalMemo: '',
          tranOrgId: '',
          tranOrgName: '',
          tranStatus: '',
        },

        rules: {
          contractNo: [
            {
              required: true,
              message: '请输入合同编号',
              trigger: 'blur',
            },
          ],
          contractName: [
            {
              required: true,
              message: '请输入合同名称',
              trigger: 'blur',
            },
          ],
          contractCnt: [
            {
              required: true,
              message: '请输入合同份数',
              trigger: 'blur',
            },
          ],
          tranOrgName: [
            {
              required: true,
              message: '请输入所属单位',
              trigger: 'blur',
            },
          ],
          handDeptName: [
            {
              required: true,
              message: '请输入经办部门',
              trigger: 'blur',
            },
          ],
          handStaffName: [
            {
              required: true,
              message: '请输入经办人',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        typeOptions: [],
        disabled: false,
        tranStatusList: [
          {
            label: '已移交',
            value: '1',
          },
          {
            label: '未移交',
            value: '0',
          },
        ],
        //提交
        jurisdictionCode: 0,
        fromId: 0,
        fromIdcopy: 0,
        ymFromId: 0,
        flowtaskinfoflowid: '',
        status: '',
      }
    },
    created() {},
    methods: {
      //回调
      handleTableSelected(row) {
        this.form.contractId = row.contractid
        this.form.contractName = row.contractname
        this.form.contractNo = row.contractno
      },
      //唤起弹框
      async showEdit(row, type) {
        if (type == 'add') {
          this.disabled = false
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          this.form.handStaffName = userInfo.realname
          this.form.handStaffId = userInfo.staffid

          this.form.handDeptName = userInfo.linkDetp.orgname
          this.form.handDeptId = userInfo.linkDetp.orgid

          this.form.tranOrgName = userInfo.linkOrg.orgname
          this.form.tranOrgId = userInfo.linkOrg.orgid
          this.title = '添加'
        } else if (type == 'edit') {
          this.form = { ...row }

          if (row.tranStatus == 2 || row.tranStatus == 3) {
            const res2 = await getFlowTaskInfo({
              tableId: 1,
              formId: row.tranId,
            })
            this.jurisdictionCode = res2.data.isFlowInfo
            if (res2.data.isFlowInfo) {
              this.flowtaskinfoflowid = res2.data.flowId + ''
              this.fromId = row.tranId + ''
              this.fromIdcopy = row.tranId + ''
              this.ymFromId = res2.data.id + ''

              const res3 = await getFaqiInfo({
                id: res2.data.id,
                flowId: res2.data.flowId,
              })
              if (res3.code == 1) {
                this.status = res3.data.dataJson.flowTaskInfo.status
              }
            }
          }

          this.disabled = false
          this.form.tranStatus2 = row.tranSituation ? +row.tranSituation : '0'
          // this.form.tranStatus2 = row.tranStatus.toString()
          // this.form.tranStatus2 = this.setStatus(row.tranStatus)
          this.title = '编辑'
        } else {
          this.title = '查看'
          this.form = { ...row }
          this.form.tranStatus2 = row.tranSituation ? +row.tranSituation : '0'
          this.disabled = true
        }
        this.dialogFormVisible = true
      },
      //过滤状态
      setStatus(tranStatus) {
        return tranStatus == 1
          ? '审批中'
          : tranStatus == 2
          ? '已退回'
          : tranStatus == 3
          ? '已通过'
          : tranStatus == 4
          ? '已终止'
          : tranStatus == 5
          ? '已跟踪'
          : tranStatus == 6
          ? '已完成'
          : '未审批'
      },
      close() {
        this.$refs['form'].resetFields()
        this.form = this.$options.data().form
        this.clearType = true
        this.$emit('fetch-data')
        this.dialogFormVisible = false
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { contractNo, contractName, tranStatus, ...other } = this.form
            const { msg, data, code } = await contractTranListSave({ ...other })
            if (code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')

              this.close()
            }
          }
        })
      },
      //提交
      async ymsubmit() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            this.$refs.resubmit.ymsubmit()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
