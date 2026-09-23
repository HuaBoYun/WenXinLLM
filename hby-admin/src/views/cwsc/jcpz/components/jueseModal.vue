<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    title="角色选择"
    width="50%"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
      <vab-query-form-left-panel :span="24">
        <el-input
          v-model="queryForm.rname"
          placeholder="请输入名称"
          style="width: 200px; margin-right: 10px; display: inline-block"
        />
        <el-button icon="el-icon-search" type="primary" @click="fetchData">
          查询
        </el-button>
        <el-button type="primary" @click="resetSearch">重 置</el-button>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      :data="tableData"
      style="width: 100%"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
    >
      <el-table-column
        type="selection"
        width="55"
        :selectable="handleSelectable"
      ></el-table-column>
      <el-table-column
        align="center"
        label="编号"
        prop="rid"
        sortable="custom"
      />
      <el-table-column
        align="center"
        label="名称"
        prop="rname"
        sortable="custom"
      />
      <el-table-column
        align="center"
        label="描述"
        prop="rdesc"
        sortable="custom"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="rstatus"
        sortable="custom"
      >
        <template #default="{ row }">
          {{ row.rstatus == 1 ? '启用' : '禁用' }}
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
  </el-dialog>
</template>

<script>
  import { roleList } from '@/api/setting/auth'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        current: [],
        select: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          rname: '',
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        list: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.fetchData()
      },
      async fetchData() {
        const {
          data: { tlist, totalRecord },
        } = await roleList(this.queryForm)
        this.tableData = tlist
        this.total = totalRecord
        this.setCheckedRows()
      },
      close() {
        this.dialogVisible = false
        this.tableData = []
      },
      handleSelectable(row) {
        // 通过是否存在 childrenList 判断是否为一级节点
        // 如果存在子节点（一级节点），则禁用勾选
        return !row.childrenList
      },

      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择角色！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('roleSelect', this.select)
        this.dialogVisible = false
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
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
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.rid == row.rid)
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
            if (row && !this.select.some((x) => x.rid == row.rid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.rid == row.rid)
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
              this.tableData.find((item) => {
                return row.rid == item.rid
              }),
              true
            )
          })
        })
      },
    },
  }
</script>

<style scoped lang="scss"></style>
