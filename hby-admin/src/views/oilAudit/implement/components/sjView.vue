<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="14">
      <vab-query-form>
        <vab-query-form-left-panel>
          <span></span>
        </vab-query-form-left-panel>
        <vab-query-form-right-panel>
          <el-button type="primary" @click="handleAddOrUpdate()">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-card shadow="never" class="secondCard">
        <el-table v-loading="listLoading" :data="list">
          <el-table-column align="center" label="工程量计算" prop="gcljs" />
          <el-table-column align="center" label="定额套用" prop="dety" />
          <el-table-column align="center" label="现场实测" prop="xcsc" />
          <el-table-column align="center" label="物资价格" prop="wzjg" />
          <el-table-column align="center" label="其他审减" prop="qtsj" />
          <el-table-column align="center" label="审减额" prop="sje" />
          <el-table-column align="center" label="审计人员" prop="sjry" />
          <el-table-column align="center" label="备注" prop="bz" />
          <el-table-column align="center" label="操作" width="120">
            <template #default="{ row }">
              <el-button type="text" @click="handleAddOrUpdate(row)">
                编辑
              </el-button>
              <el-button type="text" @click="handlerDelete(row)">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          class="pagination"
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </el-card>
    </el-row>

    <sjEdit ref="sjEdit" @queryData="fetchData" />
  </el-dialog>
</template>

<script>
  import { getList, handleDelete } from '@/oapi/audit/task'
  import sjEdit from './sjEdit'

  export default {
    name: 'LeaveSummaryInfo',
    components: { sjEdit },
    data() {
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectId: undefined,
        },
        total: 0,
        list: [],
        dialogFormVisible: false,
        title: '审减内容',
      }
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAddOrUpdate(row) {
        this.$refs['sjEdit'].showModal(row)
      },
      handlerDelete(row) {
        // 删除题目
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDelete({ ids: row.sjnrid }).then(() => {
              this.fetchData()
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm.projectId = row.projectId
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          projectId: undefined,
        }
        this.list = []
        this.total = 0
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
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
