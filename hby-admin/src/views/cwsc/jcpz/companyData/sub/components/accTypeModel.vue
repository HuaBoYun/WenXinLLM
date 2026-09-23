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
          <el-button
            icon="el-icon-search"
            native-type="submit"
            type="primary"
            @click="fetchData"
            clearable
          >
            查询
          </el-button>
          <el-button native-type="submit" type="default" @click="reset">
            重置
          </el-button>
        </el-form>
      </vab-query-form-top-panel>
    </vab-query-form>
    <vab-query-form>
      <vab-query-form-left-panel></vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button type="primary" @click="add">确定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="loading"
      :data="list"
      highlight-current-row
      @current-change="handleSelected"
      ref="multipleTable"
      row-key="pkAccount"
      :tree-props="{ children: 'childrenList' }"
    >
      <!-- <el-table-column
        align="center"
        type="selection"
        width="55"
      ></el-table-column> -->
      <el-table-column align="center" label="科目编码" prop="code" />
      <el-table-column align="center" label="科目名称" prop="name" />
      <el-table-column align="center" label="方向" prop="balanorient">
        <template #default="{ row }">
          {{ row.balanorient == 0 ? '借方' : '贷方' }}
        </template>
      </el-table-column>
      <!-- <el-table-column align="center" label="上级科目编码" prop="highAccId" />
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
      </el-table-column> -->
    </el-table>
    <TreeModal ref="selectSubject" @select="handleSelectSubject" />
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNo"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
  </el-dialog>
</template>

<script>
  import { getSubjectList } from '@/api/workbench/accountData/accountData'
  import { getKmList } from '@/api/cwsc.js'
  import TreeModal from '../../treeModal.vue'
  export default {
    name: 'accTypeModel',
    components: {
      TreeModal,
    },
    inheritAttrs: false,
    props: ['pkOrg'],
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
          pkorg: '',
          pkName: '',
          name: '',
          code: '',
          balanorient: '',
          pid: '',
        },
        current: {},
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
          pkorg: '',
          pkName: '',
          name: '',
          code: '',
          balanorient: '',
          pid: '',
        }
        this.fetchData()
      },
      selectSubject() {
        this.$refs['selectSubject'].showEdit()
      },
      handleSelectSubject(data) {
        this.queryForm.pkorg = data.pkOrg
        this.queryForm.pkName = data.name
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
        // 优先使用用户选择的组织，否则使用 props 传入的
        if (!params.pkorg && this.pkOrg) {
          params.pkorg = this.pkOrg
        }
        // params.bookYear = this.bookInfo.bookYear

        // params.wh1 = ''
        // const type = params.type
        //
        // if (type) {
        //   switch (type) {
        //     case 'ACCID':
        //       params.accId = params.keyword
        //       break
        //     case 'ACCNAME':
        //       params.accName = params.keyword
        //       break
        //     default:
        //       break
        //   }
        // }
        this.loading = true
        const { data } = await getKmList(params)
        this.list = data
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
  .w200 {
    width: 200px;
  }
</style>
