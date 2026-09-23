<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-select
              v-model="queryForm.type"
              clearable
              placeholder="查询类型"
            />
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.filter"
              clearable
              placeholder="过滤"
            />
          </el-form-item>
          <el-form-item>
            <el-input v-model="queryForm.name" clearable placeholder="名称" />
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
            <el-button native-type="submit" @click="fetchData">重置</el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="表单编号"
        prop="data"
        width="100"
      />
      <el-table-column
        align="center"
        label="表单名称"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="表单描述"
        prop="data"
        show-overflow-tooltip
        width="150"
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="创建人"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column
        align="center"
        label="表单状态"
        prop="data"
        show-overflow-tooltip
        width="120"
      />
      <el-table-column align="center" label="操作">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleEnable(row)">启用</el-button>
          <el-button type="text" @click="handleAbandon(row)">弃用</el-button>
          <el-button type="text" @click="handleControl(row)">
            列表控制
          </el-button>
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
    <CustomForm ref="edit" />
  </div>
</template>

<script>
  import { getList } from '@/api/systemLog'
  import CustomForm from './CustomForm'

  export default {
    name: 'Fillin',
    components: { CustomForm },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          type: '',
          filter: '',
          name: '',
          pageNo: 1,
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
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEnable() {},
      handleAbandon() {},
      handleControl() {},
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
    },
  }
</script>
<style scoped>
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
