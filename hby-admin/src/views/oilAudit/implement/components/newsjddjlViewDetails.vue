<template>
  <el-dialog
    v-if="dialogFormVisible"
    :append-to-body="true"
    title="详情"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <el-card shadow="never" class="secondCard">
            <el-table
                v-if="type == '31'"
                v-loading="listLoading"
                ref="multipleTable"
                :data="list"
                tooltip-effect="dark" 
                style="width: 100%"
              >
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="createDraft(row)">
                    审查
                  </el-button>
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
                </template>
              </el-table-column>
              </el-table>
          </el-card>
        </div>
      </div>
      <!-- 审查 -->
      <newscView ref="sc" @queryData="queryData" />
    </div>
  </el-dialog>
  
</template>

<script>
  import { getDetail, getList } from '@/oapi/audit/task'
  import { formatDate } from '@/utils/index'
  import newscView from './newscView'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { currSsProject  } from '@/oapi/audit/projectData'
  import {  getgcmyrwListnew,getJsxmmyrwlistnew} from '@/oapi/audit/project'

  export default {
    name: 'newsjddjlViewDetails',
    components: {
      newscView,
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
        dialogFormVisible: false,
      }
    },
    created() {
      
    },
    methods: {
      // row 审计督导跳转用
      showEdit(row) {
        this.dialogFormVisible = true
        if(row && row.gljhxmlx) {   
          this.$nextTick(async () => {
            this.type = row.gljhxmlx 
            if(row.gljhxmlx == '31'){ 
              this.listLoading = true
              const res  = await getgcmyrwListnew({id:row.gljhxmid})
              this.list = res.data
            }else{ 
              const res  = await getJsxmmyrwlistnew({id:row.gljhxmid})
              this.list = res.data
            }
            this.listLoading = false
          })
        } else {
          this.fetchData()
        }
        this.initTable()
        this.searchNow = this.getFiled()
        this.searchItem = this.searchNow.slice(0, 4)
        this.initSearch()
      },
      close() {
        this.dialogFormVisible = false
      },
      getFiled() {
        return [{ name: '合同编号', key: 'businessType' }]
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
         this.type = data.pj.xmqd.gljhxmlx 
         if(data.pj.xmqd.gljhxmlx == '31'){ 
           this.listLoading = true
          const res  = await getgcmyrwListnew({id:data.pj.xmqd.gljhxmid})
          this.list = res.data
          
         }else{ 
          const res  = await getJsxmmyrwlistnew({id:data.pj.xmqd.gljhxmid})
          this.list = res.data
         }
        this.listLoading = false
      },
      createDraft(row) {
        this.$refs['sc'].showEdit({projectId:this.pj.id,projecttempId:this.pj.projecttempId,templateId:this.type=='32'?row.jsxmtzwcqkid:row.gcxmzjzjbid})
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
