<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <el-card shadow="never" class="secondCard">
          <vab-query-form-right-panel class="option-row">
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
          </vab-query-form-right-panel>
          <el-table :data="list" v-loading="listLoading">
            <el-table-column label="ID" width="50">
              <template slot-scope="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                label="名称"
                prop="name"
                show-overflow-tooltip
                v-if="item.name === '名称'"
              />
              <el-table-column
                label="描述"
                prop="des"
                show-overflow-tooltip
                v-if="item.name === '描述'"
              />
            </div>
            <el-table-column label="操作" width="180">
              <template slot-scope="scope">
                <el-button
                  type="text"
                  @click="handleDownload(scope.row)"
                  v-if="hasAuth('XTMBGLdown')"
                >
                  下载
                </el-button>
                <el-button
                  type="text"
                  @click="handleExplain(scope.row)"
                  v-if="hasAuth('XTMBGLexplanin')"
                >
                  模板说明
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { hasAuth } from '@/utils'
  export default {
    name: 'Download',
    components: {
      filterSearch,
      filterTable,
    },
    data() {
      return {
        list1: [
          {
            name: '法律法规导入程序',
            des: '将企业外规数据导入应用程序中',
            pdf: '法律法规',
            xls: '内部控制（法律规章）导入模板',
          },
          {
            name: '管理制度导入程序',
            des: '将企业内部规定数据导入应用程序中',
            pdf: '管理制度',
            xls: '内部控制（规章制度）导入模板',
          },
          {
            name: '流程基本信息导入程序',
            des: '将流程基本信息导入程序',
            pdf: '流程基本信息',
            xls: '内部控制（基本信息）导入模板',
          },
          {
            name: '流程法律法规导入程序',
            des: '将流程法律法规及规章制度导入应用程序中',
            pdf: '流程法律法规',
            xls: '内部控制（内外规）导入模板',
          },
          {
            name: '流程控制矩阵导入程序',
            des: '将流程控制矩阵导入应用程序中',
            pdf: '流程控制矩阵',
            xls: '内部控制（控制矩阵）导入模板',
          },
          {
            name: '项目资料导入程序',
            des: '将项目资料导入应用程序中',
            pdf: '项目资料',
            xls: '内部审计（项目资料准备信息）导入模板',
          },
          {
            name: '组织架构导入程序',
            des: '将相关组织导入应用程序中',
            pdf: '组织架构',
            xls: '组织架构导入模板',
          },
          {
            name: '基础数据导入程序',
            des: '将相关基础数据导入应用程序中',
            pdf: '基础数据',
            xls: '基础数据导入模板',
          },
          {
            name: '人员信息导入程序',
            des: '将人员信息导入应用程序中',
            pdf: '人员信息',
            xls: '人员信息导入模板',
          },
        ],
        listLoading: false,
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-template-download-search',
        tableKey: 'setting-template-download-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [{ name: '名称' }, { name: '描述' }], //所有表格项
        filedNow: [],
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
      async fetchData() {
        this.listLoading = true
        this.list = this.list1
        this.listLoading = false
      },
      handleExplain(row) {
        const url = `./files/${row.pdf}.pdf`
        window.open(url, '_blank')
      },
      async handleDownload(row) {
        const url = `./files/${row.xls}.xls`
        window.location.href = url
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '流程编号', key: 'flownumber' },
          { name: '流程名称', key: 'flowname' },
          { name: '责任部门', key: 'departincharge' },
        ]
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
  }

  .lr-layout > .left {
    width: 250px;
    overflow: hidden;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 20px 20px 20px;
    background: #ffffff;
  }
  .right {
    flex: 1;
  }
  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }
</style>
