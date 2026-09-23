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
          <el-table
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            @select-all="handleSelectAll"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
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

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>

    <lrjjzrsqView ref="edit" @fetchData="fetchData"></lrjjzrsqView>
  </el-dialog>
</template>
<script>
  import { getSjdwlrsjSbListByzg } from '@/oapi/audit/project'
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
        loading: false,
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
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },
      showEdit(row) {
        this.dialogVisible = true
        this.current = undefined
        this.queryForm.id = row.id || ''
        this.queryForm.relaId = row.relaid || ''
        // this.queryForm.orgId = row.id ? '' : row.relaOrgId
        this.getExecutorList()

        if (row.ids && row.ids.length > 0) {
          this.select = row.ids.split(',')
          console.log(this.select)
          this.setCheckedRows()
        } else {
          this.select = []
        }
      },
      save() {
        const ids = this.select.map((res) => res.id || res)
        this.$emit('fetchData', ids)
        this.close()
      },
      close() {
        this.list = []
        this.select = []
        this.dialogVisible = false
      },

      async getExecutorList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getSjdwlrsjSbListByzg({ ...this.queryForm, type: 1 })
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
      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return (row.id || row) == item.id
              }),
              true
            )
          })
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep .el-table {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: block !important;
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
