<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    destroy-on-close
  >
    <div style="text-align: right; margin-bottom: 5px">
      <el-button type="primary" @click="addRule">选 定</el-button>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        label="全部"
        width="55"
      ></el-table-column>
      <el-table-column label="规则ID" prop="ruleid"></el-table-column>
      <el-table-column prop="rulename" label="规则名称"></el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { RuleslistSelector, addRule } from '@/api/monitor/rule/index'
  export default {
    name: 'Zbtj',
    data() {
      return {
        dialogFormVisible: false,
        title: '规则',
        tableData: [],
        multipleSelection: [],
        solutionid: '',
      }
    },
    methods: {
      async showEdit(row) {
        this.solutionid = row.solutionid
        this.dialogFormVisible = true
        const params = {
          orgId: row.orgid,
          pageNumber: 1,
          pageSize: 20,
          solutionid: row.solutionid,
        }
        const { data } = await RuleslistSelector(params)
        // RuleslistSelector(params).then((res) => {
        this.tableData = data.pageBean.records
        // })
      },
      async addRule() {
        for (var i = 0; i < this.multipleSelection.length; i++) {
          const { data } = await addRule({
            ruleid: this.multipleSelection[i].ruleid,
            souceid: this.solutionid,
          })
        }
        this.$emit('showRuleTable', this.multipleSelection)
        this.close()
      },
      close() {
        this.dialogFormVisible = false
      },
      toggleSelection(rows) {
        if (rows) {
          rows.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(row)
          })
        } else {
          this.$refs.multipleTable.clearSelection()
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
    },
  }
</script>
