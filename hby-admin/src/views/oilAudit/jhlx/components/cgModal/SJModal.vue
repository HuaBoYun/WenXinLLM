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
            <el-table-column align="center" label="姓名" prop="name">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleDetail(row)"
                  style="white-space: pre-line; line-height: 16px"
                >
                  {{ row.name }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column align="center" label="原职务" prop="oldJob" />
            <el-table-column align="center" label="现职务" prop="job" />
            <el-table-column
              align="center"
              label="原行政级别"
              prop="oldLevel"
            ></el-table-column>
            <el-table-column align="center" label="原单位" prop="oldOrg">
              <template #default="{ row }">
                {{ row.oldOrg?.orgname }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="现单位" prop="oldOrg">
              <template #default="{ row }">
                {{ row.org?.orgname }}
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
      </div>
    </div>
    <lrjjzrsqView ref="edit" @fetchData="fetchData"></lrjjzrsqView>
  </el-dialog>
</template>
<script>
  import { getSjdwlrsjSbListByhz } from '@/oapi/audit/project'
  import lrjjzrsqView from '@/views/oilAudit/lrjjzr/components/lrjjzrsqView.vue'
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
    components: { lrjjzrsqView },
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
          // orgId: undefined,
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
        // this.queryForm.orgId = row.id ? '' : row.relaOrgId
        this.getExecutorList()
        this.select = []
      },

      async getExecutorList() {
        this.listLoading = true
        const { data:{
          tlist,totalRecord
        } } = await getSjdwlrsjSbListByhz(this.queryForm)
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
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, '详情')
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
