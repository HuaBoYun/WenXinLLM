<template>
  <el-dialog
    title="审计模板"
    :visible.sync="dialogVisible"
    :modal-append-to-body="false"
    :append-to-body="true"
    width="50%"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        prop="templeteCode"
        label="模板编号"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="templeteName"
        label="模板名称"
        width="120"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="templeteType"
        label="审计类型"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="temorgname"
        label="适用机构"
        show-overflow-tooltip
        width="120"
      ></el-table-column>
      <el-table-column
        prop="createstaffname"
        label="创建人"
        width="120"
      ></el-table-column>
      <el-table-column prop="createDate" label="创建日期" width="120">
        <template slot-scope="{ row }">
          {{ dayjs(row.createDate).format('YYYY-MM-DD') }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          {{ row.status == 1 ? '启用' : '禁用' }}
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
  </el-dialog>
</template>
<script>
  import { getNbsjTempleteList, projectyzmb } from '@/oapi/audit/project'
  import * as dayjs from 'dayjs'
  export default {
    data() {
      return {
        dayjs: dayjs,
        dialogVisible: false,
        list: [],
        multipleSelection: [],
        total: 0,
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          tempType: 0,
          status: 1,
        },
      }
    },
    methods: {
      showEdit(tempType, templeteType) {
        this.queryForm.tempType = tempType
        this.queryForm.templeteType = templeteType

        this.getNbsjTempleteListFun()
        this.dialogVisible = true
      },
      async getNbsjTempleteListFun() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getNbsjTempleteList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getNbsjTempleteListFun()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getNbsjTempleteListFun()
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async save() {
        let params = {
          tempid: this.multipleSelection[0].templeteId,
        }
        const { code } = await projectyzmb(params)
        if (code != 1) return
        this.$emit('templateList', this.multipleSelection)
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
