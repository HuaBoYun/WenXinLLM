<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1200px"
    :close-on-click-modal="false"
    append-to-body
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <DeepTree @select="handleNodeClick" :isAll="isAll" />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
            <div v-if="select.length > 0">
              <el-tag type="info" v-for="item in select" :key="item.staffid">
                {{ item.realname }}
              </el-tag>
            </div>
            <vab-query-form-left-panel>
              <el-input
                v-model="queryForm.realname"
                placeholder="请输入用户名"
                clearable
                style="width: 50%; margin-right: 10px"
              />
              <el-button
                type="primary"
                @click="getExecutorList"
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
            v-loading="listLoading"
            :data="list"
            ref="multipleTable"
            style="width: 100%"
            @select="handleSelection"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="orgname" label="所属部门"></el-table-column>
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
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import DeepTree from '@/components/DepTree.vue'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: true,
      },
      secrectLevelId: {
        type: Number | String,
        default: '',
      },
      isAll: {
        type: Boolean,
        default: false,
      },
    },
    components: { DeepTree },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const curOrgId = userInfo.currentOrg.orgid
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curOrgId,
        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],
        defaultProps: {
          children: 'children',
          label: 'name',
          isLeaf: (data) => !data.isParent,
        },
        queryForm: {
          orgid: undefined,
          formSecrectId: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        },
        current: undefined,
        reviewType: '',
        select: [],
        mjId: '',
      }
    },
    methods: {
      showEdit(e, id) {
        console.log(e, id)
        this.queryForm = {
          orgid: undefined,
          formSecrectId: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        }
        if (e) {
          this.reviewType = e
        }
        if (id) {
          this.queryForm.formSecrectId = id
        }
        this.dialogVisible = true
        this.current = undefined
        this.realname = undefined
        this.getExecutorList()
        this.select = []
      },

      async getExecutorList() {
        if (this.queryForm.realname) {
          this.queryForm.pageSize = 20
          this.queryForm.pageNumber = 1
        }
        this.queryForm.formSecrectId = this.secrectLevelId
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson(this.queryForm)
        this.list = list
        this.total = total
        this.listLoading = false
      },
      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.orgid = val.id
        this.queryForm.pageSize = 20
        this.queryForm.pageNumber = 1
        this.getExecutorList()
      },
      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('projectManage', this.select)
        this.dialogVisible = false
      },
      reset() {
        this.queryForm.realname = ''
        this.getExecutorList()
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
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
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 30px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
