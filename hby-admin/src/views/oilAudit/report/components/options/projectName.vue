<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="审计项目"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form
      ref="form"
      checkable
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input
          v-model="queryForm.projectName"
          clearable
          placeholder="项目名称"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          native-type="submit"
          type="primary"
          @click="getData"
        >
          查询
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch()">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="编号" prop="qdcode" />
      <el-table-column align="center" label="审计项目名称" prop="projectName" />
      <el-table-column align="center" label="计划名称" prop="planName" />
      <el-table-column align="center" label="主审" prop="zsname" />
      <el-table-column
        align="center"
        label="项目负责人"
        prop="projectOrderName"
      />
    </el-table>
    <template slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary">确定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { implementPlanList } from '@/oapi/audit/project'
  export default {
    props: {
      xctype: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    mounted() {},
    methods: {
      reset() {
        this.queryForm.projectName = ''
        this.getData()
      },
      show() {
        this.dialogFormVisible = true
        this.getData()
      },
      resetSearch() {
        this.queryForm = {
          projectName: '',
          pageNumber: 1,
          pageSize: 9999,
        }
        this.getData()
      },
      async getData() {
        const {
          data: { tlist },
        } = await implementPlanList({
          pageNumber: 1,
          pageSize: 9999,
          projectName: this.queryForm.projectName,
          xctype: this.xctype,
        })
        this.tableData = tlist
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        this.$emit('selected', this.multipleSelection)
        this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped></style>
