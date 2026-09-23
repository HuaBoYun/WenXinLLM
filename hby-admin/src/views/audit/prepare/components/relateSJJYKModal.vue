<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input v-model="queryForm.code" clearable placeholder="编号" />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.tatle" clearable placeholder="标题" />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.experiencetype"
              clearable
              placeholder="经验类型"
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
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="reset"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button @click="close">取消</el-button>
        <el-button type="success" @click="confirm">选定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table
      v-loading="listLoading"
      ref="multipleTable"
      :row-key="getRowKeys"
      @selection-change="handleSelectionChange"
      :data="reportData"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="标题" prop="tatle" />
      <el-table-column align="center" label="编码 " prop="code" />
      <el-table-column align="center" label="经验类型" prop="experiencetype" />
      <el-table-column
        align="center"
        label="人员信息"
        prop="createStaff.realname"
      />
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
    <!-- <executor-options ref="executor" @selected="handleExecutorSelected" /> -->
  </el-dialog>
</template>
<script>
  import { getSJJYKList } from '@/api/workbench/auditTools'
  import { formatDay } from '@/utils/index'
  // import ExecutorOptions from './options/executor.vue'
  export default {
    name: 'ConfirmTable',
    // components: { ExecutorOptions },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        listLoading: false,
        reportData: [],
        dialogFormVisible: false,
        title: '审计经验库',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          code: '',
          experiencetype: '',
          tatle: '',
        },
        current: undefined,
        select: [],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      handleSelectionChange(e) {
        this.select = e
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.jykid
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.reportData.forEach((item) => {
          this.select.forEach((id) => {
            if (item.jykid === id.jykid) {
              selectItem.push(item)
            }
          })
        })
        this.$refs.multipleTable.toggleRowSelection(selectItem)
      },
      reset() {
        this.queryForm = {
          businessAffiliation: undefined,
          findPeople: undefined,
          findPeopleName: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleExecutorSelected(node) {
        this.queryForm.findPeople = node.staffid
        this.queryForm.findPeopleName = node.realname
        // this.queryForm.staffid = node.staffid
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getSJJYKList(this.queryForm)
        this.reportData = list
        this.total = total
        this.listLoading = false
      },
      showEdit() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.$refs['ruleForm'].resetFields()
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      confirm() {
        if (!this.select.length) {
          this.$baseMessage('请选择模板！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.select)
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style></style>
