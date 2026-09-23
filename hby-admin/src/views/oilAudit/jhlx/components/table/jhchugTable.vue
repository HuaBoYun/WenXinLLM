<template>
  <el-dialog
    title="计划名称"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="审计项目名称"
        width="120"
        prop="sjxmmc"
      ></el-table-column>
      <el-table-column
        prop="bsjdwmc"
        label="被审计单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        label="更新时间"
        width="120"
        prop="gxsj"
      ></el-table-column>
      <el-table-column
        prop="cjr"
        label="编制人"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="cjsj"
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
  import { jhgljhchugList, detailJhCg2 } from '@/api/monitor/question'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
      }
    },
    methods: {
      async showEdit(row) {
        const res = await jhgljhchugList({ spzt: 6 })
        // console.log(res)
        // this.tableData = res.data.tlist
        this.$set(this, `tableData`, res.data.tlist)
        this.dialogVisible = true
        setTimeout(() => {
          if (row) {
            let check = res.data.tlist.find((v) => v.id == row.id)
            console.log(check)
            this.$refs.multipleTable.toggleRowSelection(check, true)
          }
        }, 300)
      },
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async save() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择一条数据')
          return
        }
        const res = await detailJhCg2({
          jhcgid: this.multipleSelection[0].jhchugid,
        })
        this.$emit('fetch-table', res.data)
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
