<template>
  <el-dialog
    v-if="dialogFormVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <!-- <component
      :is="componentId"
      :is-inner="true"
      @selected="selected"
    ></component>  -->
    <el-form
      ref="form"
      :inline="true"
      label-width="0"
      :model="queryForm"
      @submit.native.prevent
    >
      <el-form-item>
        <el-input v-model="queryForm.gcmc" clearable placeholder="工程名称" />
      </el-form-item>
      <el-form-item>
        <el-input v-model="queryForm.htbh" clearable placeholder="合同编号" />
      </el-form-item>
      <el-form-item>
        <el-date-picker
          v-model="queryForm.createYear"
          type="year"
          value-format="yyyy"
          placeholder="请选择创建年度"
        ></el-date-picker>
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

    <el-table
      v-loading="listLoading"
      :data="list"
      ref="multipleTable"
      @select-all="$refs.multipleTable.clearSelection()"
      @select="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" label="序号" type="index" width="50" />
      <el-table-column align="center" label="合同编号" prop="htbh">
        <!-- <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.htbh }}
            </el-button>
          </template> -->
      </el-table-column>
      <el-table-column
        align="center"
        label="工程名称"
        prop="gcmc"
      ></el-table-column>
      <el-table-column
        align="center"
        label="建设单位"
        prop="jsdw"
        show-overflow-tooltip
      />
      <el-table-column
        align="center"
        label="二审审查金额（元）"
        prop="esscje"
        show-overflow-tooltip
        :formatter="(row) => row.esscje?.toFixed(2) || '-'"
      />
      <el-table-column
        align="center"
        label="施工单位"
        prop="sgdw"
        show-overflow-tooltip
      />
      <el-table-column align="center" label="联系人" prop="lxr" />
      <el-table-column align="center" label="联系电话" prop="lxdh" />
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

    <div slot="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="confirm" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import gcxmzj from '../gcxmzj.vue'
import { getChoiceList } from '@/api/oilAudit/jhgl/gcxmzj'
import { formatDate } from '@/utils/index'

export default {
  name: 'selectGczjModal',
  components: { gcxmzj },
  data() {
    return {
      dialogFormVisible: false,
      title: '选择工程项目造价',
      selectedItem: null,
      componentId: 'gcxmzj',
      list: [],
      listLoading: false,
      layout: 'total, sizes, prev, pager, next, jumper',
      total: 0,
      queryForm: {
        gcmc: '',
        htbh: '',
        createYear: '',
        pageNumber: 1,
        pageSize: 20,
      },
      multipleSelection: [],
    }
  },
  methods: {
    showEdit() {
      this.fetchData()
      this.dialogFormVisible = true
    },
    resetQueryForm() {
      this.queryForm = {
        gcmc: '',
        htbh: '',
        createYear: '',
        pageNumber: 1,
        pageSize: 20,
      }
    },
    resetSearch() {
      this.resetQueryForm()
      this.fetchData()
    },
    formatDate(row, column) {
      // 获取单元格数据
      let data = row[column.property]
      return formatDate(data)
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
        data: { tlist, totalRecord },
      } = await getChoiceList(this.queryForm)
      this.list = tlist
      this.total = totalRecord
      this.listLoading = false
    },
    handleSelectionChange(selection, row) {
      this.$refs.multipleTable.clearSelection()
      this.$refs.multipleTable.toggleRowSelection(row)
      this.selectedItem = row
      return
    },
    // selected(row) {
    //   this.selectedItem = row
    //   console.log(this.selectedItem)
    // },
    confirm() {
      if (!this.selectedItem) {
        return this.$message.error('尚未选择数据！')
      }
      let data = {...this.selectedItem}
      data.esscjewy = data.esscje / 10000
      this.$emit('selected', data)
      this.close()
    },
    close() {
      this.dialogFormVisible = false
      this.selectedItem = null
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
