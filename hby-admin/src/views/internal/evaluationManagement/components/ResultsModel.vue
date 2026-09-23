<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row class="box_row flex">
      <el-button type="primary" @click="handExcel()">导出word</el-button>
      <el-button type="primary" @click="handExcel2()">导出评论依据</el-button>
    </el-row>
    <el-col :span="24">
      <el-divider>项目总体情况</el-divider>
    </el-col>
    <el-form ref="form" label-width="120px" :model="form">
      <el-col :span="12">
        <el-form-item label="项目名称">
          <span>{{ form.assessname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价编号">
          <span>{{ form.assessid }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价机构">
          <span>{{ form.orgname }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价模板">
          <span>{{ form.templename }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价期限">
          <span>{{ form.startdate }} 至 {{ form.enddate }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="评价发起人">
          <span>{{ form.asssponsor }}</span>
        </el-form-item>
      </el-col>
    </el-form>
    <el-col :span="24">
      <el-divider>体系权重得分</el-divider>
    </el-col>
    <el-table :data="tableData">
      <el-table-column align="center" label="类别" prop="CATNAME1" />
      <el-table-column align="center" label="权重" prop="CATW" />
      <el-table-column align="center" label="评价得分" prop="SCORE">
        <template #default="{ row }">
          {{ row.SCORE ? row.SCORE.toFixed(2) : '' }}
        </template>
      </el-table-column>
    </el-table>
    <el-col :span="24">
      <div style="margin-top: 60px">
        <el-divider>要素得分明细</el-divider>
      </div>
    </el-col>
    <el-table :data="list">
      <el-table-column align="center" label="类别" prop="CATNAME1" />
      <el-table-column align="center" label="要素名称" prop="ELEMENTNAME" />
      <el-table-column align="center" label="标准分" prop="STANDARDSCORE" />
      <el-table-column align="center" label="评价分" prop="SCORE" />
      <el-table-column align="center" label="查看" prop="data">
        <template #default="{ row }">
          <el-button
            style="color: red"
            type="text"
            @click="$refs['ResultsInfo'].showEdit(row)"
          >
            详细
          </el-button>
        </template>
      </el-table-column>
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
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
    <ResultsInfo ref="ResultsInfo" />
  </el-dialog>
</template>

<script>
  import { projOrganDiap, expWordFile, ysmxExport } from '@/api/internal/result'
  import ResultsInfo from '@/views/internal/evaluationManagement/components/ResultsInfo.vue'
  export default {
    name: 'ResultsModel',
    components: { ResultsInfo },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          code: '',
          name: '',
          pageNo: 1,
          pageSize: 5,
        },
        tableData: [],
        title: '要素明细',
        dialogFormVisible: false,
        form: {
          assessname: '',
          assessid: '',
          orgname: '',
          templename: '',
          startdate: '',
          enddate: '',
          asssponsor: '',
        },
      }
    },
    // created() {
    //   this.fetchData()
    // },
    methods: {
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { list, total },
        } = await getList(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      showEdit(row) {
        this.getInfo(row)
        this.dialogFormVisible = true
      },
      async getInfo(row) {
        const { code, data } = await projOrganDiap({
          assId: row.assid,
          orgid: row.orgid,
        })
        if (code == '200') {
          this.form = data.tblAssess
          this.tableData = data.tixiquanzhong
          this.list = data.pingfenyaosu
        }
      },
      async handExcel() {
        const data = await expWordFile({
          assId: this.form.assid,
          orgid: this.form.orgid,
        })
        let fileName = '评价结果.doc'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-word',
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
      async handExcel2() {
        const data = await ysmxExport({
          assid: this.form.assid,
        })
        let fileName = '要素依据'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
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
      close() {
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
