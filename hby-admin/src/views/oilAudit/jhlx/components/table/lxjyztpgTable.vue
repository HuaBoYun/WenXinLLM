<template>
  <el-dialog
    title="计划名称"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <vab-query-form-top-panel>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item>
          <el-input
            v-model="queryForm.projectName"
            clearable
            placeholder="审计项目名称"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.projectPurpose"
            clearable
            placeholder="立项理由及审计目的"
          />
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
    </vab-query-form-top-panel>
    <el-table
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="审计项目名称"
        prop="projectName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="projectPurpose"
        label="立项理由及审计目的"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="projectType"
        label="类型" 
      ></el-table-column>
      <el-table-column
        prop="departmentName"
        label="部门"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column prop="time" label="时间" show-overflow-tooltip>
        <template #default="scope">
          {{ scope.row.timeRangel }} - {{ scope.row.timeRangeR }}
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
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
import { UTCformat } from '@/utils'
import { setLxZxsjHzShowList, getLxZxsjHzChooseList } from '@/oapi/audit/plan'
export default {
  data() {
    return {
      dialogVisible: false,
      tableData: [],
      queryForm: {
        projectName: '',
        projectPurpose: '',
        pageNumber: 1,
        pageSize: 20,
      },
      multipleSelection: [],
      list: [],
      total: 0,
      layout: 'total, sizes, prev, pager, next, jumper',
      select: [],
    }
  },
  methods: {
    async showEdit(row) {
      this.fetchData()
      this.dialogVisible = true
    },
    async fetchData() {
      this.listLoading = true
      const {
        data: { tlist, totalRecord },
        code,
      } = await getLxZxsjHzChooseList(this.queryForm)
      if (code === 1) {
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
        this.setCheckedRows()
      }
    },
    resetQueryForm() {
      // this.queryForm = this.$options.data().queryForm;
      this.queryForm = {
        projectName: '',
        projectPurpose: '',
        pageNumber: 1,
        pageSize: 20,
      }
    },
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
    },
    handleSelection(val, row) {
      const i = this.select.findIndex((x) => x.id == row.id)
      if (i < 0) {
        this.select.push(row)
      } else {
        this.select.splice(i, 1)
      }
    },
    handleSelectAll(val) {
      const curSelected = val.filter((x) => !!x)
      if (curSelected && curSelected.length) {
        curSelected.map((row) => {
          if (row && !this.select.some((x) => x.id == row.id)) {
            this.select.push(row)
          }
        })
      } else {
        this.list.map((row) => {
          const i = this.select.findIndex((x) => x.id == row.id)
          if (i >= 0) {
            this.select.splice(i, 1)
          }
        })
      }
    },

    // 翻页的时候回显已勾选的数据
    setCheckedRows() {
      this.$nextTick(() => {
        this.select.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(
            this.list.find((item) => {
              return row.id == item.id
            }),
            true
          )
        })
      })
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    save() {
      if (this.select && this.select.length > 0) {
        const ids = this.select.map((res) => res.id)
        //下发保存
        setLxZxsjHzShowList({
          idStrs: ids.toString(),
        }).then((res) => {
          if (res.code == '1') {
            this.$baseMessage(res.msg, 'success')
            this.$emit('fetchData')
            this.select = []
          }
        })
        this.dialogVisible = false
      } else {
        this.$baseMessage('请选择', 'error', 'vab-hey-message-error')
      }
    },
  },
}
</script>
<style scoped lang="scss">
//隐藏表头全选框
::v-deep thead {
  .el-table-column--selection {
    .el-checkbox__inner {
      display: none !important;
    }
  }
}
</style>
