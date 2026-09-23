<template>
  <el-dialog
    title="选择人员"
    :visible.sync="dialogVisible"
    width="1000px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
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
              <el-input
                v-model="queryForm.realName"
                clearable
                placeholder="名称"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.education"
                clearable
                placeholder="学历"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.jobExperiences"
                clearable
                placeholder="经验"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.jobName"
                clearable
                placeholder="岗位"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                v-model="queryForm.major"
                clearable
                placeholder="专业"
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
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>

      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @select="handleSelection"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          align="center"
          label="真实姓名"
          width="100"
          prop="realname"
        ></el-table-column>
        <el-table-column align="center" label="用户姓名" prop="username" />
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
    </div>
  </el-dialog>
</template>
<script>
  import { loadPersonData } from '@/api/audit/structure'
  export default {
    components: {},
    name: 'xxx',
    data() {
      return {
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          realName: '',
          education: '',
          jobExperiences: '',
          jobName: '',
          major: '',
          pageNumber: 1,
          pageSize: 20,
        },
        listLoading: false,
        list: [],
        dialogVisible: false,
        multipleSelection: [],
        current: undefined,
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        const { ...other } = this.queryForm
        const {
          data: {
            pageInfo: { tlist: list, totalRecord: total },
          },
        } = await loadPersonData({ ...other })
        this.list = list
        this.total = total
        this.listLoading = false
      },
      resetQueryForm() {
        this.queryForm = this.$options.data().queryForm
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      showEdit() {
        this.dialogVisible = true
        this.current = undefined
        this.fetchData()
      },
      save() {
        if (!this.current) {
          this.$baseMessage('请选择人员', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('handlePersonInfo', this.multipleSelection)
        this.dialogVisible = false
      },
      handleSelection(val) {
        this.current = val
        // if (this.flagTitle) {
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
        // } else {
        //   this.multipleSelection = val
        // }
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
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 200px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    width: 75%;
  }
</style>
