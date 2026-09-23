<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="查看底稿"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-card shadow="never" class="secondCard">
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="审计项目名称"
          prop="projectName"
        />
        <el-table-column align="center" label="审计事项" prop="auditMatters" />
        <el-table-column
          align="center"
          label="被审计单位名称"
          prop="auditeeName"
        ></el-table-column>
        <el-table-column
          align="center"
          label="创建时间"
          prop="createTime"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="handleAddOrUpdate(row)">
              修改
            </el-button>
            <el-button type="text" @click="handlerDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <Views ref="edit" @queryData="fetchData"></Views>
  </el-dialog>
</template>

<script>
  import { getAllList, handleDelete } from '@/oapi/audit/newMyDraft'
  import Views from './newMyDraftView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  export default {
    components: {
      Views,
      filterTable,
      filterSearch,
    },
    data() {
      return {
        type: 'edit',
        listLoading: true,
        dialogFormVisible: false,
        typeId: '', //从我的任务页面打开时需要
        templateId:'',
        list: [],
      }
    },
    methods: {
      async showModal(row,templateId) {
        this.dialogFormVisible = true
        this.typeId = row.typeId
        this.templateId =  templateId
        // console.log("row",row)
        await this.fetchData()
      },
      close() {
        this.list = []
        this.typeId = ''
        this.dialogFormVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { MyManuscriptList },
        } = await getAllList({
          typeId: this.typeId,
          templateId:this.templateId
        })

        this.list = MyManuscriptList
        this.listLoading = false
      },
      handleAddOrUpdate(row) {
        this.$refs['edit'].showModal(row)
      },
      handlerDelete(row) {
        // 删除题目
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            handleDelete({ id: row.id }).then(() => {
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
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
