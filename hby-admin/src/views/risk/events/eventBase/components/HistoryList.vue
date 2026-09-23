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
              <!-- <el-button
						type="text"
						@click="handleEdit(row)"
						:disabled="!!row.status"
					>
						修改
					</el-button> -->
              <el-button type="text" @click="handleExport(row)">导出</el-button>
              <!-- <el-dropdown style="margin-left: 10px">
						<el-button type="text">更多</el-button>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item>
								<el-button
									type="text"
									@click.native="handleManage(row)"
									:disabled="!row.status"
								>
									办理
								</el-button>
							</el-dropdown-item>
							<el-dropdown-item>
								<el-button
									type="text"
									@click="handleApproval(row)"
									:disabled="!!row.status"
								>
									提交审批
								</el-button>
							</el-dropdown-item>
							<el-dropdown-item>

							</el-dropdown-item>
							<el-dropdown-item>
								<el-button
									type="text"
									@click="handleReport(row)"
									:disabled="
										row.status == 6 && row.reportstatus != 1 ? false : true
									"
								>
									上报
								</el-button>
							</el-dropdown-item>
							<el-dropdown-item>
								<el-button
									type="text"
									@click="handleUpdate(row)"
									:disabled="row.reportstatus == 1 ? false : true"
								>
									更新
								</el-button>
							</el-dropdown-item>
							<el-dropdown-item>
								<el-button type="text" @click="handleHistory(row)">
									查看上报记录
								</el-button>
							</el-dropdown-item>
							<el-dropdown-item>
								<el-button
									type="text"
									@click="handleDelete(row)"
									:disabled="!!row.status"
								>
									删除
								</el-button>
							</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown> -->
            </template>
          </el-table-column>
        </el-table>

        <!-- <el-pagination
			background
			class="pager"
			:current-page="queryForm.pageNo"
			:layout="layout"
			:page-size="queryForm.pageSize"
			:total="list.length"
			@current-change="handleCurrentChange"
			@size-change="handleSizeChange"
		/> -->
      </el-card>
      <!-- <EventEdit ref="edit" @fetch-data="fetchData" /> -->
      <!-- <ProcessList ref="process" @fetchData="fetchData" />
	<WfqdDeal ref="wfqddeal" />
	<HandleHistory ref="historyList" /> -->
    </el-dialog>
    <EventRead ref="read" />
  </div>
</template>
<script>
  import {
    getRiskListById,
    deletFxsj,
    exportRiskEvent,
    reportToLeader,
    getViewHistoricalVersions,
  } from '@/api/risk/riskEvents'
  import { UTCformat } from '@/utils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import EventEdit from './EventEdit.vue'
  import EventRead from './EventRead.vue'
  import HandleHistory from './HistoryList.vue'

  export default {
    components: {
      EventEdit,
      EventRead,
      ProcessList,
      WfqdDeal,
      HandleHistory,
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
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(40, row.riseveid)
      },
      /**
       * @description: 办理
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.riseveid,
          tableId: 40,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.fetchData(row.riseveid)
      },
      async fetchData(id) {
        this.listLoading = true
        const { data } = await getViewHistoricalVersions({
          riseveid: id,
        })

        data.page = data.page.map((v) => {
          v.discovereddate = UTCformat(v.discovereddate)
          v.occureddate = UTCformat(v.occureddate)
          return v
        })
        this.list = data.page
        // page.records = page.records.map((v) => {
        //   v.discovereddate = UTCformat(v.discovereddate)
        //   v.occureddate = UTCformat(v.occureddate)
        //   return v
        // })

        // this.list = page.records
        // this.total = page.total
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
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        this.$refs['edit'].showEdit(row, this.riskcatid)
      },
      /**
       * @description: 打开详细
       * @return {*}
       */
      handleRead(row) {
        console.log(row)
        this.$refs['read'].showRead(row)
      },
      /**
       * @description: 数据删除
       * @return {*}
       */
      handleDelete(row) {
        console.log(row, '删除====')
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deletFxsj({ riseveid: row.riseveid })
          if (code !== 0) {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          }
          await this.fetchData()
        })
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
      //更新
      async handleUpdate(val) {
        this.$refs['edit'].showEdit(val, this.riskcatid, true)
      },
      //查看历史记录
      async handleUpdate(val) {
        this.$refs['historyList'].showEdit(val)
      },
      //上报
      async handleReport(val) {
        const { data, code, msg } = await reportToLeader({
          riseveid: val.riseveid,
        })
        if (code !== 0) {
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        }
        await this.fetchData()
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
