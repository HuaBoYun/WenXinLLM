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
            <el-form-item v-for="(item, index) in searchItem" :key="index">
              <span v-if="item.name === '姓名'">
                <el-input
                  v-model="queryForm.practiceApplyName"
                  clearable
                  placeholder="姓名"
                  style="width: 140px"
                  v-if="item.name === '姓名'"
                />
                <el-button
                  @click="projectManager"
                  style="margin-left: 10px"
                  type="primary"
                >
                  选择
                </el-button>
              </span>

              <!-- <el-date-picker
                v-model="queryForm.year"
                type="year"
                placeholder="年度"
                style="width: 140px; margin-right: 20px"
                value-format="yyyy"
                v-if="item.name === '年度'"
              />
              <el-input
                v-model="queryForm.state"
                clearable
                placeholder="执业状态"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '执业状态'"
              />
              <el-select
                v-model="queryForm.isCertificateNumber"
                placeholder="从业资格证"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '从业资格证'"
              >
                <el-option :value="1" label="有公司律师执业资格证"></el-option>
                <el-option :value="0" label="无公司律师执业资格证"></el-option>
              </el-select> -->
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
              <el-button @click="resetSearch">重置</el-button>
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
        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
        <el-button @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>

      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="姓名" prop="practiceApplyName">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.practiceApplyName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="任职公司"
            prop="nowWorkUnit"
            v-if="item.name === '任职公司'"
          />
          <el-table-column
            align="center"
            label="类别"
            prop="workDepartment"
            v-if="item.name === '类别'"
          />
          <el-table-column
            align="center"
            label="法律职业资格证"
            prop="certificationNumber"
            v-if="item.name === '法律职业资格证'"
          />
          <el-table-column
            align="center"
            label="公司律师执业资格证"
            prop="certificateNumber"
            v-if="item.name === '公司律师执业资格证'"
          />
          <el-table-column
            align="center"
            label="联系电话"
            prop="phone"
            v-if="item.name === '联系电话'"
          />
          <el-table-column
            align="center"
            label="年龄"
            prop="age"
            v-if="item.name === '年龄'"
          />
          <el-table-column
            align="center"
            label="身份证号"
            prop="identityCard"
            v-if="item.name === '身份证号'"
          />
          <el-table-column
            align="center"
            label="毕业学校"
            prop="schoolOfGraduation"
            v-if="item.name === '毕业学校'"
          />
          <el-table-column
            align="center"
            label="公司律师执业证号"
            prop="公司律师执业证号"
            v-if="item.name === '公司律师执业证号'"
          />
          <el-table-column
            align="center"
            label="专业"
            prop="specialty"
            v-if="item.name === '专业'"
          />

          <el-table-column
            align="center"
            label="具体工作部门"
            prop="workDepartment"
            v-if="item.name === '具体工作部门'"
          />
          <el-table-column
            align="center"
            label="邮编"
            prop="postalCode"
            v-if="item.name === '邮编'"
          />
          <el-table-column
            align="center"
            label="性别"
            prop="sex"
            v-if="item.name === '性别'"
          >
            <template #default="{ row }">
              <span v-if="row.sex == 0">女</span>
              <span v-if="row.sex == 1">男</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="民族"
            prop="phone"
            v-if="item.nation === '民族'"
          />
          <el-table-column
            align="center"
            label="政治面貌"
            prop="politicsStatus"
            v-if="item.name === '政治面貌'"
          />
          <el-table-column
            align="center"
            label="住所地址"
            prop="homeAddress"
            v-if="item.name === '住所地址'"
          />
          <el-table-column
            align="center"
            label="最高学历"
            prop="highestEducation"
            v-if="item.name === '最高学历'"
          >
            <template #default="{ row }">
              <span v-if="row.highestEducation == 1">博士研究生</span>
              <span v-if="row.highestEducation == 2">硕士研究生</span>
              <span v-if="row.highestEducation == 3">大学本科</span>
              <span v-if="row.highestEducation == 4">大学专科</span>
              <span v-if="row.highestEducation == 5">中专</span>
              <span v-if="row.highestEducation == 6">大学及以下</span>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="现工作单位"
            prop="nowWorkUnit"
            v-if="item.name === '现工作单位'"
          />
          <el-table-column
            align="center"
            label="办公电话"
            prop="officePhone"
            v-if="item.name === '联办公电话电话'"
          />
          <el-table-column
            align="center"
            label="技术职务"
            prop="phone"
            v-if="item.technicalPosition === '联系电技术职务话'"
          />
          <el-table-column
            align="center"
            label="外语水平"
            prop="foreignLanguageLevel"
            v-if="item.name === '外语水平'"
          />
        </div>
        <el-table-column align="center" label="" prop="" width="1px" />
        <!-- <el-table-column align="center" label="操作" width="120">
        <template #default="{ row }">
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="sendToManuscript(row)">
                申请
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToManuscript(row)">
                抄送
              </el-dropdown-item>
              <el-dropdown-item @click.native="sendToManuscript(row)">
                同意
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column> -->
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
    <rytzView ref="gslsView" @fetchData="fetchData" />
    <project-manage
      @projectManage="getChildlistPro"
      ref="manage"
    ></project-manage>
  </div>
</template>

<script>
  import projectManage from '@/views/audit/project/components/formComponents/projectManage.vue'
  import { getRYTZList, exportRYTZ } from '@/api/fwgl/gsls'
  import { doDelete } from '@/api/table'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import rytzView from './components/rytzView.vue'
  import { downloadFile } from '@/utils/otherUtils'

  export default {
    name: 'NormalReportList',
    components: { projectManage, rytzView, filterTable, filterSearch },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 0,
          pageSize: 20,
          practiceApplyName: '',
          practiceApplyId: '',
          state: '',
          year: '',
        },
        filedAll: [
          { name: '任职公司' },
          { name: '类别' },
          { name: '法律职业资格证' },
          { name: '公司律师执业资格证' },
          { name: '联系电话' },
          { name: '年龄' },
          { name: '身份证号' },
          { name: '毕业学校' },
          { name: '专业' },
          { name: '具体工作部门' },
          { name: '邮编' },
          { name: '性别' },
          { name: '民族' },
          { name: '政治面貌' },
          { name: '住所地址' },
          { name: '最高学历' },
          { name: '现工作单位' },
          { name: '办公电话' },
          { name: '技术职务' },
          { name: '外语水平' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-gsls-rytz-search',
        tableKey: 'fwgl-gsls-rytz-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
      }
    },
    created() {
      this.fetchData()
      /**
       * @description: 下面四句：控制筛选项、表格的位置以及显示隐藏
       */
      this.initTable() //初始化表格 //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      getChildlistPro(val) {
        // staffid: val[0].staffid,
        this.queryForm.practiceApplyName = val[0].realname
        this.queryForm.practiceApplyId = val[0].staffid
      },
      /**
       * @description: 打开选择人员组件
       * @return {*}
       */      
      projectManager() {
        this.$refs['manage'].showEdit()
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '姓名', key: 'practiceApplyName' },
          // { name: '类别', key: 'type' },
          // { name: '执业状态', key: 'state' },
          // { name: '年度', key: 'year' },
          // { name: '从业资格证', key: 'isCertificateNumber' },
        ]
        return fields
      },
      /**
       * @description: 从上一次缓存中获取搜索项初始化
       * @return {*}
       */
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
      /**
       * @description: 展开收起查询条件
       * @return {*}
       */
      showMore() {
        this.searchMore = !this.searchMore

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
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
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          pageNumber: 1,
          pageSize: 20,
          practiceApplyName: '',
          state: '',
          startTime: '',
          endTime: '',
        }
        this.fetchData()
      },
      /**
       * @description: 重置并请求
       * @return {*}
       */      
      resetSearch() {
        this.resetQueryForm()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        this.listLoading = true
        if (this.queryForm.year) {
          this.queryForm.startTime = this.queryForm.year
          this.queryForm.endTime = this.queryForm.year
        }

        // 人员台账列表
        this.queryForm.iszysq = 0

        const {
          data: { tlist, totalRecord },
        } = await getRYTZList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      /**
       * @description: 导出
       * @return {*}
       */      
      async handleExport() {
        this.listLoading = true
        const res = await exportRYTZ(this.queryForm)
        downloadFile(res, '人员台账列表.xlsx')
        this.listLoading = false
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */      
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['gslsView'].showEdit('detail', row)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */      
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
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
