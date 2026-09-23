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
          v-model="queryForm.advicecoed"
          clearable
          placeholder="审计通知书编号"
        />
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
      <el-table-column align="center" label="审计通知审批编号" prop="adviceid">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="$refs['edit'].showEdit(row, true)"
            style="white-space: pre-line; line-height: 16px"
          >
            {{ row.adviceid }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="项目名称" prop="projectname" />
      <el-table-column
        align="center"
        label="审计项目实施部门"
        prop="department"
      />

      <el-table-column align="center" label="审计实施时间" prop="sjsstime">
        <template #default="{ row }">{{ formatDay(row.sjsstime) }}</template>
      </el-table-column>
      <el-table-column align="center" label="审计组成员">
        <el-table-column align="center" label="组长" prop="teamleader" />
        <el-table-column align="center" label="副组长" prop="fznames" />
        <el-table-column align="center" label="主审" prop="mainreviewer" />
        <el-table-column align="center" label="助审" prop="helpreviewer" />
      </el-table-column>
      <el-table-column align="center" label="经办人" prop="operator" />
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
    <NoticeaprEdit ref="edit" />
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { noticeaprSplist } from '@/oapi/audit/preparation'
  import NoticeaprEdit from '@/views/oilAudit/prepare/components/noticeaprEdit.vue'
  import { formatDay } from '@/utils/index'
  export default {
    components: {
      selectTeam: () =>
        import(
          '@/views/oilAudit/project/components/formComponents/selectTeam.vue'
        ),
      NoticeaprEdit,
    },
    props: {
      xctype: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        formatDay,
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          advicecoed: undefined,
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await noticeaprSplist({...this.queryForm, xctype: this.xctype })
        this.tableData = tlist || []
        this.total = totalRecord || 0
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
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
          advicecoed: undefined,
          pageNumber: 1,
          pageSize: 10,
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
