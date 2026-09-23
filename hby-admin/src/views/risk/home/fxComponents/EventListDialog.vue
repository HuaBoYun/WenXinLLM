<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="title"
    width="1200px"
    :close-on-click-modal="false"
    append-to-body
    @close="handleClose"
  >
    <el-form
      :inline="true"
      :model="queryForm"
      size="mini"
      style="margin-bottom: 10px"
    >
      <el-form-item>
        <el-input v-model="queryForm.code" clearable placeholder="事件编号" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="queryForm.name" clearable placeholder="事件名称" />
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="queryForm.losseventcategory"
          placeholder="损失事件定性类别"
          clearable
        >
          <el-option label="一般事件" value="1" />
          <el-option label="重大事件" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryData">查询</el-button>
        <el-button @click="resetQueryForm">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%"
      max-height="800"
    >
      <el-table-column
        align="center"
        label="事件编号"
        prop="riskeventcode"
        width="140"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleRead(row)">
            {{ row.riskeventcode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="版本号"
        prop="version"
        width="80"
      />
      <el-table-column
        align="center"
        label="事件名称"
        prop="riskeventname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="公司名称"
        prop="unitname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="风险类型"
        prop="riskcatname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="发生部门"
        prop="occureddepartment"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="发生日期" prop="occureddate" />
      <el-table-column
        align="center"
        label="损失事件定性类别"
        prop="losseventcategory"
        show-overflow-tooltip
      >
        <template slot-scope="{ row }">
          <span>
            {{ row.losseventcategory == '1' ? '一般事件' : '重大事件' }}
          </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="审批状态" prop="status">
        <template #default="{ row }">
          {{
            row.status == 1
              ? '审批中'
              : row.status == 2
              ? '需调整'
              : row.status == 3
              ? '已撤销'
              : row.status == 4
              ? '已终止'
              : row.status == 5
              ? '已跟踪'
              : row.status == 6
              ? '已完成'
              : '未审批'
          }}
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      style="margin-top: 10px; text-align: right"
      :current-page="queryForm.pageNo"
      layout="total, sizes, prev, pager, next, jumper"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <EventRead ref="read" />
  </el-dialog>
</template>

<script>
  import { disposalmanageMain } from '@/api/risk/riskEvents'
  import { UTCformat } from '@/utils'
  import EventRead from '@/views/risk/events/eventBase/components/EventRead.vue'

  export default {
    name: 'EventListDialog',
    components: {
      EventRead,
    },
    data() {
      return {
        dialogVisible: false,
        title: '事件列表',
        list: [],
        listLoading: false,
        total: 0,
        queryForm: {
          code: '',
          name: '',
          losseventcategory: '',
          companyname: '',
          pageNo: 1,
          pageSize: 10,
        },
      }
    },
    methods: {
      show(params = {}) {
        this.dialogVisible = true
        // 支持传入筛选参数
        if (params.losseventcategory) {
          this.queryForm.losseventcategory = params.losseventcategory
          const categoryName =
            params.losseventcategory == '1' ? '一般事件' : '重大事件'
          this.title = `事件列表 - ${categoryName}`
        }
        if (params.companyname) {
          this.queryForm.companyname = params.companyname
        }
        this.fetchData()
      },
      handleClose() {
        this.resetQueryForm()
        this.list = []
        this.title = '事件列表'
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        try {
          const {
            data: { page },
          } = await disposalmanageMain(this.queryForm)

          page.list = page.list.map((v) => {
            v.discovereddate = UTCformat(v.discovereddate)
            v.occureddate = UTCformat(v.occureddate)
            return v
          })

          this.list = page.list
          this.total = page.total
        } catch (error) {
          console.error('获取事件数据失败:', error)
        } finally {
          this.listLoading = false
        }
      },
      resetQueryForm() {
        this.queryForm = {
          code: '',
          name: '',
          losseventcategory: '',
          companyname: '',
          pageNo: 1,
          pageSize: 20,
        }
      },
      handleRead(row) {
        this.$refs['read'].showRead(row)
      },
    },
  }
</script>
