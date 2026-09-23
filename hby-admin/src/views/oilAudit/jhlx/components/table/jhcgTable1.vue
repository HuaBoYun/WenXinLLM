<template>
  <el-dialog
    title="生产经营管理专项审计"
    :visible.sync="dialogVisible"
    width="70%"
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
      style="width: 100%"
      :max-height="600"
      @select-all="handleSelectAll"
      @select="handleSelection"
      ref="multipleTable"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column
        align="center"
        label="业务类型"
        prop="projectType"
      ></el-table-column>
      <el-table-column
        align="center"
        label="建议科室"
        prop="departmentName"
      ></el-table-column>
      <el-table-column
        align="center"
        label="排序"
        prop="sortNumber"
      ></el-table-column>
      <el-table-column align="center" label="审计项目名称" prop="projectName">
        <template #default="{ row }">
          <el-button type="text" @click="handleDetail(row)">
            {{ row.projectName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="立项理由及审计目的"
        prop="projectPurpose"
      />
      <el-table-column
        align="center"
        label="重点关注内容"
        prop="concernsContent"
      />
      <el-table-column align="center" label="单位范围" prop="unitRange" />
      <el-table-column
        align="center"
        label="时间范围"
        prop="activityTime"
        #default="{ row }"
        width="200"
      >
        {{ row.timeRangel + ' ~ ' + row.timeRangeR }}
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" />
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

    <!-- <el-dialog
      title="补录"
      :visible.sync="appendVisible"
      width="50%"
      append-to-body
      :close-on-click-modal="false"
    >
      <el-form ref="ruleForm" label-width="80px" :model="formData" size="mini">
        <el-col :span="24">
          <el-form-item label="审计范围" prop="auditScope">
            <el-input
              v-model.trim="formData.auditScope"
              placeholder="请输入审计范围"
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
    </el-dialog> -->

    <lxjyzypgEdit ref="lxjyzypgEdit" />
  </el-dialog>
</template>
<script>
  import { getLxZxsjList, updateAuditScope } from '@/api/oilAudit/jhgl/jhcg'
  import * as dayjs from 'dayjs'
  import lxjyzypgEdit from '@/views/oilAudit/jhlx/components/lxjyzypgEdit.vue'

  export default {
    components: { lxjyzypgEdit },
    data() {
      return {
        dialogVisible: false,
        multipleSelection: [],
        formData: {
          auditScope: '',
        },

        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          projectName: '',
        },
        appendVisible: false,
        loading: false,
        row: null,
        userId: [],
        userList: [],
        sourceType: 1,
        jhid: '',
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
      async showEdit(row, projectType, sourceType = 1, jhid = '') {
        this.projectType = projectType
        this.sourceType = sourceType
        this.jhid = jhid
        this.list = []
        this.fetchData()

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
        const res = await getLxZxsjList({
          ...this.queryForm,
          projectType: this.projectType,
          sourceType: this.sourceType,
          jhid: this.jhid,
        })
        this.list = res.data.tlist.map((x) => {
          x.timeRangeR = dayjs(x.timeRangeR).format('YYYY-MM-DD')
          x.timeRangel = dayjs(x.timeRangel).format('YYYY-MM-DD')
          return x
        })
        // setTimeout(() => {
        //   if (this.row) {
        //     let check = res.data.tlist.find((v) => v.id == this.row.id)
        //     console.log(check)
        //     this.$refs.multipleTable.toggleRowSelection(check, true)
        //   }
        // }, 300)
        this.total = res.data.totalRecord || 0
        this.listLoading = false
        this.setCheckedRows()
      },
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选中数据
       * @return {*}
       */
      async handleDetail(row) {
        // const data = await approachSummaryDisp({ enterid: row.enterid })
        await this.$refs['lxjyzypgEdit'].showEdit('detail', row)
      },
      save() {
        console.log(this.multipleSelection, 'this.multipleSelection')
        if (this.multipleSelection.length == 0) {
          return this.$message.error('请选择数据')
        }
        this.$emit('fetch-table', this.multipleSelection)
        this.multipleSelection = []
        this.list = []
        this.dialogVisible = false
        // this.appendVisible = true
      },
      // saveAuditScope() {
      //   if (!this.formData.auditScope) {
      //     return this.$message.error('请输入审计范围')
      //   }

      //   this.loading = true
      //   updateAuditScope({
      //     id: this.multipleSelection[0].id,
      //     ...this.formData,
      //   })
      //     .then((res) => {
      //       if (res && res.code === 1) {
      //         this.appendVisible = false
      //         this.multipleSelection[0].auditScope = this.formData.auditScope
      //         this.$emit('fetch-table', this.multipleSelection)
      //         this.dialogVisible = false
      //         this.list = []
      //         this.formData.auditScope = ''
      //       } else {
      //         this.$message.error(res.msg || '请求出错')
      //       }
      //     })
      //     .finally(() => {
      //       this.loading = false
      //     })
      // },
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
        this.fetchData()
      },
    },
  }
</script>
<style scoped lang="scss"></style>
