<template>
  <div>
    <el-row :gutter="15">
      <el-form
        ref="elForm"
        label-width="150px"
        :model="formData"
        :rules="rules"
        size="medium"
      >
        <el-col :span="12">
          <el-form-item label="风险编号" prop="risknumber">
            <el-input
              v-model="formData.risknumber"
              clearable
              placeholder="请输入风险编号"
              :style="{ width: '100%' }"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险名称" prop="riskname">
            <el-input
              disabled
              v-model="formData.riskname"
              clearable
              placeholder="请输入风险名称"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="风险描述" prop="riskdes">
            <el-input
              disabled
              v-model="formData.riskdes"
              clearable
              placeholder="请输入风险描述"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="归属单位" prop="ssjgName">
            <el-input
              disabled
              v-model="formData.ssjgName"
              clearable
              placeholder="请输入归属单位"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="level">
            <el-select
              disabled
              v-model="formData.level"
              clearable
              placeholder="请选择风险等级"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field105Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险应对策略类型" prop="copingPlot">
            <el-select
              disabled
              v-model="formData.copingPlot"
              clearable
              placeholder="请选择风险应对策略类型"
              :style="{ width: '100%' }"
            >
              <el-option
                v-for="(item, index) in field106Options"
                :key="index"
                :disabled="item.disabled"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险期望值" prop="riskHopeValue">
            <el-input
              disabled
              v-model="formData.riskHopeValue"
              clearable
              placeholder="请输入风险期望值"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险应对负责人" prop="ydusername">
            <el-input
              disabled
              v-model="formData.ydusername"
              clearable
              placeholder="请输入风险应对负责人"
              :style="{ width: '100%' }"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="应对方案" prop="yddes">
            <el-input
              disabled
              v-model="formData.yddes"
              :autosize="{ minRows: 4, maxRows: 8 }"
              placeholder="请输入应对方案"
              :style="{ width: '100%' }"
              type="textarea"
            />
          </el-form-item>
        </el-col>

        <el-col :span="24">
          <el-divider>一体化管控措施</el-divider>
        </el-col>
        <el-col :span="24" style="margin-bottom: 20px">
          <!-- <div style="text-align: right; margin-bottom: 5px; margin-right: 10px" v-if="!disabled">
              <el-button type="success" @click="hanAddControl">新增</el-button>
            </div> -->
          <el-table :data="controlList">
            <el-table-column
              align="center"
              label="风险控制点编号"
              prop="controlnumber"
              show-overflow-tooltip
            >
              <template #default="{ row }">
                <el-button type="text" @click="showControlDetail(row)">
                  {{ row.controlnumber }}
                </el-button>
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              label="一体化控制目标"
              prop="controldes"
              show-overflow-tooltip
            />
            <el-table-column
              align="center"
              label="控制措施"
              prop="conkzcs"
              show-overflow-tooltip
            />
            <!-- <el-table-column align="center" label="操作">
                <template #default="{ row }">
                  <el-button type="text" @click="handleEditControl(row)"  disabled>
                    修改
                  </el-button>
                  <el-button type="text" @click="handleDeleteControl(row)" disabled >
                    删除
                  </el-button>
                </template>
              </el-table-column> -->
          </el-table>
        </el-col>
        <el-col :span="24">
          <UEditor
            ref="ueditor"
            v-model="formData.content"
            :height="300"
            :templates="templates"
            template="nbsj"
          />
        </el-col>
        <el-col :span="24">
          <el-divider>附件</el-divider>
        </el-col>
        <el-col :span="24">
          <div style="text-align: right; margin-bottom: 5px">
            <!-- <el-button type="success">上传</el-button> -->
          </div>
          <el-table :data="formData.attachments">
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
                <el-button type="text" @click="handleDownload(row)">
                  下载
                </el-button>
                <el-button type="text" @click="handlePreviewFile(row)">
                  预览
                </el-button>
                <!-- <el-button type="text" @click="handleDelete(row)">
                          删除
                        </el-button>  -->
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-form>
    </el-row>

    <controlMeasures ref="controlMeasures" />
  </div>
</template>
<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import { riPlanInfo } from '@/api/systemLog'
  import { downFieldById } from '@/api/risk/riskEvents'
  import UEditor from '@/components/UEditor'
  import controlMeasures from '@/views/risk/treatment/controlMeasures'
  export default {
    name: 'TreatmentEdit',
    components: { UEditor, controlMeasures },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        controlList: [],
        title: '',
        dialogFormVisible: false,
        templates: [],
        formData: {
          risknumber: '',
          riskname: '',
          riskdes: '',
          ssjgName: '',
          level: '',
          copingPlot: '',
          riskHopeValue: '',
          ydusername: '',
          yddes: '',
        },
        rules: {
          field106: [
            {
              required: true,
              message: '请选择风险应对策略类型',
              trigger: 'change',
            },
          ],
          field107: [
            {
              required: true,
              message: '请输入风险期望值',
              trigger: 'blur',
            },
          ],
          field108: [
            {
              required: true,
              message: '请选择风险应对负责人',
              trigger: 'blur',
            },
          ],
        },
        field105Options: [
          {
            label: '未评估',
            value: '0',
          },
          {
            label: '很低',
            value: '1',
          },
          {
            label: '较低',
            value: '2',
          },
          {
            label: '中等',
            value: '3',
          },
          {
            label: '较高',
            value: '4',
          },
          {
            label: '很高',
            value: '5',
          },
        ],
        field106Options: [
          {
            label: '暂无',
            value: '0',
          },
          {
            label: '承担',
            value: '1',
          },
          {
            label: '转移',
            value: '2',
          },
          {
            label: '规避',
            value: '3',
          },
          {
            label: '降低（风险控制）',
            value: '4',
          },
        ],
        controltypeOptions: [
          {
            label: '预防性控制',
            value: '1',
          },
          {
            label: '发现性控制',
            value: '2',
          },
          {
            label: '纠正性控制',
            value: '3',
          },
        ],
        controlmethodOptions: [
          {
            label: '手工',
            value: '1',
          },
          {
            label: '自动',
            value: '2',
          },
          {
            label: '依赖手工的自动化',
            value: '3',
          },
        ],
        financialreportidentifyOptions: [
          {
            label: '存在与发生',
            value: '1',
          },
          {
            label: '完整性',
            value: '2',
          },
          {
            label: '权利与义务',
            value: '3',
          },
          {
            label: '估计与平摊',
            value: '4',
          },
          {
            label: '表达与披露',
            value: '5',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {},
    methods: {
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
      save() {},
      showRead(row) {
        console.log(row, 'row')
        this.title = '风险应对'
        let data = {
          riskid: row.riskid,
        }
        riPlanInfo(data).then((res) => {
          this.riskcopingid = res.data.copings && res.data.copings.riskcopingid
          Object.assign(this.formData, {
            ...(res.data.controls ? res.data.controls[0] : {}),
            content: res.data.copings && res.data.copings.content,
            status: res.data.risk.status,
            risknumber: res.data.risk.risknumber,
            riskname: res.data.risk.riskname,
            riskdes: res.data.risk.riskdes,
            ssjgName: res.data.risk.ssjgName,
            level: res.data.risk.level ? res.data.risk.level : '0',
            copingPlot:
              res.data.copings && res.data.copings.copingplot
                ? res.data.copings.copingplot
                : '',
            riskHopeValue: res.data.copings && res.data.copings.riskhopevalue,
            ydusername: res.data.userName,
            userId: res.data.copings && res.data.copings.copinghead,
            yddes: res.data.copings && res.data.copings.yddes,
            riskid: res.data.risk.riskid,
            copingId: res.data.copings && res.data.copings.riskcopingid,
            riskcopingid: res.data.copings && res.data.copings.riskcopingid,
            attachments: res.data.attachments || [],
          })
          this.tableDataFile = res.data.attachments || []
          this.fileIdList = []
          if (res.data.attachments && res.data.attachments.length != 0) {
            res.data.attachments.forEach((item) => {
              this.fileIdList.push(item.attid)
            })
          }
          this.controlList = res.data.controls || []
        })

        this.dialogFormVisible = true
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
      close() {
        this.dialogFormVisible = false
      },
      showControlDetail(row) {
        //一体化控制目标详情
        this.$refs.controlMeasures.showEdit(
          row,
          {
            riskcopingid: this.riskcopingid,
            riskHopeValue: this.formData.riskHopeValue,
          },
          true
        )
      },
    },
  }
</script>
<style></style>
