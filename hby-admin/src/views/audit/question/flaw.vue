<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-top-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-input
              v-model="queryForm.bugnumber"
              clearable
              placeholder="缺陷编号"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.defectsname"
              clearable
              placeholder="缺陷名称"
            />
          </el-form-item>
          <!-- <el-form-item>
            <el-select
              v-model="queryForm.bugcriid"
              placeholder="请输入缺陷等级"
            >
              <el-option
                v-for="item in bugcriidList"
                :label="item.bugcrilevel"
                :value="item.bugcriid"
                :key="item.bugcriid"
              />
            </el-select>
          </el-form-item> -->
          <!-- <el-form-item>
                <el-select
                  v-model="queryForm.bugcrilevel"
                  placeholder="请输入缺陷性质"
                >
                  <el-option label="设计缺陷" value="设计缺陷" key="设计缺陷" />
                  <el-option label="执行缺陷" value="执行缺陷" key="执行缺陷" />
                </el-select>
              </el-form-item> -->
          <el-form-item>
            <el-date-picker
              v-model="queryForm.Date"
              clearable
              end-placeholder="发生结束日期"
              format="yyyy-MM-dd"
              range-separator="-"
              start-placeholder="发生开始日期"
              :style="{ width: '100%' }"
              type="daterange"
              value-format="yyyy-MM-dd"
            />
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
      <vab-query-form-left-panel>
        <span></span>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button
          type="primary"
          @click="handleDownloadTemplate"
          style="margin-bottom: 10px; margin-right: 10px"
        >
          下载模板
        </el-button>
        <el-upload
          class="upload-demo"
          :show-file-list="false"
          :action="baseApi + api"
          :headers="headers"
          :on-success="handleSuccess"
        >
          <el-button
            type="success"
            style="margin-bottom: 10px; margin-right: 10px"
          >
            导入
          </el-button>
        </el-upload>
        <el-button type="success" @click="handleAdd">新建</el-button>
        <el-button type="success" @click="handleExport">导出</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table v-loading="listLoading" :data="list">
      <el-table-column
        align="center"
        label="缺陷编号"
        prop="bugnumber"
        width="170"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.bugnumber }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="缺陷名称"
        prop="defectsname"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="缺陷描述"
        prop="bugdescripte"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="发生时间"
        prop="discovertime"
        show-overflow-tooltip
        :formatter="formatDate"
      />

      <el-table-column
        align="center"
        label="缺陷等级"
        prop="bugcrilevel"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="状态" prop="status">
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
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="120"
      >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleEdit(row)"
            :disabled="!!+row.status || createId != row.createstaffid"
          >
            修改
          </el-button>
          <el-dropdown style="margin-left: 10px">
            <el-button type="text">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleManage(row)"
                  :disabled="!+row.status"
                >
                  办理
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  @click="handleApproval(row)"
                  :disabled="
                    !!+row.status || btnLoading || createId != row.createstaffid
                  "
                >
                  提交审批
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  type="text"
                  :disabled="!!+row.status || createId != row.createstaffid"
                  @click="handleDelete(row)"
                >
                  删除
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <flaw-info ref="edit" @fetch-data="fetchData" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <WfqdDeal ref="wfqdDeal" />
  </div>
</template>

<script>
  import { findOrganizationByTreeAllss } from '@/api/audit/implement'
  import {
    bugcriidList,
    defectDel,
    defectDetail,
    defectFileExport,
    defectList,
  } from '@/api/audit/question'
  import { formatDay, parseTime } from '@/utils/index'
  import FlawInfo from './components/FlawInfo'
  import ProjectDataTree from './components/ProjectDataTree'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal.vue'
  import { getFlowPkInfo } from '@/api/contract/manage.js'
  import { baseURL } from '@/config'
  import store from '@/store'
  const token = store.getters['user/token']
  export default {
    name: 'Download',
    components: { FlawInfo, ProjectDataTree, ProcessList, WfqdDeal },
    data() {
      return {
        baseApi: baseURL,
        api: '/audit/auditPS/sjbb/import',
        headers: { token },
        list: [],
        bugcriidList: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          bugnumber: '',
          bugcriid: '',
          orgid: '',
          defectsname: '',
          Date: [],
          pageNumber: 1,
          pageSize: 20,
        },
        orgId: '',
        current: undefined,
        flagTitle: false,
        btnLoading: false,
        createId: JSON.parse(localStorage.getItem('userInfo')).staffid,
      }
    },
    created() {
      this.fetchData()
      this.fectchBugcriidList()
      //初始化树结构
      this.getTreeData()
    },
    methods: {
      //审批
      handleApproval(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        try {
          this.btnLoading = true
          this.$refs['process'].save(209, row.bugid)
        } catch (error) {
          this.btnLoading = false
        }
      },
      async handleManage(row) {
        this.listLoading = true
        const res = await getFlowPkInfo({
          formId: row.bugid,
          tableId: 209,
        })
        this.listLoading = false
        this.$refs['wfqdDeal'].show(res.data, false)
      },
      async getTreeData() {
        let res = await findOrganizationByTreeAllss()
        this.orgId = res[0].id
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      async fectchBugcriidList() {
        const res = await bugcriidList()
        this.bugcriidList = res.data.list
      },
      setTree(node) {
        let id = node.pId === 1 ? undefined : node.id
        this.queryForm.orgid = id
        this.orgId = node.id
        this.fetchData()
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDay(data)
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
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        try {
          const {
            data: {
              pageInfo: { tlist: list, totalRecord: total },
            },
          } = await defectList({ ...other, startDate, endDate })
          this.list = list
          this.list.forEach((item) => {
            if (item.discovertime) {
              item.discovertime = parseTime(item.discovertime, '{y}-{m}-{d}')
            }
          })
          this.total = total
        } catch (error) {
          console.error('Error fetching data:', error)
          // 你可以在这里处理错误，比如显示错误提示
        } finally {
          this.listLoading = false
        }
      },
      /**
       * @description: 打开新增表单弹框
       * @return {*}
       */
      handleAdd() {
        this.$refs['edit'].showEdit('add', null)
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        const { data } = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('detail', data.bug)
      },
      /**
       * @description: 打开编辑表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleEdit(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        const { data } = await defectDetail({ bugid: row.bugid })
        await this.$refs['edit'].showEdit('edit', data.bug)
      },
      /**
       * @description: 删除列表数据
       * @param {*} row 当前行数据
       * @return {*}
       */
      handleDelete(row) {
        // if (this.createId != row.createstaffid) {
        //   return this.$message.error('只有创建人可以操作')
        // }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await defectDel({
            bugid: row.bugid,
          })
          if (code == 1) {
            this.$baseMessage(msg, 'success')
          } else {
            this.$baseMessage(msg, 'error')
          }
          await this.fetchData()
        })
      },
      handleSuccess(response) {
        if (response.data == '200') {
          this.fetchData()
          this.$baseMessage('导入成功', 'success')
        } else {
          this.$baseMessage(response.msg, 'error')
        }
      },
      async handleExport(row) {
        const { Date, ...other } = this.queryForm
        let startDate = ''
        let endDate = ''
        if (Date) {
          startDate = Date[0]
          endDate = Date[1]
        }
        const data = await defectFileExport({
          ...other,
          startDate,
          endDate,
          orgId: this.orgId,
        })
        let fileName = '评价缺陷.xlsx'
        let blob = new Blob([data], {
          type: 'application/vnd.ms-excel',
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
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleDownloadTemplate() {
        // 获取当前域名和协议
        const baseUrl = window.location.origin
        // 拼接完整的文件URL
        const fileUrl = `${baseUrl}/files/评价缺陷导入模板.xlsx`

        // 创建一个隐藏的a标签用于下载
        const link = document.createElement('a')
        link.href = fileUrl
        link.setAttribute('download', '评价缺陷导入模板.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }

  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    /* width: 200px; */
    width: 15%;
    border-right: 1px solid ghostwhite;
    margin-right: 10px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 85%;
  }
</style>
