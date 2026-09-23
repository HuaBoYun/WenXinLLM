<template>
  <div class="system-log-container">
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
              v-model="queryForm.title"
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
          <el-form-item>
            <el-tooltip
              class="item"
              effect="dark"
              content="搜索筛选"
              placement="top"
            >
              <el-popover placement="left" trigger="click">
                <filter-search
                  v-if="true"
                  :list="searchAll"
                  :name="localKey"
                  @updateSearchShow="initSearch"
                />
                <el-button slot="reference" style="height: 32px">
                  <vab-icon icon="filter" :is-custom-svg="true" />
                </el-button>
              </el-popover>
            </el-tooltip>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-right-panel :span="24">
        <el-tooltip
          class="item"
          effect="dark"
          content="表格筛选"
          placement="top"
        >
          <el-popover placement="right" trigger="click">
            <filter-table
              :list="filedAll"
              :name="tableKey"
              @updateTableShow="initTable"
            />
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleEdit('新增', null)">
          新建
        </el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <div v-for="(item, index) in filedNow" :key="index">
        <el-table-column
          align="center"
          label="编号"
          prop="no"
          show-overflow-tooltip
          v-if="item.name === '编号'"
        />
        <el-table-column
          align="center"
          label="名称"
          prop="title"
          show-overflow-tooltip
          v-if="item.name === '名称'"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit('详情', row)">
              {{ row.title }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="描述"
          prop="rycontent"
          show-overflow-tooltip
          v-if="item.name === '描述'"
        />
      </div>
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="180"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit('编辑', row)">
            修改
          </el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <el-button type="text" @click="xiafa(row)">下发</el-button>
        </template>
      </el-table-column>
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
    <lwhjtzEdit ref="edit" @fetchData="fetchData" />
    <projectManage ref="people" @projectManage="selectPerson" />
  </div>
</template>
<script>
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { getHjtzList, deleteHjtz, dxfryLlyytz } from '@/oapi/audit/lwpy'
  import lwhjtzEdit from './components/lwhjtzEdit.vue'
  import projectManage from '@/components/selectPerson.vue'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    components: { lwhjtzEdit, filterSearch, filterTable, projectManage },
    name: 'wthc',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          title: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        filedAll: [{ name: '编号' }, { name: '名称' }, { name: '描述' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-lwpy-lwhjtz-search',
        tableKey: 'oilAudit-lwpy-lwhjtz-list',
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
      getFiled() {
        return [{ name: '名称', key: 'title' }]
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
        } = await getHjtzList({ ...this.queryForm })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      async handleDetail(a, b) {
        await this.$refs['edit'].showEdit(a, b)
      },
      resetQueryForm() {
        this.queryForm = {
          title: '',
          pageNumber: 1,
          pageSize: 20,
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
          const { msg, code } = await deleteHjtz({
            ryid: row.ryid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          }
          await this.fetchData()
        })
      },
      xiafa(row) {
        this.$refs['people'].showEdit()
        this.ryid = row.ryid
        this.select = [row]
      },
      selectPerson(val) {
        let staffidList = val.map((item) => item.staffid).join(',')
        let realnameList = val.map((item) => item.realname).join(',')

        let params = {
          ryid: this.ryid,
          xfryids: staffidList,
          xfrynames: realnameList,
        }
        dxfryLlyytz(params).then((res) => {
          if (res.code == 1) {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
          }
        })
        this.xiafa1(val)
      },
      xiafa1(val) {
        const ids = this.select.map((res) => res.ryid)
        const titles = this.select.map((res) => res.title)
        const names = val.map((res) => res.staffid)
        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }

        //下发通知
        xiafaListNew({
          tableId: '80051',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            // this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.multipleSelection = []
          }
        })
      },
    },
  }
</script>
