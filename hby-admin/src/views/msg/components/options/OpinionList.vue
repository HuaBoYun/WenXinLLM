<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :append-to-body="true"
    width="60%"
    @close="closeCurrent"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.tempTitle"
              clearable
              placeholder="模板标题"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.tempMemo"
              clearable
              placeholder="模板内容"
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
          </el-form-item>
          <el-form-item>
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="6">
        <el-button type="success" @click="handleAdd()">新建</el-button>
        <el-button type="primary" @click="handleSelect()">确定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="tableData"
      highlight-current-row
      @current-change="handleSelected"
    >
      <el-table-column align="center" label="模板标题" prop="tempTitle" />
      <el-table-column align="center" label="模板内容" prop="tempMemo" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="150"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <OpinionEdit ref="edit" @fetchData="fetchData" />
  </el-dialog>
</template>

<script>
  import {
    getFlowTemplateList,
    getFlowTemplateInfo,
    removeFlowTemplate,
  } from '@/api/contract/manage.js'
  import OpinionEdit from './OpinionEdit.vue'
  export default {
    name: 'OpinionList',
    props: {},
    components: {
      OpinionEdit,
    },
    data() {
      return {
        queryForm: {
          currentPage: 1,
          pageSize: 10,
          taskNodeId: '',
          flowId: '',
          tempMemo: '',
          tempTitle: '',
        },
        total: 0,
        tableData: [],
        current: {},
        visible: false,
        title: '意见模板列表',
      }
    },
    methods: {
      async show(row) {
        this.visible = true
        this.queryForm.taskNodeId = row.taskNodeId
        this.queryForm.flowId = row.flowId
        //
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = {
          currentPage: 1,
          pageSize: 10,
          taskNodeId: this.queryForm.taskNodeId,
          flowId: this.queryForm.flowId,
          tempMemo: '',
          tempTitle: '',
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        const res = await getFlowTemplateList(this.queryForm)
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = res
        this.tableData = tlist
        this.total = totalRecord
      },

      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      closeCurrent() {
        this.visible = false
        this.current = {}
        this.tableData = []
      },
      handleAdd() {
        this.$refs.edit.show({}, 'add', this.queryForm)
      },
      handleEdit(row) {
        this.$refs.edit.show(row, 'edit', this.queryForm)
      },
      handleDelete(row) {
        this.$confirm('此操作将永久删除该模板, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            const res = await removeFlowTemplate({ tempId: row.tempId })
            if (res.code == 1) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            }
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleSelected(val) {
        this.current = val
      },
      handleSelect() {
        if (!this.current) {
          this.$baseMessage('请选择模板！', 'error', 'vab-hey-message-error')
          return
        }

        this.$emit('selected', this.current)
        this.closeCurrent()
      },
    },
  }
</script>

<style></style>
