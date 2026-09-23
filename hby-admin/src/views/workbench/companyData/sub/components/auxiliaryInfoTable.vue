<template>
  <div>
    <!-- <el-page-header content="辅助信息表" /> -->
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
            <el-select
              v-model="queryForm.type"
              placeholder="类型"
              @change="handleTypeChange"
            >
              <el-option label="辅助类型" value="ASSTYPE" />
              <el-option label="辅助编码" value="ASSID" />
              <el-option label="辅助名称" value="ASSNAME" />
              <el-option label="辅助级别" value="ASSLEVEL" />
              <el-option label="上级编码" value="ASSSJBM" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="条件">
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.assName"
              clearable
              placeholder="关键字"
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
            <el-button native-type="submit" type="default" @click="reset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-dropdown style="margin-left: 10px" v-loading="listLoading">
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
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
      :data="list"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="辅助类型" prop="assType" />
      <el-table-column align="center" label="辅助编码" prop="assId" />
      <el-table-column align="center" label="辅助名称" prop="assName" />
      <el-table-column align="center" label="辅助级别" prop="assLevel" />
      <el-table-column align="center" label="上级编码" prop="assSjbm" />
      <!-- <el-table-column
        align="center"
        label="备注"
        prop="data"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="状态" prop="data" /> -->
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />
    <FlawInfo ref="flaw" @fetch-data="fetchData" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData" />
    <RiskInfo ref="risk" @fetch-data="fetchData" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />
    <DiGaoFile ref="digaoFile" @fetch-data="fetchData" />
  </div>
</template>

<script>
  import {
    getAccountAssistVoucher,
    getAccountFileList,
  } from '@/api/workbench/accountData/accountData'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'
  import DiGaoFile from '../fly/toDiGaoFile.vue'
  export default {
    name: 'User',
    props: ['leftItem'],
    components: { FlawInfo, DoubtfulInfo, RiskInfo, MyDraftInfo, DiGaoFile },
    data() {
      return {
        list: [],
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        bookInfo: null,
        queryForm: {
          leftMenu: '1',
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
        },
        select: [],
      }
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
      const bookInfo = localStorage.getItem('bookInfo')
      this.bookInfo = JSON.parse(bookInfo)
      this.fetchData()
    },
    watch: {
      leftItem: function () {
        this.fetchData() //监听props变化查询表
      },
    },
    methods: {
      goBack() {
        this.$router.back(-1)
      },
      handleTypeChange() {
        this.queryForm.status = undefined
        this.queryForm.accName = undefined
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
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      reset() {
        this.queryForm = {
          leftMenu: '1',
          type: undefined,
          status: undefined,
          accName: undefined,
          pageNumber: 1,
          pageSize: 20,
        }
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        //
        const params = JSON.parse(JSON.stringify(this.queryForm))
        params.bookYear = this.bookInfo.bookYear
        params.typeName = this.leftItem
        const type = params.type
        if (type) {
          switch (type) {
            case 'AMONTH':
              params.minMonth = params.accName[0].split('-')[1]
              params.maxMonth = params.accName[1].split('-')[1]
              delete params.status
              delete params.accName
              break
            case '':
              break
            default:
              break
          }
        }
        const {
          data: { list, total },
        } = await getAccountAssistVoucher(params)
        this.list = list
        this.total = total
        this.listLoading = false

        this.setCheckedRows()
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
      handleCommand(command) {
        switch (command) {
          case 'copy':
            this.$refs.copyToIndustry.showEdit()
            break
        }
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x == row.assId)
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
            if (row && !this.select.some((x) => x.assId == row.assId)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.assId == row.assId)
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
                return row.typeId == item.typeId
              }),
              true
            )
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

      async getFile(type) {
        const id = this.select.map((res) => res.assId)
        this.listLoading = true
        const info = await getAccountFileList({
          assIds: id.toString(),
          exprotType: 7,
        })
        this.listLoading = false
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
    },
  }
</script>
