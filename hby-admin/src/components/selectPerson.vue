<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="1200px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left" v-if="dialogVisible">
          <DeepTree @select="handleNodeClick" />
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
            @select-all="handleSelectAll"
            @select="handleSelection"
            ref="multipleTable"
            style="width: 100%"
          >
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="orgname" label="所属部门"></el-table-column>
            <el-table-column
              prop="secretLevelName"
              label="密级"
            ></el-table-column>
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
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        },
        mjId: '',
        select: [],
      }
    },
    methods: {
      showEdit(e) {
        // e有值的时候获取密级id
        if (e) {
          this.mjId = e
        }
        this.dialogVisible = true
        this.getExecutorTree()
        this.getExecutorList()
        this.select = []
      },
      async getExecutorTree() {
        const { data } = await findOrganizationByTreeAllss({
          fatherorgid: this.curOrgId,
        })
        this.dataTree = data
      },
      async getExecutorList() {
        if (this.queryForm.realname) {
          this.queryForm.pageSize = 20
          this.queryForm.pageNumber = 1
        }
        this.listLoading = true
        const {
          data: { list, total },
        } = await selectPerson({ ...this.queryForm, formSecrectId: this.mjId })
        this.list = list
        this.total = total
        this.listLoading = false
        this.setCheckedRows()
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
        this.close()
      },
      close() {
        this.dialogVisible = false
        this.queryForm = {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          realname: undefined,
        }
      },
      reset() {
        this.queryForm.realname = ''
        this.getExecutorList()
      },
      handleSelection(val, row) {
        const i = this.select.findIndex((x) => x.staffid == row.staffid)
        if (i < 0) {
          this.select.push(row)
        } else {
          this.select.splice(i, 1)
        }
      },
      handleSelectAll(val) {
        const curSelected = val.filter((x) => !!x)
        if (curSelected && curSelected.length) {
          curSelected.map((row) => {
            if (row && !this.select.some((x) => x.staffid == row.staffid)) {
              this.select.push(row)
            }
          })
        } else {
          this.list.map((row) => {
            const i = this.select.findIndex((x) => x.staffid == row.staffid)
            if (i >= 0) {
              this.select.splice(i, 1)
            }
          })
        }
      },

      // 翻页的时候回显已勾选的数据
      setCheckedRows() {
        this.$nextTick(() => {
          this.select.forEach((row) => {
            this.$refs.multipleTable.toggleRowSelection(
              this.list.find((item) => {
                return row.staffid == item.staffid
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
        display: block !important;
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
