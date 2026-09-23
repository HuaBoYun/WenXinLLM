<template>
  <div class="system-log-container">
    <el-col v-if="showTitle" :span="24">
      <h3>待发事宜</h3>
    </el-col>
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
              v-model="queryForm.title"
              clearable
              placeholder="流程标题"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.flowType"
              clearable
              placeholder="所属类型"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="queryData"
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
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="标题" prop="title" />
      <el-table-column align="center" label="所属类型" prop="flowType" />
      <el-table-column
        align="center"
        label="当前人接收时间"
        prop="receiveTime"
      />
      <el-table-column align="center" label="是否阅读" prop="readFlag">
        <template #default="{ row }">
          {{ row.readFlag ? '是' : '否' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" show-overflow-tooltip>
        <template #default="{ row }">
          <el-button type="text" @click="showDetail(row)">详情</el-button>
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :page-sizes="pageSizes"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <OAModel ref="oaModel" />
  </div>
</template>

<script>
  import { flowInfolist, getOaurl } from '@/api/contract/manage'
  import { formatDate } from '@/utils/index'
  import OAModel from './options/OAModel.vue'

  export default {
    name: 'oayb',
    components: {
      OAModel,
    },
    props: {},
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        pageSizes: [5, 10, 15, 20, 50, 100],
        queryForm: {
          title: '',
          flowType: '',
          state: 'col_done',
          currentPage: 1,
          pageSize: 10,
        },
        type: '',
        showModal: false,
        UEditorCloudEdit: false,
        UEditorShenPiCloudEdit: false,
        oaurl: '',
        ticket: '',
      }
    },
    created() {
      this.fetchData()
      this.getUrl()
    },

    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetUEditorStatus() {
        this.UEditorCloudEdit = false
        this.UEditorShenPiCloudEdit = false
      },
      reload() {
        this.fetchData()
        this.resetUEditorStatus()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.currentPage = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await flowInfolist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      async getUrl() {
        const res = await getOaurl()
        this.oaurl = res.data.oaurl
        this.ticket = res.data.ticket
      },
      async showDetail(row) {
        window.open(
          this.oaurl.substring(0, this.oaurl.length - 1) +
            row.url +
            '&ticket=' +
            this.ticket
        )
      },
      handleEdit(row) {
        this.$refs.oaModel.showEdit(row)
      },
    },
  }
</script>
