<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
      <el-form-item label="系统版本" prop="fid">
        <el-cascader
          ref="cascader"
          v-model="formData.fid"
          :options="versionOptions"
          :props="{ expandTrigger: 'hover' }"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="数据库类型" prop="sourceDbType">
        <el-radio-group v-model="formData.sourceDbType" size="medium">
          <el-radio
            v-for="(item, index) in sourceDbTypeOptions"
            :key="index"
            :disabled="item.disabled"
            :label="item.value"
          >
            {{ item.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="数据库连接" prop="sourceConn">
        <el-input
          v-model="formData.sourceConn"
          clearable
          placeholder="请输入数据库连接"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="数据库用户" prop="sourceUserId">
        <el-input
          v-model="formData.sourceUserId"
          clearable
          placeholder="请输入数据库用户"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="数据库密码" prop="sourcePassWord">
        <el-input
          v-model="formData.sourcePassWord"
          class="input-psword"
          clearable
          placeholder="请输入数据库密码"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="采集时间" required>
        <el-date-picker
          v-model="formData.startdate"
          class="width-130"
          :default-value="defaultValue"
          format="yyyy"
          :picker-options="startPickerOptions"
          :style="{ width: '40%' }"
          type="year"
          value-format="yyyy"
        />
        至
        <el-date-picker
          v-model="formData.enddate"
          class="width-130"
          :default-value="defaultValue"
          format="yyyy"
          :picker-options="endPickerOptions"
          :style="{ width: '40%' }"
          type="year"
          value-format="yyyy"
        />
      </el-form-item>
      <el-form-item label="公司ID" prop="companyId">
        <el-input
          v-model="formData.companyId"
          clearable
          placeholder="请输入公司ID"
          :style="{ width: '100%' }"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { addConfig, getDbVersion } from '@/api/setting/dataGather'

  export default {
    name: 'SjpzEdit',
    data: function () {
      return {
        defaultValue: '',
        startPickerOptions: '',
        endPickerOptions: '',
        formData: {
          orderId: undefined,
          fid: undefined,
          sourceDbType: undefined,
          sourceConn: undefined,
          sourceUserId: undefined,
          sourcePassWord: undefined,
          startdate: '2021',
          enddate: '2025',
          companyId: undefined,
        },
        rules: {
          fid: [
            {
              required: true,
              message: '请选择系统版本',
              trigger: 'blur',
            },
          ],
          sourceDbType: [
            {
              required: true,
              message: '数据库类型不能为空',
              trigger: 'change',
            },
          ],
          sourceConn: [
            {
              required: true,
              message: '请输入数据库连接',
              trigger: 'blur',
            },
          ],
          sourceUserId: [
            {
              required: true,
              message: '请输入数据库用户',
              trigger: 'blur',
            },
          ],
          sourcePassWord: [
            {
              required: true,
              message: '请输入数据库密码',
              trigger: 'blur',
            },
          ],
          startdate: [
            {
              required: true,
              message: '采集开始时间不能为空',
              trigger: 'change',
            },
          ],
          enddate: [
            {
              required: true,
              message: '采集结束时间不能为空',
              trigger: 'change',
            },
          ],
        },
        sourceDbTypeOptions: [
          {
            label: 'Oracle',
            value: 'Oracle',
          },
          {
            label: 'Mysql',
            value: 'Mysql',
          },
          {
            label: 'SqlServer',
            value: 'SqlServer',
          },
        ],
        versionOptions: [],
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        this.getDbVersion()
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
        }
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.dialogFormVisible = false
      },
      async getDbVersion() {
        const res = await getDbVersion()
        const keyMap = {
          text: 'label',
          id: 'value',
        }
        function convertKey(arr) {
          for (let i = 0; i < arr.length; i++) {
            let obj = arr[i]
            for (let key in obj) {
              if (key === 'children' && obj[key].length) {
                convertKey(obj[key])
              } else if (key === 'text' || key === 'id') {
                let newKey = keyMap[key]
                obj[newKey] = obj[key]
                delete obj[key]
              }
            }
          }
          return arr
        }
        this.versionOptions = convertKey(res[0].children)
      },
      async save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const data = Object.assign({}, this.formData)
            const { startdate, enddate } = data
            data.parseStart = startdate
            data.pendDate = enddate
            delete data.startdate
            delete data.enddate
            data.fid = data.fid[data.fid.length - 1]
            const { code, msg } = await addConfig(data)
            if (code == 1) {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              this.$emit('fetch-data')
              this.close()
            }
          }
        })
      },
    },
  }
</script>
<style>
  .input-psword {
    -webkit-text-security: disc;
  }
</style>
