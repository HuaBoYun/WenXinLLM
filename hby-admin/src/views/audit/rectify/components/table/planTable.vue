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
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >  
        <el-form-item>
          <el-input
            v-model="queryForm.planCode"
            clearable
            placeholder="方案编号"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.planName"
            clearable
            placeholder="方案名称"
          />
        </el-form-item>
        <el-form-item>
          <el-select
            v-model="queryForm.planType"
            placeholder="请选择方案类别"
            clearable
          >
            <el-option label="审计" value="1" />
            <el-option label="内控" value="2" />
            <el-option label="非系统实施" value="3" />
            <el-option label="外部审计" value="4" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="fetchData"
          >
            查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button native-type="submit" type="primary" @click="resetSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="confirm">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        v-loading="listLoading"
        :data="list"
        highlight-current-row
        @select="handleSelection"
        @select-all="handleAllSelection"
      >
          <el-table-column type="selection" width="55" v-if="multiple"></el-table-column>
          <el-table-column align="center" label="方案编号" prop="planCode" />
          <el-table-column align="center" label="方案名称" prop="planName" />
          <el-table-column
            align="center"
            label="方案类别"
            prop="planType"
            #default="{ row }"
          >
            {{ ['审计', '内控', '非系统实施', '外部审计'][Number(row.planType) - 1] }}
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
    </div>
  </el-dialog>
</template>
<script>
  import { formatDay } from '@/utils/index'
  import { getRectificationPlanByReportType } from '@/api/zgzz/index.js'
  export default {
    name: 'ExecutorOptions',
    props: {
      multiple: {
        type: Boolean,
        default: true,
      },
    },
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
          planCode: '',
          planName: '',
          planType: '',
          reporttype: '',
          planIdStrs: '',
          pageNumber: 1,
          pageSize: 20,
        },
        current: '',
        multipleSelection: [],
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
        this.queryForm.reporttype = options.type
        Object.assign(this.queryForm, options)
        this.current = ''
        this.multipleSelection = []
        this.dialogFormVisible = true
        this.list = []
        this.total = 0
        this.fetchData()
        
      },
      resetSearch() {
        this.queryForm.planCode = ''
        this.queryForm.planName = ''
        this.queryForm.planType = ''
        this.queryForm.pageNumber = 1
        this.queryForm.pageSize = 20
        this.fetchData()
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
      handleAllSelection(selection) {
        (selection || []).forEach(row => {
          this.handleSelection(row, row)
        })
      }
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
