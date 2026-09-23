<template>
  <el-dialog
    title="项目"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
    @close="close"
  >
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
          placeholder="项目名称"
        />
      </el-form-item>
      <el-form-item>
        <el-input
          v-model="queryForm.projectOrderName"
          clearable
          placeholder="项目经理"
          :style="{ width: '215px' }"
          disabled
        />

        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="projectManager"
        >
          选择
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button
          icon="el-icon-search"
          native-type="submit"
          type="primary"
          @click="showEdit"
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
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="项目编号"
        width="120"
        prop="qdcode"
      ></el-table-column>
      <el-table-column
        label="项目名称"
        width="120"
        prop="projectName"
      ></el-table-column>
      <el-table-column
        prop="sjlxName"
        label="审计类型"
        width="120"
      ></el-table-column>
      <el-table-column
        prop="planStarttime"
        label="计划开始时间"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="planEndtime"
        label="计划结束时间"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="projectOrderName"
        label="项目经理"
        show-overflow-tooltip
      ></el-table-column>
      <!-- <el-table-column align="center" label="状态" prop="status">
        <template #default="{ row }">
          {{ row.status == 1 ? '启动' : '未启动' }}
        </template>
      </el-table-column> -->
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
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <!-- 人员 -->
    <project-manage @projectManage="getChildlistPro" ref="manage" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { planingList } from '@/oapi/audit/project'
  import projectManage from '@/components/danxuanPerson.vue'
  export default {
    components: {
      projectManage,
    },
    props: {
      xctype: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 10,
          spzt: 6,
          projectOrderName: undefined,
          projectOrderId: undefined,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
      }
    },
    methods: {
      /**
       * @description  初始化
       * @param {*}
       * @return {*}
       */
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit() {
        this.dialogVisible = true
        let { ...other } = this.queryForm
        let planStarttime = ''
        let planEndtime = ''
        const {
          data: { tlist, totalRecord },
        } = await planingList({ ...other, planStarttime, planEndtime, xctype: this.xctype })
        this.tableData = tlist || []
        this.total = totalRecord || 0
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
        this.close()
      },
      close() {
        this.resetQueryForm()
        this.dialogVisible = false
      },
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      getChildlistPro(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.queryForm.projectOrderName = names
        this.queryForm.projectOrderId = ids
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.showEdit()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.showEdit()
      },
      resetQueryForm() {
        this.queryForm = {
          projectOrderName: undefined,
          projectOrderId: undefined,
          pageNumber: 1,
          pageSize: 10,
          spzt: 6,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.showEdit()
      },
    },
  }
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
