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
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.auditMatter"
              clearable
              placeholder="审计（调查）事项标题"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.auditAbstract"
              clearable
              placeholder="审计（调查）事项概述"
            />
          </el-form-item>
          <!-- <el-form-item>
        <el-input
          :disabled="true"
          v-model="queryForm.findPeopleName"
          clearable
          placeholder="发现人"
          :style="{ width: '256px' }"
        />
        <el-button
          :style="{ marginLeft: '10px' }"
          type="primary"
          @click="$refs.executor.show()"
        >
          选择
        </el-button>
      </el-form-item> -->
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
      <el-table-column align="center" label="项目名称" prop="prjoectname" />
      <el-table-column
        align="center"
        label="审计（调查）事项标题 "
        prop="auditMatter"
      />
      <el-table-column
        align="center"
        label="审计（调查）事项概述"
        prop="auditAbstract"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="证据提供者"
        prop="certificateUser"
      />
      <!-- <el-table-column
        align="center"
        label="日期"
        prop="certificateDate"
        :formatter="formatDate"
      /> -->
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
  import { getQRSModalList } from '@/api/audit/implement'
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
        title: '审计取证单',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          auditMatter: undefined,
          findPeople: undefined,
          findPeopleName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        select: [],
        sheetId: '',
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
        return row.certificateId
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.reportData.forEach((item) => {
          this.select.forEach((id) => {
            if (item.certificateId === id.certificateId) {
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
        } = await getQRSModalList({
          ...this.queryForm,
          sheetid: this.sheetId || '',
        })
        this.reportData = list
        this.total = total
        this.listLoading = false
      },
      showEdit(sheetId) {
        this.dialogFormVisible = true
        this.sheetId = sheetId
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
          this.$baseMessage('请选择事项！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.select)
        this.dialogFormVisible = false
      },
    },
  }
</script>

<style></style>
