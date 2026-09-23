<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-col :span="24">
      <el-divider>
        项目名称: {{ scoreName }}&emsp;&emsp;&emsp;评价编号: {{ scoreNumber }}
      </el-divider>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <!--      <el-table-column type="selection" width="55" />-->
      <!-- <el-table-column align="center" label="序号" prop="data" /> -->
      <el-table-column
        align="center"
        label="序号"
        type="index"
      ></el-table-column>
      <el-table-column align="center" label="	被评价对象" prop="orgname" />
      <el-table-column align="center" label="	评价负责人" prop="markrealname" />
      <el-table-column
        align="center"
        label="评价日期"
        prop="assdatetime"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
        width="80"
      >
        <template #default="{ row }">
          {{
            row.status == 0
              ? '未处理'
              : row.status == 1
              ? '已保存'
              : row.status == 2
              ? '已完成'
              : '未知'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="80"
        v-if="modalType === 'edit'"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">评价</el-button>
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
    <!-- <TrackList ref="TrackList" /> -->
    <ScorekList ref="edit" @fetch-data="fetchData" />
  </el-drawer>
</template>

<script>
  // import { getList } from '@/api/systemLog'
  import { getProjPingfenList } from '@/api/internal/score'
  import ScorekList from '@/views/internal/evaluationManagement/components/ScorekList'
  // import TrackList from '@/views/internal/evaluationManagement/components/TrackList'
  export default {
    name: 'ScoreView',
    components: { ScorekList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          selectedPlans: '',
          pageNumber: 1,
          pageSize: 10,
        },
        scoreName: '',
        scoreNumber: '',
        project: '',
        title: '评价发起',
        dialogFormVisible: false,
        modalType: 'view',
        // tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
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
            pageBean: { records, total },
            project,
          },
        } = await getProjPingfenList(this.queryForm)
        this.list = records
        this.total = total
        this.project = project
        this.listLoading = false
      },
      showEdit(row, type) {
        this.scoreName = row.assessname
        this.scoreNumber = row.assessid
        this.queryForm.selectedPlans = row.assid
        this.modalType = type
        this.queryData()
        this.dialogFormVisible = true
      },
      close() {
        this.$emit('fetch')
        this.dialogFormVisible = false
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, this.project)
      },
    },
  }
</script>
<style scoped></style>
