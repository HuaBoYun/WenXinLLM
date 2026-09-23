<template>
  <div>
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      width="1000px"
      @close="close"
      append-to-body
    >
      <el-row :gutter="15">
        <el-form
          ref="elForm"
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12" v-if="showMJ">
            <el-form-item
              label="密级"
              prop="secrectLevelId"
              :rules="[
                { required: true, trigger: 'change', message: '请选择密级' },
              ]"
            >
              <el-select
                disabled
                v-model="formData.secrectLevelId"
                clearable
                placeholder="密级"
                style="width: 100%"
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
                :style="{ width: '75%' }"
                disabled
              />
              <el-button
                :style="{ marginLeft: '10px' }"
                type="primary"
                @click="$refs.ZXPerson.showEdit(formData.secrectLevelId)"
                disabled
              >
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="showMJ">
            <el-divider>基本信息</el-divider>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportname">
              <el-input
                v-model="formData.reportname"
                clearable
                disabled
                placeholder="请输入报告名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告时间" prop="reporttime">
              <el-date-picker
                v-model="formData.reporttime"
                clearable
                disabled
                format="yyyy-MM-dd"
                placeholder="请选择报告时间"
                :style="{ width: '100%' }"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="reporttype">
              <el-select
                v-model="formData.reporttype"
                clearable
                disabled
                placeholder="请选择报告类型"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reporttypeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告方式" prop="reportmode">
              <el-select
                v-model="formData.reportmode"
                clearable
                disabled
                placeholder="请选择报告方式"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reportmodeOptions"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告层级" prop="reportlevel">
              <el-select
                v-model="formData.reportlevel"
                clearable
                disabled
                placeholder="请选择报告层级"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="(item, index) in reportmodeOptions2"
                  :key="index"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告人" prop="reporter">
              <el-input
                v-model="formData.reporter"
                disabled
                style="width: 75%; margin-right: 8px"
              ></el-input>
              <el-button type="primary" @click="handleShowUser" disabled>
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告部门" prop="reportdepartment">
              <el-input
                v-model="formData.reportdepartment"
                disabled
                style="width: 75%; margin-right: 8px"
              ></el-input>
              <el-button type="primary" disabled @click="handleShowCompent">
                选择
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="内容" prop="repdesc">
              <UEditor
                ref="ueditor"
                v-model="formData.repdesc"
                :height="300"
                disabled
                :templates="templates"
                style="margin-left: -100px"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-divider>附件</el-divider>
          </el-col>
          <el-col :span="24">
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
                width="120"
              >
                <template #default="{ row }">
                  <el-button
                    type="text"
                    @click="handleDownload(row)"
                    :disabled="false"
                  >
                    下载
                  </el-button>
                  <el-button
                    type="text"
                    @click="handlePreviewFile(row)"
                    :disabled="false"
                  >
                    预览
                  </el-button>
                  <!-- <el-button type="text" @click="handleEdit2(row)">
                    删除
                  </el-button> -->
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
      </template>
      <ZXPerson ref="ZXPerson" @projectManage="handleZXPersonSelected" />
    </el-dialog>
  </div>
</template>
<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { getDefaultReportInfo } from '@/api/risk/report'
  import UEditor from '@/components/UEditor'
  import { deleteFieldById, downFieldById } from '@/api/risk/riskEvents'
  import ZXPerson from '@/components/selectPerson'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  export default {
    name: 'NormalEdit',
    props: [],
    components: {
      UEditor,
      ZXPerson,
    },
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        disabled: true,
        formData: {
          reportname: undefined,
          reporttime: undefined,
          reporttype: null,
          reportmode: null,
          reportdepartmentid: undefined,
          reportdepartment: undefined,
          reporter: undefined,
          reporterid: undefined,
          repdesc: undefined,
          reportlevel: undefined,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        rules: {
          reportname: [
            {
              required: true,
              message: '请输入报告名称',
              trigger: 'blur',
            },
          ],
          reporttime: [
            {
              required: true,
              message: '请选择报告时间',
              trigger: 'blur',
            },
          ],
          reporttype: [
            {
              required: true,
              message: '请选择报告类型',
              trigger: 'change',
            },
          ],
          reportmode: [
            {
              required: true,
              message: '请选择报告方式',
              trigger: 'change',
            },
          ],
          reportlevel: [
            {
              required: true,
              message: '请选择报告层级',
              trigger: 'change',
            },
          ],
          reporter: [
            {
              required: true,
              message: '请选择报告人',
              trigger: 'change',
            },
          ],
          reportdepartment: [
            {
              required: true,
              message: '请选择报告部门',
              trigger: 'change',
            },
          ],
        },
        list: [],
        tableData: [],
        templates: [],
        reporttypeOptions: [
          {
            label: '对内报告',
            value: '对内报告',
          },
          {
            label: '对外报告',
            value: '对外报告',
          },
        ],
        reportmodeOptions: [
          {
            label: '定期报告',
            value: '定期报告',
          },
          {
            label: '非定期报告',
            value: '非定期报告',
          },
        ],
        reportmodeOptions2: [
          {
            label: '部门级',
            value: '部门级',
          },
          {
            label: '公司级',
            value: '公司级',
          },
        ],
        showMJ: false,
        MJoption: [],
      }
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('ReportNormal')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
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
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用？
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      async showRead(row) {
        this.disabled = true
        this.title = '编辑'
        let res = await getDefaultReportInfo({
          id: row.reportid,
          type: 'fx_zdy',
        })
        Object.keys(this.formData).forEach((key) => {
          this.formData[key] = res.data.report[key]
        })
        this.tableData = res.data.attachmentList || []
        this.dialogFormVisible = true
      },
      close() {
        this.dialogFormVisible = false
      },
      async handleDownload(row) {
        console.log('downlaod', row)
        const res = await downFieldById({ id: row.attid })
        console.log(res)
        if (!res) return
        let filename = row.attname
        let blob = new Blob([res]) //res即为blob数据，请注意自己的数据形式
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
      // 调起部门选择
      handleShowCompent() {
        this.$refs['comTreeRef'].show()
      },
      // 部门人员选择
      handleShowUser() {
        this.$refs['userTreeRef'].show()
      },
      async handleZXPersonSelected(val) {
        const ids = val.map((res) => res.staffid).toString()
        const names = val.map((res) => res.realname).toString()
        this.formData.staffScopeIds = ids
        this.formData.staffScopeNames = names
      },
    },
  }
</script>
<style></style>
