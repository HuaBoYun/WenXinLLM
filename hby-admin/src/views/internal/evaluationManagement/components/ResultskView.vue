<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
    append-to-body
  >
    <el-col :span="24">
      <el-divider>
        项目名称: {{ resultName }} &emsp;&emsp;&emsp;评价编号:
        {{ resultNumber }}
      </el-divider>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <!--      <el-table-column type="selection" width="55" />-->
      <el-table-column align="center" label="序号" type="index" />
      <el-table-column
        align="center"
        label="被评价对象"
        prop="orgname"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button
            style="color: red"
            type="text"
            @click="$refs['ResultsModel'].showEdit(row)"
          >
            {{ row.orgname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="评价负责人" prop="realname" />
      <el-table-column
        align="center"
        label="评价日期"
        prop="assessdate"
        #default="{ row }"
      >
        {{ row.assessdate ? row.assessdate.split(' ')[0] : '' }}
      </el-table-column>
      <el-table-column
        align="center"
        label="状态"
        prop="state"
        #default="{ row }"
      >
        {{ row.state === '4' ? '已计算' : '未计算' }}
      </el-table-column>
      <el-table-column align="center" label="总分" prop="finalscore">
        <template #default="{ row }">
          {{ row.finalscore ? row.finalscore.toFixed(2) : '' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="80"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="!(major > 0)"
          >
            计算
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
    <ResultsModel ref="ResultsModel" />
    <!--    <ScorekList ref="edit" />-->
  </el-drawer>
</template>

<script>
  import { projInitiate_TaskResult, calculate } from '@/api/internal/result'
  // import ScorekList from '@/views/internal/evaluationManagement/components/ScorekList'
  import ResultsModel from '@/views/internal/evaluationManagement/components/ResultsModel'
  export default {
    name: 'ResultskView',
    components: { ResultsModel },
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
          pageSize: 10,
        },
        resultName: '',
        resultNumber: '',
        title: '结果',
        dialogFormVisible: false,
        tableData: [],
        major: 0,
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
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
        this.listLoading = true
        const {
          data: {
            major,
            pageBean: { records, total },
          },
        } = await projInitiate_TaskResult(this.queryForm)
        this.major = major
        this.list = records
        this.total = total
        this.listLoading = false
      },
      showEdit(row) {
        // console.log(row)
        this.resultName = row.assessname
        this.resultNumber = row.assessid
        this.queryForm.assId = row.assid

        this.queryData()
        this.dialogFormVisible = true
      },
      close() {
        this.major = 0
        this.dialogFormVisible = false
      },
      async handleEdit(row) {
        const { msg, code } = await calculate({
          assId: row.assid,
          orgid: row.orgid,
        })
        if (code == 200) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.fetchData()
          this.$emit('fetch-data')
        } else {
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          this.fetchData()
        }
        // this.$refs['edit'].showEdit(row)
      },
    },
  }
</script>
<style scoped>
  .box_row {
    margin-bottom: 10px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
