<template>
  <div class="system-log-container">
    <div class="lr-layout">
      <div class="left1">
        <type-tree
          @fetch-data="treeData"
          :editable="true"
          :tableData="list"
          ref="typeTree"
          @all-data="getTreeAllData"
          @nodeData="getNodeData"
        />
      </div>
      <div class="right">
        <vab-query-form>
          <el-card shadow="never">
            <vab-query-form-top-panel>
              <el-form
                ref="form"
                checkable
                :inline="true"
                label-width="0"
                :model="queryForm"
                @submit.native.prevent
              >
                <el-form-item
                  v-for="(item, index) in searchItem"
                  :key="index"
                  :prop="item.key"
                >
                  <el-input
                    v-model="queryForm.risknumber"
                    clearable
                    placeholder="风险编号"
                    v-if="item.name === '风险编号'"
                  />
                  <el-input
                    v-model="queryForm.riskname"
                    clearable
                    placeholder="风险名称"
                    v-if="item.name === '风险名称'"
                  />

                  <el-select
                    v-if="item.name === '流程名称'"
                    v-model="queryForm.flowname"
                    clearable
                    placeholder="请选择流程名称"
                    :style="{ width: '100%' }"
                  >
                    <el-option
                      v-for="item in riskProcesList"
                      :key="item.processno"
                      :label="item.processname"
                      :value="item.processname"
                    />
                  </el-select>
                  <el-select
                    v-if="item.name === '关闭状态'"
                    v-model="queryForm.closestatus"
                    clearable
                    placeholder="请选择关闭状态"
                    :style="{ width: '100%' }"
                  >
                    <el-option label="已关闭" value="0" />
                    <el-option label="未关闭" value="1" />
                  </el-select>
                  <el-input
                    v-model="queryForm.busname"
                    clearable
                    placeholder="业务名称"
                    v-if="item.name === '业务名称'"
                  />
                  <!-- <el-cascader
                    v-model="queryForm.belongstoModel"
                    clearable
                    placeholder="请选择主责部门"
                    :style="{ width: '100%' }"
                    :options="belongstoTextOptions"
                    :props="{ checkStrictly: true }"
                    :show-all-levels="false"
                    v-if="item.name === '主责部门'"
                  ></el-cascader> -->
                  <el-input
                    v-model="queryForm.belongstoModel"
                    placeholder="请选择主责部门"
                    :style="{ width: '100%' }"
                    v-if="item.name === '主责部门'"
                    @click.native="handleShowBelongStoModel"
                  ></el-input>
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
                    type="primary"
                    native-type="submit"
                    @click="resetQueryForm"
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
                    :class="
                      searchMore ? 'search-more is-opened' : 'search-more'
                    "
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
          <vab-query-form-right-panel class="option-row">
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
            <el-button type="primary" @click="handleDownloadTemplate">
              下载模板
            </el-button>
            <el-upload
              style="margin: 0 10px; display: inline-block"
              class="upload-demo"
              :show-file-list="false"
              :action="baseApi + api"
              :headers="headers"
              :on-success="handleSuccess"
            >
              <el-button type="success">导入</el-button>
            </el-upload>
            <el-button type="success" @click="handleAdd">新建</el-button>
            <!-- <el-button type="primary" @click="copyItem">从行业复制</el-button> -->
          </vab-query-form-right-panel>

          <el-table v-loading="listLoading" :data="list">
            <el-table-column
              type="selection"
              width="55"
              disabled
            ></el-table-column>
            <el-table-column align="center" label="风险编号" prop="risknumber">
              <template #default="{ row }">
                <el-button type="text" @click="handleRead(row)">
                  {{ row.risknumber }}
                </el-button>
              </template>
            </el-table-column>
            <div v-for="(item, index) in filedNow" :key="index">
              <el-table-column
                align="center"
                label="风险名称"
                prop="riskname"
                show-overflow-tooltip
                v-if="item.name === '风险名称'"
              />
              <el-table-column
                align="center"
                label="风险描述"
                prop="riskdes"
                show-overflow-tooltip
                v-if="item.name === '风险描述'"
              />
              <el-table-column
                align="center"
                label="关闭状态"
                prop="riskdes"
                show-overflow-tooltip
                v-if="item.name === '关闭状态'"
              >
                <template #default="{ row }">
                  {{ row.closestatus == 6 ? '已关闭' : '未关闭' }}
                </template>
              </el-table-column>
              <el-table-column
                align="center"
                label="创建时间"
                prop="riskcreatedt"
                show-overflow-tooltip
                v-if="item.name === '创建时间'"
              />
              <el-table-column
                align="center"
                label="状态"
                prop="status"
                v-if="item.name === '状态'"
              >
                <template #default="{ row }">
                  {{
                    row.status == 1
                      ? '审批中'
                      : row.status == 2
                      ? '需调整'
                      : row.status == 3
                      ? '已撤销'
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
            <el-table-column align="center" label="操作">
              <template #default="{ row }">
                <el-button
                  type="text"
                  @click="handleEdit(row)"
                  :disabled="
                    (row.status && row.status != 0) ||
                    (!couldAdd && createId != row.staffid)
                  "
                >
                  修改
                </el-button>

                <el-dropdown style="margin-left: 10px" @command="handleCommand">
                  <el-button type="text">更多</el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item
                      @click.native="handleManage(row)"
                      :disabled="!+row.status"
                    >
                      <el-button type="text" :disabled="!+row.status">
                        办理
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleApproval(row)"
                      :disabled="!!+row.status"
                    >
                      <el-button
                        type="text"
                        :disabled="!!+row.status || createId != row.staffid"
                      >
                        提交审批
                      </el-button>
                    </el-dropdown-item>
                    <el-dropdown-item
                      @click.native="handleCommand({ row, type: 'del' })"
                      :disabled="!!+row.status"
                    >
                      <el-button
                        type="text"
                        :disabled="!!+row.status || createId != row.staffid"
                      >
                        删除
                      </el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
        </el-card>

        <el-pagination
          background
          class="pager"
          :current-page="queryForm.pageNo"
          :layout="layout"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <RiskEdit ref="edit" :treeId="treeId" @fetch-data="fetchData" />
    <RiskRead ref="read" />
    <copy-to-industry ref="copyToIndustry" />
    <industry-copy ref="industryCopy" />
    <el-dialog
      title="部门"
      :visible.sync="visibile"
      width="30%"
      :before-close="handleClose"
    >
      <el-tree
        :data="belongstoTextOptions"
        :expand-on-click-node="false"
        node-key="value"
        :props="{
          children: 'children',
          label: 'label',
        }"
        @node-click="handleNodeClick"
      />
    </el-dialog>
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />
  </div>
</template>

<script>
  import {
    getCreationList,
    getCreationDel,
    getCreationExport,
    download,
    generateReport,
  } from '@/api/risk'

  import { doDelete } from '@/api/table'
  import RiskEdit from './components/RiskEdit'
  import RiskRead from './components/RiskRead.vue'
  import TypeTree from '@/views/risk/identify/components/TypeTree.vue'
  import CopyToIndustry from '@/views/risk/components/CopyToIndustry'
  import IndustryCopy from '@/views/risk/components/IndustryCopy'
  import { formatOptions } from '@/utils/validate'
  import { zgjkLeft } from '@/api/setting/org'
  import { UTCformat } from '@/utils'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  import {
    getContractTypes,
    getFlowPkInfo,
    getRiskProcess,
  } from '@/api/contract/manage'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList'
  export default {
    name: 'Fillin',
    components: {
      RiskEdit,
      RiskRead,
      TypeTree,
      CopyToIndustry,
      IndustryCopy,
      filterTable,
      filterSearch,
      ProcessList,
      WfqdDeal,
    },

    data() {
      return {
        baseApi: baseURL,
        api: '/riskcontrol/risk/importRiskInfo',
        headers: { token },
        visibile: false,
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        treeId: '',
        queryForm: {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: undefined,
          pageNo: 1,
          pageSize: 20,
          riskcatid: '',
          flowname: '',
          busname: '',
          closestatus: '',
        },
        riskcategory: {},
        belongstoTextOptions: [],
        searchAll: this.getFiled(), //所有搜索项
        localKey: 'risk-identify-creation-search',
        tableKey: 'risk-identify-creation-list',
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
        searchMore: true,
        filedAll: [
          { name: '风险名称' },
          { name: '风险描述' },
          { name: '创建时间' },
          { name: '关闭状态' },
          { name: '状态' },
        ], //所有表格项
        filedNow: [],
        selectList: [],
        riskProcesList: [],
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
        nodeData: [],
        couldAdd: false,
      }
    },
    created() {
      this.getBelongstoTextOptions()
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initSearch()
      this.initTable()
      this.getAssociationInfo()
      const isAdmin = JSON.parse(localStorage.getItem('userInfo')).roleNames

      if (isAdmin.includes('风险管理员')) {
        console.log(isAdmin, 'isAdmin1111')
        this.couldAdd = true
      }
    },
    /**
     * @description: 流程提交回调
     * @return {*}
     */
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        console.log('qwe')
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      //导入成功
      handleSuccess(response) {
        console.log('🚀 ~ handleSuccess ~ response:', response)
        if (response.code == 1) {
          if (response.data.data != '') {
            this.$baseMessage(response.data.data || '导入失败', 'error')
          } else {
            this.fetchData()
            this.$baseMessage('导入成功', 'success')
          }
        } else {
          this.$baseMessage(response.data.data || '导入失败', 'error')
        }
      },
      /**
       * @description: 提交审批
       * @return {*}
       */
      handleApproval(row) {
        //提交审批
        this.$refs['process'].save(90, row.riskid)
      },
      /**
       * @description: 办理
       * @return {*}
       */
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.riskid,
          tableId: 90,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
      /**
       * @description: 选择部门
       * @return {*}
       */
      handleNodeClick(e) {
        console.log(e, 'e====')
        this.queryForm.belongstoModel = e.label
        this.queryForm.belongsto = e.value
        this.visibile = false
      },
      /**
       * @description: 关闭选择部门页面
       * @return {*}
       */
      handleClose() {
        this.visibile = false
      },
      //handleShowBelongStoModel
      /**
       * @description: 打开选择部门页面
       * @return {*}
       */
      handleShowBelongStoModel() {
        this.visibile = true
        console.log(this.belongstoTextOptions)
      },
      // 定义表单所有项
      getFiled() {
        let fields = [
          { name: '风险编号', key: 'risknumber' },
          { name: '风险名称', key: 'riskname' },
          { name: '流程名称', key: 'flowname' },
          { name: '业务名称', key: 'busname' },
          { name: '关闭状态', key: 'closestatus' },
          { name: '主责部门', key: 'belongstoModel' },
        ]
        return fields
      },
      /**
       * @description: 初始化 搜索
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
                this.queryForm[x.key] = ''
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
      showMore() {
        this.searchMore = !this.searchMore
        console.log(this.searchMore)
        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      },
      copyItem() {
        this.$refs.industryCopy.showEdit(this.queryForm.riskcatid, '1')
      },
      /**
       * @description: 分页
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      //获取当前节点数据和id
      treeData(id) {
        this.treeId = id
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      //获取当前节点数据
      getNodeData(data) {
        this.nodeData = data
      },
      /**
       * @description: 重置
       * @return {*}
       */
      resetQueryForm() {
        // this.$refs['spd'].show()
        this.queryForm = {
          risknumber: '',
          riskname: '',
          belongsto: '',
          belongstoModel: undefined,
          pageNumber: 1,
          pageSize: 20,
          flowname: '',
          busname: '',
          closestatus: '',
        }
        this.fetchData()
      },
      /**
       * @description: 请求数据
       * @return {*}
       */
      async fetchData() {
        this.listLoading = true
        this.queryForm.riskcatid =
          typeof this.treeId == 'string' || typeof this.treeId == 'number'
            ? this.treeId
            : ''
        // const belongstoModel = this.queryForm.belongstoModel
        // this.queryForm.belongsto =
        //   belongstoModel.length > 0 && belongstoModel
        //     ? belongstoModel[belongstoModel.length - 1]
        //     : ''
        const {
          data: {
            pageBean: { list, total },
            riskcategory,
          },
        } = await getCreationList(this.queryForm)
        list.map((v) => {
          v.riskcreatedt = UTCformat(v.riskcreatedt)
          return v
        })
        this.list = list
        this.total = total
        this.riskcategory = riskcategory
        this.listLoading = false
      },
      async getBelongstoTextOptions() {
        const result = await zgjkLeft()
        let newValue = formatOptions(result, 'name', 'id')
        this.belongstoTextOptions = newValue
      },
      handlePreview() {},
      handleReport() {},
      handleExport() {},
      /**
       * @description: 打开编辑
       * @return {*}
       */
      handleEdit(row) {
        // this.$refs['edit'].showEdit({ category: this.riskcategory, row })
        this.$refs['edit'].showEdit(row, 'edit')
      },
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/风险识别信息导入模板.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '风险识别信息导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
      /**
       * @description: 打开新建
       * @return {*}
       */
      async handleAdd() {
        console.log('🚀 ~ handleAdd ~ this.nodeData:', this.nodeData)

        // Check if a node is selected
        if (!this.nodeData || Object.keys(this.nodeData).length === 0) {
          this.$baseMessage('请先选择一个分类节点', 'warning')
          return
        }

        // Check if current node has children - if it does, we can't create here
        if (
          this.nodeData &&
          this.nodeData.children &&
          this.nodeData.children.length > 0
        ) {
          this.$baseMessage('请在最下级创建！', 'error')
          return
        }

        // 选择当前节点信息
        const nodeInfo = this.nodeData

        // 查找当前节点的父节点
        const parentNode = this.findParentNode(this.treeAllData[0], this.treeId)
        console.log('父节点信息:', parentNode)

        // 创建结果对象
        const resultData = { ...nodeInfo }

        // 如果父节点是风险类型
        if (parentNode && parentNode.riskcatname === '风险类型') {
          // 如果是二级风险(企业风险、业务风险等)，风险领域设为当前节点，二级风险为空
          resultData.riskcatname = nodeInfo.label // 风险领域为当前节点名称
          resultData.riskcatnametwo = '' // 二级风险为空
          resultData.fatherriskcatid = parentNode.riskcatid
          resultData.fatherriskcatname = parentNode.riskcatname
        } else {
          // 正常情况，风险领域为父节点，二级风险为当前节点
          resultData.fatherriskcatid = parentNode ? parentNode.riskcatid : 0
          resultData.fatherriskcatname = parentNode
            ? parentNode.riskcatname
            : ''
          resultData.riskcatname = parentNode ? parentNode.riskcatname : ''
          resultData.riskcatnametwo = nodeInfo.label
        }

        console.log('创建风险数据:', resultData)
        this.$refs['edit'].showEdit(resultData, 'add')
      },

      // 查找父节点
      findParentNode(treeData, nodeId) {
        // 递归查找函数
        const findNode = (node, id, parent = null) => {
          if (!node) return null

          if (node.riskcatid === id) {
            return parent
          }

          if (node.children && node.children.length) {
            for (const child of node.children) {
              const result = findNode(child, id, node)
              if (result) return result
            }
          }

          return null
        }

        return findNode(treeData, nodeId, null)
      },
      //获取节点信息
      getBottomNode(tree) {
        console.log('🚀 ~ getBottomNode ~ tree:', tree)
        // 清空列表，避免重复累加
        this.selectList = []
        tree.forEach((item) => {
          if (item.children && item.children.length > 0) {
            this.getBottomNode(item.children)
          } else {
            this.selectList.push(item)
          }
        })
      },
      //获取所有节点
      getTreeAllData(data) {
        this.queryForm.pageNo = 1
        this.treeAllData = data
      },
      /**
       * @description: 打开详情
       * @return {*}
       */
      handleRead(row) {
        // this.$refs['read'].showRead({ category: this.riskcategory, row })
        // this.$refs['read'].showRead(row, this.queryForm.riskcatid)
        this.$refs['edit'].showEdit(row, this.queryForm.riskcatid, true)
      },
      /**
       * @description: 删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      handleSendTo(row) {
        console.log(row)
      },
      handleCopyFrom(row) {
        console.log(row)
      },
      /**
       * @description: 操作
       * @return {*}
       */
      handleCommand(command) {
        switch (command.type) {
          case 'look':
            console.log('预览')
            break
          case 'del':
            this.handleDelItem(command.row)
            break
          case 'generate':
            console.log('生成报告', command)
            getCreationExport({
              reportType: 'fygk',
              id: command.row.riskid,
            }).then((res) => {
              if (res.code === 200) {
                generateReport({
                  reportType: 'fygk',
                  id: command.row.riskid,
                }).then((res1) => {
                  console.log('res111', res1)
                })
              }
            })
            break
          case 'export':
            console.log('导出报告')
            getCreationExport({
              reportType: 'fygk',
              id: command.row.riskid,
            }).then((res) => {
              if (res.code === 200) {
                download({ reportType: 'fygk', id: command.row.riskid }).then(
                  (res1) => {
                    console.log('res111', res1)
                  }
                )
              }
            })
            break
          case 'copy':
            this.$refs.industryCopy.showEdit(command.row.riskid, '2')
            console.log('复制')
            break
          default:
            break
        }
      },
      handleDelItem(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await getCreationDel({
            riskcatid: row.riskcatid,
            riskid: row.riskid,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            await this.fetchData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      async getAssociationInfo() {
        let res = await getRiskProcess()
        this.riskProcesList = res.data
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    padding: 0 !important;
  }
  .secondCard {
    margin-top: -5px !important;
  }
  .lr-layout {
    background: #f6f8f9;
    display: flex;
    gap: 15px;
    min-height: 0;
  }

  .lr-layout > .left1 {
    flex: 0 0 25%;
    min-width: 200px;
    max-width: 300px;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding: 20px 10px 20px 10px;
    background: #ffffff;
  }

  .lr-layout > .right {
    flex: 1;
    min-width: 0;
    overflow: hidden;
  }

  .option-row {
    width: 100%;
    margin-bottom: 20px;
  }
  .pager {
    margin-bottom: 20px !important;
  }

  @media (max-width: 1200px) {
    .lr-layout > .left1 {
      flex: 0 0 200px;
    }
  }

  @media (max-width: 768px) {
    .lr-layout {
      flex-direction: column;
    }

    .lr-layout > .left1 {
      flex: none;
      max-width: none;
    }

    .lr-layout > .right {
      overflow: visible;
    }
  }
</style>
