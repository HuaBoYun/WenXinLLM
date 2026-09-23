<template>
  <div class="system-log-container">
    <div class="">
      <!-- 筛选条件 -->
      <div class="filter-container">
        <el-form :inline="true" :model="queryForm" class="demo-form-inline">
          <el-form-item label="">
            <el-input
              v-model="queryForm.stepno"
              placeholder="请输入模型编号"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="">
            <el-input
              v-model="queryForm.steptitle"
              placeholder="请输入模型名称"
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              @click="queryData"
            >
              查询
            </el-button>
            <el-button type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="模型编号" prop="stepno" />
        <el-table-column align="center" label="模型名称" prop="steptitle" />
        <el-table-column align="center" label="关联数据源" prop="stepcontent" />

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="180"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">预览</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        background
        v-if="showFooter"
        :current-page="queryForm.pageNumber"
        :layout="layout"
        :page-size="queryForm.pageSize"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />

      <!-- <Edit ref="edit" @fetchData="fetchData"></Edit>
      <Result ref="result" @fetchData="fetchData"></Result> -->
    </div>
    <SqlModal ref="check"></SqlModal>
  </div>
</template>

<script>
  import {
    getSJMXKList,
    deleteSJMXKInfo,
    getSJMXKDetailInfo,
    executeSql,
  } from '@/api/setting/org'
  import { querySendRiskModelListByStaff } from '@/api/risk'
  import { UTCformat } from '@/utils/index'
  import SqlModal from '@/views/audit/base/components/sqlCheck.vue'

  export default {
    name: 'Consult',
    components: { SqlModal },
    props: ['showFooter'],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          typeId: '',
          stepno: '',
          steptitle: '',
          pageNumber: 1,
          pageSize: 20,
          mpdeltype: 'FXCT',
        },
        nodeId: '',
        total: 0,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeNode(node) {
        this.queryForm.typeId = node.id
        this.nodeId = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return UTCformat(data)
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await querySendRiskModelListByStaff(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },

      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await deleteSJMXKInfo({ stepId: row.stepid })
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
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        if (!this.nodeId) {
          this.$message({
            type: 'error',
            message: '请先选择节点',
          })
          return
        }
        this.$refs['edit'].show({ nodeId: this.nodeId }, '新增')
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        this.$refs['check'].show(row.sqlstr, row.bookid)
      },
      async handleExecute(row) {
        executeSql({ stepId: row.stepid }).then((res) => {
          this.$baseMessage(res.msg, 'success', 'vab-hey-message-success')
        })
      },
      handleResult(row) {
        //弹框
        this.$refs['result'].show(row)
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
  .filter-container {
    margin-bottom: 20px;
    background: #fff;
  }

  .demo-form-inline .el-form-item {
    margin-right: 20px;
  }

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
