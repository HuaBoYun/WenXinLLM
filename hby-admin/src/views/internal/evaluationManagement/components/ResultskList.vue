<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-col :span="24">
      <el-divider>
        项目名称: {{ resultName }} &emsp;&emsp;&emsp;评价编号:
        {{ resultNumber }}
      </el-divider>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <!--      <el-table-column type="selection" width="55" />-->
      <el-table-column
        align="center"
        label="序号"
        type="index"
      ></el-table-column>
      <el-table-column align="center" label="被评价对象" prop="orgname" />
      <el-table-column align="center" label="评价负责人" prop="realname" />
      <el-table-column
        align="center"
        label="状态"
        prop="assstatus"
        #default="{ row }"
      >
        {{ row.assstatus && row.assstatus === '4' ? '已处理' : '未计算' }}
      </el-table-column>
      <el-table-column align="center" label="初步评分" prop="finalscore" />
      <el-table-column align="center" label="初步级别" prop="finallevel" />
      <el-table-column align="center" label="校正级别" prop="checklevel">
        <template #default="{ row }">
          {{ row.checklevel }}
          <!-- {{
            !row.checklevel
              ? ''
              : row.checklevel == 1
              ? '成熟级'
              : row.checklevel == 2
              ? '规范级'
              : '受控级'
          }} -->
        </template>
      </el-table-column>
      <el-table-column align="center" label="校正原因" prop="checkreason" />
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
            评级校正
          </el-button>
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
    <ResultsRevise ref="edit" @fetch-data="fetchData" />
  </el-drawer>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import { projDispGd } from '@/api/internal/result'
  import ResultsRevise from '@/views/internal/evaluationManagement/components/ResultsRevise'
  export default {
    name: 'ResultskList',
    components: { ResultsRevise },
    data() {
      return {
        assesslevels: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          assId: '',
          pageNumber: 1,
          pageSize: 10,
        },
        resultName: '',
        resultNumber: '',
        title: '评价结果 - 评价',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
        modalType: 'view',
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
            assesslevels,
            pageBean: { records, total },
            project = {},
            major,
          },
        } = await projDispGd(this.queryForm)
        this.list = records.map((x) => {
          x.assstatus = project.assstatus || ''
          return x
        })
        this.major = major

        this.assesslevels = assesslevels
        // console.log(this.assesslevels, 'this.assesslevels')
        this.total = total
        this.listLoading = false
        this.$emit('fetch-data')
      },
      showEdit(row, type) {
        // console.log(row)
        this.resultName = row.assessname
        this.resultNumber = row.assessid
        this.queryForm.assId = row.assid
        this.modalType = type
        this.queryData()
        this.dialogFormVisible = true
      },
      close() {
        this.major = 0
        this.dialogFormVisible = false
      },
      handleEdit(row) {
        const assesslevels = this.assesslevels
        this.$refs['edit'].showEdit(row, assesslevels)
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
