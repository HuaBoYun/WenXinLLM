<!--
 * @Date: 2022-04-16 22:35:32
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-11 10:29:10
 * @FilePath: /hb-admin/src/views/contract/contractManage/seal.vue
-->
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
                v-model="queryForm.contractname"
                clearable
                placeholder="合同名称"
                v-if="item.name === '合同名称'"
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
      </vab-query-form-right-panel>
      <el-table v-loading="listLoading" :data="list">
        <el-table-column align="center" label="合同编号" prop="contractno">
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.contractno }}
            </el-button>
          </template>
        </el-table-column>
        <div v-for="(item, index) in filedNow" :key="index">
          <el-table-column
            align="center"
            label="合同名称"
            prop="contractname"
            v-if="item.name === '合同名称'"
          />
          <!-- <el-table-column
            align="center"
            label="项目名称"
            prop="projectname"
            v-if="item.name === '项目名称'"
          /> -->
          <el-table-column
            align="center"
            label="印章名称"
            prop="counterpartcode"
            v-if="item.name === '印章名称'"
          />
          <el-table-column
            align="center"
            label="印章所属主体"
            prop="counterparthank"
            v-if="item.name === '印章所属主体'"
          />
          <el-table-column
            align="center"
            label="审批状态"
            prop="contractstatus"
            v-if="item.name === '审批状态'"
          >
            <template #default="{ row }">
              {{ mapSealStatus(row) }}
            </template>
          </el-table-column>
        </div>
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="150"
        >
          <template #default="{ row }">
            <!-- <el-button type="text" @click="handleDeal(row)">办理</el-button> -->
            <div style="display: flex">
              <el-button
                type="text"
                :disabled="
                  row.inspectionstatus != 0 && row.inspectionstatus != -1
                "
                @click="handleEdit(row)"
              >
                用印
              </el-button>
              <!-- <el-button type="text" @click="handleEdit(row)">用印</el-button> -->

              <!-- <el-upload
                style="margin-left: 10px"
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="tableData"
                accept=".pdf"
                :data="{ contractId: row.contractid, budgetid: row.budgetid }"
                :disabled="
                  row.inspectionstatus < 3 || row.hasOwnProperty('singingid')
                "
              >
                <el-button
                  type="text"
                  :disabled="
                    row.inspectionstatus < 3 || row.hasOwnProperty('singingid')
                  "
                >
                  上传签署文件
                </el-button>
              </el-upload> -->
              <el-button
                type="text"
                style="margin-left: 10px"
                @click="handleOpenModal(row)"
                :disabled="row.inspectionstatus != 6"
              >
                上传签署文件
              </el-button>
              <!-- <el-button
                type="text"
                style="margin-left: 10px"
                @click="handleDelete(row)"
                :disabled="!row.hasOwnProperty('singingid')"
              >
                删除附件
              </el-button> -->
            </div>
            <el-button
              type="text"
              :disabled="row.inspectionstatus < 1"
              @click="handleManage(row)"
            >
              办理
            </el-button>

            <el-button
              type="text"
              @click="handleApproval(row)"
              :disabled="row.inspectionstatus > 0"
            >
              提交审批
            </el-button>
            <el-button type="text" @click="handleDuibi(row)">比对</el-button>
            <!-- <el-button
                type="text"
                @click="handlePrint(row)"
                :disabled="row.inspectionstatus == -1"
              >
                打印审批单
              </el-button> -->
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
    <ContractSealEdit ref="edit" @fetch-data="fetchData" />
    <ContractSealDetail ref="detail" />
    <Deal ref="deal" />
    <PrintCom ref="PrintCom" />
    <ProcessList ref="process" @fetchData="fetchData" />
    <fileModal ref="fileModal" @fetchData="fetchData" />
    <WfqdDeal ref="wfqddeal" />

    <input v-show="false" type="file" ref="uploadBidui" id="fileUpload" />
  </div>
</template>

<script>
  import { getContractSealList } from '@/api/contract/manage'
  import { removeFileFromContract } from '@/api/contract/fulfil'
  import { sealStatusOptions } from '@/views/contract/consts'
  import ContractSealDetail from './components/ContractSealDetail.vue'
  import ContractSealEdit from './components/ContractSealEdit.vue'
  import Deal from './components/Deal.vue'
  import PrintCom from './components/print.vue'
  import ProcessList from './components/ProcessList.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import fileModal from './components/fileModal.vue'
  import filterSearch from '@/components/filterSearch.vue'
  import filterTable from '@/components/filterTable.vue'
  import WfqdDeal from '@/views/msg/components/options/WfqdDeal'
  import { getFlowPkInfo } from '@/api/contract/manage'
  import axios from 'axios'

  export default {
    name: 'Seal',
    components: {
      ProcessList,
      ContractSealEdit,
      ContractSealDetail,
      Deal,
      PrintCom,
      fileModal,
      filterSearch,
      filterTable,
      WfqdDeal,
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {},
        baseApi: baseURL,
        api: '/contract/contract/importFile',
        headers: {
          token: store.getters['user/token'],
        },

        localKey: 'contract-contractManage-seal-search',
        tableKey: 'contract-contractManage-seal-list',
        searchNow: [],
        searchMore: true,
        searchItem: [],
        searchAll: this.getFiled(), //所有搜索项
        filedNow: [],
        filedAll: [
          { name: '合同名称' },
          // { name: '项目名称' },
          { name: '印章名称' },
          { name: '印章所属主体' },
          { name: '审批状态' },
        ],
        curRow: {},
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
                      type: '2',
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
      async handleManage(row) {
        const res = await getFlowPkInfo({
          formId: row.budgetid,
          tableId: 6,
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
        let fields = [{ name: '合同名称', key: 'contractname' }]
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
      handlePrint(row) {
        this.$refs['PrintCom'].printClick(row)
      },
      //重置
      resetQueryForm() {
        this.queryForm = {
          contractname: undefined,
          flowId: 622324,
          pageNumber: 1,
          pageSize: 20,
        }
      },
      //重置
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      //处理状态
      mapSealStatus(row) {
        if (
          row.inspectionstatus == -1 ||
          !row.inspectionstatus ||
          row.inspectionstatus == 0
        ) {
          return '未用印'
        }
        const res = sealStatusOptions.filter((item) => {
          return item.value === row.inspectionstatus
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
        const {
          data: {
            pageInfo: { tlist, totalRecord },
          },
        } = await getContractSealList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDeal(row) {
        if (row.inspectionstatus == -1 || row.inspectionstatus == 0) {
          this.$baseMessage('未用印无法办理', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['deal'].show(row, 'seal')
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        if (!row.budgetid) {
          this.$baseMessage('未用印', 'error', 'vab-hey-message-error')
          return
        }
        this.$refs['edit'].showDetail(row)
      },
      handleEdit(row) {
        // -1的时候可以添加印章主体
        if (
          row.inspectionstatus == -1 ||
          row.inspectionstatus == 0 ||
          row.inspectionstatus == 2
        ) {
          this.$refs['edit'].showEdit(row)
        } else {
          const item = sealStatusOptions.filter((item) => {
            return item.value == row.inspectionstatus
          })
          const text = item[0].label
          this.$baseMessage(
            `${text}无法再次用印`,
            'error',
            'vab-hey-message-error'
          )
        }
      },
      handleApproval(row) {
        this.$refs['process'].save(6, row.budgetid)
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleCommand(command) {},
      //删除
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeFileFromContract({
            singingId: row.singingid,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      //成功函数
      handleSuccess(file) {
        if (file.code == '1') {
          this.$baseMessage(file.msg, 'success')
          this.fetchData()
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //打开弹窗
      handleOpenModal(row) {
        this.$refs['fileModal'].openModal(row)
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
