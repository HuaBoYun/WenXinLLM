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
                v-model="queryForm.levelName"
                clearable
                placeholder="密级名称"
                v-if="item.name === '密级名称'"
              />
              <el-select
                v-model="queryForm.levelType"
                placeholder="密级类型"
                v-if="item.name === '密级类型'"
              >
                <el-option label="功能模块" :value="1" />
                <el-option label="业务单据" :value="2" />
                <el-option label="人员" :value="3" />
                <el-option label="附件" :value="4" />
              </el-select>
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
      <vab-query-form-right-panel :span="24" style="margin-bottom: 10px">
        <el-button type="success" @click="handleAdd">新增</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="密级名称" prop="levelName" />
        <el-table-column align="center" label="密级类型" prop="levelType">
          <template #default="{ row }">
            {{ type[row.levelType] }}
          </template>
        </el-table-column>
        <el-table-column
          align="center"
          label="创建人"
          prop="createStaffName"
          show-overflow-tooltip
        />
        <el-table-column align="center" label="创建时间" prop="createTime" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { getList, deleteData } from '@/api/setting/mjsz'
  import Edit from './components/mjszEdit.vue'
  import { parseTime } from '@/utils/index'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import { getFlowList } from '@/api/setting/auth'
  export default {
    name: 'mjsz',
    components: { Edit, filterSearch, filterTable, ProcessList },
    mixins: [searchTableMixis],
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          levelName: '',
          levelType: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '密级名称' },
          { name: '密级类型' },
          { name: '创建人' },
          { name: '创建时间' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'audit-setting-mjsz-search',
        tableKey: 'audit-setting-mjsz-list',
        searchMore: true,
        projectNoticeId: '',
        multipleSelection: [],
        type: ['', '功能模块', '业务单据', '人员', '附件'],
        requireValuedata: false,
      }
    },
    created() {
      this.fetchData()

      this.initTable()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      getFiled() {
        return [
          { name: '密级名称', key: 'levelName' },
          { name: '密级类型', key: 'levelType' },
        ]
      },
      formatDate(row, column) {
        let data = row[column.property]
        return parseTime(data, '{y}-{m}-{d}')
      },
      resetQueryForm() {
        this.queryForm = {
          levelName: '',
          levelType: '',
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
        this.listLoading = true
        const {
          data: { records, total },
        } = await getList(this.queryForm)
        this.list = records
        this.total = total
        this.listLoading = false
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
          const { msg, code, data } = await deleteData({
            levelId: row.levelId,
          })
          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: data.recordId,
              targetType: 'secrect',
              operationType: 3,
            }).then((res) => {
              if (res.data == 0) {
                // 可以提交流程
                this.$refs['process'].save(220, data.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.close()
              } else {
                // 不可以提交流程
                this.$baseMessage(
                  '当前用户流程已存在,请先走审批流程',
                  'error',
                  'vab-hey-message-error'
                )
                return
              }
            })
          } else {
            if (code == 1) {
              this.$baseMessage(msg, 'success')
              await this.fetchData()
            } else {
              this.$baseMessage(msg, 'error')
            }
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
