<!--
 * @Date: 2022-04-20 13:29:00
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-20 13:31:18
 * @FilePath: /hb-admin/src/views/contract/opposite/components/MonitoringDetail.vue
-->
<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      append-to-body
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      v-if="dialogFormVisible"
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
              <el-input
                v-model="queryForm.word"
                clearable
                placeholder="公司名称"
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
        </vab-query-form-left-panel>
        <vab-query-form-right-panel :span="24">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </vab-query-form-right-panel>
      </vab-query-form>
      <el-table
        ref="multipleTable"
        :data="list"
        tooltip-effect="dark"
        @select="handleSelection"
        style="width: 100%"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="selectable"
        ></el-table-column>
        <el-table-column label="公司名称" prop="name"></el-table-column>
        <el-table-column
          label="法定代表人"
          prop="legalPersonName"
          align="center"
        ></el-table-column>
        <el-table-column
          label="省份"
          prop="base"
          align="center"
        ></el-table-column>
        <el-table-column
          label="开业日期"
          prop="estiblishTime"
          align="center"
        ></el-table-column>
        <el-table-column
          label="注册资本"
          prop="regCapital"
          align="center"
        ></el-table-column>
        <el-table-column
          label="状态"
          prop="regStatus"
          align="center"
        ></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
  import { getXDFtableData } from '@/api/contract/opposite'
  export default {
    name: 'MonitoringEdit',
    components: {},
    props: {},
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        queryForm: {
          word: '',
        },
        list: [],
        tableData: [],
        multipleSelection: [],
        current: [],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      handleSelection(val) {
        this.current = val
        if (val.length > 1) {
          let del = val.shift()
          this.$refs.multipleTable.toggleRowSelection(del, false)
        }
        this.multipleSelection = val
      },
      /**
       * @description: 数据请求
       * @return {*}
       */
      async fetchData() {
        const res = await getXDFtableData(this.queryForm)
        this.list = res.data.list
      },
      showEdit() {
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
        this.multipleSelection = []
        this.current = []
        this.queryForm = {
          word: '',
        }
      },
      resetQueryForm() {
        this.queryForm = {
          word: '',
        }
        this.fetchData()
      },
      resetSearch() {
        this.resetQueryForm()
      },
      save() {
        if (this.current.length == 0) {
          this.$baseMessage('请选择公司！', 'error', 'vab-hey-message-error')
          return
        }
        // if (this.multipleSelection[0].regStatus !== '存续') {
        //   this.$baseMessage(
        //     '请选择状态为存续的公司！',
        //     'error',
        //     'vab-hey-message-error'
        //   )
        //   return
        // }
        this.$emit('selectXDF', this.multipleSelection)
        this.dialogFormVisible = false
      },
      //处理启用禁用
      selectable(row) {
        if (
          row.regStatus == '存续' ||
          row.regStatus == '正常' ||
          row.regStatus == '在业' ||
          row.regStatus == ''
        ) {
          // 自己规定
          return true //不禁用
        } else {
          return false //禁用
        }
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
