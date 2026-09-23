<template>
  <div class="system-log-container" v-if="type == 1">
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
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
              />
              <el-input
                v-model="queryForm.contractbd"
                clearable
                placeholder="合同标的"
                v-if="item.name === '合同标的'"
              />
              <el-select
                v-model="queryForm.contracttype"
                filterable
                placeholder="合同类型"
                style="width: 100%"
                v-if="item.name === '合同类型'"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
              <el-input
                v-model="queryForm.orgmeno"
                clearable
                placeholder="请选择承办部门"
                style="width: 187px"
                v-if="item.name === '承办部门'"
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.department.show()"
                v-if="item.name === '承办部门'"
              >
                选择
              </el-button>
              <el-input
                v-model="queryForm.budgetname"
                clearable
                placeholder="请选择相对方"
                v-if="item.name === '相对方'"
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
        <el-button type="success" @click="handleEdit()">起草</el-button>
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
            label="合同金额(元)"
            prop="contractmoney"
            v-if="item.name === '合同金额(元)'"
          />
          <el-table-column
            align="center"
            label="开始日期"
            prop="startdate"
            v-if="item.name === '开始日期'"
          />
          <el-table-column
            align="center"
            label="结束日期"
            prop="enddate"
            v-if="item.name === '结束日期'"
          />
          <el-table-column
            align="center"
            label="合同状态"
            prop="contractstatus"
            v-if="item.name === '合同状态'"
          >
            <template #default="{ row }">{{ mapContractStatus(row) }}</template>
          </el-table-column>
        </div>
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
              :disabled="
                row.contractstatus != 0 &&
                row.contractstatus != 2 &&
                row.contractstatus != 3
              "
            >
              修改
            </el-button>
            <el-dropdown style="margin-left: 10px" @command="handleCommand">
              <el-button type="text">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <!-- <el-dropdown-item @click.native="handleDeal(row)">
                  查看流程图
                </el-dropdown-item> -->
                <el-dropdown-item @click.native="handleManage(row)">
                  <el-button type="text" :disabled="!row.contractstatus">
                    办理
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="Approval(row)">
                  <el-button type="text" :disabled="!!row.contractstatus">
                    提交审批
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleRemind(row)">
                  <el-button type="text">定时提醒</el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleOffice(row)">
                  <el-button type="text">合同正文预览</el-button>
                </el-dropdown-item>
                <!-- <el-dropdown-item
                  @click.native="handleStatus(row, 10, '变更')"
                  :disabled="row.contractstatus != 6"
                >
                  合同变更
                </el-dropdown-item> -->
                <el-dropdown-item @click.native="handleDelete(row)">
                  <el-button type="text" :disabled="!!row.contractstatus">
                    删除
                  </el-button>
                </el-dropdown-item>
                <el-dropdown-item @click.native="handleDuibi(row)">
                  <el-button type="text">比对</el-button>
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
    <!-- <department-options ref="department" @selected="handleDepartmentSelected" /> -->
    <remind-edit ref="remind" />
    <ChooseType
      v-if="chooseStatus"
      @close="closeChooseType"
      ref="type"
      @selected="handleTypeSelected"
    />
    <CreateEdit
      @close="closeChooseType"
      v-if="chooseStatus"
      ref="common"
      @fetch-data="fetchData"
    />
    <CreateDetail
      v-if="showDetail"
      @close="handleCreateDetailsClose"
      ref="detail"
    />
    <SealDepartment ref="department" @selected="handleDepartmentSelected" />
    <Deal ref="deal" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <ChangeEdit ref="changeEdit" @fetch-data="fetchData" />
    <WfqdDeal ref="wfqddeal" />

    <input v-show="false" type="file" ref="uploadBidui" id="fileUpload" />
  </div>
</template>

<script>
  import {
    checkStageInfo,
    checkStatus,
    deleteContract,
    getContractList,
    getContractTypes,
  } from '@/api/contract/manage'
  // import DepartmentOptions from './components/options/department.vue'
  import {
    contractStatusOptions,
    excuteStatusOptions,
  } from '@/views/contract/consts'
  import { debounce } from '@/utils'
  import ChooseType from './components/ChooseType.vue'
  import CreateDetail from './components/contractsEdit/CreateDetail.vue'
  import CreateEdit from './components/contractsEdit/CreateEdit.vue'
  import Deal from './components/Deal.vue'
  import SealDepartment from './components/options/sealDepartment.vue'
  import ProcessList from './components/ProcessList.vue'
  import RemindEdit from './components/RemindEdit.vue'
  import ChangeEdit from '@/views/contract/contractManage/components/contractsEdit/ChangeEdit.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import axios from 'axios'

  export default {
    name: 'CreateContract',
    components: {
      // DepartmentOptions,
      RemindEdit,
      ChooseType,
      CreateEdit,
      CreateDetail,
      Deal,
      SealDepartment,
      ProcessList,
      ChangeEdit,
      filterTable,
      filterSearch,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        typeOptions: [],
        currentEdit: 'moren',
        showDetail: false,
        chooseStatus: false,
        type: 1,

        localKey: 'contract-contractManage-create-search',
        tableKey: 'contract-contractManage-create-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '合同名称' },
          { name: '合同类型' },
          { name: '合同金额(元)' },
          { name: '开始日期' },
          { name: '结束日期' },
          { name: '合同状态' },
        ],
        curRow: {},
      }
    },
    async created() {
      this.resetQueryForm()
      this.fetchData()
      this.fetchTypes()
      //初始化表格&筛选
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
      this.initTable()
      this.initSearch()
    },
    mounted() {
      this.$bus.on('updateMsg', (value) => {
        if (value == 0) {
          this.fetchData()
        }
      })
    },
    methods: {
      onSelectFile(event) {
        // 获取选中的文件
        var file = event.target.files[0] // 获取第一个文件（如果有多个文件，可以通过索引访问）
        if (file) {
          this.onUploadFileSuccess(file)
        }
      },
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.contractid,
          tableId: 5,
        })

        this.$refs.wfqddeal.show(res.data, false)
      },
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
          { name: '合同名称', key: 'contractname' },
          { name: '合同标的', key: 'contractbd' },
          { name: '合同类型', key: 'contracttype' },
          { name: '承办部门', key: 'orgmeno' },
          { name: '相对方', key: 'budgetname' },
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
      //过滤状态
      mapContractStatus(row) {
        const res = contractStatusOptions.filter((item) => {
          return item.value === row.contractstatus
        })
        return res[0].label
      },
      //请求数据
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.reduce((prev, cur) => {
          const data = cur.childrenList.map((item) => {
            return {
              label: item.typename,
              value: item.typeid,
            }
          })
          return prev.concat(data)
        }, [])
      },
      resetQueryForm() {
        this.queryForm = {
          contractno: undefined,
          contractname: undefined,
          contracttype: undefined,
          contractbd: undefined,
          budgetname: undefined,
          contractdept: undefined,
          orgmeno: undefined,
          flowId: 622316,
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
        this.listLoading = true
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getContractList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      Approval: debounce(function (val) {
        //使用自定义实现的防抖功能
        //
        //
        this.handleApproval(val)
      }, 1000), //延迟执行时间可以视具体情况而定

      async handleApproval(row) {
        //
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const info = userInfo.staffid
        const roleNames = (userInfo.roleNames || '').split(',')
        const isSuperAdmin = roleNames.includes('合同管理员')
        if (row.createuser != null && row.createuser != info && !isSuperAdmin) {
          this.$baseMessage(
            '只能提交自己创建的合同',
            'error',
            'vab-hey-message-error'
          )
          return
        }
        if (row.contractstatus === 0 || row.contractstatus === '') {
          const { code, msg } = await checkStageInfo({
            contractId: row.contractid,
          })
          if (code == 1) {
            const tableId = 5
            const fromId = row.contractid
            this.$refs['process'].save(tableId, fromId, row.contracttype)
          }
        } else {
          this.$baseMessage('流程进行中', 'error', 'vab-hey-message-error')
        }
      },
      handleRemind(row) {
        this.$refs['remind'].showEdit(row)
      },
      //提交审批
      handleDeal(row) {
        if (row.contractstatus == 0) {
          this.$baseMessage('请先提交审批', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['deal'].show(row, 'create')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.showDetail = true
        let self = this
        this.$nextTick(function () {
          // self.$refs['detail'].showDetail(row, row.contracttype)
          this.$router.push({
            path: '/contractManage/createEdit',
            query: { row, disabled: true },
          })
        })
        // this.$store.commit('acl/contractidd', row.contractid)
      },
      handleCreateDetailsClose() {
        this.showDetail = false
      },
      //关闭选择合同类型
      closeChooseType() {
        this.chooseStatus = false
      },
      handleEdit(row) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const info = userInfo.staffid
        const roleNames = (userInfo.roleNames || '').split(',')
        const isSuperAdmin = roleNames.includes('合同管理员')
        if (row) {
          if (
            row.createuser != null &&
            row.createuser != info &&
            !isSuperAdmin
          ) {
            this.$baseMessage(
              '只能修改自己创建的合同',
              'error',
              'vab-hey-message-error'
            )
            return
          }
        }

        this.chooseStatus = true

        this.$nextTick(() => {
          if (row) {
            // this.$refs['common'].showEdit(row, row.contracttype)
            this.$router.push({
              path: '/contractManage/createEdit',
              query: { row },
            })
          } else {
            // this.$refs['type'].show()
            this.$router.push('/contractManage/chooseType')
          }
        })
      },
      onUploadFileSuccess(file2) {
        this.listLoading = true
        document
          .getElementById('fileUpload')
          .removeEventListener('change', this.onSelectFile)
        const contractUrl = `https://office.wenxin.example.com/api/office/getContractReview?contractId=${this.curRow.contractid}&device=mobile`
        axios({
          url: contractUrl,
          method: 'get',
          responseType: 'blob',
        })
          .then((k) => {
            console.log('kkk', k.data)
            if (k && k.data && k.data.type) {
              const fileData = k.data
              // 接下来将二进制数据转换为 File 对象
              const file1 = new File([fileData], 'filename.ext', {
                type: fileData.type,
              })

              const data = new FormData()
              data.append('ori_file1', file1)
              data.append('ori_file2', file2)
              axios({
                method: 'POST',
                url: 'https://office.wenxin.example.com/api/app/filescompare/',
                data,
              })
                .then((resp) => {
                  console.log('resp', resp)
                  this.$router.push({
                    path: '/contractManage/fileComparison',
                    query: {
                      id: resp.data.id,
                      ori_file1: contractUrl,
                      ori_file2: resp.data.ori_file2,
                      type: '1',
                      needTitle: '1',
                      needDiffList: '1',
                    },
                  })
                })
                .finally(() => {
                  console.log('清空文件')
                  document.getElementById('fileUpload').value = ''
                  this.listLoading = false
                })
            } else {
              this.listLoading = false
            }
          })
          .catch((err) => {
            console.log(err)
            this.listLoading = false
          })
      },
      handleDuibi(row) {
        this.$refs.uploadBidui.click()
        this.curRow = row
        document
          .getElementById('fileUpload')
          .addEventListener('change', this.onSelectFile)
      },
      //删除
      async handleDelete(row) {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const info = userInfo.staffid
        const roleNames = (userInfo.roleNames || '').split(',')
        const isSuperAdmin = roleNames.includes('合同管理员')

        if (row.createuser != null && row.createuser != info && !isSuperAdmin) {
          this.$baseMessage(
            '只能删除自己创建的合同',
            'error',
            'vab-hey-message-error'
          )
          return
        }

        if (!row.contractid) return
        const { code } = await checkStatus({ contractId: row.contractid })
        if (code != 1) {
          return
        }
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          await deleteContract({
            contractId: row.contractid,
          })
          await this.fetchData()
        })
      },
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handleDepartmentSelected(node) {
        this.queryForm.contractdept = node.id
        this.queryForm.orgmeno = node.name
      },
      //类型选择
      handleTypeSelected(item, typename) {
        this.chooseStatus = true
        this.$nextTick(() => {
          this.$refs['common'].showEdit(undefined, item.typename, typename)
        })
      },
      //处理状态
      handleStatus(row, goalStatus, action) {
        const disable = this.checkStatus(row)
        if (disable) return

        if (action === '变更') {
          this.$refs['changeEdit'].showEdit(
            row,
            row.contracttype,
            true,
            goalStatus
          )
          return
        }
        // this.$baseConfirm(`你确定要${action}合同吗`, null, async () => {
        //   const { msg, code } = await changeContractStatus({
        //     contractId: row.contractid,
        //     goalStatus: goalStatus,
        //   })

        //   if (code == 1) {
        //     this.$baseMessage(
        //       `${action}成功！`,
        //       'success',
        //       'vab-hey-message-success'
        //     )
        //   }

        //   await this.fetchData()
        // })
      },
      //检验
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
      handleOffice(val) {
        const info = JSON.parse(localStorage.getItem('userInfo'))
        window.open(
          `https://office.wenxin.example.com/api/office/getContractReview?fileType=word&contractId=${val.contractid}&uid=${info.staffid}&name=${info.username}`
        )
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
