<template>
  <el-dialog title="商户号" :visible.sync="dialogFormVisible" width="1000px">
    <div class="dialog-add">
      <el-button type="primary" @click="deleteSelection">删除</el-button>
      <el-button type="primary" @click="addRow">新增</el-button>
      <el-button type="primary">复制</el-button>
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
      <el-table-column label="集团">
        <template slot-scope="scope">
          <el-input v-model="scope.row.groupName" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          财务组织
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.financialOrganization"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          商户号
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.commercialNumber"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          渠道号
        </template>
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.channelNumber"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column>
        <template slot="header">
          <span class="required-star">*</span>
          用户名
        </template>
        <template slot-scope="scope">
          <el-input v-model="scope.row.userName" autocomplete="off"></el-input>
        </template>
      </el-table-column>
      <el-table-column label="创建人">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.createName"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="创建时间">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.createDate"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="修改人">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.updateName"
            autocomplete="off"
          ></el-input>
        </template>
      </el-table-column>
      <el-table-column label="修改时间">
        <template slot-scope="scope">
          <el-input
            v-model="scope.row.updateDate"
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
          groupName: '',
          financialOrganization: '',
          commercialNumber: '',
          channelNumber: '',
          userName: '',
          createName: '',
          createDate: '',
          updateName: '',
          updateDate: '',
        })
      },
      save() {
        this.isValid = true
        for (let i = 0; i < this.tableData.length; i++) {
          if (!this.tableData[i].financialOrganization) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的财务组织未填写`)
            break
          }
          if (!this.tableData[i].commercialNumber) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的商户号未填写`)
            break
          }
          if (!this.tableData[i].channelNumber) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的渠道号未填写`)
            break
          }
          if (!this.tableData[i].userName) {
            this.isValid = false
            this.$message.error(`第${i + 1}行的用户名未填写`)
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
