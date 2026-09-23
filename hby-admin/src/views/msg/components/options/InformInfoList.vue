<template>
  <div>
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <!-- <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.informstaffName"
              clearable
              placeholder="知会人姓名"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.createStaffName"
              clearable
              placeholder="发起人姓名"
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
        </el-form>-->
      </vab-query-form-left-panel>
      <!--  <vab-query-form-right-panel :span="6">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel> -->
    </vab-query-form>
    <el-table :data="list">
      <el-table-column
        align="center"
        label="发起人员姓名"
        prop="createStaffName"
      />
      <el-table-column
        align="center"
        label="知会人员姓名"
        prop="informStaffName"
      />
      <el-table-column
        align="center"
        label="知会时间"
        prop="createTime"
        :formatter="formatDate"
      />
      <el-table-column
        align="center"
        label="知会人阅读时间"
        prop="readTime"
        :formatter="formatDate"
      />
      <el-table-column align="center" label="是否已阅" prop="isRead">
        <template #default="{ row, $index }">
          <el-tag v-if="row.isRead == 1" type="success">已阅</el-tag>
          <el-tag v-else type="danger">未读</el-tag>
        </template>
      </el-table-column>

      <!-- <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="150"
      >
        <template #default="{ row, $index }">
          <el-button type="text" @click="handleDown(row)">下载</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
  </div>
</template>

<script>
  import { informInfoList } from '@/api/contract/manage'
  import { formatDate } from '@/utils/index'
  export default {
    name: 'InformInfoList',
    props: {
      flowTaskInfo: { type: Object, default: {} },
    },

    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          id: '',
          flowId: '',
          informstaffName: '',
          createStaffName: '',
          currentPage: 1,
          pageSize: 20,
        },
      }
    },
    watch: {
      flowTaskInfo: {
        handler(val) {
          if (val.id) {
            this.queryForm.flowId = val.flowId
            this.queryForm.id = val.id
            this.fetchData()
          }
        },
        immediate: true,
      },
    },
    created() {
      console.log(this.flowTaskInfo, 'flowTaskInfo')
    },
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      async fetchData() {
        // this.resetQueryForm()
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await informInfoList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.currentPage = val
        this.fetchData()
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm
        this.queryForm.informstaffName = ''
        this.queryForm.createStaffName = ''
        this.$forceUpdate()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>

<style></style>
