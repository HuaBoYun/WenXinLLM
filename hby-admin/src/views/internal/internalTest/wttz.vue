<template>
  <div class="system-log-container">
    <el-card shadow="never">
      <vab-query-form-left-panel :span="24">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-date-picker
              v-model="queryForm.testYear"
              type="year"
              placeholder="年度"
            ></el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.oneprocess"
              clearable
              placeholder="一级流程"
              style="width: 140px; margin-right: 20px"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.problemtype"
              clearable
              placeholder="问题类别"
              style="width: 140px; margin-right: 20px"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.defectlevel"
              clearable
              placeholder="缺陷等级"
              style="width: 140px; margin-right: 20px"
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
      </vab-query-form-left-panel>
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
        <el-button type="success" @click="handleExport">导出</el-button>

        <!-- <el-button type="success" @click="handleAdd">新建</el-button> -->
      </vab-query-form-right-panel>
      <el-table
        v-loading="listLoading"
        :data="list"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          align="center"
          label="一级流程"
          prop="oneprocess"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDeatil(row)">
              {{ row.oneprocess }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="item in filedNow" :key="item.findid">
          <el-table-column
            align="center"
            label="问题概述"
            v-if="item.name === '问题概述'"
            prop="problemmemo"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="缺陷具体描述"
            v-if="item.name === '缺陷具体描述'"
            prop="defectmemo"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="问题类别"
            v-if="item.name === '问题类别'"
            prop="problemtype"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="缺陷等级"
            v-if="item.name === '缺陷等级'"
            prop="defectlevel"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="定性依据"
            v-if="item.name === '定性依据'"
            prop="quabasis"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="主责部门"
            v-if="item.name === '主责部门'"
            prop="orgname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="反馈意见"
            v-if="item.name === '反馈意见'"
            prop="feedback"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="整改计划"
            v-if="item.name === '整改计划'"
            prop="reformplan"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="预计完成时间"
            v-if="item.name === '预计完成时间'"
            prop="estfinishdate"
            show-overflow-tooltip
            #default="{ row }"
          >
            {{ formatDate(row.estfinishdate) }}
          </el-table-column>
          <el-table-column
            align="center"
            label="整改落实人"
            v-if="item.name === '整改落实人'"
            prop="realname"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="转发人员"
            v-if="item.name === '转发人员'"
            prop="issuedStaffidName"
            show-overflow-tooltip
          />
          <el-table-column
            align="center"
            label="内控评价年度"
            v-if="item.name === '内控评价年度'"
            prop="testYear"
            show-overflow-tooltip
            #default="{ row }"
          >
            {{ row.testYear === 0 ? '' : row.testYear }}
          </el-table-column>
          <el-table-column
            align="center"
            label="审批状态"
            prop="status"
            v-if="item.name === '审批状态'"
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

        <el-table-column width="1" />

        <el-table-column align="center" label="操作" width="160">
          <template #default="{ row }">
            <div
              style="display: flex; justify-content: space-evenly"
              @click="saveInfo(row)"
            >
              <el-upload
                style="text-align: right; margin-bottom: 5px"
                class="upload-demo"
                :show-file-list="false"
                action=""
                :headers="headers"
                :on-success="handleSuccess"
                :file-list="fileList"
                :before-upload="handleBeforeUpload"
                :multiple="true"
              >
                <div style="margin-right: 10px">
                  <el-button type="success">点击上传</el-button>
                </div>
              </el-upload>

              <el-dropdown @command="handleCommand">
                <el-button type="text">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    :command="{ action: 'maintenance', row: row }"
                  >
                    维护
                  </el-dropdown-item>
                  <el-dropdown-item :command="{ action: 'forward', row: row }">
                    转发
                  </el-dropdown-item>
                  <el-dropdown-item
                    :command="{ action: 'forwardHistory', row: row }"
                  >
                    转发记录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

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
    <issueView ref="issue" @fetch-data="queryData" />

    <!-- 人员选择弹窗 -->
    <danxuanPerson ref="personDialog" @projectManage="handlePersonSelected" />

    <!-- 转发记录弹窗 -->
    <el-dialog
      title="转发记录"
      :visible.sync="forwardHistoryVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-table v-loading="forwardHistoryLoading" :data="forwardHistoryList">
        <el-table-column
          label="一级流程"
          prop="oneprocess"
          show-overflow-tooltip
        />
        <el-table-column
          label="转发人"
          prop="initialName"
          show-overflow-tooltip
        />
        <el-table-column label="被转发人" prop="toName" show-overflow-tooltip />
        <el-table-column
          label="转发时间"
          prop="createtime"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            {{ formatDate(row.createtime) }}
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="forwardHistoryVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getProblemLedgerList,
    testtaskProfindDel,
    exportProblemLedger,
    sendreform,
    toIssueLedger,
    getProblemTransferList,
  } from '@/api/internal/tack'
  import { candidates, submitByYmWork } from '@/api/setting/system'
  import { xiafaListNew } from '@/oapi/audit/preparation'
  import filterTable from '@/components/filterTable.vue'
  import { formatDay } from '@/utils/index'
  import issueView from '@/views/internal/internalTest/components/issueView.vue'
  import danxuanPerson from '@/components/danxuanPerson.vue'
  import store from '@/store'
  import { baseURL } from '@/config'
  const token = store.getters['user/token']
  import { testtaskProfindSave } from '@/api/internal/tack'
  import { customUpload } from '@/utils/Uploader'
  import { handleDown } from '@/utils/fileHandler'
  export default {
    name: 'wttz',
    components: { filterTable, issueView, danxuanPerson },
    data() {
      return {
        baseApi: baseURL,
        // api: '/nkhg/nbkz/pjbg/addupload',
        headers: { token: token },
        fileList: [],
        api: 'https://www.wenxin.example.com/api/file/file/upload',
        lodeapi: 'https://www.wenxin.example.com/api/file/file/download',
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          testYear: '',
          defectlevel: '',
          problemtype: '',
          defectlevel: '',
          pageNumber: 1,
          pageSize: 20,
        },
        filedAll: [
          { name: '问题概述' },
          { name: '缺陷具体描述' },
          { name: '问题类别' },
          { name: '缺陷等级' },
          { name: '定性依据' },
          { name: '主责部门' },
          { name: '反馈意见' },
          { name: '整改计划' },
          { name: '预计完成时间' },
          { name: '整改落实人' },
          { name: '转发人员' },
          { name: '内控评价年度' },
          { name: '审批状态' },
        ], //所有表格项
        filedNow: [],
        tableKey: 'internal-internalTest-pbfind-list',
        row: {}, // 当前行信息
        multipleSelection: [],
        // 转发相关数据
        forwardHistoryVisible: false,
        forwardHistoryLoading: false,
        forwardHistoryList: [],
        currentForwardRow: null, // 当前要转发的行数据
      }
    },
    created() {
      this.fetchData()
      this.initTable() //初始化表格
    },
    methods: {
      // 多选处理
      handleSelectionChange(selection) {
        console.log('handleSelectionChange', selection)
        this.multipleSelection = selection
      },
      // 处理下拉菜单命令
      handleCommand(command) {
        const { action, row } = command
        switch (action) {
          case 'maintenance':
            this.onMaintenance(row)
            break
          case 'forward':
            this.onForward(row)
            break
          case 'forwardHistory':
            this.onForwardHistory(row)
            break
        }
      },
      // 转发功能
      onForward(row) {
        this.currentForwardRow = row
        this.$refs.personDialog.showEdit()
      },
      // 人员选择完成回调
      async handlePersonSelected(selectedPersons) {
        console.log(1231231231321)
        if (!selectedPersons || selectedPersons.length === 0) {
          this.$message.warning('请选择转发人员')
          return
        }
        console.log(
          '🚀 ~ handlePersonSelected ~ selectedPersons:',
          selectedPersons
        )

        const person = selectedPersons[0] // 取第一个选中的人员
        const staffid = person.staffid
        const ids = this.currentForwardRow.findid

        try {
          // 调用转发接口
          const { code, msg } = await toIssueLedger({
            ids: ids,
            staffId: staffid,
          })

          if (code === 200 || code === 1) {
            this.$baseMessage('转发成功', 'success')

            // 调用消息下发
            const xiafaData = {
              formId: this.currentForwardRow.findid,
              distributionTitle: `${this.currentForwardRow.oneprocess}`,
              reciver: staffid,
              isread: 0,
              moduleType: 'nkhg',
            }

            const arr = [xiafaData]

            xiafaListNew({
              tableId: '53',
              jsondistribution: JSON.stringify(arr),
            }).then((response) => {
              if (response.msg === '成功') {
                this.$baseMessage('下发通知成功', 'success')
                this.fetchData()
              }
            })
          } else {
            this.$baseMessage(msg || '转发失败', 'error')
          }
        } catch (error) {
          console.error('转发失败:', error)
          this.$baseMessage('转发失败', 'error')
        }
      },
      // 转发记录功能
      async onForwardHistory(row) {
        this.forwardHistoryVisible = true
        this.forwardHistoryLoading = true

        try {
          const { data, code } = await getProblemTransferList({
            id: row.findid,
          })

          if (code == 1) {
            this.forwardHistoryList = data.data
          } else {
            this.forwardHistoryList = []
          }
        } catch (error) {
          console.error('获取转发记录失败:', error)
          this.forwardHistoryList = []
        } finally {
          this.forwardHistoryLoading = false
        }
      },
      onMaintenance(row) {
        this.$refs.issue.show(row, 'maintenance')
      },
      resetSearch() {
        this.queryForm = {
          defectlevel: '',
          problemtype: '',
          defectlevel: '',
          testYear: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      // async handleSuccess(res, b, c, row) {
      //   if (res && res.code === 200) {
      //     const fileId = res.data.attid
      //     console.log('stringId', String(fileId))
      //     const data = await testtaskProfindSave({ ...row, attids: fileId })
      //     this.$message.success('上传成功！')
      //     console.log('uploadRes', uploadRes)
      //     // if (uploadRes && uploadRes.code === 200) {
      //     //   this.$message.success('上传成功！')
      //     // } else {
      //     //   this.$message.error('上传失败！')
      //     // }
      //   } else {
      //     this.$message.error('上传失败！')
      //   }
      // },
      handleDeatil(row) {
        this.$refs.issue.show(row, 'detail', this.testtaskid)
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
      formatDate(date) {
        // 获取单元格数据
        return formatDay(date)
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
          data: {
            pageBean: { records, total },
          },
        } = await getProblemLedgerList({
          ...this.queryForm,
          testYear: this.queryForm.testYear
            ? new Date(this.queryForm.testYear).getFullYear()
            : '',
        })
        this.list = records
        this.total = total
        this.listLoading = false
      },
      handleApproval(row) {
        //提交审批
        this.$baseConfirm('你确定要提交审批当前项吗', null, async () => {
          const { data, code, msg } = await candidates({
            tableId: 47,
            fromId: row.findid,
          })

          if (data && data.candidateType) {
            const res = await submitByYmWork({
              tableId: 47,
              fromId: row.findid,
              candidateType: data.candidateType,
            })

            if (res.code === 1) {
              this.$message.success(res.msg)
              this.queryData()
            }
          }
        })
      },
      handleAdd() {
        this.$refs.issue.show({}, 'add')
      },
      handleEdit(row) {
        this.$refs.issue.show(row, 'edit')
      },
      handleIssueDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, code } = await testtaskProfindDel({ findid: row.findid })
          if (code == 200) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            this.queryData()
          } else {
            this.$baseMessage(msg, 'error', 'vab-hey-message-error')
          }
        })
      },
      async handleExport() {
        if (!this.multipleSelection || !this.multipleSelection.length) {
          return this.$message.error('请选择导出数据！')
        }
        let ids = this.multipleSelection.map((x) => x.findid)
        ids = ids.join(',')
        const data = await exportProblemLedger({ ids })
        let filename = '问题台账.xlsx'
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
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
      async goChange(e) {
        console.log(e)
        let res = await sendreform({ findid: e.findid })
        if (res.code === 200) {
          this.$message.success(res.data)
          this.queryData()
        }
      },
      customUploadWrapper(options) {
        if (
          !this.baseApi ||
          !this.api ||
          !this.headers ||
          !window.key ||
          !window.iv
        ) {
          return
        }

        // 确保 fileList 是一个数组
        const fileList = Array.isArray(options.file)
          ? options.file
          : [options.file]

        // 获取 el-upload 的 data 参数
        // const formData = {
        //   formlevel: this.getFormLevel,
        // }

        // 调用自定义上传函数
        new Promise((resolve, reject) => {
          customUpload({
            baseApi: this.baseApi,
            api: this.api,
            key: window.key,
            iv: window.iv,
            headers: this.headers,
            fileList: fileList, // 使用 fileList 而不是 file
            // formData: formData, // 传递额外的表单数据
            onProgress: this.handleProgress,
            onSuccess: (response) => {
              this.handleSuccess(response)
              resolve(response) // 成功时调用 resolve
            },
            onError: (error) => {
              // this.handleError(error)
              reject(error) // 失败时调用 reject
            },
          })
        })
      },
      async handleSuccess(file) {
        if (file.code == 200) {
          // this.fileList = [...this.fileList, ...file.data]
          // this.tableData = [...this.tableData, ...file.data]
          const fileId = file.data.map((item) => item.attid)
          for (let i = 0; i < fileId.length; i++) {
            testtaskProfindSave({
              ...this.row,
              attids: fileId[i],
            })
          }
          //     console.log('stringId', String(fileId))
          // const data = await testtaskProfindSave({
          //   ...this.row,
          //   attids: fileId.join(','),
          // })
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handleBeforeUpload(file, fileList) {
        const isLt2M = file.size / 1024 / 1024 < 100 // 检查文件大小是否小于100MB
        if (!isLt2M) {
          this.$message.error('文件大小不能超过 100MB!')
          return false // 返回false停止上传
        }

        // 如果文件大小合适，则调用自定义上传逻辑
        this.customUploadWrapper({ file })
        return false // 停止默认上传行为
      },
      saveInfo(row) {
        this.row = row
      },
    },
  }
</script>
<style scoped lang="scss">
  .system-log-container {
    background: #f6f8f9 !important;
    padding: 0 !important;
  }
  .pagination {
    margin-bottom: 20px !important;
  }

  .secondCard {
    margin-top: -5px !important;
  }
</style>
