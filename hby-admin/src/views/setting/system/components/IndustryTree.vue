<!--
 * @Date: 2022-05-05 09:59:10
 * @LastEditors: zengping.liu
 * @LastEditTime: 2022-05-05 14:08:12
 * @FilePath: /hb-admin/src/views/setting/system/components/IndustryTree.vue
-->
<template>
  <el-form ref="form" label-width="140px" :model="formData">
    <el-form-item label-width="0">
      <el-cascader
        v-model="queryData.orgid"
        v-loading="loadingOptions"
        :options="optionsData"
        :props="{ value: 'orgid', label: 'orgname' }"
        @change="handleChange"
      />
    </el-form-item>
    <el-form-item label-width="0">
      <el-tree
        v-loading="loadingTree"
        :data="treeData"
        default-expand-all
        highlight-current
        :props="defaultProps"
        @node-click="handleNodeClick"
      />
    </el-form-item>
  </el-form>
</template>

<script>
  import { copyToIndustry } from '@/api/setting/system'

  export default {
    name: 'CopyToIndustry',
    props: {
      formData: { type: Object, default: () => {} },
      selectedid: { type: [Number, String], default: () => undefined },
    },
    data() {
      return {
        loadingOptions: false,
        loadingTree: false,
        title: '',
        queryData: {
          selectedid: this.selectedid,
          orgid: undefined,
        },
        // formData: {
        //   selectedId: undefined,
        //   faflowid: undefined,
        //   flownumber: undefined,
        //   orgid: undefined,
        // },
        optionsData: [],
        treeData: [],
        defaultProps: {
          children: 'children',
          // label: 'text',
          // value: 'id',
        },
        currentNode: undefined,
      }
    },
    methods: {
      listToTree(list, key = 'father_id', subkey = 'id') {
        let map = {},
          outData = []
        list.map((item, index) => {
          //
          map[list[index][subkey]] = index // 初始化map
          list[index].children = [] // 初始化children
        })
        list.map((item) => {
          if (item[key] !== -1) {
            // 检查 map[item[key]] 是否存在，以判断悬垂的分支（父id不存在的分支）
            // 出现此问题一般是由于父id已删除，但是没有删除掉其子id
            if (map[item[key]] === undefined) {
            } else {
              list[map[item[key]]].children.push(item)
            }
          } else {
            outData.push(item)
          }
        })
        return outData
      },
      removeEmptyChildren(data) {
        for (var i = 0; i < data.length > 0; i++) {
          if (data[i].children == null || data[i].children.length <= 0) {
            // children若为空数组，则将children设为undefined
            data[i].children = undefined
          } else {
            // children若不为空数组，则继续 递归调用 本方法
            this.removeEmptyChildren(data[i].children)
          }
        }
        return data
      },
      formatList(list) {
        // const source =
        // 'tree.nodes[0_111323]=text:信息系统;method:check(111323,0);tree.nodes[0_111311]=text:人力资源;method:check(111311,0);tree.nodes[0_111317]=text:研究与开发;method:check(111317,0);tree.nodes[0_111316]=text:销售管理;method:check(111316,0);tree.nodes[0_111324]=text:内部监督;method:check(111324,0);tree.nodes[0_111312]=text:安全管理;method:check(111312,0);tree.nodes[0_111319]=text:生产管理;method:check(111319,0);tree.nodes[0_111321]=text:全面预算;method:check(111321,0);tree.nodes[0_111325]=text:质量管理;method:check(111325,0);tree.nodes[0_111310]=text:组织架构;method:check(111310,0);tree.nodes[0_111318]=text:工程项目;method:check(111318,0);tree.nodes[0_111313]=text:资金活动;method:check(111313,0);tree.nodes[0_111315]=text:资产管理;method:check(111315,0);tree.nodes[0_111320]=text:财务报告;method:check(111320,0);tree.nodes[0_113107]=text:采购与付款;method:check(113107,0);tree.nodes[0_128195]=text:审计教学;method:check(128195,0);tree.nodes[0_111322]=text:合同管理;method:check(111322,0);tree.nodes[0_129854]=text:安全管理;method:check(129854,0);tree.nodes[0_111314]=text:采购管理;method:check(111314,0);tree.nodes[0_137225]=text:固定置产管理;method:check(137225,0);tree.nodes[0_115794]=text:设计管理;method:check(115794,0);tree.nodes[-1_0]=text:业务创建;method:check(0,-1);tree.nodes[111310_111326]=text:组织架构管理;method:check(111326,111310);tree.nodes[111311_111327]=text:员工招聘与退出;method:check(111327,111311);tree.nodes[111311_111328]=text:员工薪酬管理;method:check(111328,111311);tree.nodes[111312_111329]=text:安全管理计划;method:check(111329,111312);tree.nodes[111312_111330]=text:安全事故管理;method:check(111330,111312);tree.nodes[111313_111331]=text:筹资活动管理;method:check(111331,111313);tree.nodes[111313_111332]=text:投资活动管理;method:check(111332,111313);tree.nodes[111313_111333]=text:营运资金管理;method:check(111333,111313);tree.nodes[111314_111334]=text:采购业务管理;method:check(111334,111314);tree.nodes[111315_111335]=text:固定资产管理;method:check(111335,111315);tree.nodes[111315_111336]=text:无形资产管理;method:check(111336,111315);tree.nodes[111316_111338]=text:投标管理;method:check(111338,111316);tree.nodes[111316_111339]=text:销售物流管理;method:check(111339,111316);tree.nodes[111316_111340]=text:国际业务;method:check(111340,111316);tree.nodes[111316_111341]=text:客户关系管理;method:check(111341,111316);tree.nodes[111317_111343]=text:新产品研制开发;method:check(111343,111317);tree.nodes[111318_111344]=text:工程项目管理;method:check(111344,111318);tree.nodes[111319_111345]=text:生产管理;method:check(111345,111319);tree.nodes[111320_111346]=text:报告编制管理;method:check(111346,111320);tree.nodes[111321_111347]=text:预算编制管理;method:check(111347,111321);tree.nodes[111321_111348]=text:预算执行管理;method:check(111348,111321);tree.nodes[111322_111349]=text:合同管理;method:check(111349,111322);tree.nodes[111323_111350]=text:系统运维;method:check(111350,111323);tree.nodes[111324_111351]=text:内部控制;method:check(111351,111324);tree.nodes[111324_111352]=text:内部审计;method:check(111352,111324);tree.nodes[111325_111353]=text:产品质量;method:check(111353,111325);tree.nodes[111316_111425]=text:销售预测管理;method:check(111425,111316);tree.nodes[111314_113153]=text:岗位及权限;method:check(113153,111314);tree.nodes[111314_113155]=text:供货商管理;method:check(113155,111314);tree.nodes[115794_115795]=text:初步设计;method:check(115795,115794);tree.nodes[128195_128197]=text:组织管理;method:check(128197,128195);tree.nodes[128195_128198]=text:行政管理;method:check(128198,128195);tree.nodes[128195_129820]=text:安全管理;method:check(129820,128195);tree.nodes[111312_129825]=text:安全生产管理;method:check(129825,111312);tree.nodes[111312_129859]=text:风险点;method:check(129859,111312);tree.nodes[129854_129862]=text:安全管理;method:check(129862,129854);tree.nodes[129854_129866]=text:安全管理流程;method:check(129866,129854);tree.nodes[137225_137226]=text:固定资产取得;method:check(137226,137225);tree.nodes[0_699759]=text:地质勘探;method:check(699759,0);tree.nodes[0_715148]=text:内部监管;method:check(715148,0);tree.nodes[111323_733781]=text:信息运维;method:check(733781,111323);tree.nodes[111321_736140]=text:决算管理;method:check(736140,111321);tree.nodes[0_775638]=text:表单添加;method:check(775638,0);tree.nodes[775638_775639]=text:表单添加003;method:check(775639,775638);'
        // const list = source.split(';').filter((item) => item.indexOf('tree.nodes') > -1)
        // 源数据格式
        // "tree":"tree.nodes['-1_0']=\"text:行业流程;method:check(0,-1)\";\ntree.nodes['0_207515']=\"text:建材采购;method:check(207515,0)\";\ntree.nodes['0_685542']=\"text:资产控制;method:check(685542,0)\";\ntree.nodes['0_685540']=\"text:预算业务控制;method:check(685540,0)\";\ntree.nodes['0_685541']=\"text:收支业务控制;method:check(685541,0)\";\ntree.nodes['685540_685613']=\"text:预算追加与调整;method:check(685613,685540)\";\ntree.nodes['685540_685616']=\"text:决算管理;method:check(685616,685540)\";\ntree.nodes['685540_685619']=\"text:预算绩效;method:check(685619,685540)\";\ntree.nodes['685541_685622']=\"text:经费报销流程图;method:check(685622,685541)\";\ntree.nodes['685541_685625']=\"text:差旅费管理流程图;method:check(685625,685541)\";\ntree.nodes['685541_685628']=\"text:经费支出申请;method:check(685628,685541)\";\ntree.nodes['685541_685631']=\"text:经费支出报销;method:check(685631,685541)\";\ntree.nodes['685541_685634']=\"text:资金支付-零余额账户支付;method:check(685634,685541)\";\ntree.nodes['685541_685637']=\"text:非税收入管理;method:check(685637,685541)\";\ntree.nodes['685542_685640']=\"text:合同管理流程;method:check(685640,685542)\";\ntree.nodes['685542_685643']=\"text:固定资产验收流程图;method:check(685643,685542)\";\ntree.nodes['685542_685649']=\"text:资产购置;method:check(685649,685542)\";\ntree.nodes['685542_685652']=\"text:资产出租出借;method:check(685652,685542)\";\ntree.nodes['685542_685655']=\"text:资产处置;method:check(685655,685542)\";\ntree.nodes['685544_685658']=\"text:合同管理流程;method:check(685658,685544)\";\ntree.nodes['685544_685661']=\"text:合同管理流程;method:check(685661,685544)\";\ntree.nodes['685544_685664']=\"text:合同订立;method:check(685664,685544)\";\ntree.nodes['685544_685667']=\"text:合同履行与归档;method:check(685667,685544)\";\ntree.nodes['685544_685670']=\"text:合同纠纷处理;method:check(685670,685544)\";\ntree.nodes['685544_685673']=\"text:合同变更与解除;method:check(685673,685544)\";\ntree.nodes['685545_685676']=\"text:政府采购子流程;method:check(685676,685545)\";\ntree.nodes['685545_685679']=\"text:公开招标采购子流程;method:check(685679,685545)\";\ntree.nodes['685545_685682']=\"text:协议采购子流程;method:check(685682,685545)\";\ntree.nodes['685545_685685']=\"text:自行采购子流程;method:check(685685,685545)\";\ntree.nodes['685545_685688']=\"text:政府采购预算编制与调整;method:check(685688,685545)\";\ntree.nodes['685545_685691']=\"text:政府采购;method:check(685691,685545)\";\ntree.nodes['685546_685694']=\"text:项目规划研究与审批;method:check(685694,685546)\";\ntree.nodes['685546_685697']=\"text:项目过程管理;method:check(685697,685546)\";\ntree.nodes['685546_685700']=\"text:项目进度结算审批;method:check(685700,685546)\";\ntree.nodes['685546_685703']=\"text:维修项目竣工验收;method:check(685703,685546)\";\ntree.nodes['685546_685706']=\"text:维修申请与审批;method:check(685706,685546)\";\ntree.nodes['685546_685709']=\"text:项目验收与结算;method:check(685709,685546)\";\ntree.nodes['0_685543']=\"text:建设项目控制;method:check(685543,0)\";\ntree.nodes['0_685544']=\"text:合同控制;method:check(685544,0)\";\ntree.nodes['0_685545']=\"text:政府采购业务控制;method:check(685545,0)\";\ntree.nodes['0_685546']=\"text:工程控制;method:check(685546,0)\";\ntree.nodes['685540_685595']=\"text:预算批复;method:check(685595,685540)\";\ntree.nodes['685540_685589']=\"text:预算编制;method:check(685589,685540)\";\ntree.nodes['685540_685592']=\"text:预算预下达;method:check(685592,685540)\";\ntree.nodes['685540_685598']=\"text:预算执行;method:check(685598,685540)\";\ntree.nodes['685540_685601']=\"text:对应分配发布环节;method:check(685601,685540)\";\ntree.nodes['685540_685604']=\"text:借款报销及支付审核子流程;method:check(685604,685540)\";\ntree.nodes['685540_685607']=\"text:预算编审与批复;method:check(685607,685540)\";\ntree.nodes['685540_685610']=\"text:预算执行与监控;method:check(685610,685540)\";\ntree.nodes['685546_792564']=\"text:aaa;method:check(792564,685546)\";\ntree.nodes['685542_734463']=\"text:1222;method:check(734463,685542)\";\ntree.nodes['685542_699760']=\"text:法务管理;method:check(699760,685542)\";\ntree.nodes['685542_699762']=\"text:法务管理;method:check(699762,685542)\";\ntree.nodes['685540_699786']=\"text:合同管理;method:check(699786,685540)\";\ntree.nodes['685542_699780']=\"text:合同管理;method:check(699780,685542)\";\ntree.nodes['685542_699782']=\"text:纪检管理;method:check(699782,685542)\";\ntree.nodes['207515_746040']=\"text:投标管理;method:check(746040,207515)\";\ntree.nodes['207515_746100']=\"text:普通招聘;method:check(746100,207515)\";\ntree.nodes['207515_792598']=\"text:11;method:check(792598,207515)\";\n",
        const flat = list.map((item) => {
          let res = item.match(/\[(.+?)\]/g)
          let re = res[0]
          let a = re.substring(2, re.length - 2)
          let [pid, id] = a.split('_')
          let text = item.substring(item.indexOf(':') + 1)
          return {
            pid,
            id,
            text,
          }
        })

        let arr = [
          {
            pid: -1,
            id: 0,
            value: 0,
            label: '行业流程',
            children: [],
          },
        ]

        for (let i = 0; i < flat.length; i++) {
          const item = flat[i]
          const pitem = arr[0]
          if (item.pid == -1) continue
          if (item.pid == 0) {
            pitem.children.push({
              pid: item.pid,
              id: item.id,
              value: item.id,
              label: item.text,
              children: [],
            })
          }
        }

        for (let j = 0; j < arr[0].children.length; j++) {
          const pitem = arr[0].children[j]
          for (let i = 0; i < flat.length; i++) {
            const item = flat[i]
            if (!pitem.children) pitem.children = []
            if (item.pid == pitem.id) {
              pitem.children.push({
                pid: item.pid,
                id: item.id,
                value: item.id,
                label: item.text,
              })
            }
          }
        }
        return arr
      },
      async fetchTree() {
        this.loadingOptions = true
        const { code, data } = await copyToIndustry(this.queryData)
        this.loadingOptions = false
        if (code == 1) {
          const list = data.orgTree
          const treeData = this.listToTree(list, 'fatherorgid', 'orgid')
          this.optionsData = this.removeEmptyChildren(treeData)
        }
      },
      async fetchTree2() {
        this.loadingTree = true
        const { code, data } = await copyToIndustry(this.queryData)
        this.loadingTree = false
        if (code == 1) {
          const filteredList = data.tree
            .split(';')
            .filter((item) => item.indexOf('tree.nodes') > -1)
          const treeData = this.formatList(filteredList)
          // const treeData = this.listToTree(list, 'pid', 'id')
          this.treeData = treeData
        }
      },
      handleNodeClick(data) {
        this.currentNode = data
        const res = Object.assign({}, this.formData)
        res.orgid = data.id
        // this.formData.orgid = data.id
        this.$emit('selected', res)
      },
      handleChange(data) {
        this.queryData.orgid = data[1]
        this.fetchTree2()
      },
    },
  }
</script>
