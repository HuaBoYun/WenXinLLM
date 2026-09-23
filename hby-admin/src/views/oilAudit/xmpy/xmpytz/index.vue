<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-top-panel :span="24">
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.noticeTitle"
                clearable
                placeholder="标题"
                v-if="item.name === '标题'"
              />
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
              <el-button
                native-type="submit"
                type="primary"
                @click="resetSearch"
              >
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
      </el-card>
    </vab-query-form>

    <el-card shadow="never">
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
        <el-button type="success" @click="handleAdd">新建</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="标题"
          prop="noticeTitle"
          width="100"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.noticeTitle }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="内容"
            v-if="item.name === '内容'"
            prop="noticeContent"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="创建时间"
            v-if="item.name === '创建时间'"
            prop="createdTime"
            show-overflow-tooltip
          />
        </div>

        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
            <el-button type="text" @click="xiafa(row)">下发</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <Edit ref="edit" @fetch-data="fetchData" />
    <selectPeopels ref="people" @projectManage="selectPerson" />
  </div>
</template>

<script>
  import { xmpytzList, xmpytzDelete, xiafaTZ } from '@/oapi/audit/xmpy'
  import Edit from '@/views/oilAudit/xmpy/xmpytz/edit.vue'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import selectPeopels from '@/components/selectPerson.vue'

  export default {
    name: 'xmpytz',
    components: { Edit, filterSearch, filterTable, selectPeopels },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          noticeTitle: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [{ name: '内容' }, { name: '创建时间' }], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-xmpy-xmpytz-search',
        tableKey: 'oilAudit-xmpy-xmpytz-list',
        searchMore: true,
        projectNoticeId: '',
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getFiled() {
        return [{ name: '标题', key: 'noticeTitle' }]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          noticeTitle: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
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
        // this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await xmpytzList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        // this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit('detail', row)
      },
      async handleEdit(row) {
        await this.$refs['edit'].showEdit('edit', row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await xmpytzDelete({
            id: row.id,
          })
          if (code == 200) {
            this.$baseMessage(msg, 'success')
            this.fetchData()
          }
        })
      },
      // async handleExport(row) {
      //   const data = await reportExport({ jhyjgid: row.jhyjgid })
      //   let fileName = 'test'
      //   let blob = new Blob([data], {
      //     type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      //   })
      //   if (window.navigator.msSaveOrOpenBlob) {
      //     navigator.msSaveBlob(blob, fileName)
      //   } else {
      //     let link = document.createElement('a')
      //     link.href = window.URL.createObjectURL(blob)
      //     link.download = fileName
      //     link.click()
      //     // 释放内存
      //     window.URL.revokeObjectURL(link.href)
      //   }
      // },
      xiafa(row) {
        this.$refs['people'].showEdit()
        this.projectNoticeId = row.id
      },
      selectPerson(val) {
        const arr = val.map((res) => {
          return {
            distributeId: res.staffid,
            projectNoticeId: this.projectNoticeId,
          }
        })

        xiafaTZ(arr).then((res) => {
          if (res.code == 200) {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
          }
        })
      },
    },
  }
</script>

<style scoped>
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 100%;
  }
</style>
