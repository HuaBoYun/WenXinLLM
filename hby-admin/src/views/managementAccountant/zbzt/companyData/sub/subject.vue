<template>
  <div class="system-log-container">
    <el-page-header content="科目表" @back="goBack" />
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
            <div style="display: flex; align-items: center">
              <el-input
                v-model="queryForm.pkName"
                placeholder="组织"
                disabled
                style="width: 200px; margin-right: 10px"
              />
              <el-button type="primary" @click="selectSubject">选择</el-button>
            </div>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.name"
              placeholder="科目名称"
              class="w200"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.code"
              placeholder="科目编码"
              class="w200"
              clearable
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="queryForm.balanorient"
              placeholder="科目方向"
              class="w200"
              clearable
            >
              <el-option label="借方" :value="0"></el-option>
              <el-option label="贷方" :value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.pid"
              placeholder="上级科目编码"
              class="w200"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.minaccLev"
              placeholder="最小科目级次"
              class="w200"
              clearable
              @input="
                (val) => (queryForm.minaccLev = val.replace(/[^0-9]/g, ''))
              "
            />
            -
            <el-input
              v-model="queryForm.maxaccLev"
              placeholder="最大科目级次"
              class="w200"
              clearable
              @input="
                (val) => (queryForm.maxaccLev = val.replace(/[^0-9]/g, ''))
              "
            />
          </el-form-item>
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="queryData"
            clearable
          >
            查询
          </el-button>
          <el-button native-type="submit" type="default" @click="reset">
            重置
          </el-button>
        </el-form>
        <!-- <el-button
          type="primary"
          @click="showChangeAccountModal"
          style="margin: 0 0 19px auto !important"
        >
          切换账套
        </el-button> -->
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <div style="display: flex; align-items: center">
          <el-button type="primary" @click="handleExport" :loading="loading">
            导出
          </el-button>
          <el-dropdown style="margin-left: 10px" v-loading="loading">
            <el-button type="primary">操作</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="toType('toDiGao')">
                发送至底稿
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toDiGaoFile')">
                发送至底稿附件
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toYiDian')">
                发送至疑点
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toQueXian')">
                发送至缺陷
              </el-dropdown-item>
              <el-dropdown-item @click.native="toType('toFengXian')">
                发送至风险
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
      v-loading="loading"
      row-key="pkAccount"
      :tree-props="{ children: 'childrenList' }"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目编码" prop="code" />
      <el-table-column align="center" label="科目名称" prop="name" />
      <el-table-column align="center" label="方向" prop="balanorient">
        <template #default="{ row }">
          {{ row.balanorient == 0 ? '借方' : '贷方' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="内部码" prop="innercode" />
      <!-- <el-table-column align="center" label="上级科目" prop="pid" /> -->
      <el-table-column
        align="center"
        label="科目级次"
        prop="acclev"
      ></el-table-column>
    </el-table>
    <LcdyEdit ref="edit" @fetch-data="fetchData" />

    <FlawInfo ref="flaw" @fetch-data="fetchData(true)" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData(true)" />
    <RiskInfo ref="risk" @fetch-data="fetchData(true)" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData(true)" />

    <DiGaoFile ref="digaoFile" @fetch-data="fetchData(true)" />
    <TreeModal ref="selectSubject" @select="handleSelectSubject" />
    <ChangeAccountModal
      ref="changeAccountModal"
      @fetch-data="fetchData"
    ></ChangeAccountModal>
  </div>
</template>

<script>
  import {
    getSubjectList,
    getFinancialFileList,
    exportFinancialFile,
  } from '@/api/workbench/accountData/accountData'
  import { getKmList } from '@/api/cwsc'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import DiGaoFile from './fly/toDiGaoFile.vue'
  import ChangeAccountModal from './components/changeAccountModal.vue'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import TreeModal from '../treeModal.vue'

  export default {
    name: 'Consult',
    components: {
      LcdyEdit,
      DiGaoFile,
      ChangeAccountModal,
      FlawInfo,
      DoubtfulInfo,
      RiskInfo,
      MyDraftInfo,
      TreeModal,
    },
    data() {
      return {
        loading: false,
        run: undefined,
        list: [],
        bookInfo: {},
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNo: 1,
          pageSize: 20,
          pkorg: '',
          pkName: '',
          name: '',
          code: '',
          balanorient: '',
          pid: '',
          minaccLev: undefined,
          maxaccLev: undefined,
        },
        selectList: [],
        select: [],
      }
    },
    created() {},
    async mounted() {
      // if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      if (this.$route.params.queryData) {
        const params = JSON.parse(JSON.stringify(this.queryForm))

        params.accId = this.$route.params.queryData.accid
        params.accAllName = this.$route.params.queryData.accname1
        params.wnss = this.$route.params.queryData.ayear
        params.status = '等于'
        this.run = params
      } else {
      }
      this.fetchData()
    },
    methods: {
      clearAueryForm() {
        this.queryForm.status = ''
        this.queryForm.keyword = ''
        this.$forceUpdate()
      },
      showChangeAccountModal() {
        this.$refs['changeAccountModal'].showEdit()
      },
      goBack() {
        this.$router.back(-1)
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
      queryData() {
        // 验证最大科目级次不能小于最小科目级次
        if (
          this.queryForm.minaccLev &&
          this.queryForm.maxaccLev &&
          Number(this.queryForm.maxaccLev) < Number(this.queryForm.minaccLev)
        ) {
          this.$message.error('最大科目级次不能小于最小科目级次')
          return
        }
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData(clearSelection) {
        // 发送操作完成后，清空勾选状态
        if (clearSelection) {
          this.select = []
          this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
        }
        let params = JSON.parse(JSON.stringify(this.queryForm))

        // params.bookYear = this.bookInfo.bookYear

        // params.wh1 = ''
        // const type = params.type
        // //
        // if (type) {
        //   switch (type) {
        //     case 'ACCID':
        //       params.accId = params.keyword
        //       break
        //     case 'ACCNAME':
        //       params.accName = params.keyword
        //       break
        //     case 'DC':
        //       params.dc = params.keyword
        //       break
        //     case 'HIGHACCID':
        //       params.highAccId = params.keyword
        //       break
        //     case 'ACCAllNAME':
        //       params.accAllName = params.keyword
        //       break
        //     case 'IGRADE':
        //       params.igrade = params.keyword
        //       break
        //     case 'DCACCSTATUS':
        //       params.dcAccStatus = params.keyword
        //       break
        //     case 'AMONTH':
        //       params.minMonth = params.accName[0].split('-')[1]
        //       params.maxMonth = params.accName[1].split('-')[1]
        //       delete params.status
        //       delete params.accName
        //       break
        //     case '':
        //       break
        //     default:
        //       break
        //   }
        // }
        // if (this.run) {
        //   params = this.run
        // }
        this.loading = true
        const { data } = await getKmList(params)
        this.list = data
        this.total = data.length

        this.loading = false

        // 仅在非清空模式下恢复勾选状态
        if (!clearSelection) {
          this.setCheckedRows()
        }
      },
      reset() {
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
          pkorg: '',
          pkName: '',
          name: '',
          code: '',
          balanorient: '',
          pid: '',
          minaccLev: undefined,
          maxaccLev: undefined,
        }
        this.select = []
        this.run = this.$options.data().run
        this.list = []
        this.fetchData()
      },
      handleAdd() {
        this.$refs['edit'].showEdit()
      },
      handleEdit(row) {
        this.$refs['edit'].showEdit(row)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          await this.fetchData()
        })
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.pkAccount === row.pkAccount)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll() {
        const allRows = this.getAllTreeRows(this.list)
        // 判断是否已经全选：检查所有行是否都在 select 中
        const isAllSelected = allRows.every((row) =>
          this.select.some((x) => x.pkAccount === row.pkAccount)
        )

        if (isAllSelected) {
          // 已全选，执行取消全选
          const allPkAccounts = allRows.map((r) => r.pkAccount)
          this.select = this.select.filter(
            (x) => !allPkAccounts.includes(x.pkAccount)
          )
          // 清除所有勾选状态
          this.$nextTick(() => {
            allRows.forEach((row) => {
              this.$refs.multipleTable.toggleRowSelection(row, false)
            })
          })
        } else {
          // 未全选，执行全选
          allRows.forEach((row) => {
            if (!this.select.some((x) => x.pkAccount === row.pkAccount)) {
              this.select.push(row)
            }
          })
          // 同步勾选状态到表格
          this.$nextTick(() => {
            allRows.forEach((row) => {
              this.$refs.multipleTable.toggleRowSelection(row, true)
            })
          })
        }
      },
      // 递归获取树形结构中的所有行（包括子节点）
      getAllTreeRows(list) {
        let result = []
        list.forEach((row) => {
          result.push(row)
          if (row.childrenList && row.childrenList.length > 0) {
            result = result.concat(this.getAllTreeRows(row.childrenList))
          }
        })
        return result
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          const allRows = this.getAllTreeRows(this.list)
          this.select.forEach((row) => {
            const found = allRows.find(
              (item) => row.pkAccount === item.pkAccount
            )
            if (found) {
              this.$refs.multipleTable.toggleRowSelection(found, true)
            }
          })
        })
      },
      //发送
      toType(type) {
        if (this.select.length === 0) {
          this.$message({
            message: '请选择待发送',
            type: 'error',
          })
          return
        }
        this.getFile(type)
      },
      // 导出
      async handleExport() {
        if (this.select.length === 0) {
          this.$message({
            message: '请选择待导出数据',
            type: 'error',
          })
          return
        }
        // pkAccounts: 科目表操作 科目主键数组，需要encode转译
        const pkAccounts = this.select.map((res) =>
          encodeURIComponent(res.pkAccount)
        )
        this.loading = true
        const params = {
          exprotType: 1, // 1-导出科目表
          pkAccounts: pkAccounts,
          isExport: '1', // 开启导出下载功能
        }
        // 如果选了组织，传pkOrg
        if (this.queryForm.pkorg) {
          params.pkOrg = this.queryForm.pkorg
        }
        try {
          const res = await exportFinancialFile(params)
          // 处理blob下载
          const blob = new Blob([res], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `科目表_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败')
        } finally {
          this.loading = false
        }
      },

      async getFile(type) {
        // pkAccounts: 科目表操作 科目主键数组，需要encode转译
        const pkAccounts = this.select.map((res) =>
          encodeURIComponent(res.pkAccount)
        )
        this.loading = true
        const params = {
          exprotType: 1, // 1-导出科目表
          pkAccounts: pkAccounts,
        }
        // 如果选了组织，传pkOrg
        if (this.queryForm.pkorg) {
          params.pkOrg = this.queryForm.pkorg
        }
        const info = await getFinancialFileList(params)
        this.loading = false
        if (info.data) {
          if (type == 'toQueXian') {
            this.$refs['flaw'].showEdit(
              'add',
              {},
              {},
              { attachment: info.data }
            )
          }
          if (type == 'toFengXian') {
            this.$refs['risk'].showEdit('add', null, { attachment: info.data })
          }
          if (type == 'toYiDian') {
            this.$refs['doubtful'].showEdit('add', null, {
              attachment: info.data,
            })
          }
          if (type == 'toDiGao') {
            this.$refs['manuscript'].showEdit(
              'add',
              {},
              { attachment: info.data }
            )
          }
          if (type == 'toDiGaoFile') {
            this.$refs['digaoFile'].showEdit(info.data)
          }
        }
      },
      selectSubject() {
        this.$refs['selectSubject'].showEdit()
      },
      handleSelectSubject(data) {
        this.queryForm.pkorg = data.pkOrg
        this.queryForm.pkName = data.name
        // this.queryForm.num = data.code
        this.fetchData()
      },
    },
  }
</script>
<style lang="scss" scoped>
  .w200 {
    width: 200px;
  }
</style>
