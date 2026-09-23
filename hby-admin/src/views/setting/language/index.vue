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
              <!-- <el-input
                v-model="queryForm.financeorgname"
                placeholder="翻译分类"
                v-if="item.name === '翻译分类'"
              /> -->
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

        <el-button type="success" @click="addTransfer">添加中文</el-button>
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
        </div>
        <!-- <el-table-column
          align="center"
          :label="curLanguage.targetlanguage"
          prop="trantext"
        /> -->
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleView(row)" disabled>
              导出
            </el-button> -->
            <el-button type="text" @click="addTransfer(row)">修改</el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 语种管理 -->
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      title="语种管理"
      :visible.sync="newLanguageDialogVisable"
      width="400px"
      @close="closeLanguageDialog"
      v-if="newLanguageDialogVisable"
    >
      <el-row :gutter="14" v-loading="loading" class="languageForm">
        <el-form
          ref="ruleForm"
          label-width="0"
          :model="languageForm"
          size="mini"
          style="padding-left: 40px;display: flex;align-items: center;"
        >
          <el-col :span="9">
            <el-form-item prop="targetlanguage">
              <el-input
                v-model="languageForm.targetlanguage"
                clearable
                placeholder="语言"
                :style="{ width: '100%' }"
                v-if="isNew"
              />
            </el-form-item>
            </el-col>
          <el-col :span="9">
            <el-form-item prop="infoname">
              <el-input
                v-model="languageForm.infoname"
                clearable
                placeholder="标题"
                :style="{ width: '100%' }"
                v-if="isNew"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="padding-right: 0;padding-left: 24px;">
            <div class="btn-group">
              <el-button type="primary" v-if="!isNew" @click="showNew" size="mini">添加</el-button>
              <i class="el-icon el-icon-check" v-if="isNew" @click="saveLanguage"></i>
              <i class="el-icon el-icon-close" v-if="isNew" @click="closeNew"></i>
            </div>
          </el-col>
        </el-form>
      </el-row>

      <div class="languageList">
        <ul>
          <li v-for="item in tempLanguageList" :key="item.infoid" style="display: flex;align-items: center;">
            <div style="flex: 1;margin: 6px 0;font-size: 16px;">
              <el-input
                v-model="languageForm.targetlanguage"
                clearable
                placeholder="语种"
                :style="{ width: '46%', marginRight: '4px' }"
                v-if="item.isEdit"
              />
              <el-input
                v-model="languageForm.infoname"
                clearable
                placeholder="标题"
                :style="{ width: '46%' }"
                v-if="item.isEdit"
              />
              <div v-if="!item.isEdit">{{ item.targetlanguage }}</div>
            </div>
            <div class="btn-group">
              <i class="el-icon el-icon-check" v-if="item.isEdit" @click="saveLanguage(item)"></i>
              <i class="el-icon el-icon-close" v-if="item.isEdit" @click="closeEdit(item)"></i>
              <i class="el-icon el-icon-edit-outline" v-if="!item.isEdit" @click="showEdit(item)"></i>
              <i class="el-icon el-icon-delete" v-if="!item.isEdit" @click="delLanguage(item)"></i>
            </div>
          </li>
        </ul>
      </div>
      <div slot="footer">
        <el-button @click="closeLanguageDialog">取消</el-button>
      </div>
    </el-dialog>

    <!-- 翻译管理 -->
    <el-dialog
      :close-on-click-modal="false"
      :append-to-body="true"
      :title="dialogTitle"
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
            <!-- <el-form-item label="翻译" prop="trantext">
              <el-input
                v-model="transferForm.trantext"
                clearable
                placeholder="请输入翻译"
                :style="{ width: '100%' }"
              />
            </el-form-item> -->
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
    getConversionList,
    getTranslateList,
    saveOrUpdateTranslate,
    removeTranslate,
    saveOrUpdateConversion,
    removeConversion
  } from '@/api/setting/language.js'
  import { mapGetters, mapActions } from 'vuex'

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
          menuname: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '序号' },
          { name: '简体中文' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'setting-language-zh-search',
        tableKey: 'setting-language-zh-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        newLanguageDialogVisable: false,
        newTransferDialogVisable: false,
        languageForm: {
          infoid: '',
          infoname: '',
          targetlanguage: ''
        },
        transferForm: {
          menuname: '',
          trantext: '',
          configid: ''
        },
        loading: false,
        dialogTitle: '添加',
        tempLanguageList: [],
        curLanguage: {},
        isNew: false,
        isEdit: false,
        targetlanguage: '',
        infoname: ''
      }
    },
    // computed: {
    //   ...mapGetters({
    //     languageList: 'settings/languageList'
    //   }),
    // },
    // watch: {
    //   languageList: {
    //     deep: true,
    //     immediate: true,
    //     handler(val) {
    //       console.log('tempLanguageList', this.languageList)
    //       this.tempLanguageList = this.languageList.map(x => {
    //         return {
    //           ...x,
    //           isEdit: false
    //         }
    //       })
    //     }
    //   }
    // },
    created() {
      this.curLanguage = {
        targetlanguage: '英语',
        infoid: '1',
        infoname: '汉译英'
      }
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
        const tranRes = await getTranslateList({...this.queryForm, infoid: this.curLanguage.infoid})
        console.log('tranRes', tranRes)
        this.list = tranRes.data ? tranRes.data.records : []
        this.total = tranRes.data ? tranRes.data.total : 0
        this.listLoading = false
      },
      addLanguage(item) {
        if (item) {
          Object.assign(this.languageForm, {
            infoname: item.infoname,
            infoid: item.infoid,
            targetlanguage: item.targetlanguage
          })
        }
        this.newLanguageDialogVisable = true
      },
      closeLanguageDialog() {
        this.isNew = false
        this.tempLanguageList.map(x => x.isEdit = false)
        this.newLanguageDialogVisable = false
      },
      addTransfer(e, item) {
        console.log('item', item)
        this.transferForm.infoid = this.curLanguage.infoid || ''
        if (item) {
          this.dialogTitle = '修改'
          Object.assign(this.transferForm, {
            trantext: item.trantext,
            menuname: item.menuname,
            configid: item.configid,
          })
        } else {
          this.dialogTitle = '添加'
        }
        this.newTransferDialogVisable = true
      },
      showNew() {
        this.tempLanguageList.map(x => x.isEdit = false)
        this.languageForm.infoid = ''
        this.languageForm.infoname = ''
        this.languageForm.targetlanguage = ''
        this.isNew = true
      },
      closeNew() {
        this.isNew = false
      },
      closeEdit(item) {
        this.tempLanguageList.map(x => {
          if (item.infoid === x.infoid) {
            x.isEdit = false
          }
        })

      },
      showEdit(item) {
        this.isNew = false
        if (item && item.infoid) {
          this.languageForm.infoid = item.infoid
          this.languageForm.infoname = item.infoname
          this.languageForm.targetlanguage = item.targetlanguage
        }
        this.tempLanguageList.map(x => {
          if (item.infoid === x.infoid) {
            x.isEdit = true
          } else {
            x.isEdit = false
          }
        })

      },
      async saveLanguage(item) {
        const res = await saveOrUpdateConversion(this.languageForm)
        if (res.code == 1 && res.msg == '成功') {
          this.$baseMessage('成功', 'success', 'vab-hey-message-success')
          // this.getLanguageList()
          this.newLanguageDialogVisable = false
          return
        }
        this.$baseMessage(res.msg || '失败', 'error', 'vab-hey-message-error')
      },
      async delLanguage(item) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await removeConversion({infoid: item.infoid})
          if (res.code == 1 && res.msg == '成功') {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            // this.getLanguageList()
            this.newLanguageDialogVisable = false
            return
          }
        })
        this.$baseMessage(res.msg || '失败', 'error', 'vab-hey-message-error')
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
      selectLanguage(lang) {
        this.curLanguage = lang
        this.fetchData()
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await removeTranslate({ infoid: this.curLanguage.infoid, configid: row.configid })
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
