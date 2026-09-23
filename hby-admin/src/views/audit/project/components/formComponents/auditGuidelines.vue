<template>
  <el-dialog
    title="指引模板"
    :visible.sync="dialogVisible"
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
        show-overflow-tooltip
        width="120"
      ></el-table-column>
      <el-table-column
        prop="templeteType"
        label="审计类型"
        show-overflow-tooltip
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
import { getNbsjTempleteList } from '@/api/audit/project'
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
      /**
       * @description   组件初始化
       * @param {*}  
        * @return {*}
       */ 
    showEdit(tempType, templeteType) {
      this.queryForm.tempType = tempType
      this.queryForm.templeteType = templeteType

      this.getNbsjTempleteListFun()
      this.dialogVisible = true
    },
      /**
       * @description 获取列表
       * @param {*}  
        * @return {*}
       */  
    async getNbsjTempleteListFun() {
      this.listLoading = true
      const {
        data: {
          pageInfo: { tlist, totalRecord },
        },
      } = await getNbsjTempleteList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
      /**
       * @description 分页，选择每页几条数据，查询每页多少条数据
       * @param {*}  
        * @return {*}
       */  
    /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.getNbsjTempleteListFun()
    },
      /**
       * @description  分页，选择页码，查询第几页的数据
       * @param {*}  
        * @return {*}
       */  
    /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.getNbsjTempleteListFun()
    },
      /**
       * @description 选择列表数据,把选择的数据存入multipleSelection
       * @param {*}  
        * @return {*}
       */  
    handleSelection(val) {
      if (val.length > 1) {
        let del = val.shift()
        this.$refs.multipleTable.toggleRowSelection(del, false)
      }
      this.multipleSelection = val
    },
      /**
       * @description  点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}  
        * @return {*}
       */  
    save() {
      this.$emit('guidelinesList', this.multipleSelection)
      this.dialogVisible = false
    },
  },
}
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
::v-deep thead {
  .el-table-column--selection {
    .el-checkbox__inner {
      display: none !important;
    }
  }
}
</style>
