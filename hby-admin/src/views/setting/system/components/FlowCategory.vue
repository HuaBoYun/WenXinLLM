<template>
  <div>
    <div class="top-action">
      <el-button
        size="mini"
        type="success"
        @click="$refs.categoryAdd.showEdit(currentNode, 'add')"
        v-if="hasAuth('XTYWCJtreeAdd')"
      >
        新建
      </el-button>
      <el-button
        size="mini"
        type="primary"
        @click="$refs.categoryAdd.showEdit(currentNode, 'update')"
        v-if="hasAuth('XTYWCJtreeEdit')"
      >
        修改
      </el-button>
      <el-button
        size="mini"
        type="danger"
        @click="$refs.categoryAdd.deleteNode(currentNode)"
        v-if="hasAuth('XTYWCJtreeDelete')"
      >
        删除
      </el-button>
    </div>
    <el-tree
      ref="tree"
      v-loading="loading"
      :data="data"
      :default-checked-keys="[0]"
      :default-expanded-keys="[0]"
      :expand-on-click-node="false"
      highlight-current
      node-key="id"
      :props="defaultProps"
      @node-click="handleNodeClick"
    />
    <flow-category-add ref="categoryAdd" @fetch-tree="fetchTree" />
  </div>
</template>

<script>
  import FlowCategoryAdd from '@/views/setting/system/components/FlowCategoryAdd'
  import { getBusinessTree } from '@/api/setting/system'
  import { hasAuth } from '@/utils'

  export default {
    name: 'FlowCategory',
    components: { FlowCategoryAdd },
    data() {
      return {
        loading: false,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        data: [],
        currentNode: {},
      }
    },
    created() {
      this.fetchTree()
    },
    methods: {
      formatTree(list) {
        // const source =
        // 'tree.nodes[0_111323]=text:信息系统;method:check(111323,0);tree.nodes[0_111311]=text:人力资源;method:check(111311,0);tree.nodes[0_111317]=text:研究与开发;method:check(111317,0);tree.nodes[0_111316]=text:销售管理;method:check(111316,0);tree.nodes[0_111324]=text:内部监督;method:check(111324,0);tree.nodes[0_111312]=text:安全管理;method:check(111312,0);tree.nodes[0_111319]=text:生产管理;method:check(111319,0);tree.nodes[0_111321]=text:全面预算;method:check(111321,0);tree.nodes[0_111325]=text:质量管理;method:check(111325,0);tree.nodes[0_111310]=text:组织架构;method:check(111310,0);tree.nodes[0_111318]=text:工程项目;method:check(111318,0);tree.nodes[0_111313]=text:资金活动;method:check(111313,0);tree.nodes[0_111315]=text:资产管理;method:check(111315,0);tree.nodes[0_111320]=text:财务报告;method:check(111320,0);tree.nodes[0_113107]=text:采购与付款;method:check(113107,0);tree.nodes[0_128195]=text:审计教学;method:check(128195,0);tree.nodes[0_111322]=text:合同管理;method:check(111322,0);tree.nodes[0_129854]=text:安全管理;method:check(129854,0);tree.nodes[0_111314]=text:采购管理;method:check(111314,0);tree.nodes[0_137225]=text:固定置产管理;method:check(137225,0);tree.nodes[0_115794]=text:设计管理;method:check(115794,0);tree.nodes[-1_0]=text:业务创建;method:check(0,-1);tree.nodes[111310_111326]=text:组织架构管理;method:check(111326,111310);tree.nodes[111311_111327]=text:员工招聘与退出;method:check(111327,111311);tree.nodes[111311_111328]=text:员工薪酬管理;method:check(111328,111311);tree.nodes[111312_111329]=text:安全管理计划;method:check(111329,111312);tree.nodes[111312_111330]=text:安全事故管理;method:check(111330,111312);tree.nodes[111313_111331]=text:筹资活动管理;method:check(111331,111313);tree.nodes[111313_111332]=text:投资活动管理;method:check(111332,111313);tree.nodes[111313_111333]=text:营运资金管理;method:check(111333,111313);tree.nodes[111314_111334]=text:采购业务管理;method:check(111334,111314);tree.nodes[111315_111335]=text:固定资产管理;method:check(111335,111315);tree.nodes[111315_111336]=text:无形资产管理;method:check(111336,111315);tree.nodes[111316_111338]=text:投标管理;method:check(111338,111316);tree.nodes[111316_111339]=text:销售物流管理;method:check(111339,111316);tree.nodes[111316_111340]=text:国际业务;method:check(111340,111316);tree.nodes[111316_111341]=text:客户关系管理;method:check(111341,111316);tree.nodes[111317_111343]=text:新产品研制开发;method:check(111343,111317);tree.nodes[111318_111344]=text:工程项目管理;method:check(111344,111318);tree.nodes[111319_111345]=text:生产管理;method:check(111345,111319);tree.nodes[111320_111346]=text:报告编制管理;method:check(111346,111320);tree.nodes[111321_111347]=text:预算编制管理;method:check(111347,111321);tree.nodes[111321_111348]=text:预算执行管理;method:check(111348,111321);tree.nodes[111322_111349]=text:合同管理;method:check(111349,111322);tree.nodes[111323_111350]=text:系统运维;method:check(111350,111323);tree.nodes[111324_111351]=text:内部控制;method:check(111351,111324);tree.nodes[111324_111352]=text:内部审计;method:check(111352,111324);tree.nodes[111325_111353]=text:产品质量;method:check(111353,111325);tree.nodes[111316_111425]=text:销售预测管理;method:check(111425,111316);tree.nodes[111314_113153]=text:岗位及权限;method:check(113153,111314);tree.nodes[111314_113155]=text:供货商管理;method:check(113155,111314);tree.nodes[115794_115795]=text:初步设计;method:check(115795,115794);tree.nodes[128195_128197]=text:组织管理;method:check(128197,128195);tree.nodes[128195_128198]=text:行政管理;method:check(128198,128195);tree.nodes[128195_129820]=text:安全管理;method:check(129820,128195);tree.nodes[111312_129825]=text:安全生产管理;method:check(129825,111312);tree.nodes[111312_129859]=text:风险点;method:check(129859,111312);tree.nodes[129854_129862]=text:安全管理;method:check(129862,129854);tree.nodes[129854_129866]=text:安全管理流程;method:check(129866,129854);tree.nodes[137225_137226]=text:固定资产取得;method:check(137226,137225);tree.nodes[0_699759]=text:地质勘探;method:check(699759,0);tree.nodes[0_715148]=text:内部监管;method:check(715148,0);tree.nodes[111323_733781]=text:信息运维;method:check(733781,111323);tree.nodes[111321_736140]=text:决算管理;method:check(736140,111321);tree.nodes[0_775638]=text:表单添加;method:check(775638,0);tree.nodes[775638_775639]=text:表单添加003;method:check(775639,775638);'
        // const list = source.split(';').filter((item) => item.indexOf('tree.nodes') > -1)
        // 源数据格式
        // 20: "tree.nodes[0_115794]=text:设计管理"
        // 21: "tree.nodes[-1_0]=text:业务创建"
        // 22: "tree.nodes[111310_111326]=text:组织架构管理"
        const flat = list.map((item) => {
          let res = item.match(/\[(.+?)\]/g)
          let re = res[0]
          let a = re.substring(1, re.length - 1)
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
            label: '业务创建',
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
            if (item.pid === pitem.id) {
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
        this.loading = true
        const res = await getBusinessTree()
        this.loading = false
        const filteredList = res.data.tree
          .split(';')
          .filter((item) => item.indexOf('tree.nodes') > -1)
        this.data = this.formatTree(filteredList)
        this.$nextTick(() => {
          this.$refs.tree.setCurrentKey(this.data[0].id) // 默认选中节点第一个
          this.currentNode = this.data[0]
        })
      },
      handleNodeClick(data, node) {
        this.currentNode = node
        this.$emit('node-change', data)
      },
    },
  }
</script>
<style scoped>
  .top-action {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
  }
</style>
