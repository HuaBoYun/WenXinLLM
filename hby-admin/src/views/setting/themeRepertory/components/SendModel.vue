<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="800px"
    @close="close"
  >
    <vab-query-form>
      <el-form
        ref="form"
        :inline="true"
        label-width="0"
        :model="queryForm"
        @submit.native.prevent
      >
        <!--        <el-form-item class="">-->
        <!--          <el-input-->
        <!--            v-model="queryForm.rightname"-->
        <!--            clearable-->
        <!--            placeholder="模块名称"-->
        <!--          />-->
        <!--        </el-form-item>-->
        <!--        <el-form-item>-->
        <!--          <el-button-->
        <!--            icon="el-icon-search"-->
        <!--            native-type="submit"-->
        <!--            type="primary"-->
        <!--            @click="fetchData"-->
        <!--          >-->
        <!--            查询-->
        <!--          </el-button>-->
        <!--        </el-form-item>-->
        <el-button style="float: right" type="primary" @click="save">
          确定
        </el-button>
      </el-form>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        align="left"
        label="模块名称"
        prop="projectName"
        show-overflow-tooltip
      />
    </el-table>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
  </el-dialog>
</template>

<script>
  import {
    moduleDistributioList,
    moduleDistributioSave,
    moduleDistributioSaveTwo,
  } from '@/api/setting/themeRepertory'

  export default {
    name: 'Send',
    data() {
      return {
        title: '模块下发',
        dialogFormVisible: false,
        list: [],
        total: 0,
        queryForm: {
          ids: '',
          pid: '',
          cztype: 1,
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        options: [],
        listLoading: false,
        multipleSelection: [],
        pid:-1,
        getSelectModules: false,
        showModules: null
      }
    },
    created() {},
    methods: {
      handleSelectionChange(val) {
        console.warn('handleSelectionChange', val)
        this.multipleSelection = val
      },
      show(data) {
        this.getSelectModules = true
        this.dialogFormVisible = true
        this.showModules = data?.modules
        this.fetchData()
      },
      showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm.ids = row.ids
        this.pid = row.pid 
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.pid = -1
      },
      async save() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        const rightids = this.multipleSelection.map((i) => i.uniqueIdentification).join(',')

        // 只选择模块
        if (this.getSelectModules) {
          this.$emit('selected', rightids)
          this.dialogFormVisible = false
          return
        }
        
        if(!this.pid){
          const { msg } = await moduleDistributioSave({
            moduleTypes: rightids,
            pageids: this.queryForm.ids,
          })
          this.$baseMessage(
            msg || '设置成功',
            'success',
            'vab-hey-message-success'
          )
        }else{
          const { msg } = await moduleDistributioSaveTwo({
            moduleTypes: rightids,
            pageids: this.queryForm.ids,
            pid:this.pid,
          })
          this.$baseMessage(
            msg || '设置成功',
            'success',
            'vab-hey-message-success'
          )
        }
       
        
        this.$emit('fetch-data')
        this.close()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data
        } = await moduleDistributioList(this.queryForm)
        if (this.getSelectModules && this.showModules) {
          this.list = data.filter(x => this.showModules.includes(x.uniqueIdentification))
        } else {
          this.list = data
        }
        this.total = 0
        this.listLoading = false
      },
    },
  }
</script>
