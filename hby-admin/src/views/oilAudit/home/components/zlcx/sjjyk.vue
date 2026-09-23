<template>
  <div class="system-log-container">
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="标题"
        prop="tatle"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <el-button @click="handleEdit(true, '详情', scope.row)" type="text">
            {{ scope.row.tatle }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="编码" prop="code" />
      <el-table-column align="center" label="经验类型" prop="experiencetype" />
      <el-table-column
        align="center"
        label="开始时间"
        prop="createdtime"
        :formatter="formatDate"
      />

      <el-table-column align="center" label="人员信息" prop="createStaff">
        <template slot-scope="scope">
          {{ scope.row.createStaff.realname }}
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData" />
  </div>
</template>
<script>
  import { getSJJYKList, sjjykDelete } from '@/oapi/workbench/auditTools'
  import { parseTime } from '@/utils/index'
  import Edit from '@/views/workbench/controlLib/components/sjjykEdit.vue'
  export default {
    components: { Edit },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          code: '',
          experiencetype: '',
          tatle: '',
        },
        list: [],
        listLoading: true,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleDetail(row) {},

      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getSJJYKList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(disabled, type, row) {
        this.$refs['edit'].showEdit(disabled, type, row)
      },

      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await sjjykDelete({ jykid: row.jykid })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
    },
  }
</script>
