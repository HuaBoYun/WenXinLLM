<template>
  <el-dialog
    title="模板"
    :visible.sync="dialogVisible"
    :modal-append-to-body="false"
    :append-to-body="true"
    width="50%"
    :close-on-click-modal="false"
  >
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="模板名称"
        width="120"
        prop="templateName"
      ></el-table-column>
      <el-table-column label="关联审计类型" align="center">
        <template slot-scope="{ row }">
          {{ row.auditTypeList?.map((el) => el.typeName).join(',') }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        show-overflow-tooltip
      ></el-table-column>
      <!-- <el-table-column
        prop="externalassig"
        label="是否外委"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.externalassig == 0 ? '否' : '是' }}
        </template>
      </el-table-column> -->
    </el-table>
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
      }
    },
    methods: {
      async showEdit(resPlanProjectArr) {
        if (resPlanProjectArr) {
          resPlanProjectArr.map((v) => {
            v.finishtime = UTCformat(v.finishtime)
          })
        }
        this.tableData = resPlanProjectArr
        this.dialogVisible = true
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      save() {
        this.$emit('projectList', this.multipleSelection)
        this.dialogVisible = false
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
