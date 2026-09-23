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
    <!-- <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form> -->
    <el-table :data="list">
      <el-table-column
        align="center"
        label="名称"
        prop="tableNameEn"
      ></el-table-column>

      <el-table-column align="center" label="状态" prop="state">
        <template #default="{ row }">
          {{ status[+row.state] }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="错误信息"
        prop="errorMsg"
      ></el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template #default="{ row }">
          <el-button type="text" @click="handleAdd(row)">字段信息</el-button>
        </template>
      </el-table-column>
    </el-table>
    <FieldEdit ref="edit" @fetchData="fetchData"></FieldEdit>
  </el-dialog>
</template>

<script>
  import { getExcelDetailList } from '@/api/setting/org'
  import FieldEdit from './fieldEdit.vue'
  export default {
    components: { FieldEdit },
    data() {
      return {
        dialogFormVisible: false,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          excelId: '',
        },
        loading: false,
        footer: true,
        title: 'excel列表',
        list: [],
        status: ['未生成表', '已生成表', '失败'],
      }
    },
    methods: {
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
      },
      /**
       * @description: 打开表单
       * @param {*} row 传入数据
       * @return {*}
       */      
      showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm.excelId = row.id
        this.fetchData(this.queryForm)
      },
      handleAdd(row) {
        this.$refs['edit'].showEdit(row)
      },
      async fetchData(info) {
        const res = await getExcelDetailList(info || this.queryForm)
        this.list = res.data.tlist
      },
    },
  }
</script>
