
<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="20">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.jobname"
              clearable
              placeholder="表单名称"
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
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="4">
        <el-button type="success" @click="handleAdd">添加</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="表单名称" prop="jobname" />
      <el-table-column align="center" label="关键字" prop="jobid" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="customEdit(row)">编辑表单</el-button>
          <el-button type="text" @click="handleDelete(row)">禁用</el-button>
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
    <AddFormEdit ref="edit" @fetch-data="fetchData" />
    <CustomFormEdit ref="customForm" />
  </div>
</template>

<script>
import { jobList } from '@/api/setting/auth'
import { jobDel } from '@/api/setting/auth'
import AddFormEdit from '@/views/setting/activiti/components/AddFormEdit'
import CustomFormEdit from "@/views/setting/activiti/components/CustomFormEdit";

export default {
  name: 'Download',
  components: {CustomFormEdit, AddFormEdit },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        jobname: '',
        pageNumber: 1,
        pageSize: 20,
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
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
    async fetchData() {
      this.listLoading = true
      const {
        pageInfo: { tlist, totalRecord },
      } = await jobList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
    handleAdd() {
      this.$refs['edit'].showEdit()
    },
    handleEdit(row) {
      this.$refs['edit'].showEdit(row)
    },
    customEdit(){
      this.$refs['customForm'].show()
    },
    handleDelete(row) {
      this.$baseConfirm('你确定要删除当前项吗', null, async () => {
        this.$baseMessage('操作成功', 'success', 'vab-hey-message-success')
        await this.fetchData()
      })
    },
  },
}
</script>
