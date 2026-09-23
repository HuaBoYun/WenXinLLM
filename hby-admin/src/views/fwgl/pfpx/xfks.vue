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
              <el-input
                v-model="queryForm.nickName"
                clearable
                placeholder="用户昵称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '用户昵称'"
              />
              <el-select
                v-model="queryForm.replied"
                placeholder="是否已回复"
                clearable
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '是否已回复'"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-date-picker
                v-model="queryForm.createTime"
                type="daterange"
                range-separator=":"
                placeholder="创建时间"
                value-format="timestamp"
                style="width: 340px; margin-right: 20px"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                v-if="item.name === '创建时间'"
              />
              <el-select
                v-model="queryForm.knowledgeType"
                clearable
                placeholder="知识类型"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '知识类型'"
              >
                <el-option :value="1" label="劳动用工"></el-option>
                <el-option :value="2" label="知识产权"></el-option>
                <el-option :value="3" label="投融资"></el-option>
                <el-option :value="4" label="法律尽调"></el-option>
                <el-option :value="5" label="法律纠纷"></el-option>
                <el-option :value="6" label="其他"></el-option>
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
        <el-button type="success" @click="handleAdd">提问</el-button>
        <el-button type="danger" @click="handleDeleteMore">批量删除</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          align="center"
          label="序号"
          type="selection"
        ></el-table-column>
        <el-table-column align="center" label="提问人" prop="nickNames">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleDetail(row)"
              style="color: #0f98fa"
            >
              {{ row.nickNames }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="知识类型"
            prop="knowledgeTypes"
            v-if="item.name === '知识类型'"
          >
            <template #default="{ row }">
              {{
                row.knowledgeType ? knowledgeTypes[row.knowledgeType - 1] : ''
              }}
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="留言内容"
            prop="content"
            v-if="item.name === '留言内容'"
          />

          <el-table-column
            align="center"
            label="留言时间"
            prop="createTime"
            :formatter="formatDate"
            v-if="item.name === '留言时间'"
          />
          <el-table-column
            align="center"
            label="是否回复"
            prop="replied"
            v-if="item.name === '是否回复'"
          >
            <template #default="{ row }">
              <el-tag v-if="row.replied" type="success">已回复</el-tag>
              <el-tag v-else type="danger">未回复</el-tag>
            </template>
          </el-table-column>

          <!-- <el-table-column
            align="center"
            label="回复人"
            prop="replyNames"
            v-if="item.name === '回复人'"
          />
          <el-table-column
            align="center"
            label="回复内容"
            prop="replyContent"
            v-if="item.name === '回复内容'"
          />
          <el-table-column
            align="center"
            label="回复时间"
            prop="replyTime"
            :formatter="formatDate"
            v-if="item.name === '回复时间'"
          /> -->
        </div>

        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleEdit(row)">回复</el-button> -->
            <el-button
              type="text"
              @click="handleEdit(row)"
              v-if="
                row.isReplyDisplay &&
                String(userInfo.staffid) === String(row.creator)
              "
            >
              回复
            </el-button>
            <el-button
              type="text"
              @click="handleDelete(row)"
              v-if="String(userInfo.staffid) === String(row.creator)"
            >
              删除
            </el-button>
            <!-- <el-button type="text">导出</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      class="pagination"
      background
      :current-page="queryForm.currentPage"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <quizView ref="quizView" @fentch-data="fetchData" />
    <replyView ref="replyView" @fentch-data="fetchData" />
    <infoView ref="infoView" />
  </div>
</template>

<script>
  import { deleteMsg, queryMsg } from '@/api/fwgl/pfpx'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { formatDate } from '@/utils/index'
  import infoView from './components/infoView.vue'
  import quizView from './components/quizView.vue'
  import replyView from './components/replyView.vue'

  export default {
    name: 'bbsList',
    components: { filterTable, filterSearch, quizView, replyView, infoView },
    data() {
      return {
        multipleSelection: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          content: '',
          createTimeEnd: '',
          createTimeStart: '',
          currentPage: 1,
          nickName: '',
          pageSize: 20,
          replied: '',
        },
        knowledgeTypes: [
          '劳动用工',
          '知识产权',
          '投融资',
          '法律尽调',
          '法律纠纷',
          '其他',
        ],
        filedAll: [
          { name: '留言内容' },
          { name: '知识类型' },
          { name: '留言时间' },
          { name: '是否回复' },
          // { name: '回复人' },
          // { name: '回复内容' },
          // { name: '回复时间' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'fwgl-pfpx-xfks-search',
        tableKey: 'fwgl-pfpx-xfks-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        options: [
          {
            value: true,
            label: '是',
          },
          {
            value: false,
            label: '否',
          },
        ],
        userInfo: JSON.parse(localStorage.getItem('userInfo')),
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
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '用户昵称', key: 'nickName' },
          { name: '是否已回复', key: 'replied' },
          { name: '创建时间', key: 'createTime' },
          { name: '知识类型', key: 'knowledgeType' },
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
        this.queryForm.currentPage = val
        this.fetchData()
      },
      /**
       * @description: 重置查询条件
       * @return {*}
       */      
      resetQueryForm() {
        this.queryForm = {
          currentPage: 0,
          pageSize: 20,
          planYearBeginDate: '',
          planYearEndDate: '',
          popularizeLawPlanName: '',
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
        this.queryForm.currentPage = 1
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */      
      async fetchData() {
        //
        if (this.queryForm.createTime) {
          this.queryForm.createTimeStart = this.queryForm.createTime[0] || 0
          this.queryForm.createTimeEnd = this.queryForm.createTime[1] || 0
        }
        //
        // this.list = [{ nickName: 'asdf' }, {}, {}]
        this.listLoading = true
        const { bbsList, total } = await queryMsg(this.queryForm)
        this.list = bbsList
        this.total = total
        this.listLoading = false
      },
      async handleDelete(row) {
        let list = []
        list.push(row.id)

        const res = await deleteMsg({ list: list })
        // if(res)
        if (res) {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.fetchData()
        }
      },
      async handleDeleteMore() {
        let list = []
        this.multipleSelection.map((item) => {
          list.push(item.id)
        })
        const res = await deleteMsg({ list: list })
        if (res) {
          this.$message({
            type: 'success',
            message: '删除成功!',
          })
          this.fetchData()
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */      
      handleAdd() {
        this.$refs['quizView'].showEdit('add', null)
      },
      handleEdit(row) {
        this.$refs['replyView'].showEdit('edit', row)
      },
      handleDetail(row) {
        //获取当前ID的留言todo
        // getMsgById(row.id)
        //
        this.$refs['replyView'].showEdit('detail', row)
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
