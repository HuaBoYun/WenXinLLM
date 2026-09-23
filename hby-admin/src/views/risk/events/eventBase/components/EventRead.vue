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
          label-width="180px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="24">
            <el-col :span="12" v-if="showMJ">
              <el-form-item
                label="密级"
                prop="secrectLevelId"
                :rules="[
                  { required: true, trigger: 'change', message: '请选择密级' },
                ]"
              >
                <div>{{ getMJName(formData.secrectLevelId) }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="showMJ">
              <el-form-item label="知悉范围" prop="staffScopeNames">
                <div>{{ formData.staffScopeNames || '' }}</div>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>基本信息</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事件编号" prop="riskeventcode">
                {{ formData.riskeventcode }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="事件名称" prop="riskeventname">
                {{ formData.riskeventname }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发生部门" prop="occureddepartment">
                {{ formData.occureddepartment }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发生日期" prop="occureddate">
                {{ formData.occureddate }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="发现日期" prop="discovereddate">
                {{ formData.discovereddate }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="损失事件定性类别" prop="losseventcategory">
                {{
                  formData.losseventcategory == '1' ? '一般事件' : '重大事件'
                }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="经办人" prop="reporter">
                {{ formData.riskfactor2 }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="经办部门" prop="reportdepartment">
                {{ formData.recorddepart }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="版本号" prop="version">
                {{ formData.version }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="涉及企业名称"
                prop="involvingenterprisenames"
              >
                {{ formData.involvingenterprisenames }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="涉及企业层级"
                prop="involvingenterprisehierarchy"
              >
                {{ formData.involvingenterprisehierarchy }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="损失(风险)金额(万元)" prop="amountofdamages">
                {{ formData.amountofdamages }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="事件说明" prop="riskeventdescription">
                {{ formData.riskeventdescription }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="当期情况描述" prop="description">
                {{ formData.description }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="处置进展情况" prop="progressofdisposal">
                {{ formData.progressofdisposal }}
              </el-form-item>
            </el-col>
            <!-- <el-col :span="24">
              <el-divider>财务信息</el-divider>
            </el-col>
            <el-col :span="24">
              <el-form-item label="最大预估损失汇总金额" prop="maxestimateloss">
                {{ formData.maxestimateloss }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="已确认的直接损失总金额"
                prop="confirmeddirectloss"
              >
                {{ formData.confirmeddirectloss }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item
                label="已确认的直接损失净额"
                prop="confirmeddirectlossa"
              >
                {{ formData.confirmeddirectlossa }}
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-divider>索赔信息</el-divider>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔类型" prop="claimtype">
                {{ formData.claimtype }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔名称" prop="claimname">
                {{ formData.claimname }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="贷方会计科目代码" prop="accidd">
                {{ formData.accidd }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="贷方会计科目名称" prop="accnamed">
                {{ formData.accnamed }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔日" prop="claimdate">
                {{ formData.claimdate }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="入账日" prop="indate">
                {{ formData.indate }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔金额" prop="pricenumber">
                {{ formData.pricenumber }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="索赔说明" prop="claimdes">
                {{ formData.claimdes }}
              </el-form-item>
            </el-col> -->
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
              <!-- <div style="text-align: right; margin-bottom: 5px">
                    <el-button type="success">上传</el-button>
                  </div> -->
              <el-table :data="formData.attachments">
                <el-table-column
                  align="center"
                  label="附件名称"
                  prop="attname"
                />
                <el-table-column
                  align="center"
                  label="文件大小(KB)"
                  prop="attsize"
                />
                <el-table-column
                  align="center"
                  label="创建人"
                  prop="uploader"
                />
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
                        </el-button> -->
                  </template>
                </el-table-column>
              </el-table>
            </el-col>

            <!-- </el-tabs> -->
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <!-- <el-button type="primary" @click="save">确 定</el-button> -->
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { getPrivewAttInfo } from '@/api/contract/manage'
  import UEditor from '@/components/UEditor'
  import {
    getFxsjkDetails,
    getFxsjkspDetails,
    addFxsjData,
    addFxsjspData,
    downFieldById,
  } from '@/api/risk/riskEvents'
  import { formatDay } from '@/utils'
  import { couldMJ, hasMJ } from '@/utils'
  import { getMJ } from '@/api/setting/mjsz'
  export default {
    name: 'EventEdit',
    components: { UEditor },
    inheritAttrs: false,
    props: [],
    data() {
      return {
        activeName: 'first',
        title: '',
        dialogFormVisible: false,
        formData: {
          riskeventcode: undefined,
          riskeventname: undefined,
          occureddepartment: null,
          discovereddate: null,
          occureddate: undefined,
          losseventcategory: undefined,
          riskeventdescription: undefined,
          maxestimateloss: undefined,
          confirmeddirectloss: undefined,
          report: undefined,
          reportdepartment: undefined,
          riskfactor2: undefined,
          recorddepart: undefined,
          field111: null,
          claimdate: null,
          indate: null,
          secrectLevelId: '',
          staffScopeNames: '',
          staffScopeIds: '',
        },
        list: [],
        tableData: [],
        templates: [],
        rules: {
          riskeventcode: [],
          riskeventname: [],
          occureddepartment: [],
          discovereddate: [],
          occureddate: [],
          losseventcategory: [],
          riskeventdescription: [],
          maxestimateloss: [
            {
              required: true,
              message: '请输入最大预估损失汇总金额',
              trigger: 'blur',
            },
          ],
          confirmeddirectloss: [
            {
              required: true,
              message: '请输入已确认的直接损失总金额',
              trigger: 'blur',
            },
          ],
          confirmeddirectlossa: [
            {
              required: true,
              message: '请输入已确认的直接损失净额',
              trigger: 'change',
            },
          ],
        },
        showMJ: false,
        MJoption: [],
      }
    },
    computed: {},
    watch: {
      'formData.content'(val) {
        if (this.$refs['ueditor'].editor.openTemplate) {
          this.$refs['ueditor'].editor.openTemplate = false
          let s = val
          const arr = [
            ['$[contract.contractno]', 'contractno'],
            ['$[contract.contractname]', 'contractname'],
            ['$[contract.contractamount]', 'contractmoney'],
            ['$[contract.contractItem]', 'contractitem'],
            ['$[contract.executor]', 'realname'],
            ['$[contract.rmbinwords]', 'hzsumowing'],

            ['$[counterpart.coupersion]', 'counterpartcode'],
            ['$[counterpart.personincharge]', 'contractbd'],
            ['$[counterpart.counterpartHank]', 'bankkhyh'],
            ['$[counterpart.counumber]', 'counterpartno'],
            ['$[counterpart.couname]', 'budgetname'],
            ['$[counterpart.couaddress]', 'counterpartaddress'],
            ['$[counterpart.coupersion]', 'contacts'],
            ['$[counterpart.contactsPhone]', 'contactsphone'],
            ['$[counterpart.counterpartHankAccount]', 'bankaccount'],
            ['$[counterpart.legarepresentative]', 'contacts'],
            ['$[counterpart.pctelephonenumber]', 'contractzd'],
            // ['$[counterpart.taxpayeridentification]', 'hzsumowing'], //纳税人识别号
          ]
          arr.forEach((i) => {
            if (this.formData[i[1]]) {
              s = s.replace(i[0], this.formData[i[1]])
            }
          })
          this.formData.content = s
        }
      },
    },
    async created() {
      this.showMJ = couldMJ()
      if (this.showMJ) {
        const res = await hasMJ('EventsEventBase')
        this.menuId = res[0].menuid
        const res2 = await getMJ({ rightId: res[0].menuid })
        this.MJoption = res2.data
      }
    },
    mounted() {},
    methods: {
      getMJName(id) {
        const item = this.MJoption.find((item) => item.levelId === id)
        return item ? item.levelName : ''
      },
      /**
       * @description: 预览
       * @return {*}
       */
      async handlePreviewFile(row) {
        console.log('row', row)
        const { data } = await getPrivewAttInfo({
          //此接口通用
          attId: row.attid,
          attType: 2,
        })

        window.open(
          data.previewurl +
            '?url=' +
            encodeURIComponent(Base64.encode(data.ftpUrl))
        )
      },
      handleClick(tab, event) {
        console.log(tab, event)
      },
      save() {},
      /**
       * @description: 初始化
       * @return {*}
       */
      async showRead(row) {
        this.title = '查看'
        const res = await getFxsjkDetails({ eventid: row.riseveid })
        this.formData = res.data.riskevent
        this.formData.discovereddate = formatDay(
          res.data.riskevent.discovereddate
        )
        this.formData.occureddate = formatDay(res.data.riskevent.occureddate)

        this.formData.attachments = res.data.attachments || []
        const res1 = await getFxsjkspDetails({
          riseveid: row.riseveid,
        })
        if (res1.data && res1.data.length) {
          this.formData = { ...this.formData, ...res1.data[0] }
        }
        this.formData.claimdate =
          res1.data && res1.data.length && formatDay(res1.data[0].claimdate)
        this.formData.indate =
          res1.data && res1.data.length && formatDay(res1.data[0].indate)
        // this.form = Object.assign({}, row)
        // this.form.org = '长江集团有限公司'
        // this.form.code = 'XXXXXXXXXX'
        // this.form.name = 'XXXXXXXXXX'
        this.formData.content = res.data.riskevent.content || undefined
        this.dialogFormVisible = true
      },
      /**
       * @description: 关闭页面
       * @return {*}
       */
      close() {
        this.dialogFormVisible = false
      },
      handleSetStaff(row) {
        console.log('set staff', row)
      },
      handleSetWeight(row) {
        console.log('set weight', row)
      },
      /**
       * @description: 附件下载
       * @return {*}
       */
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
      /**
       * @description: 附件删除
       * @return {*}
       */
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg } = await doDelete({ ids: row.id })
          this.$baseMessage(msg, 'success', 'vab-hey-message-success')
          // await this.fetchData()
        })
      },
    },
  }
</script>
<style></style>
