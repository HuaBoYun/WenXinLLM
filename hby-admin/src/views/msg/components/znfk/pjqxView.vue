<template>
  <div>
    <el-form ref="form" label-width="120px" :model="formData" :rules="rules">
      <el-row>
        <el-col :span="12" v-if="showMJ">
          <el-form-item
            label="密级"
            prop="secrectLevelId"
            :rules="[
              { required: true, trigger: 'change', message: '请选择密级' },
            ]"
          >
            <el-select
              v-model="formData.secrectLevelId"
              clearable
              placeholder="密级"
              style="width: 100%"
              :disabled="!footer"
              @change="changeMJ"
            >
              <el-option
                v-for="item in MJoption"
                :key="item.levelId"
                :label="item.levelName"
                :value="item.levelId"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="showMJ">
          <el-form-item label="知悉范围" prop="staffScopeNames">
            <el-input
              v-model="formData.staffScopeNames"
              readonly
              placeholder="请选择知悉范围"
              :style="{ width: '80%' }"
              disabled
            />
            <el-button
              :style="{ marginLeft: '10px' }"
              type="primary"
              @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
              :disabled="!formData.secrectLevelId || !footer"
            >
              选择
            </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-if="showMJ">
          <el-divider>基本信息</el-divider>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷编号" prop="defectscode">
            <el-input
              placeholder="请输入缺陷编号"
              v-model.trim="formData.defectscode"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷名称" prop="defectsname">
            <el-input
              placeholder="请输入缺陷名称"
              v-model.trim="formData.defectsname"
              :disabled="!footer"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发生日期" prop="createtime">
            <el-date-picker
              style="width: 100%"
              v-model="formData.createtime"
              type="date"
              value-format="yyyy-MM-dd"
              placeholder="选择日期"
              :disabled="!footer"
            ></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="涉及金额(万元)" prop="amount">
            <el-input
              v-model.trim="formData.amount"
              :disabled="!footer"
              @input="numChange"
              placeholder="请输入涉及金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷类别" prop="defectcategory">
            <el-select
              v-model="formData.defectcategory"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="内控体系设计缺陷" :value="1"></el-option>
              <el-option label="内控制度缺陷" :value="2"></el-option>
              <el-option label="内控执行缺陷" :value="3"></el-option>
              <el-option label="内控监督缺陷" :value="4"></el-option>
              <el-option label="重大风险防控缺陷" :value="5"></el-option>
              <el-option label="其它内控缺陷" :value="6"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="缺陷等级" prop="defectlevel">
            <el-select
              v-model="formData.defectlevel"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="一般缺陷" :value="1"></el-option>
              <el-option label="重要缺陷" :value="2"></el-option>
              <el-option label="重大缺陷" :value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" style="height: 48px">
          <el-form-item label="缺陷种类" prop="defecttype">
            <el-select
              v-model="formData.defecttype"
              style="width: 100%"
              :disabled="!footer"
            >
              <el-option label="财报缺陷" :value="1"></el-option>
              <el-option label="非财报缺陷" :value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否涉诉" prop="litigation" style="height: 30px">
            <el-radio-group v-model="formData.litigation" :disabled="!footer">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否境外" prop="overseas">
            <el-radio-group v-model="formData.overseas" :disabled="!footer">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="原因分析" prop="causeanalysis">
            <el-input
              v-model.trim="formData.causeanalysis"
              :disabled="!footer"
              placeholder="请输入原因分析"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="缺陷描述及依据" prop="description">
            <el-input
              type="textarea"
              :rows="4"
              v-model.trim="formData.description"
              :disabled="!footer"
              placeholder="请输入缺陷描述及依据"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>文件上传</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px" v-if="footer">
            <el-upload
              class="upload-demo"
              :show-file-list="false"
              :action="
                assid ? baseApi + api + '?assId=' + assid : baseApi + api2
              "
              :headers="{
                token: token,
              }"
              :on-preview="handlePreview"
              :on-success="handleSuccess"
              :file-list="tableData"
            >
              <el-button type="success">上传</el-button>
            </el-upload>
          </div>
          <el-table :data="tableData">
            <el-table-column align="center" label="附件名称" prop="attname" />
            <el-table-column
              align="center"
              label="文件大小(KB)"
              prop="attsize"
            />
            <el-table-column align="center" label="创建人" prop="uploader" />
            <el-table-column
              align="center"
              label="操作"
              show-overflow-tooltip
              width="200"
            >
              <template #default="{ row }">
                <el-button type="text" @click="handleDown(row)">下载</el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <el-button v-if="footer" type="text" @click="handleDelete(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-form>
    <div style="text-align: right; margin-top: 10px" v-if="footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
      <el-button @click="ymsubmit" type="primary" :disabled="btnLoading">
        提交
      </el-button>
    </div>
    <Resubmit
      ref="resubmit"
      :flowtaskinfoflowid="flowtaskinfoflowid"
      :fromId="fromId"
      :ymFromId="ymFromId"
      :fromIdcopy="fromIdcopy"
      @fetchClose="close"
      :status="status"
    />
    <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
  </div>
</template>

<script>
  import { getDetails, saveOrUpdate } from '@/api/internal/project'
  import Resubmit from '@/views/msg/components/options/Resubmit.vue'
  const { baseURL } = require('@/config')
  import store from '@/store'
  import { getPrivewAttInfo } from '@/api/fwgl/gsls'
  import ZXPerson from '@/components/selectPerson.vue'
  import { getSPMJ } from '@/api/setting/mjsz'
  import { hasMJ, couldMJ } from '@/utils'
  export default {
    name: 'ProjectView',
    components: {
      Resubmit,
      ZXPerson,
    },
    data() {
      return {
        baseApi: baseURL,
        api: '/nkhg/nbkz/pjgl/updateupload ',
        api2: '/nkhg/nbkz/pjbg/addupload',
        token: store.getters['user/token'],
        formData: {
          amount: '',
          id: '',
          defectscode: '',
          defectsname: '',
          causeanalysis: '',
          description: '',
          defecttype: '',
          defectcategory: '',
          defectlevel: '',
          overseas: '',
          litigation: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        templates: [],
        tableData: [],
        renderData: [],
        modalType: 'new',
        assid: '', // 用于修改保存
        rules: {
          defectscode: [
            { required: true, trigger: 'blur', message: '请输入评价计划编号' },
          ],
          defectsname: [
            { required: true, trigger: 'blur', message: '请输入计划名称' },
          ],
        },
        footer: true,
        title: '',
        dialogFormVisible: false,

        //提交
        ymFromId: 0,
        flowId: 0,
        fromId: 0,
        fromIdcopy: 0,
        flowtaskinfoflowid: '',
        status: 0,
        MJoption: [],
        menuId: 0,
        showMJ: false,
        btnLoading: false,
      }
    },

    methods: {
      changeMJ(selectedValue) {
        const selectedItem = this.MJoption.find(
          (item) => item.levelId === selectedValue
        )
        if (selectedItem) {
          const label = selectedItem.levelName
          if (label == '非密' || label == '公开') {
            this.formData.staffScopeNames = '全部人员'
            this.formData.staffScopeIds = ''
          } else {
            this.formData.staffScopeIds = ''
            this.formData.staffScopeNames = ''
          }
        }
      },
      numChange(event) {
        // 允许数字和小数点
        const regex = /^\d*\.?\d*$/
        if (!regex.test(event)) {
          this.formData.num = this.formData.num
            .replace(/[^0-9.]/g, '')
            .replace(/(\..*)\./g, '$1')
        }
      },

      async showEdit(row, title) {
        if (title == 'edit') {
          this.title = '编辑'
        } else if (title == 'add') {
          this.title = '添加'
        } else {
          this.title = '查看'
          this.footer = false
        }
        if (row) {
          console.log('🚀 ~ showEdit ~ this.formData:', this.formData)
        }
        this.dialogFormVisible = true
      },
      async getMJData(id) {
        this.showMJ = couldMJ()
        if (this.showMJ) {
          const res2 = await getSPMJ({ flowType: id })
          this.MJoption = res2.data
        }
      },
      async showEdit(
        title,
        formId,
        flowtaskinfoflowid,
        ymFromId,
        isWfqdedit,
        status,
        nextNodeName,
        flowType
      ) {
        console.log('🚀 ~ flowType:', flowType)
        // 拿到类型传给getMJData获取审批的密级的下拉数据
        if (flowType) {
          this.getMJData(flowType)
        }
        // 流程相关
        this.fromId = formId
        this.flowtaskinfoflowid = flowtaskinfoflowid
        this.ymFromId = ymFromId
        this.status = status
        this.footer = title === 'edit'
        if (formId) {
          let { data } = await getDetails({ id: formId })
          this.formData = data.entity
          if (data.entity?.secrectLevelId) {
            localStorage.setItem(
              'SPsecrectLevelId',
              res.data.plan.secrectLevelId
            )
          }
        }
      },
      close() {
        this.formData = {
          amount: '',
          id: '',
          defectscode: '',
          defectsname: '',
          causeanalysis: '',
          description: '',
          defecttype: '',
          defectcategory: '',
          defectlevel: '',
          overseas: '',
          litigation: '',
          createtime: '',
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        }
        this.tableData = []
        this.$refs['form']?.resetFields()
        this.footer = true
        this.$bus.$emit('updateMsg', 0)
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          console.log(this.form)
          if (valid) {
            let attids = ''
            this.tableData.map((item) => {
              attids += item.attid
              attids += ','
            })
            attids = attids.substring(0, attids.length - 1)
            const params = {
              ...this.formData,
              attids: attids,
            }
            const { msg, data, code } = await saveOrUpdate(params)
            this.$message.success('新增成功')
            this.$emit('fetch-data')
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
      async handleDelete(row) {
        let list = this.tableData
        list = list.filter((item) => item.attid != row.attid)
        this.tableData = list
        const res = await deleteFile({ attid: row.attid })
        if (res && res.code === 200) {
          this.$message.success('删除成功')
        } else {
          this.$message.error('删除失败')
        }
      },
      handlePreview(file) {},
      handleSuccess(file) {
        if (file.code == '200') {
          let list = this.tableData || []
          list.push(file.data.Attachment)
          this.tableData = list
          this.$baseMessage('上传成功', 'success')
        } else {
          this.$baseMessage(file.msg, 'error')
        }
      },
      //提交
      async ymsubmit() {
        try {
          this.$refs['form'].validate(async (valid) => {
            if (valid) {
              this.btnLoading = true
              this.$refs.resubmit.ymsubmit()
            }
          })
        } catch (error) {
          this.btnLoading = false
        }
      },
      /**
       * @description: 文件预览
       * @param {*} row 文件信息
       * @return {*}
       */ async handlePreviewFile(row) {
        const { data } = await getPrivewAttInfo({
          fileId: row.fileId,
          attType: 2,
        })

        const url =
          data.previewurl +
          '?url=' +
          encodeURIComponent(Base64.encode(data.ftpUrl))
        this.$iFrameDialog({ iframeUrl: url }) // iframe弹框预览形式
      },
      handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
    },
  }
</script>
