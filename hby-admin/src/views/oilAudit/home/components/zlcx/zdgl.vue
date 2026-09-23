<template>
  <div class="system-log-container">
    <div>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="制度编号" prop="rulecode">
          <template #default="{ row }">
            <el-button @click="handleView(row, true)" type="text">
              {{ row.rulecode }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="制度名称" prop="rulename" />
        <el-table-column align="center" label="类别" prop="innruletype" />
        <el-table-column
          align="center"
          label="生效日期"
          prop="publishdate"
          :formatter="formatDate"
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

    <ManageEdit ref="edit" @fetch-data="fetchData" />
    <LawPreview ref="preview" />
  </div>
</template>

<script>
  import {
    deleteInnerRuleInfo,
    exportInnerRuleInfo,
    getInnerRulePageList,
  } from '@/oapi/workbench/auditTools'
  import { UTCformat } from '@/utils/index'
  import LawPreview from '@/views/workbench/controlLib/components/LawPreview'

  import ManageEdit from '@/views/workbench/controlLib/ManageEdit.vue'

  export default {
    name: 'Consult',
    components: { ManageEdit, LawPreview },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      preview(row) {
        console.dir(row)
        this.$refs['preview'].showEdit(row.bodyinfo)
      },
      async exportFile(row) {
        console.dir(row)
        const data = await exportInnerRuleInfo(row.innrulid)
        let fileName = row.rulename
        let blob = new Blob([data], {
          type: 'application/msword;charset=utf-8',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeNode(node) {
        console.dir(node)
        this.queryForm.publishorg = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        const { Date, ...other } = this.queryForm
        let starttime = undefined
        let endtime = undefined
        if (Date) {
          starttime = Date[0]
          endtime = Date[1]
        }
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getInnerRulePageList({ ...other, starttime, endtime })
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleView(row, flag) {
        this.$refs['edit'].showEdit(row, flag)
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteInnerRuleInfo(row.innrulid)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
