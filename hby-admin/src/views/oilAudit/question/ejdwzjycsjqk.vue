<template>
  <div class="system-log-container">
    <Table1></Table1>
    <Table2></Table2>
  </div>
</template>

<script>
  import Table1 from './components/table/ejdwTable1.vue'
  import Table2 from './components/table/ejdwTable2.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'ejdwzjycsjqk',
    components: {
      Table1,
      Table2
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pc: '',
          projectname: '',
          auditorgname: '',
        },
        filedAll: [
          { name: '项目承担单位（实施单位）' },
          { name: '被审计单位名称' },
          { name: '审计项目名称' },
          // { name: '项目级别（二级机构/三级机构）' },
          // { name: '业务领域' },
          // { name: '具体业务' },
          { name: '经责科负责人' },
          { name: '审计组人数' },
          { name: '现场审计开始日期' },
          { name: '实际现场结束日期' },
          { name: '实际工作天数' },
          { name: '投入资源（人日）' },
          { name: '工效比' },
          { name: '复合底稿数量' },
          { name: '问题底稿数量' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-oilAudit-question-search',
        tableKey: 'oilAudit-oilAudit-question-list',
        searchMore: true,
      }
    },
    created() {
      // this.fetchData()
      // this.initTable()
      // this.searchNow = this.getFiled()
      // this.searchItem = this.searchNow.slice(0, 4)
      // this.initSearch()
    },
    methods: {
      getFiled() {
        return [
          { name: '批次', key: 'pc' },
          { name: '项目名称', key: 'projectname' },
          { name: '被审计单位名称', key: 'auditorgname' },
        ]
      },
      resetQueryForm() {
        this.queryForm = {
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
            tlist, totalRecord
          }
        } = await ywtsjGetlist(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      // handleAdd() {
      //   this.$refs['edit'].showEdit('add', null)
      // },
      async handleDetail(row) {
        // const data = await dgDetail({ sheetid: Number(row.sheetId) })
        await this.$refs['edit'].showEdit('detail', row)
      },
      // async handleEdit(row) {
      //   const data = await dgDetail({ sheetid: row.sheetid })
      //   await this.$refs['edit'].showEdit('edit', data.data)
      // },
      // handleDelete(row) {
      //   this.$baseConfirm('你确定要删除当前项吗', null, async () => {
      //     const { msg, code } = await riskDel({
      //       riskid: row.riskid,
      //     })
      //     if (code == 0) {
      //       this.$baseMessage(msg, 'success')
      //     } else {
      //       this.$baseMessage(msg, 'error')
      //     }
      //     await this.fetchData()
      //   })
      // },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleObject() {
        this.$refs['audiTree'].showEdit()
      },
      getChildlistObj(val, flag) {
        if (flag == 'right') {
          this.pdDx = 'yh'
          this.$set(this.queryForm, 'orgName', val[0].realname)
          this.$set(this.queryForm, 'auditStaffId', val[0].staffid)
          this.$set(this.queryForm, 'auditOrgId', '')
        } else {
          this.pdDx = 'bm'
          this.$set(this.queryForm, 'auditOrgId', val.id)
          this.$set(this.queryForm, 'orgName', val.name)
          this.$set(this.queryForm, 'auditStaffId', '')
        }
        this.$forceUpdate()
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
