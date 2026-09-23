<template>
  <theme :theme-type="1" />
  <!--  <div class="system-log-container">-->
  <!--    <vab-query-form>-->
  <!-- <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <el-form-item class="">
          <el-input v-model="queryForm.pageid" clearable placeholder="编号" />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="queryForm.pagename"
            clearable
            placeholder="栏目名称"
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
      </el-form> -->
  <!--      <vab-query-form-left-panel>-->
  <!--        <div class="table-action">-->
  <!--          <el-button size="mini" type="primary" @click="sendModel">-->
  <!--            模块下发-->
  <!--          </el-button>-->
  <!--          <el-button size="mini" type="primary" @click="send">下发</el-button>-->
  <!--        </div>-->
  <!--      </vab-query-form-left-panel>-->
  <!--      <vab-query-form-right-panel>-->
  <!--        <el-button type="success" @click="handleAdd">新建</el-button>-->
  <!--      </vab-query-form-right-panel>-->
  <!--    </vab-query-form>-->

  <!--    <el-table v-loading="listLoading" :data="list">-->
  <!--      <el-table-column type="selection" width="55" />-->
  <!--      <el-table-column-->
  <!--        align="center"-->
  <!--        label="编号"-->
  <!--        prop="pageid"-->
  <!--        show-overflow-tooltip-->
  <!--        width="230px"-->
  <!--      />-->
  <!--      <el-table-column-->
  <!--        align="center"-->
  <!--        label="栏目名称"-->
  <!--        prop="pagename"-->
  <!--        show-overflow-tooltip-->
  <!--        width="230px"-->
  <!--      />-->
  <!--      <el-table-column align="center" label="描述" prop="pageDes" />-->
  <!--      <el-table-column-->
  <!--        align="center"-->
  <!--        label="操作"-->
  <!--        show-overflow-tooltip-->
  <!--        width="100"-->
  <!--      >-->
  <!--        <template #default="{ row }">-->
  <!--          <el-button type="text" @click="handleEdit(row)">修改</el-button>-->
  <!--          <el-button type="text" @click="handleDelete(row)">删除</el-button>-->
  <!--        </template>-->
  <!--      </el-table-column>-->
  <!--    </el-table>-->
  <!--    <el-pagination-->
  <!--      background-->
  <!--      :current-page="queryForm.pageNumber"-->
  <!--      :layout="layout"-->
  <!--      :page-size="queryForm.pageSize"-->
  <!--      :total="total"-->
  <!--      @current-change="handleCurrentChange"-->
  <!--      @size-change="handleSizeChange"-->
  <!--    />-->
  <!--    <theme-edit ref="edit" @fetch-data="fetchData" />-->
  <!--    <send-model ref="sendModel" @fetch-data="fetchData" />-->
  <!--    <send ref="send" @fetch-data="fetchData" />-->
  <!--  </div>-->
</template>

<script>
  // import { reportlist } from '@/api/setting/themeRepertory'
  // import ThemeEdit from '@/views/setting/themeRepertory/components/ThemeEdit'
  // import { pagedel } from '@/api/setting/themeRepertory'
  // import SendModel from '@/views/setting/themeRepertory/components/SendModel'
  // import Send from '@/views/setting/themeRepertory/components/Send'
  import theme from './theme'
  export default {
    name: 'ThemePhone',
    components: { theme },
    // components: { Send, SendModel, ThemeEdit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageid: '',
          pagename: '',
          pageNumber: 1,
          pageSize: 20,
          type: 1,
        },
      }
    },
    created() {
      // this.fetchData()
    },
    methods: {
      // handleSizeChange(val) {
      //   this.queryForm.pageSize = val
      //   this.fetchData()
      // },
      // handleCurrentChange(val) {
      //   this.queryForm.pageNumber = val
      //   this.fetchData()
      // },
      // queryData() {
      //   this.queryForm.pageNumber = 1
      //   this.fetchData()
      // },
      // async fetchData() {
      //   this.listLoading = true
      //   const {
      //     pageInfo: { tlist, totalRecord },
      //   } = await reportlist(this.queryForm)
      //   this.list = tlist
      //   this.total = totalRecord
      //   this.listLoading = false
      // },
      // handleAdd() {
      //   this.$refs['edit'].showEdit(1)
      // },
      // handleEdit(row) {
      //   this.$refs['edit'].showEdit(1, row)
      // },
      // handleDelete(row) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg } = await pagedel({ selectedId: row.pageid })
      //     this.$baseMessage(msg, 'success', 'vab-hey-message-success')
      //     await this.fetchData()
      //   })
      // },
      // sendModel() {
      //   this.$refs['sendModel'].showEdit()
      // },
      // send() {
      //   this.$refs['send'].showEdit()
      // },
    },
  }
</script>
