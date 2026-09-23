<template>
  <div>
    <div class="image"><img :src="url" /></div>
    <div class="table">
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="流程ID" prop="processName" />
        <el-table-column align="center" label="办理人" prop="approver">
          <template #default="{ row }">
            <el-button type="text">{{ row.approver }}</el-button>
          </template>
        </el-table-column>
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
        <!-- <el-table-column
          align="center"
          label="相关资料"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">资料</el-button>
          </template>
        </el-table-column> -->
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
      borrow: {
        type: Object,
        default: null,
      },
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
          lendid: undefined,
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
      //请求数据
      async fetchApproval() {
        if (this.moduleName == 'borrow') {
          this.queryForm.lendid = this.borrow.lendid
        } else {
          this.queryForm.contractId = this.contract.contractid
          this.queryForm.budgetId = this.contract.budgetid
        }

        // let fn = null
        // if (this.moduleName == 'seal' || this.moduleName == 'opposite') {
        //   fn = viewDealProcessForSeal
        // } else if (this.moduleName == 'borrow') {
        //   fn = viewDealProcessForBorrow
        // } else {
        //   fn = viewDealProcess
        // }
        const fn =
          this.moduleName == 'seal' || this.moduleName == 'opposite'
            ? viewDealProcessForSeal
            : this.moduleName == 'borrow'
            ? viewDealProcessForBorrow
            : viewDealProcess

        this.listLoading = true
        if (this.queryForm.taskId) {
          const {
            data: { taskList, url },
          } = await fn(this.queryForm)
          this.list = taskList
          this.total = 0
          this.url = url + `?timestamp=${new Date().getTime()}`
          this.listLoading = false
        }
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
    },
  }
</script>
