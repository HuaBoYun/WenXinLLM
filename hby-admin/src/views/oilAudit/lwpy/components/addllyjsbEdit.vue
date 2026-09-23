<template>
  <!-- 理论研究上报 -->
  <el-dialog :close-on-click-modal="false" :append-to-body="true" :title="title" :visible.sync="dialogFormVisible"
    width="1000px" @close="close" v-if="dialogFormVisible">
    <el-row :gutter="14">
      <el-form ref="ruleForm" label-width="135px" :model="formData" :rules="rules" size="mini" :disabled="formDisabled">
        <el-col :span="12">
          <el-form-item label="研究方向" prop="direction">
            <el-input v-model="formData.direction" :style="{ width: '100%' }" clearable placeholder="请输入研究方向" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="组长" prop="zzname">
            <el-input v-model="formData.zzname" :style="{ width: '80%' }" clearable placeholder="组长" disabled />
            <el-button @click="openPerson('zzname')" style="margin-left: 10px; height: 30px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="研究人员" prop="yjname">
            <el-input v-model="formData.yjname" :style="{ width: '80%' }" clearable placeholder="研究人员" disabled />
            <el-button @click="openPerson('yjname')" style="margin-left: 10px; height: 30px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="撰写人" prop="zxrname">
            <el-input v-model="formData.zxrname" :style="{ width: '80%' }" clearable placeholder="撰写人" disabled />
            <el-button @click="openPerson('zxrname')" style="margin-left: 10px; height: 30px" type="primary">
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remarks">
            <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="formData.remarks"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <!-- 选择人员 -->
    <projectManage ref="manage" @projectManage="selectPerson" :multiple="selectPersonType" />
    <div slot="footer" v-if="!formDisabled">
      <el-button @click="close">取消</el-button>
      <el-button @click="save" type="primary" v-loading="loading">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveLlyjsbzbData, getLlyjsbzbData } from '@/oapi/audit/lwpy'
import { formatDay } from '@/utils/index'
import projectManage from '@/components/selectPerson.vue'

export default {
  name: 'addllyjsbEdit',
  components: { projectManage },
  inheritAttrs: false,
  data() {
    const validator = (_rule, value, callback) => {
      if (value !== '' && isNaN(value)) {
        callback(new Error('请输入数字值'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      tableData: [],
      formDisabled: false,
      formData: {
        direction: '',
        zzname: '',
        zzstaffid: '',
        yjname: '',
        yjstaffid: '',
        zxrname: '',
        zxrstaffid: '',
        remarks: '',
      },
      rules: {
        direction: [
          {
            required: true,
            message: '请输入研究方向',
            trigger: 'blur',
          },
        ],
        zzname: [
          {
            required: true,
            message: '请选择组长',
            trigger: 'change',
          },
        ],
        yjname: [
          {
            required: true,
            message: '请选择研究人员',
            trigger: 'change',
          },
        ],
        zxrname: [
          {
            required: true,
            message: '请选择撰写人',
            trigger: 'change',
          },
        ],
      },
      dialogFormVisible: false,
      title: '新增',
      disabled: false,
      personType: '',
      selectPersonType: false
    }
  },
  methods: {
    async showEdit(row, title) {
      this.dialogFormVisible = true
      this.formDisabled = title == '详情'
      this.title = title
      if (row) {
        this.formData.dataIndex = row.index
        const res = await getLlyjsbzbData({ chid: row.id })
        Object.assign(this.formData, res.data.data)
      }
    },
    close() {
      this.formData = {
        direction: '',
        zzname: '',
        zzstaffid: '',
        yjname: '',
        yjstaffid: '',
        zxrname: '',
        zxrstaffid: '',
        remarks: '',
      }
      this.dialogFormVisible = false
      this.$refs["ruleForm"].resetFields();
    },
    async save() {
      this.$refs['ruleForm'].validate(async (valid) => {
        if (valid) {
          this.loading = true
          const params = JSON.parse(JSON.stringify(this.formData))
          const res = await saveLlyjsbzbData(params)
          this.loading = false
          if (res && res.code === 1) {
            this.$emit('fetchData', {
              data: [res.data.data],
              index: this.formData.dataIndex,
            })
            this.close()
            this.$message({
              message: '保存成功！',
              type: 'success',
            })
          } else {
            this.$message({
              message: '保存失败',
              type: 'error',
            })
          }
        }
      })
    },
    openPerson(type) {
      this.personType = type
      if (type == 'yjname') {
        this.selectPersonType = true
      } else {
        this.selectPersonType = false
      }
      this.$refs.manage.showEdit()
    },
    selectPerson(val) {
      if (this.personType == 'zzname') {
        this.formData.zzstaffid = val[0].staffid
        this.formData.zzname = val[0].realname
      } else if (this.personType == 'yjname') {
        this.formData.yjstaffid = val.map(item => item.staffid).join(',')
        this.formData.yjname = val.map(item => item.realname).join(',')
      } else {
        this.formData.zxrstaffid = val[0].staffid
        this.formData.zxrname = val[0].realname
      }
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
