<!--
 * @Date: 2022-04-13 16:32:03
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-04-24 14:17:54
 * @FilePath: /hb-admin/src/views/contract/contractManage/components/block/SubEdit.vue
-->

<template>
  <el-dialog
    append-to-body
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
    :close-on-click-modal="false"
  >
    <el-row :gutter="15">
      <el-form ref="form" label-width="100px" :model="formData" :rules="rules">
        <el-col v-for="field in fields" :key="field.value" :span="12">
          <el-form-item :label="field.label" :prop="field.value">
            <template v-if="field.type == 'date-picker'">
              <el-date-picker
                v-model="formData[field.value]"
                clearable
                :placeholder="`请选择${field.label}`"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </template>
            <template v-else>
              <el-input
                v-model="formData[field.value]"
                clearable
                :placeholder="`请输入${field.label}`"
                :style="{ width: '100%' }"
              />
            </template>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <template #footer>
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { saveCc } from '@/api/contract/manage'

  export default {
    name: 'SubEdit',
    inject: ['fatherFetchItem'],
    props: {
      fields: {
        type: Array,
        default: () => [],
      },
      contract: {
        type: Object,
        default: () => {
          return {
            informationList: [],
            nodeList: [],
          }
        },
      },
    },
    data() {
      return {
        formData: {
          infoid: undefined,
          infoname: undefined,
          infoxh: undefined,
          infopinpai: undefined,
          infoorg: undefined,
          infoprice: undefined,
          infonum: undefined,
          infodesc: undefined,
          infomomo: undefined,
          infostartdate: undefined,
          infoenddate: undefined,
          infotype: undefined,
        },
        rules: {
          field101: [
            {
              required: true,
              message: '请输入物品名称',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
      }
    },

    created() {},
    methods: {
      showEdit(row) {
        this.formData.contractid = row.contractid
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
        this.form = this.$options.data().form
        this.dialogFormVisible = false
      },
      //保存
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { msg } = await saveCc(this.formData)
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.close()
          }
        })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
</style>
