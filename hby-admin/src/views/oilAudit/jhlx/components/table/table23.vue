<template>
  <el-dialog
    title="三级单位离任审计汇总"
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
          <el-button type="text" @click="handleSJDetail(row)">
            {{ row.projectCount }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="remarks"
        label="备注"
        show-overflow-tooltip
      ></el-table-column>
      <!-- <el-table-column
        prop="externalassig"
        label="是否外委"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          {{ scope.row.externalassig == 0 ? '否' : '是' }}
        </template>
      </el-table-column> -->
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
          <el-form-item label="单位数量" prop="unitCount ">
            <!-- <el-input
              v-model.trim="formData.unitCount"
              placeholder="请输入单位数量"
              :style="{ width: '100%' }"
            /> -->
            <el-input-number
              v-model="formData.unitCount"
              :min="1"
              :max="9999"
            ></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input
              v-model.trim="formData.remarks"
              placeholder="请输入备注"
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
    <SJmodal ref="SJmodal"></SJmodal>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { audit3LGetList } from '@/api/monitor/question'
  import SJmodal from '@/views/oilAudit/jhlx/components/modal/SJModal.vue'
  import {
    audit2LsaveOrUpdate3,
    getListDraftPlan2,
  } from '@/api/oilAudit/jhgl/jhcg'
  export default {
    components: {
      SJmodal,
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
          unitCount: '',
          remarks: '',
        },
      }
    },
    methods: {
      handleSJDetail(row) {
        this.$refs['SJmodal'].showEdit(row)
      },
      async showEdit(row) {
        this.row = row
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

        const res = await getListDraftPlan2({ ...this.queryForm })
        this.list = res.data

        // this.total = res.data.total || 0

        // setTimeout(() => {
        //   if (this.row) {
        //     let check = res.data.find((v) => v.id == this.row.id)
        //     console.log(check)
        //     this.$refs.multipleTable.toggleRowSelection(check, true)
        //   }
        // }, 300)
        this.listLoading = false
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
        // this.appendVisible = true
        this.$emit('fetch-table', this.multipleSelection)
        this.dialogVisible = false
      },
      saveAuditScope() {
        // if (!this.formData.auditScope) {
        //   return this.$message.error('请输入审计范围')
        // }

        this.loading = true
        audit2LsaveOrUpdate3({
          id: this.multipleSelection[0].id,
          ...this.formData,
        })
          .then((res) => {
            if (res && res.code === 1) {
              this.appendVisible = false
              this.multipleSelection[0].unitCount = this.formData.unitCount
              this.multipleSelection[0].remarks = this.formData.remarks
              this.$emit('fetch-table', this.multipleSelection)
              this.dialogVisible = false
              this.formData.unitCount = ''
              this.formData.remarks = ''
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
