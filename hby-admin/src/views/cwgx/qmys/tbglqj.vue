<!-- 收入分摊规则 -->
<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left">
        <czggTree @iconClick="iconClick" />
      </div>
      <div class="right">
        <el-card shadow="never">
          <vab-query-form>
            <vab-query-form-left-panel>
              <span></span>
            </vab-query-form-left-panel>
            <vab-query-form-right-panel>
              <!-- <el-tooltip
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
              </el-tooltip> -->
              <el-button type="success" v-if="showEdit">保存</el-button>
              <el-button v-if="showEdit">取消</el-button>
              <!-- <el-button type="success" @click="handleSend()">下发</el-button>
              <el-button type="danger" @click="handleBack()">撤回</el-button> -->
            </vab-query-form-right-panel>
            <el-form :model="queryForm" label-width="100px">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="名称">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入名称"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="所属分类">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入所属分类"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建系统">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入创建系统"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="创建组织">
                    <el-input
                      v-model="queryForm.name"
                      placeholder="请输入创建组织"
                      v-if="showEdit"
                    />
                    <span v-else></span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </vab-query-form>
        </el-card>
      </div>
    </div>
    <fyhjrjgBaseEdit ref="fyhjrjgBaseEdit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    handleTabs,
    noticeCancel,
    reportExport,
    distributionBack,
  } from '@/oapi/audit/preparation'
  import {
    proposalNoticeList,
    proposalNoticeDelete,
  } from '@/api/monitor/question'
  import { getFlowPkInfo } from '@/api/contract/manage'
  // import fyhjrjgBaseEdit from './components/fyhjrjgBaseEdit.vue'
  import czggTree from './components/czggTree.vue'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'srftgz',
    components: {
      // fyhjrjgBaseEdit,
      czggTree,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          name: '',
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
        options: [
          {
            value: '选项1',
            label: '开启',
          },
          {
            value: '选项2',
            label: '关闭',
          },
        ],
        filedAll: [
          { name: '序号' },
          { name: '分摊方式' },
          { name: '分摊比例' },
          { name: '起始日期' },
          { name: '分摊期数' },
          { name: '均摊计算方式' },
          { name: '截止日期' },
          { name: '说明' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'cwgl-srgl-srftgz-search',
        tableKey: 'cwgl-srgl-srftgz-list',
        searchMore: false,
        showEdit: false,
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
      getFiled() {
        return [
          { name: '分摊比例', key: 'name' },
          { name: '编码', key: 'code' },
          { name: '启用状态', key: 'type' },
        ]
      },

      async fetchData() {
        this.listLoading = false
        const { date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (date) {
          startDate = date[0]
          endDate = date[1]
        }
        const {
          data: { tlist, totalRecord },
        } = await proposalNoticeList({ ...other, startDate, endDate })
        this.listLoading = false
        this.total = totalRecord
        // this.list = tlist.map((item) => ({
        //   ...item,
        //   createUser: item.createUser ? item.createUser.realname : "",
        // }));
      },
      iconClick() {
        this.showEdit = true
      },
    },
  }
</script>

<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
    height: 80vh;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    min-width: 250px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
    height: 80vh;
  }

  .lr-layout > .right {
    width: 80%;
    height: 80vh;
  }
</style>
