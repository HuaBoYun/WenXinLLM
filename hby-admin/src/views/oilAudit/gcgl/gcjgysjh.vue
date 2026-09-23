<template>
  <!-- 工程项目验收计划 -->
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
                v-model="queryForm.xmmc"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-date-picker
                v-model="queryForm.queryYear"
                type="year"
                format="yyyy"
                value-format="yyyy"
                placeholder="年份"
                v-if="item.name === '年份'"
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
        <el-button type="success" @click="handleEdit(null)">新增</el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :data="{ isCover: isCover }"
          :headers="headers"
          :before-upload="beforeUpload"
          :on-success="handleSuccess"
        >
          <el-button type="success">导入</el-button>
        </el-upload>
        <el-button type="success" @click="handleExport()">导出</el-button>
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="55" />
        <el-table-column align="center" label="序号" prop="gcxmjgysjhNo" />
        <el-table-column align="center" label="项目名称" type="xmmc">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row, true)">
              {{ row.xmmc }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column width="1" />
        <!-- <div v-for="(item, index) in filedNow" :key="index"></div> -->
        <el-table-column
          align="center"
          label="建设单位"
          prop="jsdw"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目类别"
          prop="xmlb"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目总投资（万元）"
          prop="xmztzje"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="项目投产时间"
          prop="xmtcsj"
          show-overflow-tooltip
        />
        <el-table-column
          align="center"
          label="生产考核完成时间"
          prop="sckhwcsj"
          show-overflow-tooltip
          :formatter="formatDate"
        />
        <el-table-column align="center" label="专项验收">
          <el-table-column
            align="center"
            label="消防设施验收-计划完成时间"
            prop="xfssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="环境保护验收-计划完成时间"
            prop="hjbhysjhwcsj"
          />
          <el-table-column
            align="center"
            label="安全设施验收-计划完成时间"
            prop="aqssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="职业病防护设施验收-计划完成时间"
            prop="zybfhssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="水土保持设施验收-计划完成时间"
            prop="stbcssysjhwcsj"
          />
          <el-table-column
            align="center"
            label="土地利用验收-计划完成时间"
            prop="tdlyysjhwcsj"
          />
          <el-table-column
            align="center"
            label="节能验收-计划完成时间"
            prop="jnysjhwcsj"
          />
          <el-table-column
            align="center"
            label="安全防范系统验收-计划完成时间"
            prop="aqffxtysjhwcsj"
          />
          <el-table-column
            align="center"
            label="雷电防护装置验收-计划完成时间"
            prop="ldfhzzysjhwcsj"
          />
          <el-table-column
            align="center"
            label="档案验收-计划完成时间"
            prop="daysjhwcsj"
          />
          <el-table-column
            align="center"
            label="竣工决算验收-上报审计时间"
            prop="jgjsyssbsjsj"
          />
        </el-table-column>
        <el-table-column
          align="center"
          label="项目结算验收-计划完成时间"
          prop="xmjsysjhwcsj"
        />
        <el-table-column
          align="center"
          label="初步验收验收-计划完成时间"
          prop="cbysjhwcsj"
        />
        <el-table-column
          align="center"
          label="竣工验收验收-计划完成时间"
          prop="jgysjhwcsj"
        />
        <el-table-column
          align="left"
          label="备注"
          prop="bz"
          show-overflow-tooltip
        />
        <el-table-column width="1" />

        <el-table-column
          label="操作"
          #default="{ row }"
          fixed="right"
          align="center"
          width="90"
        >
          <el-button type="text" @click="handleEdit(row)">修改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
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
    <gcjgysjhEdit ref="edit" @fetchData="fetchData" />
  </div>
</template>

<script>
  import {
    engineeringProjectExaminePlanList,
    engineeringProjectExaminePlanDelete,
    exportList,
  } from '@/oapi/audit/plan'
  import gcjgysjhEdit from './components/gcjgysjhEdit'
  import { formatDate } from '@/utils/index'

  import filterSearch from '@/components/filterSearch'
  import filterTable from '@/components/filterTable'
  import { searchTableMixis } from '@/mixis/index'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    name: 'gcjgysjh',
    components: { filterSearch, filterTable, gcjgysjhEdit },

    mixins: [searchTableMixis],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/gcxmjgysjh/importData',
        headers: { token },
        select: [],
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        isCover: 0,
        queryForm: {
          gcxmjgysjhNo: '',
          queryYear: '',
          xmmc: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          // { name: '建设单位' },
          // { name: '项目类别' },
          // { name: '项目总投资（万元）' },
          // { name: '项目投产时间' },
          // { name: '生产考核完成时间' },
          // { name: '专项验收' },
          // { name: '项目结算验收-计划完成时间' },
          // { name: '初步验收验收-计划完成时间' },
          // { name: '竣工验收验收-计划完成时间' },
          // { name: '备注' },
        ], //所有表格项
        filedNow: [], //当前表格项
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        localKey: 'oilAudit-gcgl-gcjgysjh-search',
        tableKey: 'oilAudit-gcgl-gcjgysjh-list',
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
      handleSuccess(response) {
        if (response.data == '1') {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg || '导入失败', 'error')
        }
      },
      handleSelectionChange(val) {
        this.select = val;
      },
      async handleExport() {
        const ids = this.select.map((res) => res.gcxmjgysjhid)
        const data = await exportList({...this.queryForm, ids: ids.join()})
        let fileName = '工程竣工验收计划表'
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
      getFiled() {
        return [
          { name: '项目名称', key: 'projectName' },
          { name: '年份', key: 'queryYear' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          xmmc: '',
          gcxmjgysjhNo: '',
          queryYear: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
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
          code,
        } = await engineeringProjectExaminePlanList(this.queryForm)
        if (code === 1) {
          this.list = tlist || []
          this.total = totalRecord || 0
          this.listLoading = false
        }
      },
      async handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await engineeringProjectExaminePlanDelete({
            ids: row.gcxmjgysjhid,
          })
          if (code === 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      beforeUpload() {
        return new Promise((resolve, reject) => {
          this.$baseConfirm(
            '是否根据序号覆盖已存在的数据',
            null,
            () => {
              console.log('s是')
              this.isCover = 1
              resolve()
            },
            () => {
              console.log('s否')
              this.isCover = 0
              resolve()
            },
            '是',
            '否'
          )
        })
      },
      async handleEdit(row, disabled) {
        this.$refs['edit'].showEdit(row, disabled)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
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
