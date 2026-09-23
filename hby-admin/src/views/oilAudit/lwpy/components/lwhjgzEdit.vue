<template>
  <div>
    <el-dialog :close-on-click-modal="false" :append-to-body="true" :title="title" :visible.sync="dialogFormVisible"
      width="1000px" @close="close">
      <el-row :gutter="24" v-loading="loading">
        <el-form ref="ruleForm" label-width="135px" :model="formData" :rules="rules" size="mini">
          <el-col :span="12">
            <el-form-item label="年度" prop="ruleyear">
              <el-date-picker v-model="formData.ruleyear" placeholder="年度" style="width: 100%" type="year" format="yyyy"
                :disabled='disabled' value-format="yyyy" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="填报单位" prop="tbrgname">
              <el-input v-model="formData.tbrgname" disabled placeholder="请输入填报单位" :style="{ width: '80%' }" />
              <el-button @click="handleObject" style="margin-left: 10px" type="primary" :disabled='disabled'>
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>规则</el-divider>
          </el-col>
          <el-col :span="24">
            <el-table :data="tableData" :border="false" style="width: 100%; margin-top: 15px">
              <el-table-column label="获奖等级" prop="hjtype" align="center" />
              <el-table-column label="获奖比例" prop="hjbl" align="center">
                <template slot-scope="scope">
                <el-input v-model="scope.row.hjbl" size="mini" style="width: 90%" type="number" :disabled='disabled'
                  @input="updatePercentage(scope.$index, scope.row.hjbl)" :max="remainingPercentage(scope.$index)" min="0" />
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <SelectDepartment ref="audiTree" @submit="getDepartmentInfo" />
      <template #footer v-if="!disabled">
        <el-button @click="close">关 闭</el-button>
        <el-button :loading="loading" type="primary" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { saveLwhjgzData, isAddYear, getLwhjgzData } from '@/oapi/audit/lwpy'
import { getDetail, getInfoList } from '@/oapi/fwgl/pfgl/pfbgl'
import { getList } from '@/api/fwgl/pfgl/pfzdgl'
import SelectDepartment from '@/views/oilAudit/jhlx/components/department.vue'

export default {
  name: 'lwhjgzEdit',
  components: { SelectDepartment },
  data() {
    return {
      title: '新增',
      loading: false,
      dialogFormVisible: false,
      formData: {
        ruleyear: '',
        tbrgname: '',
        tbrgid: ''
      },
      tableData: [
        { hjtype: '一等奖', hjbl: 0 },
        { hjtype: '二等奖', hjbl: 0 },
        { hjtype: '三等奖', hjbl: 0 }
      ],
      disabled: false,
      rules: {
        ruleyear: [{ required: true, message: '请选择年度', trigger: 'change', }],
        tbrgname: [{ required: true, message: '请选择填报单位', trigger: 'change', }],
      },
      year: ''
    }
  },
  computed: {
    totalPercentage() {  
      return this.tableData.reduce((acc, item) => acc + item.hjbl, 0);  
    },
  },
  created() { },
  methods: {
    updatePercentage(index, newValue) {  
      const newValueNum = parseInt(newValue, 10);  
      if (isNaN(newValueNum)) {
        return;  
      }  
      this.$set(this.tableData, index, { ...this.tableData[index], hjbl: newValueNum });    
      if (this.totalPercentage > 100) {
        const diff = this.totalPercentage - 100;  
        if (newValueNum > diff) { 
          this.$set(this.tableData, index, { ...this.tableData[index], hjbl: newValueNum - diff });  
        } else {
          this.$set(this.tableData, index, { ...this.tableData[index], hjbl: 0 });
        }  
      }  
    },  
    remainingPercentage(index) { 
      let sum = 0;  
      this.tableData.forEach((item, idx) => {  
        if (idx !== index) {  
          sum += item.hjbl;  
        }  
      });  
      return Math.max(0, 100 - sum);  
    }, 
    handleSave() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (!valid) {
          return
        }
        else if (this.tableData.some((s) => !s.hjbl))
          return this.$notify({
            title: '提示',
            message: '请设置获奖比例！',
            type: 'error',
            duration: 1500,
          })


        if(this.title == '新增') {
          let isyear = await isAddYear({ year: this.formData.ruleyear })
          if (isyear.code == 1){
            this.submitData()
          }
        }else{
          if(this.year != this.formData.ruleyear) {
            let isyear = await isAddYear({ year: this.formData.ruleyear })
            if (isyear.code == 1){
              this.submitData()
            }
          }else{
            this.submitData()
          }
        }
      })
    },
    async submitData() {
      let params = {
        ruleyear: this.formData.ruleyear,
        tbrgid: this.formData.tbrgid,
        tbrgname: this.formData.tbrgname,
        tbid: this.formData.tbid,
        gljson: JSON.stringify(this.tableData)
      }
      saveLwhjgzData(params).then((res) => {
        this.$emit('fetch')
        this.close()
      })
    },
    async showEdit(title, row) {
      this.dialogFormVisible = true

      if (title == 'edit') {
        this.title = '编辑'
        this.disabled = false
      } else if (title == 'detail') {
        this.title = '详细'
        this.disabled = true
      } else {
        this.title = '新增'
        this.disabled = false
        this.formData = {
          ...this.formData
        }
      }

      if (row) {
        let res = await getLwhjgzData({ tbid: row.tbid })
        this.formData = res.data.data
        this.tableData = res.data.data.list
        this.year = res.data.data.ruleyear
      }
    },
    /**
     * @description: 关闭弹窗并清理缓存数据
     * @return {*}
     */
    close() {
      this.formData = {
        ruleyear: '',
        tbrgname: '',
        tbrgid: ''
      },
        this.tableData = [
          { hjtype: '一等奖', hjbl: '' },
          { hjtype: '二等奖', hjbl: '' },
          { hjtype: '三等奖', hjbl: '' }
        ]
      this.dialogFormVisible = false
      this.$refs["ruleForm"].resetFields();
    },
    /**
     * @description: 填报单位
     * @param {*}
     * @return {*}
     */
    handleObject() {
      this.$refs['audiTree'].showEdit()
    },
    getDepartmentInfo(val) {
      this.formData.tbrgname = val.name
      this.formData.tbrgid = val.id
    },
  },
}
</script>
