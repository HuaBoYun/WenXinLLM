<template>
  <el-dialog title="还款方式" :visible.sync="dialogFormVisible" width="1000px">
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
      <el-table-column width="100">
        <template slot="header">
          <span class="required-star">*</span>
          编码
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.code" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column width="180">
        <template slot="header">
          <span class="required-star">*</span>
          还款方式名称
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.repaymentName"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          还本方式
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.repaymentType"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          还本日期
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.repaymentDate"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="180">
        <template slot="header">
          <span class="required-star">*</span>
          还本开始月
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.repaymentStartMonth"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          还本周期
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.repaymentWeek"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          付息方式
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.paymentType"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          付息周期
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.paymentDate"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="180">
        <template slot="header">
          <span class="required-star">*</span>
          付息开始月
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.paymentStartMonth"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          付息周期
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.paymentWeek"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column width="120">
        <template slot="header">
          <span class="required-star">*</span>
          系统标识
        </template>
        <template slot-scope="scope">
          <el-checkbox-group v-model="scope.row.system">
            <el-checkbox label="美食/餐厅线上活动" name="type"></el-checkbox>
          </el-checkbox-group>
        </template>
      </el-table-column>
      <el-table-column label="创建人" width="110">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.createName"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="120">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.createDate"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="最后修改人" width="180">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.updateName"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="最后修改时间" width="190">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.updateDate"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" width="120">
        <template slot-scope="scope">
          <el-input v-model="scope.row.status" autocomplete="off"></el-input>
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
    name: 'hkfsEdit',
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
      handleOpenHkfsDialog() {
        this.dialogFormVisible = true
      },
      addRow() {
        this.tableData.push({
          id: new Date().getTime(),
          code: '',
          repaymentName: '',
          repaymentType: '',
          repaymentDate: '',
          repaymentStartMonth: '',
          repaymentWeek: '',
          paymentType: '',
          paymentDate: '',
          paymentStartMonth: '',
          paymentWeek: '',
          system: '',
          createName: '',
          createDate: '',
          updateName: '',
          updateDate: '',
          status: '',
        })
      },
      save() {
        this.isValid = true
        for (let i = 0; i < this.tableData.length; i++) {
          if (!this.tableData[i].code) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的编码未填写`)
            break
          }
          if (!this.tableData[i].repaymentName) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的还款方式名称未填写`)
            break
          }
          if (!this.tableData[i].repaymentType) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的还本方式未填写`)
            break
          }
          if (!this.tableData[i].repaymentDate) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的还本日期未填写`)
            break
          }
          if (!this.tableData[i].repaymentStartMonth) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的还本开始月未填写`)
            break
          }
          if (!this.tableData[i].repaymentWeek) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的还本周期未填写`)
            break
          }
          if (!this.tableData[i].paymentType) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的付息方式未填写`)
            break
          }
          if (!this.tableData[i].paymentDate) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的付息周期未填写`)
            break
          }
          if (!this.tableData[i].paymentStartMonth) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的付息开始月未填写`)
            break
          }
          if (!this.tableData[i].paymentWeek) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的付息周期未填写`)
            break
          }
          if (!this.tableData[i].system) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的系统标识未填写`)
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
