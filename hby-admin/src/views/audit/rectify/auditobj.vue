<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form>
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
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column width="1" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="人员名称"
            v-if="item.name === '人员名称'"
            
            prop="realname"
            show-overflow-tooltip
          >
            <!-- <template slot-scope="scope">
              <el-button @click="handleEdit(scope.row, '查看')" type="text">
                {{ scope.row.realname }}
              </el-button>
            </template> -->
          </el-table-column>
          <el-table-column
            align="center"
            label="性别"
            v-if="item.name === '性别'"
            prop="gender"
            v-model="queryForm.gender"
          >
            <template slot-scope="scope">
              {{ scope.row.gender === '1' ? '男' : '女' }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            
            label="出生年月"
            v-if="item.name === '出生年月'"
            prop="birthday"
            v-model="queryForm.birthday"
          />
          <el-table-column
            align="center"
            label="政治面貌"
            v-if="item.name === '政治面貌'"
            prop="politicaloutlook"
            v-model="queryForm.politicaloutlook"
          />
          <el-table-column
            align="center"
            label="学历/学位"
            v-if="item.name === '学历/学位'"
            prop="education"
            
            v-model="queryForm.education"
          />
          <el-table-column
            align="center"
            label="专业"
            v-if="item.name === '专业'"
            prop="major"
            v-model="queryForm.major"
          />
        </div>
        <el-table-column width="1" />
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
    <!-- <Personnel ref="edit" /> -->
  </div>
</template>
<script>
  import {
    getObjectPageList,
    personDelete,
    personShenPi,
  } from '@/api/setting/personnel'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  // import Personnel from './components/personnelEdit.vue'
  export default {
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          realName: '',
          education: '',
          jobExperiences: '',
          jobName: '',
          major: '',
          pageNumber: 1,
          pageSize: 20,
        },
        list: [],
        listLoading: true,
        statusName: [
          '未审批',
          '审批中',
          '需调整',
          '已通过',
          '已终止',
          '',
          '已完成',
        ],
        // 筛选列表配置
        filedAll: [
          { name: '人员名称' },
          { name: '性别' },
          { name: '出生年月' },
          { name: '政治面貌' },
          { name: '学历/学位' },
          { name: '专业' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-rectify-suditobj-search',
        tableKey: 'audit-rectify-suditobj-list',
        searchMore: true,
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
        return []
      },
      handleDetail(row) {
        console.log('handleDetail', row)
      },

      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true

        const orgid = JSON.parse(localStorage.getItem('userInfo')).linkDetp
          .orgid

        this.queryForm.orgId = ''
        // this.queryForm.orgId = orgid

        const res = await getObjectPageList(this.queryForm)

        res.data.tlist.forEach((element) => {
          if (element.birthday) {
            element.birthday = element.birthday.split('T')[0]
          }
          if (element.worktime) {
            element.worktime = element.worktime.split('T')[0]
          }
        })
        this.list = res.data.tlist
        this.total = res.data.totalRecord
        this.listLoading = false
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handlePersonShenPi(row) {
        personShenPi({
          staffid: row.staffid,
        }).then((res) => {
          if (res.msg === '成功') {
            this.$baseMessage('操作成功', 'success')
            this.fetchData()
          }
        })
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await personDelete({ staffId: row.staffid })
          if (code === 1) {
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
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
    background: #f6f8f9 !important;
  }
  .margin-b0 {
    margin-bottom: 0;
  }
  ::v-deep .is-never-shadow {
    // margin: -26px;
  }
  // ::v-deep .el-form-item__content {
  //   height: 30px;
  // }
</style>