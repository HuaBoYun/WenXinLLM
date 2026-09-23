<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button type="success" @click="fetchData">刷新</el-button>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table :data="list">
      <el-table-column align="center" label="数据用户" prop="dataBaseUsers">
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.dataBaseUsers }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="excel名称"
        prop="dataBaseConnectionAddress"
      ></el-table-column>

      <el-table-column align="center" label="状态" prop="state">
        <template #default="{ row }">
          {{ status[+row.state] }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="信息"
        prop="errorMsg"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ row.errorMsg || '' }}
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="210">
        <template #default="{ row }">
          <el-button type="text" @click="handleList(row)">
            excel列表信息
          </el-button>
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDown(row)">下载</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <ExcelEdit ref="edit" @fetchData="fetchData"></ExcelEdit>
    <FiledList ref="filed" @fetchData="fetchData"></FiledList>
  </el-dialog>
</template>

<script>
  import ExcelEdit from './excelEdit.vue'
  import FiledList from './fieldList.vue'
  import { getExcelExportList, ExcelExportDelete } from '@/oapi/setting/org'
  import { download } from '@/oapi/audit/implement'
  export default {
    components: { ExcelEdit, FiledList },
    data() {
      return {
        dialogFormVisible: false,
        loading: false,
        title: '导入记录',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          dataBaseId: '',
        },
        list: [],
        status: ['', '进行中', '已完成', '失败'],
        info: {},
      }
    },
    methods: {
      close() {
        this.dialogFormVisible = false
      },
      showEdit(id, row) {
        this.queryForm.dataBaseId = id
        this.info = row
        this.dialogFormVisible = true
        this.fetchData()
      },
      handleRead(row) {
        this.$refs['edit'].showEdit(row, 'detail', this.info)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, 'edit', this.info)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add', this.info)
      },
      handleList(row) {
        this.$refs['filed'].showEdit(row)
      },
      handleDelete(row) {
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.loading = true
            ExcelExportDelete({ id: row.id }).then((res) => {
              this.loading = false
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      fetchData() {
        getExcelExportList(this.queryForm).then((res) => {
          this.list = res.data.tlist
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.excelId })
        let filename = row.excelName
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
    },
  }
</script>
