<template>
  <el-dialog
    title="二级单位离任及预计离任明细"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="16">
        <el-input
          v-model="queryForm.projectName"
          placeholder="请输入项目名称"
          clearable
          style="width: 30%; margin-right: 10px"
        />
        <el-input
          v-model="queryForm.auditOrg"
          placeholder="请输入被审计单位"
          clearable
          style="width: 30%; margin-right: 10px"
        />
        <el-button
          type="primary"
          @click="fetchData"
          style="margin-top: 10px !important"
        >
          查询
        </el-button>
        <el-button
          type="primary"
          @click="reset"
          style="margin-top: 10px !important"
        >
          重置
        </el-button>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="项目名称" prop="projectName">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetailLRJYJLRSJview(row)">
            {{ row.projectName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="auditOrgId" label="项目单位" show-overflow-tooltip>
        <template #default="{ row }">
          {{ row.auditOrg && row.auditOrg.orgname }}
        </template>
      </el-table-column>
      <el-table-column
        label="审计开始时间"
        width="120"
        prop="auditStartTime"
      ></el-table-column>
      <el-table-column
        label="审计结束时间"
        width="120"
        prop="auditEndTime"
      ></el-table-column>
      <el-table-column prop="createTime" label="编制人" show-overflow-tooltip>
        <template #default="{ row }">
          {{ row.createUser && row.createUser.realname }}
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="编制时间"
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
    <LRJYJLRSJview ref="LRJYJLRSJview"></LRJYJLRSJview>
    <el-dialog
      title="补录"
      :visible.sync="appendVisible"
      width="50%"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="ruleForm" label-width="80px" :model="formData" size="mini">
        <el-col :span="24">
          <el-form-item label="委托时间" prop="entrustTime">
            <el-date-picker
              v-model="formData.entrustTime"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              type="date"
              placeholder="选择日期"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="审计范围" prop="auditScope">
            <el-input
              v-model.trim="formData.auditScope"
              placeholder="请输入审计范围"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="项目类型" prop="projectType">
            <el-input
              v-model.trim="formData.projectType"
              placeholder="请输入项目类型"
              :style="{ width: '100%' }"
            />
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
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { getListDraftPlan } from '@/api/monitor/question'
  import { audit2LsaveOrUpdate } from '@/api/oilAudit/jhgl/jhcg'
  import LRJYJLRSJview from '@/views/oilAudit/lrjjzr/components/lrjyjlrsjView.vue'
  export default {
    components: { LRJYJLRSJview },
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
          auditOrg: '',
          projectName: '',
        },
        row: null,
        appendVisible: false,
        loading: false,
        formData: {
          entrustTime: '',
          projectType: '',
          auditScope: '',
          remarks: '',
        },
        userId: [],
        userList: [],
        sourceType: 1,
      }
    },
    methods: {
      setSelection(list) {
        this.$nextTick(() => {
          list.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      async showEdit(row, other, sourceType = 1, jhid = '') {
        this.row = row
        this.sourceType = sourceType
        this.jhid = jhid
        this.queryData()
        this.multipleSelection = []
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
        const res = await getListDraftPlan({
          ...this.queryForm,
          sourceType: this.sourceType,
          jhid: this.jhid,
        })
        this.list = res.data.tlist
        console.log('list', this.list)

        this.total = res.data.totalRecord || 0
        this.listLoading = false
        // this.setCheckedRows()
        // setTimeout(() => {
        //   if (this.row) {
        //     let check = res.data.tlist.find((v) => v.id == this.row.id)
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
      save() {
        if (this.multipleSelection.length == 0) {
          return this.$message.error('请选择数据')
        }
        this.$emit('fetch-table', this.multipleSelection)
        this.dialogVisible = false
      },
      saveAuditScope() {
        //   // if (!this.formData.auditScope) {
        //   //   return this.$message.error('请输入审计范围')
        //   // }
        //   this.loading = true
        //   audit2LsaveOrUpdate({
        //     id: this.multipleSelection[0].id,
        //     ...this.formData,
        //   })
        //     .then((res) => {
        //       if (res && res.code === 1) {
        //         this.appendVisible = false
        //         this.multipleSelection[0].entrustTime = this.formData.entrustTime
        //         this.multipleSelection[0].projectType = this.formData.projectType
        //         this.multipleSelection[0].auditScope = this.formData.auditScope
        //         this.multipleSelection[0].remarks = this.formData.remarks
        //         this.$emit('fetch-table', this.multipleSelection)
        //         this.dialogVisible = false
        //         this.formData.entrustTime = ''
        //         this.formData.projectType = ''
        //         this.formData.auditScope = ''
        //         this.formData.remarks = ''
        //       } else {
        //         this.$message.error(res.msg || '请求出错')
        //       }
        //     })
        //     .finally(() => {
        //       this.loading = false
        //     })
      },
      handleDetailLRJYJLRSJview(row) {
        this.$refs['LRJYJLRSJview'].showEdit(row, true)
      },
      handleSelection(val, row) {
        const i = this.multipleSelection.findIndex((x) => x.id == row.id)
        if (i < 0) {
          this.multipleSelection.push(row)
        } else {
          this.multipleSelection.splice(i, 1)
        }
        console.log(this.multipleSelection, 'this.multipleSelection')
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.multipleSelection.some((x) => x.id == row.id)) {
              this.multipleSelection.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.multipleSelection.findIndex((x) => x.id == row.id)
            if (i >= 0) {
              this.multipleSelection.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.multipleSelection.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.id == item.id
              }),
              true
            )
          })
        })
      },
      reset() {
        this.queryForm.projectName = ''
        this.queryForm.auditOrg = ''
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss">
  //隐藏表头全选框
</style>
