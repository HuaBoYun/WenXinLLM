<template>
  <div class="system-log-container">
    <div>
      <vab-query-form>
        <vab-query-form-left-panel>
          <el-form
            ref="form"
            :inline="true"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item>
              <el-input
                v-model="queryForm.code"
                clearable
                placeholder="问题编号"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.projectname"
                clearable
                placeholder="项目名称"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.orgname"
                clearable
                placeholder="公司"
                :style="{ width: '256px' }"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.department.show()"
              >
                选择
              </el-button>
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
                重置
              </el-button>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
        <!-- <vab-query-form-right-panel>
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel> -->
      </vab-query-form>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="问题编号"
          prop="code"
          width="100"
        />
        <el-table-column align="center" label="项目名称" prop="projectname" />
        <el-table-column align="center" label="被审计对象" prop="company" />
        <el-table-column
          align="center"
          label="问题详情"
          prop="details"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="整改结果"
          prop="reformresult"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="整改执行人"
          prop="zgzxxrname"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="整改状态"
          prop="status"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{
              row.status == '1'
                ? '开始'
                : row.status == '2'
                ? '整改中'
                : row.status == '4'
                ? '方案审批中'
                : '整改完成'
            }}
          </template>
        </el-table-column>
        <!-- <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="evaluate(row)">评价</el-button>
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
    </div>
    <department-options ref="department" @selected="handleDepartmentSelected" />
  </div>
</template>

<script>
  import { getzlAllReformlist } from '@/oapi/audit/rectify'
  import { formatDate } from '@/utils/index'
  import DepartmentOptions from './components/options/department.vue'

  export default {
    name: 'Standingbook',
    components: { DepartmentOptions },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          orgname: '',
          auditorg: '',
          code: '',
          projectname: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleDepartmentSelected(node) {
        this.queryForm.auditorg = node.id
        this.queryForm.orgname = node.name
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await getzlAllReformlist(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async evaluate(row) {
        await this.$refs['table'].showEdit(row.solutionid)
      },
      async handleDetail(row) {
        const data = await getSolutionDetail({ solutinid: row.solutionid })
        await this.$refs['edit'].showEdit('detail', data.data)
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
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
