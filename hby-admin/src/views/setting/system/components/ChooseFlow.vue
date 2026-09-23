<!--
 * @Date: 2022-04-25 09:33:50
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-25 17:54:26
 * @FilePath: /hb-admin/src/views/setting/system/components/ChooseFlow.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="800px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <el-table
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @current-change="handleRowChange"
      >
        <el-table-column
          align="center"
          label="模块名称"
          prop="module"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="模板描述"
          prop="remark"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="状态">
          <template #default="{ row }">
            {{ row.status == 'ON' ? '开启' : '关闭' }}
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="confirm">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getContractAssociateWithFlow } from '@/api/setting/system'

  export default {
    name: 'ChooseFlow',
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          contracttype: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        title: '选择流程',
        dialogVisible: false,
        currentRow: undefined,
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      show() {
        this.fetchData()
        this.dialogVisible = true
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getContractAssociateWithFlow(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleRowChange(row) {
        this.currentRow = row
      },
      async confirm() {
        this.$emit('selected', this.currentRow)
        this.dialogVisible = false
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
