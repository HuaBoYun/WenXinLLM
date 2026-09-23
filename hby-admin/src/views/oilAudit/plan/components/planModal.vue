<template>
  <el-dialog
    title="计划项目"
    :visible.sync="dialogVisible"
    width="50%"
    :modal="false"
    :close-on-click-modal="false"
  >
    <vab-query-form class="margin-b0">
      <el-card shadow="never">
        <vab-query-form-top-panel>
          <el-form
            ref="form"
            :inline="true"
            label-width="0"
            :model="queryForm"
            @submit.native.prevent
          >
            <el-form-item
              :prop="item.key"
              v-for="(item, index) in searchItem"
              :key="index"
            >
              <el-input
                v-model="queryForm.jhmc"
                clearable
                v-if="item.name === '计划名称'"
                placeholder="计划名称"
              />

              <!-- <el-date-picker
                v-model="queryForm.sj"
                placeholder="时间"
                type="datetime"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '时间'"
              /> -->
              <el-input
                v-model="queryForm.cjr"
                clearable
                v-if="item.name === '编制人'"
                placeholder="编制人"
              />
              <el-date-picker
                v-model="queryForm.cjsj"
                placeholder="编制时间"
                type="date"
                format="yyyy-MM-dd"
                value-format="yyyy-MM-dd"
                v-if="item.name === '编制时间'"
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
          </el-form>
        </vab-query-form-top-panel>
      </el-card>
    </vab-query-form>

    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        :key="index"
        label="计划名称"
        prop="sjxmmc"
      >
        <!-- <template #default="{ row }">
              <el-button type="text" @click="handleDetail(row)">
                {{ row.sjxmmc }}
              </el-button>
            </template> -->
      </el-table-column>
      <!-- <el-table-column prop="bsjdwmc" label="被审计单位" show-overflow-tooltip>
        <template #default="{ row }">
              {{row.oldOrg.orgname}}
            </template>
      </el-table-column> -->
      <el-table-column
        align="center"
        label="更新时间"
        prop="gxsj"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="编制人"
        prop="cjr"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="创建时间"
        prop="cjsj"
        show-overflow-tooltip
      />
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
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { jhgljhList } from '@/api/monitor/question'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        queryForm: {
          jhmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
          pageNumber: 1,
          pageSize: 20,
        },
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        searchAll: this.getFiled(), //所有搜索项
        searchNow: [], //当前所有搜索项
        searchItem: [], //可见搜索项
      }
    },
    created() {
      this.searchNow = this.getFiled()
      this.searchItem = this.searchNow.slice(0, 4)
    },
    methods: {
      getFiled() {
        return [
          { name: '计划名称', key: 'jhmc' },
          { name: '编制人', key: 'cjr' },
          { name: '编制时间', key: 'cjsj' },
        ]
      },
      resetQueryForm() {
        // this.queryForm = this.$options.data().queryForm;
        this.queryForm = {
          jhmc: '',
          cjr: '',
          cjsj: '',
          sj: '',
          pageNumber: 1,
          pageSize: 20,
        }
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      /**
       * @description  初始化
       * @param {*}
       * @return {*}
       */
      async fetchData() {
        const {
          data: { tlist, totalRecord },
        } = await jhgljhList(this.queryForm)
        this.tableData = tlist || []
        this.total = totalRecord || 0
      },
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */
      async showEdit() {
        this.dialogVisible = true
        // const {
        //   data: { tlist, totalRecord },
        // } = await jhgljhList(this.queryForm)
        // this.tableData = tlist || []
        // this.total = totalRecord || 0
        this.fetchData()
      },
      /**
       * @description  选择列表数据，把数据存入multipleSelection
       * @param {*}
       * @return {*}
       */
      handleSelection(val) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description   点击确定,把数据回传到父组件,关闭当前组件
       * @param {*}
       * @return {*}
       */
      save() {
        this.$emit('selected', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss">
  // 隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
