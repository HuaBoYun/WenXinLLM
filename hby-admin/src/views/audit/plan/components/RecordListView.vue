<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="业务单元" prop="data">
        <template #default="{ row }">
          <el-button type="text" @click="$refs['recordListInfo'].showEdit()">
            {{ row.data }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="风险描述"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="控制措施" prop="data" />
      <el-table-column
        align="center"
        label="审计程序"
        prop="data"
        width="100"
      />
      <el-table-column align="center" label="所需资料" prop="data" />
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <record-list-info ref="recordListInfo" />
  </div>
</template>

<script>
import { getList } from '@/api/systemLog'
import RecordListInfo from './RecordListInfo'

export default {
  name: 'Download',
  components: { RecordListInfo },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        code: '',
        name: '',
        pageNo: 1,
        pageSize: 20,
      },
    }
  }, 
  created() {
    this.fetchData()
  },
  methods: { 
      /**
       * @description 分页，选择每页几条数据，查询每页多少条数据
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
      this.fetchData()
    },
     /**
       * @description 分页，选择页码，查询第几页的数据
       * @param {*}  
       * @return {*}
       */  
    /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
      this.queryForm.pageNo = val
      this.fetchData()
    },
    /**
       * @description 查询按钮，回到第一页，查询数据
       * @param {*}  
       * @return {*}
       */   
    queryData() {
      this.queryForm.pageNo = 1
      this.fetchData()
    },
     /**
       * @description 查询接口，条件查询
       * @param {*}  
       * @return {*}
       */   
    /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
      this.listLoading = true
      const {
        data: { list, total },
      } = await getList(this.queryForm)
      this.list = list
      this.total = total
      this.listLoading = false
    },
     /**
       * @description 新建按钮触发，唤起新建弹框
       * @param {*}  
       * @return {*}
       */    
    /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
      this.$refs['edit'].showEdit()
    },
    /**
       * @description 编辑按钮触发，唤起编辑弹框
       * @param {*}  
       * @return {*}
       */    
    handleEdit(row) {
      this.$refs['edit'].showEdit(row)
    },
    // 无意义
    sendModel() {
      this.$refs['sendModel'].showEdit()
    },
    // 无意义
    send() {
      this.$refs['send'].showEdit()
    },
  },
}
</script>
