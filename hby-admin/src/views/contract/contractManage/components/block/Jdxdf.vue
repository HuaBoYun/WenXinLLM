<template>
  <el-dialog
    title="选择人员"
    :visible.sync="dialogVisible"
    width="1000px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <vab-query-form>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @selection-change="handleSelection"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="相对方类型"
          width="100"
          prop="budgettype"
        ></el-table-column>
        <el-table-column align="center" label="相对方名称" prop="budgetname" />
      </el-table>
    </div>
  </el-dialog>
</template>
<script>
  export default {
    components: {},
    name: 'xxx',
    data() {
      return {
        listLoading: false,
        list: [],
        dialogVisible: false,
        multipleSelection: [],
        current: undefined,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.list = row
        this.dialogVisible = true
      },
      //保存前置校验
      save() {
        if (!this.current) {
          this.$baseMessage('请选择相对方', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      //回调
      handleSelection(val) {
        this.current = val
        this.multipleSelection = val
      },
    },
  }
</script>
<style scoped lang="scss">
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
