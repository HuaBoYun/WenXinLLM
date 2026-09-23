<template>
  <div v-loading="listLoading">
    <div class="image"><img :src="url" width="100%" /></div>
    <div class="table">
      <el-table :data="list">
        <el-table-column align="center" label="流程ID" prop="processName" />
        <el-table-column align="center" label="办理人" prop="approver" />
        <el-table-column align="center" label="办理角色" prop="approvalrole" />
        <el-table-column align="center" label="办理结果" prop="result" />
        <el-table-column align="center" label="办理意见" prop="examination" />
        <el-table-column align="center" label="电子签名" prop="imgbasestr">
          <template #default="{ row }">
            <img :src="row.imgbasestr" width="100" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="办理时间" prop="approvaldate" />
        <el-table-column
          align="center"
          label="下一步：办理人/办理角色"
          prop="handle"
        />
        <!--        <el-table-column-->
        <!--          align="center"-->
        <!--          label="相关资料"-->
        <!--          show-overflow-tooltip-->
        <!--          width="120"-->
        <!--        >-->
        <!--          <template #default="{ row }">-->
        <!--            <el-button type="text" @click="handleEdit(row)">资料</el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
      </el-table>
    </div>
  </div>
</template>
<script>
  import {
    viewDealProcess,
    viewDealProcessForBorrow,
    viewDealProcessForSeal,
  } from '@/api/contract/manage'

  export default {
    name: 'DealApprovalInfo',
    props: {
      contract: {
        type: Object,
        default: null,
      },
      moduleName: {
        type: String,
        default: 'sample',
      },
      businessKey: {
        type: [String, Number],
        default: '',
      },
    },

    data() {
      return {
        url: undefined,
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          contractId: undefined,
          budgetid: undefined,
          taskId: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        options: [],
      }
    },
    watch: {
      businessKey(val) {
        this.queryForm.taskId = val
        this.fetchApproval()
      },
    },
    created() {},

    methods: {
      async fetchApproval() {
        this.listLoading = true
        this.queryForm.contractId = this.contract && this.contract.taskid
        this.queryForm.budgetId = this.contract && this.contract.taskid
        this.queryForm.lendid = this.contract && this.contract.taskid
        if (this.queryForm.taskId) {
          const fn =
            this.moduleName == 'seal' || this.moduleName == 'opposite'
              ? viewDealProcessForSeal
              : this.moduleName == 'borrow'
              ? viewDealProcessForBorrow
              : viewDealProcess
          const {
            data: { taskList, url },
          } = await fn(this.queryForm)

          this.list = taskList
          this.total = 0
          this.url = url + `?timestamp=${new Date().getTime()}`
        }

        // this.reload(url)
        this.listLoading = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
    },
  }
</script>
