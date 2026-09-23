<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="14">
      <el-form
        ref="ruleForm"
        label-width="80px"
        :model="formData"
        :rules="rules"
        size="mini"
        :disabled="formDisabled"
      >  
          <el-col :span="24">
            <el-form-item label="原因" prop="gzzdqksm">
              <el-input
                v-model="formData.gzzdqksm"
                placeholder="原因"
                :style="{ width: '100%' }"
                type="textarea"
                rows="4"
              />
            </el-form-item>
          </el-col>
         
        </el-form> 
    </el-row>
      
    <div slot="footer" v-if="!formDisabled"> 
      <el-button @click="close">取消</el-button>
      <el-button @click="add" type="primary">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {
    editInfo,
    getDetailInfo,
    deleteFileEditInfo,
    deleteFileInfo,
  } from '@/oapi/yqns_sjzg/sjzgtjxx'
  import { download } from '@/oapi/audit/report'
  import store from '@/store'
  const { baseURL } = require('@/config')
  export default {
    name: 'sjzgtjxxEdit',
    inheritAttrs: false,
    props: [],
    data() {
      return {
        baseApi: baseURL,
        api: '/oiaudit/fileManage/upload',
        headers: {
          token: store.getters['user/token'],
        },
        formData: {
          gzzdqksm:"",
        },
        formDisabled: true,
        tableData: [],
        tableData2: [],
        rules: {
          // code: [
          //   {
          //     required: true,
          //     message: '请输入文号',
          //     trigger: 'blur',
          //   },
          // ],
        },
        dialogFormVisible: false,
        title: '原因',
        typeOptions: [
          {
            label: '节约各类开支',
            value: '节约各类开支',
          },
          {
            label: '建设工程项目审减',
            value: '建设工程项目审减',
          },
          {
            label: '经济合同审减',
            value: '经济合同审减',
          },
          {
            label: '物资采购项目审减',
            value: '物资采购项目审减',
          },
          {
            label: '招投标审减',
            value: '招投标审减',
          },
          {
            label: '剔除联合账簿不合理费用',
            value: '剔除联合账簿不合理费用',
          },
          {
            label: '挽回各类损失',
            value: '挽回各类损失',
          },
          {
            label: '挽回债券性和股权性投资损失',
            value: '挽回债券性和股权性投资损失',
          },
          {
            label: '挽回货币性资产损失',
            value: '挽回货币性资产损失',
          },
          {
            label: '挽回非货币性资产损失',
            value: '挽回非货币性资产损失',
          },
          {
            label: '内部收缴',
            value: '内部收缴',
          },
          {
            label: '罚款金额',
            value: '罚款金额',
          },
          {
            label: '收缴小金库',
            value: '收缴小金库',
          },
          {
            label: '账外资金',
            value: '账外资金',
          },
          {
            label: '收缴/罚没其他违规违纪资金',
            value: '收缴/罚没其他违规违纪资金',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
      // 送审
      send() {},
      async showEdit(row, disabled) {
        this.dialogFormVisible = true
        this.formDisabled = disabled
        // if (row && !disabled) {
        //   this.title = '编辑'
        // } else if (row && disabled) {
        //   this.title = '详细'
        // }
      },
      close() {
        this.formData = {
          zdgzzds: '',
          xggzzds: '',
          ysclsx: '',
          sjsfjgysbg: '',
          ysclje: '',
          ysclr: '',
          sjsfjgysbgr: '',
          lsysclsx: '',
          yscllsqk: '',
          qzsjdjcf: '',
          qzsjzwcf: '',
          qzsjnbjlcf: '',
          qtjjcf: '',
          cjr: '',
          cjsj: '',
          sjzgtjid: '', 
        }
        this.dialogFormVisible = false
        this.tableData = []
        this.formDisabled = true
      },
      add() {
        this.$refs['ruleForm'].validate(async (valid) => {
          if (valid) {
            this.formData.attIds = this.tableData.map((item) => item.attid).join(",") 
              this.$emit("setTable",this.formData)
              this.dialogFormVisible = false
            
          } else {
            return false
          }
        })
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      async handleDelete(row, type) {
        if (type == '1') {
          let list = this.tableData
          list = list.filter((item) => item.attid != row.attid)
          this.tableData = list
          await deleteFileEditInfo({ attid: row.attid })
        } else if (type == '2') {
          let list = this.tableData2
          list = list.filter((item) => item.attid != row.attid)
          this.tableData2 = list
          await deleteFileInfo({ attid: row.attid })
        }
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.result == '200') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      handlePreview2(file) {},
      handleSuccess2(file) {
        if (file.result == '200') {
          let list = this.tableData2
          list.push(file.data)
          this.tableData2 = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
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

  .title {
    margin-top: 50px;
  }
</style>
