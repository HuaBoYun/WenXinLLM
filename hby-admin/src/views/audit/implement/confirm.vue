<template>
  <div class="system-log-container">
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
            <el-input
              v-model="queryForm.factcode"
              clearable
              placeholder="编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.factname"
              clearable
              placeholder="所属计划"
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
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="编号" prop="factcode" width="100">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.factcode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="所属计划"
        prop="tblNbsjProject.prjoectName"
      />
      <el-table-column
        align="center"
        label="拟稿人"
        prop="createstaffname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="事实描述"
        prop="describe"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="审批状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{
            row.status == '1'
              ? '未审批'
              : row.status == '2'
              ? '审批中'
              : row.status == '3'
              ? '需调整'
              : row.status == '4'
              ? '已终止'
              : '已完成'
          }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="拟稿时间"
        prop="createtime"
        show-overflow-tooltip
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="row.status === 1 ? false : true"
          >
            修改
          </el-button>
          <el-button
            type="text"
            :disabled="row.status === 1 ? false : true"
            @click="handleEdit2(row)"
          >
            审批
          </el-button>
          <el-button
            type="text"
            :disabled="row.status === 1 ? false : true"
            @click="handleDelete(row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
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
    <ConfirmInfo ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    confirmDelete,
    confirmDetail,
    confirmList,
    saveTblNbsjFactbookArrpoval,
  } from '@/api/audit/implement'
  import { formatDate } from '@/utils/index'
  import ConfirmInfo from '@/views/audit/implement/components/ConfirmInfo'

  export default {
    name: 'Download',
    components: { ConfirmInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          factcode: '',
          factname: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleEdit2(row) {
        this.$baseConfirm('你确定要审批当前项吗', null, async () => {
          const { msg, code } = await saveTblNbsjFactbookArrpoval({
            factid: row.factid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        } = await confirmList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        const data = await confirmDetail({ factid: row.factid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        const data = await confirmDetail({ factid: row.factid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await confirmDelete({ factid: row.factid })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
    },
  }
</script>
