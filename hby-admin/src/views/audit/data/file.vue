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
              v-model="queryForm.attname"
              clearable
              placeholder="附件名称"
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
    </vab-query-form>

    <el-table v-loading="listLoading" :data="list">
      <el-table-column align="center" label="编号" prop="attid" width="100" />
      <el-table-column align="center" label="附件名称" prop="attname" />
      <el-table-column
        align="center"
        label="文件大小(KB)"
        prop="attsize"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="上传时间"
        prop="uploadtime"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ dayjs(scope.row.uploadtime).format('YYYY-MM-DD') }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="创建人"
        prop="uploader"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit2(row)">下载</el-button>
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import { download } from '@/api/audit/implement'
  import { getfjlistDownload } from '@/api/audit/projectData'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import * as dayjs from 'dayjs'
  export default {
    name: 'Download',
    components: { LcdyEdit },
    data() {
      return {
        dayjs: dayjs,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          attname: '',
          pageNumber: 1,
          pageSize: 20,
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      //重置
      resetSearch() {
        this.queryForm = { attname: '', pageNumber: 1, pageSize: 20 }
        this.fetchData()
      },
      //下载
      async handleEdit2(e) {
        const res = await download({ attid: e.attid })
        this.downloadFileByBlob(res, e.attname)
      },
      downloadFileByBlob(blob, fileName = 'file') {
        let blobUrl = window.URL.createObjectURL(blob)
        let link = document.createElement('a')
        link.download = fileName || 'defaultName'
        link.style.display = 'none'
        link.href = blobUrl
        // 触发点击
        document.body.appendChild(link)
        link.click()
        // 移除
        document.body.removeChild(link)
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
            pageInfo: { tlist, totalRecord },
          },
        } = await getfjlistDownload(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
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
