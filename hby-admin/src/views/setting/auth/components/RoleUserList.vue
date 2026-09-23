<!--
 * @Date: 2022-03-31 15:03:53
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-07 11:18:00
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/BorrowHistory.vue
-->
<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.username"
              clearable
              placeholder="用户名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.realname"
              clearable
              placeholder="真实姓名"
            />
          </el-form-item>

          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="用户名" prop="username">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.username }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="手机" prop="miblephone" />
      <el-table-column align="center" label="固定电话" prop="fixedphone" />
      <el-table-column align="center" label="电子邮件" prop="email" />
      <el-table-column
        align="center"
        label="备注"
        prop="memo"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="所属部门"
        prop="orgname"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="启用弃用状态"
        prop="status"
        #default="{ row }"
      >
        {{ row.status === 1 ? '启用' : '弃用' }}
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
    <template #footer>
      <el-button @click="close">取 消</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { getroleUserList } from '@/api/setting/auth'

  export default {
    name: 'BorrowHistory',
    components: {},
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          roleid: undefined,
          realname: undefined,
          username: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        dialogVisible: false,
        title: '所属用户',
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.dialogVisible = true
        this.queryForm.roleid = row.rid
        this.queryForm.pageSize = 100
        this.fetchData()
      },

      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getroleUserList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },

      close() {
        this.dialogVisible = false
      },
      resetQueryForm() {
        this.queryForm.realname = undefined
        this.queryForm.username = undefined
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
