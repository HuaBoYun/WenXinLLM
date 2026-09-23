<template>
  <div class="system-log-container">
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
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
                v-model="queryForm.realName"
                clearable
                placeholder="名称"
                v-if="item.name === '名称'"
              />
              <el-input
                v-model="queryForm.education"
                clearable
                placeholder="学历"
                v-if="item.name === '学历'"
              />
              <el-input
                v-model="queryForm.major"
                clearable
                placeholder="专业"
                v-if="item.name === '专业'"
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
            <el-form-item style="cursor: pointer">
              <span
                :class="searchMore ? 'search-more is-opened' : 'search-more'"
                @click="showMore"
              >
                <span>{{ searchMore ? '收起' : '展开' }}</span>
                <i class="el-icon-arrow-down"></i>
              </span>
            </el-form-item>
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

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
          <el-button type="success" @click="handleEdit(false, '新建')">
            新建
          </el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          align="center"
          label="人员名称"
          width="100"
          prop="realname"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <el-button @click="handleEdit(scope.row, '查看')" type="text">
              {{ scope.row.realname }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
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
            width="100"
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
            width="100"
            v-model="queryForm.education"
          />
          <el-table-column
            align="center"
            label="专业"
            v-if="item.name === '专业'"
            prop="major"
            v-model="queryForm.major"
          />
          <!-- <el-table-column
            align="center"
            label="状态"
            v-if="item.name === '状态'"
            prop="status"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              {{ statusName[row.aprStatus] || '未审核' }}
            </template>
          </el-table-column> -->
        </div>
        <el-table-column align="center" label="操作" show-overflow-tooltip>
          <template slot-scope="scope">
            <!-- <el-button
              :disabled="scope.row.aprStatus"
              type="text"
              @click="handlePersonShenPi(scope.row)"
            >
              提交审批
            </el-button> -->
            <el-button
              :disabled="scope.row.aprStatus"
              type="text"
              @click="handleEdit(scope.row, '修改')"
            >
              修改
            </el-button>

            <el-button
              :disabled="scope.row.aprStatus"
              type="text"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
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
    <Personnel ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" />
  </div>
</template>
<script>
  import {
    getStaffPageList,
    personDelete,
    personShenPi,
  } from '@/api/setting/personnel'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import Personnel from './components/personnelEdit.vue'
  import { searchTableMixis } from '@/mixis/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'

  export default {
    mixins: [searchTableMixis],
    components: { filterSearch, filterTable, Personnel, ProcessList },
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          realName: '',
          education: '',
          // jobExperiences: '',
          // jobName: '',
          major: '',
          pageNumber: 1,
          pageSize: 20,
        },
        list: [],
        listLoading: true,
        statusName: [
          '未审批',
          '审批中',
          '已退回',
          '已撤回',
          '已终止',
          '',
          '已完成',
        ],
        // 筛选列表配置
        filedAll: [
          { name: '性别' },
          { name: '出生年月' },
          { name: '政治面貌' },
          { name: '学历/学位' },
          { name: '专业' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-structure-personnel-search',
        tableKey: 'audit-structure-personnel-list',
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
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      getFiled() {
        return [
          { name: '名称', key: 'realName' },
          { name: '学历', key: 'education' },
          { name: '专业', key: 'major' },
        ]
      },
      handleDetail(row) {},

      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true

        const orgid = JSON.parse(localStorage.getItem('userInfo')).currentOrg
          .orgid

        this.queryForm.orgId = orgid

        const {
          data: { tlist: list, totalRecord: total },
        } = await getStaffPageList(this.queryForm)

        list.forEach((element) => {
          if (element.birthday) {
            element.birthday = element.birthday.split('T')[0]
          }
          if (element.worktime) {
            element.worktime = element.worktime.split('T')[0]
          }
        })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled, this.planNum)
      },
      handlePersonShenPi(row) {
        const tableId = 14
        const fromId = row.staffid
        this.$refs['process'].save(tableId, fromId)
        // if (this.statusName[row.aprStatus] != '未审批' && row.aprStatus) {
        //   this.$baseMessage('流程进行中', 'error')
        // } else {
        //   this.$refs['process'].show(row, 14)
        // }
        // personShenPi({
        //   staffid: row.staffid,
        // }).then((res) => {
        //   if (res.msg === '成功') {
        //     this.$baseMessage('操作成功', 'success')
        //     this.fetchData()
        //   }
        // })
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
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
      showGroupLeader(sIndex) {
        this.sIndex = sIndex
        this.$refs['select'].showEdit('leader')
      },
      showTeamMembers(sIndex) {
        this.sIndex = sIndex
        this.$refs['select'].showEdit('members')
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
