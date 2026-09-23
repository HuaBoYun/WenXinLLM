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
              v-model="queryForm.proname"
              clearable
              placeholder="建议书名称"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.procode"
              clearable
              placeholder="建议书编号"
            />
          </el-form-item>
          <el-form-item>
            <el-form-item>
              <el-date-picker
                align="right"
                end-placeholder="报告结束日期"
                range-separator="至"
                format="yyyy-MM-dd"
                start-placeholder="报告开始日期"
                type="daterange"
                unlink-panels
                v-model="queryForm.Date"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
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
      <el-table-column
        align="center"
        label="建议书编号"
        prop="procode"
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.procode }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="建议书名称" prop="proname" />
      <el-table-column
        align="center"
        label="创建人"
        prop="tblStaff.realname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="createTime"
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="状态"
        prop="status"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          {{ row.status == 1 ? '已作废' : '使用' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleInvalid(row)">作废</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <SuggestView ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { formatDate } from '@/utils/index'
  import {
    auditSuggestList,
    auditSuggestCancel,
    auditSuggestDetail,
    suggestDel,
  } from '@/api/audit/report'
  import SuggestView from '@/views/audit/report/components/SuggestView'

  export default {
    name: 'Download',
    components: { SuggestView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          procode: '',
          proname: '',
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await auditSuggestList({ ...other, startDate, endDate })
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
        const data = await auditSuggestDetail({ proid: row.proid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleEdit(row) {
        const data = await auditSuggestDetail({ proid: row.proid })
        await this.$refs['edit'].showEdit('edit', data.data)
      },
      handleInvalid(row) {
        if (row.status == 1) {
          this.$baseMessage('已作废的建议书不能作废', 'error')
          return
        }
        this.$baseConfirm('你确定要作废当前项吗', null, async () => {
          const { msg, code } = await auditSuggestCancel({
            proid: row.proid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await suggestDel({
            proid: row.proid,
          })
          if (code == 1) {
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
