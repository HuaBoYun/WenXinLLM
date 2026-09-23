<!--
 * @Date: 2022-03-28 15:06:04
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-21 16:55:07
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/setupInfo.vue
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.projectcode"
              clearable
              placeholder="项目编号"
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
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="6">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
    >
      <el-table-column align="center" label="项目编号" prop="projectcode" />
      <el-table-column align="center" label="项目名称" prop="projectname" />
      <el-table-column
        align="center"
        label="项目负责人"
        prop="undertakestaff.realname"
      />
      <el-table-column
        align="center"
        label="申报时间"
        prop="createtime"
        :formatter="formatDate"
      />
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
  </el-dialog>
</template>
<script>
  // import { getSetupInfoOptions } from '@/api/contract/manage'
  import { contractProList } from '@/api/contract/project'
  import { formatDate } from '@/utils/index'
  export default {
    name: 'BankOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {},
        current: undefined,
      }
    },
    created() {},
    methods: {
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = {
          projectcode: undefined,
          projectname: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      show() {
        this.resetQueryForm()
        this.current = undefined
        this.dialogFormVisible = true
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        // const jsonArry = []
        // const { TEXTWZRY, TEXTXKTB, TEXT7WXZ, DATEJ4K2 } = this.queryForm[
        //   (TEXTWZRY, TEXTXKTB, TEXT7WXZ, DATEJ4K2)
        // ].forEach((key) => {
        //   if (key) {
        //     if (key == 'DATEJ4K2') {
        //       jsonArry.push(['DATEJ4K2', '等于', `${this.queryForm.DATEJ4K2}`])
        //     } else {
        //       jsonArry.push(key, '包含', `${this.queryForm[key]}`)
        //     }
        //   }
        // })
        // this.queryForm.jsonArry = jsonArry
        this.listLoading = true
        const {
          data: {
            pageInfo: { totalRecord, tlist },
          },
        } = await contractProList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
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
      handleSelected(val) {
        this.current = val
        // this.$emit('selected', val)
        // this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      //前置校验
      confirm() {
        if (!this.current) {
          this.$baseMessage(
            '请选择立项信息！',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
    },
  }
</script>
