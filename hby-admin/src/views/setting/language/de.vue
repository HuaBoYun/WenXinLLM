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
                v-model="queryForm.menuname"
                placeholder="简体中文"
                v-if="item.name === '简体中文'"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="fetchData"
              >
              {{ $translateTitle('查询') }}
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetSearch()" type="primary">{{ $translateTitle('重置') }}</el-button>
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
      <vab-query-form-right-panel :span="24" style="margin-bottom: 20px;">
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

        <el-button type="success" @click="addTransfer">添加翻译</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column
          type="selection"
          align="center"
          width="55">
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            type="index"
            label="序号"
            width="55"
            align="center"
            v-if="item.name === '序号'"
          >
          </el-table-column>
          <el-table-column
            align="center"
            label="简体中文"
            prop="menuname"
            v-if="item.name === '简体中文'"
          />
          <el-table-column
            align="center"
            label="德语"
            prop="trantext"
            v-if="item.name === '德语'"
          />
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button type="text" @click="addTransfer(row)">修改</el-button>
            <!-- <el-button type="text" @click="handleDelete(row)">删除</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 翻译管理 -->
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle + '翻译'"
      :visible.sync="newTransferDialogVisable"
      width="500px"
      @close="newTransferDialogVisable = false"
      v-if="newTransferDialogVisable"
    >
      <el-row :gutter="14" v-loading="loading">
        <el-form
          ref="ruleForm"
          label-width="100px"
          :model="transferForm"
          size="mini"
        >
          <el-col :span="24">
            <el-form-item label="简体中文" prop="menuname">
              <el-input
                v-model="transferForm.menuname"
                clearable
                placeholder="请输入简体中文"
                :style="{ width: '100%' }"
              />
            </el-form-item>
            <el-form-item label="翻译" prop="trantext">
              <el-input
                v-model="transferForm.trantext"
                clearable
                placeholder="请输入翻译"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div slot="footer">
        <el-button @click="newTransferDialogVisable = false">取消</el-button>
        <el-button @click="saveTransfer" type="primary">确定</el-button>
      </div>
    </el-dialog>
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
  </div>
</template>

<script>
  import { getCjfaList, deleteCjfa } from '@/api/cwsc'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import edit from './edit.vue'
  import {
    getTranslateList,
    saveOrUpdateTranslate,
    removeTranslate
  } from '@/api/setting/language.js'
  import { mapGetters, mapActions } from 'vuex'

  const infoid = 'de'

  export default {
    name: 'NormalReportList',
    components: { filterTable, filterSearch, edit },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          infoid: infoid,
          menuname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '序号' },
          { name: '简体中文' },
          { name: '德语' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-language-index-search',
        tableKey: 'setting-language-index-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        newTransferDialogVisable: false,
        transferForm: {
          infoid,
          menuname: '',
          trantext: '',
          configid: ''
        },
        loading: false,
        dialogTitle: '添加',
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
    },
    methods: {
      // ...mapActions({
      //   getLanguageList: 'settings/getLanguageList',
      // }),
      // 定义表单所有项
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '简体中文', key: 'menuname' },
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

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },

      // 动态表格开始
      // 动态表格开始
      /**
       * @description: 从上一次缓存中初始化表头
       * @return {*}
       */
      initTable() {
        this.listLoading = true
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
          this.listLoading = false
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
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        const tranRes = await getTranslateList(this.queryForm)
        this.list = tranRes.data ? tranRes.data.records : []
        this.total = tranRes.data ? tranRes.data.total : 0
        this.listLoading = false
      },
      addTransfer(e, item) {
        if (item) {
          this.dialogTitle = '修改'
          Object.assign(this.transferForm, {
            infoid: infoid,
            trantext: item.trantext,
            menuname: item.menuname,
            configid: item.configid,
          })
        } else {
          this.dialogTitle = '添加'
        }
        this.newTransferDialogVisable = true
      },
      async saveTransfer() {
        const res = await saveOrUpdateTranslate(this.transferForm)
        if (res.code == 1 && res.msg == '成功') {
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          this.fetchData()
          this.newTransferDialogVisable = false
          return
        }
        this.$baseMessage(res.msg || '失败', 'error', 'vab-hey-message-error')
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await removeTranslate({ infoid: infoid, configid: row.configid })
          if (res.code == 1 && res.msg == '成功') {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            this.fetchData()
          } else {
            this.$baseMessage(res.msg || '失败', 'error', 'vab-hey-message-error')
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          infoid: infoid,
          menuname: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
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

  .btn-group i {
    cursor: pointer;
    font-size: 18px;
    margin-right: 8px;
    color: #787878;
  }

  .btn-group i:hover {
    font-weight: 600;
    color: #000;
  }

  ::v-deep(.languageForm .el-form-item) {
    margin: 0 !important;
  }
</style>
