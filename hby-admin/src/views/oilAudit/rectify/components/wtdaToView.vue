<template>
  <el-dialog
    title="填报"
    :visible.sync="dialogVisible"
    width="80%"
    append-to-body
    :close-on-click-modal="false"
    @close="close"
  >
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
    >
      <el-table-column align="center" label="项目名称" prop="projectName">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">
            {{ row.projectName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="当期直接经济成果类型"
        prop="dqzjjjcgtype"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="整改状态" prop="zgzt">
        <template #default="{ row }">
          <div v-if="row.version != 1">
            {{
              row.dqzgzt == 3
                ? '整改完毕'
                : row.dqzgzt == 2
                ? '正在整改'
                : row.dqzgzt == 1
                ? '无法整改'
                : ''
            }}
          </div>
          <div v-else-if="row.version == 1">
            {{
              row.zgzt == 3
                ? '整改完毕'
                : row.zgzt == 2
                ? '正在整改'
                : row.zgzt == 1
                ? '尚未开始整改'
                : row.zgzt == 0
                ? '不接受审计意见'
                : ''
            }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="整改时间"
        prop="cjsj"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row, true)">详情</el-button>
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
    <!-- <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>  -->
    <!-- <jjzrsjjgbgView ref="table7View" /> -->
    <gzhfEdit ref="edit" @fetch-data="fetchData" />
    <wtzgEdit ref="hxzgEdit" @fetch-data="fetchData" />
    <!-- <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" /> -->
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { getHistoryVersion } from '@/oapi/audit/report'
  import { fillInList } from '@/oapi/yqns_sjzg/hxzg'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import gzhfEdit from './gzhfEdit'
  import wtzgEdit from '@/views/oilAudit/rectify/components/gzhf/wtzgEdit.vue'

  export default {
    components: {
      gzhfEdit,
      ProcessList,
      WfqdDeal,
      wtzgEdit,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          issueNumber: '',
          unitName: '',
          dqzgzt: '',
          wtzgfl: '',
          pageNumber: 1,
          pageSize: 20,
        },
        Options: [
          {
            value: '1',
            label: '无法整改',
          },
          {
            value: '2',
            label: '正在整改',
          },
          {
            value: '3',
            label: '整改完毕',
          },
        ],
      }
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      async showEdit(row) {
        this.projectId = row.id
        this.fetchData()
        this.dialogVisible = true
      },
      async fetchData() {
        this.listLoading = true
        const { data } = await getHistoryVersion({
          ...this.queryForm,
          issuesId: this.projectId,
        })
        this.list = data || []
        this.listLoading = false
      },
      async handleEdit(row, disabled) {
        if (row.version == 1) {
          this.$refs['edit'].showEdit(row, disabled)
        } else {
          this.$refs['hxzgEdit'].showEdit(row, disabled)
        }
      },
      close() {
        this.dialogVisible = false
      },
    },
  }
</script>
<style scoped lang="scss">
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
