<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel>
        <el-form
          ref="form"
          :inline="true"
          label-width="0"
          :model="queryForm"
          @submit.native.prevent
        >
          <el-form-item>
            <el-select
              v-model="queryForm.moduletype"
              placeholder="请选择模块"
              @change="fetchData"
            >
              <!-- <el-option label="系统设置" value="xtsz" />
              <el-option label="合同管理" value="htgl" /> -->
              <!-- <el-option label="法务管理" value="fwgl" />
              <el-option label="智能监控" value="znjk" />
              <el-option label="内控合规" value="nkhg" />
              <el-option label="内部审计" value="znsj" />
              <el-option label="智能分析" value="znfx" />
              <el-option label="风险管控" value="fxgk" /> -->
              <el-option
                v-for="item in moduleLists"
                :key="item.id"
                :label="item.projectName"
                :value="item.uniqueIdentification"
              />
            </el-select>
          </el-form-item>
        </el-form>
      </vab-query-form-left-panel>
      <vab-query-form-right-panel>
        <el-button
          v-if="hasAuth('SettingAuthmMenuAdd')"
          type="success"
          @click="handleAdd"
        >
          添加
        </el-button>
        <!-- <el-button
          v-if="hasAuth('SettingAuthmMenuAdd')"
          type="success"
          @click="batchDeal"
        >
          批量
        </el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>
    <el-table
      v-loading="listLoading"
      :data="list"
      row-key="id"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column label="菜单名称" min-width="100" prop="name" />
      <el-table-column label="类型" min-width="60" prop="type">
        <template slot-scope="{ row }">
          {{
            row.type === 0
              ? '目录'
              : row.type === 1
              ? '页面'
              : row.type === 2
              ? '操作'
              : ''
          }}
        </template>
      </el-table-column>
      <el-table-column label="图标" min-width="60" prop="icon">
        <template slot-scope="{ row }">
          <vab-icon
            v-if="row.type !== 2"
            :icon="row.icon"
            :is-custom-svg="true"
          />
        </template>
      </el-table-column>
      <el-table-column label="排序" min-width="60" prop="sort" />
      <el-table-column label="权限标识" min-width="100" prop="perms" />
      <el-table-column label="组件地址" min-width="100" prop="component" />
      <el-table-column label="状态" min-width="70" prop="visible">
        <template slot-scope="{ row }">
          {{ row.visible === 1 ? '正常' : row.visible === 0 ? '禁用' : '' }}
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="120" prop="name">
        <template slot-scope="{ row }">
          <el-button
            size="mini"
            type="text"
            @click="handleShowDialog(row)"
            v-if="hasAuth('CDGLedit')"
          >
            修改
          </el-button>
          <el-button
            size="mini"
            :style="
              row.visible === 1
                ? 'color: #F16060;'
                : row.visible === 0
                ? 'color: #71D965;'
                : ''
            "
            type="text"
            @click="handleUpdStatus(row)"
            v-if="hasAuth('CDGLstatus')"
          >
            {{ row.visible === 1 ? '禁用' : row.visible === 0 ? '启用' : '' }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleDelete(row)"
            v-if="hasAuth('CDGLdelete')"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <CategoryEdit
      ref="edit"
      @fetch-data="fetchData"
      :key="queryForm.moduletype"
    />
    <ProcessList ref="process" @fetchData="fetchData" />
  </div>
</template>

<script>
  import { delAuthInfo, getAuthList, updAuthsStatus } from '@/api/setting/auths'
  import CategoryEdit from './components/CategoryEdit.vue'
  // import { hasAuth } from '@/utils'
  import { getModuleList } from '@/api/setting/system'
  import { saveAuthList } from '@/api/setting/auths.js'
  import { getFlowList } from '@/api/setting/auth'
  import ProcessList from '@/views/contract/contractManage/components/ProcessList.vue'
  export default {
    name: 'Download',
    components: { CategoryEdit, ProcessList },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          moduletype: 'xtsz',
          judge: 1,
        },
        moduleLists: [],
        requireValuedata: false,
      }
    },
    created() {
      this.fetchData()
      this.moduleList()
      const info = JSON.parse(localStorage.getItem('userInfo'))
      // 判断是否需要流程校验
      if (info.requireValuedata) {
        this.requireValuedata = info.requireValuedata
      }
    },
    methods: {
      batchDeal() {
        const icons = [
          'menu-account',
          'menu-add',
          'menu-amount',
          'menu-apply',
          'menu-apply2',
          'menu-assign',
          'menu-auth',
          'menu-balance',
          'menu-banben',
          'menu-baobiao',
          'menu-bg',
          'menu-bgb',
          'menu-bill',
          'menu-bj',
          'menu-black',
          'menu-bug',
          'menu-card',
          'menu-category',
          'menu-chat',
          'menu-checklist',
          'menu-comment',
          'menu-company',
          'menu-contract',
          'menu-course',
          'menu-customer',
          'menu-data',
          'menu-dj',
          'menu-down',
          'menu-downdata',
          'menu-edit',
          'menu-fa',
          'menu-fangan',
          'menu-flfw',
          'menu-flow',
          'menu-fuzhu',
          'menu-fw',
          'menu-fx',
          'menu-genzhong',
          'menu-gw',
          'menu-gys',
          'menu-handle',
          'menu-home',
          'menu-import',
          'menu-init',
          'menu-ja',
          'menu-jf',
          'menu-jh',
          'menu-jizhang',
          'menu-jk',
          'menu-kemu',
          'menu-kong',
          'menu-libray',
          'menu-line',
          'menu-listing',
          'menu-lixiang',
          'menu-login',
          'menu-ls',
          'menu-metting',
          'menu-mingxi',
          'menu-mm',
          'menu-mode',
          'menu-model',
          'menu-monitor',
          'menu-no',
          'menu-notice',
          'menu-org',
          'menu-pay',
          'menu-phone',
          'menu-pingzheng',
          'menu-pointer',
          'menu-position',
          'menu-px',
          'menu-qeustion',
          'menu-ready',
          'menu-result',
          'menu-risk',
          'menu-risklist',
          'menu-rule',
          'menu-scan',
          'menu-search',
          'menu-set',
          'menu-setlist',
          'menu-shijian',
          'menu-sign',
          'menu-ss',
          'menu-standard',
          'menu-star',
          'menu-submit',
          'menu-task',
          'menu-test',
          'menu-theme',
          'menu-topup',
          'menu-user',
          'menu-work',
          'menu-xd',
          'menu-xies',
          'menu-yewu',
          'menu-yingyong',
          'menu-yz',
          'menu-zb',
          'menu-zhuanjia',
          'menu-zip',
          'menu-zongzhang',
          'menu-zwgz',
          'menu-zxt',
          'menu-zy',
          'menu-zz',
        ]
        const arr = [
          { name: '应用模型-全局', key: 'yymxqj', sort: 1 },
          { name: '套表管理-全局', key: 'btglqj', sort: 2 },
          { name: '套表设计', key: 'tbsj', sort: 3 },
          { name: '业务规则-全局', key: 'ywgzqj', sort: 4 },
          { name: '语义模型取数-全局', key: 'yymxqsqj', sort: 5 },
          { name: '控制策略', key: 'kzcl', sort: 6 },
          { name: '控制规则-全局', key: 'kzgzqj', sort: 7 },
          { name: '零预算规则-全局', key: 'lysgzqj', sort: 8 },
          { name: '任务管理·全局', key: 'rwglqj', sort: 9 },
          { name: '应用发布', key: 'yyfb', sort: 10 },
          { name: '预算编制', key: 'ysbz', sort: 11 },
          { name: '日常执行', key: 'rczx', sort: 12 },
          { name: '直接调整', key: 'zjtz', sort: 13 },
          { name: '局部调整', key: 'jbtz', sort: 14 },
          { name: '预算调剂', key: 'ystj', sort: 15 },
          { name: '调整单管理', key: 'tzdgl', sort: 16 },
          { name: '预算预审批', key: 'ysysp', sort: 17 },
          { name: '预算审批', key: 'yssp', sort: 18 },
          { name: '报送管理', key: 'bsgl', sort: 19 },
          { name: '控制方案', key: 'kzfa', sort: 20 },
          { name: '计算监控台', key: 'jsjkt', sort: 21 },
          { name: '折算方案', key: 'zsfa', sort: 22 },
          { name: '折算执行', key: 'zszx', sort: 23 },
          { name: '折算数据中心', key: 'zssjzx', sort: 24 },
          { name: '预算查阅', key: 'yscy', sort: 25 },
          { name: '版本查询', key: 'bbcx', sort: 26 },
          { name: '分析查询', key: 'fxcx', sort: 27 },
          { name: '目标系统管理', key: 'mbxtgl', sort: 28 },
          { name: '数据传输', key: 'sjcs', sort: 29 },
          { name: '传输数据管理', key: 'cssjgl', sort: 30 },
          { name: '传输数据日志', key: 'cssjrz', sort: 31 },
          { name: '数据导入', key: 'sjdr', sort: 32 },
          { name: '导入数据查询', key: 'drsjcx', sort: 33 },
          // { name: '应收票据余额表', key: 'yspjyeb', sort: 34, },
          // { name: '应付票据余额表', key: 'yfpjyeb', sort: 35, },
          // { name: '应收票据到期分析', key: 'yspjdqfx', sort: 36, },
          // { name: '应付票据到期分析', key: 'yfpjdqfx', sort: 37, },
          // { name: '外部贴现票据统计', key: 'wbtxpjtj', sort: 38, },
          // { name: '票据入池', key: 'pjrc', sort: 39, },
          // { name: '票据出池', key: 'pjcc', sort: 40, },
          // { name: '池内贴现申请', key: 'cntxsq', sort: 41, },
          // { name: '池内贴现', key: 'cntx', sort: 42, },
          // { name: '池内质押', key: 'cnzy', sort: 43, },
          // { name: '池内背书', key: 'cnbs', sort: 44, },
          // { name: '池内托收', key: 'cnst', sort: 45, },
          // { name: '票据池额度汇总', key: 'pjcedhz', sort: 46, },
          // { name: '票据池额度明细表', key: 'pjcedmxb', sort: 47, },
          // { name: '质押池额度使用汇总', key: 'zycedsyhz', sort: 48, },
          // { name: '质押池额度使用明细表', key: 'zycedsymxb', sort: 49, },
          // { name: '额度申请', key: 'edsq', sort: 50, },
          // { name: '额度管理', key: 'edgl', sort: 51, },
          // { name: '票据上收申请', key: 'pjsssq', sort: 52, },
          // { name: '票据上收', key: 'pjss', sort: 53, },
          // { name: '票据上收回单', key: 'pjsshd', sort: 54, },
          // { name: '票据下拨申请', key: 'pjxbsq', sort: 55, },
          // { name: '票据下拨', key: 'pjxb', sort: 56, },
          // { name: '票据下拨回单', key: 'pjxbhd', sort: 57, },
          // { name: '内部托管', key: 'nbtg', sort: 58, },
          // { name: '内部托管办理', key: 'nbtgbl', sort: 59, },
          // { name: '内部领用', key: 'nbly', sort: 60, },
          // { name: '内部领用办理', key: 'nblybl', sort: 61, },
          // { name: '内部转让', key: 'nbzr', sort: 62, },
          // { name: '内部转让办理', key: 'nbzrbl', sort: 63, },
          // { name: '票据调剂', key: 'pjtj', sort: 64, },
          // { name: '票据调剂回单', key: 'pjtjhd', sort: 65, },
          // { name: '清算单', key: 'qsd', sort: 66, },
          // { name: '清算回单', key: 'qshd', sort: 67, },
          // { name: '占用费率设置', key: 'zyflsz', sort: 68, },
          // { name: '使用费率设置', key: 'syflsz', sort: 69, },
          // { name: '占用费计算', key: 'zyfjs', sort: 70, },
          // { name: '使用费计算', key: 'syfjs', sort: 71, },
          // { name: '费用清单', key: 'fyqd', sort: 72, },
          // { name: '单位费用清单', key: 'dwfyqd', sort: 73, },
          // { name: '费用差额汇总', key: 'fycehz', sort: 74, },
          // { name: '开证申请', key: 'kzsq', sort: 75, },
          // { name: '开证登记', key: 'kzdj', sort: 76, },
          // { name: '开证修改', key: 'kzxg', sort: 77, },
          // { name: '到单承付', key: 'ddcf', sort: 78, },
          // { name: '付款登记', key: 'fkdj', sort: 79, },
          // { name: '开证查询', key: 'kzcx', sort: 80, },
          // { name: '收证登记', key: 'szdj', sort: 81, },
          // { name: '收证修改', key: 'szxg', sort: 82, },
          // { name: '交单登记', key: 'jddj', sort: 83, },
          // { name: '通知收款', key: 'tzsk', sort: 84, },
          // { name: '收证查询', key: 'szcx', sort: 85, },
          // { name: '押汇申请', key: 'yhsq', sort: 86, },
          // { name: '押汇合同', key: 'yhht', sort: 87, },
          // { name: '押汇放款', key: 'yhfk', sort: 88, },
          // { name: '押汇还款', key: 'yhhk', sort: 89, },
        ]

        arr.map((x) => {
          const randomNum = Math.ceil(Math.random() * 100)
          saveAuthList({
            name: x.name,
            type: 1,
            parent: 675,
            sort: x.sort,
            perms: x.key,
            path: x.key,
            component: `@/views/managementAccounting/qmys/${x.key}.vue`,
            islink: 0,
            visible: 1,
            icon: icons[randomNum],
            moduletype: 'cwy',
            secrectLevelId: 625874086133829,
          })
        })
      },
      async fetchData() {
        this.listLoading = true
        const { data } = await getAuthList(this.queryForm)
        if (data.rightList) {
          this.list = data.rightList
        } else {
          this.list = []
        }
        console.log(this.list)
        this.listLoading = false
      },
      handleAdd() {
        this.$refs['edit'].showEdit('', this.queryForm.moduletype)
      },
      handleShowDialog(row) {
        this.$refs['edit'].showEdit(row, this.queryForm.moduletype)
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          const { msg, data } = await delAuthInfo({ rightId: row.id })
          // 流程校验
          if (this.requireValuedata) {
            //  查询当前是否有流程
            getFlowList({
              targetId: data.recordId,
              targetType: 'right',
              operationType: 3,
            }).then((res) => {
              if (res.data == 0) {
                // 可以提交流程
                this.$refs['process'].save(220, data.recordId)
                this.$baseMessage(
                  '审批流程提交成功,请等待审批',
                  'success',
                  'vab-hey-message-success'
                )
                this.close()
              } else {
                // 不可以提交流程
                this.$baseMessage(
                  '当前用户流程已存在,请先走审批流程',
                  'error',
                  'vab-hey-message-error'
                )
                return
              }
            })
          } else {
            this.$baseMessage(msg, 'success', 'vab-hey-message-success')
            await this.fetchData()
          }
        })
      },
      handleUpdStatus(row) {
        this.$baseConfirm(
          `你确定要${row.visible === 1 ? '禁用' : '启用'}当前项吗`,
          null,
          async () => {
            const { msg, data } = await updAuthsStatus({
              rightId: row.id,
              visible: row.visible === 0 ? 1 : 0,
            })
            // 流程校验
            if (this.requireValuedata) {
              //  查询当前是否有流程
              getFlowList({
                targetId: data.data.recordId,
                targetType: 'right',
                operationType: row.visible == 1 ? 5 : 4,
              }).then((res) => {
                if (res.data == 0) {
                  // 可以提交流程
                  this.$refs['process'].save(220, data.data.recordId)
                  this.$baseMessage(
                    '审批流程提交成功,请等待审批',
                    'success',
                    'vab-hey-message-success'
                  )
                  this.close()
                } else {
                  // 不可以提交流程
                  this.$baseMessage(
                    '当前用户流程已存在,请先走审批流程',
                    'error',
                    'vab-hey-message-error'
                  )
                  return
                }
              })
            } else {
              this.$baseMessage(msg, 'success', 'vab-hey-message-success')
              await this.fetchData()
            }
          }
        )
      },
      sendModel() {
        this.$refs['sendModel'].showEdit()
      },
      send() {
        this.$refs['send'].showEdit()
      },
      moduleList() {
        getModuleList({}).then((res) => {
          this.moduleLists = res.data || []
        })
      },
    },
  }
</script>
