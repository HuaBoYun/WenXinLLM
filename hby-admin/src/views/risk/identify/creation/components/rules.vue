<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      append-to-body
      width="1000px"
      @close="close"
    >
      <div style="text-align: right; margin-bottom: 5px">
        <el-button type="success" @click="setNG">选择</el-button>
      </div>
      <el-table
        v-loading="loading"
        max-height="500"
        ref="multipleTable"
        :data="dataList"
        tooltip-effect="dark"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column align="center" label="制度名称" prop="rulename" />
        <el-table-column align="center" label="发文文号" prop="rulenumber" />
        <el-table-column align="center" label="发文日期" prop="publishdate" />
        <el-table-column align="center" label="状态" prop="status" />
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
    </el-dialog>
  </div>
</template>

<script>
  // /innerrule/inner_common_qxwt
  import { innerCommonQxwt, outerCommonQxwt } from '@/api/systemLog'
  import { UTCformat } from '@/utils'
  export default {
    name: 'HbAdminRules',

    data() {
      return {
        title: '',
        layout: 'total, sizes, prev, pager, next, jumper',
        loading: false,
        type: '',
        dialogFormVisible: false,
        dataList: [],
        setItem: [],
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 10,
        },
      }
    },

    mounted() {},

    methods: {
      showEdit(type) {
        this.type = type
        this.dialogFormVisible = true
        this.getData()
      },
      setNG() {
        this.$emit('setRules', this.setItem)
        this.dialogFormVisible = false
      },
      getData() {
        this.loading = true
        if (this.type === 'ng') {
          this.title = '公司管理制度'
          innerCommonQxwt(this.queryForm).then((res) => {
            if (res.code === 200) {
              res.data.pageBean.records.map((v) => {
                v.publishdate = UTCformat(v.publishdate)
                return v
              })
              this.dataList = res.data.pageBean.records
              this.total = res.data.pageBean.total
              this.loading = false
            }
          })
        } else {
          this.title = '法律法规'
          outerCommonQxwt(this.queryForm).then((res) => {
            if (res.code === 200) {
              this.dataList = res.data.pageBean.records
              this.total = res.data.pageBean.total
              this.loading = false
            }
          })
        }
      },
      close() {
        this.dialogFormVisible = false
      },
      handleSelectionChange(val) {
        this.setItem = val
      },

      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.getData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getData()
      },
    },
  }
</script>

<style lang="scss" scoped></style>
