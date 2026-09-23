<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.counterpartno"
              clearable
              placeholder="编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.budgetname"
              clearable
              placeholder="名称"
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
      <vab-query-form-right-panel :span="6">
        <el-button type="success" @click="addXdf">新建</el-button>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @select="handleSelected"
      ref="multipleTable"
      v-if="recordType == 'HTGL001'"
    >
      <template>
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="相对方编号"
          prop="counterpartno"
        />
        <el-table-column
          align="center"
          label="相对方名称"
          prop="budgetname"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleDetail(row)">
              {{ row.budgetname }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="注册资本" prop="totaltmoney" />
        <el-table-column
          align="center"
          label="法定代人"
          prop="projectstagegoal"
        />
        <!-- <el-table-column
          align="center"
          label="审批状态"
          prop="inspectionstatus"
        >
          <template slot-scope="{ row }">
            {{ mapStatus(row) }}
          </template>
        </el-table-column> -->
        <el-table-column align="center" label="操作" prop="inspectionstatus">
          <template slot-scope="{ row }">
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>

        <!-- <el-table-column
          align="center"
          label="相对方地址"
          prop="counterpartaddress"
        />
        <el-table-column
          align="center"
          label="开户银行"
          prop="counterparthank"
        /> -->
      </template>
    </el-table>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected1"
      ref="multipleTable"
      v-else
    >
      <el-table-column align="center" label="合同编号" prop="contractno" />
      <el-table-column align="center" label="合同名称" prop="contractname" />
      <el-table-column align="center" label="合同类型" prop="contracttype" />
      <el-table-column align="center" label="合同金额" prop="contractmoney" />
      <el-table-column align="center" label="开始日期" prop="startdate" />
      <el-table-column align="center" label="结束日期" prop="enddate" />
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
    <MaintainEdit ref="xdf-add" @fetch-data="fetchData" />
    <MaintainDetail ref="detail" />
  </el-dialog>
</template>
<script>
  import { getXdfOptions } from '@/api/contract/manage'
  import { deleteOpposite } from '@/api/contract/opposite'
  import { approvalStatus } from '@/views/contract/consts'
  import MaintainDetail from '@/views/contract/opposite/components/MaintainDetail'
  import MaintainEdit from '@/views/contract/opposite/components/MaintainEdit'
  export default {
    name: 'XdfOptions',
    components: {
      MaintainEdit,
      MaintainDetail,
    },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {},
        field: undefined,
        editIndex: 0,
        recordType: 'HTGL001', // 存储recordType，以防搜索重置时重置成相对方的搜索
      }
    },
    created() {},
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row)
      },
      async handleDelete(row) {
        const userInfo = window.localStorage.getItem('userInfo')

        // const { code } = await checkStatus({ budgetId: row.budgetid });
        // if (code != 1) return;
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteOpposite({
            budgetId: row.budgetid,
            staffId: JSON.parse(userInfo).staffid,
            // flowId: this.queryForm.flowId,
          })
          if (code == '1') {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
      },
      // async mapStatus(row) {
      //   const { inspectionstatus, isblack } = row
      //   // if (isblack == 1) return '已加入黑名单'
      //   const res = approvalStatus.filter((item) => {
      //     return item.value === inspectionstatus
      //   })
      //   if (code == '1') {
      //     this.$baseMessage(msg, 'success', 'vab-hey-message-success')
      //   }
      //   await this.fetchData()
      // },

      mapStatus(row) {
        const { inspectionstatus, isblack } = row
        // if (isblack == 1) return '已加入黑名单'
        const res = approvalStatus.filter((item) => {
          return item.value === inspectionstatus
        })
        return res[0].label
      },
      addXdf() {
        this.$refs['xdf-add'].showEdit()
      },
      resetQueryForm() {
        this.queryForm = {
          counterpartno: undefined,
          budgetname: undefined,
          recordType: 'HTGL001',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      show(recordType, field, index) {
        this.editIndex = index
        this.resetQueryForm()
        this.recordType = recordType // 在显示时确定
        this.field = field
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.queryForm.recordType = this.recordType
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getXdfOptions(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelected(val) {
        const { isblack } = val
        if (isblack === 1) return this.$message.warning('已加入黑名单')
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.current = val
        // this.$emit('selected', val, this.field)
        // this.dialogFormVisible = false
      },
      handleSelected1(val) {
        const { isblack } = val
        if (isblack === 1) return this.$message.warning('已加入黑名单')
        this.current = val
        // this.$emit('selected', val, this.field)
        // this.dialogFormVisible = false
      },

      confirm() {
        if (this.current.length == 0) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }
        if (
          this.recordType == 'HTGL001'
            ? this.current[0].isblack == 1
            : this.current.isblack == 1
        ) {
          this.$baseMessage(
            '该相对方已加入黑名单，不可选择',
            'error',
            'vab-hey-message-error'
          )
          return
        }

        this.$emit(
          'selected',
          this.recordType == 'HTGL001' ? this.current[0] : this.current,
          this.field,
          this.editIndex
        )
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
        this.current = []
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
