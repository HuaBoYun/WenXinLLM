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
            <el-select
              v-model="queryForm.type"
              placeholder="类型"
              @change="clearAueryForm()"
            >
              <el-option label="科目编码" value="ACCID" />
              <el-option label="科目名称" value="ACCNAME" />
              <el-option label="方向" value="DC" />
              <el-option label="上级科目编号" value="HIGHACCID" />
              <el-option label="全名" value="ACCAllNAME" />
              <el-option label="级次" value="IGRADE" />
              <el-option label="底层科目" value="DCACCSTATUS" />
            </el-select>
          </el-form-item>
          <!--ACCID  -->
          <el-form-item v-if="queryForm.type == 'ACCID'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'ACCID'">
            <el-input
              @input="$forceUpdate()"
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
            />
          </el-form-item>
          <!-- ACCNAME -->
          <el-form-item v-if="queryForm.type == 'ACCNAME'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'ACCNAME'">
            <el-input
              @input="$forceUpdate()"
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
            />
          </el-form-item>
          <!-- DC -->
          <el-form-item v-if="queryForm.type == 'DC'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'DC'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.keyword"
              placeholder="方向"
            >
              <el-option label="借" value="借" />
              <el-option label="贷" value="贷" />
            </el-select>
          </el-form-item>
          <!-- HIGHACCID -->
          <el-form-item v-if="queryForm.type == 'HIGHACCID'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'HIGHACCID'">
            <el-input
              @input="$forceUpdate()"
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
            />
          </el-form-item>
          <!-- ACCAllNAME -->
          <el-form-item v-if="queryForm.type == 'ACCAllNAME'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'ACCAllNAME'">
            <el-input
              @input="$forceUpdate()"
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
            />
          </el-form-item>
          <!-- IGRADE -->
          <el-form-item v-if="queryForm.type == 'IGRADE'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'IGRADE'">
            <el-input
              @input="$forceUpdate()"
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
            />
          </el-form-item>
          <!-- DCACCSTATUS -->
          <el-form-item v-if="queryForm.type == 'DCACCSTATUS'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.status"
              placeholder="条件"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'DCACCSTATUS'">
            <el-select
              @change="$forceUpdate()"
              v-model="queryForm.keyword"
              placeholder="是否底层科目"
            >
              <el-option label="是" value="1" />
              <el-option label="否" value="0" />
            </el-select>
          </el-form-item>
          <!-- <el-form-item>
            <el-select
              v-model="queryForm.status"
              placeholder="条件"
              v-if="queryForm.type == '2' || queryForm.type == 'ACCAllNAME'"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="包含" value="包含" />
              <el-option label="不包含" value="不包含" />
            </el-select>
            <el-select
              v-model="queryForm.status"
              placeholder="条件"
              v-if="queryForm.type == 'DC' || queryForm.type == 'DCACCSTATUS'"
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
            </el-select>
            <el-select
              v-model="queryForm.status"
              placeholder="条件"
              v-if="
                queryForm.type == 'ACCID' ||
                queryForm.type == 'IGRADE' ||
                queryForm.type == 'HIGHACCID'
              "
            >
              <el-option label="等于" value="等于" />
              <el-option label="不等于" value="不等于" />
              <el-option label="大于" value="大于" />
              <el-option label="小于" value="小于" />
              <el-option label="大于等于" value="大于等于" />
              <el-option label="小于等于" value="小于等于" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="queryForm.keyword"
              clearable
              placeholder="关键字"
              v-if="
                queryForm.type == 'ACCID' ||
                queryForm.type == '2' ||
                queryForm.type == 'HIGHACCID' ||
                queryForm.type == 'ACCAllNAME'
              "
            />
            <el-select
              v-model="queryForm.keyword"
              placeholder="方向"
              v-if="queryForm.type == 'DC'"
            >
              <el-option label="借" value="借" />
              <el-option label="贷" value="贷" />
            </el-select>
            <el-select
              v-model="queryForm.keyword"
              placeholder="级次"
              v-if="queryForm.type == 'IGRADE'"
            >
              <el-option label="1" value="1" />
              <el-option label="2" value="2" />
              <el-option label="3" value="3" />
              <el-option label="4" value="4" />
            </el-select>
            <el-select
              v-model="queryForm.keyword"
              placeholder="是否底层科目"
              v-if="queryForm.type == 'DCACCSTATUS'"
            >
              <el-option label="是" value="是" />
              <el-option label="否" value="否" />
            </el-select>
          </el-form-item> -->
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
        </el-form>
        <el-button
          type="primary"
          @click="showChangeAccountModal"
          style="margin: 0 0 19px auto !important"
        >
          切换账套
        </el-button>
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
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
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      :data="list"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
      v-loading="loading"
    >
      <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column>
      <el-table-column align="center" label="科目编码" prop="accId" />
      <el-table-column align="center" label="科目名称" prop="accName" />
      <el-table-column align="center" label="方向" prop="dcName" />
      <el-table-column align="center" label="上级科目编码" prop="highAccId" />
      <el-table-column align="center" label="上级科目" prop="highAccName" />
      <el-table-column
        align="center"
        label="全名"
        prop="accAllName"
      ></el-table-column>
      <el-table-column align="center" label="级次" prop="grade" />
      <el-table-column align="center" label="底层科目" prop="dcAccStatus">
        <template #default="{ row }">
          {{ row.dcAccStatus == 1 ? '是' : '否' }}
        </template>
      </el-table-column>
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
    <LcdyEdit ref="edit" @fetch-data="fetchData" />

    <FlawInfo ref="flaw" @fetch-data="fetchData" />
    <DoubtfulInfo ref="doubtful" @fetch-data="fetchData" />
    <RiskInfo ref="risk" @fetch-data="fetchData" />
    <MyDraftInfo ref="manuscript" @fetch-data="fetchData" />

    <DiGaoFile ref="digaoFile" @fetch-data="fetchData" />
    <ChangeAccountModal
      ref="changeAccountModal"
      @fetch-data="fetchData"
    ></ChangeAccountModal>
  </div>
</template>

<script>
  import {
    getSubjectList,
    getAccountFileList,
  } from '@/api/workbench/accountData/accountData'
  import { doDelete } from '@/api/table'
  import LcdyEdit from '@/views/setting/system/components/LcdyEdit'
  import getUserSelectedBookInfo from './../utils/getBookInfo'
  import DiGaoFile from './fly/toDiGaoFile.vue'
  import ChangeAccountModal from './components/changeAccountModal.vue'
  import FlawInfo from '@/views/audit/question/components/FlawInfo.vue'
  import DoubtfulInfo from '@/views/audit/implement/components/DoubtfulInfo'
  import RiskInfo from '@/views/audit/question/components/RiskInfo'
  import MyDraftInfo from '@/views/audit/implement/components/myDraftInfo'

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
        },
        selectList: [],
        select: [],
      }
    },
    created() {
      this.fetchData()
    },
    async mounted() {
      if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
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
        this.queryForm.pageNo = 1
        this.fetchData()
      },
      async fetchData() {
        let params = JSON.parse(JSON.stringify(this.queryForm))

        params.bookYear = this.bookInfo.bookYear

        // params.wh1 = ''
        const type = params.type
        //
        if (type) {
          switch (type) {
            case 'ACCID':
              params.accId = params.keyword
              break
            case 'ACCNAME':
              params.accName = params.keyword
              break
            case 'DC':
              params.dc = params.keyword
              break
            case 'HIGHACCID':
              params.highAccId = params.keyword
              break
            case 'ACCAllNAME':
              params.accAllName = params.keyword
              break
            case 'IGRADE':
              params.igrade = params.keyword
              break
            case 'DCACCSTATUS':
              params.dcAccStatus = params.keyword
              break
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
        if (this.run) {
          params = this.run
        }
        this.loading = true
        const {
          data: { list, total },
        } = await getSubjectList(params)
        this.list = list
        this.total = total
        this.loading = false

        this.setCheckedRows()
      },
      reset() {
        this.queryForm = {
          pageNo: 1,
          pageSize: 20,
        }
        this.select = []
        this.run = this.$options.data().run
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
        const i = this.select.findIndex((x) => x == row.typeId)
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
            if (row && !this.select.some((x) => x.typeId == row.typeId)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.typeId == row.typeId)
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
        const id = this.select.map((res) => res.accId)
        this.loading = true
        const info = await getAccountFileList({
          accidStrs: id.toString(),
          exprotType: 1,
        })
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
    },
  }
</script>
