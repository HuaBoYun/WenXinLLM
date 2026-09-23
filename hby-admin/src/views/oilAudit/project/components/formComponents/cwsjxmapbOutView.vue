<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="详情"
    :visible.sync="dialogJdVisible"
    width="80%"
    @close="close"
    v-if="dialogJdVisible"
  >
    <el-row :gutter="14">
      <el-table
        v-loading="listLoading"
        :data="tableData"
        ref="multipleTable"
        :key="tableKey"
        @select="handleSelection"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="序号"
          type="index"
        ></el-table-column>
        <el-table-column
          align="center"
          label="项目名称"
          prop="projectname"
          width="180"
        >
        </el-table-column>
        <el-table-column
          align="center"
          label="审计范围"
          prop="name"
          #default="{ row }"
          width="180"
        >
          {{
            row.oldJobStartTime && row.oldJobEndTime
              ? row.oldJobStartTime + ' ~ ' + row.oldJobEndTime
              : ''
          }}
        </el-table-column>
        <el-table-column align="center" label="组长" prop="zznames" />
        <el-table-column align="center" label="副组长" prop="fznames" />
        <el-table-column align="center" label="主审" prop="zsname" />
        <el-table-column
          align="center"
          label="助审"
          prop="fzname"
          width="180"
        />
        <el-table-column align="center" label="人数" prop="rsyq" />
        <el-table-column
          align="center"
          label="现场时间"
          prop="name"
          #default="{ row }"
          width="180"
        >
          {{
            row.xcsrarttime && row.xcendtime
              ? row.xcsrarttime + ' ~ ' + row.xcendtime
              : ''
          }}
        </el-table-column>
      </el-table>
    </el-row>

    <template #footer>
      <el-button @click="dialogJdVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { getSjdwlrsjSbListByhz } from '@/oapi/audit/plan'
  import { fundAuditOutProjectExportsjdw } from '@/api/monitor/question'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { xiafaListNew } from '@/oapi/audit/preparation'

  export default {
    props: {
      type: {
        type: String,
        default: '',
      },
    },
    components: { },
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        total: 0,
        listLoading: false,
        tableData: [],
        dialogJdVisible: false,
        id: '',
        tableKey: new Date().getTime(),
        propsData: null,
        multipleSelection: [],
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      async showEdit(row) {
        console.log('row1', row)
        this.propsData = row.data
        this.dialogJdVisible = true
        this.id = row.data.id
        this.fetchData()
      },
      close() {
        this.propsData = null
        this.tableData = []
        this.dialogJdVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const res = await getSjdwlrsjSbListByhz({
          glid: this.id,
          ...this.queryForm,
        })
        this.tableData = res.data.tlist
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      save() {
        let row = this.multipleSelection[0]
        this.$emit('selected', row.projectname, row)
        this.close()
      },
      handleSaveData(formData) {
        console.log('formData', formData)
        console.log('this.tableData', this.tableData)
        this.tableData.some((x) => {
          if (x.id === formData.id) {
            x.zznames = formData.zznames
            x.fznames = formData.fznames
            x.zsname = formData.zsname
            x.fzname = formData.fzname
            x.rsyq = formData.rsyq
            x.xcsrarttime = formData.xcsrarttime
            x.xcendtime = formData.xcendtime
            return true
          }
        })
        this.tableKey = new Date().getTime()
      },
      async handleExport() {
        const data = await fundAuditOutProjectExportsjdw({
          glid: this.id,
        })
        let fileName = '财务审计项目安排表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
