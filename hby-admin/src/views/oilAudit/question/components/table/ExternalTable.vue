<template>
  <el-dialog
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @selection-change="handleSelectionChange"
      @current-change="handleSelected"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="制度名称" prop="rulename" />
      <el-table-column align="center" label="发文文号" prop="rulenumber" />
      <el-table-column align="center" label="发布机构" prop="orgname" />
      <el-table-column
        align="center"
        :formatter="formatDate"
        label="发布日期"
        prop="publishdate"
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
  import { outerCommonQxwt, addOuterCommonQxwt } from '@/oapi/audit/question'
  import { parseTime } from '@/utils/index'
  export default {
    name: 'ExternalTable',
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        listLoading: false,
        list: [],
        dialogFormVisible: false,
        title: '外规',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectDataList: '',
        queryForm: {
          businessAffiliation: undefined,
          findPeople: undefined,
          bugid: undefined,
          pageNumber: 1,
          pageSize: 10,
        },
        current: undefined,
        formData: {
          outrulids: '',
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSelectionChange(val) {
        this.formData.outrulids = ''
        val.forEach((item) => {
          this.formData.outrulids += item.outrulid + ','
        })
        console.dir(this.formData)
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await outerCommonQxwt(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      showEdit(bugid) {
        this.formData.bugid = bugid
        this.dialogFormVisible = true
        if (bugid) {
          this.queryForm.bugid = bugid
          this.fetchData()
        }
      },
      close() {
        this.dialogFormVisible = false
        this.tableData = []
        this.queryForm.solutionid = undefined
      },
      handleSelected(val) {
        // this.$emit('selected', val)
        this.current = val
        // this.dialogFormVisible = false
      },
      async confirm() {
        if (this.formData.outrulids.length === 0) {
          this.$message.warning('请选择事项！')
          return
        }
        const res = await addOuterCommonQxwt(this.formData)
        if (res.code === 1) {
          this.$message.success('添加成功')
          this.$emit('selected', this.formData.bugid)
          this.dialogFormVisible = false
        }
      },
    },
  }
</script>

<style></style>
