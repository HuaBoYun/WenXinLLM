<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1300px"
    @close="close"
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
      <el-table-column align="center" label="缺陷编号" prop="bugnumber" />
      <el-table-column align="center" label="缺陷描述" prop="bugdescripte" />
      <el-table-column
        align="center"
        :formatter="formatDate"
        label="发现时间"
        prop="discovertime"
      />
      <el-table-column align="center" label="发现人" prop="discoverperson" />
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
  import { associateDefect, addAssociateDefect } from '@/api/internal/question'
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
        title: '关联缺陷',
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        selectDataList: '',
        queryForm: {
          businessAffiliation: undefined,
          findPeople: undefined,
          orgit: undefined,
          pageNumber: 1,
          pageSize: 10,
          orgid: undefined,
          bugcriid: undefined,
          bugnumber: undefined,
          bugid: '',
        },
        current: undefined,
        formData: {
          bugids: '',
        },
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSelectionChange(val) {
        this.formData.bugids = val
        // val.forEach((item) => {
        //   this.formData.bugids += item.bugid + ','
        // })
        // console.dir(this.formData)
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
        } = await associateDefect(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      showEdit(data) {
        console.log(data)
        // this.formData.orgId = data.orgId
        // this.formData.bugcriid = data.bugcriid
        // this.formData.bugnumber = data.bugnumber
        this.queryForm.bugid = data
        this.dialogFormVisible = true
        this.fetchData()
        // if (data.orgId) {
        //   this.queryForm.orgid = data.orgId
        //   this.queryForm.bugcriid = data.bugcriid
        //   this.queryForm.bugnumber = data.bugnumber
        //   this.fetchData()
        // }
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
        const data = {
          bugids: this.formData.bugids.map((v) => v.bugid).toString(),
          bugid: this.queryForm.bugid,
        }
        if (this.formData.bugids.length === 0) {
          this.$message.warning('请选择事项！')
          return
        }
        const res = await addAssociateDefect(data)
        if (res.code === 1) {
          this.$message.success('添加成功')
          this.$emit('selected', data.bugid)
          this.dialogFormVisible = false
        }
      },
    },
  }
</script>

<style></style>
