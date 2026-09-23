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
                v-model="queryForm.assetName"
                clearable
                placeholder="资产名称"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '资产名称'"
              ></el-input>
              <el-input
                v-model="queryForm.specificationType"
                clearable
                placeholder="规格型号"
                style="width: 140px; margin-right: 20px"
                v-if="item.name === '规格型号'"
              ></el-input>
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
        <!-- <el-button type="primary" @click="() => {}">导入</el-button> -->
        <el-upload
          accept=".xls,.xlsx"
          :action="
            baseApi + '/centralaudit/api-auth/asset/mgt/download-express-import'
          "
          :data="{}"
          :show-file-list="false"
          :on-success="handleSuccess"
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
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          align="center"
          label="类别"
          prop="assetType"
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'detail')">
              {{ type[+row.assetType] }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="资产编码"
            prop="assetCode"
            v-if="item.name === '资产编码'"
          />
          <el-table-column
            align="center"
            label="资产名称"
            prop="assetName"
            v-if="item.name === '资产名称'"
          />
          <el-table-column
            align="center"
            label="规格型号"
            prop="specificationType"
            v-if="item.name === '规格型号'"
          />
          <el-table-column
            align="center"
            label="制造厂家"
            prop="manufacturer"
            v-if="item.name === '制造厂家'"
          />
          <el-table-column
            align="center"
            label="所属单位编码"
            prop="unitCode"
            v-if="item.name === '所属单位编码'"
          />
          <el-table-column
            align="center"
            label="所属单位名称"
            prop="unitName"
            v-if="item.name === '所属单位名称'"
          />
          <el-table-column
            align="center"
            label="车牌井号"
            prop="licensePlate"
            v-if="item.name === '车牌井号'"
          />
          <el-table-column
            align="center"
            label="自编号"
            prop="selfNum"
            v-if="item.name === '自编号'"
          />
          <el-table-column
            align="center"
            label="出厂编号"
            prop="factoryNumber"
            v-if="item.name === '出厂编号'"
          />
          <el-table-column
            align="center"
            label="出厂、建筑或完井日期"
            prop="factoryTime"
            v-if="item.name === '出厂、建筑或完井日期'"
          />
          <el-table-column
            align="center"
            label="投产日期"
            prop="productionTime"
            v-if="item.name === '投产日期'"
          />
          <el-table-column
            align="center"
            label="区块"
            prop="block"
            v-if="item.name === '区块'"
          />
          <el-table-column
            align="center"
            label="区块名称"
            prop="blockName"
            v-if="item.name === '区块名称'"
          />
          <el-table-column
            align="center"
            label="存放（安装）地点"
            prop="depositPlace"
            v-if="item.name === '存放（安装）地点'"
          />
          <el-table-column
            align="center"
            label="计量单位"
            prop="measurement"
            v-if="item.name === '计量单位'"
          />
          <el-table-column
            align="center"
            label="复合数量"
            prop="compositeQuantity"
            v-if="item.name === '复合数量'"
          />
          <el-table-column
            align="center"
            label="功率能力"
            prop="powerCapacity"
            v-if="item.name === '功率能力'"
          />
          <el-table-column
            align="center"
            label="技术状况名称"
            prop="technicalConditionName"
            v-if="item.name === '技术状况名称'"
          />
          <el-table-column
            align="center"
            label="使用状态名称"
            prop="usageStatusName"
            v-if="item.name === '使用状态名称'"
          />
          <el-table-column
            align="center"
            label="增加原因名称-台账"
            prop="addCauseName"
            v-if="item.name === '增加原因名称-台账'"
          />
          <el-table-column
            align="center"
            label="资金渠道名称-台账"
            prop="fundSchannelName"
            v-if="item.name === '资金渠道名称-台账'"
          />
          <el-table-column
            align="center"
            label="增加日期"
            prop="addTime"
            v-if="item.name === '增加日期'"
          />
          <el-table-column
            align="center"
            label="停产日期"
            prop="discontinuedTime"
            v-if="item.name === '停产日期'"
          />
          <el-table-column
            align="center"
            label="保管人"
            prop="custodianName"
            v-if="item.name === '保管人'"
          />
          <el-table-column
            align="center"
            label="期末原值"
            prop="finalOriginalValue"
            v-if="item.name === '期末原值'"
          />
          <el-table-column
            align="center"
            label="期末累计折旧"
            prop="finalCumulativeDepreciation"
            v-if="item.name === '期末累计折旧'"
          />
          <el-table-column
            align="center"
            label="期末净值"
            prop="finalNetWorth"
            v-if="item.name === '期末净值'"
          />
          <el-table-column
            align="center"
            label="期末减值准备"
            prop="finalImpairmentPrepare"
            v-if="item.name === '期末减值准备'"
          />
          <el-table-column
            align="center"
            label="在用人员"
            prop="usePeopleName"
            v-if="item.name === '在用人员'"
          />
          <el-table-column
            align="center"
            label="在用部门"
            prop="useDepartmentName"
            v-if="item.name === '在用部门'"
          />
          <el-table-column
            align="center"
            label="特殊事项"
            prop="specialMatter"
            v-if="item.name === '特殊事项'"
          />
          <el-table-column
            align="center"
            label="备注"
            prop="remark"
            v-if="item.name === '备注'"
          />
        </div>
        <el-table-column align="center" label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, 'edit')">
              修改
            </el-button>
            <el-button type="text" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <zcglView ref="edit" @fetchData="fetchData"></zcglView>
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
  import zcglView from './components/zcglView.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import FileUpload from '@/components/FileUploadZhgl.vue'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import {
    getList,
    deleteInfo,
    downloadTemplateFn,
    downloadAssest,
  } from '@/oapi/ypns_zhgl/zygl'
  export default {
    components: {
      zcglView,
      filterTable,
      filterSearch,
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
          specificationType: null,
          assetName: null,
        },
        headers: { token: token },
        baseApi: baseURL,
        filedAll: [
          { name: '资产编码' },
          { name: '资产名称' },
          { name: '规格型号' },
          { name: '制造厂家' },
          { name: '所属单位编码' },
          { name: '所属单位名称' },
          { name: '车牌井号' },
          { name: '自编号' },
          { name: '出厂编号' },
          { name: '出厂、建筑或完井日期' },
          { name: '投产日期' },
          { name: '区块' },
          { name: '区块名称' },
          { name: '存放（安装）地点' },
          { name: '计量单位' },
          { name: '复合数量' },
          { name: '功率能力' },
          { name: '技术状况名称' },
          { name: '使用状态名称' },
          { name: '增加原因名称' },
          { name: '资金渠道名称' },
          { name: '增加日期' },
          { name: '停产日期' },
          { name: '保管人' },
          { name: '期末原值' },
          { name: '期末累计折旧' },
          { name: '期末净值' },
          { name: '期末减值准备' },
          { name: '在用人员' },
          { name: '在用部门' },
          { name: '特殊事项' },
          { name: '备注' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'oilAudit-zhgl-zcgl-search',
        tableKey: 'oilAudit-zhgl-zcgl-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        type: [
          '上移资产',
          '审计部上市',
          '审计部未上市',
          '审计中心上市',
          '审计中心未上市',
        ],
        select: [],
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
      // 导出资产
      async exportDefault() {
        const ids = this.select.map((res) => res.id)
        const data = await downloadAssest({
          assetName: this.queryForm.assetName,
          specificationType: this.queryForm.specificationType,
          assetCode: this.queryForm.assetCode,
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          ids,
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
      // 下载模板
      async downLoadTemplate() {
        const data = await downloadTemplateFn()
        console.log(data)
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
      async downloadList() {
        const data = await downloadTemplateFn()
        console.log(data)
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
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '资产名称', key: 'name' },
          { name: '规格型号', key: 'code' },
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
        this.setCheckedRows()
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
          specificationType: null,
          assetName: null,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      handleView(row) {
        this.$refs['check'].showEdit(row)
      },
      // 导入成功
      handleSuccess(file) {
        if (file.code == 200) {
          this.$baseMessage('导入成功', 'success')
          this.fetchData()
        }
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
