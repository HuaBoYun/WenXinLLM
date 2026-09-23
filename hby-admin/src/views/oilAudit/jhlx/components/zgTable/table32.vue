<template>
  <el-dialog
    title="选择建设项目投资基本情况表"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        label="项目名称"
        width="120"
        prop="projectName"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="relaOrgName"
        label="被审计单位"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="projectCount"
        label="单位数量"
        show-overflow-tooltip
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleJSTZDetail(row)">
            {{ row.projectCount }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="项目金额" prop="projectAmount"></el-table-column>
      <el-table-column
        prop="remarks"
        label="备注"
        show-overflow-tooltip
      ></el-table-column>
    </el-table>
    <!-- <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    /> -->
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>

    <el-dialog
      title="补录"
      :visible.sync="appendVisible"
      width="50%"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="ruleForm" label-width="80px" :model="formData" size="mini">
        <el-col :span="24">
          <el-form-item label="项目数量" prop="itemCount ">
            <el-input
              v-model.trim="formData.itemCount"
              placeholder="请输入项目数量"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
      </el-form>

      <template #footer>
        <el-button @click="appendVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveAuditScope" :loading="loading">
          确 定
        </el-button>
      </template>
    </el-dialog>
    <JSTZmodal ref="JSTZmodal"></JSTZmodal>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { engineeringProjectExaminePlanList } from '@/oapi/audit/plan'
  import { formatDate } from '@/utils/index'
  import {
    audit2LsaveOrUpdate5,
    getListDraftPlan4ZG,
  } from '@/api/oilAudit/jhgl/jhcg'
  import JSTZmodal from '@/views/oilAudit/jhlx/components/zgModal/JSTZModal.vue'
  export default {
    components: {
      JSTZmodal,
    },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
        },
        row: null,
        appendVisible: false,
        loading: false,
        formData: {
          itemCount: '',
        },
        jhcgid: '',
        relaid: '',
      }
    },
    methods: {
      handleJSTZDetail(row) {
        this.$refs['JSTZmodal'].showEdit({ relaid: row.id })
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      async showEdit(jhcgid, table = []) {
        this.jhcgid = jhcgid
        this.relaid = table
          .map((item) => {
            return item.relaid
          })
          .join(',')
        this.queryData()
        this.dialogVisible = true
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
        const res = await getListDraftPlan4ZG({
          ...this.queryForm,
          jhchugid: this.jhcgid,
          relaid: this.relaid,
        })
        this.list = res.data

        // this.total = res.data.total || 0
        this.listLoading = false
        // setTimeout(() => {
        //   if (this.row) {
        //     let check = res.data.find((v) => v.gcxmjgysjhid == this.row.gcxmjgysjhid)
        //     console.log(check)
        //     this.$refs.multipleTable.toggleRowSelection(check, true)
        //   }
        // }, 300)
      },
      // handleSelection(val) {
      //   if (val.length > 1) {
      //     let del = val.shift()
      //     this.$refs.multipleTable.toggleRowSelection(del, false)
      //   }
      //   this.multipleSelection = val
      // },
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      save() {
        if (!this.multipleSelection || !this.multipleSelection.length) {
          return this.$message.error('请选择数据')
        }
        this.$emit('fetch-table', this.multipleSelection)
        this.dialogVisible = false

        // this.appendVisible = true
      },
      saveAuditScope() {
        // if (!this.formData.auditScope) {
        //   return this.$message.error('请输入审计范围')
        // }

        this.loading = true
        audit2LsaveOrUpdate5({
          gcxmjgysjhid: this.multipleSelection[0].gcxmjgysjhid,
          ...this.formData,
        })
          .then((res) => {
            if (res && res.code === 1) {
              this.appendVisible = false
              this.multipleSelection[0].itemCount = this.formData.itemCount

              this.$emit('fetch-table', this.multipleSelection)
              this.dialogVisible = false
              this.formData.itemCount = ''
            } else {
              this.$message.error(res.msg || '请求出错')
            }
          })
          .finally(() => {
            this.loading = false
          })
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
