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
          <el-table v-loading="listLoading" :data="list" highlight-current-row style="width: 100%" 
            @select="handleSelection"
            ref="multipleTable">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column align="center" label="合同编号" prop="htbh">
              <!-- <template #default="{ row }">
                <el-button
                  type="text"
                  :disabled="false"
                  @click="handleDetail(row)"
                >
                  {{ row.htbh }}
                </el-button>
              </template> -->
            </el-table-column>
            <el-table-column
              align="center"
              label="项目类别"
              prop="fl"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="验收项目名称"
              prop="ysxmmc"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="填报单位"
              prop="tborgname"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="计划投资金额（万元）"
              prop="jhtzje"
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
          <div style="text-align: right;">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button @click="save" type="primary">
              确定
            </el-button>
          </div>
        </div>
      </div>
    </div>
    <!-- <jsxmtzwcqkEdit ref="edit"></jsxmtzwcqkEdit> -->
  </el-dialog>
</template>
<script>
  import { jsxmjbqkJswcgetlist } from '@/oapi/audit/plan'
  // import jsxmtzwcqkEdit from '@/views/oilAudit/gcgl/components/jsxmtzwcqkEdit.vue'
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
    // components: { jsxmtzwcqkEdit },
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
          htbh: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
      }
    },
    methods: {
      handleClick(row) {
        this.rows = row
      },
      showEdit(row) {
        this.dialogVisible = true
        this.current = undefined
        this.getExecutorList()
        this.multipleSelection = []
      },
      async getExecutorList() {
        this.listLoading = true
        const { data:{
          tlist,totalRecord
        } } = await jsxmjbqkJswcgetlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        // this.setCheckedRows()
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
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      }
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
