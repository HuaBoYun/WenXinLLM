<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1200px"
    @close="close"
  >
    <div class="lr-layout">
      <div class="right">
        <vab-query-form>
          <vab-query-form-right-panel :span="24">
            <el-button @click="close">取 消</el-button>
            <el-button type="primary" @click="confirm">确 定</el-button>
          </vab-query-form-right-panel>
        </vab-query-form>
        <el-table
          v-loading="listLoading"
          :data="list"
          highlight-current-row
          @current-change="handleSelected"
        >
          <el-table-column
            align="center"
            label="在报告中的对应编号"
            prop="reportnum"
          ></el-table-column>
          <el-table-column
            align="center"
            label="问题所属单位名称"
            prop="field1"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="报告中的问题定性"
            prop="wtdx"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="问题金额（元）"
            prop="wtje"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="定性（定性词典）"
            prop="dx"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="定性法规依据"
            prop="dxfgyj"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="处理意见或整改建议 "
            prop="clyj"
          ></el-table-column>
          <el-table-column
            align="center"
            label="整改时限"
            prop="zgsx"
          ></el-table-column>
          <el-table-column
            align="center"
            label="整改督促牵头部门或单位"
            prop="zgqtbm"
          ></el-table-column>
          <el-table-column
            align="center"
            label="整改责任人"
            prop="zrr"
          ></el-table-column>
          <el-table-column align="center" label="整改状态" prop="zgzt">
            <template #default="{ row }">
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
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="直接经济成果类型"
            prop="dqzjjjcgtype"
          ></el-table-column>
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
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import { getList } from '@/oapi/yqns_sjzg/wtzg'
  export default {
    name: 'ExecutorOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        data: [],
        queryForm: {
          pid: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
      }
    },
    created() {},
    methods: {
      show() {
        this.current = undefined
        this.dialogFormVisible = true
        this.getWtzgList()
      },
      async getWtzgList() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await getList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getWtzgList()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getWtzgList()
      },
      handleSelected(val) {
        this.current = val
      },
      confirm() {
        if (!this.current) {
          this.$baseMessage(
            '请选择问题编号！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style scoped>
  .lr-layout > .right {
    width: 100%;
  }
</style>
