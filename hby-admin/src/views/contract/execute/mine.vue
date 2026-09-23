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
                v-model="queryForm.contractno"
                clearable
                placeholder="合同编号"
                v-if="item.name === '合同编号'"
              />
              <el-input
                v-model="queryForm.contracttype"
                clearable
                placeholder="合同类型"
                v-if="item.name === '合同类型'"
              />
              <el-select
                v-model="queryForm.dctype"
                placeholder="收付款方向"
                v-if="item.name === '收付款方向'"
              >
                <el-option label="收款" value="收款" />
                <el-option label="付款" value="付款" />
              </el-select>
              <el-input
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />
              <el-switch
                v-if="judgeJob && item.name === '本部门合同'"
                v-model="queryForm.isDept"
                active-text="本部门合同"
              ></el-switch>
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="合同编号" prop="contractno" />
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          >
            <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.contractname }}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column
            align="center"
            label="合同类型"
            prop="contracttype"
            v-if="item.name === '合同类型'"
          />
          <el-table-column
            align="center"
            label="收付款方向"
            prop="dctype"
            v-if="item.name === '收付款方向'"
          />
          <el-table-column
            align="center"
            label="合同金额(元)"
            prop="contractmoney"
            v-if="item.name === '合同金额(元)'"
          />
          <el-table-column
            align="center"
            label="合同状态"
            prop="contractstatus"
            v-if="item.name === '合同状态'"
          >
            <template #default="{ row }">
              {{ mapContractStatus(row) }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleExcute(row)">执行</el-button> -->
            <!-- <el-button type="text" @click="handleCompare(row)">比对</el-button> -->
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <!-- <el-dropdown-item
                  @click.native="checkUpload(row)"
                  :disabled="row.contractstatus != 6"
                >
                  <file-upload
                    v-model="formData.url"
                    accept=".pdf,.doc,.docx "
                    api="/contract/contract/importFile"
                    :data="{ contractId: row.contractid }"
                    :disabled="disableUpload(row)"
                    :show-file-list="false"
                    @success="handleSuccess"
                  >
                    文件上传
                  </file-upload>
                </el-dropdown-item>
                <el-dropdown-item
                  @click.native="handleDeleteAtt(row)"
                  :disabled="row.contractstatus != 6"
                >
                  附件删除
                </el-dropdown-item> -->

                <!-- <el-dropdown-item
                  :disabled="row.contractstatus !== 9"
                  @click.native="handleExcute(row)"
                >
                  继续执行
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="row.contractstatus != 7"
                  @click.native="handleStatus(row, 9, '暂停')"
                >
                  合同暂停
                </el-dropdown-item> -->
                <el-dropdown-item
                  :disabled="
                    row.contractstatus == 8 ||
                    row.contractstatus == 10 ||
                    row.contractstatus == 11 ||
                    row.contractstatus == 12
                  "
                  @click.native="handleStatus(row, 10, '变更')"
                >
                  合同变更
                </el-dropdown-item>
                <el-dropdown-item
                  :disabled="
                    row.contractstatus == 8 ||
                    row.contractstatus == 10 ||
                    row.contractstatus == 11 ||
                    row.contractstatus == 12
                  "
                  @click.native="handleStatus(row, 11, '终止')"
                >
                  合同终止
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="judgeJob"
                  @click.native="handleUpdatePerson(row)"
                >
                  变更落实人
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-pagination
      background
      class="pagination"
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <FileList ref="files" @fetch-data="fetchData" />
    <CreateDetail ref="common" />
    <CompareEdit ref="compare" />
    <ChangeBGModal ref="changeEdit" @fetch-data="fetchData" />
    <MineUpdatePerson
      ref="updatePerson"
      @fetch-data="fetchData"
      @selected="handleMineSelected"
    />
  </div>
</template>

<script>
import {
  changeContractStatus,
  excuteContract,
  getContractList,
  changeContractStaff,
} from '@/api/contract/fulfil'
import { saveContract, getContractItem } from '@/api/contract/manage'
import { excuteStatusOptions } from '@/views/contract/consts'
import CreateDetail from '@/views/contract/contractManage/components/contractsEdit/CreateDetail.vue'
import FileUpload from '@/views/contract/contractManage/components/FileUpload.vue'
import CompareEdit from './components/CompareEdit.vue'
import FileList from './components/FileList.vue'
import ChangeBGModal from '@/views/contract/contractManage/components/contractsEdit/ChangeEdit.vue'
import MineUpdatePerson from './components/MineUpdatePerson.vue'
import filterSearch from '@/components/filterSearch.vue'
import filterTable from '@/components/filterTable.vue'

export default {
  name: 'Mine',
  components: {
    FileList,
    CreateDetail,
    FileUpload,
    CompareEdit,
    ChangeBGModal,
    MineUpdatePerson,
    filterSearch,
    filterTable,
  },
  data() {
    return {
      list: [],
      listLoading: true,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        isDept: undefined,
      },
      current: {},
      currentEdit: 'moren',
      formData: {
        url: undefined,
      },
      judgeJob: 0,

      localKey: 'contract-execute-mine-search',
      tableKey: 'contract-execute-mine-list',
      searchNow: [],
      searchMore: true,
      searchItem: [],
      searchAll: this.getFiled(), //所有搜索项
      filedNow: [],
      filedAll: [
        { name: '合同名称' },
        { name: '合同类型' },
        { name: '收付款方向' },
        { name: '合同金额(元)' },
        { name: '合同状态' },
      ],
    }
  },
  created() {
    this.resetQueryForm()
    this.fetchData()
    //初始化表格&筛选
    this.searchNow = this.getFiled()
    this.searchItem = this.searchNow.slice(0, 4)
    this.initTable()
    this.initSearch()
  },
  methods: {
    // 动态筛选 动态表格 初始化数据&相关方法
    showMore() {
      this.searchMore = !this.searchMore

      if (this.searchMore) {
        this.searchItem = this.searchNow
      } else {
        this.searchItem = this.searchNow.slice(0, 4)
      }
    },
    getFiled() {
      let fields = [
        { name: '合同编号', key: 'contractno' },
        { name: '合同类型', key: 'contracttype' },
        { name: '收付款方向', key: 'dctype' },
        { name: '合同名称', key: 'contractname' },
        { name: '本部门合同', key: 'isDept' },
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
    //回调
    handleMineSelected(val) {
      let obj = {
        contractId: this.current.contractid,
        staffId: val.staffid,
      }
      this.changePerson(obj)
    },
    //人员变更
    async changePerson(obj) {
      const res = await changeContractStaff(obj)
      if (res.code == 1) {
        this.$baseMessage('变更成功', 'success', 'vab-hey-message-success')
        this.fetchData()
      }
    },
    resetQueryForm() {
      this.queryForm = {
        contractno: undefined,
        contractname: undefined,
        contracttype: undefined,
        dctype: undefined,
        flowId: 765521,
        pageNumber: 1,
        pageSize: 20,
      }
    },
    handleSuccess(val) {
      this.$baseMessage(val.msg, 'success', 'vab-hey-message-success')
    },
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
    },
    //过滤
    mapContractStatus(row) {
      const res = excuteStatusOptions.filter((item) => {
        return item.value === row.contractstatus
      })
      return res[0].label
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
      const { isDept, ...other } = this.queryForm
      const {
        data: {
          pageInfo: { tlist, totalRecord },
          judgeJob,
        },
      } = await getContractList({ isDept: isDept ? 1 : 0, ...other })
      this.judgeJob = judgeJob
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
    /**
     * @description: 打开详情表单弹框
     * @param {*} row 选择的行数据
     * @return {*}
     */
    handleDetail(row) {
      this.$refs['common'].showDetail(row, row.contracttype)
    },
    handleExcute(row) {
      const enable = this.checkExcute(row)
      if (!enable) return
      this.$baseConfirm('你确定要执行吗', null, async () => {
        const { msg } = await excuteContract({ contractId: row.contractid })
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        await this.fetchData()
      })
    },
    handleCompare(row) {
      this.$refs['compare'].showEdit(row)
    },
    handleDeleteAtt(row) {
      const disable = this.checkDeleteAttch(row)
      if (!disable) {
        this.$refs.files.show(row)
      }
    },
    //过滤状态
    async handleStatus(row, goalStatus, action) {
      const disable = this.checkStatus(row)
      if (disable) return

      if (action === '变更') {
        const res = await getContractItem({
          contractId: row.contractid,
        })
        const { contractid, budgetList, ...other } = res.data.tcu
        const info = (budgetList || []).map((res) => {
            return {
              budgetname: res.budgetname,
              bugetId: res.budgetid,
              bugetType: res.budgettype,
            }
          })

        const resw = await saveContract({
          ...other,
          contractxdf: JSON.stringify(info),
          preContractId: contractid,
          goalStatus: 10,
        })
        this.$refs['changeEdit'].showEdit(
          { ...resw.data, preContractId: contractid },
          resw.data.contracttype,
          true,
          10
        )
        return
      }
      this.$baseConfirm(`你确定要${action}合同吗`, null, async () => {
        const { msg, code } = await changeContractStatus({
          contractId: row.contractid,
          goalStatus: goalStatus,
        })

        if (code == 1) {
          this.$baseMessage(
            `${action}成功！`,
            'success',
            'vab-hey-message-success'
          )
        }

        await this.fetchData()
      })
    },
    handleCommand(command) {},
    disableUpload(row) {
      const contractStatus = row.contractstatus
      const intercept = [7, 10, 9, 8, 12, 13, 14, 15]
      const filteredOpt = excuteStatusOptions.filter((item) => {
        const value = item.value
        return intercept.indexOf(value) > -1
      })
      return filteredOpt.some((item) => {
        return item.value == contractStatus
      })
    },
    //上传校验
    checkUpload(row) {
      const contractStatus = row.contractstatus
      const intercept = [7, 10, 9, 8, 12, 13, 14, 15]
      const filteredOpt = excuteStatusOptions.filter((item) => {
        const value = item.value
        return intercept.indexOf(value) > -1
      })
      return filteredOpt.some((item) => {
        if (item.value == contractStatus) {
          const msg = `当前${item.label}，无法上传盖章文件！`
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        return item.value == contractStatus
      })
    },
    checkDeleteAttch(row) {
      const contractStatus = row.contractstatus
      const intercept = [7, 11, 10, 9, 8, 12, 13, 14, 15]
      const filteredOpt = excuteStatusOptions.filter((item) => {
        const value = item.value
        return intercept.indexOf(value) > -1
      })

      return filteredOpt.some((item) => {
        if (item.value == contractStatus) {
          const msg = `合同正在${item.label}，无法删除盖章文件！`
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        return item.value == contractStatus
      })
    },
    //校验
    checkExcute(row) {
      const statusStr = excuteStatusOptions.filter((item) => {
        return item.value == row.contractstatus
      })[0].label
      const intercept = [
        '未审批',
        '审批中',
        '需调整',
        '已通过',
        '已跟踪',
        '已终止',
      ]
      const isHit = intercept.some((item) => item == statusStr)
      if (isHit) {
        this.$baseMessage(
          '审批未完成，无法执行!',
          'error',
          'vab-hey-message-error'
        )
        return false
      }
      if (statusStr == '执行中') {
        this.$baseMessage('正在执行!', 'error', 'vab-hey-message-error')
        return false
      }
      if (statusStr == '已完成') {
        this.$baseMessage(
          '执行完成，等待归档!',
          'error',
          'vab-hey-message-error'
        )
        return false
      }
      if (statusStr == '已归档') {
        this.$baseMessage(
          '已归档无法再次执行!',
          'error',
          'vab-hey-message-error'
        )
        return false
      }
      const intercept2 = [
        '已变更',
        '已终止',
        '纠纷中',
        '协商中',
        '诉讼中',
        '仲裁中',
      ]
      const isHit2 = intercept2.some((item) => item == statusStr)
      if (isHit2) {
        this.$baseMessage(
          `${statusStr}无法执行!`,
          'error',
          'vab-hey-message-error'
        )
        return false
      }
      return true
    },
    //过滤状态
    checkStatus(row) {
      const contractStatus = row.contractstatus
      const intercept = [11, 10, 8, 12, 13, 14, 15]
      const filteredOpt = excuteStatusOptions.filter((item) => {
        const value = item.value
        return intercept.indexOf(value) > -1
      })

      return filteredOpt.some((item) => {
        if (item.value == contractStatus) {
          const msg = `合同正在${item.label}，无法变更合同状态！`
          this.$baseMessage(msg, 'error', 'vab-hey-message-error')
        }
        return item.value == contractStatus
      })
    },
    handleUpdatePerson(row) {
      this.current = row
      this.$refs['updatePerson'].show(row)
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
