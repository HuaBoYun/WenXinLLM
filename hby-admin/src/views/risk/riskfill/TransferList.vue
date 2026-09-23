<template>
  <el-dialog
    title="转派记录"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    append-to-body
  >
    <el-table :data="list" v-loading="loading">
      <el-table-column
        prop="impRiskName"
        label="风险名称"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="initialName"
        label="转派人"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="toName"
        label="被转派人"
        align="center"
      ></el-table-column>

      <el-table-column
        prop="createtime"
        label="转派时间"
        align="center"
      ></el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { getMajorTransferList } from '@/api/risk/create.js'

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
        this.id = row.id
        this.getList()
      },
      async getList() {
        this.loading = true
        try {
          const {
            data: { data },
          } = await getMajorTransferList({ id: this.id })
          this.list = data || []
        } catch (error) {
          console.error('获取转派记录失败:', error)
        } finally {
          this.loading = false
        }
      },
    },
  }
</script>
<style lang="scss" scoped></style>
