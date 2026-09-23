<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="600px"
    @close="close"
    v-if="dialogFormVisible"
  >
    <el-table
      ref="multipleTable"
      :data="lists"
      tooltip-effect="dark"
      @selection-change="handleSelection"
      :row-key="getRowKeys"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column align="center" label="名称" prop="columnName">
        <template #default="{ row }">
          <el-input
            v-model="row.columnName"
            size="mini"
            style="width: 90%"
            disabled
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column align="center" label="类型" prop="columnType">
        <template #default="{ row, $index }">
          <el-select
            v-model="row.columnType"
            @change="handleSelect($index, row)"
          >
            <el-option
              key="VARCHAR2(500)"
              label="VARCHAR2(500)"
              value="VARCHAR2(500)"
            ></el-option>
            <el-option key="NUMBER" label="NUMBER" value="NUMBER"></el-option>
            <el-option key="DATE" label="DATE" value="DATE"></el-option>
            <el-option
              key="NUMBER(10,2)"
              label="NUMBER(10,2)"
              value="NUMBER(10,2)"
            ></el-option>
            <el-option
              key="VARCHAR2(4000)"
              label="VARCHAR2(4000)"
              value="VARCHAR2(4000)"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column align="center" label="列名" prop="columnNameNe">
        <template #default="{ row, $index }">
          <el-input
            v-model="row.columnNameNe"
            size="mini"
            style="width: 90%"
            @input="handleInput($index, row)"
          ></el-input>
        </template>
      </el-table-column>
    </el-table>
    <div slot="footer" v-if="footer">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-if="showSaveButton">
        确定
      </el-button>
      <el-button type="primary" v-if="showSuccessButton" @click="submitField">
        生成表
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
  import { getExcelFiledList, editExcelField, makeB } from '@/api/setting/org'
  export default {
    components: {},
    inheritAttrs: false,
    props: [],
    data() {
      return {
        loading: false,
        lists: [],
        footer: true,
        dialogFormVisible: false,
        title: '字段信息',
        list: [],
        queryForm: {
          pageNumber: 1,
          pageSize: 20,
          excelId: '',
          tableNameEn: '',
        },
        selectList: [],
        select: [],
        showSuccessButton: false,
        showSaveButton: true,
      }
    },
    created() {},
    mounted() {},
    methods: {
      /**
       * @description: 外部打开内部dialog
       * @param {*} title 表单类型
       * @param {*} row 编辑、详情带入的数据
       * @return {*}
       */      
      async showEdit(row) {
        this.dialogFormVisible = true
        this.queryForm.excelId = row.excelId
        this.queryForm.tableNameEn = row.tableNameEn
        this.showSaveButton = row.state == 2 || row.state == 0
        this.fetchData(this.queryForm)
      },
      /**
       * @description: 关闭弹窗并清理缓存数据
       * @return {*}
       */      
      close() {
        this.dialogFormVisible = false
        this.footer = true
        this.showSuccessButton = false
        this.showSaveButton = true
      },
      /**
       * @description: 保存表单
       * @return {*}
       */      
      async save() {
        const info = this.selectList.filter(
          (res) =>
            res.hasOwnProperty('columnNameNe') &&
            res.hasOwnProperty('columnType')
        )
        if (info.length < this.selectList.length) {
          this.$message.error('请填写完整信息')
          return
        }
        const res = await editExcelField(this.selectList)
        if (res && res.code == 200) {
          this.$emit('fetchData')
          this.$message({
            message: '提交成功！',
            type: 'success',
          })
          this.showSuccessButton = true
        } else {
          this.$message({
            message: '提交失败',
            type: 'error',
          })
        }
      },

      openModal() {
        this.$refs['modal'].showEdit()
      },
      handleSelection(val) {
        const info = val.map((res) => {
          return { ...res, isSelected: 1 }
        })
        this.selectList = info
        this.select = val.map((item) => item.id)
      },
      async fetchData(info) {
        const res = await getExcelFiledList(info)
        this.lists = res.data.tlist
        const infos = res.data.tlist.filter((res) => res.isSelected == 1)
        this.selectList = infos
        this.$nextTick(() => {
          this.setCheckedRows(infos)
        })
      },
      handleInput(a, b) {
        //a是索引
        this.lists[a] = b
        // 同步更新selectList中对应的数据
        const selectedIndex = this.selectList.findIndex(item => item.id === b.id)
        if (selectedIndex !== -1) {
          this.selectList[selectedIndex] = { ...this.selectList[selectedIndex], ...b }
        }
      },
      handleSelect(a, b) {
        //a是索引
        this.lists[a] = b
        // 同步更新selectList中对应的数据
        const selectedIndex = this.selectList.findIndex(item => item.id === b.id)
        if (selectedIndex !== -1) {
          this.selectList[selectedIndex] = { ...this.selectList[selectedIndex], ...b }
        }
      },
      // 2、设置row-key
      getRowKeys(row) {
        return row.id
      },
      // 4、回显已勾选的数据
      setCheckedRows(info) {
        info.forEach((res) => {
          this.$refs.multipleTable.toggleRowSelection(res, true)
        })
      },
      submitField() {
        makeB({
          excelId: this.queryForm.excelId,
          tableNameEn: this.queryForm.tableNameEn,
        }).then((res) => {
          if (res.code == 200) {
            this.$message({
              message: '生成成功！',
              type: 'success',
            })
            this.$emit('fetchData')
            this.close()
          } else {
            this.$message({
              message: '生成失败！',
              type: 'error',
            })
          }
        })
      },
    },
  }
</script>
<style scoped>
  .el-form-item__content span {
    font-size: 14px;
    font-weight: 500;
    color: darkgray;
  }
</style>
