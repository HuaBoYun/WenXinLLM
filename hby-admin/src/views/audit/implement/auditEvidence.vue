<template>
  <div class="system-log-container">
    <vab-query-form>
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
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
                v-if="item.name === '项目名称'"
              />
              <el-input
                v-model="queryForm.auditMatter"
                clearable
                placeholder="审计事项"
                v-if="item.name === '审计事项'"
              />
              <el-input
                v-model="queryForm.auditAbstract"
                clearable
                placeholder="审计事项摘要"
                v-if="item.name === '审计事项摘要'"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                icon="el-icon-search"
                native-type="submit"
                type="primary"
                @click="queryData"
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
        </vab-query-form-top-panel>
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
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @select-all="handleSelectAll"
        @select="handleSelection"
        ref="multipleTable"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column align="center" label="项目名称" prop="projectName">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.prjoectname }}
            </el-button>
          </template>
        </el-table-column>

        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            v-if="item.name === '审计事项'"
            label="审计事项"
            prop="auditMatter"
          />
          <el-table-column
            align="center"
            v-if="item.name === '审计事项摘要'"
            label="审计事项摘要"
            prop="auditAbstract"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '证据提供单位意见'"
            label="证据提供单位意见"
            prop="evidenceOpinion"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '证据提供者'"
            label="证据提供者"
            prop="certificateUser"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            v-if="item.name === '审计日期'"
            label="审计日期"
            prop="createDate"
            show-overflow-tooltip
            :formatter="formatDate"
          />
          <el-table-column
            align="center"
            v-if="item.name === '状态'"
            label="状态"
            prop="status"
          >
            <template #default="{ row }">
              {{
                row.status == 1
                  ? '审批中'
                  : row.status == 2
                  ? '已退回'
                  : row.status == 3
                  ? '已撤回'
                  : row.status == 4
                  ? '已终止'
                  : row.status == 5
                  ? '已跟踪'
                  : row.status == 6
                  ? '已完成'
                  : '未审批'
              }}
            </template>
          </el-table-column>
        </div>
        <el-table-column label="操作" fixed="right" align="center">
          <template #default="{ row }">
            <el-button
              type="text"
              @click="handleEdit(row)"
              :disabled="!!row.status || createId != row.createstaffid"
              v-if="id == row.auditUserId"
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!row.status"
                    @click.native="handleDeal(row)"
                  >
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="
                      !!row.status ||
                      btnLoading ||
                      createId != row.createstaffid
                    "
                    @click.native="handleShenPi(row)"
                    v-if="id == row.auditUserId"
                  >
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item>
                  <el-button
                    type="text"
                    :disabled="!!row.status || createId != row.createstaffid"
                    @click.native="handleDelete(row)"
                    v-if="id == row.auditUserId"
                  >
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleExport(row)">
                  <el-button type="text">导出</el-button>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
    <DoubtfulInfo
      ref="edit"
      @fetch-data="fetchData"
      :effectDetail="effectDetail"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
    <!-- <project-manage @projectManage="getChildlistPro" ref="manage" /> -->
  </div>
</template>

<script>
  import {
    createImPlementDetail,
    deleteImPlementOrder,
    getImPlementOrder,
    imPlementOrderDetail,
    qZDExport,
    sendDefect,
    sendDoubtful,
    sendManuscript,
    sendManuscriptGzdg,
    sendRisk,
    sjqzdXF,
  } from '@/api/audit/implement'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import store from '@/store'
  import { formatDay } from '@/utils/index'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/auditEvidenceInfo'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import projectManage from '@/components/selectPerson'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import { searchTableMixis } from '@/mixis/index'

  export default {
    name: 'Download',
    mixins: [searchTableMixis],
    components: {
      filterTable,
      filterSearch,
      DoubtfulInfo,
      ProcessList,
      WfqdDeal,
      projectManage,
    },
    data() {
      return {
        flawStatus: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          projectName: '',
          auditMatter: '',
          auditAbstract: '',
          pageNumber: 1,
          pageSize: 20,
        },
        token: store.getters['user/token'],
        effectDetail: {},
        id: '',
        select: [],
        // 筛选、表格头自定义
        filedAll: [
          { name: '审计事项' },
          { name: '审计事项摘要' },
          { name: '证据提供单位意见' },
          { name: '证据提供者' },
          { name: '审计日期' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'audit-implement-auditEvidence-search',
        tableKey: 'audit-implement-auditEvidence-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
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
      this.id = JSON.parse(localStorage.getItem('userInfo')).staffid
    },
    methods: {
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '项目名称', key: 'projectName' },
          { name: '审计事项', key: 'auditMatter' },
          { name: '审计事项摘要', key: 'auditAbstract' },
        ]
        return fields
      },
      /**
       * @description  时间格式化
       * @param {*}
       * @return {*}
       */
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
      },
      /**
       * @description  打开疑点
       * @param {*}
       * @return {*}
       */
      async sendToManuscript(row) {
        const data = await sendManuscript({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['manuscript'].showEdit('疑点', data.data)
      },
      async sendToManuscriptAtt(row) {
        const data = await sendManuscriptGzdg({
          auditStaff: row.editor,
          // auditedUnit: row.auditedunit,
          selectIds: row.dpointid,
          type: 'nbsj',
        })
      },
      async sendToDoubtful(row) {
        const data = await sendDoubtful({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['doubtful'].showEdit(data.data)
      },
      async sendToDefect(row) {
        this.flawStatus = true

        const data = await sendDefect({
          selectIds: row.dpointid,
          type: 'nbsj',
        })

        this.$nextTick(() => {
          this.$refs['flaw'].showEdit(data.data)
        })
      },
      async sendToRisk(row) {
        const data = await sendRisk({
          selectIds: row.dpointid,
          type: 'nbsj',
        })
        this.$refs['risk'].showEdit('发送至风险', data.data)
      },
      resetQueryForm() {
        this.queryForm = {
          projectName: '',
          auditMatter: '',
          auditAbstract: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
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
        this.btnLoading = false
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getImPlementOrder(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
        this.setCheckedRows()
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      async handleAdd() {
        this.$refs['edit'].showEdit('add')
        const result = await createImPlementDetail()
        this.effectDetail = result.data.pj
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const data = await imPlementOrderDetail({
          certificateId: row.certificateId,
        })
        this.$refs['edit'].showEdit('detail', data.data.certificate)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        const data = await imPlementOrderDetail({
          certificateId: row.certificateId,
        })
        await this.$refs['edit'].showEdit('edit', data.data.certificate)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await deleteImPlementOrder({
            certificateId: row.certificateId,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      async handleExport(row) {
        const data = await qZDExport({ certificateId: row.certificateId })

        let fileName = '审计取证单'
        let blob = new Blob([data], {
          type: 'application/msword',
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
      handleShenPi(row) {
        try {
          this.$baseConfirm('你确定要审核当前项吗', null, async () => {
            this.btnLoading = true
            const tableId = 16
            const fromId = row.certificateId
            this.$refs['process'].save(tableId, fromId)
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleDeal(row) {
        const res = await getFlowPkInfo({
          formId: row.certificateId,
          tableId: 16,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      handleSelection(val, row) {
        const i = this.select.findIndex(
          (x) => x.certificateId == row.certificateId
        )
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
            if (
              row &&
              !this.select.some((x) => x.certificateId == row.certificateId)
            ) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex(
              (x) => x.certificateId == row.certificateId
            )
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
                return row.certificateId == item.certificateId
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
