<template>
  <el-dialog
    :close-on-click-modal="false"
    :visible.sync="visible"
    :append-to-body="true"
    :data="modelList"
    title="模型列表"
    destroy-on-close
  >
    <el-table :data="modelList">
      <el-table-column type="selection"></el-table-column>
      <el-table-column label="模型ID" prop="modelid"></el-table-column>
      <el-table-column label="模型名称" prop="modelname"></el-table-column>
      <el-table-column label="模型类别" prop="modelcategory"></el-table-column>
      <el-table-column label="模型状态" prop="modelstatus"></el-table-column>
      <el-table-column label="创建日期" prop="createdate"></el-table-column>
    </el-table>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
    /> -->
    <template #footer>
      <el-button type="primary">保 存</el-button>
      <el-button>关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getModelList } from '@/api/monitor/model/remind'
  export default {
    data() {
      return {
        visible: false,
        modelList: [],
        modelPagination: {
          pageNumber: 1,
          limit: 20,
        },
      }
    },
    methods: {
      async show(type, form) {
        this.visible = true
        const { orgid } = form
        const { code, msg, data } = await getModelList({
          ...this.modelPagination,
          orgId: orgid,
          modelids: 0,
          solutionid: '',
          OrgidOrgid: '',
          modelid: '',
        })
        if (code === 200 && msg === '成功') {
          const { records } = data.pageBean
          this.modelList = records
        }
      },
    },
  }
</script>

<style>
  .form-edit .el-cascader {
    width: 100%;
  }

  .formula-view {
    margin-bottom: 10px;
  }

  .formula-view > * {
    margin-right: 10px;
  }
</style>
