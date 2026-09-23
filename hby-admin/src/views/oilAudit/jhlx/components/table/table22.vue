<template>
  <el-dialog
    title="任中审计明细"
    :visible.sync="dialogVisible"
    width="50%"
    append-to-body
    :close-on-click-modal="false"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="16">
        <!-- <el-select
          v-model="userId"
          multiple
          style="width: 400px"
          @remove-tag="removeTag"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.projectName"
            :value="item.id"
          ></el-option>
        </el-select> -->
        <el-form
          ref="form"
          checkable
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-row>
            <el-form-item>
              <el-input
                v-model="queryForm.projectName"
                clearable
                placeholder="项目名称"
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
              <el-button type="primary" @click="resetSearch()">重置</el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </vab-query-form-left-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      :max-height="600"
      @select-all="handleSelectAll"
      @select="handleSelection"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <!-- <el-table-column align="center" label="编号" prop="tbname" >
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleDetail(row)"
            style="white-space: pre-line; line-height: 16px"
          >
            {{ row.tbname }}
          </el-button>
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="项目名称" prop="projectName">
        <template #default="{ row }">
          <el-button
            type="text"
            @click="handleDetail(row)"
            style="white-space: pre-line; line-height: 16px"
          >
            {{ row.projectName }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="orgId" label="被审计单位" show-overflow-tooltip>
        <template #default="{ row }">
          {{ row.org && row.org.orgname }}
        </template>
      </el-table-column>
      <el-table-column label="审计范围" prop="workStartTime">
        <template #default="{ row }">
          {{ row.workStartTime }} - {{ row.workEndTime }}
        </template>
      </el-table-column>
      <el-table-column label="委托时间" prop=""></el-table-column>
      <el-table-column label="备注" prop="remarks"></el-table-column>
      <el-table-column label="项目类型" prop="projectlx"></el-table-column>
      <!-- <el-table-column
        label="工作开始时间"
        
        prop="workStartTime"
      ></el-table-column>
      <el-table-column
        label="工作结束时间"
        
        prop="workEndTime"
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
      ></el-table-column> -->
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
    <rzsjmxView
      ref="edit"
      @fetchData="fetchData"
      @selected="selected"
    ></rzsjmxView>
  </el-dialog>
</template>
<script>
  import { UTCformat } from '@/utils'
  import { suggestion2LGetList } from '@/api/monitor/question'
  import {
    audit2LsaveOrUpdate2,
    getListDraftPlan,
    getRzsjmxListDraftPlan,
  } from '@/api/oilAudit/jhgl/jhcg'
  import rzsjmxView from '@/views/oilAudit/lrjjzr/components/rzsjmxView.vue'
  export default {
    components: { rzsjmxView },
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        multipleSelection: [],

        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          tbname: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
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
      resetSearch() {
        this.resetQueryForm()
      },
      resetQueryForm() {
        this.queryForm = {
          tbname: '',
          projectName: '',
          pageNumber: 1,
          pageSize: 20,
        }
        this.queryData()
      },
      async showEdit(row, other, sourceType = 1, jhid = '') {
        this.row = row
        this.sourceType = sourceType
        this.jhid = jhid
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

        const res = await getRzsjmxListDraftPlan({
          ...this.queryForm,
          sourceType: this.sourceType,
          jhid: this.jhid,
        })
        this.list = res.data.tlist

        this.total = res.data.totalRecord || 0

        // setTimeout(() => {
        //   if (this.row) {
        //     let check = res.data.tlist.find((v) => v.id == this.row.id)
        //     console.log(check)
        //     this.$refs.multipleTable.toggleRowSelection(check, true)
        //   }
        // }, 300)
        this.listLoading = false
        this.setCheckedRows()
      },
      // handleSelection(val) {
      //   if (val.length > 1) {
      //     let del = val.shift()
      //     this.$refs.multipleTable.toggleRowSelection(del, false)
      //   }
      //   this.multipleSelection = val
      // },
      handleSelectionChange(val) {
        // this.multipleSelection = val
      },
      getNewArr(a, b) {
        const arr = [...a, ...b]
        const newArr = arr.filter((item) => {
          return !(a.includes(item) && b.includes(item))
        })
        return newArr
      },
      onSelect(rows, row) {
        let selected = rows.length && rows.indexOf(row) !== -1

        let list = this.userList || []
        if (selected) {
          list.push(row)
        } else {
          list = list.filter((item) => item.id !== row.id)
        }
        this.userId = list.map((item) => item.id)
        this.userList = list
        this.$forceUpdate()
      },
      async removeTag(e) {
        let list = this.userList
        let id = this.userId
        list = await list.filter((item) => item && item.id != e)
        id = await id.filter((item) => item !== e)
        this.userList = list
        this.userId = id
        this.$forceUpdate()
        await this.$refs.multipleTable.clearSelection()
        await this.setSelection(list)
      },
      save() {
        // if (!this.multipleSelection || !this.multipleSelection.length) {
        //   return this.$message.error('请选择数据')
        // }
        // this.appendVisible = true
        // this.$emit('fetch-table', this.multipleSelection)
        // this.dialogVisible = false
        if (this.multipleSelection.length == 0) {
          return this.$message.error('请选择数据')
        }
        this.$emit('fetch-table', this.multipleSelection)
        this.multipleSelection = []
        this.userId = []
        this.dialogVisible = false
      },
      handleDetail(row) {
        this.$refs['edit'].showEdit({ id: row.id }, '详情')
      },
      selected(val) {
        const arr = this.tableData.filter((res) => res.id != val.id)
        this.tableData = [...arr, val]
      },
      saveAuditScope() {
        // if (!this.formData.auditScope) {
        //   return this.$message.error('请输入审计范围')
        // }

        this.loading = true
        audit2LsaveOrUpdate2({
          id: this.multipleSelection[0].id,
          ...this.formData,
        })
          .then((res) => {
            if (res && res.code === 1) {
              this.appendVisible = false
              this.multipleSelection[0].entrustTime = this.formData.entrustTime
              this.multipleSelection[0].projectType = this.formData.projectType
              this.multipleSelection[0].auditScope = this.formData.auditScope
              this.multipleSelection[0].remarks = this.formData.remarks
              this.$emit('fetch-table', this.multipleSelection)
              this.dialogVisible = false
              this.formData.entrustTime = ''
              this.formData.projectType = ''
              this.formData.auditScope = ''
              this.formData.remarks = ''
            } else {
              this.$message.error(res.msg || '请求出错')
            }
          })
          .finally(() => {
            this.loading = false
          })
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
