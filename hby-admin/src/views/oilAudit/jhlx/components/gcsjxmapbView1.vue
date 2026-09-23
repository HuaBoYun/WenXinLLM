<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="详情"
    :visible.sync="dialogJdVisible"
    width="1000px"
    @close="close"
    v-if="dialogJdVisible"
  >
    <div style="width: 100%; text-align: right; margin-bottom: 10px;">
      <el-button
        type="success"
        @click="handleExport()"
      >
        导出
      </el-button>
      <el-button type="primary" v-if="type === 'report'" style="margin-right: 20px" @click="handleAssign">分配</el-button>
    </div>
    <el-row :gutter="14">
      <el-table
        v-loading="listLoading"
        :data="tableData"
        ref="multipleTable"
        @selection-change="handleSelection"
      >
        <el-table-column v-if="type === 'report'" type="selection" width="55" />
        <el-table-column
          align="center"
          label="序号"
          type="index"
        ></el-table-column>
        <el-table-column
          align="center"
          label="合同编号"
          prop="tblYqnsGcxmzj.htbh"
        />
        <el-table-column
          align="center"
          label="施工单位"
          prop="tblYqnsGcxmzj.sgdw"
        />
        <el-table-column
          align="center"
          label="二审审查金额(元)"
          prop="tblYqnsGcxmzj.esscje"
        />
        <el-table-column align="center" label="本次审计人员" prop="rwnames" />
        <el-table-column
            align="center"
            label="操作"
            show-overflow-tooltip
            width="120"
          >
            <template #default="{ row }">
              <el-button
                type="text"
                @click="handleSplit(row)"
                v-if="row.rwnames && row.rwnames.split(',').length > 0"
              >
                详情
              </el-button>
            </template>
          </el-table-column>
      </el-table>

      <gcxmrysbAssignEdit ref="gcxmrysbAssignEdit" @save="onSave" />
      <gcsjxmapbView1s ref="gcsjxmapbView1s" @save="onSave" />
    </el-row>
  </el-dialog>
</template>

<script>
  import { getgcsjxmapbView1 } from '@/oapi/audit/plan'
  import {
    enginAuditProjectExportgcjs
  } from '@/api/monitor/question'
  import { formatDate } from '@/utils'
  import store from '@/store'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import gcxmrysbAssignEdit from './gcxmrysbAssignEdit.vue'
  import gcsjxmapbView1s from './gcsjxmapbView1s.vue'

  export default {
    components: { gcxmrysbAssignEdit, gcsjxmapbView1s },
    props: {
      type: {
        type: String,
        default: '',
      },
    },
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
        select: [],
        dialogJdVisible: false,
        id: '',
        propsData: {},
      }
    },
    mounted() {
      // this.getOption()
    },
    methods: {
      async showEdit(row) {
        this.select = []
        this.dialogJdVisible = true
        this.id = row.data.gljhxmid
        this.propsData = row.data
        this.fetchData()
      },
      close() {
        this.tableData = []
        this.select = []
        this.propsData = {}
        this.dialogJdVisible = false
      },
      async fetchData() {
        this.listLoading = true
        const data = await getgcsjxmapbView1({ id: this.id, ...this.queryForm })
        this.tableData = data.data
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
      handleAssign() {
        if (!this.select || !this.select.length)
          return this.$message.error('请先选择数据')
        this.$refs.gcxmrysbAssignEdit.showEdit({
          propsData: this.propsData,
          projects: this.select,
        })
      },
      handleSelection(val) {
        console.log(val)
        this.select = val
      },
      handleSplit(row) {
        this.$refs.gcsjxmapbView1s.showEdit(row)
      },
      onSave(flag) {
        if (flag) this.fetchData()
      },
      async handleExport() {
        const data = await enginAuditProjectExportgcjs({
          id: this.id,
        })
        let fileName = '工程审计项目安排'
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
