<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :close-on-click-modal="false"
    :append-to-body="true"
    title="版本选择"
    width="50%"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-right-panel :span="24">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </vab-query-form-right-panel>
      <!-- <vab-query-form-left-panel>
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
      </vab-query-form-left-panel> -->
    </vab-query-form>
    <el-table
      :data="tableData"
      style="width: 100%"
      row-key="fid"
      :tree-props="{ children: 'childrenList' }"
      @select="handleSelection"
      ref="multipleTable"
    >
      <el-table-column
        type="selection"
        width="55"
        :selectable="handleSelectable"
      ></el-table-column>
      <el-table-column prop="handtext" label="版本名称"></el-table-column>
      <el-table-column prop="createdtime" label="创建时间"></el-table-column>
      <el-table-column prop="modifiedtime" label="更新时间"></el-table-column>
    </el-table>
  </el-dialog>
</template>

<script>
  import { getCwbbxxList } from '@/api/cwsc'
  export default {
    data() {
      return {
        dialogVisible: false,
        tableData: [],
        current: [],
        select: [],
      }
    },
    methods: {
      showEdit() {
        this.dialogVisible = true
        this.getVersionList()
      },
      getVersionList() {
        getCwbbxxList({
          page: 1,
          pageSize: 1000,
        }).then((res) => {
          this.tableData = res.data
        })
      },
      close() {
        this.dialogVisible = false
        this.tableData = []
      },
      handleSelectable(row) {
        // 通过是否存在 childrenList 判断是否为一级节点
        // 如果存在子节点（一级节点），则禁用勾选
        return !row.childrenList
      },
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.select = val
      },
      save() {
        if (this.select.length == 0) {
          this.$baseMessage('请选择版本！', 'error', 'vab-hey-message-error')
          return
        }
        this.$emit('versionManage', this.select)
        this.dialogVisible = false
      },
    },
  }
</script>

<style scoped lang="scss">
  ::v-deep .is-disabled {
    display: none !important;
  }

  ::v-deep thead {
    .el-table-column--selection {
      .el-checkbox__inner {
        display: none !important;
      }
    }
  }
</style>
