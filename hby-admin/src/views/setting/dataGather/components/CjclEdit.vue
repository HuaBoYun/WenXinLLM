<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="500px"
    @close="close"
  >
    <el-form ref="form" label-width="80px" :model="formData" :rules="rules">
      <el-form-item label="策略名称" prop="infoName">
        <el-input
          v-model="formData.infoName"
          clearable
          placeholder="请输入策略名称"
          :style="{ width: '100%' }"
        />
      </el-form-item>
      <el-form-item label="采集频率" prop="acWeek">
        <el-checkbox-group v-model="formData.acWeek" size="medium">
          <el-checkbox
            v-for="(item, index) in acWeekOptions"
            :key="index"
            :disabled="item.disabled"
            :label="item.value"
          >
            {{ item.label }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="采集时间" prop="acHour">
        <el-checkbox-group v-model="formData.acHour" size="medium">
          <el-checkbox
            v-for="(item, index) in acHourOptions"
            :key="index"
            :disabled="item.disabled"
            :label="item.value"
          >
            {{ item.label }}
          </el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveStrategy, updateStrategy } from '@/api/setting/dataGather'

  export default {
    name: 'CjclEdit',
    data: function () {
      return {
        formData: {
          infoId: undefined,
          infoName: undefined,
          acWeek: [],
          acHour: [],
        },
        rules: {
          infoName: [
            {
              required: true,
              message: '请输入策略名称',
              trigger: 'blur',
            },
          ],
          acWeek: [
            {
              required: true,
              type: 'array',
              message: '请至少选择一个采集频率',
              trigger: 'change',
            },
          ],
          acHour: [
            {
              required: true,
              type: 'array',
              message: '请至少选择一个采集时间',
              trigger: 'change',
            },
          ],
        },
        acWeekOptions: [
          {
            label: '周一',
            value: 1,
          },
          {
            label: '周二',
            value: 2,
          },
          {
            label: '周三',
            value: 3,
          },
          {
            label: '周四',
            value: 4,
          },
          {
            label: '周五',
            value: 5,
          },
          {
            label: '周六',
            value: 6,
          },
          {
            label: '周日',
            value: 7,
          },
          {
            label: '全选',
            value: '',
          },
        ],
        acHourOptions: [
          {
            label: '00:00',
            value: 0,
          },
          {
            label: '02:00',
            value: 2,
          },
          {
            label: '04:00',
            value: 4,
          },
          {
            label: '06:00',
            value: 6,
          },
          {
            label: '08:00',
            value: 8,
          },
          {
            label: '12:00',
            value: 12,
          },
          {
            label: '14:00',
            value: 14,
          },
          {
            label: '16:00',
            value: 16,
          },
          {
            label: '18:00',
            value: 18,
          },
          {
            label: '20:00',
            value: 20,
          },
          {
            label: '22:00',
            value: 22,
          },
        ],
        title: '',
        dialogFormVisible: false,
      }
    },
    created() {},
    methods: {
      showEdit(row) {
        if (!row) {
          this.title = '添加'
        } else {
          this.title = '编辑'
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = row[key]
          })
          this.formData.acWeek = this.formData.acWeek
            .split('-')
            .map((i) => parseInt(i))
          this.formData.acHour = this.formData.acHour.split('-').map((i) => {
            return this.acHourOptions.find((j) => j.value === parseInt(i)).value
          })
        }
        this.dialogFormVisible = true
      },
      close() {
        // this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let saveData, func
            if (!this.formData.infoId) {
              saveData = {
                weekStr: this.formData.acWeek.join('-'),
                hourStr: this.formData.acHour.join('-'),
                acName: this.formData.infoName,
              }
              func = saveStrategy
            } else {
              saveData = {
                acWeek: this.formData.acWeek.join('-'),
                setDate: this.formData.acHour.join('-'),
                infoId: this.formData.infoId,
                infoName: this.formData.infoName,
              }
              func = updateStrategy
            }

            const msg = await func(saveData).msg
            this.$baseMessage(
              msg || '成功！',
              'success',
              'vab-hey-message-success'
            )
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
