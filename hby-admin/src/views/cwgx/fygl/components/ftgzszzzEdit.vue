<template>
  <el-dialog
    title="分摊规则设置 - 组织"
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

      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          编码
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.code" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          名称
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.name" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          分摊对象
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.allocationObject"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          分摊方式
        </template>
        <template slot-scope="scope">
          <el-select v-model="scope.row.allocationWay" placeholder="请选择">
            <el-option
              v-for="item in WayOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            ></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="分摊比例">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.amortizationRatio"
            autocomplete="off"
          ></el-input>
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
        WayOptions: [
          {
            value: '1',
            label: '平均分摊',
          },
          {
            value: '2',
            label: '按比例分摊',
          },
          {
            value: '3',
            label: '按金额分摊',
          },
        ],
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
          code: '',
          name: '',
          allocationObject: '',
          allocationWay: '',
          amortizationRatio: '',
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
          if (!this.tableData[i].name) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的名称未填写`)
            break
          }
          if (!this.tableData[i].allocationObject) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的分摊对象未填写`)
            break
          }
          if (!this.tableData[i].allocationWay) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的分摊方式未填写`)
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
