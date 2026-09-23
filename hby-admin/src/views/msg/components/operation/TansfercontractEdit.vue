<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="form" label-width="100px" :model="form" :rules="rules">
        <el-col :lg="12" :md="12" :sm="24">
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
              v-if="!disabled"
              @click="$refs.table.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="合同名称" prop="contractName">
            <el-input
              v-model="form.contractName"
              disabled
              placeholder="请输入合同名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="合同份数" prop="contractCnt">
            <el-input
              type="number"
              v-model="form.contractCnt"
              :disabled="disabled"
              placeholder="请输入合同份数"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="所属单位" prop="tranOrgName">
            <el-input
              v-model="form.tranOrgName"
              disabled
              placeholder="请输入所属单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
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
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="经办人" prop="handStaffName">
            <el-input
              v-model="form.handStaffName"
              disabled
              placeholder="请输入经办人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :lg="12" :md="12" :sm="24">
          <el-form-item label="移交情况" prop="tranStatus">
            <el-select
              v-model="form.tranStatus"
              :disabled="disabled"
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
          <el-form-item label="接收人意见" prop="legalMemo">
            <el-input
              v-model="form.legalMemo"
              type="textarea"
              :rows="3"
              placeholder="请输入接收人意见"
              :style="{ width: '100%' }"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div style="text-align: right; margin-top: 10px" v-if="!disabled">
      <!-- <el-button @click="close">取 消</el-button> -->
      <el-button type="primary" @click="add">确 定</el-button>
      <el-button type="primary" @click="ymsubmit">提 交</el-button>
    </div>

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
  </div>
</template>

<script>
  import {
    contractTranListSave,
    getContractTranInfo,
    ymWorkCandidates,
    ymWorkSubmit,
  } from '@/api/contract/manage'
  import CandidateUserSelect from '@/components/CandidateUserSelect.vue'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  import ContractTable from '@/views/contract/contractManage/components/ContractTable'
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
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
      }
    },
    created() {},
    methods: {
      handleTableSelected(row) {
        this.form.contractId = row.contractid
        this.form.contractName = row.contractname
        this.form.contractNo = row.contractno
      },
      async showEdit(
        type,
        row,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status
      ) {

        console.log("type",type) 
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
          const res = await getContractTranInfo({ tranId: row.id })
          this.form = res.data.tct
          this.disabled = false
          // this.form.tranStatus = this.setStatus(res.data.tct.tranStatus)
          this.form.tranStatus = res.data.tct.tranSituation
            ? +res.data.tct.tranSituation
            : '0'
          this.title = '编辑'
        } else {
          this.title = '查看'
          const res = await getContractTranInfo({ tranId: row.id })
          this.form = res.data.tct
          this.form.tranStatus = res.data.tct.tranSituation
            ? +res.data.tct.tranSituation
            : '0'
          this.disabled = true
        }

        this.fromId = formId
        this.fromIdcopy = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
      },
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

        this.$bus.$emit('updateMsg', 0)
      },
      add() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { contractNo, contractName, tranStatus, ...other } = this.form
            const { msg, data, code } = await contractTranListSave({ ...other })
            if (code == 1) {
              this.$baseMessage('成功', 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              // this.close()
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
