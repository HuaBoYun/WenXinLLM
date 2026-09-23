<template>
  <el-dialog
    title="个人授权设置"
    :visible.sync="dialogFormVisible"
    width="1000px"
  >
    <div class="dialog-add">
      <el-button type="primary" @click="deleteSelection">删除</el-button>
      <el-button type="primary" @click="addRow">新增</el-button>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
        align="center"
      ></el-table-column>

      <el-table-column width="150">
        <template slot="header">
          <span class="required-star">*</span>
          授权的操作员
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.operator" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="所属组织" width="150">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.organization"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="240">
        <template slot="header">
          <span class="required-star">*</span>
          开始日期
        </template>
        <template slot-scope="scope">
          <el-date-picker
            v-model="scope.row.startDate"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </template>
      </el-table-column>
      <el-table-column width="240">
        <template slot="header">
          <span class="required-star">*</span>
          结束日期
        </template>
        <template slot-scope="scope">
          <el-date-picker
            v-model="scope.row.endDate"
            type="date"
            placeholder="选择日期"
          ></el-date-picker>
        </template>
      </el-table-column>
      <el-table-column width="150">
        <template slot="header">
          <span class="required-star">*</span>
          交易类型
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.dealType" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template slot-scope="scope">
          <el-button
            @click="handleClickDelete(scope.$index, scope.row)"
            type="text"
            size="small"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: 'sxlbEdit',
    components: {},
    data() {
      return {
        dialogFormVisible: false,
        tableData: [],
        isValid: false,
        selectionLength: [],
      }
    },
    created() {},

    methods: {
      showEdit() {
        this.dialogFormVisible = true
      },
      addRow() {
        this.tableData.push({
          id: new Date().getTime(),
          operator: '',
          organization: '',
          startDate: '',
          endDate: '',
          dealType: '',
        })
      },
      save() {
        this.isValid = true
        for (let i = 0; i < this.tableData.length; i++) {
          if (!this.tableData[i].operator) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的授权的操作员未填写`)
            break
          }
          if (!this.tableData[i].startDate) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的开始日期未填写`)
            break
          }
          if (!this.tableData[i].endDate) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的结束日期未填写`)
            break
          }
          if (!this.tableData[i].dealType) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的交易类型未填写`)
            break
          }
        }
        if (this.isValid) {
          console.log('[ this.tableData ] >', this.tableData)
        }
      },
      handleClickDelete(index, row) {
        this.$confirm('此操作将永久删除该条目, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(() => {
            this.tableData.splice(index, 1)
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除',
            })
          })
      },
      handleSelectionChange(val) {
        console.log('[ val ] >', val)
        this.selectionLength = val
      },
      deleteSelection() {
        if (this.selectionLength.length > 0) {
          console.log('[ qq ] >', qq)
        } else {
          this.$message.error('请选择要删除的条目')
        }
      },
    },
  }
</script>
<style scoped>
  .dialog-add {
    width: 100%;
    display: flex;
    justify-content: flex-end;
    margin-bottom: 20px;
  }
  .required-star {
    color: red;
  }
</style>
