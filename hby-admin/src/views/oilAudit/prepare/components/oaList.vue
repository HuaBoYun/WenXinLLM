<!--
 * @Date: 2022-03-28 15:06:04
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-21 16:55:07
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/options/setupInfo.vue
-->
<template>
  <el-dialog
    append-to-body
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <vab-query-form>
      <vab-query-form-left-panel :span="18">
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-select
              v-model="queryForm.type"
              filterable
              placeholder="类型"
              style="width: 100%"
              @change="changeData"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'edoc'">
            <el-select
              v-model="queryForm.appType"
              filterable
              placeholder="类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in appTypeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item v-if="queryForm.type == 'col'">
            <el-select
              v-model="queryForm.state"
              filterable
              placeholder="类型"
              style="width: 100%"
            >
              <el-option
                v-for="item in appTypeOptions1"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              type="text"
              v-model="queryForm.textfield"
              placeholder="标题"
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
            <el-button native-type="submit" type="primary" @click="resetSearch">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="6">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      ref="multipleTable"
      v-loading="listLoading"
      :data="list"
      highlight-current-row
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="标题" prop="subject" />
      <el-table-column align="center" label="发送人" prop="sendName" />
      <el-table-column align="center" label="状态" prop="stateName">
        <template #default="{ row }">
          {{
            row.stateName == '' && queryForm.type == 'col'
              ? appTypeOptions1[renderState - 2].label
              : row.stateName
          }}
        </template>
      </el-table-column>
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
  </el-dialog>
</template>
<script>
  import { getOAList } from '@/oapi/contract/manage'
  // import { contractProList } from '@/oapi/contract/project'
  import { formatDate } from '@/utils/index'
  export default {
    name: 'BankOptions',
    data() {
      return {
        listLoading: false,
        list: [],
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        title: '',
        dialogFormVisible: false,
        queryForm: {
          type: 'edoc',
          appType: '19',
          state: undefined,
          pageNumber: 1,
          pageSize: 10,
          textfield: undefined,
        },
        current: undefined,
        typeOptions: [
          {
            label: '公文',
            value: 'edoc',
          },
          {
            label: '协同',
            value: 'col',
          },
        ],
        appTypeOptions: [
          {
            label: '发文',
            value: 19,
          },
          {
            label: '收文',
            value: 20,
          },
          {
            label: '签报',
            value: 21,
          },
        ],
        appTypeOptions1: [
          {
            label: '已发',
            value: 2,
          },
          {
            label: '待办',
            value: 3,
          },
          {
            label: '已办',
            value: 4,
          },
        ],
        multipleSelection: [],
        contractid: undefined,
      }
    },
    created() {},
    methods: {
      handleSelectionChange(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      formatDate(row, column) {
        // 获取单元格数据
        let data = row[column.property]
        return formatDate(data)
      },
      resetQueryForm() {
        this.queryForm = {
          type: 'edoc',
          appType: 19,
          state: undefined,
          pageNumber: 1,
          pageSize: 20,
          textfield: undefined,
        }
      },
      show() {
        this.resetQueryForm()
        this.current = undefined
        this.dialogFormVisible = true
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { data: tlist, total },
        } = await getOAList(this.queryForm)
        this.list = tlist
        this.total = total
        this.listLoading = false
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      handleSelected(val) {
        this.current = val
        // this.$emit('selected', val)
        // this.dialogFormVisible = false
      },
      close() {
        this.dialogFormVisible = false
      },
      async confirm() {
        if (this.multipleSelection.length == 0) {
          this.$baseMessage('请选择OA信息！', 'error', 'vab-hey-message-error')
          return
        }
        this.$baseMessage('保存成功！', 'success', 'vab-hey-message-success')
        this.$emit('selected', this.multipleSelection)
        this.dialogFormVisible = false
      },
      resetSearch() {
        this.resetQueryForm()
        this.fetchData()
      },
      changeData() {
        this.queryForm.state = undefined
        this.queryForm.appType = undefined
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
