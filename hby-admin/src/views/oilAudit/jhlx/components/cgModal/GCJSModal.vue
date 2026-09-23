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
                <el-button type="text" @click="handleDetail(row)">
                  {{ row.tblYqnsGcxmzj.htbh }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="工程名称"
              prop="tblYqnsGcxmzj.gcmc"
            ></el-table-column>
            <el-table-column
              align="center"
              label="建设单位"
              prop="tblYqnsGcxmzj.jsdw"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="二审审查金额（万元）"
              prop="tblYqnsGcxmzj.esscje"
              show-overflow-tooltip
              min-width="90"
              :formatter="(row) => row.tblYqnsGcxmzj.esscje?.toFixed(2)"
            />
            <el-table-column
              align="center"
              label="额度"
              prop="edje"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="内外部"
              prop="nwb"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="施工单位"
              prop="tblYqnsGcxmzj.sgdw"
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
    <gcxmzjzjbEdit ref="edit" @fetch-data="fetchData" />
  </el-dialog>
</template>
<script>
  import { getGcxmjsListByhz } from '@/oapi/audit/project'
  import gcxmzjzjbEdit from '@/views/oilAudit/gcgl/components/gcxmzjzjbEdit.vue'
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
    components: { gcxmzjzjbEdit },
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
          // jsdw: undefined,
          pageNumber: 1,
          pageSize: 20,
          id: undefined,
          relaId: undefined,
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
        this.queryForm.relaId = row.relaid || ''
        this.getExecutorList()
        this.select = []
      },

      async getExecutorList() {
        this.listLoading = true
        const { data:{
          tlist,totalRecord
        } } = await getGcxmjsListByhz(this.queryForm)
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
        await this.$refs['edit'].showEdit(row, true)
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
