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
                v-model="queryForm.usePeopleName"
                clearable
                placeholder="使用人"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '使用人'"
              ></el-input>
              <el-select
                v-model="queryForm.flagUsePeople"
                v-if="item.name === '是否有使用人'"
                placeholder="是否有使用人"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
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
              <el-button @click="resetSearch()" type="primary">重置</el-button>
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
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="primary" @click="downLoadTemplate">下载模板</el-button>
        <el-upload
          accept=".xls,.xlsx"
          :action="
            baseApi +
            '/centralaudit/api-auth/ip/inventory/download-express-import'
          "
          :data="{}"
          :show-file-list="false"
          :on-success="handleSuccess"
          :on-error="handleError"
          :headers="headers"
          style="display: inline-block; margin-left: 10px"
        >
          <el-button type="primary" style="margin-right: 10px">导入</el-button>
        </el-upload>
        <el-button
          type="primary"
          @click="exportDefault"
          style="margin-right: 10px"
        >
          导出
        </el-button>
        <el-button type="success" @click="handleSend">下发</el-button>
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="使用人" prop="usePeopleName">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ row.usePeopleName }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="IP地址"
            prop="ipAddress"
            v-if="item.name === 'IP地址'"
          />
          <el-table-column
            align="center"
            label="办公楼层"
            prop="officeFloor"
            v-if="item.name === '办公楼层'"
          ></el-table-column>
        </div>
        <el-table-column align="center" label="操作" width="120">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row, 'edit')"
              :disabled="!!row.state"
            >
              修改
            </el-button>
            <el-button
              @click="handleDelete(row)"
              type="text"
              :disabled="!!row.state"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <ipqdView ref="edit" @fetchData="fetchData"></ipqdView>
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
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import ipqdView from './components/ipqdView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import {
    getList,
    deleteInfo,
    downloadTemplateFn,
    downloadAssest,
  } from '@/oapi/ypns_zhgl/ipqd.js'
  import store from '@/store'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  import { getFlowPkInfo } from '@/api/setting/system.js'
  import FileUpload from '@/components/FileUploadZhgl.vue'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  export default {
    components: {
      ipqdView,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
      FileUpload,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          flagUsePeople: '',
          usePeopleName: '',
        },
        headers: { token: token },
        baseApi: baseURL,
        filedAll: [{ name: 'IP地址' }, { name: '办公楼层' }], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-ipqd-search',
        tableKey: 'oilAudit-zhgl-ipqd-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        select: [],
        options: [
          {
            value: '1',
            label: '是',
          },
          {
            value: '0',
            label: '否',
          },
        ],
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
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(133, row.id)
      },
      // 办理
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.id,
          tableId: 133,
        })
        this.$refs.wfqddeal.show(res.data, false)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '使用人', key: 'name' },
          { name: '是否有使用人', key: 'flagUsePeople' },
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
        } = await getList(this.queryForm)
        this.list = tlist || []
        this.total = totalRecord || 0
        this.listLoading = false
      },
      handleExport() {},
      handleEdit(row, type) {
        this.$refs['edit'].showEdit(row, type)
      },
      handleAdd() {
        this.$refs['edit'].showEdit(null, 'add')
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const res = await deleteInfo({ id: row.id })
          if (res.code == 200) {
            this.$baseMessage('成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          createType: 1,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      // 下载模板
      async downLoadTemplate() {
        const data = await downloadTemplateFn()
        let filename = decodeURI(
          data.headers['content-disposition'].split('=')[1].split("'")[2]
        )
        let blob = new Blob([data.data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      // 导出ip
      async exportDefault() {
        const ids = this.select.map((res) => res.id)
        const data = await downloadAssest({
          ...this.queryForm,
          ids: ids,
        })
        let filename = decodeURI(
          data.headers['content-disposition'].split('=')[1].split("'")[2]
        )
        let blob = new Blob([data.data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.id == row.id)
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
            if (row && !this.select.some((x) => x.id == row.id)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.id == row.id)
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
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      //下发
      handleSend() {
        if (this.select && this.select.length > 0) {
          this.getChildlistPro()
        } else {
          this.$baseMessage(
            '请选择需要下发的数据',
            'error',
            'vab-hey-message-error'
          )
        }
      },
      async getChildlistPro(val) {
        const ids = this.select.map((res) => res.id)
        const titles = this.select.map((res) => res.usePeopleName)
        const names = this.select.map((res) => res.usePeopleId)

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
        //下发通知
        xiafaListNew({
          tableId: '569272297635909',
          jsondistribution: JSON.stringify([...arr]),
        }).then((res) => {
          if (res.msg == '成功') {
            this.$baseMessage(res.msg, 'success')
            this.fetchData()
            this.select = []
          }
        })
      },
      // 导入成功
      handleSuccess(file) {
        console.log(file, 'file')
        if (file.code == 200) {
          this.$baseMessage('导入成功', 'success')
          this.fetchData()
        }
      },
      // 导入失败
      handleError(err) {
        console.log(err)
        this.$baseMessage(err, 'error')
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
