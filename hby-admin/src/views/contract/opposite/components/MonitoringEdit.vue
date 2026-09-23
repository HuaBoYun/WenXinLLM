<template>
  <div>
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
          label-width="120px"
          :model="formData"
          :rules="rules"
          size="medium"
        >
          <el-col :span="12">
            <el-form-item label="公司名称" prop="companyname">
              <el-input
                v-model="formData.companyname"
                clearable
                placeholder="请输入公司名称"
                :style="{ width: '100%' }"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属分组" prop="teamid">
              <el-select
                v-model="formData.teamid"
                clearable
                placeholder="请选择分组"
                :style="{ width: '100%' }"
              >
                <el-option
                  v-for="item in teamList"
                  :key="item.teamid"
                  :label="item.teamname"
                  :value="item.teamid"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险状况" prop="fxtype">
              <el-select
                v-model="formData.fxtype"
                clearable
                placeholder="风险状况"
              >
                <el-option
                  v-for="item in typeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="监控外部数据" prop="priceid">
              <el-checkbox-group v-model="formData.priceid">
                <el-checkbox
                  v-for="item in cjbdiItems"
                  :key="item.priceid"
                  :label="item.priceid"
                >
                  {{ item.interfacename }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" style="display: none;">
            <el-form-item label="监控内部数据" prop="pageid">
              <el-checkbox-group v-model="formData.pageid">
                <el-checkbox
                  v-for="item in internalData"
                  :key="item.pageid"
                  :label="item.pageid"
                >
                  {{ item.pagename }}
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script>
  import { saveCompany, getTeamList } from '@/api/contract/opposite'
  import { saveCjbdiCompanyMonitor } from '@/api/risk/cjbdi'
  import { CJBDI_MONITOR_ITEMS } from '@/config/cjbdi-monitor-items'
  export default {
    name: 'MonitoringEdit',
    components: {},
    inheritAttrs: false,
    props: {
      internalData: {
        type: Array,
        default: () => [],
      },
      externalData: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        title: '',
        dialogFormVisible: false,
        // cjbdi_02 企业名录通用版由 save() 自动注入，不在勾选项里展示
        cjbdiItems: CJBDI_MONITOR_ITEMS.filter(item => item.priceid !== 'cjbdi_02'),
        teamList: [], // 分组列表
        formData: {
          teamid: undefined,
          companyid: undefined,
          companyname: undefined,
          creditCode: undefined, // 统一社会信用代码
          legalPerson: undefined, // 法定代表人
          entStatus: undefined, // 企业状态
          regCap: undefined, // 注册资本
          fxtype: undefined,
          priceid: [],
          pageid: [],
          // 企业详细信息（用于保存到数据库）
          enterpriseInfo: {},
        },
        list: [],
        tableData: [],
        rules: {
          companyname: [
            {
              required: true,
              message: '请输入公司名称',
              trigger: 'blur',
            },
          ],
          teamid: [
            {
              required: true,
              message: '请选择所属分组',
              trigger: 'change',
            },
          ],
          fxtype: [
            {
              required: true,
              message: '请选择风险状况',
              trigger: 'blur',
            },
          ],
          priceid: [
            {
              required: true,
              message: '请选择监控外部数据',
              trigger: 'change',
            },
          ],
          pageid: [
            {
              required: false, // 内部数据不再必填
              message: '请选择监控内部数据',
              trigger: 'change',
            },
          ],
        },
        typeOptions: [
          {
            label: '特别预警',
            value: '特别预警',
          },
          {
            label: '一般预警',
            value: '一般预警',
          },
          {
            label: '关注',
            value: '关注',
          },
          {
            label: '正常',
            value: '正常',
          },
        ],
      }
    },
    computed: {},
    watch: {},
    created() {},
    mounted() {
      this.loadTeamList()
    },
    methods: {
      // 加载分组列表
      async loadTeamList() {
        try {
          const { data: { teams } } = await getTeamList()
          this.teamList = teams || []
        } catch (e) {
          console.error('加载分组列表失败', e)
        }
      },
      showEdit(teamid) {
        this.formData.teamid = teamid
        this.dialogFormVisible = true
      },
      // 新增方法：带企业信息的打开
      showEditWithCompany(teamid, companyData) {
        this.formData.teamid = teamid || undefined
        this.formData.companyname = companyData.companyname
        this.formData.creditCode = companyData.creditCode
        this.formData.legalPerson = companyData.legalPerson || ''
        this.formData.entStatus = companyData.entStatus || companyData.status || ''
        this.formData.regCap = companyData.regCap || ''
        
        // 保存完整的企业信息
        this.formData.enterpriseInfo = { ...companyData }
        
        // 默认不勾选任何监控项，由用户自行选择
        // 注意：cjbdi_02（企业名录通用版）不需要用户勾选，保存时会自动添加
        this.formData.priceid = []
        this.formData.pageid = []
        
        this.dialogFormVisible = true
      },
      save() {
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            const { pageid, priceid, enterpriseInfo, ...rest } = this.formData
            
            // 自动添加企业名录通用版（cjbdi_02），因为已经查询过企业信息
            let finalPriceIds = [...priceid]
            if (!finalPriceIds.includes('cjbdi_02')) {
              finalPriceIds.unshift('cjbdi_02') // 添加到开头
            }
            
            // 组装保存数据
            const saveData = {
              pageid: pageid && pageid.length > 0 ? pageid.join(',') : '',
              priceid: finalPriceIds.join(','),
              ...rest,
              // 传递企业详细信息，用于保存到数据库
              ...enterpriseInfo,
            }
            
            console.log('保存数据：', saveData)
            
            try {
              // 调用新的CJBDI保存接口（使用统一社会信用代码作为COMPANYID）
              const res = await saveCjbdiCompanyMonitor(saveData)
              console.log('保存结果：', res)
              
              if (res.code === 200) {
                this.$baseMessage('保存成功', 'success', 'vab-hey-message-success')
                this.$emit('fetch-data')
                this.close()
              } else {
                this.$message.error(res.msg || '保存失败')
              }
            } catch (e) {
              console.error('保存企业监控失败', e)
              this.$message.error('保存失败：' + (e.message || '未知错误'))
            }
          }
        })
      },
      close() {
        this.$refs['form'].resetFields()
        this.formData = this.$options.data().formData
        this.dialogFormVisible = false
      },
    },
  }
</script>
<style></style>
