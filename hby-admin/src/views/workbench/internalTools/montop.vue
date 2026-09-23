<template>
  <div class="system-log-container">
    <vab-query-form>
      <el-card shadow="never">
        <vab-query-form-left-panel :span="24">
          <el-form
            ref="form"
            checkable
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              v-for="(item, index) in searchItem"
              :key="index"
              :prop="item.key"
            >
              <el-input
                v-model="queryForm.weeklytitle"
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
              <el-button @click="resetSearch" type="primary">重置</el-button>
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
            <el-form-item>
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-left-panel>
      </el-card>
    </vab-query-form>

    <el-card shadow="never" class="secondCard">
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
            <!-- <i class="el-icon-delete" slot="reference"></i> -->
            <el-button
              slot="reference"
              icon="el-icon-s-grid"
              class="biaoge"
              style="margin-bottom: 10px; margin-right: 10px"
            ></el-button>
          </el-popover>
        </el-tooltip>
        <el-button type="success" @click="handleEdit('add', null)">
          新建
        </el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="标题" prop="weeklytitle">
          <template #default="{ row }">
            <el-button type="text" @click="$refs.read.show(row.id)">
              {{ row.weeklytitle }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column width="1" />

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="简介"
            prop="memo"
            v-if="item.name === '简介'"
          />
          <el-table-column
            align="center"
            label="生效日期"
            prop="effdate"
            v-if="item.name === '生效日期'"
          />
          <el-table-column
            align="center"
            label="生效状态"
            prop="effstatus"
            v-if="item.name === '生效状态'"
          >
            <template #default="{ row }">
              {{ row.effstatus ? '生效' : '未生效' }}
            </template>
          </el-table-column>
        </div>

        <el-table-column align="center" label="操作">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit('edit', row)"
              :disabled="createId != row.creator"
            >
              修改
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              :disabled="createId != row.creator"
            >
              删除
            </el-button>
            <el-button
              type="text"
              v-if="!!row.effstatus"
              @click="handleStatus(row)"
            >
              失效
            </el-button>
            <el-button type="text" v-else @click="handleStatus(row)">
              生效
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <comweeklyView ref="edit" @fetchData="fetchData" />
    <readView ref="read" />
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import readView from './components/ReadView.vue'

  import {
    getComplianceWeenlyList,
    complianceWeenlyDel,
    complianceWeenlySave,
  } from '@/api/fwgl/hgzk'
  import comweeklyView from './components/comweeklyView.vue'
  export default {
    name: 'zxflfwList',
    components: { filterTable, filterSearch, comweeklyView, readView },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          lytype: 4,
          pageNumber: 1,
          pageSize: 20,
          weeklytitle: '',
        },
        filedAll: [
          { name: '标题' },
          { name: '生效日期' },
          { name: '简介' },
          { name: '生效状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-flfw-khtz-search',
        tableKey: 'fwgl-flfw-khtz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        isHeadquartersLegal: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.initData()
    },
    //离开页面清空localStorage.setItem('workbench')
    beforeRouteLeave(to, from, next) {
      localStorage.removeItem('weekly')
      next()
    },
    methods: {
      initData() {
        this.initTable() //初始化表格
        this.searchNow = this.getFiled()
        this.searchItem = this.searchNow.slice(0, 4)
        this.initSearch()
        this.fetchData()
      },
      resetSearch() {
        this.queryForm = {
          lytype: 4,
          pageNumber: 1,
          pageSize: 20,
          weeklytitle: '',
        }
        this.fetchData()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [{ name: '标题', key: 'weeklytitle' }]
        return fields
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getComplianceWeenlyList(this.queryForm)
        this.listLoading = false
        this.list = tlist.map((v) => {
          if (v.effdate) {
            v.effdate = v.effdate.split(' ')[0]
          }
          return v
        })
        this.total = totalRecord
      },
      async handleEdit(type, row) {
        // if (type == 'edit') {
        //   if (this.createId != row.creator) {
        //     return this.$message.error('只有创建人可以操作')
        //   }
        // }
        await this.$refs['edit'].showEdit(type, row, 4)
      },
      handleDelete(row) {
        // if (this.createId != row.creator) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await complianceWeenlyDel({ id: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleStatus(row) {
        this.$baseConfirm('你确定要修改当前状态吗', null, async () => {
          let params = {
            id: row.id,
            effstatus: row.effstatus == 0 ? 1 : 0,
          }
          const { msg } = await complianceWeenlySave(params)
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }
</style>
