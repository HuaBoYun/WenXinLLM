<template>
  <el-dialog
    title="转发记录"
    :visible="visible"
    width="800px"
    :close-on-click-modal="false"
    append-to-body
    @close="close"
  >
    <el-table :data="list" v-loading="loading">
      <el-table-column
        prop="zfrename"
        label="转发人名称"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="jsrename"
        label="接收人名称"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="createdate"
        label="转发时间"
        align="center"
        :formatter="formatDate"
      ></el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { zgzzGetzfUserlist } from '@/api/zgzz/index.js'
  import { formatDay } from '@/utils/index'

  export default {
    data() {
      return {
        visible: false,
        loading: false,
        list: [],
        id: '',
      }
    },
    methods: {
      showEdit(row) {
        this.visible = true
        this.id = row.relaId
        this.getList()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      async getList() {
        this.loading = true
        try {
          const { data } = await zgzzGetzfUserlist({ relaId: this.id })
          this.list = data || []
        } catch (error) {
          console.error('获取转发记录失败:', error)
        } finally {
          this.loading = false
        }
      },
      close() {
        this.visible = false
      },
    },
  }
</script>
<style lang="scss" scoped></style>
