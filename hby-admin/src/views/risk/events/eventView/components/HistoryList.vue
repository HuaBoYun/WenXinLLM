<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      title="上报记录"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
    >
      <el-card>
        <el-table v-loading="listLoading" :data="list">
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
          <el-table-column align="center" label="版本号" prop="version" />

          <el-table-column
            align="center"
            label="事件名称"
            prop="riskeventname"
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
          <el-table-column align="center" label="操作">
            <template slot-scope="{ row }">
              <el-button type="text" @click="handleExport(row)">导出</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-dialog>
    <EventRead ref="read" />
  </div>
</template>
<script>
  import {
    exportRiskEvent,
    getViewHistoricalVersionsMain,
  } from '@/api/risk/riskEvents'
  import { UTCformat } from '@/utils'
  import EventRead from '@/views/risk/events/eventBase/components/EventRead.vue'

  export default {
    components: {
      EventRead,
    },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        dialogFormVisible: false,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
        },
        riskcatid: undefined,
      }
    },
    mounted() {},
    methods: {
      showEdit(row) {
        this.dialogFormVisible = true
        this.fetchData(row.riseveid)
      },
      async fetchData(id) {
        this.listLoading = true
        const { data } = await getViewHistoricalVersionsMain({
          riseveid: id,
        })

        data.page = data.page.map((v) => {
          v.discovereddate = UTCformat(v.discovereddate)
          v.occureddate = UTCformat(v.occureddate)
          return v
        })
        this.list = data.page
        this.listLoading = false
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        console.log('handleSizeChange')
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleCurrentChange(val) {
        console.log('handleCurrentChange')
        this.queryForm.pageNo = val
        this.fetchData()
      },
      /**
       * @description: 打开详细
       * @return {*}
       */
      handleRead(row) {
        console.log(row)
        this.$refs['read'].showRead(row)
      },
      //导出
      async handleExport(val) {
        const data = await exportRiskEvent({
          riseveid: val.riseveid,
        })
        let fileName = '风险事件报告'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },

      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
        }
      },
    },
  }
</script>
<style lang="scss" scoped></style>
