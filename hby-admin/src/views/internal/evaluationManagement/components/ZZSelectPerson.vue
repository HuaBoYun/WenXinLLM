<template>
  <el-dialog
    :title="title"
    :visible.sync="dialogVisible"
    width="1400px"
    :modal-append-to-body="false"
    :append-to-body="true"
    :close-on-click-modal="false"
    v-if="dialogVisible"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="left">
          <el-tree
            ref="tree"
            :check-strictly="true"
            :data="dataTree"
            default-expand-all
            :expand-on-click-node="false"
            highlight-current
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          />
        </div>
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table
            ref="multipleTable"
            :data="list"
            :row-key="getRowKeys"
            @select="handleSelection"
          >
            <el-table-column
              type="selection"
              width="55"
              :reserve-selection="true"
            ></el-table-column>
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
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
  import { ZZUserList, saveZPRInfo, leftTree } from '@/api/internal/project'
  import { selectPerson } from '@/api/audit/project'
  import { zgjkLeft } from '@/api/setting/org'
  export default {
    data() {
      return {
        orgId: '',
        dialogVisible: false,
        dataTree: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        defaultProps: {
          children: 'children',
          label: 'name',
          value: 'id',
        },
        queryForm: {
          nodeId: '198328',
          pageNumber: 1,
          pageSize: 20,
        },
        current: undefined,
        multipleSelection: [],
        listLoading: false,
        list: [],
        zids: undefined, //用于参评人保存
        select: [],
      }
    },
    methods: {
      showEdit(row) {
        this.dataTree = []
        if (row.name === '主评人') {
          this.title = '主评人选择'
        }
        if (row.name === '参评人') {
          this.title = '参评人选择'
        }
        this.dialogVisible = true
        this.current = undefined
        this.zids = row.z
        this.orgId = row.orgId
        this.secrectLevelId = row.secrectLevelId
        this.queryForm.nodeId = this.orgId
        this.getExecutorTree()
        this.getExecutorList()
      },
      handleNodeClick(val) {
        this.queryForm.nodeId = val.id
        this.getExecutorList()
      },
      save() {
        if (!this.select) {
          this.$baseMessage(
            '请选择评价负责人',
            'error',
            'vab-hey-message-error'
          )
          return
        }

        const _select = this.select.filter((x) => !!x).join(',')
        const _zids = this.zids
          .split(',')
          .filter((x) => !!x)
          .join(',')

        saveZPRInfo({
          userid: _select,
          z: _zids,
        }).then((res) => {
          if (res.code == 200) {
            this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
            this.$emit('fetchData')
            this.dialogVisible = false
          }
        })
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val.map((item) => item.staffid)
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.getExecutorList()
      },
      async getExecutorTree() {
        // const res = await zgjkLeft(this.queryForm)
        // this.dataTree = res
        const res = await leftTree({ z: this.zids })
        this.dataTree = res.data.tree
      },
      async getExecutorList() {
        this.listLoading = true
        const {
          data: {
            pageBean: { list, total },
          },
        } = await ZZUserList({
          ...this.queryForm,
          z: this.zids,
          secrectLevelId: this.secrectLevelId,
        })
        this.list = list
        this.total = total
        this.listLoading = false
        this.setCheckedRows()
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.getExecutorList()
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.staffid
      },
      // 4、回显已勾选的数据
      setCheckedRows() {
        let selectItem = []
        this.list.forEach((item) => {
          this.select.forEach((id) => {
            if (item.staffid === id) {
              selectItem.push(item)
            }
          })
        })
        // console.log(selectItem, 'selectItem')
        this.$refs.multipleTable.toggleRowSelection(selectItem)
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
