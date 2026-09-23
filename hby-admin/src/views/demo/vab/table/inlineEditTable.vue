<template>
  <div class="inline-edit-table-container">
    <el-alert
      :closable="false"
      show-icon
      title="三级路由【不缓存路由并且不固定表格高度】的示例"
      type="success"
    />
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input v-model="queryForm.title" placeholder="标题" />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="handleQuery"
            >
              查询
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" border :data="list">
      <el-table-column
        align="center"
        show-overflow-tooltip
        type="selection"
        width="55"
      />
      <el-table-column
        align="center"
        label="序号"
        show-overflow-tooltip
        width="95"
      >
        <template #default="{ $index }">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="标题"
        min-width="300px"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <template v-if="row.edit">
            <el-input v-model="row.title" style="width: 300px" />
            <el-button
              class="cancel-btn"
              type="warning"
              @click="cancelEdit(row)"
            >
              取消
            </el-button>
          </template>
          <span v-else>{{ row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="作者"
        prop="author"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="200"
      >
        <template #default="{ row }">
          <el-button
            v-if="row.edit"
            size="small"
            type="success"
            @click="confirmEdit(row)"
          >
            保存
          </el-button>
          <el-button
            v-else
            icon="el-icon-edit"
            size="small"
            type="primary"
            @click="row.edit = !row.edit"
          >
            编辑
          </el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-image
          class="vab-data-empty"
          :src="require('@/assets/empty_images/data_empty.png')"
        />
      </template>
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
  </div>
</template>

<script>
  import { getList } from '@/api/table'

  export default {
    name: 'InlineEditTable',
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          title: '',
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list.map((v) => {
          this.$set(v, 'edit', false)
          v.originalTitle = v.title
          return v
        })
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      handleQuery() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      cancelEdit(row) {
        row.title = row.originalTitle
        row.edit = false
      },
      confirmEdit(row) {
        row.edit = false
        row.originalTitle = row.title
      },
    },
  }
</script>
