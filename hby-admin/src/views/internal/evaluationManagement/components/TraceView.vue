<template>
  <el-drawer
    :before-close="close"
    size="1000px"
    :title="title"
    :visible.sync="dialogFormVisible"
  >
    <el-col :span="24">
      <el-divider>项目名称: {{ project.assessname || '' }} &emsp;&emsp;&emsp;评价编号: {{ project.assessid || '' }}</el-divider>
    </el-col>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column type="index" width="50" label="序号"></el-table-column>
      <!-- <el-table-column align="center" label="序号" prop="data" /> -->
      <el-table-column align="center" label="	评价对象" prop="orgname">
        <template #default="{ row }">
          <el-button
            style="color: red"
            type="text"
            @click="$refs['TrackList'].showEdit(row)"
          >
            {{ row.orgname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="	评价负责人" prop="realname" />
      <el-table-column
        align="center"
        label="启动时间"
        prop="assstartday"
        show-overflow-tooltip
      >
        {{ assstartday }}
      </el-table-column>
      <el-table-column
        align="center"
        label="状态"
        prop="state"
        show-overflow-tooltip
        width="80"
      >
        <template #default="{ row }">
          {{
            row.state == 1
              ? '已立项'
              : row.state == 2
              ? '已启动'
              : row.state == 3
              ? '已处理'
              : '已完成'
          }}
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
    <TrackList ref="TrackList" />
  </el-drawer>
</template>

<script>
  import TrackList from '@/views/internal/evaluationManagement/components/TrackList'
  import { getProjTask } from '@/api/internal/trace'

  export default {
    name: 'TraceView',
    components: { TrackList },
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
        title: '跟踪',
        dialogFormVisible: false,
        tableData: [{ name: 'XXXXX' }, { name: 'XXXXX' }, { name: 'XXXXX' }],
        assstartday: '',
        project: {}
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
            pageBean: { records: list, total },
            project = {},
          },
        } = await getProjTask(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
        this.project = project
        this.assstartday = project.assstartday
          ? project.assstartday.split(' ')[0]
          : ''
      },
      showEdit(row) {
        console.log('row', row)
        this.queryForm.selectedPlans = row.assid
        this.fetchData()
        this.dialogFormVisible = true
      },
      close() {
        this.project = {}
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped></style>
