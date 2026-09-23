<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="right">
        <!-- <vab-query-form>
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
                    v-model="queryForm.businessType"
                    clearable
                    placeholder="合同编号"
                    v-if="item.name === '合同编号'"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    native-type="submit"
                    type="primary"
                    @click="queryData"
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
        </vab-query-form> -->

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
                <el-button
                  slot="reference"
                  icon="el-icon-s-grid"
                  class="biaoge"
                  style="margin-bottom: 10px; margin-right: 10px"
                ></el-button>
              </el-popover>
            </el-tooltip>
            <!-- <el-button type="success" @click="handleAddOrUpdate()">
              新建
            </el-button> -->
          </vab-query-form-right-panel>
          <el-table
              v-if="type == '31'"
              v-loading="listLoading"
              ref="multipleTable"
              :data="list"
              tooltip-effect="dark" 
              style="width: 100%"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                label="合同编号"
                prop="tblYqnsGcxmzj.htbh"
              ></el-table-column>
              <el-table-column
                prop="tblYqnsGcxmzj.gcmc"
                label="工程名称"
              ></el-table-column>
              <el-table-column
                prop="tblYqnsGcxmzj.sgdw"
                label="实施单位"
              ></el-table-column>

              <el-table-column
                prop="tblYqnsGcxmzj.esscje"
                label="金额"
              ></el-table-column> 
              <el-table-column prop="rwnames" label="人员"></el-table-column>
              <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
            >
              <template #default="{ row }">
                <el-button type="text" @click="createDraft(row)">
                  审查
                </el-button>
                <!-- <el-button type="text" @click="checkRelate(row)">
                  审减内容
                </el-button>  -->
              </template>
            </el-table-column>
            </el-table>
            <el-table
              v-else
              v-loading="listLoading"
              ref="multipleTable"
              :data="list"
              tooltip-effect="dark" 
              style="width: 100%"
            >
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column
                label="合同编号"
                prop="htbh"
              ></el-table-column>
              <el-table-column
                prop="gchfymc"
                label="工程名称"
              ></el-table-column>
              <el-table-column
                prop="ssdw"
                label="实施单位"
              ></el-table-column>

              <el-table-column
                prop="htje"
                label="金额"
              ></el-table-column> 
              <el-table-column prop="rwnames" label="人员"></el-table-column>
              <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
            >
              <template #default="{ row }">
                <el-button type="text" @click="createDraft(row)">
                  审查
                </el-button>
                <!-- <el-button type="text" @click="checkRelate(row)">
                  审减内容
                </el-button>  -->
              </template>
            </el-table-column>
            </el-table>
          <!-- <el-table v-loading="listLoading" :data="list">
            <el-table-column
              align="center"
              label="合同编号"
              prop="businessType"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleRecordList(row)">
                  {{ row.businessType }}
                </el-button>
              </template>
            </el-table-column>
            <div
              v-for="(item, index) in filedNow"
              :key="item.name + '_' + index"
            >
              <el-table-column
                align="center"
                label="工程名称"
                prop="riskPoint"
                show-overflow-tooltip
                v-if="item.name === '工程名称'"
              />
              <el-table-column
                align="center"
                label="施工单位"
                prop="suditProcess"
                show-overflow-tooltip
                v-if="item.name === '施工单位'"
              />
              <el-table-column
                align="center"
                label="二审审查金额（元）"
                prop="finishtime"
                show-overflow-tooltip
                v-if="item.name === '二审审查金额（元）'"
              />
              <el-table-column
                align="center"
                label="本次审计人员"
                prop="finishtime1"
                show-overflow-tooltip
                v-if="item.name === '本次审计人员'"
              />
              <el-table-column
                align="center"
                label="审计专业科室人员"
                prop="finishtime2"
                show-overflow-tooltip
                v-if="item.name === '审计专业科室人员'"
              />
            </div> -->
            <!-- <el-table-column
            align="center"
            label="状态"
            prop="finish"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ row.finish === 1 ? '已完成' : '未完成' }}
            </template>
          </el-table-column> -->
            <!-- <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="220"
            >
              <template #default="{ row }">
                <el-button type="text" @click="createDraft(row)">
                  审查
                </el-button>
                <el-button type="text" @click="checkRelate(row)">
                  审减内容
                </el-button> 
              </template>
            </el-table-column>
          </el-table> -->
        </el-card>

        <!-- <el-pagination
          class="pagination"
          background
          :current-page="queryForm.pageNumber"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        /> -->
      </div>
    </div>
    <!-- <TaskTree ref="edit" @fetch-data="fetchData" /> -->
    <record-list-info ref="recordListInfo" />
    <myDraftInfo ref="draft" />
    <relateDraftModalList ref="modal" />
    <!-- 审查 -->
    <scView ref="sc" @queryData="queryData" />
    <!-- 审减内容 -->
    <sjView ref="sj" />
  </div>
</template>

<script>
  import { getDetail, getList } from '@/oapi/audit/task'
  import { formatDate } from '@/utils/index'
  // import DraftManageInfo from './components/myDraftInfo'
  import myDraftInfo from './components/myDraftInfo'
  import relateDraftModalList from './components/options/relateDraftModal.vue'
  import RecordListInfo from './components/RecordListInfo'
  import TaskTree from './components/TaskTree'
  import scView from './components/scView'
  import sjView from './components/sjView'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { currSsProject  } from '@/oapi/audit/projectData'
  import {  getgcmyrwList,getJsxmmyrwlist} from '@/oapi/audit/project'

  export default {
    name: 'Download',
    components: {
      TaskTree,
      RecordListInfo,
      // DraftManageInfo
      myDraftInfo,
      relateDraftModalList,
      scView,
      sjView,
      filterSearch,
      filterTable,
    },
    mixins: [searchTableMixis],
    props: {
      isShow: {
        //项目查看传参
        type: Boolean,
        default: true,
      },
      row: {
        type: Object,
        default: () => {},
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          businessType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          {
            name: '工程名称',
          },
          {
            name: '施工单位',
          },
          {
            name: '二审审查金额（元）',
          },
          {
            name: '本次审计人员',
          },
          {
            name: '审计专业科室人员',
          },
        ], //所有表格项
        filedNow: [],
        tableKey: 'oilAudit-sjss-wdrw-list',
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-prepare-projectData-search',
        searchMore: true,
        type:'',
        pj:null,
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
        return 
        // [{ name: '合同编号', key: 'businessType' }]
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      // 动态表格开始
      initTable() {
        this.$nextTick(() => {
          let data = localStorage.getItem(this.tableKey)
          if (data) {
            data = JSON.parse(data)
            this.filedNow = data.filter((item) => item.show)
          } else {
            this.filedNow = this.filedAll
          }
        })
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.queryData()
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
         const { data } = await currSsProject() 
         this.pj = data.pj
         this.type = data.pj.xmqd ? data.pj.xmqd.gljhxmlx : ''
         const gljhxmid = data.pj.xmqd ? data.pj.xmqd.gljhxmid : ''
         if(this.type == '31'){ 
           this.listLoading = true
          const res  = await getgcmyrwList({id: gljhxmid})
          this.list = res.data
          
         }else{ 
          const res  = await getJsxmmyrwlist({id: gljhxmid})
          this.list = res.data
         }
        // this.listLoading = true
        // const {
        //   data: { tlist, totalRecord: total },
        // } = await getList(this.queryForm)
        // this.list = tlist
        // this.list = [
        //   {
        //     projectId: 852,
        //     businessType: '假数据-合同编号',
        //     riskPoint: '假数据-工程名称',
        //     suditProcess: '假数据-施工单位',
        //     finishtime: '32544.32',
        //     finishtime1: '假数据-本次审计人员',
        //     finishtime2: '假数据-审计专业科室人员',
        //   },
        // ]
        // this.total = total
        this.listLoading = false
      },
      handleAddOrUpdate(row) {
        console.log(row)
      },
      createDraft(row) {
        // this.$refs['draft'].showEdit('我的任务', row)
        this.$refs['sc'].showEdit({projectId:this.pj.id,projecttempId:this.pj.projecttempId,templateId:this.type=='32'?row.jsxmtzwcqkid:row.gcxmzjzjbid})
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      async handleRecordList(row) {
        const data = await getDetail({ programid: row.programid })
        this.$refs['recordListInfo'].showEdit(data.data)
      },
      // handleFinal(row) {
      //   this.$baseConfirm('你确定要完成当前项吗', null, async () => {
      //     const { msg, code } = await myTaskFinal({
      //       programId: row.programid,
      //       operateid: row.operateid,
      //     })
      //     if (code === 1) {
      //       this.$baseMessage(msg, 'success')
      //     }
      //     await this.fetchData()
      //   })
      // },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await handleDelete({ ids: row.id })
          if (code == 0) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      checkRelate(row) {
        this.$refs['sc'].showEdit(row, 'sjnr')
      },
    },
  }
</script>
<style scoped>
  /* .lr-layout {
    display: flex;
  } */

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  /* .lr-layout > .right {
    width: calc(100% - 210px);
  } */

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
