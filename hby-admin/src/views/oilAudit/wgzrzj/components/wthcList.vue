<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    title="问题线索核查"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item v-for="(item, index) in searchItem" :key="index">
            <el-input
              v-if="item.name === '名称'"
              v-model="queryForm.hcname"
              placeholder="名称"
            ></el-input>
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
      </vab-query-form-top-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>

      <el-table-column
        align="center"
        label="编号"
        prop="hcnumber"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="名称"
        prop="hcname"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit('详情', row)">
            {{ row.hcname }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="填报单位"
        prop="editorgname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="编制时间"
        prop="edittime"
        show-overflow-tooltip
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
    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="save">确定</el-button>
    </div>
    <wthcView ref="edit" @fetchData="fetchData" />
  </el-dialog>
</template>
<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import wthcView from './wthcView.vue'
  import { wthcList, wthcDelete } from '@/oapi/audit/wgzrzj.js'
  export default {
    components: { wthcView, filterSearch, filterTable },
    name: 'wthc',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          hcname: '',
          pageNumber: 1,
          pageSize: 20,
          status: 6,
          isuse: 0,
        },
        listLoading: false,
        list: [],
        filedAll: [
          { name: '编号' },
          { name: '名称' },
          { name: '填报单位' },
          { name: '编制时间' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-wgzrzj-wthc-search',
        tableKey: 'oilAudit-wgzrzj-wthc-list',
        dialogFormVisible: false,
        multipleSelection: [],
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    mounted() {},
    methods: {
      show() {
        this.dialogFormVisible = true
        this.fetchData()
      },
      close() {
        this.dialogFormVisible = false
        this.queryForm = {
          slname: '',
          pageNumber: 1,
          pageSize: 20,
          status: 6,
          isuse: 0,
        }
      },
      handleSelectionChange(val) {
        // 确保每次只选择一个选项
        if (val.length > 1) {
          // 如果选择了多个，只保留最后一个选择的
          const lastSelected = val[val.length - 1]
          this.$refs.multipleTable.clearSelection()
          this.$refs.multipleTable.toggleRowSelection(lastSelected, true)
          this.multipleSelection = [lastSelected]
        } else {
          this.multipleSelection = val
        }
      },
      save() {
        this.$emit('submit', this.multipleSelection)
        this.close()
      },
      getFiled() {
        return [{ name: '名称', key: 'hcname' }]
      },
      // 动态表格开始
      initTable() {
        this.loading = true
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.tableKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.filedNow = tempArr
          } else {
            this.filedNow = this.filedAll
          }
          this.loading = false
        })
      },
      initSearch() {
        let self = this
        this.$nextTick(function () {
          let data = localStorage.getItem(self.localKey)
          if (data) {
            data = JSON.parse(data)
            let tempArr = []
            for (let i = 0; i < data.length; i++) {
              if (data[i].show) {
                tempArr.push(data[i])
              }
            }
            this.searchNow = tempArr
          } else {
            this.searchNow = this.searchAll
          }

          // 重置非展示搜索项
          this.searchAll.forEach((x) => {
            if (!this.searchNow.some((y) => y.key === x.key)) {
              if (Array.isArray(this.queryForm[x.key])) {
                this.queryForm[x.key] = []
              } else if (this.queryForm[x.key] instanceof Object) {
                this.queryForm[x.key] = {}
              } else {
                this.queryForm[x.key] = null
              }
            }
          })
          if (this.searchMore) {
            this.searchItem = this.searchNow
          } else {
            this.searchItem = this.searchNow.slice(0, 4)
          }
        })
      },
      handleEdit(name, row) {
        this.$refs.edit.show(name, row)
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await wthcList({ ...this.queryForm })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          hcname: '',
          pageNumber: 1,
          pageSize: 20,
          status: 6,
          isuse: 0,
        }
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await wthcDelete({
            id: row.id,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
    },
  }
</script>
