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
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.gsldxm"
                clearable
                v-if="item.name === '公司领导'"
                placeholder="公司领导"
              />
              <el-date-picker
                v-model="queryForm.createYear"
                type="year"
                value-format="yyyy"
                placeholder="请选择创建年度"
                v-if="item.name === '创建年度'"
              ></el-date-picker>
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
        <el-button type="success" @click="handleAdd(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport">导出</el-button>
        <el-button type="success" @click="hadnlePush">下发</el-button>
      </vab-query-form-right-panel>

      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column
          width="48"
          type="selection"
          :reserve-selection="true"
        ></el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            v-if="item.name === '公司领导'"
            align="center"
            label="公司领导"
            prop="gsldxm"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.gsldxm }}
              </el-button>
            </template>
          </el-table-column>

          <el-table-column
            v-if="item.name === '立项要求'"
            align="center"
            label="立项要求"
            prop="lxyq"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '落实建议'"
            align="center"
            label="落实建议"
            prop="lsjy"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿人'"
            align="center"
            label="拟稿人"
            prop="cjrxm"
            show-overflow-tooltip
          />
          <el-table-column
            v-if="item.name === '拟稿日期'"
            align="center"
            label="拟稿日期"
            prop="cjsj"
            show-overflow-tooltip
          />
          <!-- <el-table-column
            v-if="item.name === '状态'"
            align="center"
            label="状态"
          >
            <template #default="{ row }">
              {{
                row.spzt == 1
                  ? '审批中'
                  : row.spzt == 2
                  ? '已退回'
                  : row.spzt == 3
                  ? '已撤回'
                  : row.spzt == 4
                  ? '已终止'
                  : row.spzt == 5
                  ? '已跟踪'
                  : row.spzt == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column> -->
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.spzt"
            >
              修改
            </el-button>
            <el-button
              type="text"
              :disabled="!!row.spzt"
              @click.native="handleDelete(row)"
            >
              删除
            </el-button>
            <!-- <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :disabled="!row.spzt"
                  @click.native="handleDeal(row)"
                >
                  办理
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.spzt"
                  @click.native="handleSubmit(row)"
                >
                  提交审批
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="!!row.spzt"
                  @click.native="handleDelete(row)"
                >
                  删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown> -->
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
    <fgldhzEdit ref="edit" @fetchData="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <lxjybSelectModal @projectManage="getChildlistPro" ref="person" />
  </div>
</template>

<script>
  import lxjybSelectModal from '@/components/selectPerson'
  import {
    fgldhzList,
    fgldhzDelete,
    fgldhzExportList,
    fgldhzXf,
  } from '@/api/monitor/question'
  // import DraftInfo from "./components/DraftInfo";
  import fgldhzEdit from './components/fgldhzEdit'
  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import { xiafaListNew } from '@/oapi/audit/preparation'
  export default {
    name: 'Download',
    components: {
      // DraftInfo,
      fgldhzEdit,
      filterSearch,
      filterTable,
      ProcessList,
      WfqdDeal,
      lxjybSelectModal,
    },
    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fgldhz/importData',
        headers: { token },
        select: [],
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          gsldxm: '',
          pageNumber: 1,
          pageSize: 20,
          createYear: null,
        },
        filedAll: [
          { name: '公司领导' },
          { name: '立项要求' },
          { name: '落实建议' },
          { name: '拟稿人' },
          { name: '拟稿日期' },
          { name: '所属项目' },
          // { name: '状态' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-jhlx-fgldhz-search',
        tableKey: 'oilAudit-jhlx-fgldhz-list',
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
      hadnlePush() {
        if (this.select && this.select.length > 0) {
          this.$refs.person.showEdit()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async handleExport() {
        const ids = this.select.map((res) => res.fgldhzid)
        let params = { ...this.queryForm }
        if(ids.length > 0) {
          params.ids = ids.join()
        }
        const data = await fgldhzExportList(params)
        let fileName = '分管领导汇总表'
        let blob = new Blob([data], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        })
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let link = document.createElement('a')
          link.href = window.URL.createObjectURL(blob)
          link.download = fileName
          link.click()
          // 释放内存
          window.URL.revokeObjectURL(link.href)
        }
      },
      handleSuccess(response) {
        if (response.code == 1) {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      getFiled() {
        return [
          { name: '公司领导', key: 'gsldxm' },
          { name: '创建年度', key: 'createYear' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          gsldxm: '',
          createYear: '',
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
          data: { tlist, totalRecord },
        } = await fgldhzList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      async handleDetail(row) {
        await this.$refs['edit'].showEdit(row, true)
      },
      async handleEdit(row) {
        await this.$refs['edit'].showEdit(row, false)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await fgldhzDelete({
            ids: row.fgldhzid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error')
          }
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      // async handleDeal(row) {
      //   const res = await getFlowPkInfo({
      //     formId: row.fgldhzid,
      //     tableId: 106,
      //   })

      //   this.$refs.wfqddeal.show(res.data, false)
      // },
      // handleSubmit(row) {
      //   this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
      //     this.$refs['process'].save(106, row.fgldhzid)
      //   })
      // },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.fgldhzid)
        const titles = this.select.map((res) => res.gsldxm)
        const names = val.map((res) => res.staffid)

        const arr = []
        for (let i = 0; i < this.select.length; i++) {
          for (let k = 0; k < names.length; k++) {
            arr.push({
              formId: ids[i],
              distributionTitle: titles[i],
              isread: 0,
              reciver: names[k],
              moduleType: 'yqns',
            })
          }
        }
        //下发保存
        fgldhzXf({
          ids: ids.toString(),
          personIds: names.toString(),
        })
        //下发通知
        xiafaListNew({
          tableId: '1388',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.fgldhzid == row.fgldhzid)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.fgldhzid == row.fgldhzid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.fgldhzid == row.fgldhzid)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.fgldhzid == item.fgldhzid
              }),
              true
            )
          })
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
  .upload-demo {
    display: inline-block;
    margin: 0 10px;
  }
</style>
