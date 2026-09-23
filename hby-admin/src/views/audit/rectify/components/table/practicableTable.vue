<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1400px"
    @close="close"
  >
    <div>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @select="handleSelection"
      >
        <el-table-column align="center" label="问题编号" prop="issuesCode">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.issuesCode }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="问题名称"
          prop="issuesName"
        />
        <el-table-column
          align="center"
          label="整改方案"
          prop="rectificationPlan"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="预计完成时间"
          prop="deadline"
          show-overflow-tooltip
          :formatter="formatDate"
        />
      </el-table>
    </div>

    <practicableForm v-if="showPracticableForm" ref="practicableForm" @closeDialog="closeDialog" />
  </el-dialog>
</template>
<script>
  import { formatDay } from '@/utils/index'
  import { getRectificationPlanByReportType, getIssuesAllDetailInfo } from '@/api/zgzz/index.js'
  import practicableForm from '@/views/audit/rectify/components/form/practicableForm.vue'

  export default {
    name: 'ExecutorOptions',
    props: {
      multiple: {
        type: Boolean,
        default: true,
      },
    },
    components: { practicableForm },
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '整改落实列表',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          reporttype: '',
          planIdStrs: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: '',
        multipleSelection: [],
        showPracticableForm: false,
      }
    },
    created() {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      show(options) {
        this.list = options.list
        this.dialogFormVisible = true
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getRectificationPlanByReportType(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async handleSizeChange(val) {
        this.queryForm.pageSize = val
        await this.fetchData()
        this.setSelection(this.multipleSelection)
      },
      async handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        await this.fetchData()
        this.setSelection(this.multipleSelection)
      },
      confirm() {
        if (!this.multipleSelection || !this.multipleSelection.length) {
          this.$baseMessage('请选择数据！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.planId == item.planId
              }),
              true
            )
          })
        })
      },
      handleSelection(val, row) {
        this.current = val
        if (!this.multiple && val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }

        if (this.multiple) {          
          const i = this.multipleSelection.findIndex(x => x.planId == row.planId)
          if (i < 0) {
            this.multipleSelection.push(row)
          } else {
            this.multipleSelection.splice(i, 1)
          }
          this.userId = this.multipleSelection.map((item) => item.planId)
        } else {
          this.multipleSelection = val
        }
      },
      async handleDetail(row) {
        this.showPracticableForm = true
        this.$nextTick(async () => {
          if (row.relaId) {
            const res = await getIssuesAllDetailInfo({ relaId: row.relaId })
            if (res && res.data) {
              this.$refs.practicableForm.showEdit('detail', res.data)
            }
          }
        })
      },
      closeDialog() {
        this.showPracticableForm = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 20%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 80%;
  }
</style>
