<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="20">
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
          <el-form-item>
            <el-button
              icon="el-icon-search"
              native-type="submit"
              type="primary"
              @click="fetchData"
            >
              查询
            </el-button>
            <el-button native-type="submit" type="default" @click="reset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="4">
        <el-button type="primary" @click="add">确定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="loading"
      :data="list"   
      highlight-current-row
      @current-change="handleSelected"
      ref="multipleTable"
    >
      <!-- <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column> -->
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
  </el-dialog>
</template>

<script>
import { getSubjectList } from '@/api/workbench/accountData/accountData'
export default {
  name: 'accTypeModel',
  components: {},
  inheritAttrs: false,
  props: [],
  data() {
    return {
      dialogFormVisible: false,
      loading: false,
      list: [],
      layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
      title: '科目表',
      queryForm: {
        pageNo: 1,
        pageSize: 20,
      },
      current:{},
    }
  },
  computed: {},
  watch: {},
  created() {},
  async mounted() {
    if (!localStorage.getItem('bookInfo')) await getUserSelectedBookInfo()
    const bookInfo = localStorage.getItem('bookInfo')
    this.bookInfo = JSON.parse(bookInfo)
  },
  methods: { 
    handleSelected(val) { 
        this.current = val 
      },
      add() {
        if (!this.current) {
          this.$baseMessage('请选择！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('selected', this.current)
        this.dialogFormVisible = false
      },
    handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNo = val
        this.fetchData()
      },
    reset() {
      this.queryForm = {
        pageNo: 1,
        pageSize: 20,
        status: '',
        keyword: '',
      }
      this.fetchData()
    },
    clearAueryForm() {
      this.queryForm.status = ''
      this.queryForm.keyword = ''
      this.$forceUpdate()
    },
    showEdit() {
      this.fetchData()
      this.dialogFormVisible = true
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
          default:
            break
        }
      }
      this.loading = true
      const {
        data: { list, total },
      } = await getSubjectList(params)
      this.list = list
      this.total = total
      this.loading = false
    },
    handleEdit(row) {},
    close() {
      this.dialogFormVisible = false
    },
  },
}
</script>
<style scoped>
.el-form-item__content span {
  font-size: 14px;
  font-weight: 500;
  color: darkgray;
}
</style>
