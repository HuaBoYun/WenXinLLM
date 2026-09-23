<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      width="500px"
      @close="close"
      :close-on-click-modal="false"
    >
      <el-select
        v-model="moduletype"
        placeholder="请选择模块"
        @change="changeModuleType"
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
      <el-tree
        ref="tree"
        v-loading="loading"
        :data="data"
        :default-checked-keys="checkedKeys"
        :default-expanded-keys="expandedKeys"
        :expand-on-click-node="false"
        :highlight-current="true"
        node-key="id"
        :props="defaultProps"
        show-checkbox
        @node-click="handleNodeClick"
      />
      <template #footer>
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="save">授 权</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
  import { getCompanyRightList, grantCompanyRight } from '@/api/setting/org'
  import { getModuleList } from '@/api/setting/system'
  export default {
    name: 'MenuList',
    data() {
      return {
        title: '选择权限',
        dialogFormVisible: false,
        defaultProps: {
          children: 'children',
          label: 'label',
        },
        orgid: '',
        checklist: undefined,
        data: [],
        loading: false,
        checkedKeys: [],
        expandedKeys: [],
        moduletype: 'xtsz',
        moduleLists: [],
      }
    },
    created() {
      this.moduleList()
    },
    methods: {
      handleNodeClick(data) {
        console.log(data)
        this.checklist = data
      },
      // 注意：半选状态后端的checked为true，这里需要筛选出最里层子级里checked为true的item设置为默认选中值
      getLeafCheckedKeys(data) {
        let arr = []
        const acc = (data) => {
          data.forEach((i) => {
            if (i.children && i.children.length) {
              acc(i.children)
            } else {
              if (i.checked) {
                arr = arr.concat(i.id)
              }
            }
          })
          return arr
        }
        return acc(data)
      },
      getAllKeys(data, key) {
        let arr = []
        data.forEach((i) => { 
          if (i[key]) {
            arr.push(i.id)
          }
          if (i.children && i.children.length) {
            arr = arr.concat(this.getAllKeys(i.children, key))
          }
        })
        return arr
      },
      showEdit(orgid) {
        this.dialogFormVisible = true
        this.loading = true
        getCompanyRightList({ orgId: orgid, moduleType: this.moduletype })
          .then((res) => {
            this.data = res.data.rightList
            this.checkedKeys = this.getAllKeys(this.data, 'isChecked')
            // this.expandedKeys = this.getAllKeys(this.data, 'isChecked')
            console.log(this.checkedKeys)
          })
          .finally(() => {
            this.loading = false
          })
        this.orgid = orgid
        this.defaultProps.label = 'name'
      },
      changeModuleType(type) {
        this.moduletype = type
        this.loading = true
        getCompanyRightList({ orgId: this.orgid, moduleType: this.moduletype })
          .then((res) => {
            this.data = res.data.rightList
            this.checkedKeys = this.getAllKeys(this.data, 'isChecked')
            // this.expandedKeys = this.getAllKeys(this.data, 'isChecked')
          })
          .finally(() => {
            this.loading = false
          })
        this.orgid = orgid
        this.defaultProps.label = 'name'
      },
      close() {
        this.dialogFormVisible = false
      },
      async save() {
        const pri_id = this.$refs['tree'].getCheckedKeys().join(',')
        const { msg } = await grantCompanyRight({
          rightIds: pri_id,
          orgId: this.orgid,
          moduleType: this.moduletype
        })
        this.$baseMessage(msg, 'success', 'vab-hey-message-success')
        this.$emit('fetch-data')
        // this.close()
      },
      moduleList() {
        getModuleList({}).then((res) => {
          this.moduleLists = res.data
        })
      },
    },
  }
</script>
