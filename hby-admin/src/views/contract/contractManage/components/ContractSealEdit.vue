<template>
  <el-dialog
    :close-on-click-modal="false"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-row :gutter="15">
      <el-form
        ref="form"
        :disabled="disabled"
        label-width="150px"
        :model="formData"
        :rules="rules"
      >
        <el-col :span="24">
          <el-divider>合同基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractno">
            <el-input
              v-model="node.contractno"
              disabled
              placeholder=""
              readonly
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractname">
            <el-input
              v-model="node.contractname"
              disabled
              placeholder=""
              readonly
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              :disabled="false"
              @click="handleDetail(node)"
            >
              详细
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额（元）" prop="contractmoney">
            <el-input
              v-model="node.contractmoney"
              disabled
              placeholder=""
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同类型" prop="contracttype">
            <el-select
              v-model="node.contracttype"
              filterable
              placeholder=""
              style="width: 100%"
              disabled
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.label"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请用印日期" prop="createtime">
            <el-date-picker
              v-model="formData.createtime"
              clearable
              placeholder=""
              :style="{ width: '100%' }"
              value-format="yyyy-MM-dd"
            />
          </el-form-item>
        </el-col>
        <!-- <el-col :span="12">
          <el-form-item label="项目名称" prop="topicname">
            <el-input
              v-model="node.topicname"
              clearable
              readonly
              placeholder=""
              :style="{ width: '256px' }"
            />
          </el-form-item>
        </el-col> -->

        <el-col :span="12">
          <el-form-item label="印章所属主体" prop="counterparthank">
            <el-input
              v-model="formData.counterparthank"
              clearable
              placeholder="请输入印章所属主体"
              :style="{ width: '256px' }"
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.unit.show()"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="印章名称">
            <!-- <el-input
              v-model="formData.counterpartcode"
              clearable
              placeholder="请输入印章名称"
              :style="{ width: '256px' }"
            /> -->
            <el-checkbox
              :indeterminate="isIndeterminate"
              v-model="checkAll"
              @change="handleCheckAllChange"
            >
              全选
            </el-checkbox>
            <div style="margin: 15px 0"></div>
            <el-checkbox-group
              v-model="counterpartcode"
              @change="handleCheckedCitiesChange"
            >
              <el-checkbox
                v-for="city in cities"
                :label="city"
                :key="city"
              ></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-col>
        <!-- <el-col :span="24">
          <el-form-item label="用印信息">
            <el-input
              v-model="formData.projectgoal"
              :autosize="{ minRows: 4, maxRows: 4 }"
              placeholder="请输入用印信息"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col> -->

        <!-- <el-col :span="24">
          <el-divider>盖章文件</el-divider>
          <el-col :span="24">
            <div style="text-align: right; margin-bottom: 5px">
              <el-upload
                class="upload-demo"
                :show-file-list="false"
                :action="baseApi + api"
                :headers="headers"
                :on-preview="handlePreview"
                :on-success="handleSuccess"
                :file-list="tableData"
                accept=".pdf"
                :data="{ contractId: formData.contractid }"
              >
                <el-button type="success" v-if="this.tableData.length == 0">
                  上传
                </el-button>
              </el-upload>
            </div>
            <el-table :data="tableData">
              <el-table-column
                align="center"
                label="文件名称"
                prop="singingName"
              />
              <el-table-column
                align="center"
                label="文件大小(KB)"
                prop="singingSize"
              />
              <el-table-column align="center" label="创建人" prop="realname" />
              <el-table-column
                align="center"
                label="操作"
                show-overflow-tooltip
                width="120"
              >
                <template #default="{ row }">
                  <el-button type="text" @click="handlePreview(row)">
                    预览
                  </el-button>
                  <el-link
                    :href="`${baseApi}/contract/downloadFtp/upload?singingId=${row.singingId}`"
                    style="font-size: 12px; margin: 0 10px"
                    type="primary"
                  >
                    下载
                  </el-link>
                  <el-button type="text" @click="handleDelete(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-col> -->
      </el-form>
    </el-row>
    <template #footer v-if="!disabled">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
    <unit-options ref="unit" @selected="handleUnitSelected" />
    <CreateDetail ref="detail" />
    <ProcessList ref="process" />
  </el-dialog>
</template>

<script>
  import {
    getContractSealDetail,
    getContractSealDetailForAdd,
    getContractTypes,
    saveContractSeal,
    download,
    pushInfo,
  } from '@/api/contract/manage'
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { removeFileFromContract } from '@/api/contract/fulfil'
  import AttachList from './AttachList.vue'
  import CreateDetail from './contractsEdit/CreateDetail.vue'
  import UnitOptions from './options/unit.vue'
  import { formatDay } from '@/utils'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  const cityOptions = [
    '公司公章',
    '党委章',
    // "纪委章",
    '工会章',
    // "团支部章",
    // "党支部章",
    '法人章',
    // "财务专用章",
    // "发票专用章",
    // "预留印鉴章",
    // "部门章",
    '合同专用章',
    '其它印鉴',
  ]
  export default {
    name: 'ContractSealEdit',
    components: { AttachList, UnitOptions, CreateDetail, ProcessList },
    data() {
      return {
        baseApi: baseURL,
        api: '/contract/contract/importFile',
        headers: {
          token: store.getters['user/token'],
        },
        submiting: false,
        node: {},
        // localList: [],
        formData: {
          flowId: 622324,
          contractid: undefined,
          budgetid: undefined,
          recordparent: undefined,

          counterparthank: undefined,
          createtime: undefined,
          projectgoal: undefined,
          sealorgid: undefined,
          // attList: [],
        },
        typeOptions: [],
        //不能放formData中，否则选择其中一个，就会全选，应该是element的bug
        counterpartcode: [],
        rules: {
          counterparthank: [
            {
              required: true,
              message: '请输入印章所属主体',
              trigger: 'blur',
            },
          ],
        },
        title: '',
        dialogFormVisible: false,
        disabled: false,
        curRow: {},
        checkAll: false,
        cities: cityOptions,
        isIndeterminate: false,
        tableData: [],
        contractid: '',
        show: false,
      }
    },
    created() {
      this.fetchTypes()
      this.show =
        JSON.parse(localStorage.getItem('userInfo')).currentOrg.orgname ==
        '浙江富浙科技有限公司'
    },
    methods: {
      /**
       * @description: 打开详情表单弹框
       * @param {*} row 选择的行数据
       * @return {*}
       */
      handleDetail(row) {
        this.$refs['detail'].showDetail(row, row.contracttype)
      },
      //多选的回调
      handleCheckAllChange(val) {
        this.counterpartcode = val ? cityOptions : []
        this.isIndeterminate = false
      },
      handleCheckedCitiesChange(value) {
        let checkedCount = value.length
        this.checkAll = checkedCount === this.cities.length
        this.isIndeterminate =
          checkedCount > 0 && checkedCount < this.cities.length
      },
      handleUnitSelected(node) {
        this.formData.sealorgid = node.id
        this.formData.counterparthank = node.label
        this.$forceUpdate()
      },
      // handleDelAtt(row, index) {
      //
      //
      //   deleteSealAttach({
      //     attid: row.attid,
      //   }).then((res) => {
      //     this.formData.attList.splice(index, 1)
      //     this.$message.success('删除成功！')
      //   })
      // },
      async fetchTypes() {
        const res = await getContractTypes()
        this.typeOptions = res.typeofList.map((item) => {
          return {
            label: item.typename,
            value: item.typeid,
          }
        })
      },
      async fetchItem(row) {
        this.counterpartcode = []
        if (row.budgetid) {
          const res = await getContractSealDetail({
            budgetId: row.budgetid,
            flowId: this.formData.flowId,
          })
          Object.keys(this.formData).forEach((key) => {
            this.formData[key] = res.data.tcpb[key]
          })
          this.counterpartcode = res.data.tcpb.counterpartcode.split(',')
          this.formData.flowId = res.data.flowId
          this.node = res.data.tcu
          // this.formData.attList = res.data.attList
        } else {
          const res = await getContractSealDetailForAdd({
            contractId: row.contractid,
            flowId: this.formData.flowId,
          })
          this.node = res.dataMap.unit
          this.formData.counterparthank = res.dataMap.unit.orgname
          this.formData.contractid = this.node.contractid
          this.formData.recordparent = this.node.recordparent
          this.formData.createtime = formatDay(new Date())
        }
      },
      // showDetail(row) {
      //   this.title = '查看'
      //   this.disabled = true
      //   this.fetchItem(row)
      //   this.dialogFormVisible = true
      // },
      showEdit(row) {
        this.contractid = row.contractid
        this.title = '编辑'
        this.curRow = row
        this.fetchItem(row)
        this.dialogFormVisible = true
      },
      showDetail(row) {
        this.disabled = true
        this.contractid = row.contractid
        this.title = '详情'
        this.curRow = row
        this.fetchItem(row)
        this.dialogFormVisible = true
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
        this.tableData = []
        this.disabled = false
      },
      // handleUploadSuccess(val) {
      //   this.localList.push(val.data)
      // },
      //保存
      save() {
        if (this.submiting) return
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            let counterpartcodeList = ''
            if (this.counterpartcode.length > 0) {
              counterpartcodeList = this.counterpartcode.join(',')
            }

            // const arrAttid = this.localList.map((item) => item.attid)
            // this.formData.attids = arrAttid.join(',')

            const { msg, data } = await saveContractSeal({
              counterpartcode: counterpartcodeList,
              ...this.formData,
              // singingId: this.tableData[0].singingId.toString(),
            })
            this.disabled = false
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            this.$emit('fetch-data')
            this.fetchItem(this.curRow)
            pushInfo({
              contractid: this.contractid,
              counterpartcode: counterpartcodeList,
            }) //消息推送接口
            if (this.show) {
              this.$refs['process'].save(6, this.formData.budgetid)
            }
            this.close()
          }
        })
      },
      //上传的回调
      handleSuccess(file) {
        if (file.code == '1') {
          let list = this.tableData
          list.push(file.data)
          this.tableData = list
          this.$baseMessage(file.msg, 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //下载的回调
      async handleDown(row) {
        const data = await download({ singingId: row.singingId })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/pdf',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await removeFileFromContract({
            singingId: row.singingId,
          })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          this.tableData = []
          await this.fetchData()
        })
      },
      //预览
      async handlePreview(row) {
        const { data } = await getPrivewAttInfo({
          attId: row.singingId,
          attType: 1,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url })
      },
    },
  }
</script>
<style scoped>
  .formula .el-form-item--small.el-form-item {
    margin-bottom: 5px;
  }
  .el-table {
    margin-top: 10px;
    margin-bottom: 18px;
  }
</style>
