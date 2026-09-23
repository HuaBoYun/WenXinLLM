<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <el-table v-loading="listLoading" :data="list" style="width: 100%">
            <el-table-column align="center" label="合同编号" prop="htbh">
              <template #default="{ row }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handleDetail(row)"
                >
                  {{ row.htbh }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="工程或费用名称"
              prop="gchfymc"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="填报单位"
              prop="tbdwName"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="实施单位"
              prop="ssdw"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="批复概算投资"
              prop="pfgstzje"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="合同金额"
              prop="htje"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="结算金额"
              prop="jsje"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="项目类别"
              prop="fl"
              show-overflow-tooltip
            />
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
      </div>
    </div>
    <jsxmtzwcqkEdit ref="edit"></jsxmtzwcqkEdit>
  </el-dialog>
</template>
<script>
  import { getJSTZmodalList } from '@/oapi/audit/project'
  import jsxmtzwcqkEdit from '@/views/oilAudit/gcgl/components/jsxmtzwcqkEdit.vue'
  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: true,
      },
    },
    components: { jsxmtzwcqkEdit },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const curOrgId = userInfo.currentOrg.orgid
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curOrgId,
        dialogVisible: false,
        list: [],
        dataTree: [],
        queryForm: {
          tbdwName: undefined,
          pageNumber: 1,
          pageSize: 20,
          id: undefined,
        },
        current: undefined,
        reviewType: '',
        select: [],
      }
    },
    methods: {
      showEdit(row) {
        this.dialogVisible = true
        this.current = undefined
        this.queryForm.id = row.id || ''
        this.queryForm.tbdwName = row.id ? '' : row.relaOrgName
        this.getExecutorList()
        this.select = []
      },

      async getExecutorList() {
        this.listLoading = true
        const { data:{
          tlist,totalRecord
        } } = await getJSTZmodalList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, 'detail')
      },
    },
  }
</script>
<style scoped lang="scss">
  // //隐藏表头全选框
  // ::v-deep thead {
  //   .el-table-column--selection {
  //     .el-checkbox__inner {
  //       display: none !important;
  //     }
  //   }
  // }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
