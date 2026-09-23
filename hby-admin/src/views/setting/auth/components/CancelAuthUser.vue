<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="21">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.realname"
              clearable
              placeholder="用户姓名"
              style="margin-left: 20px"
            />
          </el-form-item>
          <!--          <el-form-item>-->
          <!--            <el-cascader-->
          <!--              clearable-->
          <!--              :options="data"-->
          <!--              placeholder="所属部门"-->
          <!--              :props="{ checkStrictly: true }"-->
          <!--              style="width: 100%"-->
          <!--            />-->
          <!--          </el-form-item>-->
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
      <vab-query-form-right-panel :span="3">
        <el-button
          native-type="submit"
          type="primary"
          @click="handleCancelRight()"
        >
          取消授权
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="60" />
      <el-table-column align="center" label="用户真实姓名" prop="realname" />
      <el-table-column align="center" label="所属部门" prop="orgName" />
      <el-table-column
        v-if="false"
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="$refs.cancelAuthUser.showEdit(row)">
            取消授权
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
    <ContractInfo ref="contractInfo" />
  </el-drawer>
</template>

<script>
  import ContractInfo from '@/views/contract/opposite/components/ContractInfo'
  import { zbqxindexs, delAccBookManage } from '@/api/setting/auth'
  export default {
    name: 'CancelAuthUser',
    components: { ContractInfo },
    data() {
      return {
        queryForm: {
          realname: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 3,
        title: '取消授权',
        dialogFormVisible: false,
        list: [],
        data: [],
        multipleSelection: [],
        acctid: '',
      }
    },
    created() {},
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.queryForm.acctid = this.acctid
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm.acctid = row.acctid
          .split(',')
          .map((i) => "'" + i + "'")
          .join(',')
        this.acctid = this.queryForm.acctid
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await zbqxindexs(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelectionChange(val) {
        console.warn('handleSelectionChange', val)
        this.multipleSelection = val
      },
      handleCancelRight() {
        //
        const staffid = this.multipleSelection.map((i) => i.staffid).join(',')
        if (!staffid.length) {
          this.$message.error('请选择')
          return
        }
        const acctid = this.queryForm.acctid
          .split(',')
          .map((item) => item.replaceAll("'", ''))
          .join(',')
        delAccBookManage({
          orgid: this.orgid,
          acctid: acctid,
          sid: staffid,
        }).then((res) => {
          console.log('delAccBookManage', res)
          if (res.code === 1) {
            this.$message.success('已选定')
            this.$refs.multipleTable.clearSelection()
          } else {
            this.$message.error('操作失败')
          }
        })
      },
    },
  }
</script>
<style scoped></style>
